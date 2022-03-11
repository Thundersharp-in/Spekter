package thundersharp.aigs.spectre.core.models;

import android.media.Image;

import java.io.Serializable;

import thundersharp.aigs.spectre.core.starters.Tickets;

public class TicketsData implements Serializable {
    public TicketsData(){}

    public String event_start_time;
    public String guest_time;
    public String event_name;
    public String event_Description;
    public String guest_name;
    public String guest_email;
    public String guest_phone;
    public String venue;
    public Image qr_code;
    public String booking_id;

    public TicketsData(String event_start_time, String guest_time, String event_name, String event_Description, String guest_name, String guest_email, String guest_phone, String venue, Image qr_code, String booking_id) {
        this.event_start_time = event_start_time;
        this.guest_time = guest_time;
        this.event_name = event_name;
        this.event_Description = event_Description;
        this.guest_name = guest_name;
        this.guest_email = guest_email;
        this.guest_phone = guest_phone;
        this.venue = venue;
        this.qr_code = qr_code;
        this.booking_id = booking_id;
    }
}
