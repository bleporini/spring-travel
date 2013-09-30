package fr.springframework.webflow.samples.booking;

import javax.persistence.PostLoad;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * User: blep
 * Date: 27/09/13
 * Time: 08:50
 */

public class HotelListener {

    public static AtomicBoolean bugEnabled = new AtomicBoolean(true);

    @PostLoad
    public void onPostLoad(Hotel hotel){
        if(bugEnabled.get())
            hotel.getBookings().size();
//        System.out.println("hotel.getBookings().size() = " + hotel.getBookings().size());
/*
        final RuntimeException yes = new RuntimeException("yes");
        yes.printStackTrace();
        throw yes;
*/
    }
}
