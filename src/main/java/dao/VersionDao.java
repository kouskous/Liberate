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
import models.FichiersUsers;
import models.Projet;
import models.User;
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
    
    
    //Recupere les versions d'un projet
    //renvoie null si il ne trouve aucune versions pour un projet
    //renvoie une liste de versions sinon
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
    
    
    //Recupere la derniere version d'un projet
    //Si pas de resultats ou trop de resultats return null
    //sinon retourne la derniere version du projet
    public Version getLastVersionByProjet(Projet projet){
        TypedQuery<Version> query =em.createNamedQuery("Version.findLastByProjet", Version.class);
        query.setParameter("projet", projet);
        List<Version> results = query.getResultList();
        
        if (results.size()==1){
            return results.get(0);
        }else return null;
    }
    
    //Retourne la derniere version d'un projet pushe par un utilisateur
    //Si vide ou trop de resultats on renvoie null 
    //Sinon on renvoie la version en question
    public Version getLastVersionByUserAndProjet(User user, Projet projet){
        TypedQuery<Version> query =em.createNamedQuery("Version.findLastByUserAndProjet", Version.class);
        query.setParameter("user", user);
        query.setParameter("projet", projet);
        List<Version> results = query.getResultList();
        
        if (results.size()==1){
            return results.get(0);
        }else return null;
    }
    
    
    
    //Creation d'une nouvelle version
    public Version createNewVersion(int numVersion, Date dateCreation, User user, Projet projet, String messageCommit){
        
        // Création nouvel utilisateur
        Version newVersion = new Version(numVersion, dateCreation, user, projet);
        newVersion.setMessageCommit(messageCommit);
        
        // On essaye d'ajouter l'utilisateur à la persistence
        try{
            em.getTransaction().begin();
            em.persist(newVersion);
            em.getTransaction().commit();
            return newVersion;
        }
        catch(Exception e){
            System.out.println("Erreur lors de l'ajout d'une nouvelle version :");
            System.out.println(e);
            return null;
        }
    } 
}


