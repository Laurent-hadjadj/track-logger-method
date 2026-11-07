# Changement de licence : motif et portée

Cette note explique le changement de licence de **Track Logger Method**, ce qu'il couvre et ce qu'il ne couvre pas. Le texte qui fait foi est celui du fichier [`LICENSE`](LICENSE).

## En bref

| Élément | Licence | À partir de |
| --- | --- | --- |
| Code source | **GNU Lesser General Public License, version 3 ou ultérieure (`LGPL-3.0-or-later`)** | 4 novembre 2026 |
| Documentation (`README.md`, le dossier `documentation/`) | [CC BY-SA 4.0](documentation/LICENSE.md) | 4 novembre 2026 |
| Versions publiées jusqu'à la version 2.0.0-RELEASE incluse | CC BY-NC-SA 4.0 | inchangées |
| Composants tiers | leur propre licence (voir [`NOTICE`](NOTICE)) | inchangée |

## Pourquoi ce changement

Jusqu'ici, le projet était placé sous CC BY-NC-SA 4.0. Cette licence est conçue pour des contenus créatifs et non pour du logiciel, et sa clause « pas d'usage commercial » est incompatible avec la définition de l'open source.

L'objectif est de **garantir que le logiciel reste ouvert** : que chacun puisse l'utiliser, l'étudier, le modifier et le partager, et que personne ne puisse le fermer. C'est ce que fait une licence à copyleft. La licence choisie est la **LGPL 3.0** parce que c'est la licence de l'écosystème SonarQube, que ses utilisateurs acceptent généralement.

## Ce que cela change pour vous

Cette licence est un **copyleft faible** : les modifications apportées à ce plugin lui-même, si vous le diffusez, doivent être publiées sous la même licence, mais vous pouvez l'utiliser librement avec d'autres logiciels, y compris non libres (SonarQube compris).

Vous pouvez :

- utiliser le logiciel, **y compris dans un cadre commercial** ;
- l'étudier, le modifier, le redistribuer ;
- le revendre ou le proposer en service, en respectant la licence.

Vous devez :

- conserver les mentions de copyright et de licence, ainsi que le fichier `NOTICE` ;
- indiquer vos modifications ;
- publier le code source de vos versions diffusées sous la même licence (pour les modifications du plugin lui-même).

## Portée

**Ce qui change.** Les versions publiées à partir du 4 novembre 2026 (tag `licence-lgpl-3.0`) sont sous LGPL 3.0.

**Ce qui ne change pas.**

- Les versions publiées jusqu'à la version 2.0.0-RELEASE incluse restent disponibles sous leur licence d'origine pour qui les a reçues. Une licence déjà accordée est irrévocable.
- L'historique git n'est pas réécrit : les commits antérieurs au 4 novembre 2026 restent attachés à la licence en vigueur lors de leur publication.
- Les composants tiers gardent leur licence (voir `NOTICE`).

**En-têtes des fichiers sources.** Des en-têtes de fichiers antérieurs au 4 novembre 2026 mentionnent encore l'ancienne licence. Ils sont remplacés par la licence du dépôt (LGPL 3.0) : en cas de contradiction, **le fichier `LICENSE` prévaut**. Les en-têtes sont mis à jour au fil des modifications des fichiers.

**Versions dérivées.** Une version dérivée, y compris maintenue par une organisation, reste soumise à cette licence pour la part qui en provient, sauf accord écrit du titulaire des droits.

## Questions fréquentes

- **Puis-je l'utiliser dans mon entreprise, y compris à titre commercial ?** Oui. Si vous ne le modifiez pas, vous n'avez aucune obligation particulière.
- **Puis-je le modifier pour mon usage ?** Oui. Vous n'avez d'obligation que si vous diffusez votre version modifiée à d'autres.
- **Puis-je l'utiliser avec un SonarQube dont la licence n'est pas libre ?** Oui, c'est le but du choix de la LGPL.
- **Puis-je le revendre ou l'héberger en service payant ?** Oui, en respectant la licence. La clause « pas d'usage commercial » de l'ancienne licence n'existe plus.
- **Les anciennes versions changent-elles de licence ?** Non. Une licence déjà accordée ne se retire pas.
- **J'ai un doute sur mon cas.** Ouvrez un ticket sur le dépôt. Ce document est une explication, pas un avis juridique : le texte du fichier `LICENSE` fait foi.

## Documents liés

- [`LICENSE`](LICENSE) : texte de la licence du code (la LGPL renvoie à la GPL, dont le texte est dans [`LICENSES/GPL-3.0.txt`](LICENSES/GPL-3.0.txt)).
- [`NOTICE`](NOTICE) : attributions et composants tiers.
- [`LICENSES/CC-BY-NC-SA-4.0.txt`](LICENSES/CC-BY-NC-SA-4.0.txt) : texte de l'ancienne licence.
- [`documentation/LICENSE.md`](documentation/LICENSE.md) : licence de la documentation.
