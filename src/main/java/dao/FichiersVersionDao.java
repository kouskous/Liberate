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
import models.FichiersVersion;
import models.Version;

/**
 *
 * @author Christopher
 */
public class FichiersVersionDao{
    
    EntityManager em;
    
    public FichiersVersionDao(){
        
    }
    
    public FichiersVersionDao(EntityManager em_){
        em = em_;
    }
  
    
    public EntityManager getEntityManager(){
        return this.em;
    }
    
    //Pour creer un fichierVersion
    public FichiersVersion createNewFichierVersion(String pathLogique, String nomPhysique, String nomReel, Date dateCreation,
            boolean type, Version version){

        // Création nouvel utilisateur
        FichiersVersion newFichierVersion = new FichiersVersion(pathLogique, nomPhysique, nomReel, dateCreation, type, version);

        // On essaye d'ajouter l'utilisateur à la persistence
        try{
            em.getTransaction().begin();
            em.persist(newFichierVersion);
            em.getTransaction().commit();
            return newFichierVersion;
        }
        catch(Exception e){
            System.out.println("Erreur lors de l'ajout d'un nouveau fichierVersion :");
            System.out.println(e.getMessage());
            return null;
        }
    }
    
    public List<FichiersVersion> getFileByVersion(Version version){
        TypedQuery<FichiersVersion> query = em.createNamedQuery("FichiersUsers.findByVersion", FichiersVersion.class);
        query.setParameter("version", version);
        List<FichiersVersion> results = query.getResultList();
        
        if(results.size()!=0){
            if(results.get(0).getType())
             return results;
        }
        return null;
    }
    
}