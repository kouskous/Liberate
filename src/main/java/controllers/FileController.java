/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import dao.ProjetDao;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import models.User;
import org.springframework.stereotype.Controller;
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
    ProjetDao projetDao;
    
    private Function ExtractFileName(String path) {
        String filename = Mid(path, InStrRev(path, "\\") + 1);
    }            
    
    @RequestMapping(value="/newFile", method = RequestMethod.POST)
    public String newProject(HttpServletRequest request){
        String format = "dd/MM/yy H:mm:ss";
        java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat( format );
        java.util.Date date = new java.util.Date(); 
        
        
        
        // On vérifie si une session est déjà ouverte
        HttpSession session= request.getSession();
        User user = (User)session.getAttribute("user");
 
        if(user == null) // Pas de session ouverte
            return "redirect:/login";
        else // Une session déjà ouverte
            
            
            /*createNewFichierUser((String)request.getParameter("pathFile"), nomPhysique, nomReel, dateCreation,
                                     type, user);*/
    }
}
