package controllers;

import dao.UserDao;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

/**
 *
 * @author zekri
 */
@Controller
public class LoginController {
    
    @Autowired
    UserDao userDao;
    
    /**
     * Authenticate with a password and a stored password token.
     *
     * @return true if the password and token match
     */
    private boolean authenticate(String motDePasse, String token)
    {
        //do magic (inspiration : http://stackoverflow.com/questions/2860943/how-can-i-hash-a-password-in-java)

        String[] saltAndPass = token.split("\\$");
        if (saltAndPass.length != 2) {
            throw new IllegalStateException("The stored password must be of the form 'salt$hash'");
        }
        byte[] salt = Base64.getDecoder().decode(saltAndPass[0]);

        //on recalcule le mot de passe avec la même méthode de génération
        KeySpec spec = new PBEKeySpec(motDePasse.toCharArray(), salt, 65536, 128);
        try {
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            String hashOfInput = Base64.getEncoder().encodeToString(f.generateSecret(spec).getEncoded());

            return hashOfInput.equals(saltAndPass[1]);
        }
        catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Missing algorithm: PBKDF2WithHmacSHA256", e);
        }
        catch (InvalidKeySpecException e) {
            throw new IllegalStateException("Invalid SecretKeyFactory", e);
        }
    }


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
            } else if (authenticate(request.getParameter("password"), userDao.getUserByPseudo(request.getParameter("username")).getCleMotDePasse())) {
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
