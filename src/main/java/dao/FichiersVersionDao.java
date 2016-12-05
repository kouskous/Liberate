/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import models.FichiersVersion;
import models.Version;

/**
 *
 * @author Christopher
 */
@Transactional
public class FichiersVersionDao{
    
    @PersistenceContext
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
            FichiersVersion.Type type, Version version){

        // Création nouvel utilisateur
        FichiersVersion newFichierVersion = new FichiersVersion(pathLogique, nomPhysique, nomReel, dateCreation, type, version);

        // On essaye d'ajouter l'utilisateur à la persistence
        try{
           
            em.persist(newFichierVersion);
            
            return newFichierVersion;
        }
        catch(Exception e){
            System.out.println("Erreur lors de l'ajout d'un nouveau fichierVersion :");
            System.out.println(e.getMessage());
            return null;
        }
    }
    
    public List<FichiersVersion> getFileByVersion(Version version){
        TypedQuery<FichiersVersion> query = em.createNamedQuery("FichiersVersion.findByVersion", FichiersVersion.class);
        query.setParameter("version", version);
        List<FichiersVersion> results = query.getResultList();
        
        if(results.size()!=0){
            
             return results;
        }
        return null;
    }
    
}
