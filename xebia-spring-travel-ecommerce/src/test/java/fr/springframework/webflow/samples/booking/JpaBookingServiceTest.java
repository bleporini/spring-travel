package fr.springframework.webflow.samples.booking;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static junit.framework.Assert.assertNotNull;

/**
 * @author blep
 *         Date: 13/09/13
 *         Time: 19:06
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/db-test.xml")
public class JpaBookingServiceTest {

    @PersistenceContext
    private EntityManager em;

    @Test
    public void testQuery() throws Exception {
        String pattern = "'%'";
/*
        final String hqlQuery = "select h from Hotel h where " +
                "lower(h.name) like " + pattern +
                " or lower(h.city) like " + pattern +
                " or lower(h.zip) like " + pattern +
                " or lower(h.address) like " + pattern;
*/
        final String hqlQuery = "from Hotel "/*where " +
                "lower(name) like " + pattern +
                " or lower(city) like " + pattern +
                " or lower(zip) like " + pattern +
                " or lower(address) like " + pattern*/;

//        System.out.println(hqlQuery);

        final List<Hotel> hotels = em.createQuery(hqlQuery, Hotel.class)
                .setMaxResults(5)
                .getResultList();

        System.out.println("hotels.size() = " + hotels.size());


    }
}
