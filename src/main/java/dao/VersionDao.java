/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import models.FichiersUsers;
import models.Projet;
import models.Version;

/**
 *
 * @author Christopher
 */
public class VersionDao {
    
    EntityManager em;
    
    public VersionDao(){
        
    }
    
    public VersionDao(EntityManager em_){
        em = em_;
    }
  
    
    public EntityManager getEntityManager(){
        return this.em;
    }
    
    
    
    public List<Version> getVersionsByProjet(Projet projet){
        TypedQuery<Version> query = em.createNamedQuery("Version.findByProjet", Version.class);
        query.setParameter("projet", projet);
        List<Version> results =query.getResultList();
        if(results.isEmpty()){
            return null;
        }
        else
        return results;
    }
    
    public Version getLastVersionByProjet(Projet projet){
        TypedQuery<Version> query =em.createNamedQuery("Version.findLastByProjet", Version.class);
        query.setParameter("projet", projet);
        List<Version> results = query.getResultList();
        
        if (results.size()==1){
            return results.get(0);
        }else return null;
    }
}


