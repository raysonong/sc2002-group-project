package com.sc2002.model;

import java.util.Date;

import com.sc2002.enums.FlatType;

/**
 * Represents the details of a flat booking made by an applicant.
 * Stores the type of flat booked and the date of booking.
 */
public class BookingDetailModel {
    /** The type of flat that was booked (e.g., TWO_ROOM, THREE_ROOM). */
    private FlatType bookedFlatType;
    /** The date and time when the booking was made. */
    private Date bookingDate;

    /**
     * Constructs a new BookingDetailModel instance.
     * Sets the booking date to the current date and time.
     *
     * @param bookedFlatType The type of flat being booked.
     */
    public BookingDetailModel(FlatType bookedFlatType){
        this.bookedFlatType=bookedFlatType;
        this.bookingDate = new Date();
    }
}
