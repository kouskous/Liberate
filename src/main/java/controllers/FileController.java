/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import dao.FichierUserDao;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import models.FichiersUsers;
import models.User;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
        em = Persistence.createEntityManagerFactory("persistenceUnitLiber8").createEntityManager();
        fichierUserDao = new FichierUserDao(em);
    }
    
    private String extractFileName(String path) {
        String filename = path.substring(path.lastIndexOf("/")+1); 
        return filename;
    }
    
    @RequestMapping(value="/newFichier", method = RequestMethod.GET)
    public String index(HttpServletRequest request, ModelMap model){
        return "newFichier";
    }
    
    @RequestMapping(value="/newFichier", method = RequestMethod.POST)
    public String newFile(HttpServletRequest request, ModelMap model){
        Date d = new Date();

        // On vérifie si une session est déjà ouverte
        HttpSession session= request.getSession();
        User user = (User)session.getAttribute("user");
 
        em.getTransaction().begin();
        
        if(user == null) // Pas de session ouverte
            return "redirect:/login";
        else {// Une session déjà ouverte
            FichiersUsers newFile = fichierUserDao.createNewFichierUser((String)request.getParameter("pathFichier"), 
                    extractFileName((String)request.getParameter("pathFichier")), 
                    extractFileName((String)request.getParameter("pathFichier")), 
                    d, 
                    true, 
                    user);
            em.getTransaction().commit();
            em.close();
            
            return "newFichier";
        }
    }
        
    // Enregistre un fichier
    // Renvoie un Json avec clé response et errors
    // - reponse contient true sur réussite
    // - errors contient retour d'erreur si echec
    // - renvoie null si erreur avec le JSON
    @RequestMapping(value="/saveFile", method = RequestMethod.POST, produces = "application/json")
    public String saveFile(HttpServletRequest request, ModelMap model){
        
        // On créé l'objet à retourner
        JSONObject returnObject = new JSONObject();   
        
        try{       
            returnObject.put("response", "");
            returnObject.put("errors", "");
            
            String pathFichier = (String)request.getParameter("pathFichier");
            String contenuFichier = (String)request.getParameter("contenuFichier");
            
            // On vérifie les paramètres de la requête
            if(pathFichier == null || contenuFichier == null){ 
                returnObject.put("errors","Empty parameter");
                return returnObject.toString();
            }
            else{
                // Récupération de l'utilisateur
                HttpSession session= request.getSession();
                User user = (User)session.getAttribute("user");
                
                // On vérifie l'utilisateur
                if(user == null){
                    returnObject.put("errors","Empty user");
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
                        
                        // TODO: le mettre à jour sur le disque ici

                        try{
                            em.persist(fichier);
                            em.close();
                            returnObject.put("response","true");
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
            return null;
        }
    }

    
    
    
}
