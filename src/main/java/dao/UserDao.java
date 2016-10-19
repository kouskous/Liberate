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
       
    // Création d'un nouvel utilisateur
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
    
}
