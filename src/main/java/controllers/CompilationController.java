/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import dao.FichierUserDao;
import dao.ProjetDao;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.FichiersUsers;
import models.Projet;
import models.User;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
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
    @RequestMapping(value="/compile", method = RequestMethod.POST, produces = "application/json")
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
        Projet projet;
        try{
            projet = projetDao.getProjetByName(nomProjet);
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
        
        // Si projet Java, récupération de la classe principale en paramètre
        String mainClass = new String();
        if(projet.getLangage() == "java"){
            mainClass = (String)request.getParameter("mainClass");
            if(mainClass == null){
                try{
                    returnObject.put("response", "false");
                    returnObject.put("content", "");
                    returnObject.put("errors", "Projet Java et pas de classe principale");
                    return returnObject.toString();
                }
                // Json fail
                catch(Exception e2){return null;}
            }
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
        
        // Compilation pour projets C/C++
        if(projet.getLangage() == "c" || projet.getLangage() == "c++"){
            if(!buildAndCreateExecC(path, user, projet.getLangage(), nomProjet)){
                try{
                    returnObject.put("response", "false");
                    returnObject.put("content", getCompileErrors(path, user, nomProjet));
                    returnObject.put("errors", "Erreur lors de la compilation");
                    return returnObject.toString();
                }
                // Json fail
                catch(Exception e2){return null;}
            }
        }
        
        // Compilation pour projets Java
        if(projet.getLangage() == "java"){
            if(!buildAndCreateJarJava(path, user, nomProjet, mainClass)){
                try{
                    returnObject.put("response", "false");
                    returnObject.put("content", getCompileErrors(path, user, nomProjet));
                    returnObject.put("errors", "Erreur lors de la compilation");
                    return returnObject.toString();
                }
                // Json fail
                catch(Exception e2){return null;}
            }
        }
        
        // Reussite
        try{
            returnObject.put("response", "true");
            returnObject.put("content", getCompileErrors(path, user, nomProjet));
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
        Map<String, FichiersUsers.Type> arborescence = fichierUserDao.getArborescence(user, racineProjet);
        if(arborescence == null){
            System.out.println("Erreur pendant la récupération de l'arborescence");
            return false;
        }
       
        // Construction du dossier à partir des paths logiques
        // Pour chaque entrée de l'arborescence
        for (Map.Entry<String, FichiersUsers.Type> entry : arborescence.entrySet())
        {
            // Si c'est un fichier
            if(entry.getValue() == FichiersUsers.Type.FICHIER){
                try{              
                    // On obtient le nom physique du fichier
                    FichiersUsers fileUser = fichierUserDao.getFichiersByUserAndPath(user, entry.getKey());
                    if(fileUser == null){throw new Exception("Erreur dans la récupération du fichierUser");}
                    
                    // Getting file content
                    String fileName = extractFileName(entry.getKey());
                    String content = new String(Files.readAllBytes(Paths.get(directoryPath + "/../../files/" + fileUser.getNomPhysique())));
                    
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
    
    private boolean buildAndCreateExecC(String directoryPath, User user, String language, String nomProjet)
    {            
        // Recopier le makefile dans le dossier de compilation
        // Getting file content
        String content;
        try{
            if(language.equals("c")){
                content = new String(Files.readAllBytes(Paths.get(directoryPath + "/../../scripts/makefile_c")));
            }
            else{
                content = new String(Files.readAllBytes(Paths.get(directoryPath + "/../../scripts/makefile_c++")));
            }
            if(content == null)
                throw new Exception("Erreur pendant la récupération du contenu du makefile");
        }
        catch(Exception e){
            System.out.println("Erreur pendant la récupération du contenu du makefile");
            return false;
        }

        // Creating makefile and writing content to it
        try{
            File file = new File(directoryPath + "/../../compile_" + user.getPseudo() + "/" + nomProjet + "/" + "makefile");
            FileWriter writer = new FileWriter(file);
            writer.write(content);
            writer.close();
        }
        catch(Exception e){
            System.out.println("Erreur pendant l'ecriture du makefile");
            return false;
        }

        // Executing make command
        String pathToMakeFile = directoryPath + "/../../compile_" + user.getPseudo() + "/" + nomProjet;
        try{
            ProcessBuilder builder = new ProcessBuilder("make");
            builder.directory(new File(pathToMakeFile));
            builder.redirectError(new File(pathToMakeFile + "/errors.txt"));
            Process p = builder.start();
            p.waitFor();
        }
        catch(Exception e){
            System.out.println("Erreur pendant l'execution du makefile");
            return false;
        }

        // Moving exe to right folder
        try{                
            File file = new File(directoryPath + "/../../execs_" + user.getPseudo() + "/" + "exe");
            file.getParentFile().mkdirs();

            if(new File(directoryPath + "/../../compile_" + user.getPseudo() + "/" + nomProjet + "/" + "exe").exists()){
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(Files.readAllBytes(
                        Paths.get(directoryPath + "/../../compile_" + user.getPseudo() + "/" + nomProjet + "/" + "exe")));
                fos.close();
            }
            else{
                return false;
            }
        }
        catch(Exception e){
            System.out.println("Erreur pendant le déplacement de l'executable");
            return false;
        }

        // Moving errors to right folder
        try{                
            File file = new File(directoryPath + "/../../execs_" + user.getPseudo() + "/" + "errors.txt");
            file.getParentFile().mkdirs();

            if(new File(directoryPath + "/../../compile_" + user.getPseudo() + "/" + nomProjet + "/" + "errors.txt").exists()){
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(Files.readAllBytes(
                        Paths.get(directoryPath + "/../../compile_" + user.getPseudo() + "/" + nomProjet + "/" + "errors.txt")));
                fos.close();
            }
        }
        catch(Exception e){
            System.out.println("Erreur pendant le déplacement du fichier d'erreurs");
            return false;
        }

        // Deleting compile folder
        try{
            deleteFile(new File(pathToMakeFile + "/../../compile_" + user.getPseudo()));
        }
        catch(Exception e){
            System.out.println("Erreur pendant la suppression du dossier de compile");
            return false;
        }

        return true;
    }
        
    private boolean buildAndCreateJarJava(String directoryPath, User user, String nomProjet, String mainClass)
    {
        // Compilation des sources java
        String pathToProject = directoryPath + "/../../compile_" + user.getPseudo() + "/" + nomProjet;
        
        try{
            ProcessBuilder builder = new ProcessBuilder("find . -name *.java -exec javac");
            builder.directory(new File(pathToProject));
            builder.redirectError(new File(pathToProject + "/errors.txt"));
            Process p = builder.start();
            p.waitFor();
        }
        catch(Exception e){
            System.out.println("Erreur pendant la compilation javac");
            return false;
        }
        
        //find . -name \*.php -exec sed -i 's/a remplacer/remplacement/g' {} \;
        //
        return true;
    }
    
    private void deleteFile(File element) {
        if (element.isDirectory()) {
            for (File sub : element.listFiles()) {
                deleteFile(sub);
            }
        }
        element.delete();
    }

    private String getCompileErrors(String directoryPath, User user, String nomProjet)
    {
        File file = new File(directoryPath + "/../../execs_" + user.getPseudo() + "/" + "errors.txt");
        if(!file.exists()){
            return new String("");
        }
        else{
            try{
                return new String(Files.readAllBytes(
                        Paths.get(directoryPath + "/../../execs_" + user.getPseudo() + "/" + "errors.txt")));
            }
            catch(Exception e){
                return new String("");
            }
        }
    }
    
    // Téléchargement de l'executable
    @ResponseBody
    @RequestMapping(value="/downloadExec", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public FileSystemResource downloadExec(HttpServletRequest request, ModelMap model, HttpServletResponse response)
    {
        // Récupération de la session de l'utilisateur
        HttpSession session= request.getSession();
        User user = (User)session.getAttribute("user");
        
        // Si pas d'utilisateur connecté
        if(user == null){
            return null;
        }
        
        try {
            ServletContext ctx = request.getServletContext();
            String path = ctx.getRealPath("/");
            String pathToExec = path + "/../../execs_" + user.getPseudo() + "/" + "exe";
            return new FileSystemResource(pathToExec); 
        } 
        catch (Exception e) {
            return null;
        }
    }

}
