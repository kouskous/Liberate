/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import dao.FichierUserDao;
import dao.ProjetDao;
import dao.UserDao;
import dao.UserProjetDao;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Luc Di Sanza
 */
@Configuration
public class beanConfig {
    
    @Bean(name = "EntityManager")
    public EntityManager getEntityManager(){
        return Persistence.createEntityManagerFactory("persistenceUnitLiber8").createEntityManager();
    }
    
    @Bean(name = "FichierUserDao")
    public FichierUserDao getFichierUserDao() {
        return new FichierUserDao(getEntityManager());
    }    
    
    @Bean(name = "ProjetDao")
    public ProjetDao getProjetDao() {
        return new ProjetDao(getEntityManager());
    }
    
    @Bean(name = "UserDao")
    public UserDao getUserDao() {
        return new UserDao(getEntityManager());
    }
    
    @Bean(name = "UserProjetDao")
    public UserProjetDao getUserProjetDao() {
        return new UserProjetDao(getEntityManager());
    }
   
}
