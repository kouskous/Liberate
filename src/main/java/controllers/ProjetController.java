/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import dao.ProjetDao;
import dao.UserDao;
import dao.UserProjetDao;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import models.Projet;
import models.User;
import models.UserProjet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Luc Di Sanza
 */
@Controller
public class ProjetController {
    
    @Autowired
    ProjetDao projetDao;
    
    @Autowired
    UserDao userDao;
    
    @Autowired
    UserProjetDao userProjetDao;
    
    // Regex1 utilisée pour le nom du projet
    static String regex1 = "^([a-zA-Z]{3,50})";
    
    public ProjetController(){
    }
    
    @RequestMapping(value="/newProjet", method = RequestMethod.GET)
    public String index(HttpServletRequest request, ModelMap model){
        return "newProjet";
    }
    
    @RequestMapping(value="/newProjet", method = RequestMethod.POST)
    public String nouveauProjet(HttpServletRequest request, ModelMap model){

        EntityManager em = userDao.getEntityManager();
        
        // Obtention de la session
        HttpSession session= request.getSession();
        User user = (User)session.getAttribute("user");
        
        // Si pas de session ouverte
        if(user == null){
            model.addAttribute("Erreur", "Pas de session ouverte");
            return "redirect:/";
        }
        else{
            
            String nomProjet = request.getParameter("nomProjet");
            String langageProjet = request.getParameter("langageProjet");
          
            // TODO: Gestion des fichiers importés
            //ArrayList<String> fichiersImportés = request.getParameter("ListeFichiersImportés");
            
            // TODO: Gestion des utilisateurs du projet et de leurs droits
            //ArrayList<String> usersProjet = request.getParameter("ListeNomsUsers");
            //ArrayList<String> droitsUsers = request.getParameter("ListeDroitsUsers");
            
            // Test du nom
            if(!testRegex(regex1, nomProjet)){
                model.addAttribute("Erreur", "Nom de projet invalide");
                return "newProjet";
            }

            // Création du projet
            em.getTransaction().begin();
            Projet projet = projetDao.createNewProjet(nomProjet, new Date(), new Date(), langageProjet);
            
            // Si le projet a bien été créé
            if(projet != null){
                
                // Le user qui l'a créé (celui dans la session) est admin
                UserProjet userProjet = userProjetDao.createNewUserProjet("Admin", new Date(), new Date(), user, projet);
                
                // Si le userProjet a bien été créé
                if(userProjet != null){
                    
                    // TODO: ajouter les autres users projet ici
                    // TODO: ajouter tous les fichiers de base du projet ici
                    try{
                        em.getTransaction().commit();
                        // Réussite, redirection page principale
                        return "redirect:/";
                    }
                    catch(Exception e){
                        // Erreur pendant le commit
                        model.addAttribute("Erreur", "Communication BDD");
                        return "newProjet";
                    }
                }
                else{ // Le userProjet Admin n'a pas pu être créé
                    model.addAttribute("Erreur", "BDD - User admin n'a pas pu être assigné au projet");
                    return "newProjet";
                }
            }
            else{ // Projet n'a pas pu être créé
                model.addAttribute("Erreur", "BDD - Projet n'a pas pu être créé");
                return "newProjet";
            }
        }
    }
    
    private boolean testRegex(String regex, String aTester){
        return Pattern.compile(regex).matcher(aTester).matches();
    }
            
    
}
