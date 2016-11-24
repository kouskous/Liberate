/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import dao.FichierUserDao;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import models.FichiersUsers;
import models.User;
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
 * @author Kilian & Luc Di Sanza
 */

@Controller
public class FileController {
    
    @Autowired
    FichierUserDao fichierUserDao;
    
    public FileController(){
    }
    
    private String extractFileName(String path) {
        String filename = path.substring(path.lastIndexOf("/")+1); 
        return filename;
    }
    
    @RequestMapping(value="/newFile", method = RequestMethod.GET)
    public String index(HttpServletRequest request, ModelMap model){
        return "newFile";
    }
    @RequestMapping(value="/newDossier", method = RequestMethod.GET)
    public String indexD(HttpServletRequest request, ModelMap model){
        return "newDossier";
    }
    @RequestMapping(value="/saveFile", method = RequestMethod.GET)
    public String indexSave(HttpServletRequest request, ModelMap model){
        return "saveFile";
    }
    
   /* @RequestMapping(value="/renameFile", method = RequestMethod.GET)
    public String renameFile(HttpServletRequest request, ModelMap model){
        return "renameFile";
    }*/
    
    // Création d'un fichier vide
    // Création d'un fichier
    // - Nécessite le champs "pathFichier" dans la requête
    //
    // Renvoie un Json avec clés "response" et "errors"
    // - response contient true si réussite
    // - errors contient retour d'erreur si echec
    // - renvoie null si erreur Json
    @ResponseBody 
    @RequestMapping(value="/newFile", method = RequestMethod.POST, produces = "application/json")
    public String newFile(HttpServletRequest request, ModelMap model){           
        
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
                    // Nom du fichier
                    String fileName = extractFileName((String)request.getParameter("pathFichier"));  
                    
                    // Génération du path physique
                    UUID idOne = UUID.randomUUID();

                    FichiersUsers newFile = fichierUserDao.createNewFichierUser((String)request.getParameter("pathFichier"), 
                    idOne.toString(), 
                    fileName, 
                    new Date(), FichiersUsers.Type.FICHIER, user, 2);
                    
                    if(newFile == null){
                        returnObject.put("errors", "Failed to create file");
                        return returnObject.toString();
                    }
                    else{
                        try{

                            try{                          
                                ServletContext ctx = request.getServletContext();
                                String path = ctx.getRealPath("/");
                                
                                // TODO: change this path when deploying to server
                                FileOutputStream out = new FileOutputStream(path + "/../../files/" + idOne.toString());
                            }
                            catch(Exception e){
                                returnObject.put("response",e.getMessage());
                                return returnObject.toString();
                            }
                            
                            returnObject.put("response",true);
                            return returnObject.toString();
                        }
                        catch(Exception e){
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
    
    @ResponseBody 
    @RequestMapping(value="/newDossier", method = RequestMethod.POST, produces = "application/json")
    public String newDossier(HttpServletRequest request, ModelMap model){           
        
        // On créé l'objet à retourner
        JSONObject returnObject = new JSONObject();   
        
        // On vérifie qu'une session est bien ouverte
        HttpSession session= request.getSession();
        User user = (User)session.getAttribute("user");
        
        if(user == null){
            try{
                returnObject.put("response", "false");
                returnObject.put("content", "");
                returnObject.put("errors", "No user");
                return returnObject.toString();
            }
            // JSon fail
            catch(Exception e){return null;}
        }
        
        // Extraction du nom de dossier
        String fileName = extractFileName((String)request.getParameter("pathDossier"));
        
         // Génération du path physique
        UUID idOne = UUID.randomUUID();
        
        if(fileName == null){
            try{
                returnObject.put("response", "false");
                returnObject.put("content", "");
                returnObject.put("errors", "Erreur pendant la récupération du nom de dossier");
                return returnObject.toString();
            }
            // JSon fail
            catch(Exception e){return null;}
        }
        
        // Création du dossier
        try{
            FichiersUsers newFile = fichierUserDao.createNewFichierUser((String)request.getParameter("pathDossier"), 
            idOne.toString(), 
            fileName, 
            new Date(), 
            FichiersUsers.Type.DOSSIER, 
            user, 4);
            
            if(newFile == null){
                throw new Exception("Erreur pendant la création du dossier");
            }
        }
        catch(Exception e){
            try{
                returnObject.put("response", "false");
                returnObject.put("content", "");
                returnObject.put("errors", "Erreur pendant la création du dossier");
                return returnObject.toString();
            }
            // JSon fail
            catch(Exception e2){return null;}
        }
        
        // Réussite
        try{
            returnObject.put("response", "true");
            returnObject.put("content", "");
            returnObject.put("errors", "");
            return returnObject.toString();
        }
        // Json fail
        catch(Exception e){return null;}
    }
        
    // Enregistre un fichier
    // - Nécessite les champs "pathFichier" et "contenuFichier" dans la requête
    //
    // Renvoie un Json avec clé response et errors
    // - reponse contient true sur réussite
    // - errors contient retour d'erreur si echec
    // - renvoie null si erreur avec le JSON
    @ResponseBody 
    @RequestMapping(value="/saveFile", method = RequestMethod.POST, produces = "application/json")
    public String saveFile(HttpServletRequest request, ModelMap model){
        
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
                
                    // On trouve le fichier à enregistrer dans la BDD
                    FichiersUsers fichier = fichierUserDao.getFichiersByUserAndPath(user, pathFichier);
                    
                    if(fichier == null){ // Le fichier n'a pas été trouvé
                        returnObject.put("errors","File not found");
                        return returnObject.toString();
                    }
                    else{
                        // Mise à jour date
                        fichier.setDateCreation(new Date());
                        
                        // TODO: enregistrement bdd nouvelle date ici
                        
                        // Enregistrement du fichier sur le disque ici
                        try{
                            ServletContext ctx = request.getServletContext();
                            String path = ctx.getRealPath("/");
                            
                            FileOutputStream out = new FileOutputStream(path + "/../../files/" + fichier.getNomPhysique());
                            out.write(contenuFichier.getBytes());
                        }
                        catch(Exception e){
                            returnObject.put("errors","Erreur pendant l'enregistrement sur le serveur");
                            return returnObject.toString();
                        }

                        try{
                            returnObject.put("response",true);
                            return returnObject.toString();
                        }
                        catch(Exception e){
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
    
    @ResponseBody
    @RequestMapping(value="/getFile", method = RequestMethod.POST,produces = "application/json")
    public String contentFile(HttpServletRequest request){
        
        // On vérifie qu'une session est ouverte
        HttpSession session= request.getSession();
        User user = (User)session.getAttribute("user");

        // Pas de session ouverte
        if(user == null) return "redirect:/login";
        
        // Objet réponse
        JSONObject response = new JSONObject();
        
        FichiersUsers file = fichierUserDao.getPathByPathLogique(user,request.getParameter("pathLogique"));
        if(file == null){
            try{
                response.put("pathLogique","");
                response.put("pathPhysique","");
                response.put("content","");
                response.put("errors", "Erreur dans la récupération du contenu d'un fichier");
                return response.toString();
            }
            // Json Fail
            catch (Exception e){return null;}
        }      
          
        // Récupération du path physique
        String pathPhysique =file.getNomPhysique();
        if(pathPhysique == null){
            try{
                response.put("pathLogique","");
                response.put("pathPhysique","");
                response.put("content","");
                response.put("errors", "Erreur dans la récupération du path physique d'un fichier");
                return response.toString();
            }		
            // Json fail
            catch (Exception e){System.out.println(e.toString()); return null;}
        }
        
        if(pathPhysique != null){
            try{
                response.put("pathLogique",request.getParameter("pathLogique"));
                response.put("pathPhysique",pathPhysique);
                response.put("content","");
            }		
            // Json fail
            catch (Exception e){System.out.println(e.toString()); return null;}
            
            // Récupération du contenu du fichier
            try{
                ServletContext ctx = request.getServletContext();
                String path = ctx.getRealPath("/");
                InputStream flux=new FileInputStream(path+"/../../files/" + pathPhysique); 
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
    
    
    // Suppression d'un fichier
    // - Nécessite le champs "pathFichier" dans la requête
    //
    // Renvoie un Json avec clés "response" et "errors"
    // - response contient true si réussite
    // - errors contient retour d'erreur si echec
    // - renvoie null si erreur Json
    @ResponseBody 
    @RequestMapping(value="/deleteFile", method = RequestMethod.POST, produces = "application/json")
    public String deleteFile(HttpServletRequest request, ModelMap model){           
        
        // On créé l'objet à retourner
        JSONObject returnObject = new JSONObject();   
        
        // Récupération de la session de l'utilisateur
        HttpSession session= request.getSession();
        User user = (User)session.getAttribute("user");

        if(user == null){
            try{
                returnObject.put("response", "false");
                returnObject.put("content","");
                returnObject.put("errors", "No user");
                return returnObject.toString();
            }
            // Json fail
            catch(Exception e){System.out.println(e.getMessage()); return null;}
        }

        // Récupération paramètre pathFichier
        String pathFichier = (String)request.getParameter("pathFichier");
        if(pathFichier == null){
            try{
                returnObject.put("response", "false");
                returnObject.put("content","");
                returnObject.put("errors", "Pas de chemin de fichier indiqué");
                return returnObject.toString();
            }
            // Json fail
            catch(Exception e){System.out.println(e.getMessage()); return null;}
        }
        
        // Récupération du fichier en base
        FichiersUsers fichier;
        try{
            fichier = fichierUserDao.getFichiersByUserAndPath(user, pathFichier);
            if (fichier == null){
                try{
                returnObject.put("response", "false");
                returnObject.put("content","");
                returnObject.put("errors", "Le fichier n'a pas été trouvé en base de données");
                return returnObject.toString();
            }
            // Json fail
            catch(Exception e2){System.out.println(e2.getMessage()); return null;}
            }
        }
        catch(Exception e){
            try{
                returnObject.put("response", "false");
                returnObject.put("content","");
                returnObject.put("errors", "Une erreur est survenue pendant la récupération du fichier en base");
                return returnObject.toString();
            }
            // Json fail
            catch(Exception e2){System.out.println(e2.getMessage()); return null;}
        }
            
        // Suppression physique du fichier
        String nomPhysique = fichier.getNomPhysique();
        ServletContext ctx = request.getServletContext();
        String path = ctx.getRealPath("/");
        try {
           Files.delete(FileSystems.getDefault().getPath(path + "/../../files/", nomPhysique));
        }
        catch(Exception e){
            try{
                returnObject.put("response", "false");
                returnObject.put("content","");
                returnObject.put("errors", "Echec de la suppression du fichier physique");
                return returnObject.toString();
            }
            // Json fail
            catch(Exception e2){System.out.println(e2.getMessage()); return null;}
        }
        
        // Suppression du fichier en base de données
        try{
            if(!fichierUserDao.deleteFichierUserByNomPhysique(nomPhysique)){
                try{
                    returnObject.put("response", "false");
                    returnObject.put("content","");
                    returnObject.put("errors", "Echec de la suppression du fichier en BDD");
                    return returnObject.toString();
                }
                // Json fail
                catch(Exception e2){System.out.println(e2.getMessage()); return null;}
            }
        }
        catch(Exception e){System.out.println(e.getMessage()); return null;}
        
        // Réussite
        try{
            returnObject.put("response", "true");
            returnObject.put("content","");
            returnObject.put("errors", "");
            return returnObject.toString();
        }
        // Json fail
        catch(Exception e2){System.out.println(e2.getMessage()); return null;}
    }
    
    // Rennomage d'un fichier
    // - Nécessite les champs "pathFichier" et "nomFichier" dans la requête
    //
    // Renvoie un Json avec clés "response" et "errors"
    // - response contient true si réussite
    // - errors contient retour d'erreur si echec
    // - renvoie null si erreur Json
    @ResponseBody 
    @RequestMapping(value="/renameFile", method = RequestMethod.GET, produces = "application/json")
    public String renameFile(HttpServletRequest request, ModelMap model){           
        
        // On créé l'objet à retourner
        JSONObject returnObject = new JSONObject();   
        
        // Récupération de la session de l'utilisateur
        HttpSession session= request.getSession();
        User user = (User)session.getAttribute("user");

        if(user == null){
            try{
                returnObject.put("response", "false");
                returnObject.put("content","");
                returnObject.put("errors", "No user");
                return returnObject.toString();
            }
            // Json fail
            catch(Exception e){System.out.println(e.getMessage()); return null;}
        }

        // Récupération paramètres pathFichier et nomFichier
        String pathFichier = (String)request.getParameter("pathFichier");
        String nomFichier = (String)request.getParameter("nomFichier");
        if(pathFichier == null || nomFichier == null){
            try{
                returnObject.put("response", "false");
                returnObject.put("content","");
                returnObject.put("errors", "Pas de chemin de fichier indiqué ou pas de nom");
                return returnObject.toString();
            }
            // Json fail
            catch(Exception e){System.out.println(e.getMessage()); return null;}
        }
        
        // Récupération du fichier en base
        FichiersUsers fichier;
        try{
            fichier = fichierUserDao.getFichiersByUserAndPath(user, pathFichier);
            if (fichier == null){
                try{
                returnObject.put("response", "false");
                returnObject.put("content","");
                returnObject.put("errors", "Le fichier n'a pas été trouvé en base de données");
                return returnObject.toString();
            }
            // Json fail
            catch(Exception e2){System.out.println(e2.getMessage()); return null;}
            }
        }
        catch(Exception e){
            try{
                returnObject.put("response", "false");
                returnObject.put("content","");
                returnObject.put("errors", "Une erreur est survenue pendant la récupération du fichier en base");
                return returnObject.toString();
            }
            // Json fail
            catch(Exception e2){System.out.println(e2.getMessage()); return null;}
        }
            
        // Renommage du fichier
        if(!fichierUserDao.renameFichier(fichier, nomFichier)){
            try{
                returnObject.put("response", "false");
                returnObject.put("content","");
                returnObject.put("errors", "Une erreur est survenue pendant le renommage du fichier");
                return returnObject.toString();
            }
            // Json fail
            catch(Exception e2){System.out.println(e2.getMessage()); return null;}
        }
        
        // Réussite
        try{
            returnObject.put("response", "true");
            returnObject.put("content","");
            returnObject.put("errors", "");
            return returnObject.toString();
        }
        // Json fail
        catch(Exception e2){System.out.println(e2.getMessage()); return null;}
    }
    
}
