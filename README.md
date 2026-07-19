# 🪵 Track Logger Method

Plugin SonarQube qui identifie le framework de logging utilisé dans du code Java (`SLF4J`,`Lombok @Slf4j`, `Log4j2`, `Lombok @Log4j2`, `Log4j1`, `JUL`, `Logback`, `Commons Logging`,`JBoss Logging`) pour chaque appel `logger.error/warn/info/debug/trace()`, et publie des métriques par framework et par niveau dans le dashboard SonarQube.

Développé dans le cadre du projet **Ma-Moulinette**.

## 📚 Documentation

| Public | Contenu | Lien |
| --- | --- | --- |
| **Utilisateurs SonarQube** | Installer le plugin, règles, profil qualité, métriques, API | [docs/USER_GUIDE.md](documentation/USER_GUIDE.md) |
| **Développeurs** | Architecture, stratégie de compatibilité multi-versions, build, tests, ajouter une règle | [docs/DEVELOPER_GUIDE.md](documentation/DEVELOPER_GUIDE.md) |
| **Tout le monde** | Historique des versions | [CHANGELOG.md](CHANGELOG.md) |

## 🚀 Démarrage rapide

```bash
mvn clean install
```

Copier `target/track-logger-method-2.0.0-RELEASE.jar` dans `extensions/plugins/` de votre serveur SonarQube, puis redémarrer. Détails complets : [guide utilisateur](documentation/USER_GUIDE.md#-déploiement).

> [!NOTE]
> Le build ne nécessite **aucun accès à un miroir Nexus** — toutes les dépendances sont publiques (Maven Central).
> Voir [guide développeur](documentation/DEVELOPER_GUIDE.md#-compilation).

## ✅ Version supportée

| Version plugin | SonarQube | Java | Statut |
| --- | --- | --- | --- |
| 2.0.0-RELEASE | 10.7+ LTS (10.x, 24.x, 25.x, 26.x) | 17 | ✅ Actuel |
| 1.0.0-RELEASE | 9.9 LTS | 11 | ⚠️ Legacy |

> [!WARNING]
> Les versions SonarQube 8.9 LTS et antérieures ne sont plus supportées depuis la 2.0.0.

## 📜 Licence

Ce projet est basé sur le projet **Custom Rules** pour SonarJava de SonarSource :
<https://github.com/SonarSource/sonar-java/blob/master/docs/CUSTOM_RULES_101.md>

La licence [GNU LGPL 3.0](https://www.gnu.org/licenses/lgpl.txt) de ce dépôt ne s'applique pas
au dossier `java-custom-rules-example` : son contenu est sous licence
[MIT No Attribution](LICENSE.txt).

## 💬 Notes personnelles

Je ne suis pas développeur Java, et le développement de ce plugin a été un défi personnel — à plusieurs reprises j'ai été tenté d'abandonner tant les difficultés étaient nombreuses. Le système d'extensions de SonarQube n'est pas simple d'accès : documentation parfois sommaire, et forte adhérence à la version de SonarQube et à l'extension Java utilisée.

Ça explique pourquoi beaucoup d'extensions tierces finissent abandonnées par leurs mainteneurs — et c'est pour ça que ce dépôt documente maintenant explicitement sa stratégie de compatibilité multi-versions dans le [guide développeur](documentation/DEVELOPER_GUIDE.md#-stratégie-de-compatibilité-multi-versions).
