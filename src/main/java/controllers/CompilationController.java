/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import dao.FichierUserDao;
import dao.ProjetDao;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.FichiersUsers;
import models.Projet;
import models.User;
import org.json.JSONException;
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
    
    /**
     *
     */
    public CompilationController(){
    }
    
    // Compilation d'un projet. La requête doit contenir un champs "nomProjet"

    /**
     * @author Luc Di Sanza
     * Traitement d'une requête de compilation.
     * La requête doit contenir le champs "nomProjet"
     * @param request
     * @param model
     * @return Renvoie un Json contenant les champs "response", "content", "errors" et "nomExec" si réussite
     */
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
            catch(JSONException e){System.out.println("Erreur JSON: " + e);return null;}
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
            catch(JSONException e){System.out.println("Erreur JSON: " + e);return null;}
        }
        
        // Vérification de l'existence en base
        Projet projet;
        try{
            projet = projetDao.getProjetByName(nomProjet);
            if(projet == null) {
                try{
                    returnObject.put("response", "false");
                    returnObject.put("content", "");
                    returnObject.put("errors", "Erreur lors de la recuperation du projet en base de donnees");
                    return returnObject.toString();
                }
                // Json fail
                catch(JSONException e){System.out.println("Erreur JSON: " + e);return null;}
            }
        }
        catch(Exception e){
            try{
                System.out.println("Erreur lors de la récupération du projet en base de données" + e);
                returnObject.put("response", "false");
                returnObject.put("content", "");
                returnObject.put("errors", "Erreur lors de la recuperation du projet en base de donnees");
                return returnObject.toString();
            }
            // Json fail
            catch(Exception e2){System.out.println("Erreur JSON: " + e2); return null;}
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
            catch(Exception e2){System.out.println("Erreur JSON: " + e2);return null;}
        }
        
        // Compilation pour projets C/C++
        if("c++".equals(projet.getLangage()) || "c".equals(projet.getLangage())){
            if(!buildAndCreateExecC(path, user, projet.getLangage(), nomProjet)){
                try{
                    returnObject.put("response", "false");
                    returnObject.put("content", getCompileErrors(path, user, nomProjet));
                    returnObject.put("errors", "Erreur lors de la compilation");
                    return returnObject.toString();
                }
                // Json fail
                catch(Exception e2){System.out.println("Erreur JSON: " + e2); return null;}
            }
        }
        
        // Compilation pour projets Java
        if("java".equals(projet.getLangage())){
            if(!buildAndCreateJarJava(path, user, nomProjet)){
                try{
                    returnObject.put("response", "false");
                    returnObject.put("content", getCompileErrors(path, user, nomProjet));
                    returnObject.put("errors", "Erreur lors de la compilation");
                    return returnObject.toString();
                }
                // Json fail
                catch(Exception e2){System.out.println("Erreur JSON: " + e2); return null;}
            }
            
        }
        
        // Reussite
        try{
            returnObject.put("response", "true");
            if("c".equals(projet.getLangage()) || "c++".equals(projet.getLangage())){
                returnObject.put("nomExec", "exe");
            }
            else if("java".equals(projet.getLangage())){
                returnObject.put("nomExec", nomProjet + ".jar");
            }
            returnObject.put("content", getCompileErrors(path, user, nomProjet));
            returnObject.put("errors", "");
            return returnObject.toString();
        }
        // Json fail
        catch(Exception e){
            System.out.println("Erreur JSON: " + e);
            return null;
        }        
    }
    
    private boolean createCompileDirectory(String directoryPath, User user, String nomProjet){
        
        // Récupération du dossier racine du projet
        FichiersUsers racineProjet;
        racineProjet = fichierUserDao.getFichiersByUserAndPath(user, "/" + nomProjet);
        if(racineProjet == null){System.out.println("Erreur pendant la récupération de la racine du projet"); return false;}
        
        // Récupération de l'arborescence du projet
        List<FichiersUsers> arborescence = fichierUserDao.getArborescence(user, racineProjet);
        
        if(arborescence == null){
            System.out.println("Erreur pendant la récupération de l'arborescence");
            return false;
        }
       
        // Construction du dossier à partir des paths logiques
        // Pour chaque entrée de l'arborescence
        for (FichiersUsers entry : arborescence)
        {
            try{              
                // On obtient le nom physique du fichier
                FichiersUsers fileUser = fichierUserDao.getFichiersByUserAndPath(user, entry.getPathLogique());
                if(fileUser == null){return false;}

                // Getting file content
                String content = new String(Files.readAllBytes(Paths.get(directoryPath + "/../../files/" + fileUser.getNomPhysique())));

                // Creating compile file and writing content to it
                File file = new File(directoryPath + "/../../compile_" + user.getPseudo() + "/" + entry.getPathLogique());
                file.getParentFile().mkdirs();
                FileWriter writer = new FileWriter(file);
                writer.write(content);
                writer.close();
            }
            catch(Exception e){
                System.out.println("Erreur pendant la création du dossier de compilation" + e);
                return false;
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
            if("c".equals(language)){
                content = new String(Files.readAllBytes(Paths.get(directoryPath + "/../../scripts/makefile_c")));
            }
            else{
                content = new String(Files.readAllBytes(Paths.get(directoryPath + "/../../scripts/makefile_c++")));
            }
        }
        catch(IOException e){
            System.out.println("Erreur pendant la récupération du contenu du makefile" + e);
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
            System.out.println("Erreur pendant l'ecriture du makefile" + e);
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
            System.out.println("Erreur pendant l'execution du makefile" + e);
            return false;
        }

        // Moving exe to right folder
        Boolean compileWorked;
        try{                
            File file = new File(directoryPath + "/../../execs_" + user.getPseudo() + "/" + "exe");
            file.getParentFile().mkdirs();

            if(new File(directoryPath + "/../../compile_" + user.getPseudo() + "/" + nomProjet + "/" + "exe").exists()){
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(Files.readAllBytes(
                        Paths.get(directoryPath + "/../../compile_" + user.getPseudo() + "/" + nomProjet + "/" + "exe")));
                fos.close();
                compileWorked = true;
            }
            else{
                System.out.println("Pas trouvé d'executable, la compilation est un echec");
                compileWorked = false;
            }
        }
        catch(Exception e){
            System.out.println("Erreur pendant le déplacement de l'executable" + e);
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
            else{
                System.out.println("Pas trouvé le fichier errors.txt dans le dossier de compilation");
                return false;
            }
        }
        catch(Exception e){
            System.out.println("Erreur pendant le déplacement du fichier d'erreurs" + e);
            return false;
        }

        // Deleting compile folder
        try{
            deleteFile(new File(pathToMakeFile + "/../../compile_" + user.getPseudo()));
        }
        catch(Exception e){
            System.out.println("Erreur pendant la suppression du dossier de compile" + e);
            return false;
        }

        return compileWorked;
    }
        
    private boolean buildAndCreateJarJava(String directoryPath, User user, String nomProjet)
    {
        // Compilation des sources java
        String pathToProject = directoryPath + "/../../compile_" + user.getPseudo() + "/" + nomProjet;
        try{
            ProcessBuilder builder = new ProcessBuilder("find", ".","-name", "*.java", "-exec", "javac", "{}", ";");
            builder.directory(new File(pathToProject));
            builder.redirectError(new File(pathToProject + "/errors.txt"));
            Process p = builder.start();
            p.waitFor();
        }
        catch(IOException | InterruptedException e){
            System.out.println("Erreur pendant la compilation javac: " + e);
            return false;
        }
        
        Boolean compileWorked;
        if(new File(pathToProject + "/" + "main.class").exists()){
        
            // Création du manifest
            try{
                File file = new File(pathToProject + "/" + "MANIFEST.MF");
                FileWriter writer = new FileWriter(file);
                writer.write("Main-Class: " + "main\n");
                writer.close();
            }
            catch(Exception e){
                System.out.println("Erreur pendant la création du manifest pour le jar" + e);
                return false;
            }

            // Création du jar
            try{
                // Init du jar
                File file = new File(directoryPath + "/../../execs_" + user.getPseudo() + "/" + nomProjet + ".jar");
                file.getParentFile().mkdirs();
                
                ProcessBuilder builder = new ProcessBuilder("jar", "-cvmf", "MANIFEST.MF", directoryPath + "/../../execs_" + user.getPseudo() + "/" + nomProjet+".jar");
                builder.directory(new File(pathToProject));
                Process p = builder.start();
                p.waitFor();
                
                // Ajout de tous les .class
                ProcessBuilder builder2 = new ProcessBuilder("find","." ,"-name", "*.class", "-exec", "jar", "-uf", directoryPath + "/../../execs_" + user.getPseudo() + "/" + nomProjet+".jar", "{}", ";");
                builder2.directory(new File(pathToProject));
                Process p2 = builder2.start();
                p2.waitFor();
            }
            catch(Exception e){
                System.out.println("Erreur pendant la création du jar:" + e);
                return false;
            }
            
            compileWorked = true;
        }
        else{
            System.out.println("Echec de la compilation");
            compileWorked = false;
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
            System.out.println("Erreur pendant le déplacement du fichier d'erreurs: " + e);
            return false;
        }

        // Deleting compile folder
        try{
            deleteFile(new File(directoryPath + "/../../compile_" + user.getPseudo()));
        }
        catch(Exception e){
            System.out.println("Erreur pendant la suppression du dossier de compile: " + e);
            return false;
        }
        
        return compileWorked;
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
            return "";
        }
        else{
            try{
                return new String(Files.readAllBytes(
                        Paths.get(directoryPath + "/../../execs_" + user.getPseudo() + "/" + "errors.txt")));
            }
            catch(Exception e){
                System.out.println("Erreur pendant la récupération des erreurs de compilation: " + e);
                return "";
            }
        }
    }
    
    /**
     *
     * @param request
     * @param model
     * @param response
     * @return
     */
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
        
        // Récupération du nom de l'executable à télécharger
        String execName = (String)request.getParameter("nomExec");
        if(execName == null){
            return null;
        }
        
        try {
            ServletContext ctx = request.getServletContext();
            String path = ctx.getRealPath("/");
            String pathToExec = path + "/../../execs_" + user.getPseudo() + "/" + execName;
            return new FileSystemResource(pathToExec); 
        } 
        catch (Exception e) {
            System.out.println("Erreur pendant le téléchargement de l'executable: " + e);
            return null;
        }
    }

}
