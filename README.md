## Nom et Prénom : Anejjar Wiame
## Filière: MSDIA

---
# Rapport de TP – Application de Gestion Hospitalière avec Spring Boot, Spring Security et Thymeleaf

## 📌 Objectif du TP

L'objectif de ce TP est de développer une application web complète de gestion hospitalière en utilisant Spring Boot comme framework principal, Spring MVC pour l'architecture web,
Thymeleaf comme moteur de templates,Spring Data JPA pour la persistance des données,Spring Security pour la gestion de l'authentification et des autorisations.
Cette application permet de modéliser les principales entités d’un système hospitalier, telles que les patients, les médecins, les rendez-vous, les consultations, et leur gestion au travers de services et d’un contrôleur REST.

L'application doit permettre de :
 - Gérer les patients (CRUD complet).
 - Implémenter une pagination des résultats.
 - Ajouter des fonctionnalités de recherche.
 - Sécuriser l'accès aux différentes fonctionnalités.
 - Utiliser un système de templates pour une interface cohérente.
 - Valider les données des formulaires.

---

## 🧱 Structure du Projet

Le projet suit une architecture MVC (Modèle-Vue-Contrôleur) typique d'une application Spring Boot, il contient les packages suivants :
 - entities : contient les classes de domaine représentant les entités métier : Classe Patient.
 - repositories : contient les interfaces JPA permettant l’accès aux données :
    - Interface PatientRepository: Fournit des méthodes CRUD automatiques et la recherche paginée.
 - security :Gère l'authentification et l'autorisation via Spring Security, incluant la modélisation des utilisateurs/rôles, la configuration de sécurité et les contrôleurs dédiés.
   Il contient les packages:
    - Entités qui contient les classes AppRole pour définir les rôles d'accès et AppUser pour modéliser un utilisateur avec ses credentials et rôles associés.
    - Répo qui contient les interfaces AppRoleRepository / AppUserRepository pour persister et rechercher rôles/utilisateurs en base.
    - Service qui contient l'interface AccountService qui définit les contrats pour la gestion des utilisateurs et rôles, l'implémentation AccountServiceImpl qui implémente les règles métier (validation des mots de passe, gestion des transactions avec @Transactional), ainsi l'implémentation UserDetailServiceImpl pour adapter le modèle AppUser à Spring Security en implémentant UserDetailsService pour l'authentification.
    - La classe SecurityConfig pour configurer les règles d'accès et l'authentification (ex: routes protégées).
 - web : Contient les contrôleurs MVC :
     - Classe PatientController: Gère l'affichage et la recherche des patients.
     - Classe SecurityController: Gère les vues liées à l'authentification.
 - HospitalApplication : Point d'entrée de l'application avec configuration automatique.
 - templates: Contient les vues Thymeleaf pour l'interface utilisateur, structurées avec des fragments réutilisables et des formulaires liés aux entités.Il contient les fichiers suivants:
     - template1.html : Template de base avec navbar et layout commun à toutes les pages.
     - patients.html : Affiche la liste paginée des patients avec recherche et actions (éditer/supprimer).
     - formPatients.html : Formulaire de création d'un patient avec validation.
     - editPatients.html : Vue spécifique pour modifier un patient existant.
     - login.html : Page d'authentification avec formulaire de connexion.
     - notAuthorized.html : Message d'erreur pour les accès non autorisés.
  - application.properties : Paramètres de l'application (BDD, sécurité, etc.).
  - schema.sql : Script SQL pour initialiser la structure de la base de données.
  
  ![img](structure1.JPG)  
  ![img](structure2.JPG)  

 ---
## 📄 Explication détaillée 
---
 ## 🗂 Package entities
### - Classe Patient :
La classe Patient est une entité JPA qui modélise un patient dans le système hospitalier. Annotée avec @Entity, elle est mappée à une table en base de données c'est à dire que cette classe représente une table dans la base de données, où chaque instance de Patient correspondra à une ligne dans cette table. L'annotation @Id marque le champ id comme clé primaire, tandis que @GeneratedValue(strategy = GenerationType.IDENTITY) permet sa génération automatique. Les contraintes de validation (@NotEmpty, @Size, @DecimalMin) assurent l'intégrité des données c'est à dire que le nom doit être non vide et compris entre 4 et 40 caractères, et le score minimal est fixé à 100.Ainsi, on a utilisé l'annotation @Temporal(TemporalType.DATE) pour préciser que le champ dateNaissance stocke uniquement la date (sans l'heure), et @DateTimeFormat(pattern = "yyyy-MM-dd") standardise son format, ainsi l'attribut malade c'est pour de type boolean pour spécifier l'état de chaque patient.  
Les annotations Lombok (@Data, @NoArgsConstructor, @AllArgsConstructor, @Builder) génèrent automatiquement les getters/setters, constructeurs et un builder. Cette classe sert de fondation pour la persistance et la validation des données patients dans l'application.

  ![img](classPatients.JPG)

 ## 🗂️ Package repositories
### - Interface `PatientRepository` : 
L'interface PatientRepository étend JpaRepository, ce qui lui permet d'hériter automatiquement des opérations CRUD de base sans implémentation manuelle, car Spring Data JPA fournit ces fonctionnalités prêtes à l'emploi. Elle inclut deux méthodes de recherche : findByNomContains, une méthode dérivée où Spring génère automatiquement la requête à partir du nom de la méthode, et chercher c'une méthode personnalisée utilisant l'annotation @Query pour spécifier une requête explicite. Les deux méthodes retournent un objet Page contenant les résultats paginés. Les deux méthodes acceptent un paramètre Pageable pour gérer la pagination et le tri.   
L'annotation @Param lie le paramètre keyword à la variable x dans la requête JPQL, pour garantir une liaison sécurisée des paramètres et éviter les injections SQL. Ainsi, ce repository combine à la fois la simplicité des requêtes générées automatiquement et la flexibilité des requêtes personnalisées pour répondre aux besoins spécifiques de l'application.
 ![img](patientRepo.JPG)

## 🛠️ Package security
### 1.  Package `entities`:
#### - Classe `AppRole`:
La classe AppRole est une entité JPA qui modélise un rôle de sécurité dans l'application. Elle est mappée à une table en base de données en utilisant l'annotation @Entity, tandis que @Id désigne le champ role comme clé primaire. Les annotations Lombok (@Data, @NoArgsConstructor, @AllArgsConstructor, @Builder) génèrent automatiquement les getters/setters, les constructeurs et un builder pour simplifier le code.  
Cette classe permet de gérer les différents rôles (comme "ADMIN" ou "USER") qui seront utilisés pour sécuriser l'accès aux fonctionnalités de l'application via Spring Security. Sa simplicité et son intégration avec JPA en font un composant essentiel pour la gestion des autorisations.

 ![img](approle.JPG)
 
#### - Classe `AppUser`:
Cette classe représente un utilisateur du système et est conçue pour fonctionner avec Spring Security, car elle stocke les informations d'authentification comme le username et le password, ainsi que les rôles associés via une relation ManyToMany avec AppRole. Le champ username est marqué comme unique (@Column(unique = true)), ce qui empêche les doublons en base de données, tandis que la stratégie de chargement FetchType.EAGER garantit que les rôles sont chargés immédiatement avec l'utilisateur, puisque cela est essentiel pour les vérifications d'autorisation.  
L'utilisation de Lombok évite le code redondant, et l'annotation @Builder facilite la création d'objets complexes. Cette entité à pour rôle de lier les identifiants de connexion aux permissions accordées via les rôles.

 ![img](appuser.JPG)

 ### 2.  Package `repo`:
#### - Interface `AppRoleRepository`:
L'interface AppRoleRepository étend JpaRepository, ce qui lui permet d'hériter automatiquement des opérations CRUD standards (Create, Read, Update, Delete) sans nécessiter d'implémentation manuelle, car Spring Data JPA fournit ces fonctionnalités prêtes à l'emploi. Spécialisée pour l'entité AppRole avec une clé primaire de type String, cette interface simplifie la gestion des rôles en base de données, tandis que son intégration native avec Spring Security facilite les vérifications d'autorisation. 

 ![img](repoapprole.JPG)
 
#### - Interface `AppUserRepository`:
Cette interface étend aussi de JpaRepository, pour bénéficier des opérations CRUD de base pour la gestion des utilisateurs, mais elle ajoute également une méthode personnalisée findByUsername() pour rechercher un utilisateur par son identifiant. Cette méthode est automatiquement implémentée par Spring Data JPA grâce à la convention de nommage, évitant ainsi d'écrire du code supplémentaire. 

 ![img](repoappuser.JPG)

  ### 3.  Package `service`:
#### - Interface `AccountService`:
L'interface AccountService définit le contrat pour la gestion des utilisateurs et des rôles dans le système de sécurité. Elle propose des méthodes pour créer un nouvel utilisateur (avec vérification du mot de passe via le paramètre confirmPassword), ajouter/supprimer des rôles, et manipuler les associations entre utilisateurs et rôles. La méthode loadUserByUsername permet de récupérer un utilisateur pour l'authentification, ce qui est crucial pour Spring Security. Cette abstraction offre une séparation claire entre la couche métier et l'implémentation, facilitant ainsi la maintenance tout en centralisant la logique de gestion des comptes. Les paramètres comme email et confirmPassword permettent des validations supplémentaires ce qui montre une approche orientée sécurité.

 ![img](accountserv.JPG)

 #### - Implémentation `AccountServiceImpl`:
L'implémentation AccountServiceImpl implémente AccountService et fournit une gestion complète des utilisateurs et rôles, tout en assurant la sécurité des opérations grâce à l'annotation @Transactional qui garantit l'intégrité des données. Elle utilise AppUserRepository et AppRoleRepository pour persister les informations, ainsi que PasswordEncoder pour hacher les mots de passe, ce qui renforce la sécurité contre les attaques. Les méthodes addNewUser et addNewRole vérifient d'abord l'existence des entités avant de les créer, évitant ainsi les doublons, puis confirme que les mots de passe saisis (password et confirmPassword) correspondent. Si tout est valide, elle utilise le pattern Builder (via Lombok) pour construire l'objet, tandis que addRoleToUser récupère l'utilisateur par son username et le rôle par son ID, puis ajoute le rôle à la liste roles de l'utilisateur , ainsi on a utilisé @Transactional pour la mise à jour automatique, et removeRoleFromUser retire un rôle d'un utilisateur. Similaire à addRoleToUser, mais utilise remove() sur la liste roles.  
La méthode loadUserByUsername récupère un utilisateur par son username via appUserRepository.findByUsername(). Cette méthode est essentielle pour Spring Security, qui l'utilise lors de l'authentification pour charger les détails de l'utilisateur (credentials, rôles, etc.).

 ![img](account1.JPG)
  ![img](account2.JPG)
   ![img](account3.JPG)



  
  
---
 ## - Conclusion
Ce TP a permis de mettre en pratique Spring Boot, Spring Security et Thymeleaf pour développer une application web sécurisée de gestion des patients. Nous avons implémenté un CRUD complet avec pagination, une authentification basée sur les rôles (USER/ADMIN), et une interface responsive grâce à Bootstrap. L’utilisation de fragments Thymeleaf a favorisé la réutilisabilité du code, tandis que Spring Security a garanti la protection des routes sensibles. Ce projet illustre comment concevoir une application robuste, maintenable et sécurisée avec l’écosystème Spring.



---
 
  
