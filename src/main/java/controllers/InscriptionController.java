/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;
import dao.UserDao;
import java.util.Date;
import java.util.regex.Pattern;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Luc Di Sanza
 */
@Controller
public class InscriptionController {
    
    EntityManager em;
    UserDao userDao;
    
    // Regex1 utilisée pour nom, prenom, pseudo
    static String regex1 = "^([a-zA-Z]{3,50})";
    // Regex2 utilisée pour email
    static String regex2 = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)";
    
    public InscriptionController(){
        em = Persistence.createEntityManagerFactory("persistenceUnitLiber8").createEntityManager();
        userDao = new UserDao(em);
    }
    
    @RequestMapping(value="/inscription", method = RequestMethod.GET)
    public String index(){
        return "inscription";
    }
    
    // Nouvelle inscription
    @RequestMapping(value="/inscription/new", method = RequestMethod.POST)
    public String newUser(HttpServletRequest request, ModelMap model){
        
        String pseudo = request.getParameter("pseudo");
        String mail = request.getParameter("mail");
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        
        // Test regex des entrées
        if(!testRegex(regex1, pseudo) ||
           !testRegex(regex1, nom) ||
           !testRegex(regex1, prenom) ||
           !testRegex(regex2, mail))
        {
            model.addAttribute("Erreur", "Echec Regex");
            return "/inscription";
        }
        
        // Test d'existence pour les champs uniques (pseudo, email).
        try{
            User testEmail = userDao.getUserByEmail(mail);
            if(testEmail != null){
                // Email déjà utilisé
                model.addAttribute("Erreur", "Email déjà utilisé");
                return "/inscription";
            }
            
            //try
            User testPseudo = userDao.getUserByEmail(pseudo);
            if(testPseudo != null){
                // Email déjà utilisé
                model.addAttribute("Erreur", "Pseudo déjà utilisé");
                return "/inscription";
            }
        }
        catch(Exception e){
            // Erreur pendant le requetage.
            model.addAttribute("Erreur", "Communication BDD");
            return "/inscription";
        }
        
        // TODO: génération de la clé mot de passe ici
        
        // Création de l'utilisateur
        em.getTransaction().begin();
        User newUser = userDao.createNewUser(request.getParameter("pseudo"),
                                            request.getParameter("mail"), 
                                            request.getParameter("nom"), 
                                            request.getParameter("prenom"), 
                                            new Date(), 
                                            new Date(),
                                            "motDePasse");
               
        if(newUser != null){
            // L'utilisateur a bien été créé
            try{
                em.getTransaction().commit();
                // Réussite, redirection page principale
                return "redirect:/";
            }
            catch(Exception e){
                // Erreur pendant le commit
                model.addAttribute("Erreur", "Communication BDD");
                return "/inscription";
            }
        }
        else{
            // L'utilisateur n'a pas été créé
            model.addAttribute("Erreur", "Communication BDD");
            return "/inscription";
        }
    }
    
    private boolean testRegex(String regex, String aTester){
        return Pattern.compile(regex).matcher(aTester).matches();
    }
}