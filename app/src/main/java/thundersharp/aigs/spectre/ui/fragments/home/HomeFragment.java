package thundersharp.aigs.spectre.ui.fragments.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.request.RequestOptions;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.animations.DescriptionAnimation;
import com.glide.slider.library.slidertypes.BaseSliderView;
import com.glide.slider.library.slidertypes.DefaultSliderView;
import com.glide.slider.library.tricks.ViewPagerEx;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import thundersharp.aigs.newsletter.core.utils.TimeUtils;
import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.adapters.CustomPagerAdapter;
import thundersharp.aigs.spectre.core.helpers.DatabaseHelpers;
import thundersharp.aigs.spectre.core.helpers.MapsHelpers;
import thundersharp.aigs.spectre.core.helpers.ProfileDataSync;
import thundersharp.aigs.spectre.core.interfaces.TestimonialLoader;
import thundersharp.aigs.spectre.core.models.MarkersData;
import thundersharp.aigs.spectre.core.models.SliderModel;
import thundersharp.aigs.spectre.core.models.Testimonials;
import thundersharp.aigs.spectre.core.utils.CONSTANTS;
import thundersharp.aigs.spectre.core.utils.LatLongConverter;
import thundersharp.aigs.spectre.ui.activities.barcode.BarCodeScanner;
import thundersharp.aigs.spectre.ui.activities.exhibition.ExhibitionHome;
import thundersharp.aigs.spectre.ui.activities.feedback.EventFeedback;
import thundersharp.aigs.spectre.ui.activities.feedback.FacultyFeedback;
import thundersharp.aigs.spectre.ui.activities.home.CompetationHome;
import thundersharp.aigs.spectre.ui.activities.home.InitiativesHomes;
import thundersharp.aigs.spectre.ui.activities.home.InnovativeChallengeHome;
import thundersharp.aigs.spectre.ui.activities.home.KnowUs;
import thundersharp.aigs.spectre.ui.activities.home.UpcommingEventsHome;
import thundersharp.aigs.spectre.ui.activities.home.WorkshopsHome;
import thundersharp.aigs.spectre.ui.activities.lectures.LecturesHome;


public class HomeFragment extends Fragment implements
        BaseSliderView.OnSliderClickListener,
        OnMapReadyCallback,
        ViewPagerEx.OnPageChangeListener {

    private SliderLayout slider;
    private GoogleMap mMap;
    private ViewPager2 viewPager;
    private MaterialCardView materialCardView;
    private TextView tittleUpcoming, descriptionUpcoming;

    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        try {

            root = inflater.inflate(R.layout.fragment_home, container, false);
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            slider = root.findViewById(R.id.slider);
            viewPager = root.findViewById(R.id.pager);
            setupCrousel();

            // root.findViewById(R.id.rrr).setOnClickListener(n->HomeActivity.navController.navigate(R.id.navigation_profile));

            ((TextView) root.findViewById(R.id.textName)).setText(ProfileDataSync.getInstance(getActivity()).initializeLocalStorage().pullDataBack().name);
            root.findViewById(R.id.notification).setOnClickListener(u -> startActivity(new Intent(getActivity(), BarCodeScanner.class)));
            root.findViewById(R.id.alive).setOnClickListener(u -> startActivity(new Intent(getActivity(), ExhibitionHome.class)));
            root.findViewById(R.id.iniatives).setOnClickListener(u -> startActivity(new Intent(getActivity(), InitiativesHomes.class)));
            root.findViewById(R.id.lectures).setOnClickListener(u -> startActivity(new Intent(getActivity(), LecturesHome.class)));
            root.findViewById(R.id.workshops).setOnClickListener(u -> startActivity(new Intent(getActivity(), WorkshopsHome.class)));
            root.findViewById(R.id.competations).setOnClickListener(u -> startActivity(new Intent(getActivity(), CompetationHome.class)));
            root.findViewById(R.id.upcomingEvents).setOnClickListener(u -> startActivity(new Intent(getActivity(), UpcommingEventsHome.class)));
            root.findViewById(R.id.ic).setOnClickListener(u -> startActivity(new Intent(getActivity(), InnovativeChallengeHome.class)));
            root.findViewById(R.id.knowUs).setOnClickListener(u -> startActivity(new Intent(getActivity(), KnowUs.class)));
            materialCardView = root.findViewById(R.id.latest_update);
            tittleUpcoming = root.findViewById(R.id.time);
            descriptionUpcoming = root.findViewById(R.id.eventName);

            root.findViewById(R.id.feedBack).setOnClickListener(r -> {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
                bottomSheetDialog.setContentView(R.layout.bottom_sheet_feedback);

                LinearLayout app_feed_Back = bottomSheetDialog.findViewById(R.id.app_feed_Back);
                LinearLayout event_feedBack = bottomSheetDialog.findViewById(R.id.event_feedBack);
                LinearLayout facFeedback = bottomSheetDialog.findViewById(R.id.facFeedback);

                app_feed_Back.setOnClickListener(o -> {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("email/rfc822");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"contact_spekter@acharya.ac.in"});
                    intent.putExtra(Intent.EXTRA_TEXT, "__________________________________________________________\n");
                    intent.putExtra(Intent.EXTRA_TEXT, "DEVICE: " + Build.DEVICE + "\nMANUFACTURER: "
                            + Build.MANUFACTURER + "\nBOOTLOADER: "
                            + Build.BOOTLOADER + "\nPRODUCT: "
                            + Build.PRODUCT + "\nUSER: "
                            + Build.USER + "\nDEVICE ID: "
                            + Build.ID + "\nCPU_ABI: "
                            + Build.CPU_ABI + "\n"
                            + "________Your message after here _______\n");
                    Intent mailer = Intent.createChooser(intent, "Choose a email app to send Feedback/Report Bug");
                    startActivity(mailer);
                });


                facFeedback.setOnClickListener(i -> startActivity(new Intent(getActivity(), FacultyFeedback.class)));
                event_feedBack.setOnClickListener(i -> startActivity(new Intent(getActivity(), EventFeedback.class)));


                bottomSheetDialog.show();
            });

            root.findViewById(R.id.facebook).setOnClickListener(u -> {
                Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://www.facebook.com/sppekter"));
                startActivity(intent);
            });

            root.findViewById(R.id.instagram).setOnClickListener(u -> {
                Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://www.instagram.com/spekter_aigs"));
                startActivity(intent);
            });

            root.findViewById(R.id.youtube).setOnClickListener(u -> {
                Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://www.youtube.com/channel/UCMdjjf3FhIoxCrtk3RXvexA"));
                startActivity(intent);
            });

            root.findViewById(R.id.linkedIn).setOnClickListener(u -> {
                Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://in.linkedin.com/company/spekter/"));
                startActivity(intent);
            });

            DatabaseHelpers
                    .getInstance()
                    .fechTestimonials(new TestimonialLoader() {
                        @Override
                        public void OnTestimonialsLoaded(Testimonials... testimonialsList) {
                            viewPager.setAdapter(new CustomPagerAdapter(getActivity(), testimonialsList));
                        }

                        @Override
                        public void onDatabaseError(Exception e) {
                            Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return root;
    }


    private void setupCrousel() {
        ArrayList<String> listUrl = new ArrayList<>();
        Bundle bundle = new Bundle();
        int d;

        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_TOP_CAROUSEL)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            RequestOptions requestOptions = new RequestOptions();
                            requestOptions.centerCrop();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                DefaultSliderView sliderView = new DefaultSliderView(getActivity());
                                sliderView
                                        .image(dataSnapshot.child("URL").getValue(String.class))
                                        .description(null)
                                        .setRequestOption(requestOptions)
                                        .setProgressBarVisible(true)
                                        .setOnSliderClickListener(HomeFragment.this);

                                //add your extra information
                                SliderModel model = dataSnapshot.getValue(SliderModel.class);
                                Bundle bundle1 = new Bundle();
                                bundle1.clear();
                                bundle1.putSerializable("data", model);
                                //bundle1.putInt("PAGE",dataSnapshot.child("PAGE").getValue(Integer.class));
                                sliderView.bundle(bundle1);

                                slider.addSlider(sliderView);
                            }

                        } else {
                            listUrl.add("https://vidya-india.org/wp-content/uploads/2021/02/ai.jpg");
                            listUrl.add("https://images.squarespace-cdn.com/content/v1/5d7187438c38e20001dcc19b/1624733785132-4ZPPKZH7IC3RNSPAOTWX/GTF_TicketsGTF2022.jpg?format=2500w");

                            RequestOptions requestOptions = new RequestOptions();
                            requestOptions.centerCrop();

                            for (int i = 0; i < listUrl.size(); i++) {
                                DefaultSliderView sliderView = new DefaultSliderView(getActivity());
                                // if you want show image only / without description text use DefaultSliderView instead

                                // initialize SliderLayout
                                sliderView
                                        .image(listUrl.get(i))
                                        .description(null)
                                        .setRequestOption(requestOptions)
                                        .setProgressBarVisible(true)
                                        .setOnSliderClickListener(HomeFragment.this);

                                //add your extra information
                                sliderView.bundle(bundle);

                                slider.addSlider(sliderView);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        slider.setPresetTransformer(SliderLayout.Transformer.ZoomOutSlide);

        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slider.setCustomAnimation(new DescriptionAnimation());
        slider.setDuration(4000);
        //slider.addOnPageChangeListener(Content_home.this);
        slider.stopCyclingWhenTouch(false);
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.mapstyle_night);
        //markerOptions = new MarkerOptions();
        mMap = googleMap;
        mMap.setMapStyle(style);

        FirebaseDatabase
                .getInstance()
                .getReference("UPCOMING")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            descriptionUpcoming.setText(snapshot.child("DESCRIPTION").getValue(String.class));
                            tittleUpcoming.setText(snapshot.child("TITTLE").getValue(String.class) + "\n\n" + TimeUtils.getTimeInStringFromTimeStamp(snapshot.child("ID").getValue(String.class)));
                            MapsHelpers.getInstance().setGoogleMaps(mMap).setMarkers(new MarkersData(LatLongConverter.initialize().getlat(snapshot.child("LOCATION").getValue(String.class)), LatLongConverter.initialize().getlang(snapshot.child("LOCATION").getValue(String.class)), snapshot.child("TITTLE").getValue(String.class))).locate();

                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLongConverter.initialize().getlatlang(snapshot.child("LOCATION").getValue(String.class)), 18));

                        } else {
                            materialCardView.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }
}