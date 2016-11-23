package dao;

import models.FichiersUsers;
import models.Projet;
import models.User;
import models.UserProjet;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;

/**
 * Created by SlowFlow on 22/11/2016.
 */
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/dispatcher-servlet.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class UserProjetDaoTest {

    @Autowired
    private UserProjetDao userProjetDao;

    @Autowired
    private UserDao userDAO;

    @Autowired
    private ProjetDao projetDAO;

    @Test
    @Transactional
    @Rollback
    public void getUserProjetByUIdPId() throws Exception {
        User florian = userDAO.createNewUser("fbautry", "florian@test.test", "Bautry", "Florian", Date.from(Instant.now()), Date.from(Instant.now()), "mdp");

        Projet projetFlorian = projetDAO.createNewProjet("MonProjet", Date.from(Instant.now()), Date.from(Instant.now()), "Java");

        userProjetDao.createNewUserProjet("Admin", Date.from(Instant.now()), Date.from(Instant.now()), florian, projetFlorian);

        Assert.assertEquals("Admin", userProjetDao.getUserProjetByUIdPId(florian.getIdUser(), projetFlorian.getIdProjet()).getTypeDroit());
    }

    @Test
    @Transactional
    @Rollback
    public void getProjetsByUser() throws Exception {
        User florian = userDAO.createNewUser("fbautry", "florian@test.test", "Bautry", "Florian", Date.from(Instant.now()), Date.from(Instant.now()), "mdp");
        User remi = userDAO.createNewUser("remiSansFichiers", "remi@test.test", "SansFichiers", "Remi", Date.from(Instant.now()), Date.from(Instant.now()), "mdp");

        Projet projetFlorian1 = projetDAO.createNewProjet("MonProjet1", Date.from(Instant.now()), Date.from(Instant.now()), "Java");
        Projet projetFlorian2 = projetDAO.createNewProjet("MonProjet2", Date.from(Instant.now()), Date.from(Instant.now()), "Java");

        userProjetDao.createNewUserProjet("Admin", Date.from(Instant.now()), Date.from(Instant.now()), florian, projetFlorian1);
        userProjetDao.createNewUserProjet("Admin", Date.from(Instant.now()), Date.from(Instant.now()), florian, projetFlorian2);

        Collection<Projet> projets = userProjetDao.getProjetsByUser(florian);
        Collection<Projet> projetsVide = userProjetDao.getProjetsByUser(remi);

        Assert.assertTrue(projets.contains(projetFlorian1));
        Assert.assertTrue(projets.contains(projetFlorian2));

        Assert.assertTrue(projetsVide.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void createNewUserProjet() throws Exception {
        User florian = userDAO.createNewUser("fbautry", "florian@test.test", "Bautry", "Florian", Date.from(Instant.now()), Date.from(Instant.now()), "mdp");

        Projet projetFlorian = projetDAO.createNewProjet("MonProjet", Date.from(Instant.now()), Date.from(Instant.now()), "Java");

        userProjetDao.createNewUserProjet("Admin", Date.from(Instant.now()), Date.from(Instant.now()), florian, projetFlorian);

        Assert.assertNotNull(userProjetDao.getUserProjetByUIdPId(florian.getIdUser(), projetFlorian.getIdProjet()));
    }

    @Test
    @Transactional
    @Rollback
    public void deleteUserProjet() throws Exception {
        User florian = userDAO.createNewUser("fbautry", "florian@test.test", "Bautry", "Florian", Date.from(Instant.now()), Date.from(Instant.now()), "mdp");

        Projet projetFlorian = projetDAO.createNewProjet("MonProjet", Date.from(Instant.now()), Date.from(Instant.now()), "Java");
        Projet projetAutreUtilisateur = projetDAO.createNewProjet("AutreProjet", Date.from(Instant.now()), Date.from(Instant.now()), "Java");

        userProjetDao.createNewUserProjet("Admin", Date.from(Instant.now()), Date.from(Instant.now()), florian, projetFlorian);

        boolean resultTrue = userProjetDao.deleteUserProjet(florian, projetFlorian);
        boolean resultFalse = userProjetDao.deleteUserProjet(florian, projetAutreUtilisateur);

        Assert.assertTrue(resultTrue);
        Assert.assertFalse(resultFalse);

        Assert.assertNull(userProjetDao.getUserProjetByUIdPId(florian.getIdUser(), projetFlorian.getIdProjet()));
    }

    @Test
    @Transactional
    @Rollback
    public void changeDroitsUserProjet() throws Exception {
        User florian = userDAO.createNewUser("fbautry", "florian@test.test", "Bautry", "Florian", Date.from(Instant.now()), Date.from(Instant.now()), "mdp");

        Projet projetFlorian = projetDAO.createNewProjet("MonProjet", Date.from(Instant.now()), Date.from(Instant.now()), "Java");

        UserProjet userProjetFlorian = userProjetDao.createNewUserProjet("Admin", Date.from(Instant.now()), Date.from(Instant.now()), florian, projetFlorian);

        userProjetDao.changeDroitsUserProjet("Dev", userProjetFlorian);

        Assert.assertEquals("Dev", userProjetDao.getUserProjetByUIdPId(florian.getIdUser(), projetFlorian.getIdProjet()).getTypeDroit());

        userProjetDao.changeDroitsUserProjet("Admin", florian.getIdUser(), projetFlorian.getIdProjet());

        Assert.assertEquals("Admin", userProjetDao.getUserProjetByUIdPId(florian.getIdUser(), projetFlorian.getIdProjet()).getTypeDroit());
    }
}