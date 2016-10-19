/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;
import dao.UserDao;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author sofiafaddi
 */
@Controller
public class InscriptionController {
    @RequestMapping(value="/inscription", method = RequestMethod.GET)
    public String index(){
        return "inscription";
    }
    
    @RequestMapping(value="/inscription/new", method = RequestMethod.POST)
    public String newUser(HttpServletRequest request){
        
        EntityManager em = Persistence.createEntityManagerFactory("persistenceUnitLiber8").createEntityManager();
        UserDao userDao = new UserDao(em);
        
        em.getTransaction().begin();
        
        userDao.createNewUser(request.getParameter("pseudo"),
                request.getParameter("pseudo"), 
                request.getParameter("pseudo"), 
                request.getParameter("pseudo"), 
                new Date(), 
                new Date(),
                "fred");
        
        
        em.getTransaction().commit();
        em.close();
        
        return "redirect:/";
    }
}