/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import models.FichiersUsers;
import models.FichiersVersion;
import models.Projet;
import models.User;
import models.Version;
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
    
    // Cr√©ation d'un fichier vide
    // Cr√©ation d'un fichier
    // - N√©cessite le champs "pathFichier" dans la requ√™te
    //
    // Renvoie un Json avec cl√©s "response" et "errors"
    // - response contient true si r√©ussite
    // - errors contient retour d'erreur si echec
    // - renvoie null si erreur Json
    @ResponseBody 
    @RequestMapping(value="/newFile", method = RequestMethod.POST, produces = "application/json")
    public String newFile(HttpServletRequest request, ModelMap model){           
        
        // On cr√©√© l'objet √† retourner
        JSONObject returnObject = new JSONObject();   
        
        try{
            returnObject.put("response", "");
            returnObject.put("errors", "");
            
            // On v√©rifie qu'une session est bien ouverte
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
                    
                    // G√©n√©ration du path physique
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
            
            //TODO: ce try-catch ne sert qu'√† afficher les erreurs
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
        
        // On cr√©√© l'objet √† retourner
        JSONObject returnObject = new JSONObject();   
        
        // On v√©rifie qu'une session est bien ouverte
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
        
         // G√©n√©ration du path physique
        UUID idOne = UUID.randomUUID();
        
        if(fileName == null){
            try{
                returnObject.put("response", "false");
                returnObject.put("content", "");
                returnObject.put("errors", "Erreur pendant la r√©cup√©ration du nom de dossier");
                return returnObject.toString();
            }
            // JSon fail
            catch(Exception e){return null;}
        }
        
        // Cr√©ation du dossier
        try{
            FichiersUsers newFile = fichierUserDao.createNewFichierUser((String)request.getParameter("pathDossier"), 
            idOne.toString(), 
            fileName, 
            new Date(), 
            FichiersUsers.Type.DOSSIER, 
            user, 4);
            
            if(newFile == null){
                throw new Exception("Erreur pendant la cr√©ation du dossier");
            }
        }
        catch(Exception e){
            try{
                returnObject.put("response", "false");
                returnObject.put("content", "");
                returnObject.put("errors", "Erreur pendant la cr√©ation du dossier");
                return returnObject.toString();
            }
            // JSon fail
            catch(Exception e2){return null;}
        }
        
        // R√©ussite
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
    // - N√©cessite les champs "pathFichier" et "contenuFichier" dans la requ√™te
    //
    // Renvoie un Json avec cl√© response et errors
    // - reponse contient true sur r√©ussite
    // - errors contient retour d'erreur si echec
    // - renvoie null si erreur avec le JSON
    @ResponseBody 
    @RequestMapping(value="/saveFile", method = RequestMethod.POST, produces = "application/json")
    public String saveFile(HttpServletRequest request, ModelMap model){
        
        // On cr√©√© l'objet √† retourner
        JSONObject returnObject = new JSONObject();   
        
        try{       
            returnObject.put("response", "");
            returnObject.put("errors", "");
            
            String pathFichier = (String)request.getParameter("pathFichier");
            String fileName = extractFileName((String)request.getParameter("pathFichier"));
            String contenuFichier = (String)request.getParameter("contenuFichier");
            
            // On v√©rifie les param√®tres de la requ√™te
            if(pathFichier == null || contenuFichier == null){ 
                returnObject.put("errors","No filepath or content");
                return returnObject.toString();
            }
            else{
                // R√©cup√©ration de l'utilisateur
                HttpSession session= request.getSession();
                User user = (User)session.getAttribute("user");
                
                // On v√©rifie l'utilisateur
                if(user == null){
                    returnObject.put("errors","No user");
                    return returnObject.toString();
                }
                else{
                
                    // On trouve le fichier √† enregistrer dans la BDD
                    FichiersUsers fichier = fichierUserDao.getFichiersByUserAndPath(user, pathFichier);
                    
                    if(fichier == null){ // Le fichier n'a pas √©t√© trouv√©
                        returnObject.put("errors","File not found");
                        return returnObject.toString();
                    }
                    else{
                        // Mise √† jour date
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
            
            //TODO: ce try-catch ne sert qu'√† afficher les erreurs
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
        
        // On v√©rifie qu'une session est ouverte
        HttpSession session= request.getSession();
        User user = (User)session.getAttribute("user");

        // Pas de session ouverte
        if(user == null) return "redirect:/login";
        
        // Objet r√©ponse
        JSONObject response = new JSONObject();
        
        FichiersUsers file = fichierUserDao.getPathByPathLogique(user,request.getParameter("pathLogique"));
        if(file == null){
            try{
                response.put("pathLogique","");
                response.put("pathPhysique","");
                response.put("content","");
                response.put("errors", "Erreur dans la r√©cup√©ration du contenu d'un fichier");
                return response.toString();
            }
            // Json Fail
            catch (Exception e){return null;}
        }      
          
        // R√©cup√©ration du path physique
        String pathPhysique =file.getNomPhysique();
        if(pathPhysique == null){
            try{
                response.put("pathLogique","");
                response.put("pathPhysique","");
                response.put("content","");
                response.put("errors", "Erreur dans la r√©cup√©ration du path physique d'un fichier");
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
            
            // R√©cup√©ration du contenu du fichier
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
    private static boolean copier(String fichier_source, String fichier_dest)
    { 
        try{
            FileInputStream src = new FileInputStream(fichier_source);
           FileOutputStream dest = new FileOutputStream(fichier_dest);

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
    @RequestMapping(value="/pushProjet", method = RequestMethod.GET,produces = "application/json")
    public String pusherProjet(HttpServletRequest request) {
  
    // Pas de session ouverte
    JSONObject returnObject = new JSONObject();
    
    
    try{
            returnObject.put("response", "");
            returnObject.put("errors", "");
            
            // On v√©rifie qu'une session est bien ouverte
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
                        returnObject.put("errors", "Rien a pushÈ");
                        return returnObject.toString();
                    }
                    //Une fois recu on les ajoute a la nouvelle version pis on repasse leur verrou ‡ 0
                    for(int c=0;c<filesFromProjet.size();c++){
                        UUID idOne = UUID.randomUUID();
                       if(filesFromProjet.get(c).getType()==FichiersUsers.Type.FICHIER){
                            FichiersVersion newFichierVersion = fichiersVersionDao.createNewFichierVersion(filesFromProjet.get(c).getPathLogique(),idOne.toString(),filesFromProjet.get(c).getNomReel(),new Date(),FichiersVersion.Type.FICHIER,newVersion);
                            if(newFichierVersion==null){
                                returnObject.put("response",false);
                                returnObject.put("errors", "Le push n'a pas fonctionnÈ: "+c);
                                return returnObject.toString();
                            }
                            //On crÈe le fichier en physique
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
                            String src ="/files/"+filesFromProjet.get(c).getNomPhysique();
                            String dest="/files/"+newFichierVersion.getNomPhysique();
                            copier(src,dest);
                       }else{
                           FichiersVersion newDossierVersion = fichiersVersionDao.createNewFichierVersion(filesFromProjet.get(c).getPathLogique(),null,filesFromProjet.get(c).getNomReel(),new Date(),FichiersVersion.Type.DOSSIER,newVersion);
                           if(newDossierVersion==null){
                                returnObject.put("response",false);
                                returnObject.put("errors", "Le push n'a pas fonctionnÈ pour un dossier :" +newDossierVersion);
                                return returnObject.toString();
                            }
                       }
                       
                    }
                    FichiersVersion newDossierVersion = fichiersVersionDao.createNewFichierVersion("/"+projet.getNom(),null,projet.getNom(),new Date(),FichiersVersion.Type.DOSSIER,newVersion);
                        if(newDossierVersion==null){
                                returnObject.put("response",false);
                                returnObject.put("errors", "Le push n'a pas fonctionnÈ pour un dossier :" +newDossierVersion);
                                return returnObject.toString();
                        }
                }else{
                       //On recupere tous les fichiers qu'on a verrouillÈ
                       List<FichiersUsers> lockedFiles = fichierUserDao.getLockedByUserAndProjet(user, request.getParameter("projet"));
                       if(lockedFiles==null){
                           System.out.println("lockedFiles null");
                           returnObject.put("response",false);
                           returnObject.put("errors", "Rien a pushÈ");
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
                               FichiersVersion file = fichiersVersionDao.createNewFichierVersion(lockedFiles.get(i).getPathLogique(), lockedFiles.get(i).getNomPhysique(),lockedFiles.get(i).getNomReel(), new Date(), FichiersVersion.Type.FICHIER, newVersion);
                               if(file==null){
                                   System.out.println("file null");
                                   returnObject.put("response",false);
                                    returnObject.put("errors", "Erreur lors de la creation du fichier :"+i);
                                    return returnObject.toString();
                                }
                               for(int j=0;j<fichiersLastVersion.size();j++){
                                   if(lockedFiles.get(i).getPathLogique()!=fichiersLastVersion.get(j).getPathLogique()){
                                       FichiersVersion file2 = fichiersVersionDao.createNewFichierVersion(fichiersLastVersion.get(j).getPathLogique(), fichiersLastVersion.get(j).getNomPhysique(),fichiersLastVersion.get(j).getNomReel(), new Date(), FichiersVersion.Type.FICHIER, newVersion);
                                        if(file2==null){
                                            System.out.println("file2 null");
                                            returnObject.put("response",false);
                                            returnObject.put("errors", "Erreur lors de la creation du fichier :"+j);
                                            return returnObject.toString();
                                        }
                                   }
                           }
                           //Puis on ajoute les fichers verrouillÈs a cette nouvelle version:
                           //dans la base dans le cas de nouveaux fichiers, puis aussi en physique
                           //(c‡d copie du contenu des fichiers verrouillÈs dans les fichers correpondant dans la version)
                           
                           //On peut ensuite enlever le verrou sur ces fichiers.(seulement pour moi 2-->0)
                           }
                           for(int k =0 ;k<lockedFiles.size();k++){
                           boolean test = fichierUserDao.changeVerrou(lockedFiles.get(k),0);
                           if(!test){
                               System.out.println("user null");
                               returnObject.put("response",false);
                               returnObject.put("errors","Le verrou n'a pas ÈtÈ relachÈ pour "+lockedFiles.get(k).getNomPhysique());
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
            
            //TODO: ce try-catch ne sert qu'√† afficher les erreurs
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
    @RequestMapping(value="/pullProjet", method = RequestMethod.GET,produces = "application/json")
    public String pullerProjet(HttpServletRequest request) {
    
        
        JSONObject returnObject = new JSONObject();
    
    
    try{
            returnObject.put("response", "");
            returnObject.put("errors", "");
            
            // On v√©rifie qu'une session est bien ouverte
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
                    returnObject.put("errors", "Le projet demandÈ n'a pas pu etre trouvÈ");
                    return returnObject.toString();
               }
               
                Version lastVersion = versionDao.getLastVersionByProjet(projet);
                
                if(lastVersion==null){
                    returnObject.put("response",false);
                    returnObject.put("errors", "Le projet est vide.Rien a pushÈ");
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
                                     returnObject.put("errors", "Le pull n'a pas fonctionnÈ: "+a);
                                     return returnObject.toString();
                                 }
                                 //On crÈe le fichier en physique
                                 boolean creation = creerFichier(request,idOne.toString());
                                 if(!creation){
                                     returnObject.put("response",false);
                                     returnObject.put("errors", "Probleme de creation du nouveau fichier de version ");
                                     return returnObject.toString();
                                 } 
                            String src ="/files/"+filesFromVersion.get(a).getNomPhysique();
                            String dest="/files/"+newFichierUser.getNomPhysique();
                            copier(src,dest);
                            }else{
                                 FichiersUsers newDossierUser = fichierUserDao.createNewFichierUser(filesFromVersion.get(a).getPathLogique(),null,filesFromVersion.get(a).getNomReel(),new Date(),FichiersUsers.Type.DOSSIER,user,4);
                                 if(newDossierUser==null){
                                     returnObject.put("response",false);
                                     returnObject.put("errors", "Le pull n'a pas fonctionnÈ: "+a);
                                     return returnObject.toString();
                                 }
                            }
                        }
                        returnObject.put("response",true);
                        returnObject.put("errors", "");
                        return returnObject.toString();
                    }else{
                        
                        System.out.println("Pas encore codÈ DSL");
                        returnObject.put("response",false);
                        returnObject.put("errors", "Pas codÈ dsl");
                        return returnObject.toString();
                    }
            
            }
            }catch(Exception e){
            System.out.println("Erreur JSON");
            System.out.println(e.getMessage());
            
            //TODO: ce try-catch ne sert qu'√† afficher les erreurs
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
            
            // On v√©rifie qu'une session est bien ouverte
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
                    returnObject.put("errors", "Pas de fichier trouvÈ pour ce pathLogique");
                    return returnObject.toString();
                }
                int verrou = file.getVerrou();
                if(verrou==0){
                    //On essaye de mettre le verrou sur le fichier pour nous
                    
                    boolean verrouMoi = fichierUserDao.changeVerrou(file, 2);
                    
                    if(!verrouMoi){
                        returnObject.put("response",false);
                        returnObject.put("errors", "La mise en place du verrou a ÈchouÈ (val. 2)");
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
                            returnObject.put("errors", "La mise en place du verrou sur les fichiers des autres utilisateurs a ÈchouÈ (val. 1)");
                            return returnObject.toString();
                        }
                    }
                    returnObject.put("response",true);
                    return returnObject.toString();
                }else if (verrou==2){
                    returnObject.put("response",false);
                    returnObject.put("errors", "Vous avez deja verrouillÈ ce fichier!");
                    return returnObject.toString();
                }else{
                    returnObject.put("response",false);
                    returnObject.put("errors", "Ce fichier a deja ÈtÈ verrouillÈ par un autre utilisateur!");
                    return returnObject.toString();
                }
            }
        }
        catch(Exception e){
            System.out.println("Erreur JSON");
            System.out.println(e.getMessage());
            
            //TODO: ce try-catch ne sert qu'√† afficher les erreurs
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
