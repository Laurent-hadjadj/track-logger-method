# 🪵 Track Logger Method

[![Version](https://img.shields.io/badge/version-2.1.0-blue)](CHANGELOG.md)
[![JAVA](https://img.shields.io/badge/JAVA-%E2%89%A5%2017-777BB4?logo=java&logoColor=white)](https://www.java.com/)
[![SonarQube](https://img.shields.io/badge/26.7.0.124771-000000?logo=sonar&logoColor=white)](https://www.sonarsource.com/products/sonarqube/downloads/)
[![JAVA-PLUGIN](https://img.shields.io/badge/8.34.0.44906-000000?logo=sonar&logoColor=white)](https://www.sonarsource.com/products/sonarqube/downloads/)
[![Tests](https://img.shields.io/badge/tests-14%20passed-success?logo=junit&logoColor=white)](https://junit.org/)
[![Coverage](https://img.shields.io/badge/coverage-96.00%25-brightgreen)](https://www.jacoco.org/jacoco/)
[![Licence](https://img.shields.io/badge/licence-LGPL%203.0-blue)](LICENSE)

┏┓┏┓┏┓━┏━━┓━┏━━━┓\
┃┃┃┃┃┃━┗┫┣┛━┃┏━┓┃\
┃┃┃┃┃┃━━┃┃━━┃┗━┛┃\
┃┗┛┗┛┃━━┃┃━━┃┏━━┛\
┗┓┏┓┏┛━┏┫┣┓━┃┃\
━┗┛┗┛━━┗━━┛━┗┛  NeXt 2.1.0 Release on November 2026 !

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

Copier `target/track-logger-method-2.1.0-RELEASE.jar` dans `extensions/plugins/` de votre serveur SonarQube, puis redémarrer. Détails complets : [guide utilisateur](documentation/USER_GUIDE.md#-déploiement).

> [!NOTE]
> Le build ne nécessite **aucun accès à un miroir Nexus** — toutes les dépendances sont publiques (Maven Central).
> Voir [guide développeur](documentation/DEVELOPER_GUIDE.md#-compilation).

## ✅ Version supportée

| Version plugin | SonarQube | Java | Statut |
| --- | --- | --- | --- |
| 2.1.0-RELEASE | 10.7+ LTS (10.x, 24.x, 25.x, 26.x) | 17 | ✅ Actuel |
| 2.0.0-RELEASE | 10.7+ LTS (10.x, 24.x, 25.x, 26.x) | 17 | ⚠️ Précédent (CC BY-NC-SA 4.0) |
| 1.0.0-RELEASE | 9.9 LTS | 11 | ⚠️ Legacy |

> [!WARNING]
> Les versions SonarQube 8.9 LTS et antérieures ne sont plus supportées depuis la 2.0.0.

## 📜 Licence

- **Code** : [GNU LGPL 3.0 ou ultérieure](LICENSE) (`LGPL-3.0-or-later`), licence à copyleft : le logiciel reste ouvert.
- **Documentation** (`README.md`, `documentation/`) : [CC BY-SA 4.0](documentation/LICENSE.md).

**Changement de licence.** Depuis le 4 novembre 2026, le code est distribué sous LGPL 3.0. Jusqu'à la version 2.0.0-RELEASE incluse, il était publié sous CC BY-NC-SA 4.0 ([texte conservé](LICENSES/CC-BY-NC-SA-4.0.txt)) ; ces versions restent sous cette licence. L'usage commercial est désormais autorisé, à condition de respecter le copyleft. Les en-têtes des sources ont été mis à jour en 2.1.0. **Motif et portée du changement : [LICENSING.md](LICENSING.md)**. Attributions : [NOTICE](NOTICE).

**Origine.** Ce projet est basé sur le projet **Custom Rules** pour SonarJava de SonarSource :
<https://github.com/SonarSource/sonar-java/blob/master/docs/CUSTOM_RULES_101.md>. L'exemple fourni par SonarSource (`java-custom-rules-example`) est sous licence MIT No Attribution, qui n'impose aucune obligation d'attribution. Le jar embarque `sonar-analyzer-commons` (LGPL 3.0).

## 💬 Notes personnelles

Je ne suis pas développeur Java, et le développement de ce plugin a été un défi personnel — à plusieurs reprises j'ai été tenté d'abandonner tant les difficultés étaient nombreuses. Le système d'extensions de SonarQube n'est pas simple d'accès : documentation parfois sommaire, et forte adhérence à la version de SonarQube et à l'extension Java utilisée.

Ça explique pourquoi beaucoup d'extensions tierces finissent abandonnées par leurs mainteneurs — et c'est pour ça que ce dépôt documente maintenant explicitement sa stratégie de compatibilité multi-versions dans le [guide développeur](documentation/DEVELOPER_GUIDE.md#-stratégie-de-compatibilité-multi-versions).
