package dao;

import models.FichiersUsers;
import models.Projet;
import models.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by SlowFlow on 22/11/2016.
 */
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/dispatcher-servlet.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ProjetDaoTest {

    @Autowired
    private ProjetDao projetDAO;

    @Test
    @Transactional
    @Rollback
    public void getProjetByName() throws Exception {
        projetDAO.createNewProjet("nouveauProjet", Date.from(Instant.now()), Date.from(Instant.now()), "Java");
        Projet nouveauProjet = projetDAO.getProjetByName("nouveauProjet");

        Assert.assertEquals("nouveauProjet", nouveauProjet.getNom());
    }

    @Test
    @Transactional
    @Rollback
    public void createNewProjet() throws Exception {
        Projet nouveauProjet = projetDAO.createNewProjet("nouveauProjet", Date.from(Instant.now()), Date.from(Instant.now()), "Java");

        Assert.assertEquals("nouveauProjet", nouveauProjet.getNom());
    }

    @Test
    @Transactional
    @Rollback
    public void deleteProjetByName() throws Exception {
        projetDAO.createNewProjet("nouveauProjet", Date.from(Instant.now()), Date.from(Instant.now()), "Java");
        boolean resultTrue = projetDAO.deleteProjetByName("nouveauProjet");
        boolean resultFalse = projetDAO.deleteProjetByName("projetInexistant");

        Assert.assertTrue(resultTrue);
        Assert.assertFalse(resultFalse);

        Assert.assertNull(projetDAO.getProjetByName("nouveauProjet"));
    }

    @Test
    @Transactional
    @Rollback
    public void projetNameAlreadyUsed() throws Exception {
        projetDAO.createNewProjet("nouveauProjet", Date.from(Instant.now()), Date.from(Instant.now()), "Java");
        boolean resultTrue = projetDAO.projetNameAlreadyUsed("nouveauProjet");
        boolean resultFalse = projetDAO.projetNameAlreadyUsed("projetInexistant");

        Assert.assertTrue(resultTrue);
        Assert.assertFalse(resultFalse);
    }

}