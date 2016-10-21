/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import dao.FichierUserDao;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import dao.FichierUserDao;
import dao.UserDao;
import models.FichiersUsers;
import models.User;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 *
 * @author zekri
 */
@Controller
public class PrincipalController {
    FichierUserDao fichierUserDao;
    
    public PrincipalController(){
       fichierUserDao = new FichierUserDao();
    }
    
    @RequestMapping(value="/", method = RequestMethod.GET)
    public String index(HttpServletRequest request) {
        // On vérifie qu'une session n'est pas déjà ouverte

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) // Pas de session ouverte
            return "login";
         // Une session déjà ouverte
         return "redirect:/";

    }
    
        @RequestMapping(value="/", method = RequestMethod.GET,produces = "application/json")
    public String currentTree(HttpServletRequest request){
            // On vérifie qu'une session n'est pas déjà ouverte
    HttpSession session= request.getSession();
    User user = (User)session.getAttribute("user");
    // Pas de session ouverte
    if(user == null) return "redirect:/login";

    JSONObject response = new JSONObject();
    Map<String, Boolean> arborescence = fichierUserDao.getArborescence(user);
    for (Map.Entry<String, Boolean> fichier: arborescence.entrySet()) {
        try{
            response.put(fichier.getKey(), fichier.getValue());
        } catch(Exception e){
        }
    }

    return  response.toString();

       
    }
}
