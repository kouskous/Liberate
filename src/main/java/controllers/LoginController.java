/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import dao.UserDao;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author zekri
 */
@Controller
public class LoginController {
        
    EntityManager em;
    UserDao userDao;
    
    public LoginController(){
        em = Persistence.createEntityManagerFactory("persistenceUnitLiber8").createEntityManager();
        userDao = new UserDao(em);
    }
    
    @RequestMapping(value="/login", method = RequestMethod.GET)
    public String index(){
        return "login";
    }
   
    @RequestMapping(value="/login", method = RequestMethod.POST)
    public String connexion(HttpServletRequest request, ModelMap model){
        String error;
        try {
            User userDb = userDao.getUserByPseudo(request.getParameter("username"));

            if(userDb == null){
                error = "Aucun compte n'est rattaché à ce pseudo";
            } else if (request.getParameter("password").equals((userDao.getUserByPseudo(request.getParameter("username"))).getCleMotDePasse())) {
                HttpSession session = request.getSession();
                session.setAttribute("pseudo", (String)request.getParameter("username"));
                return "redirect:/";
            } else {
                error = "mot de passe incorrect";
            }
        } catch (Exception e) {
            error = "Erreur : plusieurs utilisateurs ont le même pseudo !";
        }
        model.addAttribute("error", error);

        return "login";
            
    }
 /** utilisation de JSON
    @ResponseBody @RequestMapping(value="/login", method = RequestMethod.GET,produces = "application/json") 
    public String login(){ 
    return "login"; 
     }
  */   
    
}
