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
import javax.xml.registry.infomodel.User;
import models.Version;
//import models.User;
import models.Projet;

/**
 *
 * @author chris
 */
public class VersionDao {
    
    EntityManager em;
    
    public VersionDao(){
        
    }
    
    public VersionDao(EntityManager em){
        this.em = em;
    }
    
    // Cherche les versions des projets d'un utilisateur
    // - renvoie null si il n'en possède aucun
    // - renvoie une liste de versions sinon
    // - exception si 
    public List<Version> getVersionByUser(int idUser) {
        
        // Recherche des versions par user 
        TypedQuery<Version> query = em.createNamedQuery("Version.findByUser", Version.class);
        query.setParameter("idU", idUser);
        List<Version> results = query.getResultList();
        
        // Si aucune version n'est disponible pour cet utilisateur
        if(results.isEmpty()){
            return null;
        }
        // Un utilisateur a été trouvé
        
            return results;
        
       
    
}
    
    public List<Version> getVersionByProjet(int idProjet) {
        
        // Recherche des versions par user 
        TypedQuery<Version> query = em.createNamedQuery("Version.findByProjet", Version.class);
        query.setParameter("idP", idProjet);
        List<Version> results = query.getResultList();
        
        // Si aucune version n'est disponible pour ce projet(NORMALEMENT IMPOSSIBLE)
        if(results.isEmpty()){
            return null;
        }
        // Une version ou plus a été trouvé
        
            return results;
        
       
    
}
    
    // Création d'une nouvelle version
    // Renvoie la version si réussite
    // Renvoie null sinon
    public Version createNewVersion(String messageCommit, int numVersion,Date dateCreation, Projet projet, models.User user){
        
        // Création nouvelle version
        Version newVersion = new Version(numVersion, dateCreation, user, projet);
        newVersion.setMessageCommit(messageCommit);
        
        // On essaye d'ajouter la version à la persistence
        try{
            em.persist(newVersion);
            return newVersion;
        }
        catch(Exception e){
            System.out.println("Erreur lors de l'ajout d'un nouvelle version");
            System.out.println(e.getMessage());
            return null;
        }
    }
    
}
