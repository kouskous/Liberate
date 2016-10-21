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
    
    private String extractFileName(String path) {
        String filename = path.substring(path.lastIndexOf("/")+1); 
        return filename;
    }
    
    @RequestMapping(value="/newFichier", method = RequestMethod.POST)
    public String newFile(HttpServletRequest request, ModelMap model){
        Date d = new Date();
        
        em = Persistence.createEntityManagerFactory("persistenceUnitLiber8").createEntityManager();
        fichierUserDao = new FichierUserDao(em);
        
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
    
    @RequestMapping(value="/newFichier", method = RequestMethod.GET)
    public String newIndex(HttpServletRequest request, ModelMap model){
        return "newFichier";
    }
}
