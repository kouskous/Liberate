/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import dao.FichierUserDao;
import models.User;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import org.json.JSONArray;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author zekri
 */
@Controller
public class PrincipalController {
    EntityManager em;
    FichierUserDao fichierUserDao;
    
    public PrincipalController(){
       em = Persistence.createEntityManagerFactory("persistenceUnitLiber8").createEntityManager();
       fichierUserDao = new FichierUserDao(em);
    }
    
    @RequestMapping(value="/", method = RequestMethod.GET)
    public String index(HttpServletRequest request) {
        // On vérifie qu'une session n'est pas déjà ouverte

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) // Pas de session ouverte
            return "login";
         // Une session déjà ouverte
         return "principal";

    }
    @ResponseBody
    @RequestMapping(value="/getTree", method = RequestMethod.GET,produces = "application/json")
    public String currentTree(HttpServletRequest request){
            // On vérifie qu'une session n'est pas déjà ouverte
    HttpSession session= request.getSession();
    User user = (User)session.getAttribute("user");
    // Pas de session ouverte
    if(user == null) return "redirect:/login";

    
    
    JSONArray list = new JSONArray();
        
    Map<String, Boolean> arborescence = fichierUserDao.getArborescence(user);
    for (Map.Entry<String, Boolean> fichier: arborescence.entrySet()) {
        JSONObject response = new JSONObject();
        if(fichier.getValue() == true){
            try{
                response.put(fichier.getKey(), "fichier");
            } catch(Exception e){
            }
        }else {
            try{
                response.put(fichier.getKey(), "dossier");
            } catch(Exception e){
            }
        }
        list.put(response);
    }

    return  list.toString();

       
    }
}
