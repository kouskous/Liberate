package controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import dao.FichierUserDao;
import dao.UserProjetDao;
import java.util.Collection;
import models.FichiersUsers;
import models.User;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletResponse;
import models.Projet;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author zekri
 */
@Controller
public class PrincipalController {
    
    @Autowired
    FichierUserDao fichierUserDao;
    
    @Autowired
    UserProjetDao userProjetDao;
    
    /**
     * Constructeur du controleur de la page principale
     */
    public PrincipalController(){
    }
    
    /**
     * Requête d'accès à la page principale
     * @param request
     * @return Renvoie le nom de la jsp correspondant à la page principale, ou redirige vers la page de connexion
     */
    @RequestMapping(value="/", method = RequestMethod.GET)
    public String index(HttpServletRequest request) {
        // On vérifie qu'une session n'est pas déjà ouverte

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) // Pas de session ouverte
            return "login";
         // Une session déjà ouverte
         return "principal";

    }
    
    /**
     * Requête d'obtention de l'arborescence d'un utilisateur
     * @param request
     * @param res
     * @return Renvoie un Json contenant l'arborescence d'un utilisateur
     */
    @ResponseBody
    @RequestMapping(value="/getTree", method = RequestMethod.GET,produces = "application/json")
    public String currentTree(HttpServletRequest request, HttpServletResponse res){
    
    // On vérifie qu'une session n'est pas déjà ouverte
    HttpSession session= request.getSession();
    User user = (User)session.getAttribute("user");
    // Pas de session ouverte
    if(user == null) return "redirect:/login";
        res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
        res.setHeader("Pragma", "no-cache"); // HTTP 1.0
        res.setDateHeader("Expires", 0); // Proxies
    
    JSONArray list = new JSONArray();
        
    Collection<FichiersUsers> arborescence = fichierUserDao.getArborescence(user);
    for (FichiersUsers fichier: arborescence) {
        JSONObject response = new JSONObject();
        if(fichier.getType() == FichiersUsers.Type.FICHIER){
            try{
                response.put("path","/Root"+fichier.getPathLogique());
                response.put("type","fichier");
                response.put("verrouillage",fichier.getVerrou());
            } catch(JSONException e){
                System.out.println("Erreur JSON: " + e);
            }
        }else {
            try{
                response.put("path", "/Root"+fichier.getPathLogique());
                response.put("type","dossier");
                response.put("verrouillage",fichier.getVerrou());
            } catch(JSONException e){
                System.out.println("Erreur JSON: " + e);
            }
        }
        list.put(response);
    }

    return  list.toString();
    }
    
    /**
     * Requête d'affichage de la pop-up pour le pull
     * @param request
     * @param data
     * @return Renvoie le nom de la Jsp à afficher
     */
    @RequestMapping(value="/pull", method = RequestMethod.GET)
    public String pull(HttpServletRequest request, ModelMap data) {
        // On vérifie qu'une session n'est pas déjà ouverte
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) // Pas de session ouverte
            return "login";
         // Une session déjà ouverte
         Collection<Projet> projets = userProjetDao.getProjetsByUser(user);
         data.addAttribute("projets", projets);
         return "pull";

    }
    
    

}
