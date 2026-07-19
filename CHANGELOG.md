# Changelog

Toutes les modifications notables de ce projet sont documentées dans ce fichier.
Le format est basé sur [Keep a Changelog](https://keepachangelog.com/fr/1.1.0/).

## [2.0.0-RELEASE] - 2026-07-19

### Ajouté en 2.0.0

- **Détection du framework de logging** : identification du framework utilisé pour chaque appel
  logger (`SLF4J`, `Lombok @Slf4j`, `Log4j2`, `Lombok @Log4j2`, `Log4j1`, `JUL`,
  `Logback`, `Commons Logging`, `JBoss Logging`, `Unknown`).
- **`LoggerFramework`** : enum centralisant les frameworks supportés avec leur FQN et leur
  label. Expose `fromType()` et `fromTypeAndAnnotation()` pour la résolution.
- **`LoggerCounter`** : registre statique thread-safe (`AtomicInteger`) accumulant les appels
  détectés par framework et par niveau pendant l'analyse AST. Accessible sans injection
  de dépendance depuis les checks.
- **`LoggerMetrics`** : déclaration de toutes les métriques custom dans le domaine `Logger` :
  - compteurs totaux par framework (`logger_<framework>_count`)
  - compteurs par framework × niveau (`logger_<framework>_<level>_count`)
  - ratios en pourcentage par framework (`logger_<framework>_ratio`)
  - compteur global toutes métriques confondues (`logger_total_count`)
- **`LoggerMetricsSensor`** : sensor post-analyse qui lit le `LoggerCounter` et publie
  toutes les métriques dans le dashboard SonarQube au niveau projet.
- **Détection Lombok** : distinction entre SLF4J natif et `Lombok @Slf4j`, entre Log4j2
  natif et `Lombok @Log4j2`, via inspection des annotations de la classe englobante.
- **Fallback variable conventionnelle** : si la résolution sémantique échoue (classpath
  incomplet), les noms `LOGGER`, `LOG`, `logger`, `log` sont utilisés pour détecter
  l'appel et le classer comme `Unknown`.
- **Règle `track-trace-method`** : 5ᵉ règle, détecte les appels `logger.trace()` — elle était
  documentée depuis la 2.0.0 mais n'avait jamais été réellement implémentée (pas de classe de
  check, pas de métadonnées de règle) ; les métriques `logger_*_trace_count` associées restaient
  donc toujours à 0.
- **`.mvn/maven.config` + `.mvn/settings.xml`** : le build n'a plus besoin du mirroir Nexus
  interne pour se compiler — toutes les dépendances du projet sont
  publiques (Maven Central). Ces fichiers surchargent automatiquement les *global settings* Maven
  (`-gs`) pour toute commande lancée depuis ce dépôt, sans modifier l'installation Maven partagée.
- **`LoggerMetricsSensorTest`** : couvre la publication des ratios à grand-total nul et la
  non-republication des métriques sur un second `execute()`.
- **`docs/USER_GUIDE.md`** et **`docs/DEVELOPER_GUIDE.md`** : documentation scindée par public
  (utilisateurs SonarQube / développeurs), réutilisant les captures d'écran déjà versionnées
  dans `documentation/images/`.
- **`recette/`** : application Java autonome de non-régression (SLF4J, Lombok @Slf4j, Log4j2,
  JUL, framework `Unknown`), avec compteurs et ratios attendus documentés dans
  `recette/README.md`. Validée en conditions réelles contre une instance SonarQube 26.7.0.124771 :
  compteurs et ratios corrects et cohérents entre eux, aucune erreur de mesure dupliquée — confirme
  le fix du bug des ratios.

### Modifié en 2.0.0

- **`README.md`** : réduit à une page d'accueil (présentation, démarrage rapide, liens vers les
  guides et le changelog) ; le contenu détaillé (architecture, stratégie de compatibilité,
  déploiement, API, dépannage) déménage dans `docs/`. Les logs bruts collés en fin de fichier
  (warning de build, erreur de mesure dupliquée) sont supprimés — les deux problèmes sont
  corrigés et documentés dans ce changelog.

- **`AbstractTrackLoggerMethodCheck`** : refonte de la détection. La méthode `isLoggerMethod()`
  (détection par nom de variable uniquement) est remplacée par `detectFramework()` à trois
  niveaux : résolution sémantique → annotation Lombok → nom de variable.
- **`MyJavaPlugin`** : enregistrement des trois nouvelles extensions
  (`LoggerMetrics`, `LoggerCounter`, `LoggerMetricsSensor`).
- **`MyJavaFileCheckRegistrar`** : simplifié, ne nécessite plus d'injection manuelle
  du compteur grâce à l'API statique de `LoggerCounter`.
- **`MyJavaPluginTest`** : mis à jour pour inclure les nouvelles extensions et la version
  d'API SonarQube 10.7 LTS.
- **`pom.xml`** : migration vers SonarQube 10.7 LTS (`sonarjava 8.2.0.36672`),
  Java 17 (`maven-compiler-plugin` aligné sur les properties), suppression du
  conflit `source/target 1.11`.
- **Type des règles `track-*-method`** : passage de `BUG` à `CODE_SMELL` (sévérité `Info`
  inchangée) pour les 5 règles (`track-info-method`, `track-error-method`,
  `track-warn-method`, `track-debug-method`, `track-trace-method`). Ces règles ne détectent pas
  un défaut fonctionnel mais un usage à tracer ; les laisser en `BUG` polluait la note
  *Reliability* du projet au lieu de la note *Maintainability*.
- **`pom.xml`** : découplage des versions de *compilation/test* (`sonar.plugin.api.version`,
  `sonarjava.version` — bumpées à chaque campagne de validation contre la dernière LTS annuelle)
  et des versions *plancher* déclarées dans le manifeste (`sonar.plugin.api.minVersion`,
  `sonarjava.minVersion` — figées au plancher réellement supporté). Auparavant `requirePlugins`
  était directement lié à `sonarjava.version`, ce qui obligeait à relever le plancher requis —
  donc à casser la compatibilité avec les anciennes versions SonarQube — à chaque campagne de
  test. Un seul jar `2.0.0` couvre désormais SonarQube 10.7, 24.x, 25.x et 26.x.
- **`maven-shade-plugin`** : ajout d'un filtre excluant le `META-INF/MANIFEST.MF` embarqué dans
  `sonar-analyzer-commons`, qui entrait en collision avec celui du jar final.
- **Duplication de littéraux `String`** (remontée par SonarQube) : constantes extraites dans
  `LoggerMetrics`, `LoggerMetricsSensor` (niveaux de log, désormais partagés via
  `LoggerCounter.LEVEL_*`) et `MyJavaRulesDefinitionTest` (clés de règle). Dans
  `LoggerFramework`, les FQN ne peuvent pas être partagés entre les arguments des constantes de
  l'enum (interdiction Java, pas seulement une préférence de style) : `fromType()` a été réécrit
  pour rechercher directement dans le champ déjà stocké par chaque constante plutôt que de
  dupliquer une seconde fois chaque FQN dans un switch.
- **Javadoc de `src/main`** : uniformisée en texte brut (suppression des balises HTML `<h2>`,
  `<ul>`, `<strong>`, `{@link}`...) et traduite en français sur toutes les classes, pour matcher
  le style déjà utilisé dans les classes de règles (`TrackDebugMethodCheck` et consorts). Au
  passage, corrigé le Javadoc de package `checks/package-info.java`, qui était un copier-coller
  erroné du package racine.

### Corrigé en 2.0.0

- `context.module()` remplacé par `context.project()` (déprécié en SonarQube 9.x,
  retiré en 10.x).
- `SensorContext.allIssues()` supprimé de l'architecture (retiré de l'API 9.x) ;
  remplacé par l'accumulation directe dans les checks via `LoggerCounter`.
- `LoggerFramework.java` déplacé dans le bon package
  `fr.ma.moulinette.java.checks` (était dans `fr.ma.moulinette.java`).
- **Ratios manquants/incorrects dans le dashboard SonarQube** : `LoggerMetricsSensor.execute()`
  pouvait être invoqué plusieurs fois pour une même analyse (scan multi-module, jar de plugin
  dupliqué dans `extensions/plugins/`...) et tentait à chaque fois de sauvegarder les mêmes
  métriques sur le même composant projet. La première exécution réussissait — souvent avec un
  total encore incomplet, sur lequel `publishRatios()` retournait sans rien publier si le total
  valait 0 — tandis que les exécutions suivantes échouaient silencieusement
  (`Can not add the same measure twice`). Le sensor est maintenant idempotent
  (`LoggerCounter.markPublished()` garantit une seule publication par analyse) et les ratios sont
  systématiquement publiés en cohérence avec les compteurs, même à 0.

---

## [1.3.0-RELEASE] - 2026-04-23

Dans cette version :

- les logger détectés sont : LOGGER et LOG ;
- ajout de javax.annotation.Nonnull pour éviter les problèmes de nullité ;
- Compatible avec la version 9.9.4 de SonarQube.

---

## [1.2.0-RELEASE] - 2024-07-09

Dans cette version :

- un nouveau profil Java est ajouté avec les règles ;
- la javadoc et les commentaires ont été ajoutés ;
- les règles ne sont plus comptées comme des BUG en INFO mais en BUG en INFO pour ne pas affecter le calcul de la dette technique.
- Refactor de la classe MyJavaRulePlugin en MyJavaPluglin.

---

## [1.1.0-RELEASE] - 2024-07-09

Dans cette version :

- chaque méthode a fait l'objet d'une règle.
- une classe abstraite a été ajoutée pour éviter de dupliquer le code.
- pour chaque issue, un message est affiché.
- Code Clean et corrections SonarQube.
- mise à jour des dépendances du pom.xml.
- correction de la remonté des infos jacoco dans SonarQube.
- mise à jour de la documentation.

---

## [1.0.0-RELEASE] - 2024-07-07

- Détection des appels logger Java par niveau : `error`, `warn`, `info`, `debug`.
- Règles SonarQube associées : `track-error-method`, `track-warn-method`,
  `track-info-method`, `track-debug-method`.
- Profil qualité intégré `Ma-Moulinette` avec toutes les règles pré-activées.
- Détection basée sur le nom de variable conventionnel (`LOGGER`, `LOG`).
- Compatible SonarQube 9.9 LTS, Java 11+.
- Première itération : une seule règle générique pour détecter les méthodes `info`, `error`,
  `warn` et `debug`, avec une issue affichant `Utilisation de la méthode : {method}` — remplacée
  en cours de version par les 4 règles dédiées listées ci-dessus.
