/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import dao.FichierUserDao;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author zekri
 */
@Controller
public class PrincipalController {
    
    @Autowired
    FichierUserDao fichierUserDao;
    
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
        
    Map<String, Boolean> arborescence = fichierUserDao.getArborescence(user);
    for (Map.Entry<String, Boolean> fichier: arborescence.entrySet()) {
        JSONObject response = new JSONObject();
        if(fichier.getValue() == true){
            try{
                response.put("path","/Root"+fichier.getKey());
                response.put("type","fichier");
            } catch(Exception e){
            }
        }else {
            try{
                response.put("path", "/Root"+fichier.getKey());
                response.put("type","dossier");
            } catch(Exception e){
            }
        }
        list.put(response);
    }

    return  list.toString();

       
    }
    
    @ResponseBody
    @RequestMapping(value="/getFile", method = RequestMethod.POST,produces = "application/json")
    public String contentFile(HttpServletRequest request){
            // On vérifie qu'une session n'est pas déjà ouverte
    HttpSession session= request.getSession();
    User user = (User)session.getAttribute("user");
    // Pas de session ouverte
    if(user == null) return "redirect:/login";

    
    
    JSONArray list = new JSONArray();
        
    String pathPhysique = fichierUserDao.getPathByPathLogique(user,request.getParameter("pathLogique"));
        JSONObject response = new JSONObject();
        if(pathPhysique != null){
            try{
            response.put("pathLogique",request.getParameter("pathLogique"));
            response.put("pathPhysique",pathPhysique);
            }		
                catch (Exception e){
                    System.out.println(e.toString());
                }
            try{
                ServletContext ctx = request.getServletContext();
                String path = ctx.getRealPath("/");
                InputStream flux=new FileInputStream(path+"/../" +pathPhysique); 
                InputStreamReader lecture=new InputStreamReader(flux);
                BufferedReader buff=new BufferedReader(lecture);
                String ligne;
                String contenuPage="";
                
                while ((ligne=buff.readLine())!=null){
                    contenuPage=contenuPage +ligne+"\n";
                }
                buff.close();
                
                response.put("content",contenuPage);
                }		
                catch (Exception e){
                    return e.toString();
                }
        }
       
    

    return  response.toString();

       
    }
}
