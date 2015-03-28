package fr.springframework.webflow.samples.booking;

import java.security.Principal;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import fr.springframework.webflow.samples.util.BugEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;

import static java.lang.System.currentTimeMillis;
import static java.lang.System.nanoTime;

@Controller
public class HotelsController {

    private BookingService bookingService;

    public static AtomicBoolean fakeExceptionBugEnabled = new AtomicBoolean();

    @Autowired
    private BugService bugService;

    @Autowired
    public HotelsController(BookingService bookingService) {
	this.bookingService = bookingService;
    }

    @PostConstruct
    public void init() {
        fakeExceptionBugEnabled = new AtomicBoolean(bugService.getStatusByCode(BugEnum.FAKE_EXCEPTIONS));
    }

    final static AtomicLong l = new AtomicLong(0);
    private final AbstractList<FakeException> exs = new ArrayList<FakeException>();

    private static class FakeException extends Exception {
        public FakeException() {
        }

        public FakeException(String s) {
            super(s);
        }
    }

    @RequestMapping(value = "/hotels/search", method = RequestMethod.GET)
    public void search(SearchCriteria searchCriteria, Principal currentUser, Model model) {
        if (fakeExceptionBugEnabled.get()) {
            try {
                for (int i = 0; i < 150 + currentTimeMillis() % 2; i++) {
                    exs.add(new FakeException());
                }
                throw new FakeException("" + exs.size());
            } catch (FakeException e) {
                l.compareAndSet(l.get(), new Long(e.getMessage()));
                exs.clear();
            }
        }


        if (currentUser != null) {
	    List<Booking> booking = bookingService.findBookings(currentUser.getName());
	    model.addAttribute(booking);
	}
    }

    @RequestMapping(value = "/hotels", method = RequestMethod.GET)
    public String list(SearchCriteria criteria, Model model) {
	List<Hotel> hotels = bookingService.findHotels(criteria);
	model.addAttribute(hotels);
	return "hotels/list";
    }

    @RequestMapping(value = "/hotels/{id}", method = RequestMethod.GET)
    public String show(@PathVariable Long id, Model model) {
	model.addAttribute(bookingService.findHotelById(id));
	return "hotels/show";
    }

    @RequestMapping(value = "/bookings/{id}", method = RequestMethod.DELETE)
    public String deleteBooking(@PathVariable Long id) {
	bookingService.cancelBooking(id);
	return "redirect:../hotels/search";
    }

}
