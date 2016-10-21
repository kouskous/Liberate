package dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by SlowFlow on 21/10/2016.
 */
@ContextConfiguration(locations = "classpath:application-context-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class FichierUserDaoTest {

    @Autowired
    private FichierUserDao dao;

    @org.junit.Before
    public void setUp() throws Exception {

    }

    @Test
    public void getArborescence() throws Exception {

    }

    @Test
    public void getArborescence1() throws Exception {

    }
}