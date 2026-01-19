
-------
Backend de l'application [e-tsako](https://e-tsako.vercel.app/). Fournit les API et la logique serveur pour gérer les utilisateurs, les ressources, l'authentification et les opérations métiers de l'application.

Objectifs (buts)
-----------------
- Fournir une API RESTful sécurisée pour l'application [e-tsako](https://e-tsako.vercel.app/).
- Gérer l'authentification et l'autorisation des utilisateurs.
- Offrir des endpoints CRUD pour les entités métiers (ex. utilisateurs, commandes — adapter selon le domaine).
- Assurer la persistance des données via une base de données relationnelle.
- Permettre un déploiement simple (Docker / cloud).

Principales fonctionnalités
-------------------------------------
- Authentification (JWT / sessions)
- Gestion des utilisateurs (inscription, connexion, profil)
- Endpoints CRUD pour les ressources principales
- Pagination, tri et filtres pour listes
- Validation côté serveur et gestion des erreurs
- Logs et métriques basiques

Stack technique
---------------------------
- Langage : Java
- Framework web : Spring Boot
- Build : Gradle
- Base de données : PostgreSQL
- API : REST (JSON)
- Conteneurisation : Docker 

Prérequis
---------
- Java JDK 22+ | Gradle
- Base de données (Postgres)
- Docker

Exemples d'API
--------------------------
- POST /api/auth/register
- POST /api/auth/login
  - réponse: { "token": "JWT..." }
- GET /api/users/me
  - header: Authorization: Bearer <token>

Base de données et migrations
-----------------------------
- Flyway (migrations)

Sécurité
--------
- Utilisation de spring boot sécurity
- Les secrets (JWT, DB password) hors du code source
- HTTPS en production. (vercel)
- Les rôles/permissions pour les opérations sensibles.

Contact
-------
Pour questions ou contributions > [Me contacter](https://ranto-io.vercel.app/#contact)

Auteur
------

> RAFALIMANANA Ranto Handraina
