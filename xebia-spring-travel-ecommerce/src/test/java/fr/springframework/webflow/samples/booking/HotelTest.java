package fr.springframework.webflow.samples.booking;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * User: blep
 * Date: 27/09/13
 * Time: 08:55
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:db-test.xml")
public class HotelTest {

    @Repository
    public static class HotelGenerator{
        @PersistenceContext
        private EntityManager em;

        @Transactional
        public void generateHotel(){
            final Hotel hotel = new Hotel();
            hotel.setName("hotel");
            em.persist(hotel);
            final Booking booking = new Booking();
            booking.setHotel(hotel);
            em.persist(hotel);
        }

    }

    @Autowired
    private HotelGenerator hotelGenerator;
    
    @PersistenceContext
    private EntityManager em;

    @Before
    public void setUp() throws Exception {
        hotelGenerator.generateHotel();
    }

    /**
     * Just to check the listener side effect...
     * @throws Exception
     */
    @Test
    @Ignore
    public void test_entity_listener() throws Exception {
        assertNotNull(em);

        em.createQuery("from Hotel").getResultList();


    }
}
