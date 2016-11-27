package dao;

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

/**
 * Created by SlowFlow on 22/11/2016.
 */
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/dispatcher-servlet.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @Test
    @Transactional
    @Rollback
    public void getUserByEmail() throws Exception {
        userDao.createNewUser("fbautry", "florian@test.test", "Bautry", "Florian", Date.from(Instant.now()), Date.from(Instant.now()), "mdp");

        User florian = userDao.getUserByEmail("florian@test.test");

        Assert.assertEquals("fbautry", florian.getPseudo());
    }

    @Test
    @Transactional
    @Rollback
    public void getUserByPseudo() throws Exception {
        userDao.createNewUser("fbautry", "florian@test.test", "Bautry", "Florian", Date.from(Instant.now()), Date.from(Instant.now()), "mdp");

        User florian = userDao.getUserByPseudo("fbautry");

        Assert.assertEquals("fbautry", florian.getPseudo());
    }

    @Test
    @Transactional
    @Rollback
    public void createNewUser() throws Exception {
        User florian = userDao.createNewUser("fbautry", "florian@test.test", "Bautry", "Florian", Date.from(Instant.now()), Date.from(Instant.now()), "mdp");

        Assert.assertEquals("fbautry", florian.getPseudo());
    }

    @Test
    @Transactional
    @Rollback
    public void deleteUserByEmail() throws Exception {
        userDao.createNewUser("fbautry", "florian@test.test", "Bautry", "Florian", Date.from(Instant.now()), Date.from(Instant.now()), "mdp");

        boolean resultTrue = userDao.deleteUserByEmail("florian@test.test");
        boolean resultFalse = userDao.deleteUserByEmail("inexistant@test.test");

        Assert.assertTrue(resultTrue);
        Assert.assertFalse(resultFalse);
    }

    @Test
    @Transactional
    @Rollback
    public void changeUserEmail() throws Exception {
        User florian = userDao.createNewUser("fbautry", "florian@test.test", "Bautry", "Florian", Date.from(Instant.now()), Date.from(Instant.now()), "mdp");
        userDao.createNewUser("autreUser", "autreUser@test.test", "User", "Autre", Date.from(Instant.now()), Date.from(Instant.now()), "mdp");

        boolean resultFalseAutreUser = userDao.changeUserEmail("autreUser@test.test", "florian@test.test");
        boolean resultTrue = userDao.changeUserEmail("florian@test.test", "newMail@test.test");
        boolean resultFalseFlorian = userDao.changeUserEmail("inexistant@test.test", "newMail@test.test");

        Assert.assertTrue(resultTrue);
        Assert.assertFalse(resultFalseAutreUser);
        Assert.assertFalse(resultFalseFlorian);

        Assert.assertEquals("newMail@test.test", florian.getEmail());
    }

    @Test
    @Transactional
    @Rollback
    public void emailAlreadyUsed() throws Exception {
        userDao.createNewUser("fbautry", "florian@test.test", "Bautry", "Florian", Date.from(Instant.now()), Date.from(Instant.now()), "mdp");

        boolean resultTrue = userDao.emailAlreadyUsed("florian@test.test");
        boolean resultFalse = userDao.emailAlreadyUsed("inexistant@test.test");

        Assert.assertTrue(resultTrue);
        Assert.assertFalse(resultFalse);
    }

    @Test
    @Transactional
    @Rollback
    public void pseudoAlreadyUsed() throws Exception {
        userDao.createNewUser("fbautry", "florian@test.test", "Bautry", "Florian", Date.from(Instant.now()), Date.from(Instant.now()), "mdp");

        boolean resultTrue = userDao.pseudoAlreadyUsed("fbautry");
        boolean resultFalse = userDao.pseudoAlreadyUsed("inexistant");

        Assert.assertTrue(resultTrue);
        Assert.assertFalse(resultFalse);
    }

    @Test
    @Transactional
    @Rollback
    public void getAllPseudo() throws Exception {
        userDao.createNewUser("fbautry", "florian@test.test", "Bautry", "Florian", Date.from(Instant.now()), Date.from(Instant.now()), "mdp");
        userDao.createNewUser("fbautry2", "florian@test.test", "Bautry", "Florian", Date.from(Instant.now()), Date.from(Instant.now()), "mdp");

        List<String> pseudos = userDao.getAllPseudo();

        Assert.assertTrue(pseudos.contains("fbautry"));
        Assert.assertTrue(pseudos.contains("fbautry2"));
    }
}