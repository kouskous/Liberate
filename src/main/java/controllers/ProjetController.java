/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import dao.FichierUserDao;
import dao.ProjetDao;
import dao.UserDao;
import dao.UserProjetDao;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import models.FichiersUsers;
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
    
    @Autowired
    FichierUserDao fichierUserDao;
    
    // Regex1 utilisée pour le nom du projet
    static String regex1 = "^([a-zA-Z]{3,50})";
    static String regex2 = "^([a-zA-Z]{1,50})";
    
    public ProjetController(){
    }
    
    @RequestMapping(value="/newProjet", method = RequestMethod.GET)
    public String index(HttpServletRequest request, ModelMap model){
        return "newProjet";
    }
    
    @ResponseBody 
    @RequestMapping(value="/newProjet", method = RequestMethod.POST)
    public String nouveauProjet(HttpServletRequest request, ModelMap model){
        
        // On créé l'objet à retourner
        JSONObject returnObject = new JSONObject();   
        
        // Obtention de la session
        HttpSession session= request.getSession();
        User user = (User)session.getAttribute("user");
        
        try{
            returnObject.put("response", "");
            returnObject.put("errors", "");
            
            // Si pas de session ouverte
            if(user == null){
                returnObject.put("errors", "No user");
                return returnObject.toString();
            }
            else{
                // Obtention des paramètres de la requête
                String nomProjet = request.getParameter("nomProjet");
                String langageProjet = request.getParameter("langageProjet");
                
                if(nomProjet == null || langageProjet == null){
                    returnObject.put("errors", "No project name or language");
                    return returnObject.toString();
                }
                else{
                    // Test du nom de projet
                    if(!testRegex(regex1, nomProjet)){
                        returnObject.put("errors", "Nom de projet invalide");
                        return returnObject.toString();
                    }
                    
                    JSONObject jsonUsersProjet = new JSONObject(request.getParameter("utilisateurs"));
                    JSONObject jsonDroitsProjet = new JSONObject(request.getParameter("droits"));
                    ArrayList<String> usersProjet = new ArrayList<String>();
                    ArrayList<String> droitsUsers = new ArrayList<String>();
                    
                    for (int i = 0; i <= 9; i++){
                        if(jsonUsersProjet.has(Integer.toString(i)) && !jsonUsersProjet.get(Integer.toString(i)).equals("")){
                            usersProjet.add((String)jsonUsersProjet.get(Integer.toString(i)));
                        }
                        
                        if(jsonDroitsProjet.has(Integer.toString(i)) && !jsonDroitsProjet.get(Integer.toString(i)).equals("")){
                            droitsUsers.add((String)jsonDroitsProjet.get(Integer.toString(i)));
                        }
                    }
                    
                    if(projetDao.projetNameAlreadyUsed(nomProjet)){
                        returnObject.put("errors", "Nom de projet déjà utilisé");
                        return returnObject.toString();
                    }
                    else{
                        // Création du projet
                        Projet projet = projetDao.createNewProjet(nomProjet, new Date(), new Date(), langageProjet);

                        // Si le projet a bien été créé
                        if(projet != null){

                            // Le user qui l'a créé (celui dans la session) est admin
                            UserProjet userProjet = userProjetDao.createNewUserProjet("admin", new Date(), new Date(), user, projet);

                            // Si l'admin a bien été créé
                            if(userProjet != null){
                                try{
                                    // On créé le dossier vide du projet pour l'admin
                                    FichiersUsers newFile = fichierUserDao.createNewFichierUser("/" + nomProjet, nomProjet, nomProjet, new Date(), false, user);

                                    // Pour chaque autre utilisateur a ajouter, on le trouve dans la BDD et on l'ajoute avec ses droits
                                    for (int i = 0; i < usersProjet.size(); i++){
                                        User newUser = userDao.getUserByPseudo(usersProjet.get(i));
                                        userProjetDao.createNewUserProjet(droitsUsers.get(i), new Date(), new Date(), newUser, projet);
                                        if(userProjetDao == null)
                                            throw new Exception();
                                    }
                                }
                                catch(Exception e){
                                    returnObject.put("response", Integer.toString(usersProjet.size()));
                                    returnObject.put("errors", "Erreur pendant l'ajout de membres utilisateurs");
                                    return returnObject.toString();
                                }
                                returnObject.put("response", "true");
                                return returnObject.toString();
                            }
                            else{ // Le userProjet Admin n'a pas pu être créé
                                returnObject.put("errors", "BDD - User admin n'a pas pu être assigné au projet");
                                return returnObject.toString();
                            }
                        }
                        else{ // Projet n'a pas pu être créé
                            returnObject.put("errors", "BDD - Projet n'a pas pu être créé");
                            return returnObject.toString();
                        }
                    }
                }
            }
        }
        catch(Exception e){
            try{
                return returnObject.toString();
            }
            catch(Exception e2){
                return null;
            }
        }
    }
    
    private boolean testRegex(String regex, String aTester){
        return Pattern.compile(regex).matcher(aTester).matches();
    }
            
    // Récupération des pseudo d'utilisateurs qui ne sont pas celui connecté
    // - La requête doit contenir un champs "name"
    @ResponseBody
    @RequestMapping(value="/getUsers", method = RequestMethod.GET, produces = "application/json")
    public String getUsers(HttpServletRequest request, ModelMap model){
        
        // On créé l'objet à retourner
        JSONObject returnObject = new JSONObject();   
        
        try{
            returnObject.put("response", "");
            returnObject.put("errors", "");
            
            // On récupère le pseudo de l'utilisateur courant
            HttpSession session= request.getSession();
            User user = (User)session.getAttribute("user");
            String pseudoCurrent = user.getPseudo();
            
            List<String> myList = userDao.getAllPseudo();                

            JSONArray list = new JSONArray();

            for (int i = 0; i < myList.size(); i++)
            {
                if(!myList.get(i).equals(pseudoCurrent)){
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
   
    @RequestMapping(value="/gestionsUsers", method = RequestMethod.GET)
    public String getUsersP(HttpServletRequest request, ModelMap model){
        return "gestionsUsers";
    }
    
    // Récupération des utilisateurs qui font partie d'un projet en particulier, avec leurs droits.
    // - La requête doit contenir un champs "nomProjet"
    @ResponseBody
    @RequestMapping(value="/getUsersInProject", method = RequestMethod.GET, produces = "application/json")
    public String getUsersInProject(HttpServletRequest request, ModelMap model){
        HttpSession session= request.getSession();
       User user = (User)session.getAttribute("user");
        // On créé l'objet à retourner
        JSONObject returnObject = new JSONObject();

        // Récupération du nom de projet
        String nomProjet = (String)request.getParameter("nomProjet");
        if(nomProjet == null){
            try{
                returnObject.put("response", "false");
                returnObject.put("content", "");
                returnObject.put("errors", "Pas de nom de projet");
                return returnObject.toString();
            }
            // JSon Fail
            catch(Exception e){return null;}
        }
         
         // On cherche le projet dans la bdd
        Projet projet;
        try{
            projet = projetDao.getProjetByName(nomProjet);
         
            if(projet == null){
                try{
                    returnObject.put("response", "false");
                    returnObject.put("content", "");
                    returnObject.put("errors", "Erreur pendant la récupération du projet");
                    return returnObject.toString();
                }
                // Json Fail
                catch(Exception e2){return null;} 
            }
        }
        catch(Exception e){
            try{
                returnObject.put("response", "false");
                returnObject.put("content", "");
                returnObject.put("errors", "Erreur pendant la récupération du projet");
                return returnObject.toString();
            }
            // Json Fail
            catch(Exception e2){return null;}  
        }
        
        // On récupère tous les utilisateurs du projet
        List<User> myList = new ArrayList();
        List<String> pseudoUsers = new ArrayList();
        List<String> droitsUsers = new ArrayList();
        JSONObject jsonPseudoDroits = new JSONObject();
        try{
            myList = userProjetDao.getAllUsersByProjet(projet);
            if(myList == null){
                try{
                    returnObject.put("response", "false");
                    returnObject.put("content", "");
                    returnObject.put("errors", "Erreur pendant la récupération des utilisateurs du projet");
                    return returnObject.toString();
                }
                // Json Fail
                catch(Exception e2){return null;} 
            }
            else{
         
                int j =1;
            for (int i = 0; i < myList.size(); i++){
                  //pseudoUsers.add(myList.get(i).getPseudo());
                        JSONObject jsonInter = new JSONObject();
                  jsonInter.put("utilisateur", myList.get(i).getPseudo());
                  String droitsUtilisateur = userProjetDao.getDroits(myList.get(i), projet);
                  if(droitsUtilisateur != null){
                      jsonInter.put("droit", droitsUtilisateur);
                     if(myList.get(i).getPseudo().equals(user.getPseudo())){
                        jsonPseudoDroits.put( "0",jsonInter);
                        }else{
                        jsonPseudoDroits.put( Integer.toString(j),jsonInter);
                                j++;
                        }

                  }
                  else{
                      throw new Exception("Erreur pendant la récupération des utilisateurs du projet");
                  }
              }
            }
        }
        catch(Exception e){
            try{
                returnObject.put("response", "false");
                returnObject.put("content", "");
                returnObject.put("errors", "Erreur pendant la récupération des utilisateurs du projet");
                return returnObject.toString();
            }
            // Json Fail
            catch(Exception e2){return null;}  
        }
        
        // Réussite
        try{
            returnObject.put("response", "true");
            returnObject.put("content", jsonPseudoDroits.toString());
            returnObject.put("errors", "");
            return returnObject.toString();
        }
        // Json Fail
        catch(Exception e2){return null;}  
    }
    
    // Récupération des utilisateurs qui font partie d'un projet en particulier
    // - La requête doit contenir un champs "nomProjet"
    @ResponseBody
    @RequestMapping(value="/getUsersNotInProject", method = RequestMethod.GET, produces = "application/json")
    public String getUsersNotInProject(HttpServletRequest request, ModelMap model){
        
        // On créé l'objet à retourner
        JSONObject returnObject = new JSONObject();

        // Récupération du nom de projet
        String nomProjet = (String)request.getParameter("nomProjet");
        if(nomProjet == null){
            try{
                returnObject.put("response", "false");
                returnObject.put("content", "");
                returnObject.put("errors", "Pas de nom de projet");
                return returnObject.toString();
            }
            // JSon Fail
            catch(Exception e){return null;}
        }
         
         // On cherche le projet dans la bdd
        Projet projet;
        try{
            projet = projetDao.getProjetByName(nomProjet);
         
            if(projet == null){
                try{
                    returnObject.put("response", "false");
                    returnObject.put("content", "");
                    returnObject.put("errors", "Erreur pendant la récupération du projet");
                    return returnObject.toString();
                }
                // Json Fail
                catch(Exception e2){return null;} 
            }
        }
        catch(Exception e){
            try{
                returnObject.put("response", "false");
                returnObject.put("content", "");
                returnObject.put("errors", "Erreur pendant la récupération du projet");
                return returnObject.toString();
            }
            // Json Fail
            catch(Exception e2){return null;}  
        }
        
        // On récupère tous les utilisateurs qui n'appartiennent pas au projet
        List<User> myList = new ArrayList();
        List<String> pseudoUsers = new ArrayList();
        try{
            myList = userProjetDao.getAllUsersNotInProjet(projet);
            if(myList == null){
                try{
                    returnObject.put("response", "false");
                    returnObject.put("content", "");
                    returnObject.put("errors", "Erreur pendant la récupération des utilisateurs");
                    return returnObject.toString();
                }
                // Json Fail
                catch(Exception e2){return null;} 
            }
            else{
                for (int i = 0; i < myList.size(); i++){
                    pseudoUsers.add(myList.get(i).getPseudo());
                }
            }
        }
        catch(Exception e){
            try{
                returnObject.put("response", "false");
                returnObject.put("content", "");
                returnObject.put("errors", "Erreur pendant la récupération des utilisateurs du projet");
                return returnObject.toString();
            }
            // Json Fail
            catch(Exception e2){return null;}  
        }
        
        // Réussite
       try{
       	int i=0;
           returnObject.put("response", "true");
           JSONObject Inter1 = new JSONObject();
           for(String s :  pseudoUsers){
           	JSONObject Inter = new JSONObject();
           	Inter.put("pseudo",s);
            Inter1.put(Integer.toString(i), Inter);
           	i++;
           }
           returnObject.put("content", Inter1);
           returnObject.put("errors", "");
           return returnObject.toString();
       }
        // Json Fail
        catch(Exception e2){return null;}  
    }
    
    // Ajout d'un utilisateur à un projet
    // - La requête doit contenir les champs "nomProjet", "utilisateur" et "droit"
    @ResponseBody
    @RequestMapping(value="/newUserProject", method = RequestMethod.POST, produces = "application/json")
    public String newUserProject(HttpServletRequest request, ModelMap model){
        
        // On créé l'objet à retourner
        JSONObject returnObject = new JSONObject();

        // Récupération des paramètres
        String nomProjet = (String)request.getParameter("nomProjet");
        String pseudo = (String)request.getParameter("utilisateur");
        String droit = (String)request.getParameter("droit");
        if(nomProjet == null || pseudo == null || droit == null){
            try{
                returnObject.put("response", "false");
                returnObject.put("content", "");
                returnObject.put("errors", "Parametres manquants");
                return returnObject.toString();
            }
            // JSon Fail
            catch(Exception e){return null;}
        }
        
         // On cherche le projet et l'utilisateur dans la bdd
        Projet projet;
        User user;
        try{
            projet = projetDao.getProjetByName(nomProjet);
            user = userDao.getUserByPseudo(pseudo);
         
            if(projet == null || user == null){
                try{
                    returnObject.put("response", "false");
                    returnObject.put("content", "");
                    returnObject.put("errors", "Erreur pendant la récupération du projet ou de l'utilisateur");
                    return returnObject.toString();
                }
                // Json Fail
                catch(Exception e2){return null;} 
            }
        }
        catch(Exception e){
            try{
                returnObject.put("response", "false");
                returnObject.put("content", "");
                returnObject.put("errors", "Erreur pendant la récupération du projet ou de l'utilisateur");
                return returnObject.toString();
            }
            // Json Fail
            catch(Exception e2){return null;}  
        }
        
        // On ajoute l'utilisateur au projet
        try{
            UserProjet userProjet = userProjetDao.createNewUserProjet(droit, new Date(), new Date(), user, projet);
            if(userProjet == null){
                try{
                    returnObject.put("response", "false");
                    returnObject.put("content", "");
                    returnObject.put("errors", "Erreur pendant l'assignation de l'utilisateur au projet");
                    return returnObject.toString();
                }
                // Json Fail
                catch(Exception e2){return null;} 
            }
        }
        catch(Exception e){
            try{
                returnObject.put("response", "false");
                returnObject.put("content", "");
                returnObject.put("errors", "Erreur pendant l'assignation de l'utilisateur au projet");
                return returnObject.toString();
            }
            // Json Fail
            catch(Exception e2){return null;}  
        }
        
        // Réussite
        try{
            returnObject.put("response", "true");
            returnObject.put("content", "");
            returnObject.put("errors", "");
            return returnObject.toString();
        }
        // Json Fail
        catch(Exception e2){return null;}  
    }
    
    
    // Suppression d'un utilisateur d'un projet
    // - La requête doit contenir les champs "nomProjet" et "utilisateur"
    @ResponseBody
    @RequestMapping(value="/removeUserProject", method = RequestMethod.POST, produces = "application/json")
    public String removeUserProject(HttpServletRequest request, ModelMap model){
        
        // On créé l'objet à retourner
        JSONObject returnObject = new JSONObject();

        // Récupération des paramètres
        String nomProjet = (String)request.getParameter("nomProjet");
        String pseudo = (String)request.getParameter("utilisateur");
        if(nomProjet == null || pseudo == null){
            try{
                returnObject.put("response", "false");
                returnObject.put("content", "");
                returnObject.put("errors", "Parametres manquants");
                return returnObject.toString();
            }
            // JSon Fail
            catch(Exception e){return null;}
        }
        
         // On cherche le projet et l'utilisateur dans la bdd. On vérifie que l'utilisateur est bien assigné au projet
        Projet projet;
        User user;
        UserProjet userProjet;
        try{
            projet = projetDao.getProjetByName(nomProjet);
            user = userDao.getUserByPseudo(pseudo);
         
            if(projet == null || user == null){
                try{
                    returnObject.put("response", "false");
                    returnObject.put("content", "");
                    returnObject.put("errors", "Erreur pendant la récupération du projet ou de l'utilisateur");
                    return returnObject.toString();
                }
                // Json Fail
                catch(Exception e2){return null;} 
            }
            else{
                userProjet = userProjetDao.getUserProjetByUIdPId(user.getIdUser(), projet.getIdProjet());
                if(userProjet == null){
                    try{
                    returnObject.put("response", "false");
                    returnObject.put("content", "");
                    returnObject.put("errors", "Cet utilisateur n'etait pas assigne a ce projet");
                    return returnObject.toString();
                    }
                    // Json Fail
                    catch(Exception e2){return null;} 
                }
            }
        }
        catch(Exception e){
            try{
                returnObject.put("response", "false");
                returnObject.put("content", "");
                returnObject.put("errors", "Erreur pendant la récupération du projet ou de l'utilisateur");
                return returnObject.toString();
            }
            // Json Fail
            catch(Exception e2){return null;}  
        }
        
        // On supprime l'utilisateur du projet
        try{
            Boolean res = userProjetDao.deleteUserProjet(user, projet);
            if(res == false){
                try{
                    returnObject.put("response", "false");
                    returnObject.put("content", "");
                    returnObject.put("errors", "Erreur pendant la suppression de l'utilisateur du projet");
                    return returnObject.toString();
                }
                // Json Fail
                catch(Exception e2){return null;} 
            }
        }
        catch(Exception e){
            try{
                returnObject.put("response", "false");
                returnObject.put("content", "");
                returnObject.put("errors", "Erreur pendant la suppression de l'utilisateur du projet");
                return returnObject.toString();
            }
            // Json Fail
            catch(Exception e2){return null;}  
        }
        
        // Réussite
        try{
            returnObject.put("response", "true");
            returnObject.put("content", "");
            returnObject.put("errors", "");
            return returnObject.toString();
        }
        // Json Fail
        catch(Exception e2){return null;}  
    }
    
    
    // Changement des droits d'un utilisateur d'un projet
    // - La requête doit contenir les champs "nomProjet", "utilisateur" et "droits"
    @ResponseBody
    @RequestMapping(value="/changeRightsUserProject", method = RequestMethod.POST, produces = "application/json")
    public String changeRightsUserProject(HttpServletRequest request, ModelMap model){
        
        // On créé l'objet à retourner
        JSONObject returnObject = new JSONObject();

        // Récupération des paramètres
        String nomProjet = (String)request.getParameter("nomProjet");
        String pseudo = (String)request.getParameter("utilisateur");
        String droit = (String)request.getParameter("droit");
        if(nomProjet == null || pseudo == null || droit == null){
            try{
                returnObject.put("response", "false");
                returnObject.put("content", "");
                returnObject.put("errors", "Parametres manquants");
                return returnObject.toString();
            }
            // JSon Fail
            catch(Exception e){return null;}
        }
        
         // On cherche le projet et l'utilisateur dans la bdd. On vérifie que l'utilisateur est bien assigné au projet
        Projet projet;
        User user;
        UserProjet userProjet;
        try{
            projet = projetDao.getProjetByName(nomProjet);
            user = userDao.getUserByPseudo(pseudo);
         
            if(projet == null || user == null){
                try{
                    returnObject.put("response", "false");
                    returnObject.put("content", "");
                    returnObject.put("errors", "Erreur pendant la récupération du projet ou de l'utilisateur");
                    return returnObject.toString();
                }
                // Json Fail
                catch(Exception e2){return null;} 
            }
            else{
                userProjet = userProjetDao.getUserProjetByUIdPId(user.getIdUser(), projet.getIdProjet());
                if(userProjet == null){
                    try{
                    returnObject.put("response", "false");
                    returnObject.put("content", "");
                    returnObject.put("errors", "Cet utilisateur n'etait pas assigne a ce projet");
                    return returnObject.toString();
                    }
                    // Json Fail
                    catch(Exception e2){return null;} 
                }
            }
        }
        catch(Exception e){
            try{
                returnObject.put("response", "false");
                returnObject.put("content", "");
                returnObject.put("errors", "Erreur pendant la récupération du projet ou de l'utilisateur");
                return returnObject.toString();
            }
            // Json Fail
            catch(Exception e2){return null;}  
        }
        
        // On change les droits de l'utilisateur du projet
        try{
            Boolean res = userProjetDao.changeDroitsUserProjet(droit, userProjet);
            if(res == false){
                try{
                    returnObject.put("response", "false");
                    returnObject.put("content", "");
                    returnObject.put("errors", "Erreur pendant le changements des droits");
                    return returnObject.toString();
                }
                // Json Fail
                catch(Exception e2){return null;} 
            }
        }
        catch(Exception e){
            try{
                returnObject.put("response", "false");
                returnObject.put("content", "");
                returnObject.put("errors", "Erreur pendant le changements des droits");
                return returnObject.toString();
            }
            // Json Fail
            catch(Exception e2){return null;}  
        }
        
        // Réussite
        try{
            returnObject.put("response", "true");
            returnObject.put("content", "");
            returnObject.put("errors", "");
            return returnObject.toString();
        }
        // Json Fail
        catch(Exception e2){return null;}  
    }
    
}
