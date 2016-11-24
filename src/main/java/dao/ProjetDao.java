/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import models.Projet;
import models.User;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Luc Di Sanza
 */
@Transactional
public class ProjetDao {

    @PersistenceContext
    EntityManager em;

    public ProjetDao(EntityManager em){
        this.em = em;
    }
    
    // Cherche le projet dans la BDD
    // - renvoie null si il n'y est pas.
    // - renvoie le projet si il y est
    // - exception si il y a plusieurs projets avec ce nom dans la bdd
    public Projet getProjetByName(String nom) throws Exception{
        
        // Recherche du projet par nom (unique)
        
        TypedQuery<Projet> query = em.createNamedQuery("Projet.findByNom", Projet.class);
        
        query.setParameter("nom", nom);
        
        List<Projet> results = query.getResultList();
        
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
            
            throw new Exception("Erreur BDD: plusieurs projets avec le même nom");
        }
    }
       
    // Création d'un nouveau projet
    // Renvoie le projet si réussite
    // Renvoie null sinon
    public Projet createNewProjet(String nom, Date dateCreation, Date dateModification, String langage){

        // On essaye d'ajouter le projet à la persistence
        try{
            // Création nouveau projet
            Projet newProjet = new Projet(nom, dateCreation, dateModification, langage);
            em.persist(newProjet);
            return newProjet;
        }
        catch(Exception e){
            System.out.println("Erreur lors de l'ajout d'un nouveau projet");
            System.out.println(e.getMessage());
            return null;
        }
    }
    
    // Suppression d'un projet de la base de données
    // Renvoie vrai si la suppression a réussie
    // Renvoie faux sinon.
    public boolean deleteProjetByName(String nom){
        
        try{
            Projet projetToDelete;
            // On cherche le projet
            projetToDelete = getProjetByName(nom);
            
            // Si on l'a trouvé, on le supprime
            if(projetToDelete != null){
                em.remove(projetToDelete);
                return true;
            }
            else{
                // On n'a pas trouvé le projet dans la BDD
                return false;
            }
        }
        catch(Exception e){
            System.out.println("Erreur dans la suppression d'un projet");
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean projetNameAlreadyUsed(String nom) throws Exception{
        Projet testProjet = getProjetByName(nom);

        if(testProjet != null){
            // Nom déjà utilisé
            return true;
        }
        return false;
    }
    
}
