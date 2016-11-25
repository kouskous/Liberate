package controllers;
import dao.UserDao;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.regex.Pattern;
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

/**
 *
 * @author Luc Di Sanza
 */
@Controller
public class InscriptionController {
    
    @Autowired
    UserDao userDao;
    
    // Regex1 utilisée pour nom, prenom, pseudo
    static String regex1 = "^([a-zA-Z]{3,50})";
    // Regex2 utilisée pour email
    static String regex2 = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)";
    
    /**
     * Hash a password for storage.
     *
     * @return a secure authentication token to be stored for later authentication
     */
    private String hash(String motDePasse)
    {
        //do magic (inspiration : http://stackoverflow.com/questions/2860943/how-can-i-hash-a-password-in-java)

        // le "salage" permet d'avoir un mot de passe plus sécurisé
        byte[] salt = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);

        KeySpec spec = new PBEKeySpec(motDePasse.toCharArray(), salt, 65536, 128);
        try {
            // PBKDF2WithHmacSHA256:
            // Password-based key-derivation algorithm found in PKCS #5 2.0
            // using the specified pseudo-random function SHA-256
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = f.generateSecret(spec).getEncoded();
            Base64.Encoder enc = Base64.getEncoder();
            return Base64.getEncoder().encodeToString(salt) + "$" + enc.encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Missing algorithm: PBKDF2WithHmacSHA256", e);
        } catch (InvalidKeySpecException e) {
            throw new IllegalStateException("Invalid SecretKeyFactory", e);
        }
    }

    /**
     * Constructeur du controleur d'inscriptions
     */
    public InscriptionController(){
    }
    
    /**
     * Requête d'affichage de la page d'inscription
     * @param request
     * @return Renvoie le nom de la jsp de la page d'inscription, ou redirige si une session est déjà ouverte
     */
    @RequestMapping(value="/inscription", method = RequestMethod.GET)
    public String index(HttpServletRequest request){
        
        // On vérifie qu'une session n'est pas déjà ouverte
        HttpSession session= request.getSession();
        User user = (User)session.getAttribute("user");
 
        if(user == null) // Pas de session ouverte
            return "inscription";
        else // Une session déjà ouverte
            return "redirect:/";
    }

    /**
     * Requête d'inscription d'un nouvel utilisateur
     * @param request
     * @param model
     * @return Redirige sur la page de connexion si réussite, ou sur la page d'inscription si echec.
     */
    @RequestMapping(value="/inscription", method = RequestMethod.POST)
    public String newUser(HttpServletRequest request, ModelMap model){
        
        // On vérifie qu'une session n'est pas déjà ouverte
        HttpSession session= request.getSession();
        User user = (User)session.getAttribute("user");
 
        if(user != null) // session déjà ouverte
            return "redirect:/";
        
        String pseudo = request.getParameter("pseudo");
        String mail = request.getParameter("mail");
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String motDePasse = request.getParameter("password");
        
        // Test regex des entrées
        if(!testRegex(regex1, pseudo) ||
           !testRegex(regex1, nom) ||
           !testRegex(regex1, prenom) ||
           !testRegex(regex2, mail) ||
           motDePasse.length() < 6)
        {
            model.addAttribute("Erreur", "Echec Regex");
            return "inscription";
        }
        
        // Test d'existence pour les champs uniques (pseudo, email).
        try{
            if(userDao.emailAlreadyUsed(mail)){
                // Email déjà utilisé
                model.addAttribute("Erreur", "Email déjà utilisé");
                return "inscription";
            }
            if(userDao.pseudoAlreadyUsed(pseudo)){
                // Pseudo déjà utilisé
                model.addAttribute("Erreur", "Pseudo déjà utilisé");
                return "inscription";
            }
        }
        catch(IllegalArgumentException e){
            // Erreur pendant le requetage.
            System.out.println("Erreur pendant le test d'existence du pseudo ou du mail: " + e);
            model.addAttribute("Erreur", "Communication BDD");
            return "inscription";
        }

        motDePasse = hash(motDePasse);

        // Création de l'utilisateur
        User newUser = userDao.createNewUser(request.getParameter("pseudo"),
                                            request.getParameter("mail"), 
                                            request.getParameter("nom"), 
                                            request.getParameter("prenom"), 
                                            new Date(), 
                                            new Date(),
                                            motDePasse);
           
        // Si l'utilisateur a bien été créé
        if(newUser != null){
            // Réussite, redirection page principale
            return "redirect:/login";
        }
        else{
            // L'utilisateur n'a pas été créé
            model.addAttribute("Erreur", "Communication BDD");
            return "inscription";
        }
    }
    
    /**
     * Test d'acceptation d'une chaîne de caractères par une expression régulière
     * @param regex Expression régulière considérée
     * @param aTester Chaîne de caractères à tester
     * @return Renvoie vrai si la chaine est acceptée, faux sinon
     */
    private boolean testRegex(String regex, String aTester){
        return Pattern.compile(regex).matcher(aTester).matches();
    }
}