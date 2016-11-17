/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import dao.ProjetDao;
import dao.UserDao;
import dao.UserProjetDao;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import models.Projet;
import models.User;
import models.UserProjet;
import org.json.JSONArray;
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
public class ProjetController {
    
    @Autowired
    ProjetDao projetDao;
    
    @Autowired
    UserDao userDao;
    
    @Autowired
    UserProjetDao userProjetDao;
    
    // Regex1 utilisée pour le nom du projet
    static String regex1 = "^([a-zA-Z]{3,50})";
    static String regex2 = "^([a-zA-Z]{1,50})";
    
    public ProjetController(){
    }
    
    @RequestMapping(value="/newProjet", method = RequestMethod.GET)
    public String index(HttpServletRequest request, ModelMap model){
        return "newProjet";
    }
    
    @RequestMapping(value="/newProjet", method = RequestMethod.POST)
    public String nouveauProjet(HttpServletRequest request, ModelMap model){
        
        // Obtention de la session
        HttpSession session= request.getSession();
        User user = (User)session.getAttribute("user");
        
        // Si pas de session ouverte
        if(user == null){
            model.addAttribute("Erreur", "Pas de session ouverte");
            return "redirect:/";
        }
        else{
            
            // Obtention des paramètres de la requête
            String nomProjet = request.getParameter("nomProjet");
            String langageProjet = request.getParameter("langageProjet");
          
            ArrayList<String> usersProjet = new ArrayList<String>();
            ArrayList<String> droitsUsers = new ArrayList<String>();
            for (int i = 1; i <= 10; i++){
                if(request.getParameter("utilisateur"+ Integer.toString(i)) != null){
                    usersProjet.add((String)request.getParameter("utilisateur"+ Integer.toString(i)));
                }
                
                if(request.getParameter("droit"+ Integer.toString(i)) != null){
                    droitsUsers.add((String)request.getParameter("droit"+ Integer.toString(i)));
                }
            }
         
            // TODO: Gestion des fichiers importés
            //ArrayList<String> fichiersImportés = request.getParameter("ListeFichiersImportés");

            // Test du nom
            if(nomProjet == null || !testRegex(regex1, nomProjet)){
                model.addAttribute("Erreur", "Nom de projet invalide");
                return "newProjet";
            }

            // Création du projet
            Projet projet = projetDao.createNewProjet(nomProjet, new Date(), new Date(), langageProjet);
            
            // Si le projet a bien été créé
            if(projet != null){
                
                // Le user qui l'a créé (celui dans la session) est admin
                UserProjet userProjet = userProjetDao.createNewUserProjet("Admin", new Date(), new Date(), user, projet);
                
                // Si l'admin a bien été créé
                if(userProjet != null){
                    try{
                        // Pour chaque autre utilisateur a ajouter, on le trouve dans la BDD et on l'ajoute avec ses droits
                        for (int i = 0; i < usersProjet.size(); i++){
                            User newUser = userDao.getUserByPseudo(usersProjet.get(i));
                            userProjetDao.createNewUserProjet(droitsUsers.get(i), new Date(), new Date(), newUser, projet);
                            if(userProjetDao == null)
                                throw new Exception();
                        }
                    }
                    catch(Exception e){
                        model.addAttribute("Erreur", "Erreur pendant l'ajout de membres utilisateurs");
                        return "newProjet";
                    }
                    return "redirect:/";
                }
                else{ // Le userProjet Admin n'a pas pu être créé
                    model.addAttribute("Erreur", "BDD - User admin n'a pas pu être assigné au projet");
                    return "newProjet";
                }
            }
            else{ // Projet n'a pas pu être créé
                model.addAttribute("Erreur", "BDD - Projet n'a pas pu être créé");
                return "newProjet";
            }
        }
    }
    
    private boolean testRegex(String regex, String aTester){
        return Pattern.compile(regex).matcher(aTester).matches();
    }
            
    // Récupération des utilisateurs dont le nom contient une chaine de caractères
    // - La requête doit contenir un champs "name"
    @ResponseBody
    @RequestMapping(value="/getUsers", method = RequestMethod.GET, produces = "application/json")
    public String getUsers(HttpServletRequest request, ModelMap model){
        
        // On créé l'objet à retourner
        JSONObject returnObject = new JSONObject();   
        
        try{
            returnObject.put("response", "");
            returnObject.put("errors", "");
            
            List<String> myList = userDao.getAllPseudo();                

            JSONArray list = new JSONArray();

            for (int i = 0; i < myList.size(); i++)
            {
                if(myList.get(i) != ((User)(request.getSession().getAttribute("user"))).getPseudo()){
                    list.put(myList.get(i));
                }
            }
            returnObject.put("response", list.toString());
            return returnObject.toString();
        }
        catch(Exception e){
            return null;
        }
    }
    
}
