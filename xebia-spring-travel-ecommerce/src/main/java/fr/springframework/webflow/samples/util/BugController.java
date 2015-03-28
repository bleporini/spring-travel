package fr.springframework.webflow.samples.util;

import fr.springframework.webflow.samples.booking.*;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;
import fr.springframework.webflow.samples.booking.BookingService;

import javax.annotation.PostConstruct;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import java.util.concurrent.atomic.AtomicBoolean;


@ManagedResource("travel-ecommerce:type=BugController")
@Component
public class BugController {

    @Autowired
    private BookingActionController bookingAction;
    @Autowired
    private JpaLogger jpaLogger;
    @Autowired
    private DatabaseCacheAspect databaseCacheAspect;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private CacheFilter cacheFilter;

    /**
     * {@link TimeLoggerAspect}
     */
    @Autowired
    private TimeLoggerAspect timeLoggerAspect;

    @Autowired
    private BugService bugService;

    @Autowired
    private BasicDataSource ds;

    private static AtomicBoolean bugJdbcPoolSizeEnabled = new AtomicBoolean();

    @PostConstruct
    public void init() throws MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException {
        final Bug bug = bugService.findByCode(BugEnum.DS_SIZE);
        bugJdbcPoolSizeEnabled.set(bug == null || bug.isEnabled());
        configDataSource();
    }

    private void configDataSource() {
        int maxConn = bugJdbcPoolSizeEnabled.get()?8:200;
        ds.setMaxActive(maxConn);
        ds.setMaxIdle(maxConn);
    }



    public CacheFilter getCacheFilter() {
        return cacheFilter;
    }

    public void setCacheFilter(CacheFilter cacheFilter) {
        this.cacheFilter = cacheFilter;
    }

    public BookingActionController getBookingAction() {
        return bookingAction;
    }

    public void setBookingAction(BookingActionController bookingAction) {
        this.bookingAction = bookingAction;
    }

    public JpaLogger getJpaLogger() {
        return jpaLogger;
    }

    public void setJpaLogger(JpaLogger jpaLogger) {
        this.jpaLogger = jpaLogger;
    }

    public DatabaseCacheAspect getDatabaseCacheAspect() {
        return databaseCacheAspect;
    }

    public void setDatabaseCacheAspect(DatabaseCacheAspect databaseCacheAspect) {
        this.databaseCacheAspect = databaseCacheAspect;
    }

    public BookingService getBookingService() {
        return bookingService;
    }

    public void setBookingService(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public void setBugService(BugService bugService) {
        this.bugService = bugService;
    }

    private String getStatusString(boolean enabled){

        return (enabled ? "Enabled" : "Disabled");
    }

    @ManagedAttribute
    public String getBug1(){
        return getStatusString(bookingAction.isBugEnabled());
    }

    @ManagedAttribute
    public String getBug2(){
        return getStatusString(jpaLogger.isBugEnabled());
    }

    @ManagedAttribute
    public String getBug3(){
        return getStatusString(databaseCacheAspect.isBugEnabled());
    }

    @ManagedAttribute
    public String getBug4(){
        return getStatusString(bookingService.isBookingsEnabled());
    }

    @ManagedAttribute
    public String getBug5(){
        return getStatusString(bookingService.isHotelsEnabled());
    }

    @ManagedAttribute
    public String getBug6(){
        return getStatusString(cacheFilter.isBugEnabled());
    }

    /**
     * @return status of the 7th bug.
     */
    @ManagedAttribute
    public String getBug7(){
        return getStatusString(timeLoggerAspect.isBugEnabled());
    }

    @ManagedAttribute
    public String getBug8(){
        return getStatusString(bookingService.isBookings2Enabled());
    }

    @ManagedAttribute
    public String getBug9(){
        return getStatusString(HotelListener.bugEnabled.get());
    }

    @ManagedAttribute
    public String getBug10(){
        return getStatusString(bugJdbcPoolSizeEnabled.get());
    }

    @ManagedAttribute
    public String getBug11(){
        return getStatusString(HotelsController.fakeExceptionBugEnabled.get());
    }

    @ManagedOperation
    public String disableBug1(int securityCode) {
        if (securityCode == BugEnum.BOOKING_ACTION_CONTROLLER.getCode()) {
            this.bookingAction.setBugStatus(false);
            return "Bug 1 is now disabled";
        }
        this.bookingAction.setBugStatus(true);
        return "Don't think you are, know you are.";
    }

    @ManagedOperation
    public String disableBug2(int securityCode) {
        if (securityCode == BugEnum.JPA_LOGGER.getCode()) {
            this.jpaLogger.setEnabled(false);
            return "Bug 2 is now disabled";
        }
        this.jpaLogger.setEnabled(true);
        return "Men are like steel. When they lose their temper, they lose their worth.";
    }

    @ManagedOperation
    public String disableBug3(int securityCode) {
        if (securityCode == BugEnum.DATABASE_CACHE_ASPECT.getCode()) {
            this.databaseCacheAspect.setEnabled(false);
            return "Bug 3 is now disabled";
        }
        this.databaseCacheAspect.setEnabled(true);
        return "Genius is one percent inspiration and ninety-nine percent perspiration.";
    }

    @ManagedOperation
    public String disableBug4(int securityCode) {
        if (securityCode == BugEnum.BOOKING_SERVICE_ENABLED_BOOKINGS.getCode()) {
            this.bookingService.setBookingsEnabled(false);
            return "Bug 4 is now disabled";
        }
        this.bookingService.setBookingsEnabled(true);
        return "Keep your fears to yourself, but share your inspiration with others.";
    }


    @ManagedOperation
    public String disableBug5(int securityCode) {
        if (securityCode == BugEnum.BOOKING_SERVICE_ENABLED_HOTELS.getCode()) {
            this.bookingService.setHotelsEnabled(false);
            return "Bug 5 is now disabled";
        }
        this.bookingService.setHotelsEnabled(true);
        return "The real excitement is playing the game.";
    }

    @ManagedOperation
    public String disableBug6(int securityCode) {
        if (securityCode == BugEnum.CACHE_FILTER.getCode()) {
            this.cacheFilter.setBugEnabled(false);
            return "Bug 6 is now disabled";
        }
        this.cacheFilter.setBugEnabled(true);
        return "Envy can be a positive motivator. Let it inspire you to work harder for what you want.";
    }

    /**
     * Disable bug 7.
     *
     * @param securityCode
     * @return
     */
    @ManagedOperation
    public String disableBug7(int securityCode) {
        if (securityCode == BugEnum.METHOD_LOGGER.getCode()) {
            this.timeLoggerAspect.setEnabled(false);
            return "Bug 7 is now disabled";
        }

        this.timeLoggerAspect.setEnabled(true);

        return "We cannot do anything alone, but together we can do anything. Come on, friends, unity gives strength.";
    }

    /**
     *
     * @param securityCode
     * @return
     */
    @ManagedOperation
    public String disableBug8(int securityCode) {
        if (securityCode == BugEnum.BOOKING_NO_LIMIT.getCode()) {
            this.bookingService.setBookings2Enabled(false);
            return "Bug 8 is now disabled";
        }

        this.bookingService.setBookings2Enabled(true);

        return "We cannot do anything alone, but together we can do anything. Come on, friends, unity gives strength.";
    }

    @ManagedOperation
    public String disableBug9(int securityCode) {
        if (securityCode == BugEnum.JPA_EAGER_EMULATION.getCode()) {
            HotelListener.bugEnabled.set(false);
            bugService.setStatusByCode(BugEnum.JPA_EAGER_EMULATION, false);
            return "Bug 9 is now disabled";
        }

        HotelListener.bugEnabled.set(true);
        bugService.setStatusByCode(BugEnum.JPA_EAGER_EMULATION, true);

        return "We cannot do anything alone, but together we can do anything. Come on, friends, unity gives strength.";
    }

    @ManagedOperation
    public String disableBug10(int securityCode) {
        if (securityCode == BugEnum.DS_SIZE.getCode()) {
            bugJdbcPoolSizeEnabled.set(false);
            configDataSource();
            bugService.setStatusByCode(BugEnum.DS_SIZE,false);
            return "Bug 10 is now disabled";
        }

        bugJdbcPoolSizeEnabled.set(true);
        configDataSource();
        bugService.setStatusByCode(BugEnum.DS_SIZE,true);
        return "We cannot do anything alone, but together we can do anything. Come on, friends, unity gives strength.";
    }

    @ManagedOperation
    public String disableBug11(int securityCode) {
        if (securityCode == BugEnum.FAKE_EXCEPTIONS.getCode()) {
            HotelsController.fakeExceptionBugEnabled.set(false);
            bugService.setStatusByCode(BugEnum.FAKE_EXCEPTIONS,false);
            return "Bug 11 is now disabled";
        }

        HotelsController.fakeExceptionBugEnabled.set(true);
        bugService.setStatusByCode(BugEnum.FAKE_EXCEPTIONS,true);
        return "We cannot do anything alone, but together we can do anything. Come on, friends, unity gives strength.";
    }



    @ManagedOperation
    public String resetAllBugs(){
        bugService.resetBugs();
        this.bookingAction.setBugStatus(true);
        this.cacheFilter.setBugEnabled(true);
        this.bookingService.setHotelsEnabled(true);
        this.bookingService.setBookingsEnabled(true);
        this.bookingService.setBookings2Enabled(true);
        this.databaseCacheAspect.setEnabled(true);
        this.jpaLogger.setEnabled(true);
        this.bookingAction.setBugStatus(true);
        this.timeLoggerAspect.setEnabled(true);

        return "All bugs are now activated.";
    }

}
