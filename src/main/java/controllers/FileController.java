package controllers;

import dao.FichierUserDao;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.UUID;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import models.FichiersUsers;
import models.User;
import org.json.JSONException;
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
    
    /**
     * Constructeur du controleur de fichiers
     */
    public FileController(){
    }
    
    private String extractFileName(String path) {
        String filename = path.substring(path.lastIndexOf("/")+1); 
        return filename;
    }
    
    /**
     * Requête d'affichage de la pop-up de création de fichiers
     * @param request
     * @param model
     * @return Renvoie le nom de la jsp à afficher.
     */
    @RequestMapping(value="/newFile", method = RequestMethod.GET)
    public String index(HttpServletRequest request, ModelMap model){
        return "newFile";
    }

    /**
     * Requête d'affichage de la pop-up de création de dossiers
     * @param request
     * @param model
     * @return Renvoie le nom de la jsp à afficher
     */
    @RequestMapping(value="/newDossier", method = RequestMethod.GET)
    public String indexD(HttpServletRequest request, ModelMap model){
        return "newDossier";
    }
    
    /**
     * Requête d'affichage de la pop-up de renommage de fichiers
     * @param request
     * @param model
     * @return Renvoie le nom de la jsp à afficher
     */
    @RequestMapping(value="/renameFile", method = RequestMethod.GET)
    public String renameFile(HttpServletRequest request, ModelMap model){
        return "renameFile";
    }
    
    /**
     * Requête de création d'un fichier vide
     * @param request
     * @param model
     * @return Renvoie un Json avec les clés "response" et "errors".
     */
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
                                
                                FileOutputStream out = new FileOutputStream(path + "/../../files/" + idOne.toString());
                                out.close();
                            }
                            catch(Exception e){
                                System.out.println("Erreur pendant la création physique d'un fichier: " + e);
                                returnObject.put("errors",e.getMessage());
                                return returnObject.toString();
                            }
                            
                            returnObject.put("response", true);
                            return returnObject.toString();
                        }
                        catch(Exception e){
                            System.out.println("Erreur pendant la création physique d'un fichier: " + e);
                            returnObject.put("errors","Erreur BDD");
                            return returnObject.toString();
                        }
                    }
                }
            }
        }
        catch(Exception e){
            System.out.println("Erreur JSON");
            System.out.println(e);
            return null;
        }
    }
    
    /**
     * Requête de création d'un nouveau dossier
     * @param request
     * @param model
     * @return Renvoie un Json avec les clés "response", "content" et "errors".
     */
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
            catch(Exception e){System.out.println("Erreur JSON: " + e);return null;}
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
            catch(Exception e){System.out.println("Erreur JSON: " + e);return null;}
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
                try{
                    returnObject.put("response", "false");
                    returnObject.put("content", "");
                    returnObject.put("errors", "Erreur pendant la création du dossier");
                    return returnObject.toString();
                }
                // JSon fail
                catch(Exception e2){System.out.println("Erreur JSON: " + e2);return null;}
            }
        }
        catch(Exception e){
            try{
                System.out.println("Erreur pendant la création d'un dossier: " + e);
                returnObject.put("response", "false");
                returnObject.put("content", "");
                returnObject.put("errors", "Erreur pendant la création du dossier");
                return returnObject.toString();
            }
            // JSon fail
            catch(Exception e2){System.out.println("Erreur JSON: " + e2);return null;}
        }
        
        // Réussite
        try{
            returnObject.put("response", "true");
            returnObject.put("content", "");
            returnObject.put("errors", "");
            return returnObject.toString();
        }
        // Json fail
        catch(Exception e){System.out.println("Erreur JSON: " + e); return null;}
    }
        
    /**
     * Requête d'enregistrement d'un fichier
     * La requête doit contenir les champs "pathFichier" et "contenuFichier"
     * @param request
     * @param model
     * @return Renvoie un Json avec les champs "response" et "errors".
     */
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
                        
                        // Enregistrement du fichier sur le disque ici
                        try{
                            ServletContext ctx = request.getServletContext();
                            String path = ctx.getRealPath("/");
                            
                            FileOutputStream out = new FileOutputStream(path + "/../../files/" + fichier.getNomPhysique());
                            out.write(contenuFichier.getBytes());
                            out.close();
                        }
                        catch(Exception e){
                            System.out.println("Erreur pendant l'enregistrement sur le serveur: " + e);
                            returnObject.put("errors","Erreur pendant l'enregistrement sur le serveur");
                            return returnObject.toString();
                        }

                        try{
                            returnObject.put("response",true);
                            return returnObject.toString();
                        }
                        catch(Exception e){
                            System.out.println("Erreur BDD: " + e);
                            returnObject.put("errors","Erreur BDD");
                            return returnObject.toString();
                        }
                    }
                }
            }
        }
        catch(JSONException e){
            System.out.println("Erreur JSON");
            System.out.println(e);
            return null;
        }
    }
    
    /**
     * Requête de récupération du contenu d'un fichier
     * @param request
     * @return Renvoie un Json avec les champs "pathLogique", "pathPhysique", "content" et "errors"
     */
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
            catch (Exception e){System.out.println("Erreur JSON: " + e);return null;}
        }      
          
        // Récupération du path physique
        String pathPhysique = file.getNomPhysique();
        if(pathPhysique == null){
            try{
                response.put("pathLogique","");
                response.put("pathPhysique","");
                response.put("content","");
                response.put("errors", "Erreur dans la récupération du path physique d'un fichier");
                return response.toString();
            }		
            // Json fail
            catch (Exception e){System.out.println("Erreur JSON: " + e); return null;}
        }
        
        try{
            response.put("pathLogique",request.getParameter("pathLogique"));
            response.put("pathPhysique",pathPhysique);
            response.put("content","");
        }		
        // Json fail
        catch (Exception e){System.out.println("Erreur JSON: " + e); return null;}

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
        catch (IOException | JSONException e){
            System.out.println("Erreur pendant la récupération du contenu du fichier: " + e);
            return e.toString();
        }
        return  response.toString();
    }
}
