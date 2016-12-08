package dao;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import models.User;

/**
 *
 * @author Luc Di Sanza
 */
@Transactional
public class UserDao {

    @PersistenceContext
    EntityManager em;
    
    /**
     * @author Luc Di Sanza
     * Cherche l'utilisateur dans la BDD en fonction d'une adresse email
     * @param email Email de l'utilisateur à rechercher
     * @return Renvoie l'utilisateur si il a été trouvé, null sinon
     * @throws IllegalArgumentException
     */
    public User getUserByEmail(String email) throws IllegalArgumentException{
        
        // Recherche de l'user par email (unique)
        TypedQuery<User> query = em.createNamedQuery("User.findByEmail", User.class);
        query.setParameter("email", email);
        List<User> results = query.getResultList();
        
        // Si aucun utilisateur n'est trouvé avec cet email
        if(results.isEmpty()){
            return null;
        }
        // Un utilisateur a été trouvé
        else if(results.size() == 1){
            return results.get(0);
        }
        // Anomalie: plusieurs utilisateurs ont été trouvé avec le même email
        else{
            return null;
        }
    }
    
    /**
     * @author Luc Di Sanza
     * Cherche l'utilisateur dans la BDD en fonction de son pseudo
     * @param pseudo Pseudo d'utilisateur à rechercher
     * @return Renvoie l'user si il a été trouvé, null sinon
     * @throws IllegalArgumentException
     */
    public User getUserByPseudo(String pseudo) throws IllegalArgumentException{
        
        // Recherche de l'user par pseudo (unique)
        TypedQuery<User> query = em.createNamedQuery("User.findByPseudo", User.class);
        query.setParameter("pseudo", pseudo);
        List<User> results = query.getResultList();

        // Si aucun utilisateur n'est trouvé avec ce pseudo
        if(results.isEmpty()){
            return null;
        }
        // Un utilisateur a été trouvé
        else if(results.size() == 1){
            return results.get(0);
        }
        // Anomalie: plusieurs utilisateurs ont été trouvé avec le même pseudo
        else{
            return null;
        }
    }

    /**
     * @author Luc Di Sanza
     * Création d'un nouvel utilisateur
     * @param pseudo Pseudo de l'utilisateur à créer
     * @param email Email de l'utilisateur à créer
     * @param nom Nom de l'utilisateur à créer
     * @param prenom Prénom de l'utilisateur à créer
     * @param dateCreation Date actuelle
     * @param dateModification Date actuelle
     * @param cleMotDePasse Clé d'authentification de l'utilisateur
     * @return Renvoie l'utilisateur si réussite, null sinon
     */
    public User createNewUser(String pseudo, String email, String nom, String prenom, 
            Date dateCreation, Date dateModification, String cleMotDePasse){
        
        // Création nouvel utilisateur
        User newUser = new User(pseudo, email, nom, prenom, dateCreation, dateModification);
        newUser.setCleMotDePasse(cleMotDePasse);
        
        // On essaye d'ajouter l'utilisateur à la persistence
        try{
            em.persist(newUser);
            return newUser;
        }
        catch(Exception e){
            System.out.println("Erreur lors de l'ajout d'un nouvel user :" + e);
            return null;
        }
    } 

    /**
     * @author Luc Di Sanza
     * Suppression d'un utilisateur en fonction de son adresse mail
     * @param email Email de l'utilisateur à supprimer
     * @return Renvoie vrai si la suppression a réussi, faux sinon
     */
    public boolean deleteUserByEmail(String email){
        
        try{
            User userToDelete;
            // On cherche l'utilisateur 
            userToDelete = getUserByEmail(email);
            
            // Si on l'a trouvé, on le supprime
            if(userToDelete != null){
                em.remove(userToDelete);
                return true;
            }
            else{
                // On n'a pas trouvé l'utilisateur dans la BDD
                return false;
            }
        }
        catch(Exception e){
            System.out.println("Erreur dans la suppression d'un utilisateur");
            System.out.println(e);
            return false;
        }
    }

    /**
     * @author Luc Di Sanza
     * Modification de l'adresse email d'un utilisateur
     * @param oldEmail Ancien email de l'utilisateur 
     * @param newEmail Nouvel email de l'utilisateur
     * @return Renvoie vrai si réussite, faux sinon
     */
    public boolean changeUserEmail(String oldEmail, String newEmail){
        
        // On vérifie que l'adresse mail n'existe pas déjà dans la Bdd.
        try{
            User alreadyUsedEmail = getUserByEmail(newEmail);
            
            // On a trouvé un utilisateur avec la même adresse email
            if(alreadyUsedEmail != null){
                return false;
            }
        }
        catch(Exception e){
            System.out.println("Erreur dans la modification d'email");
            System.out.println(e);
            return false;
        }
        
        try{
            User userToChange;
            
            // On cherche l'utilisateur 
            userToChange = getUserByEmail(oldEmail);
            
            // Si on l'a trouvé, on change son email
            if(userToChange != null){
                userToChange.setEmail(newEmail);
                em.persist(userToChange);
                return true;
            }
            else{
                // On n'a pas trouvé l'utilisateur dans la BDD
                return false;
            }
        }
        catch(Exception e){
            System.out.println("Erreur dans la modification d'un email d'un utilisateur");
            System.out.println(e);
            return false;
        }
    }

    /**
     * @author Luc Di Sanza
     * Cherche si l'email est déjà utilisé dans la BDD
     * @param email Email à rechercher
     * @return Renvoie vrai si l'email existe déjà, faux sinon
     * @throws IllegalArgumentException
     */
    public boolean emailAlreadyUsed(String email) throws IllegalArgumentException{
        User testEmail = getUserByEmail(email);
        return testEmail != null;
    }

    /**
     * @author Luc Di Sanza
     * Cherche si le pseudo est déjà utilisé dans la BDD
     * @param pseudo Pseudo à rechercher dans la base de données
     * @return Vrai si le pseudo existe déjà, faux sinon
     * @throws IllegalArgumentException
     */
    public boolean pseudoAlreadyUsed(String pseudo) throws IllegalArgumentException{
        User testPseudo = getUserByPseudo(pseudo);
        return testPseudo != null;
    }
    
    /**
     * @author Luc Di Sanza
     * Renvoi la liste de tous les pseudos d'utilisateurs
     * @return Renvoie la liste des pseudo d'utilisateurs trouvés
     * @throws IllegalArgumentException
     */
    public List<String> getAllPseudo() throws IllegalArgumentException
    {      
        // Recherche de users par pseudo
        TypedQuery<String> query = em.createNamedQuery("Pseudo.getAll", String.class);
        List<String> results = query.getResultList();
        
        return results;
    }
}
