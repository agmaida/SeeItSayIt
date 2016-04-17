package com.example.andrew.seeitsayit;

/**
 * Created by andyd on 4/15/2016.
 */
public class Ticket {
    //ID is auto updated in db when inserted
    String address;
    String category;
    String title;
    String description;
    int user_id;
    //datecreated
    //dateclosed
    float latitude;
    float longtitude;

    public Ticket(String address, String category, String title, String description,
                  int user_id, Float latitude, Float longtitude)
    {
        this.address = address;
        this.category = category;
        this.title = title;
        this.description = description;
        this.user_id = user_id;
        this.latitude = latitude;
        this.longtitude = longtitude;
    }
}
