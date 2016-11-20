/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import dao.FichierUserDao;
import dao.ProjetDao;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import models.FichiersUsers;
import models.Projet;
import models.User;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Luc Di Sanza
 */
@Controller
public class CompilationController {
    
    @Autowired
    ProjetDao projetDao;
    
    @Autowired
    FichierUserDao fichierUserDao;
    
    public CompilationController(){
    }
    
    // Compilation d'un projet. La requête doit contenir un champs "nomProjet"
    @ResponseBody 
    @RequestMapping(value="/compile", method = RequestMethod.GET, produces = "application/json")
    public String compile(HttpServletRequest request, ModelMap model){  
        
        // On créé l'objet à retourner
        JSONObject returnObject = new JSONObject();   
        
        // Récupération de la session de l'utilisateur
        HttpSession session= request.getSession();
        User user = (User)session.getAttribute("user");
        
        // Si pas d'utilisateur connecté
        if(user == null){
            try{
                returnObject.put("response", "false");
                returnObject.put("content", "");
                returnObject.put("errors", "Pas d'utilisateur connecte");
                return returnObject.toString();
            }
            // Json fail
            catch(Exception e){return null;}
        }
        
        // Récupération du nom de projet
        String nomProjet = (String)request.getParameter("nomProjet");
        if(nomProjet == null){
            try{
                returnObject.put("response", "false");
                returnObject.put("content", "");
                returnObject.put("errors", "Pas de nom de projet fourni");
                return returnObject.toString();
            }
            // Json fail
            catch(Exception e){return null;}
        }
        
        // Vérification de l'existence en base
        try{
            Projet projet = projetDao.getProjetByName(nomProjet);
            if(projet == null) {throw new Exception("Erreur pendant la récupération du projet");}
        }
        catch(Exception e){
            try{
                returnObject.put("response", "false");
                returnObject.put("content", "");
                returnObject.put("errors", "Erreur lors de la recuperation du projet en base de donnees");
                return returnObject.toString();
            }
            // Json fail
            catch(Exception e2){return null;}
        }
        
        // Création du dossier avec hiérarchie pour la compilation
        ServletContext ctx = request.getServletContext();
        String path = ctx.getRealPath("/");
        if(!createCompileDirectory(path, user, nomProjet)){
            try{
                returnObject.put("response", "false");
                returnObject.put("content", "");
                returnObject.put("errors", "Erreur lors de la création du dossier de compilation");
                return returnObject.toString();
            }
            // Json fail
            catch(Exception e2){return null;}
        }
        
        // Compilation TIME
        
        
        // Reussite
        try{
            returnObject.put("response", "true");
            returnObject.put("content", "");
            returnObject.put("errors", "");
            return returnObject.toString();
        }
        // Json fail
        catch(Exception e){
            return null;
        }        
    }
    
    private boolean createCompileDirectory(String directoryPath, User user, String nomProjet){
        
        // Récupération du dossier racine du projet
        FichiersUsers racineProjet;
        try{
            racineProjet = fichierUserDao.getFichiersByUserAndPath(user, "/" + nomProjet);
            if(racineProjet == null){throw new Exception("Impossible de trouver la racine du projet");}
        }
        catch(Exception e){
            System.out.println("Erreur pendant la récupération de la racine du projet");
            return false;
        }
        
        // Récupération de l'arborescence du projet
        Map<String, Boolean> arborescence = fichierUserDao.getArborescence(user, racineProjet);
        if(arborescence == null){
            System.out.println("Erreur pendant la récupération de l'arborescence");
            return false;
        }
        
  
        // Construction du dossier à partir des paths logiques
        // Pour chaque entrée de l'arborescence
        for (Map.Entry<String, Boolean> entry : arborescence.entrySet())
        {
            // Si c'est un fichier
            if(entry.getValue() == true){
                try{              
                    // Getting file content
                    String fileName = extractFileName(entry.getKey());
                    String content = new String(Files.readAllBytes(Paths.get(directoryPath + "/../../files/" + fileName)));
                    
                    // Creating compile file and writing content to it
                    File file = new File(directoryPath + "/../../compile_" + user.getPseudo() + "/" + entry.getKey());
                    file.getParentFile().mkdirs();
                    FileWriter writer = new FileWriter(file);
                    writer.write(content);
                    writer.close();
                }
                catch(Exception e){
                    System.out.println("Erreur pendant la création du dossier de compilation");
                    return false;
                }
            }
        }
        return true;
    }
    
    private String extractFileName(String path) {
        String filename = path.substring(path.lastIndexOf("/")+1); 
        return filename;
    }
}
