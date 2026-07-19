# 🧪 Recette — application Java de test

Petite application Java autonome (pas un module du plugin, ni de la réactor Maven) qui sert de
fixture de non-régression pour valider `track-logger-method` sur une vraie instance SonarQube,
après un changement touchant la détection de framework ou la publication des métriques.

Elle couvre volontairement plusieurs cas :

| Classe | Framework | error | warn | info | debug | trace | Total |
| --- | --- | --- | --- | --- | --- | --- | --- |
| `Slf4jService` | SLF4J | 2 | 1 | 3 | 1 | 1 | 8 |
| `LombokSlf4jService` | Lombok @Slf4j | 1 | 2 | 1 | 3 | 1 | 8 |
| `Log4j2Service` | Log4j2 | 1 | 1 | 2 | 1 | 2 | 7 |
| `JulService` | JUL | — | — | 2 | — | — | 2 |
| `UnknownFallbackService` | Unknown (logger maison) | 1 | 1 | 1 | — | — | 3 |

**Grand total attendu : 28**, ratios SLF4J 28.6 %, Lombok @Slf4j 28.6 %, Log4j2 25.0 %, JUL 7.1 %,
Unknown 10.7 %.

> [!IMPORTANT]
> `JulService` n'utilise que `.info()` : `java.util.logging.Logger` n'a pas de méthode nommée
> `error()`/`warn()`/`debug()`/`trace()` (il a `severe()`, `warning()`, `fine()`/`finer()`/`finest()`
> à la place), donc ce sont les seuls niveaux réellement détectables pour ce framework avec la
> logique de détection actuelle, basée sur le nom de méthode.

_
> [!WARNING]
> Les compteurs `*_trace_count` ne remonteront qu'à condition que la règle `track-trace-method`
> soit **active dans le profil qualité Java réellement assigné au projet analysé**. Le profil
> intégré `Ma-Moulinette` l'active par défaut, mais si votre instance utilise un profil Java
> personnalisé (modifié à la main), SonarQube ne synchronise pas automatiquement les nouvelles
> règles d'un plugin dans un profil non "vierge" — il faut l'activer manuellement dans ce cas.
> Voir [guide développeur](../documentation/DEVELOPER_GUIDE.md#-dépannage).

## Utilisation

1. Compiler et déployer le plugin (voir [guide développeur](../documentation/DEVELOPER_GUIDE.md#-compilation)),
   redémarrer SonarQube.
2. Depuis ce dossier :

   ```bash
   mvn sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.token=<votre-token>
   ```

3. Comparer les métriques `logger_*` du projet `tlm-test-app` (dashboard ou
   [API Measures](../documentation/USER_GUIDE.md#-récupérer-les-métriques-via-lapi)) aux valeurs attendues
   ci-dessus.

> [!NOTE]
> Ne jamais committer de token SonarQube dans ce dépôt — passez-le uniquement en ligne de
> commande (`-Dsonar.token=...`) ou via la variable d'environnement `SONAR_TOKEN`.
