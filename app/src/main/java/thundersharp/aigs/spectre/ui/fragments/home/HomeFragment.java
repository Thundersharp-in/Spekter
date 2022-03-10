package thundersharp.aigs.spectre.ui.fragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.adapters.CustomPagerAdapter;
import thundersharp.aigs.spectre.core.helpers.MapsHelpers;
import thundersharp.aigs.spectre.core.models.MarkersData;
import thundersharp.aigs.spectre.core.models.SliderModel;
import thundersharp.aigs.spectre.core.models.Testimonials;
import thundersharp.aigs.spectre.core.utils.CONSTANTS;
import thundersharp.aigs.spectre.ui.activities.CompetationHome;
import thundersharp.aigs.spectre.ui.activities.ExhibitionHome;
import thundersharp.aigs.spectre.ui.activities.InitiativesHomes;
import thundersharp.aigs.spectre.ui.activities.InnovativeChallengeHome;
import thundersharp.aigs.spectre.ui.activities.KnowUs;
import thundersharp.aigs.spectre.ui.activities.LecturesHome;
import thundersharp.aigs.spectre.ui.activities.UpcommingEventsHome;
import thundersharp.aigs.spectre.ui.activities.WorkshopsHome;
import thundersharp.aigs.spectre.ui.activities.barcode.BarCodeScanner;


public class HomeFragment extends Fragment implements
        BaseSliderView.OnSliderClickListener ,
        OnMapReadyCallback,
        ViewPagerEx.OnPageChangeListener{

    private SliderLayout slider;
    private GoogleMap mMap;
    private ViewPager viewPager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        slider = root.findViewById(R.id.slider);
        viewPager = root.findViewById(R.id.pager);
        setupCrousel();


        root.findViewById(R.id.notification).setOnClickListener(u->startActivity(new Intent(getActivity(), BarCodeScanner.class)));
        root.findViewById(R.id.alive).setOnClickListener(u->startActivity(new Intent(getActivity(), ExhibitionHome.class)));
        root.findViewById(R.id.iniatives).setOnClickListener(u->startActivity(new Intent(getActivity(), InitiativesHomes.class)));
        root.findViewById(R.id.lectures).setOnClickListener(u->startActivity(new Intent(getActivity(), LecturesHome.class)));
        root.findViewById(R.id.workshops).setOnClickListener(u->startActivity(new Intent(getActivity(), WorkshopsHome.class)));
        root.findViewById(R.id.competations).setOnClickListener(u->startActivity(new Intent(getActivity(), CompetationHome.class)));
        root.findViewById(R.id.upcomingEvents).setOnClickListener(u->startActivity(new Intent(getActivity(), UpcommingEventsHome.class)));
        root.findViewById(R.id.ic).setOnClickListener(u->startActivity(new Intent(getActivity(), InnovativeChallengeHome.class)));
        root.findViewById(R.id.knowUs).setOnClickListener(u->startActivity(new Intent(getActivity(), KnowUs.class)));


        viewPager.setAdapter(new CustomPagerAdapter(getActivity(),getTempTestimonial()));

        return root;
    }


    private Testimonials[] getTempTestimonial(){
        return new Testimonials[]{new Testimonials("","","",""),new Testimonials("","","",""),new Testimonials("","","","")};
    }

    private void setupCrousel(){
        ArrayList<String> listUrl = new ArrayList<>();
        Bundle bundle = new Bundle();

        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_TOP_CAROUSEL)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            RequestOptions requestOptions = new RequestOptions();
                            requestOptions.centerCrop();
                            for (DataSnapshot dataSnapshot :snapshot.getChildren()){

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

                        }else {
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
        MapsHelpers.getInstance().setGoogleMaps(mMap).setMarkers(new MarkersData(13.0839376,77.4849723,"Exhibition")).locate();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(13.0839376,77.4849723), 18));

    }
}