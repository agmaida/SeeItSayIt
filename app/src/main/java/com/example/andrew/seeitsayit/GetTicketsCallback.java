package com.example.andrew.seeitsayit;

import org.json.JSONArray;

/**
 * Created by andyd on 4/21/2016.
 */
interface GetTicketsCallback {
    public abstract void done(JSONArray returnedTickets);
}
