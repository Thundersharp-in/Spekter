package thundersharp.aigs.spectre.core.starters;

import android.app.Activity;
import android.content.Intent;

import thundersharp.aigs.spectre.core.exceptions.ArgumentsMissingException;
import thundersharp.aigs.spectre.core.models.TicketsData;
import thundersharp.aigs.spectre.ui.activities.passes.TicketViewer;

public class Tickets {

    private TicketsData data;
    private Activity activity;

    public Tickets(Activity activity) {
        this.activity = activity;
    }

    public static Tickets getInstance(Activity activity){
        return new Tickets(activity);
    }

    public Tickets setTicketsData(TicketsData ticketsData){
        this.data = ticketsData;
        return this;
    }

    public Tickets showTickets() throws ArgumentsMissingException {
        if (activity != null && data != null){
            activity.startActivity(new Intent(activity, TicketViewer.class).putExtra("data",data));
        }else throw new ArgumentsMissingException("Missing arguments !!");

        return this;
    }

}
