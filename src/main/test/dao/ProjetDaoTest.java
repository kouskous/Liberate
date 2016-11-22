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
public class ProjetDaoTest {
    @Test
    @Transactional
    @Rollback
    public void getProjetByName() throws Exception {

    }

    @Test
    @Transactional
    @Rollback
    public void createNewProjet() throws Exception {

    }

    @Test
    @Transactional
    @Rollback
    public void deleteProjetByName() throws Exception {

    }

    @Test
    @Transactional
    @Rollback
    public void projetNameAlreadyUsed() throws Exception {

    }

}