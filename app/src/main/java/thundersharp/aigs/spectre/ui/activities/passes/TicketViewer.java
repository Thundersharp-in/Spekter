package thundersharp.aigs.spectre.ui.activities.passes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import thundersharp.aigs.spectre.R;

public class TicketViewer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_viewer);
        findViewById(R.id.rrr).setOnClickListener(n->finish());
    }
}