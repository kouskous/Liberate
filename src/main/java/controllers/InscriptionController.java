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
import models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author sofiafaddi
 */
@Controller
public class InscriptionController {
    
    EntityManager em;
    UserDao userDao;
    
    public InscriptionController(){
        em = Persistence.createEntityManagerFactory("persistenceUnitLiber8").createEntityManager();
        userDao = new UserDao(em);
    }
    
    @RequestMapping(value="/inscription", method = RequestMethod.GET)
    public String index(){
        return "inscription";
    }
    
    //try
    
    // Nouvelle inscription
    @RequestMapping(value="/inscription/new", method = RequestMethod.POST)
    public String newUser(HttpServletRequest request, ModelMap model){
        
        em.getTransaction().begin();
        
        //TODO: génération de la clé mot de passe ici
        
        User newUser = userDao.createNewUser(request.getParameter("pseudo"),
                                            request.getParameter("mail"), 
                                            request.getParameter("nom"), 
                                            request.getParameter("prenom"), 
                                            new Date(), 
                                            new Date(),
                                            "motDePasse");
        
        if(newUser != null){
            // L'utilisateur a bien été créé
            try{
                em.getTransaction().commit();
                return "redirect:/";
            }
            catch(Exception e){
                // Erreur pendant le commit
                // TODO: Ajout de trucs dans modelMap à afficher dans le cas erreur ici
                return "/inscription/new";
            }
        }
        else{
            // L'utilisateur n'a pas été créé
            // TODO: Ajout de trucs dans modelMap à afficher ici
            return "/inscription/new";
        }
    }
}