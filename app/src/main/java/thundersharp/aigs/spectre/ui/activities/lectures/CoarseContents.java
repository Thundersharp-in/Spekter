package thundersharp.aigs.spectre.ui.activities.lectures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.adapters.CoursesItemAdapter;
import thundersharp.aigs.spectre.core.models.CourseData;
import thundersharp.aigs.spectre.core.models.ServiceItemModel;
import thundersharp.aigs.spectre.core.utils.CONSTANTS;

public class CoarseContents extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private String coarse_id;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView img_not_found;
    private Intent intent;
    private boolean subscribed = false, restricted = true;
    private ShimmerFrameLayout shimmer;
    private List<CourseData> courseDatalist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coarse_contents);   if (getIntent().getStringExtra("coarse_id") != null){
            coarse_id = getIntent().getStringExtra("coarse_id");
        }else {
            Toast.makeText(this, "Error in loading content", Toast.LENGTH_SHORT).show();
            finish();
        }

        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycler);
        swipeRefreshLayout = findViewById(R.id.swipe);
        img_not_found = findViewById(R.id.img_not_found);
        shimmer = findViewById(R.id.shimmer);
        img_not_found.setVisibility(View.GONE);

        toolbar.setNavigationOnClickListener(n->{finish();});
        toolbar.setTitle(getIntent().getStringExtra("coarse_name"));
        toolbar.setSubtitle("By "+getIntent().getStringExtra("by"));
        courseDatalist = new ArrayList<>();
        swipeRefreshLayout.setOnRefreshListener(() -> checkSubscription());
        swipeRefreshLayout.setRefreshing(true);

        //loadCoarseData(coarse_id);
        checkSubscription();

    }

    private synchronized void checkSubscription(){
        shimmer.startShimmer();
        shimmer.setVisibility(View.VISIBLE);
        img_not_found.setVisibility(View.GONE);

        subscribed = true;
        restricted = false;
        //recyclerView.setAdapter(new CoursesItemAdapter(new ArrayList<>(),"thundersharp",CoarseContents.this, false,true));
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_USER_DATA)
                .child(FirebaseAuth.getInstance().getUid())
                .child(CONSTANTS.DATABASE_PURCHASED_SERVICES)
                .child(coarse_id)
                //.child(CONSTANTS.DATABASE_PAYMENT)
                //.child(CONSTANTS.DATABASE_PAYMENT_STATUS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                   /*         PurchaseData purchaseData = snapshot.getValue(PurchaseData.class);
                            if (purchaseData.PAYMENT.PAYMENT_STATUS.equalsIgnoreCase("1"))
                                subscribed = true;
                            else subscribed = false;
                            if (purchaseData.RESTRICT.equalsIgnoreCase("false")) restricted = false; else restricted = true;*/
                        }else {
                            restricted = true;
                            subscribed = false;
                            Toast.makeText(CoarseContents.this, "Plan Un-Available! \nSubscribe plan to get access through.", Toast.LENGTH_SHORT).show();
                        }
                        loadCoarseData(coarse_id);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        shimmer.stopShimmer();
                        shimmer.setVisibility(View.GONE);
                        img_not_found.setVisibility(View.VISIBLE);
                        subscribed = false;
                        Toast.makeText(CoarseContents.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void loadCoarseData(String coarse_id){

        courseDatalist.clear();
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_NODE_COURSE_DATA)
                .child(coarse_id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            img_not_found.setVisibility(View.GONE);
                            shimmer.stopShimmer();
                            shimmer.setVisibility(View.GONE);
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                                CourseData courseData = dataSnapshot.getValue(CourseData.class);
                                courseDatalist.add(courseData);

                            }

                            recyclerView.setAdapter(new CoursesItemAdapter(courseDatalist,getIntent().getStringExtra("by"),CoarseContents.this, subscribed, restricted));
                            swipeRefreshLayout.setRefreshing(false);

                        }else {
                            shimmer.stopShimmer();
                            shimmer.setVisibility(View.GONE);
                            img_not_found.setVisibility(View.VISIBLE);
                            Toast.makeText(CoarseContents.this, "No data found", Toast.LENGTH_SHORT).show();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        shimmer.stopShimmer();
                        shimmer.setVisibility(View.GONE);
                        img_not_found.setVisibility(View.VISIBLE);
                        Toast.makeText(CoarseContents.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }



}