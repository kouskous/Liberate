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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import models.FichiersUsers;
import models.FichiersVersion;
import models.Projet;
import models.User;
import models.Version;
import org.json.JSONArray;
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
    VersionDao versionDao;
    ProjetDao projetDao;
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
    
    @ResponseBody
    @RequestMapping(value="/pushProjet", method = RequestMethod.GET,produces = "application/json")
    public String pusherProjet(HttpServletRequest request) throws JSONException{
  
    // Pas de session ouverte
    JSONObject returnObject = new JSONObject();
    
    
    try{
            returnObject.put("response", "");
            returnObject.put("errors", "");
            
            // On vérifie qu'une session est bien ouverte
            HttpSession session= request.getSession();
            User user = (User)session.getAttribute("user");

            if(user == null){
                returnObject.put("response","false");
                returnObject.put("errors", "No user");
                return returnObject.toString();
            }
            else{
                
                //Le projet qu'on souhaite pusher
                Projet projet = projetDao.getProjetByName(request.getParameter("projet"));
                if(projet==null){
                    returnObject.put("response","false");
                    returnObject.put("errors", "No project with this name");
                    return returnObject.toString();
                }
                //La derniere version existante du projet
                Version lastVersion = versionDao.getLastVersionByProjet(projet);
                
                if(lastVersion==null){
                    Version newVersion = versionDao.createNewVersion(lastVersion.getNumVersion()+1,new Date(),user,projet,"test");
                    if(newVersion==null){
                        returnObject.put("response","false");
                        returnObject.put("errors", "Erreur lors de la creation de la nouvelle version");
                        return returnObject.toString();
                        }
                    }
                else{
                       //On recupere tous les fichiers qu'on a verrouill�
                       List<FichiersUsers> lockedFiles = fichierUserDao.getLockedByUserAndProjet(user, request.getParameter("projet"));
                       if(lockedFiles==null){
                           returnObject.put("response","false");
                           returnObject.put("errors", "Rien a push�");
                           return returnObject.toString();
                       }else{
                           //On recupere les fichiers de la derniere version
                           List<FichiersVersion> fichiersLastVersion = fichiersVersionDao.getFileByVersion(lastVersion);
                           
                           //Puis on cree une nouvelle version
                           Version newVersion = versionDao.createNewVersion(lastVersion.getNumVersion()+1,new Date(),user,projet,"test");
                           if(newVersion==null){
                               returnObject.put("response","false");
                               returnObject.put("errors", "Erreur lors de la creation de la nouvelle version");
                               return returnObject.toString();
                           }
                           for(int i=0;i<lockedFiles.size();i++){
                               FichiersVersion file = fichiersVersionDao.createNewFichierVersion(lockedFiles.get(i).getPathLogique(), lockedFiles.get(i).getNomPhysique(),lockedFiles.get(i).getNomReel(), new Date(), FichiersVersion.Type.FICHIER, newVersion);
                               if(file==null){
                                   returnObject.put("response","false");
                                    returnObject.put("errors", "Erreur lors de la creation du fichier :"+i);
                                    return returnObject.toString();
                                }
                               for(int j=0;j<fichiersLastVersion.size();j++){
                                   if(lockedFiles.get(i).getPathLogique()!=fichiersLastVersion.get(j).getPathLogique()){
                                       FichiersVersion file2 = fichiersVersionDao.createNewFichierVersion(fichiersLastVersion.get(j).getPathLogique(), fichiersLastVersion.get(j).getNomPhysique(),fichiersLastVersion.get(j).getNomReel(), new Date(), FichiersVersion.Type.FICHIER, newVersion);
                                        if(file2==null){
                                            returnObject.put("response","false");
                                            returnObject.put("errors", "Erreur lors de la creation du fichier :"+j);
                                            return returnObject.toString();
                                        }
                                   }
                           }
                           //Puis on ajoute les fichers verrouill�s a cette nouvelle version:
                           //dans la base dans le cas de nouveaux fichiers, puis aussi en physique
                           //(c�d copie du contenu des fichiers verrouill�s dans les fichers correpondant dans la version)
                           
                           //On peut ensuite enlever le verrou sur ces fichiers.(seulement pour moi 2-->0)
                           }
                           for(int k =0 ;k<lockedFiles.size();k++){
                           boolean test = fichierUserDao.changeVerrou(lockedFiles.get(k),0);
                           if(!test){
                               returnObject.put("response","false");
                               returnObject.put("errors","Le verrou n'a pas �t� relach� pour "+lockedFiles.get(k).getNomPhysique());
                               return returnObject.toString();
                           }
                       }
             
                       }
                   }
                returnObject.put("result", "true");
                return returnObject.toString();
            }
        }
        catch(Exception e){
            System.out.println("Erreur JSON");
            System.out.println(e.getMessage());
            
            //TODO: ce try-catch ne sert qu'à afficher les erreurs
            try{
                JSONObject obj = new JSONObject();
                obj.put("errors",e.getMessage());
                obj.put("response","false");
                return obj.toString();
            }
            catch(Exception er){
                
            }
            return null;
        }
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
