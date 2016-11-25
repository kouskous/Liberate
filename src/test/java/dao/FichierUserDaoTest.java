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
import java.util.*;

/**
 * Created by SlowFlow on 14/11/2016.
 */

@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/dispatcher-servlet.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class FichierUserDaoTest {

    @Autowired
    private FichierUserDao fichierUserDAO;

    @Autowired
    private UserDao userDAO;

    @Test
    @Transactional
    @Rollback
    public void getFichiersByUser() throws Exception {
        User florian = userDAO.createNewUser("fbautry", "florian@test.test", "Bautry", "Florian", Date.from(Instant.now()), Date.from(Instant.now()), "mdp");

        User remiSansFichier = userDAO.createNewUser("remi69","remi@test.test", "Sansfichiers", "Rémi", Date.from(Instant.now()), Date.from(Instant.now()),"unmotdepasse");

        fichierUserDAO.createNewFichierUser("/dossier1/fichierTest", "nomPhysiqueTest", "nomReelTest", Date.from(Instant.now()), FichiersUsers.Type.FICHIER, florian, 0);
        fichierUserDAO.createNewFichierUser("/dossier1/dossierTest", "dossierTest", "dossierTest", Date.from(Instant.now()), FichiersUsers.Type.DOSSIER, florian, 4);

        List<FichiersUsers> fichiersFlorian = new ArrayList<>(fichierUserDAO.getFichiersByUser(florian));

        Collection<FichiersUsers> fichiersRemiSansFichier = fichierUserDAO.getFichiersByUser(remiSansFichier);

        Assert.assertEquals("/dossier1/fichierTest", fichiersFlorian.get(0).getPathLogique());
        Assert.assertEquals("/dossier1/dossierTest", fichiersFlorian.get(1).getPathLogique());
        Assert.assertNotEquals("/dossier1/fichierInexistant", fichiersFlorian.get(0).getPathLogique());

        Assert.assertEquals("nomPhysiqueTest" ,fichiersFlorian.get(0).getNomPhysique());
        Assert.assertEquals("nomReelTest" ,fichiersFlorian.get(0).getNomReel());
        Assert.assertEquals(FichiersUsers.Type.FICHIER ,fichiersFlorian.get(0).getType());
        Assert.assertEquals(florian ,fichiersFlorian.get(0).getUser());

        Assert.assertTrue(fichiersRemiSansFichier.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void getFichiersByUserAndPath() throws Exception {
        User florian = userDAO.createNewUser("fbautry", "florian@test.test", "Bautry", "Florian", Date.from(Instant.now()), Date.from(Instant.now()), "mdp");

        User remiSansFichier = userDAO.createNewUser("remi69","remi@test.test", "Sansfichiers", "Rémi", Date.from(Instant.now()), Date.from(Instant.now()),"unmotdepasse");

        fichierUserDAO.createNewFichierUser("/dossier1/fichierTest", "nomPhysiqueTest", "nomReelTest", Date.from(Instant.now()), FichiersUsers.Type.FICHIER, florian, 0);
        fichierUserDAO.createNewFichierUser("/dossier1/dossierTest", "dossierTest", "dossierTest", Date.from(Instant.now()), FichiersUsers.Type.DOSSIER, florian, 4);

        FichiersUsers fichierFlorian = fichierUserDAO.getFichiersByUserAndPath(florian, "/dossier1/fichierTest");
        FichiersUsers dossierFlorian = fichierUserDAO.getFichiersByUserAndPath(florian, "/dossier1/dossierTest");

        FichiersUsers fichierRemiSansFichier = fichierUserDAO.getFichiersByUserAndPath(remiSansFichier, "/fichierInexistant");

        Assert.assertEquals("/dossier1/fichierTest", fichierFlorian.getPathLogique());
        Assert.assertEquals("/dossier1/dossierTest", dossierFlorian.getPathLogique());
        Assert.assertNotEquals("/dossier1/fichierInexistant", fichierFlorian.getPathLogique());

        Assert.assertEquals("nomPhysiqueTest" ,fichierFlorian.getNomPhysique());
        Assert.assertEquals("nomReelTest" ,fichierFlorian.getNomReel());
        Assert.assertEquals(FichiersUsers.Type.FICHIER ,fichierFlorian.getType());
        Assert.assertEquals(florian ,fichierFlorian.getUser());
        Assert.assertEquals(FichiersUsers.Type.DOSSIER ,dossierFlorian.getType());

        Assert.assertNull(fichierRemiSansFichier);
    }

    @Test
    @Transactional
    @Rollback
    public void getFichierUserByNomPhysique() throws Exception {
        User florian = userDAO.createNewUser("fbautry", "florian@test.test", "Bautry", "Florian", Date.from(Instant.now()), Date.from(Instant.now()), "mdp");

        fichierUserDAO.createNewFichierUser("/dossier1/fichierTest", "nomPhysiqueTest", "nomReelTest", Date.from(Instant.now()), FichiersUsers.Type.FICHIER, florian, 0);
        fichierUserDAO.createNewFichierUser("/dossier1/dossierTest", "dossierTest", "dossierTest", Date.from(Instant.now()), FichiersUsers.Type.DOSSIER, florian, 4);

        FichiersUsers fichierFlorian = fichierUserDAO.getFichierUserByNomPhysique("nomPhysiqueTest");
        FichiersUsers dossierFlorian = fichierUserDAO.getFichierUserByNomPhysique("dossierTest");

        FichiersUsers fichierInexistant = fichierUserDAO.getFichierUserByNomPhysique("fichierInexistant");

        Assert.assertEquals("/dossier1/fichierTest", fichierFlorian.getPathLogique());
        Assert.assertEquals("/dossier1/dossierTest", dossierFlorian.getPathLogique());
        Assert.assertNotEquals("/dossier1/fichierInexistant", fichierFlorian.getPathLogique());

        Assert.assertEquals("nomPhysiqueTest" ,fichierFlorian.getNomPhysique());
        Assert.assertEquals("nomReelTest" ,fichierFlorian.getNomReel());
        Assert.assertEquals(FichiersUsers.Type.FICHIER ,fichierFlorian.getType());
        Assert.assertEquals(florian ,fichierFlorian.getUser());
        Assert.assertEquals(FichiersUsers.Type.DOSSIER ,dossierFlorian.getType());
        Assert.assertEquals("dossierTest" ,dossierFlorian.getNomPhysique());

        Assert.assertNull(fichierInexistant);
    }

    @Test
    @Transactional
    @Rollback
    public void getArborescence() throws Exception {
        User florian = userDAO.createNewUser("fbautry", "florian@test.test", "Bautry", "Florian", Date.from(Instant.now()), Date.from(Instant.now()), "mdp");

        FichiersUsers fichierTest = fichierUserDAO.createNewFichierUser("/dossier1/fichierTest", "fichierTest", "fichierTest", Date.from(Instant.now()), FichiersUsers.Type.FICHIER, florian, 0);
        FichiersUsers dossierTest = fichierUserDAO.createNewFichierUser("/dossier1/dossierTest", "dossierTest", "dossierTest", Date.from(Instant.now()), FichiersUsers.Type.DOSSIER, florian, 4);

        Collection<FichiersUsers> arborescenceFlorian = fichierUserDAO.getArborescence(florian);

        Assert.assertTrue(arborescenceFlorian.contains(fichierTest));
        Assert.assertTrue(arborescenceFlorian.contains(dossierTest));

        User remi = userDAO.createNewUser("remiSansFichiers", "remi@test.test", "Test", "Remi", Date.from(Instant.now()), Date.from(Instant.now()), "mdp");

        Collection<FichiersUsers> arborescenceRemi = fichierUserDAO.getArborescence(remi);

        Assert.assertTrue(arborescenceRemi.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void createNewFichierUser() throws Exception {
        User florian = userDAO.createNewUser("fbautry", "florian@test.test", "Bautry", "Florian", Date.from(Instant.now()), Date.from(Instant.now()), "mdp");

        fichierUserDAO.createNewFichierUser("/dossier1/fichierTest", "fichierTest", "fichierTest", Date.from(Instant.now()), FichiersUsers.Type.FICHIER, florian, 0);
        fichierUserDAO.createNewFichierUser("/dossier1/dossierTest", "dossierTest", "dossierTest", Date.from(Instant.now()), FichiersUsers.Type.DOSSIER, florian, 4);

        List<FichiersUsers> fichiersFlorian = new ArrayList<>(fichierUserDAO.getFichiersByUser(florian));

        Assert.assertEquals("/dossier1/fichierTest", fichiersFlorian.get(0).getPathLogique());
        Assert.assertEquals("/dossier1/dossierTest", fichiersFlorian.get(1).getPathLogique());
        Assert.assertNotEquals("/dossier1/fichierInexistant", fichiersFlorian.get(0).getPathLogique());
    }

    @Test
    @Transactional
    @Rollback
    public void deleteFichierUserByNomPhysique() throws Exception {
        User florian = userDAO.createNewUser("fbautry", "florian@test.test", "Bautry", "Florian", Date.from(Instant.now()), Date.from(Instant.now()), "mdp");

        fichierUserDAO.createNewFichierUser("/dossier1/fichierTest", "nomPhysiqueTest", "nomReelTest", Date.from(Instant.now()), FichiersUsers.Type.FICHIER, florian, 0);
        fichierUserDAO.createNewFichierUser("/dossier1/dossierTest", "dossierTest", "dossierTest", Date.from(Instant.now()), FichiersUsers.Type.DOSSIER, florian, 4);

        boolean resultTrueFichier = fichierUserDAO.deleteFichierUserByNomPhysique("nomPhysiqueTest");
        boolean resultTrueDossier = fichierUserDAO.deleteFichierUserByNomPhysique("dossierTest");
        boolean resultFalse = fichierUserDAO.deleteFichierUserByNomPhysique("fichierInexistant");

        Assert.assertTrue(resultTrueFichier);
        Assert.assertTrue(resultTrueDossier);
        Assert.assertFalse(resultFalse);

        Assert.assertNull(fichierUserDAO.getFichierUserByNomPhysique("nomPhysiqueTest"));
        Assert.assertNull(fichierUserDAO.getFichierUserByNomPhysique("dossierTest"));
    }

    @Test
    @Transactional
    @Rollback
    public void changePathLogique() throws Exception {
        User florian = userDAO.createNewUser("fbautry", "florian@test.test", "Bautry", "Florian", Date.from(Instant.now()), Date.from(Instant.now()), "mdp");

        FichiersUsers fichier = fichierUserDAO.createNewFichierUser("/dossier1/fichierTest", "nomPhysiqueTest", "nomReelTest", Date.from(Instant.now()), FichiersUsers.Type.FICHIER, florian, 0);

        fichierUserDAO.changePathLogique(fichier, "/new/path/logique");

        Assert.assertEquals("/new/path/logique", fichierUserDAO.getFichierUserByNomPhysique("nomPhysiqueTest").getPathLogique());
    }
}