package dao;

import models.FichiersUsers;
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
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by SlowFlow on 14/11/2016.
 */

@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/application-context-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class FichierUserDaoTest {

    @Autowired
    private FichierUserDao fichierUserDAO;

    @Autowired
    private UserDao userDAO;

    @Test
    @Transactional
    @Rollback(true)
    public void getFichiersByUser() throws Exception {

    }

    @Test
    @Transactional
    @Rollback(true)
    public void getFichiersByUserAndPath() throws Exception {

    }

    @Test
    @Transactional
    @Rollback(true)
    public void getFichierUserByNomPhysique() throws Exception {

    }

    @Test
    @Transactional
    @Rollback(true)
    public void getArborescence() throws Exception {

    }

    @Test
    @Transactional
    @Rollback(true)
    public void getArborescence1() throws Exception {

    }

    @Test
    @Transactional
    @Rollback(true)
    public void createNewFichierUser() throws Exception {
        User medini = userDAO.createNewUser("lmedini", "lmedini@test.test", "Medini", "Lionel", Date.from(Instant.now()), Date.from(Instant.now()), "mdp");

        fichierUserDAO.createNewFichierUser("/dossier1/fichierTest", "fichierTest", "fichierTest", Date.from(Instant.now()), FichiersUsers.Type.FICHIER, medini);
        fichierUserDAO.createNewFichierUser("/dossier1/dossierTest", "dossierTest", "dossierTest", Date.from(Instant.now()), FichiersUsers.Type.DOSSIER, medini);

        List<FichiersUsers> fichiersMedini = new ArrayList<>(fichierUserDAO.getFichiersByUser(medini));

        Assert.assertEquals("/dossier1/fichierTest", fichiersMedini.get(0).getPathLogique());
        Assert.assertEquals("/dossier1/dossierTest", fichiersMedini.get(1).getPathLogique());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void deleteFichierUserByNomPhysique() throws Exception {

    }

    @Test
    @Transactional
    @Rollback(true)
    public void changePathLogique() throws Exception {

    }

}