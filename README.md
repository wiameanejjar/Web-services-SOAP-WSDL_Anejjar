## Nom et Prénom : Anejjar Wiame
## Filière: MSDIA

---
# Rapport de TP – Services Web SOAP avec JAX-WS

## 📌 Objectif du TP

L'objectif de ce TP est de concevoir et implémenter un service web SOAP basé sur le protocole WSDL en utilisant JAX-WS. Ce service permettra de :  

  - Convertir un montant de l'euro en dirhams (DH).  
  - Consulter un compte bancaire (solde et détails).  
  - Lister les comptes disponibles.

     
Les étapes clés incluent :
 
   - Le déploiement du service via un serveur JAX-WS.
   - L'analyse du WSDL généré.
   - Le test des opérations avec SoapUI.  
   - La création d'un client Java SOAP (génération des stubs à partir du WSDL).  

---

## 🧱 Structure du Projet

Le projet est organisé en deux modules principaux :  
### 1. Serveur SOAP (ws-soap):  

   Contient le package `ws` qui contient les classes suivantes:  
      - `BanqueService.java` : Interface du service web qui contient les méthodes (convertion, getCompte, listComptes).  
      - `Compte.java `: Classe d'entité représentant un compte bancaire.  
      -  `ServerJWS.java` : Point d'entrée pour publier le service avec Endpoint.publish(). 
      
### 2. Client SOAP (client-soap-java):

  - Contient le package `ma.fs` avec la classe : `Main.java` c'est le client Java utilisant les stubs générés (via wsimport).
  - Et le package `proxy` : Contient les classes générées automatiquement à partir du WSDL. 
    
  ![img](Screens/stru.JPG)  
 

 ---
## 📄 Explication détaillée 
  
---
 -` Protocole SOAP` : est un protocole de communication XML basé sur des standards, utilisé pour échanger des données structurées entre applications via des messages XML, généralement transportés par HTTP. Il garantit une interopérabilité entre systèmes hétérogènes grâce à son format strict et son contrat WSDL.

---

 ### 1. Serveur SOAP (ws-soap):
 
 ---
 
 ## 🗂 Package `Java`
 ### - Classe ServerJWS  :
La classe ServerJWS est le point de déploiement du service web SOAP, utilisant la classe Endpoint de JAX-WS. Elle contient une méthode main() qui publie le service BanqueService à l'adresse "http://0.0.0:9090/" via la méthode statique Endpoint.publish(). Cette opération permet de rendre le service accessible aux clients SOAP et génère automatiquement le WSDL correspondant (disponible à l'URL "http://0.0.0:9090/BanqueWS?wsdl"). Le message affiché dans la console ("Web service déployé sur...") confirme le succès du déploiement.  
Cette classe permet l'exposition du service bancaire sur le réseau, en permettant ainsi aux clients distants d'invoquer les opérations de conversion de devises et de gestion de comptes via des requêtes SOAP.

  ![img](Screens/server.JPG)
 
 ## 🗂 Package `ws`
### - Classe BanqueService :
La classe BanqueService est un service web SOAP implémenté avec JAX-WS. Annotée avec @WebService(serviceName = "BanqueWS") pour définir le nom technique du service (BanqueWS) dans le WSDL généré, qui sera utilisé pour accéder au service via l'URL de déploiement (http://0.0.0:9090/BanqueWS?wsdl) , elle expose trois opérations accessibles via le protocole SOAP. Chaque méthode est marquée par @WebMethod, ce qui les rend disponibles dans le WSDL généré automatiquement. La méthode conversion(), annotée avec @WebMethod(operationName = "ConversionEuroToDH"), effectue une conversion de devises (euro vers dirham) en multipliant le montant par 11. Les méthodes getCompte() et ListComptes() permettent respectivement de récupérer un compte bancaire par son code et de lister plusieurs comptes, chacune utilisant @WebParam pour définir les noms des paramètres dans le WSDL. Les comptes sont instanciés avec des données simulées (solde aléatoire et date courante).  
Ce service sert de point central pour les opérations bancaires, en fournissant une interface standardisée via SOAP/WSDL pour une interopérabilité avec différents clients.

  ![img](Screens/BANQUE.JPG)

  ### - Classe Compte :
La classe Compte est une classe Java standard qui modélise un compte bancaire dans le système. Elle contient trois attributs principaux : code (identifiant unique du compte), solde (montant disponible) et dateCreation (date d'ouverture du compte). La classe propose deux constructeurs :
    - Un constructeur par défaut et un constructeur paramétré initialisant tous les champs.
    - Ainsi que les getters et setters correspondants pour chaque attribut, permettant ainsi une manipulation aisée des données.
Cette classe sert de modèle de données pour les opérations du service web BanqueService, où elle est utilisée pour représenter les comptes retournés par les méthodes getCompte() et ListComptes(). La simplicité de sa structure en fait un objet facilement sérialisable en XML pour le transport via les messages SOAP, tout en permettant une extension future avec des fonctionnalités supplémentaires comme des opérations bancaires plus complexes.
  ![img](Screens/COMPTE.JPG)



---

### 2. Client SOAP (client-soap-java):

---



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
Ce TP a permis de maîtriser la création de services web SOAP/WSDL avec JAX-WS, depuis le déploiement jusqu'au développement d'un client Java. Les compétences acquises incluent :  
     - La génération automatique de WSDL.
     - L'utilisation de stubs pour simplifier les appels SOAP.
     - L'analyse des messages XML échangés.
Ce projet démontre l'interopérabilité des services SOAP, idéale pour les systèmes hétérogènes nécessitant une communication standardisée.


---
 
  
