package com.ygaps.travelapp.View;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ygaps.travelapp.Adapter.SectionsPagerAdapter;
import com.ygaps.travelapp.R;

public class StopPointDialogInTourDetail extends AppCompatDialogFragment {
    private ViewPager mViewPager;

    View rootview;

    TabLayout tabLayout;
    ViewPager viewPager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.dialog_review_stop_point, container,false);
        if (getDialog() != null && getDialog().getWindow() != null) {

            //Set transparent background
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

            //Get data from root activity, add to bundle
            String JSONPointInfo = getArguments().getString("StopPointInfo");
            int itemId = getArguments().getInt("tourId");
            Bundle bundle = new Bundle();
            bundle.putString("StopPointInfo", JSONPointInfo);
            bundle.putInt("tourId",itemId);

            //Set tab navigation
            tabLayout = (TabLayout) rootview.findViewById(R.id.tabs);
            viewPager = (ViewPager) rootview.findViewById(R.id.sp_container_fragment);
            SectionsPagerAdapter adapter = new SectionsPagerAdapter(getChildFragmentManager());

            StopPointInformationFragment stopPointDialogTab1 = new StopPointInformationFragment();
            ReviewsFragment stopPointDialogTab2 = new ReviewsFragment();

            //Send data to 2 tabs fragment
            stopPointDialogTab1.setArguments(bundle);
            stopPointDialogTab2.setArguments(bundle);

            //Add tab fragment to adapter
            adapter.addFragment(stopPointDialogTab1, "INFORMATION");
            adapter.addFragment(stopPointDialogTab2, "REVIEWS");
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);

            //Set event click cancelBtn
            ImageButton cancelBtn = rootview.findViewById(R.id.cancel_review_stop_point);
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }

        //
        return rootview;
    }
}
