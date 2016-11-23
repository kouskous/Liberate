/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import dao.FichierUserDao;
import dao.UserProjetDao;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import models.FichiersUsers;
import models.User;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import models.Projet;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author zekri
 */
@Controller
public class PrincipalController {
    
    @Autowired
    FichierUserDao fichierUserDao;
    
    @Autowired
    UserProjetDao userProjetDao;
    
    public PrincipalController(){
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
    public String currentTree(HttpServletRequest request, HttpServletResponse res){
            // On vérifie qu'une session n'est pas déjà ouverte
    HttpSession session= request.getSession();
    User user = (User)session.getAttribute("user");
    // Pas de session ouverte
    if(user == null) return "redirect:/login";
        res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
        res.setHeader("Pragma", "no-cache"); // HTTP 1.0
        res.setDateHeader("Expires", 0); // Proxies
    
    JSONArray list = new JSONArray();
        
    Collection<FichiersUsers> arborescence = fichierUserDao.getArborescence(user);
    for (FichiersUsers fichier: arborescence) {
        JSONObject response = new JSONObject();
        if(fichier.getType() == FichiersUsers.Type.FICHIER){
            try{
                response.put("path","/Root"+fichier.getPathLogique());
                response.put("type","fichier");
                response.put("verrouillage",fichier.getVerrou());
            } catch(Exception e){
            }
        }else {
            try{
                response.put("path", "/Root"+fichier.getPathLogique());
                response.put("type","dossier");
                response.put("verrouillage",fichier.getVerrou());
            } catch(Exception e){
            }
        }
        list.put(response);
    }

    return  list.toString();
    }
    
    @RequestMapping(value="/pull", method = RequestMethod.GET)
    public String pull(HttpServletRequest request, ModelMap data) {
        // On vérifie qu'une session n'est pas déjà ouverte
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) // Pas de session ouverte
            return "login";
         // Une session déjà ouverte
         Collection<Projet> projets = userProjetDao.getProjetsByUser(user);
         data.addAttribute("projets", projets);
         return "pull";

    }
    
    

}
