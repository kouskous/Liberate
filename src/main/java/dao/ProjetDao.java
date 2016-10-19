/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import javax.persistence.EntityManager;

/**
 *
 * @author Luc Di Sanza
 */
public class ProjetDao {
    
    EntityManager em;
    
    public ProjetDao(){
        
    }
    
    public ProjetDao(EntityManager em){
        this.em = em;
    }
    
    
    
}
