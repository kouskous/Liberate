/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import models.Projet;
import models.User;
import models.UserProjet;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Luc Di Sanza
 */
public class UserProjetDao {
    
    EntityManager em;
    
    public UserProjetDao(){
        
    }
    
    public EntityManager getEntityManager(){
        return this.em;
    }
    
    public UserProjetDao(EntityManager em){
        this.em = em;
    }
    
    // Cherche le UserProjet dans la BDD
    // - renvoie null si il n'y est pas.
    // - renvoie le UserProjet si il y est
    // - exception si il y a plusieurs UserProjets avec la même paire user/projet.
    public UserProjet getUserProjetByUIdPId(int idUser, int idProjet) throws Exception{
        
        // Recherche du UserProjet par idUser et idProjet (unique)
        TypedQuery<UserProjet> query = em.createNamedQuery("UserProjet.findByIdUAndIdP", UserProjet.class);
        query.setParameter("idP", idProjet);
        query.setParameter("idU", idUser);
        List<UserProjet> results = query.getResultList();
        
        // Si aucun UserProjet n'est trouvé avec ces ID
        if(results.isEmpty()){
            return null;
        }
        // Un UserProjet a été trouvé
        else if(results.size() == 1){
            return results.get(0);
        }
        // Anomalie: plusieurs UserProjet ont été trouvé avec le même nom
        else{
            throw new Exception("Erreur BDD: plusieurs UserProjets avec la même paire User/Projet");
        }
    }
       
    // Création d'un nouveau UserProjet
    // Renvoie le UserProjet si réussite
    // Renvoie null sinon
    public UserProjet createNewUserProjet(String typeDroit, Date dateCreation, Date dateModification, 
            User user, Projet projet){
        
        em.getTransaction().begin();
        
        if (user != null && projet != null){
            
            if (!(typeDroit.equals("admin") || typeDroit.equals("developpeur") || typeDroit.equals("reporteur"))){
                return null;
            }
            
            // Création nouveau UserProjet
            UserProjet newUserProjet = new UserProjet(typeDroit, dateCreation, dateModification, user, projet);
            
            // On essaye d'ajouter le UserProjet à la persistence
            try{
               
                em.persist(newUserProjet);
                em.getTransaction().commit();
                return newUserProjet;
            }
            catch(Exception e){
                System.out.println("Erreur lors de l'ajout d'un nouveau UserProjet");
                System.out.println(e.getMessage());
                return null;
            }
        }
        return null;
    }
    
    // Suppression d'un UserProjet de la base de données
    // Renvoie vrai si la suppression a réussie
    // Renvoie faux sinon.
    public boolean deleteUserProjet(User user, Projet projet){
        
        try{
            UserProjet userProjetToDelete;
            
            // On cherche le userProjet
            userProjetToDelete = getUserProjetByUIdPId(user.getIdUser(), projet.getIdProjet());
            
            // Si on l'a trouvé, on le supprime
            if(userProjetToDelete != null){
                em.getTransaction().begin();
                em.remove(userProjetToDelete);
                em.getTransaction().commit();
                return true;
            }
            else{
                // On n'a pas trouvé le userProjet dans la BDD
                return false;
            }
        }
        catch(Exception e){
            System.out.println("Erreur dans la suppression d'un UserProjet");
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    
    // Change les droits d'un userProjet existant, ou le créé avec ces droits sinon.
    // TypeDroit doit faire partie de {"Admin", "Dev", "Reporter"}
    // - Renvoie vrai si réussite
    // - Faux sinon
    public boolean changeDroitsUserProjet(String typeDroit, int idUser, int idProjet){
        
        // On teste que typeDroit est une chaine de caractère valide
        if (typeDroit.equals("admin") || typeDroit.equals("developpeur") || typeDroit.equals("reporteur")){
            try{
                UserProjet userProjet = getUserProjetByUIdPId(idUser, idProjet);

                if(userProjet == null) // Le userProjet n'a pas été trouvé
                    return false;
                else{
                    return changeDroitsUserProjet(typeDroit, userProjet);
                }
            }
            catch(Exception e){
                System.out.println("Erreur lors du changement de droits d'utilisateur");
                System.out.println(e.getMessage());
                return false;
            }
        }
        else{ // La chaine typeDroit n'est pas valide
            return false;
        }
    }
    
    // Change les droits d'un userProjet existant, ou le créé avec ces droits sinon.
    // TypeDroit doit faire partie de {"Admin", "Dev", "Reporter"}
    // - Renvoie vrai si réussite
    // - Faux sinon
    public boolean changeDroitsUserProjet(String typeDroit, UserProjet userProjet){
        
        em.getTransaction().begin();
        
        // Vérification que typeDroit est une chaine de caractère valide
        if (typeDroit.equals("admin") || typeDroit.equals("developpeur") || typeDroit.equals("reporteur")){
            userProjet.setTypeDroit(typeDroit);
      
            try{
                em.persist(userProjet);
                em.getTransaction().commit();
                return true;
            }
            catch (Exception e){
                System.out.println("Erreur lors du changement de droits utilisateur projet");
                System.out.println(e.getMessage());
                return false;
            }
        }
        else{ // typeDroit n'est pas une chaine de caractères valide
            return false;
        }
    }
    
    // Renvoi la liste de tous les utilisateurs qui participent à un projet
    public List<User> getAllUsersByProjet(Projet projet)
    {      
        // Recherche de users du projet
        TypedQuery<UserProjet> query = em.createNamedQuery("UserProjet.findByIdP", UserProjet.class);
        query.setParameter("idP", projet.getIdProjet());
        List<UserProjet> results = query.getResultList();
        List<User> listUsers = new ArrayList<User>();
        for (int i = 0; i < results.size(); i++){
            listUsers.add(results.get(i).getUser());
        }
        
        return listUsers;
    }
    
    // Renvoi la liste de tous les utilisateurs qui ne participent pas à un projet
    public List<User> getAllUsersNotInProjet(Projet projet)
    {            
        // Recherche de tous les users
        TypedQuery<User> query = em.createNamedQuery("User.findAll", User.class);
        List<User> allUsers = query.getResultList();
        
        // Recherche des users du projet
        List<User> usersProjet = getAllUsersByProjet(projet);
        
        // Différence entre les deux
        allUsers.removeAll(usersProjet);
        
        return allUsers;
    }
    
}
