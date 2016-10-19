/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import models.User;

/**
 *
 * @author Luc Di Sanza
 */
public class UserDao {
    
    EntityManager em;
    
    public UserDao(){
        
    }
    
    public UserDao(EntityManager em){
        this.em = em;
    }
    
    // Cherche l'utilisateur dans la BDD
    // - renvoie null si il n'y est pas.
    // - renvoie l'user si il y est
    // - exception si il y a plusieurs utilisateur avec cet email dans la bdd
    public User getUserByEmail(String email) throws Exception{
        
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
            throw new Exception("Erreur BDD: plusieurs utilisateurs avec le même email");
        }
    }
    
    // Cherche l'utilisateur dans la BDD
    // - renvoie null si il n'y est pas.
    // - renvoie l'user si il y est
    // - exception si il y a plusieurs utilisateur avec ce pseudo dans la bdd
    public User getUserByPseudo(String pseudo) throws Exception{
        
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
            throw new Exception("Erreur BDD: plusieurs utilisateurs avec le même pseudo");
        }
    }

       
    // Création d'un nouvel utilisateur
    // Renvoie l'utilisateur si réussite
    // Renvoie null sinon
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
            System.out.println("Erreur lors de l'ajout d'un nouvel user");
            System.out.println(e.getMessage());
            return null;
        }
    } 
    
    // Suppression d'un utilisateur de la base de données
    // Renvoie vrai si la suppression a réussie
    // Renvoie faux sinon.
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
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    // Modifie l'adresse email d'un utilisateur
    // Renvoie vrai si réussi
    // Renvoie faux sinon (pas d'utilisateur avec oldEmail, ou déjà un utilisateur avec newEmail
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
            System.out.println(e.getMessage());
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
            System.out.println("Erreur dans la suppression d'un utilisateur");
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    // TODO: toutes les autres fonctions de modification qu'on aura besoin.
    //sur le modèle de changeUserEmail
    
}
