package thundersharp.aigs.spectre.ui.activities.lectures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.adapters.PurchasedServiceAdapter;
import thundersharp.aigs.spectre.core.models.PurchaseData;
import thundersharp.aigs.spectre.core.models.PurchasedPlansModel;
import thundersharp.aigs.spectre.core.utils.CONSTANTS;

public class SubsribedLectures extends AppCompatActivity {

    private RecyclerView all_services;
    private List<PurchaseData> purchaseDataList = new ArrayList<>();
    private ShimmerFrameLayout shimmer;
    private ImageView img_not_found;
    //private Intent intent;
    private Toolbar tool;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subsribed_lectures);

        tool = findViewById(R.id.tool);

        tool.setNavigationOnClickListener(on->finish());

        all_services = findViewById(R.id.all_services);
        shimmer = findViewById(R.id.shimmer);
        img_not_found = findViewById(R.id.img_not_found);
        swipeRefreshLayout = findViewById(R.id.swipe);

     /*   if (getIntent().getSerializableExtra("services_list")!=null){
            purchaseDataList = (List<PurchaseData>) getIntent().getSerializableExtra("services_list");
            shimmer.stopShimmer();
            shimmer.setVisibility(View.GONE);
            img_not_found.setVisibility(View.GONE);
            all_services.setAdapter(new PurchasedServiceAdapter(PurchasedServices.this, purchaseDataList));

        }else {
            shimmer.stopShimmer();
            shimmer.setVisibility(View.GONE);
            img_not_found.setVisibility(View.VISIBLE);
            navController.navigate(R.id.nav_services);
            finish();
        }*/

        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                shimmer.startShimmer();
                shimmer.setVisibility(View.VISIBLE);
                loadData();
            }
        });

        loadData();
    }

    private synchronized void loadData(){
        img_not_found.setVisibility(View.GONE);
        purchaseDataList.clear();
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_USER_DATA)
                .child(FirebaseAuth.getInstance().getUid())
                .child(CONSTANTS.DATABASE_PURCHASED_SERVICES)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                PurchaseData purchaseData = snapshot1.getValue(PurchaseData.class);
                                if (purchaseData.PAYMENT.PAYMENT_STATUS.equalsIgnoreCase("1"))
                                    purchaseDataList.add(snapshot1.getValue(PurchaseData.class));
                            }
                            shimmer.stopShimmer();
                            shimmer.setVisibility(View.GONE);
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(SubsribedLectures.this,2);
                            all_services.setLayoutManager(gridLayoutManager);
                            all_services.setAdapter(new PurchasedServiceAdapter(SubsribedLectures.this, purchaseDataList,1));
                            swipeRefreshLayout.setRefreshing(false);
                        }else {
                            swipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(SubsribedLectures.this, "Don't contain any plan! \nChoose plans to get access through services page.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(SubsribedLectures.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void onStart() {
        super.onStart();
        /*intent = new Intent(this, TimeCounterService.class);
        startService( intent
                .putExtra("SESSION_ID",gen())
                .putExtra("PAGE_NAME", Pages.PAGE_PURCHASED_SERVICES)
                .putExtra("PAGE_DESCRIPTION","Visited purchased services page for knowing course contents."));
   */ }

    @Override
    public void onStop() {
        super.onStop();
        //stopService(intent);
    }

    public int gen() {
        Random r = new Random( System.currentTimeMillis() );
        return ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
    }

}