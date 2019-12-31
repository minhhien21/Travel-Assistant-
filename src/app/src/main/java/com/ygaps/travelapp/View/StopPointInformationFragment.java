package com.ygaps.travelapp.View;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.ygaps.travelapp.Model.StopPointInfos;
import com.ygaps.travelapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StopPointInformationFragment extends Fragment {
    private TextView name;
    private Spinner serviceType;
    private TextView address;
    private Spinner province;
    private TextView minCost;
    private TextView maxCost;
    private TextView timeStart;
    private TextView dateStart;
    private TextView timeEnd;
    private TextView dateEnd;
    private StopPointInfos stopPointInfos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_point_information, container, false);
        Bundle bundle = getArguments();
        String JSONPointInfo = bundle.getString("StopPointInfo");
        Gson gson = new Gson();
        stopPointInfos = gson.fromJson(JSONPointInfo, StopPointInfos.class);


        name = view.findViewById(R.id.stop_point_name);
        serviceType = view.findViewById(R.id.stop_point_service_type);
        address = view.findViewById(R.id.stop_point_address);
        province = view.findViewById(R.id.stop_point_province);
        minCost = view.findViewById(R.id.stop_point_min_cost);
        maxCost = view.findViewById(R.id.stop_point_max_cost);

        timeStart = view.findViewById(R.id.stop_point_time_start);
        dateStart = view.findViewById(R.id.stop_point_select_date_start);

        timeEnd = view.findViewById(R.id.stop_point_time_end);
        dateEnd = view.findViewById(R.id.stop_point_select_date_end);

        name.setText(stopPointInfos.getName());
        address.setText(stopPointInfos.getAddress());
        serviceType.setSelection(stopPointInfos.getServiceTypeId() - 1);
        province.setSelection(stopPointInfos.getProvinceId() - 1);
        minCost.setText(stopPointInfos.getMinCost());
        maxCost.setText(stopPointInfos.getMaxCost());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        String sArrivalAt = simpleDateFormat.format(new Date(stopPointInfos.getArrivalAt()));
        String[] separated = sArrivalAt.split(" ");
        dateStart.setText(separated[0]);
        timeStart.setText(separated[1]);
        String sLeaveAt = simpleDateFormat.format(new Date(stopPointInfos.getLeaveAt()));
        separated = sLeaveAt.split(" ");
        dateEnd.setText(separated[0]);
        timeEnd.setText(separated[1]);
        return view;
    }
}
