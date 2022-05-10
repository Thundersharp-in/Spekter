package thundersharp.aigs.spectre.ui.activities.passes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.helpers.QRCodeGenerator;
import thundersharp.aigs.spectre.core.models.TicketsData;
import thundersharp.aigs.spectre.core.utils.TimeUtils;

public class TicketViewer extends AppCompatActivity {

    private TicketsData ticketsData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_viewer);
        ticketsData = (TicketsData) getIntent().getSerializableExtra("data");

        try {
            findViewById(R.id.rrr).setOnClickListener(n -> finish());
            ((TextView) findViewById(R.id.id)).setText(ticketsData.booking_id.toUpperCase());
            ((TextView) findViewById(R.id.dec)).setText(ticketsData.event_Description);
            ((TextView) findViewById(R.id.event_name)).setText(ticketsData.event_name.toUpperCase());
            ((TextView) findViewById(R.id.guest_name)).setText(ticketsData.guest_name.toUpperCase());
            ((TextView) findViewById(R.id.venue)).setText(ticketsData.venue.toUpperCase());
            ((TextView) findViewById(R.id.guest_phone)).setText("XXXX" + ticketsData.guest_phone.substring(7));
            ((TextView) findViewById(R.id.entry_gate)).setText("1B");

            ((ImageView) findViewById(R.id.qr)).setImageBitmap(QRCodeGenerator.getQrCodeFromData(ticketsData.booking_id));

            String startTime = ticketsData.guest_time.substring(0,ticketsData.guest_time.indexOf("-"));
            String endTime = ticketsData.guest_time.substring(ticketsData.guest_time.indexOf("-")+1);

            TextView timeSlot = findViewById(R.id.slot);

            if (Integer.parseInt(startTime) > 12) {
                timeSlot.setText((Integer.parseInt(startTime) - 12) + "-" + (Integer.parseInt(endTime) - 12)+"PM");
            }else if (Integer.parseInt(startTime) == 12){
                timeSlot.setText(startTime + "-" + (Integer.parseInt(endTime) - 12)+"PM");
            }else {
                timeSlot.setText(startTime + "-" + endTime+"AM");
            }

            ((TextView) findViewById(R.id.event_start)).setText(TimeUtils.getTimeFromTimeStamp(ticketsData.event_start_time));
        }catch (Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}