package controllers;

import dao.UserDao;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author zekri
 */
@Controller
public class LoginController {
    
    @Autowired
    UserDao userDao;
    
    /**
     * Constructeur du controlleur de connexion
     */
    public LoginController(){

    }
    
    /**
     * Requête d'affichage de la page de connexion
     * @param request
     * @return Renvoie le nom de la Jsp de connexion, ou redirige sur l'écran principal si une session est déjà ouverte
     */
    @RequestMapping(value="/login", method = RequestMethod.GET)
    public String index(HttpServletRequest request){
        // On vérifie qu'une session n'est pas déjà ouverte
        HttpSession session= request.getSession();
        User user = (User)session.getAttribute("user");
 
        if(user == null) // Pas de session ouverte
            return "login";
        else // Une session déjà ouverte
            return "redirect:/";
    }
   
    /**
     * Requête de connexion d'un utilisateur
     * @param request
     * @param model
     * @return Connecte l'utilisateur et l'envoie à la page principale si réussite
     */
    @RequestMapping(value="/login", method = RequestMethod.POST)
    public String connexion(HttpServletRequest request, ModelMap model){
        String error;
        try {
            User userDb = userDao.getUserByPseudo(request.getParameter("username"));

            if(userDb == null){
                error = "Aucun compte n'est rattaché à ce pseudo";
            } else if (request.getParameter("password").equals((userDao.getUserByPseudo(request.getParameter("username"))).getCleMotDePasse())) {
                HttpSession session = request.getSession();
                session.setAttribute("user", userDb);
                return "redirect:/";
            } else {
                error = "mot de passe incorrect";
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur pendant la connexion: " + e);
            error = "Erreur : illegal Argument ! "+e.getLocalizedMessage();
        }catch (Exception e) {
            System.out.println("Erreur pendant la connexion: " + e);
            error = "Erreur : plusieurs utilisateurs ont le même pseudo ! "+e.getLocalizedMessage();
        }
        model.addAttribute("error", error);

        return "login";
            
    }
    
    /**
     * Requête de déconnexion d'un utilisateur
     * @param request
     * @return Ferme la session et renvoie le nom de la jsp de connexion.
     */
    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.invalidate();
        return "login";
            
    }
   
}
