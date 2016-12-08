package controllers;

import dao.FichierUserDao;
import dao.VersionDao;
import dao.ProjetDao;
import dao.FichiersVersionDao;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Base64;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
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
import models.FichiersVersion;
import models.Projet;
import models.User;
import org.json.JSONArray;
import models.Version;
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
    @Autowired
    VersionDao versionDao;
    @Autowired
    ProjetDao projetDao;
    @Autowired
    FichiersVersionDao fichiersVersionDao;
    
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

                            //TODO: fermer le FileOutputStream
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
    
    
    // Suppression d'un fichier
    // - Nécessite le champs "pathFichier" dans la requête
    //
    // Renvoie un Json avec clés "response" et "errors"
    // - response contient true si réussite
    // - errors contient retour d'erreur si echec
    // - renvoie null si erreur Json
    @ResponseBody 
    @RequestMapping(value="/removeFile", method = RequestMethod.POST, produces = "application/json")
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
    @RequestMapping(value="/renameFile", method = RequestMethod.POST, produces = "application/json")
    public String renameFileP(HttpServletRequest request, ModelMap model){           
        
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
        String nomFichier = (String)request.getParameter("filename");
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
    
    private static boolean copier(HttpServletRequest request,String fichier_source, String fichier_dest)
    { 
        try{
            ServletContext ctx = request.getServletContext();
            String path = ctx.getRealPath("/");
            FileInputStream src = new FileInputStream(path +fichier_source);
           FileOutputStream dest = new FileOutputStream(path +fichier_dest);

           FileChannel inChannel = src.getChannel();
           FileChannel outChannel = dest.getChannel();

           for (ByteBuffer buffer = ByteBuffer.allocate(1024*1024);
                inChannel.read(buffer) != -1;
                buffer.clear()) {
              buffer.flip();
              while (buffer.hasRemaining()) outChannel.write(buffer);
           }

           inChannel.close();
           outChannel.close();
           return true;
        }catch(Exception e){
            return false;
            }
    }
    
        
        private boolean creerFichier(HttpServletRequest request,String idOne){
            try{                          
                ServletContext ctx = request.getServletContext();
                String path = ctx.getRealPath("/");
                                
                // TODO: change this path when deploying to server
                FileOutputStream out = new FileOutputStream(path + "/../../files/" + idOne.toString());
                return true;            
                }
                            catch(Exception e){
                                return false;
                            }
        }
        
        
    
    @ResponseBody
    @RequestMapping(value="/pushProjet", method = RequestMethod.POST,produces = "application/json")
    public String pusherProjet(HttpServletRequest request) {
  
    // Pas de session ouverte
    JSONObject returnObject = new JSONObject();
    
    
    try{
            returnObject.put("response", "");
            returnObject.put("errors", "");
            
            // On vérifie qu'une session est bien ouverte
            HttpSession session= request.getSession();
            User user = (User)session.getAttribute("user");

            if(user == null){
                System.out.println("user null");
                returnObject.put("response",false);
                returnObject.put("errors", "No user");
                return returnObject.toString();
            }
            else{
                System.out.println("user pas null");
                //Le projet qu'on souhaite pusher
                Projet projet = projetDao.getProjetByName(request.getParameter("projet"));
                System.out.println("user pas null");
                if(projet==null){
                    System.out.println("projet null");
                    returnObject.put("response",false);
                    returnObject.put("errors", "No project with this name");
                    return returnObject.toString();
                }
                System.out.println("projet pas null");
                //La derniere version existante du projet
                Version lastVersion = versionDao.getLastVersionByProjet(projet);
                
                if(lastVersion==null){
                    System.out.println("lastVersion null");
                    Version newVersion = versionDao.createNewVersion(1,new Date(),user,projet,"test");
                    if(newVersion==null){
                        System.out.println("newVersion null");
                        returnObject.put("response",false);
                        returnObject.put("errors", "Erreur lors de la creation de la nouvelle version");
                        return returnObject.toString();
                        }
                    //On recupere la liste des fichierUser pour ce projet puis on les met dans fichiersVersion
                    List<FichiersUsers> filesFromProjet =fichierUserDao.getByUserAndProjet(user, projet.getNom());
                    if(filesFromProjet==null){
                        returnObject.put("response",false);
                        returnObject.put("errors", "Rien a push�");
                        return returnObject.toString();
                    }
                    //Une fois recu on les ajoute a la nouvelle version pis on repasse leur verrou � 0
                    for(int c=0;c<filesFromProjet.size();c++){
                        UUID idOne = UUID.randomUUID();
                       if(filesFromProjet.get(c).getType()==FichiersUsers.Type.FICHIER){
                            FichiersVersion newFichierVersion = fichiersVersionDao.createNewFichierVersion(filesFromProjet.get(c).getPathLogique(),idOne.toString(),filesFromProjet.get(c).getNomReel(),new Date(),FichiersVersion.Type.FICHIER,newVersion);
                            if(newFichierVersion==null){
                                returnObject.put("response",false);
                                returnObject.put("errors", "Le push n'a pas fonctionn�: "+c);
                                return returnObject.toString();
                            }
                            //On cr�e le fichier en physique
                            boolean creation = creerFichier(request,idOne.toString());
                            if(!creation){
                                returnObject.put("response",false);
                                returnObject.put("errors", "Probleme de creation du nouveau fichier de version ");
                                return returnObject.toString();
                            }
                            boolean unlock = fichierUserDao.changeVerrou(filesFromProjet.get(c), 0);
                            if(!unlock){
                                returnObject.put("response",false);
                                returnObject.put("errors", "Probleme de deverouillage lors du push ");
                                return returnObject.toString();
                            }
                            //on copie ensuite le contenu de mes fichiers dans les fichiers de la nouvelle version
                            String src ="/../../files/"+filesFromProjet.get(c).getNomPhysique();
                            String dest="/../../files/"+newFichierVersion.getNomPhysique();
                            copier(request,src,dest);
                       }else{
                           FichiersVersion newDossierVersion = fichiersVersionDao.createNewFichierVersion(filesFromProjet.get(c).getPathLogique(),null,filesFromProjet.get(c).getNomReel(),new Date(),FichiersVersion.Type.DOSSIER,newVersion);
                           if(newDossierVersion==null){
                                returnObject.put("response",false);
                                returnObject.put("errors", "Le push n'a pas fonctionn� pour un dossier :" +newDossierVersion);
                                return returnObject.toString();
                            }
                       }
                       
                    }
                    FichiersVersion newDossierVersion = fichiersVersionDao.createNewFichierVersion("/"+projet.getNom(),null,projet.getNom(),new Date(),FichiersVersion.Type.DOSSIER,newVersion);
                        if(newDossierVersion==null){
                                returnObject.put("response",false);
                                returnObject.put("errors", "Le push n'a pas fonctionn� pour un dossier :" +newDossierVersion);
                                return returnObject.toString();
                        }
                }else{
                       //On recupere tous les fichiers qu'on a verrouill�
                       
                       List<FichiersUsers> lockedFiles = fichierUserDao.getLockedByUserAndProjet(user, projet.getNom());
                       if(lockedFiles==null){
                           System.out.println("lockedFiles null");
                           returnObject.put("response",false);
                           returnObject.put("errors", "Rien a push�");
                           return returnObject.toString();
                       }else{
                           //On recupere les fichiers de la derniere version
                           List<FichiersVersion> fichiersLastVersion = fichiersVersionDao.getFileByVersion(lastVersion);
                           
                           //Puis on cree une nouvelle version
                           Version newVersion = versionDao.createNewVersion(lastVersion.getNumVersion()+1,new Date(),user,projet,"test");
                           if(newVersion==null){
                               System.out.println("newVersion null");
                               returnObject.put("response",false);
                               returnObject.put("errors", "Erreur lors de la creation de la nouvelle version");
                               return returnObject.toString();
                           }
                           for(int i=0;i<lockedFiles.size();i++){
                               UUID idOne = UUID.randomUUID();
                               FichiersVersion file = fichiersVersionDao.createNewFichierVersion(lockedFiles.get(i).getPathLogique(),idOne.toString(),lockedFiles.get(i).getNomReel(), new Date(), FichiersVersion.Type.FICHIER, newVersion);
                               if(file==null){
                                   System.out.println("file null");
                                   returnObject.put("response",false);
                                    returnObject.put("errors", "Erreur lors de la creation du fichier :"+i);
                                    return returnObject.toString();
                                }
                                String src ="/../../files/"+lockedFiles.get(i).getNomPhysique();
                                String dest="/../../files/"+file.getNomPhysique();
                                copier(request,src,dest);
                               List<FichiersVersion> fichiersLastVersionTamp=new ArrayList<FichiersVersion>(fichiersLastVersion);
                               for(int j=fichiersLastVersionTamp.size()-1;j>=0;j--){
                                   if(lockedFiles.get(i).getPathLogique().equals(fichiersLastVersionTamp.get(j).getPathLogique())){
                                       fichiersLastVersion.remove(j);
                                      
                                   }
                               }
                               
                           }
                           
                           for(int g=0;g<fichiersLastVersion.size();g++){
                               UUID idTwo = UUID.randomUUID();
                               if(fichiersLastVersion.get(g).getType()==FichiersVersion.Type.FICHIER){
                               FichiersVersion file = fichiersVersionDao.createNewFichierVersion(fichiersLastVersion.get(g).getPathLogique(),idTwo.toString() ,fichiersLastVersion.get(g).getNomReel(), new Date(), FichiersVersion.Type.FICHIER, newVersion);
                              if(file==null){
                                   System.out.println("file null");
                                   returnObject.put("response",false);
                                    returnObject.put("errors", "Erreur lors de la creation du fichier :"+g);
                                    return returnObject.toString();
                                }
                                String src ="/../../files/"+fichiersLastVersion.get(g).getNomPhysique();
                                String dest="/../../files/"+file.getNomPhysique();
                                copier(request,src,dest);
                           }else{
                                  FichiersVersion file = fichiersVersionDao.createNewFichierVersion(fichiersLastVersion.get(g).getPathLogique(),null,fichiersLastVersion.get(g).getNomReel(), new Date(), FichiersVersion.Type.DOSSIER, newVersion);
                              if(file==null){
                                   System.out.println("file null");
                                   returnObject.put("response",false);
                                    returnObject.put("errors", "Erreur lors de la creation du dossier :"+file);
                                    return returnObject.toString();
                                } 
                               }
                           }
                           //Puis on ajoute les fichers verrouill�s a cette nouvelle version:
                           //dans la base dans le cas de nouveaux fichiers, puis aussi en physique
                           //(c�d copie du contenu des fichiers verrouill�s dans les fichers correpondant dans la version)
                           
                           //On peut ensuite enlever le verrou sur ces fichiers.(seulement pour moi 2-->0)
                           
                           for(int k =0 ;k<lockedFiles.size();k++){
                           boolean test = fichierUserDao.changeVerrou(lockedFiles.get(k),0);
                           if(!test){
                               System.out.println("user null");
                               returnObject.put("response",false);
                               returnObject.put("errors","Le verrou n'a pas �t� relach� pour "+lockedFiles.get(k).getNomPhysique());
                               return returnObject.toString();
                           }
                       }
             
                       }
                   }
                returnObject.put("response", true);
                return returnObject.toString();
            }
        }
        catch(Exception e){
            System.out.println("Erreur JSON");
            System.out.println(e.getMessage());
            
            //TODO: ce try-catch ne sert qu'à afficher les erreurs
            try{
                JSONObject obj = new JSONObject();
                System.out.println("catch");
                obj.put("response",false);
                obj.put("errors",e.getMessage());
                return obj.toString();
            }
            catch(Exception er){
                
            }
            return null;
        }
    }
    
    @ResponseBody
    @RequestMapping(value="/pullProjet", method = RequestMethod.POST,produces = "application/json")
    public String pullerProjet(HttpServletRequest request) {
    
        
        JSONObject returnObject = new JSONObject();
    
    
    try{
            returnObject.put("response", "");
            returnObject.put("errors", "");
            
            // On vérifie qu'une session est bien ouverte
            HttpSession session= request.getSession();
            User user = (User)session.getAttribute("user");

            if(user == null){
                returnObject.put("response",false);
                returnObject.put("errors", "No user");
                return returnObject.toString();
            }else{
               Projet projet = projetDao.getProjetByName(request.getParameter("projet"));
               if(projet==null){
                    returnObject.put("response",false);
                    returnObject.put("errors", "Le projet demand� n'a pas pu etre trouv�");
                    return returnObject.toString();
               }
               
                Version lastVersion = versionDao.getLastVersionByProjet(projet);
                
                if(lastVersion==null){
                    returnObject.put("response",false);
                    returnObject.put("errors", "Le projet est vide.Rien a push�");
                    return returnObject.toString();
                }
                
                List<FichiersUsers> filesFromProjet =fichierUserDao.getByUserAndProjet(user, projet.getNom());
                    if(filesFromProjet==null){
                        System.out.println("On clone le projet");
                        List<FichiersVersion> filesFromVersion =fichiersVersionDao.getFileByVersion(lastVersion);
                        if(filesFromVersion==null){
                            returnObject.put("response",false);
                            returnObject.put("errors", "Pas de fichiers dans la derniere version->ERROR");
                            return returnObject.toString();
                        }
                        for(int a=0;a<filesFromVersion.size();a++){
                            UUID idOne = UUID.randomUUID();
                            if(filesFromVersion.get(a).getType()==FichiersVersion.Type.FICHIER){
                                 FichiersUsers newFichierUser = fichierUserDao.createNewFichierUser(filesFromVersion.get(a).getPathLogique(),idOne.toString(),filesFromVersion.get(a).getNomReel(),new Date(),FichiersUsers.Type.FICHIER,user,0);
                                 if(newFichierUser==null){
                                     returnObject.put("response",false);
                                     returnObject.put("errors", "Le pull n'a pas fonctionn�: "+a);
                                     return returnObject.toString();
                                 }
                                 //On cr�e le fichier en physique
                                 boolean creation = creerFichier(request,idOne.toString());
                                 if(!creation){
                                     returnObject.put("response",false);
                                     returnObject.put("errors", "Probleme de creation du nouveau fichier de version ");
                                     return returnObject.toString();
                                 } 
                            String src ="/../../files/"+filesFromVersion.get(a).getNomPhysique();
                            String dest="/../../files/"+newFichierUser.getNomPhysique();
                            copier(request,src,dest);
                            }else{
                                 FichiersUsers newDossierUser = fichierUserDao.createNewFichierUser(filesFromVersion.get(a).getPathLogique(),null,filesFromVersion.get(a).getNomReel(),new Date(),FichiersUsers.Type.DOSSIER,user,4);
                                 if(newDossierUser==null){
                                     returnObject.put("response",false);
                                     returnObject.put("errors", "Le pull n'a pas fonctionn�: "+a);
                                     return returnObject.toString();
                                 }
                            }
                        }
                        returnObject.put("response",true);
                        returnObject.put("errors", "");
                        return returnObject.toString();
                    }else{
                        
                        System.out.println("On met a jour");
                        List<FichiersVersion> filesFromVersion =fichiersVersionDao.getFileByVersion(lastVersion);
                        
                        if(filesFromVersion==null){
                            returnObject.put("response",false);
                            returnObject.put("errors", "Pas de fichiers dans la derniere version->ERROR");
                            return returnObject.toString();
                        }
                        List<FichiersVersion> filesFromVersionTamp = new ArrayList<FichiersVersion>();
                        System.out.println("fichers de la version :");
                        for(int compt=0;compt<filesFromVersion.size();compt++){
                            filesFromVersionTamp.add(new FichiersVersion(filesFromVersion.get(compt)));
                            
                        System.out.println(filesFromVersion.get(compt).getPathLogique()+"-");
                                }
                        
                        System.out.println("fichers du projet :");
                        for(int compt2=0;compt2<filesFromProjet.size();compt2++){
                        System.out.println(filesFromProjet.get(compt2).getPathLogique()+"-");
                        }
                        //On copie les verrouill�s dans notre local.On supprime ceux qu'on traite de la liste
                        for(int u=filesFromVersion.size()-1;u>=0;u--){
                            
                            for(int v=0;v<filesFromProjet.size();v++){
                                if(filesFromVersion.get(u).getPathLogique().equals(filesFromProjet.get(v).getPathLogique())){
                                   if(filesFromProjet.get(v).getVerrou()==1){
                                       
                                       boolean delock = fichierUserDao.changeVerrou(filesFromProjet.get(v),0);
                                       if(!delock){
                                            returnObject.put("response",false);
                                            returnObject.put("errors", "Le changement d'un verrou a echou�");
                                            return returnObject.toString();
                                       }
                                        String src ="/../../files/"+filesFromVersion.get(u).getNomPhysique();
                                        String dest="/../../files/"+filesFromProjet.get(v).getNomPhysique();
                                        copier(request,src,dest);
                                   }
                                   
                                   System.out.println(filesFromVersion.get(u).getPathLogique()+"="+filesFromProjet.get(v).getPathLogique());
                                   filesFromVersionTamp.remove(u);
                                }
                            }
                            
                        }
                        System.out.println(filesFromVersion.size());
                        System.out.println(filesFromVersionTamp.size());
                        //on teste la liste
                        if(!filesFromVersionTamp.isEmpty()){
                            System.out.println("Les fichiers qui seront cr��");
                            for(int e=0;e<filesFromVersionTamp.size();e++){
                                System.out.println(filesFromVersionTamp.get(e).getPathLogique());
                                UUID idOne = UUID.randomUUID();
                                if(filesFromVersionTamp.get(e).getType()==FichiersVersion.Type.FICHIER){
                                    FichiersUsers newFichierUser = fichierUserDao.createNewFichierUser(filesFromVersionTamp.get(e).getPathLogique(),idOne.toString(),filesFromVersionTamp.get(e).getNomReel(),new Date(),FichiersUsers.Type.FICHIER,user,0);
                                 if(newFichierUser==null){
                                     returnObject.put("response",false);
                                     returnObject.put("errors", "Le pull n'a pas fonctionn�: "+e);
                                     return returnObject.toString();
                                 }
                                 //On cr�e le fichier en physique
                                 boolean creation = creerFichier(request,idOne.toString());
                                 if(!creation){
                                     returnObject.put("response",false);
                                     returnObject.put("errors", "Probleme de creation du nouveau fichier de version ");
                                     return returnObject.toString();
                                 } 
                            String src ="/../../files/"+filesFromVersionTamp.get(e).getNomPhysique();
                            String dest="/../../files/"+newFichierUser.getNomPhysique();
                            copier(request,src,dest);
                            }else{
                                 FichiersUsers newDossierUser = fichierUserDao.createNewFichierUser(filesFromVersionTamp.get(e).getPathLogique(),null,filesFromVersionTamp.get(e).getNomReel(),new Date(),FichiersUsers.Type.DOSSIER,user,4);
                                 if(newDossierUser==null){
                                     returnObject.put("response",false);
                                     returnObject.put("errors", "Le pull n'a pas fonctionn�: "+e);
                                     return returnObject.toString();
                                 }
                            }
                                }
                            }
                        }
                        
                        returnObject.put("response",true);
                        returnObject.put("errors", "");
                        return returnObject.toString();
                    }
            
            }
            catch(Exception e){
            System.out.println("Erreur JSON");
            System.out.println(e.getMessage());
            
            //TODO: ce try-catch ne sert qu'à afficher les erreurs
            try{
                JSONObject obj = new JSONObject();
                obj.put("response",false);
                obj.put("errors",e.getMessage());
                return obj.toString();
            }
            catch(Exception er){
                
            }
            }
    return null;
    }
    
    @ResponseBody
    @RequestMapping(value="/verrouillerFichier", method = RequestMethod.POST,produces = "application/json")
    public String verrouillerFichier(HttpServletRequest request){
  
    // Pas de session ouverte
    JSONObject returnObject = new JSONObject();
    
    
    try{
            returnObject.put("response", "");
            returnObject.put("errors", "");
            
            // On vérifie qu'une session est bien ouverte
            HttpSession session= request.getSession();
            User user = (User)session.getAttribute("user");

            if(user == null){
                returnObject.put("response",false);
                returnObject.put("errors", "No user");
                return returnObject.toString();
            }
            else
            {
                //On recupere le fichier grace a sn pathLogique
                FichiersUsers file = fichierUserDao.getPathByPathLogique(user,request.getParameter("pathLogique"));
                System.out.println("file : "+file);
                if(file==null){
                    returnObject.put("response",false);
                    returnObject.put("errors", "Pas de fichier trouv� pour ce pathLogique");
                    return returnObject.toString();
                }
                int verrou = file.getVerrou();
                if(verrou==0){
                    //On essaye de mettre le verrou sur le fichier pour nous
                    
                    boolean verrouMoi = fichierUserDao.changeVerrou(file, 2);
                    
                    if(!verrouMoi){
                        returnObject.put("response",false);
                        returnObject.put("errors", "La mise en place du verrou a �chou� (val. 2)");
                        return returnObject.toString();
                    }
                    //On recupere les fichiers qui correspondent au mien pour les autres utilisateurs
                    List<FichiersUsers> files =fichierUserDao.getPathsByPathLogique(user,request.getParameter("pathLogique"));
                    System.out.println("files : "+files);
                    if(files!=null){
                        boolean verrouAutre = fichierUserDao.changeVerrouAutre(files, 1);
                        System.out.println("verrouAutre : "+verrouAutre);
                        if(!verrouAutre){
                            returnObject.put("response",false);
                            returnObject.put("errors", "La mise en place du verrou sur les fichiers des autres utilisateurs a �chou� (val. 1)");
                            return returnObject.toString();
                        }
                    }
                    returnObject.put("response",true);
                    return returnObject.toString();
                }else if (verrou==2){
                    returnObject.put("response",false);
                    returnObject.put("errors", "Vous avez deja verrouill� ce fichier!");
                    return returnObject.toString();
                }else{
                    returnObject.put("response",false);
                    returnObject.put("errors", "Ce fichier a deja �t� verrouill� par un autre utilisateur!");
                    return returnObject.toString();
                }
            }
        }
        catch(Exception e){
            System.out.println("Erreur JSON");
            System.out.println(e.getMessage());
            
            //TODO: ce try-catch ne sert qu'à afficher les erreurs
            try{
                JSONObject obj = new JSONObject();
                obj.put("response",false);
                obj.put("errors",e.getMessage());
                return obj.toString();
            }
            catch(Exception er){
                
            }
            return null;
        }
}
}
