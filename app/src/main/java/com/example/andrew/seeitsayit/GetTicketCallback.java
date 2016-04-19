package com.example.andrew.seeitsayit;

import org.json.JSONArray;

/**
 * Created by andyd on 4/15/2016.
 */
interface GetTicketCallback {
    public abstract void done(JSONArray returnedTickets);
}
