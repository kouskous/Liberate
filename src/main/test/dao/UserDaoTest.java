package dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

/**
 * Created by SlowFlow on 22/11/2016.
 */
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/dispatcher-servlet.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class UserDaoTest {
    @Test
    @Transactional
    @Rollback
    public void getUserByEmail() throws Exception {

    }

    @Test
    @Transactional
    @Rollback
    public void getUserByPseudo() throws Exception {

    }

    @Test
    @Transactional
    @Rollback
    public void createNewUser() throws Exception {

    }

    @Test
    @Transactional
    @Rollback
    public void deleteUserByEmail() throws Exception {

    }

    @Test
    @Transactional
    @Rollback
    public void changeUserEmail() throws Exception {

    }

    @Test
    @Transactional
    @Rollback
    public void emailAlreadyUsed() throws Exception {

    }

    @Test
    @Transactional
    @Rollback
    public void pseudoAlreadyUsed() throws Exception {

    }

    @Test
    @Transactional
    @Rollback
    public void searchUsersByName() throws Exception {

    }

    @Test
    @Transactional
    @Rollback
    public void getAllPseudo() throws Exception {

    }

}