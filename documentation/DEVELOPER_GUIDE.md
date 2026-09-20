# 🛠️ Guide développeur

Ce guide s'adresse aux personnes qui **compilent, maintiennent ou étendent** le plugin `track-logger-method`.
Pour l'installation et l'exploitation côté SonarQube, voir le [guide utilisateur](USER_GUIDE.md).

## Sommaire

- [Prérequis](#-prérequis)
- [Compilation](#-compilation)
- [Tests](#-tests)
- [Recette (tests d'acceptation)](#-recette-tests-dacceptation)
- [Architecture technique](#️-architecture-technique)
- [Stratégie de compatibilité multi-versions](#-stratégie-de-compatibilité-multi-versions)
- [Ajouter une nouvelle règle](#-ajouter-une-nouvelle-règle)
- [Dépannage](#-dépannage)

## 📋 Prérequis

- JDK 17 pour développer et compiler (JDK 11 pour la branche legacy 1.x) ;
- une version de `sonar-plugin-api` compatible avec la version de SonarQube ciblée ;
- la version de l'extension `sonar-java` correspondante.

> [!NOTE]
> Le build ne dépend d'aucun mirroir Nexus interne : toutes les dépendances du projet sont publiques (Maven Central).
> `.mvn/maven.config` + `.mvn/settings.xml` surchargent
> automatiquement les *global settings* Maven (`-gs`) pour toute commande lancée depuis ce dépôt — inutile de modifier ou commenter votre `settings.xml` d'installation Maven.

## 🔨 Compilation

```bash
mvn clean install
```

Une compilation réussie affiche notamment le manifeste généré par
`sonar-packaging-maven-plugin` :

```plaintext
[INFO] --- sonar-packaging-maven-plugin:1.23.0.740:sonar-plugin ---
[INFO] Plugin definition in update center
[INFO]     Key: trackloggermethod
[INFO]     Name: Track Logger Method
[INFO]     Version: 2.0.0-RELEASE
[INFO]     Entry-point Class: fr.ma.moulinette.java.MyJavaPlugin
[INFO]     Required Plugins: java:8.2.0.36672
[INFO]     Minimal Sonar Plugin API Version: 10.7.0.96327
[INFO]     Minimal JRE Specification Version: 11
[INFO]     Languages: java
[INFO] BUILD SUCCESS
```

> [!TIP]
> `Required Plugins` et `Minimal Sonar Plugin API Version` reflètent le **plancher** (`sonarjava.minVersion` / `sonar.plugin.api.minVersion`), pas la version de compilation — voir [Stratégie de compatibilité multi-versions](#-stratégie-de-compatibilité-multi-versions).

Le jar final se trouve dans `target/track-logger-method-2.0.0-RELEASE.jar`.

## 🧪 Tests

```bash
mvn test
```

Comme recommandé par SonarSource, lancez toujours les tests avant de compiler le jar final. Une exécution réussie se termine par `Tests run: 14, Failures: 0, Errors: 0, Skipped: 0` suivi de `BUILD SUCCESS`.

## 🧑‍🔬 Recette (tests d'acceptation)

Les tests unitaires (`mvn test`) valident le code en isolation, mais pas le comportement réel du plugin déployé dans SonarQube.

Le dossier [`recette/`](../recette) contient une petite application
Java autonome (pas un module Maven du plugin) couvrant plusieurs frameworks et niveaux de log, avec un tableau des compteurs et ratios attendus — voir [`recette/README.md`](../recette/README.md).

À utiliser après tout changement touchant la détection de framework, le comptage ou la publication des métriques :

1. `mvn clean package`, déployer le jar (voir [Compilation](#-compilation) et
   [Déploiement](USER_GUIDE.md#-déploiement)), redémarrer SonarQube.
2. Depuis `recette/` : `mvn sonar:sonar -Dsonar.host.url=<url> -Dsonar.token=<token>`.
3. Comparer les métriques `logger_*` obtenues aux valeurs attendues documentées dans
   `recette/README.md`.

## 🏗️ Architecture technique

### Flux de données

```plaintext
Fichier Java analysé
       │
       ▼
AbstractTrackLoggerMethodCheck (AST)
  ├── résolution sémantique                  →  receiver.symbolType().fullyQualifiedName()
  ├── annotation Lombok                      →  @Slf4j, @Log4j2, …
  └── fallback variable                      →  LOGGER, LOG, logger, log
       │
       ▼
LoggerCounter.increment(framework, level)    ← statique, thread-safe
       │
       ▼
LoggerMetricsSensor.execute()                ← idempotent (LoggerCounter.markPublished())
  ├── totaux par framework     logger_<framework>_count
  ├── compteurs par niveau     logger_<framework>_<level>_count
  ├── ratios                   logger_<framework>_ratio  (%)
  └── grand total              logger_total_count
       │
       ▼
Dashboard SonarQube — domaine "Logger"
```

### Classes clés

| Classe | Rôle |
| --- | --- |
| `AbstractTrackLoggerMethodCheck` | Base commune des 5 checks AST — résout le framework et incrémente `LoggerCounter` |
| `LoggerFramework` | Enum des frameworks supportés, résolution par FQN et par annotation Lombok |
| `LoggerCounter` | Registre statique thread-safe (`AtomicInteger`) partagé entre checks et sensor, `@ScannerSide` |
| `LoggerMetrics` | Déclaration de toutes les métriques custom du domaine `Logger` |
| `LoggerMetricsSensor` | Sensor post-analyse qui lit `LoggerCounter` et publie les métriques, une seule fois par analyse |
| `RulesList` | Source unique de vérité des checks enregistrés — alimente `MyJavaFileCheckRegistrar` et `MyJavaQualityProfile` |

> [!WARNING]
>
> `SensorContext.allIssues()` et `context.module()` ont été retirés de l'API SonarQube (9.x et > 10.x respectivement).
> Ne pas réintroduire de code qui en dépend — l'accumulation passe exclusivement par `LoggerCounter`, et les mesures se sauvegardent sur `context.project()`.

## 🔄 Stratégie de compatibilité multi-versions

**Objectif : un seul jar par version majeure du plugin, compatible avec plusieurs versions SonarQube, sans refactorer le code à chaque nouvelle version SonarQube.**

Le `pom.xml` sépare volontairement deux catégories de propriétés :

| Catégorie | Propriétés | Rôle | Fréquence de mise à jour |
| --- | --- | --- | --- |
| **Compilation/test** | `sonar.plugin.api.version`, `sonarjava.version` | Version utilisée pour compiler et lancer les tests unitaires | À chaque campagne de validation contre une nouvelle LTS |
| **Plancher (manifeste)** | `sonar.plugin.api.minVersion`, `sonarjava.minVersion` | Version minimale déclarée dans le manifeste (`pluginApiMinVersion`, `requirePlugins`) — détermine sur quels serveurs le jar peut s'installer | Uniquement en cas de rupture volontaire du support d'anciennes versions |

L'API `sonar-plugin-api` et l'extension `sonar-java` sont conçues pour être rétro-compatibles :
un jar compilé contre la dernière LTS continue de s'installer sur des serveurs plus anciens tant que le plancher déclaré dans le manifeste reste bas.

> [!IMPORTANT]
>
> Avant ce découplage, `requirePlugins` pointait directement sur `sonarjava.version` : chaque campagne de test contre une nouvelle LTS relevait automatiquement le plancher requis pour **tous** les utilisateurs, cassant la compatibilité avec les anciennes versions SonarQube.
>
> C'est ce qui obligeait à publier un jar différent par version SonarQube.
>
> Avec le découplage, un seul jar `2.0.0` couvre SonarQube 10.7, 24.x, 25.x et 26.x.

### Règle en 2 étapes, à chaque campagne de test contre la dernière LTS de l'année

1. **Version SonarQube cible → `sonar.plugin.api.version`** : consulter la matrice de compatibilité SonarSource (tableau ci-dessous).
2. **Version SonarQube cible → `sonarjava.version`** : inspecter directement `lib/extensions/sonar-java-plugin-*.jar` sur un serveur réel de cette version — c'est la méthode la plus fiable, les numéros de version ne suivent pas la même numérotation que SonarQube.

> [!CAUTION]
>
> Le plancher (`*.minVersion`) ne doit être relevé **que** si le code utilise volontairement une API absente des anciennes versions supportées — jamais automatiquement à chaque bump de  compilation.

### Matrice de compatibilité SonarQube ↔ Plugin API

| SonarQube | Plugin API |
| --- | --- |
| 26.7.0.124771 | 13.8.0.4399 |
| 26.6.0.123539 | 13.7.0.4381 |
| 26.5.0.122743 | 13.5.0.4319 |
| 26.4.0.121862 | 13.5.0.4319 |
| 26.3.0.120487 | 13.5.0.4319 |
| 26.2.0.119303 | 13.4.3.4290 |
| 26.1.0.118079 | 13.4.3.4290 |
| 25.12.0.117093 | 13.4.2.4284 |
| 25.11.0.114957 | 13.3.0.3209 |
| 25.10.0.114319 | 13.2.0.3137 |
| 25.8.0.111898 | 13.0.0.3026 |
| 25.7.0.110598 | 13.0.0.3026 |
| 25.5.0.107428 | 11.3.0.2824 |
| 25.4.0.105899 | 11.3.0.2824 |
| 25.3.0.104237 | 11.2.0.2797 |
| 25.2.0.102705 | 11.1.0.2693 |
| 25.1.0.102122 | 11.1.0.2693 |

Sources :

- <https://github.com/SonarSource/sonarqube/releases> et
- <https://github.com/SonarSource/sonar-plugin-api>.

### Branche legacy 1.x (SonarQube 9.9 LTS)

Non maintenue activement, conservée pour référence :

```xml
<sonarqube.version>9.9.4.87374</sonarqube.version>
<sonar.plugin.api.version>9.17.0.587</sonar.plugin.api.version>
<sonarjava.version>7.16.0.30901</sonarjava.version>
```

`Note` : pour SonarQube 9.9 LTS, il faut au minimum `sonar.plugin.api.version` `9.14.0.375`.

## ➕ Ajouter une nouvelle règle

Les 5 règles `track-*-method` suivent toutes le même patron.

Pour en ajouter une :

1. **Classe de check** — `src/main/java/.../checks/TrackXxxMethodCheck.java`, extends
   `AbstractTrackLoggerMethodCheck`, implémente `getMethodName()` et `getLoggerMessage()`.
2. **Métadonnées de règle** — `src/main/resources/org/sonar/l10n/java/rules/java/track-xxx-method.json` et `.html`, en miroir d'une règle existante (`type: CODE_SMELL`, `defaultSeverity: Info`).
3. **Enregistrement** — ajouter la classe dans `RulesList.getJavaChecks()`. C'est la seule source de vérité : `MyJavaFileCheckRegistrar` (enregistrement des checks) et
   `MyJavaQualityProfile` (activation dans le profil `Ma-Moulinette`) en dépendent tous les deux automatiquement, rien d'autre à modifier côté registration.
4. **Fixture de test** — `src/test/files/TrackXxxMethodCheck.java`, avec un commentaire `// Noncompliant {{message attendu}}` sur la ligne détectée.
5. **Test du check** — ajouter un cas dans `TrackLoggerMethodCheckTest` (`CheckVerifier`).
6. **Tests des extensions** — `MyJavaRulesDefinitionTest` (assertions par règle : nom, type, fonction de remédiation) et `MyJavaQualityProfileTest` (`verify(newProfile).activateRule(...)`) codent en dur la liste des règles : à mettre à jour manuellement.

## 🩺 Dépannage

> [!WARNING]
>
> **`sonar-analyzer-commons-X.jar, track-logger-method-X.jar define 1 overlapping resource:
> META-INF/MANIFEST.MF`** (warning `maven-shade-plugin`)
>
> Symptôme historique déjà corrigé dans ce dépôt (filtre dans la config du
> `maven-shade-plugin`).
> Si vous embarquez une nouvelle dépendance en scope `compile` et que le
> warning réapparaît, ajoutez un `<filter>` excluant `META-INF/MANIFEST.MF` pour cette
> dépendance — inoffensif mais bruyant, pas la peine de le laisser traîner dans les logs de build.

_
> [!WARNING]
>
> **`Can not add the same measure twice on [key=...]`** au moment de l'analyse
>
> Symptôme historique déjà corrigé (`LoggerMetricsSensor` est maintenant idempotent via
> `LoggerCounter.markPublished()`). Si vous le revoyez, la cause la plus probable est un jar de plugin dupliqué dans `extensions/plugins/` du serveur SonarQube (ancienne version laissée à côté de la nouvelle) — voir le [guide utilisateur](USER_GUIDE.md#-déploiement).

_
> [!WARNING]
>
> **`invalid flag: --release`** à la compilation
>
> Le `JAVA_HOME` actif pointe vers un JDK antérieur à 9 (le flag `--release` du compilateur
> n'existe qu'à partir de JDK 9).
> Ce projet nécessite JDK 17 — vérifiez `java -version` et
> `JAVA_HOME`.

_
> [!WARNING]
>
> **Une règle nouvellement ajoutée (ex. `track-trace-method`) ne remonte jamais d'issue ni de compteur**, alors que le plugin se déploie et démarre sans erreur.
>
> Vérifiez le profil qualité **réellement assigné au projet analysé**
> (`GET api/qualityprofiles/search?project=<clé-projet>`), pas seulement le profil intégré `Ma-Moulinette`.
> SonarQube ne synchronise automatiquement les nouvelles règles d'un plugin que dans les profils intégrés non modifiés — un profil Java personnalisé (édité à la main dans l'UI, `isBuiltIn: false` ou `userUpdatedAt` renseigné) n'hérite pas automatiquement des règles ajoutées après sa dernière édition.
>
> Il faut soit activer la règle manuellement dans ce profil, soit assigner `Ma-Moulinette` au projet.

Pour les erreurs de déploiement côté serveur SonarQube (plugin ignoré, manifeste invalide), voir le [guide utilisateur](USER_GUIDE.md#erreurs-fréquentes-au-déploiement).
