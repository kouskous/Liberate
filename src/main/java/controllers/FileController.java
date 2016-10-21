/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import dao.FichierUserDao;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import models.FichiersUsers;
import models.User;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Kilian
 */
//(String)request.getParameter("pathFile");

@Controller
public class FileController {
    EntityManager em;
    FichierUserDao fichierUserDao;
    
    public FileController(){
        
    }
    
    private String extractFileName(String path) {
        String filename = path.substring(path.lastIndexOf("/")+1); 
        return filename;
    }
    
    @RequestMapping(value="/newFichier", method = RequestMethod.GET)
    public String index(HttpServletRequest request, ModelMap model){
        return "newFichier";
    }
    
    @RequestMapping(value="/saveFichier", method = RequestMethod.GET)
    public String indexSave(HttpServletRequest request, ModelMap model){
        return "saveFichier";
    }
    
    // Création d'un fichier vide
    // Renvoie un Json avec clés "response" et "errors"
    // - response contient true si réussite
    // - errors contient retour d'erreur si echec
    // - renvoie null si erreur Json
    @ResponseBody 
    @RequestMapping(value="/newFichier", method = RequestMethod.POST, produces = "application/json")
    public String newFile(HttpServletRequest request, ModelMap model){           
            
        em = Persistence.createEntityManagerFactory("persistenceUnitLiber8").createEntityManager();
        fichierUserDao = new FichierUserDao(em);
        
        // On créé l'objet à retourner
        JSONObject returnObject = new JSONObject();   
        
        try{
            returnObject.put("response", "");
            returnObject.put("errors", "");
            
            // On vérifie qu'une session est bien ouverte
            HttpSession session= request.getSession();
            User user = (User)session.getAttribute("user");

            if(user == null){
                returnObject.put("errors", "No user");
                return returnObject.toString();
            }
            else{
                String pathFichier = (String)request.getParameter("pathFichier");
                
                if(pathFichier == null){
                    returnObject.put("errors", "No filepath");
                    return returnObject.toString();
                }
                else{
                    em.getTransaction().begin();
                    
                    String fileName = extractFileName((String)request.getParameter("pathFichier"));
                    
                    FichiersUsers newFile = fichierUserDao.createNewFichierUser((String)request.getParameter("pathFichier"), 
                    fileName, 
                    fileName, 
                    new Date(), true, user);
                    
                    if(newFile == null){
                        returnObject.put("errors", "Failed to create file");
                        return returnObject.toString();
                    }
                    else{
                        try{
                            em.persist(newFile);
                            em.getTransaction().commit();
                            em.close();
                            
                            try{                          
                                ServletContext ctx = request.getServletContext();
                                String path = ctx.getRealPath("/");
                                
                                // TODO: change this path when deploying to server
                                FileOutputStream out = new FileOutputStream(path + "/../" + fileName);
                            }
                            catch(Exception e){
                                returnObject.put("response",e.getMessage());
                                return returnObject.toString();
                            }
                            
                            returnObject.put("response",true);
                            return returnObject.toString();
                        }
                        catch(Exception e){
                            em.close();
                            returnObject.put("errors","Erreur BDD");
                            return returnObject.toString();
                        }
                    }
                }
            }
        }
        catch(Exception e){
            System.out.println("Erreur JSON");
            System.out.println(e.getMessage());
            
            //TODO: ce try-catch ne sert qu'à afficher les erreurs
            try{
                JSONObject obj = new JSONObject();
                obj.put("errors",e.getMessage());
                return obj.toString();
            }
            catch(Exception er){
                
            }
            return null;
        }
    }
        
    // Enregistre un fichier
    // Renvoie un Json avec clé response et errors
    // - reponse contient true sur réussite
    // - errors contient retour d'erreur si echec
    // - renvoie null si erreur avec le JSON
    @ResponseBody 
    @RequestMapping(value="/saveFichier", method = RequestMethod.POST, produces = "application/json")
    public String saveFile(HttpServletRequest request, ModelMap model){
        
        em = Persistence.createEntityManagerFactory("persistenceUnitLiber8").createEntityManager();
        fichierUserDao = new FichierUserDao(em);
        
        // On créé l'objet à retourner
        JSONObject returnObject = new JSONObject();   
        
        try{       
            returnObject.put("response", "");
            returnObject.put("errors", "");
            
            String pathFichier = (String)request.getParameter("pathFichier");
            String fileName = extractFileName((String)request.getParameter("pathFichier"));
            String contenuFichier = (String)request.getParameter("contenuFichier");
            
            // On vérifie les paramètres de la requête
            if(pathFichier == null || contenuFichier == null){ 
                returnObject.put("errors","No filepath or content");
                return returnObject.toString();
            }
            else{
                // Récupération de l'utilisateur
                HttpSession session= request.getSession();
                User user = (User)session.getAttribute("user");
                
                // On vérifie l'utilisateur
                if(user == null){
                    returnObject.put("errors","No user");
                    return returnObject.toString();
                }
                else{
                    em.getTransaction().begin();
                
                    // On trouve le fichier à enregistrer dans la BDD
                    FichiersUsers fichier = fichierUserDao.getFichiersByUserAndPath(user, pathFichier);
                    
                    if(fichier == null){ // Le fichier n'a pas été trouvé
                        returnObject.put("errors","File not found");
                        return returnObject.toString();
                    }
                    else{
                        // Mise à jour date
                        fichier.setDateCreation(new Date());
                        
                        // Enregistrement du fichier sur le disque ici
                        try{
                            ServletContext ctx = request.getServletContext();
                            String path = ctx.getRealPath("/");
                            
                            FileOutputStream out = new FileOutputStream(path + "/../" + fileName);
                            out.write(contenuFichier.getBytes());
                        }
                        catch(Exception e){
                            returnObject.put("errors","Erreur pendant l'enregistrement sur le serveur");
                            return returnObject.toString();
                        }

                        try{
                            em.persist(fichier);
                            em.close();
                            returnObject.put("response",true);
                            return returnObject.toString();
                        }
                        catch(Exception e){
                            em.close();
                            returnObject.put("errors","Erreur BDD");
                            return returnObject.toString();
                        }
                    }
                }
            }
        }
        catch(Exception e){
            System.out.println("Erreur JSON");
            System.out.println(e.getMessage());
            
            //TODO: ce try-catch ne sert qu'à afficher les erreurs
            try{
                JSONObject obj = new JSONObject();
                obj.put("errors",e.getMessage());
                return obj.toString();
            }
            catch(Exception er){
                
            }
            
            return null;
        }
    }

    
    
    
}
