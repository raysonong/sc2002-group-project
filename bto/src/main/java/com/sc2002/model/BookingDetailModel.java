package com.sc2002.model;

import java.util.Date;

import com.sc2002.enums.FlatType;

public class BookingDetailModel {
    private FlatType bookedFlatType;
    private Date bookingDate;
    public BookingDetailModel(FlatType bookedFlatType){
        this.bookedFlatType=bookedFlatType;
        this.bookingDate = new Date();
    }
}
