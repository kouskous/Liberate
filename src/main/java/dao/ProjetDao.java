package dao;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import models.Projet;

/**
 *
 * @author Luc Di Sanza
 */
@Transactional
public class ProjetDao {

    @PersistenceContext
    EntityManager em;
    
    /**
     *
     * Récupération d'un projet en fonction de son nom
     * @param nom Nom du projet à récupérer
     * @return Renvoie le projet si réussite, null sinon
     */
    public Projet getProjetByName(String nom) throws IllegalArgumentException{
        
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
        return null;
    }
       
    /**
     * Création d'un nouveau projet
     * @param nom Nom du projet à créer
     * @param dateCreation Date actuelle
     * @param dateModification Date actuelle
     * @param langage Langage de rpogrammation du projet à créer
     * @return renvoie le projet si réussite, null sinon
     */
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
            System.out.println(e);
            return null;
        }
    }
    
    /**
     * Suppression d'un projet dans la base de données
     * @param nom Nom du projet à supprimer
     * @return Vrai si la suppression à réussie, faux sinon
     */
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
            System.out.println(e);
            return false;
        }
    }

    /**
     * Vérification de l'existence d'un nom de projet en base de donnée (pour l'unicité)
     * @param nom Nom de projet à rechercher
     * @return Vrai si il existe déja, Faux sinon
     */
    public boolean projetNameAlreadyUsed(String nom) throws IllegalArgumentException{
        Projet testProjet = getProjetByName(nom);
        return testProjet != null;
    }
    
}
