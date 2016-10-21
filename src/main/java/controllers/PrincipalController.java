/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import dao.FichierUserDao;
import dao.UserDao;
import models.FichiersUsers;
import models.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 *
 * @author zekri
 */
@Controller
public class PrincipalController {
    @RequestMapping(value="/", method = RequestMethod.GET)
    public String index(HttpServletRequest request) {
        // On vérifie qu'une session n'est pas déjà ouverte

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        EntityManager em = Persistence.createEntityManagerFactory("persistenceUnitLiber8").createEntityManager();
        FichierUserDao fichierUserDao = new FichierUserDao(em);
        Map<String, Boolean> arborescence;

        if (user == null) // Pas de session ouverte
            return "login";
         // Une session déjà ouverte


             arborescence = fichierUserDao.getArborescence(user);

            for (Map.Entry<String, Boolean> fichier : arborescence.entrySet()) {
                if (fichier.getValue().booleanValue() == true)
                    System.out.print("Fichier : ");
                else
                    System.out.print("Dossier : ");
                System.out.println(fichier.getKey());
            }

        return "redirect:/";
    }
}
