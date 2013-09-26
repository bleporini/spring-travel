package fr.springframework.webflow.samples.booking;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import fr.springframework.webflow.samples.util.BugEnum;

/**
 * A JPA-based implementation of the Booking Service. Delegates to a JPA entity manager to issue data access calls
 * against the backing repository. The EntityManager reference is provided by the managing container (Spring)
 * automatically.
 */
@Service("bookingService")
@Repository
public class JpaBookingService implements BookingService {

    /**
     * Logger
     */
    private static Logger logger = LoggerFactory.getLogger(JpaBookingService.class);

    private EntityManager em;

    private AtomicBoolean isBookingsBugEnabled;

    private AtomicBoolean isHotelsBugEnabled;

    private AtomicBoolean isBookingsBug2Enabled;

    /**
     * {@link BugService}
     */
    @Autowired
    private BugService bugService;

    /**
     * Initialize bugs status.
     */
    @PostConstruct
    public void init() {
        isBookingsBugEnabled = new AtomicBoolean(bugService.getStatusByCode(BugEnum.BOOKING_SERVICE_ENABLED_BOOKINGS));
        isHotelsBugEnabled = new AtomicBoolean(bugService.getStatusByCode(BugEnum.BOOKING_SERVICE_ENABLED_HOTELS));
        isBookingsBug2Enabled = new AtomicBoolean(bugService.getStatusByCode(BugEnum.BOOKING_NO_LIMIT));
    }

    public void setHotelsEnabled(boolean enabled) {
        bugService.setStatusByCode(BugEnum.BOOKING_SERVICE_ENABLED_HOTELS, enabled);
        isHotelsBugEnabled.set(enabled);
    }

    public void setBookingsEnabled(boolean enabled) {
        bugService.setStatusByCode(BugEnum.BOOKING_SERVICE_ENABLED_BOOKINGS, enabled);
        this.isBookingsBugEnabled.set(enabled);
    }

    @Override
    public void setBookings2Enabled(boolean enabled) {
        bugService.setStatusByCode(BugEnum.BOOKING_NO_LIMIT, enabled);
        this.isBookingsBug2Enabled.set(enabled);
    }

    @Override
    public boolean isBookingsEnabled() {
        return isBookingsBugEnabled.get();
    }

    @Override
    public boolean isBookings2Enabled() {
        return isBookingsBug2Enabled.get();
    }

    @Override
    public boolean isHotelsEnabled() {
        return isHotelsBugEnabled.get();
    }


    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<Booking> findBookings(String username) {
        if (username != null) {
            final String hqlQuery;

            if (isBookingsBugEnabled.get()) {
                hqlQuery = "select distinct b from Booking b" +
                            " left join fetch b.hotel f" +
                            " left join fetch f.bookings bb" +
                            " left join fetch bb.user u " +
                            " left join fetch u.bookings  d" +
                            " left join fetch d.hotel  h " +
                            "where b.user.username = :username " +
                            "order by b.checkinDate";
            } else {
                hqlQuery = "select  b from Booking b where b.user.username = :username order by b.checkinDate";
            }

            final Query bookings = em.createQuery(hqlQuery).setParameter("username", username);
            return isBookingsBug2Enabled.get()?
                    bookings.getResultList():
                    bookings.setMaxResults(5).getResultList();
        } else {
            return null;
        }
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<Hotel> findHotels(SearchCriteria criteria) {
        final String pattern = getSearchPattern(criteria);
        final String hqlQuery = "select h from Hotel h where " +
                "lower(h.name) like " + pattern +
                " or lower(h.city) like " + pattern +
                " or lower(h.zip) like " + pattern +
                " or lower(h.address) like " + pattern;
        final List resultList;

        if (isHotelsBugEnabled.get()) {
            List<Hotel> hotels = em.createQuery(hqlQuery).getResultList();
            resultList = new ArrayList<Hotel>();
            int i = 0;

            for (Hotel h : hotels) {
                if (i >= criteria.getPage() * criteria.getPageSize()) {
                    resultList.add(h);
                    if (resultList.size() == criteria.getPageSize()) {
                        break;
                    }
                }
                i++;
            }
        } else {
            resultList = em.createQuery(hqlQuery)
                    .setMaxResults(criteria.getPageSize())
                    .setFirstResult(criteria.getPage() * criteria.getPageSize())
                    .getResultList();
        }

        return resultList;
    }

    @Transactional(readOnly = true)
    public Hotel findHotelById(Long id) {
        return em.find(Hotel.class, id);
    }

    @Transactional(readOnly = true)
    public Booking createBooking(Long hotelId, String username) {
        Hotel hotel = em.find(Hotel.class, hotelId);
        User user = findUser(username);
        Booking booking = new Booking(hotel, user);
        em.persist(booking);
        return booking;
    }

    @Transactional
    public void cancelBooking(Long id) {
        Booking booking = em.find(Booking.class, id);
        if (booking != null) {
            em.remove(booking);
        }

    }

    // helpers

    private String getSearchPattern(SearchCriteria criteria) {
        if (StringUtils.hasText(criteria.getSearchString())) {
            return "'%" + criteria.getSearchString().toLowerCase().replace('*', '%') + "%'";
        } else {
            return "'%'";
        }
    }

    private User findUser(String username) {
        return (User) em.createQuery("select u from User u where u.username = :username")
                .setParameter("username", username)
                .getSingleResult();
    }



}