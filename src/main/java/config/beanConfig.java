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
import javax.transaction.TransactionManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

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
        return new FichierUserDao();
    }    
    
    @Bean(name = "ProjetDao")
    public ProjetDao getProjetDao() {
        return new ProjetDao();
    }
    
    @Bean(name = "UserDao")
    public UserDao getUserDao() {
        return new UserDao();
    }
    
    @Bean(name = "UserProjetDao")
    public UserProjetDao getUserProjetDao() {
        return new UserProjetDao();
    }
   
}
