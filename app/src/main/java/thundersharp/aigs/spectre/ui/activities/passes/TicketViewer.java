package thundersharp.aigs.spectre.ui.activities.passes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.models.TicketsData;

public class TicketViewer extends AppCompatActivity {

    private TicketsData ticketsData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_viewer);
        ticketsData = (TicketsData) getIntent().getSerializableExtra("data");


        findViewById(R.id.rrr).setOnClickListener(n->finish());
    }
}