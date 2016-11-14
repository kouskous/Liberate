/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import models.Projet;
import models.User;
import models.UserProjet;

/**
 *
 * @author Luc Di Sanza
 */
public class UserProjetDao {
    
    EntityManager em;
    
    public UserProjetDao(){
        
    }
    
    public UserProjetDao(String persistenceUnitName){
        em = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager();
    }
    
    public EntityManager getEntityManager(){
        return this.em;
    }
    
    /*public UserProjetDao(EntityManager em){
        this.em = em;
    }*/
    
    // Cherche le UserProjet dans la BDD
    // - renvoie null si il n'y est pas.
    // - renvoie le UserProjet si il y est
    // - exception si il y a plusieurs UserProjets avec la même paire user/projet.
    public UserProjet getUserProjetByUIdPId(int idUser, int idProjet) throws Exception{
        
        // Recherche du projet par nom (unique)
        TypedQuery<UserProjet> query = em.createNamedQuery("UserProjet.findByIdUAndIdP", UserProjet.class);
        query.setParameter("idP", idProjet);
        query.setParameter("idU", idUser);
        List<UserProjet> results = query.getResultList();
        
        // Si aucun projet n'est trouvé avec ce nom
        if(results.isEmpty()){
            return null;
        }
        // Un projet a été trouvé
        else if(results.size() == 1){
            return results.get(0);
        }
        // Anomalie: plusieurs projets ont été trouvé avec le même nom
        else{
            throw new Exception("Erreur BDD: plusieurs UserProjets avec la même paire User/Projet");
        }
    }
       
    // Création d'un nouveau UserProjet
    // Renvoie le UserProjet si réussite
    // Renvoie null sinon
    public UserProjet createNewUserProjet(String typeDroit, Date dateCreation, Date dateModification, 
            User user, Projet projet){
        
        if (user != null && projet != null){
            
            // Création nouveau UserProjet
            UserProjet newUserProjet = new UserProjet(typeDroit, dateCreation, dateModification, user, projet);
            
            // On essaye d'ajouter le UserProjet à la persistence
            try{
                em.persist(newUserProjet);
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
                em.remove(userProjetToDelete);
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
    
}
