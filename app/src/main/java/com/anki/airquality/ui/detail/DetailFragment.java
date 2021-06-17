package com.anki.airquality.ui.detail;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anki.airquality.AppConstant;
import com.anki.airquality.R;
import com.anki.airquality.databinding.FragmentDetailBinding;
import com.anki.airquality.pojo.AQIModel;
import com.anki.airquality.ui.main.MainActivity;
import com.anki.airquality.ui.viewModel.DetailViewModel;
import com.anki.airquality.utils.CommonMethods;
import com.anki.airquality.utils.DebugUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class DetailFragment extends Fragment implements OnChartValueSelectedListener {
    private static final String TAG = "DetailFragment";
    private FragmentDetailBinding binding;
    private ArrayList<Float> selectedAQIValue;
    private DetailViewModel mainViewModel;
    private AQIModel selectedAQI;
    private String selectedCity;

    public DetailFragment() {
    }

    public static DetailFragment newInstance(String city, AQIModel selectedAQI) {
        DetailFragment myFragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(AppConstant.CITY_NAME, city);
        args.putParcelable(AppConstant.SELECTED_AQI, selectedAQI);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_detail, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        selectedAQIValue = new ArrayList<>();
        if (getActivity() != null) {
            if (((MainActivity) getActivity()).binding.layoutAppBar != null) {
                ((MainActivity) getActivity()).binding.layoutAppBar.setExpanded(true, true);
            }
            ((MainActivity) getActivity()).showToolbar(false, "Detail");
        }
        if (getArguments() != null) {
            selectedAQI = getArguments().getParcelable(AppConstant.SELECTED_AQI);
            selectedCity = getArguments().getString(AppConstant.CITY_NAME);
        }
        init();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void init() {
        mainViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
        mainViewModel.getSocketLiveData().observe(this, socketEventModelObserver);
        mainViewModel.getSocketLiveData().connect();
        binding.setAqiData(selectedAQI);
        setChart(binding.chart);
        mmChartHandler.postDelayed(mRunnableCode, 30000);
        binding.executePendingBindings();
    }

    /*
    * Setting up the chart UI
    * */
    private void setChart(LineChart chart) {
        chart.setAutoScaleMinMaxEnabled(true);
        chart.getDescription().setEnabled(false);
        chart.setOnChartValueSelectedListener(this);
        chart.invalidate();

        chart.moveViewToX(10);
        XAxis xAxis = chart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);

        YAxis yAxis = chart.getAxisLeft();
        chart.getAxisRight().setEnabled(false);

        yAxis.enableGridDashedLine(10f, 10f, 0f);
        yAxis.setAxisMaximum(500f);
        yAxis.setAxisMinimum(0f);
        addEntryInChart(selectedAQI);
    }

    /*
     *  Observe the updated value
     * */
    private final Observer<List<AQIModel>> socketEventModelObserver = new Observer<List<AQIModel>>() {
        @Override
        public void onChanged(List<AQIModel> aqiModels) {
            String date = CommonMethods.getCurrentDate();
            for (AQIModel aqiModel : aqiModels) {
                if (aqiModel.getCity().equals(selectedCity)) {
                    aqiModel.setTime(date);
                    selectedAQI = aqiModel;
                }
            }
        }
    };

    /*
     *  Add the entry dynamically in chart
     * */
    private void addEntryInChart(AQIModel aqiModel) {
        selectedAQIValue.add(aqiModel.getAqi());
        LineData data = binding.chart.getData();
        if (data == null) {
            data = new LineData();
            binding.chart.setData(data);
        }
        ILineDataSet set = data.getDataSetByIndex(0);
        if (set == null) {
            set = createSet();
            data.addDataSet(set);
        }

        data.addEntry(new Entry(set.getEntryCount(), aqiModel.getAqi()), 0);
        data.notifyDataChanged();

        binding.chart.notifyDataSetChanged();

        binding.chart.setVisibleXRangeMaximum(120);

        // move to the latest entry
        binding.chart.moveViewToX(data.getEntryCount());

    }
/*
* Setting the line UI
* */
    private LineDataSet createSet() {
        int color = CommonMethods.getColorAccToStatus(getActivity(), selectedAQI.getStatus());
        LineDataSet set = new LineDataSet(null, selectedCity);
        set.setLineWidth(2.5f);
        set.setCircleRadius(4.5f);
        set.setColor(color);
        set.setCircleColor(color);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setValueTextSize(10f);

        return set;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }


    private final Handler mmChartHandler = new Handler();
    private final Runnable mRunnableCode = new Runnable() {

        @Override
        public void run() {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // This method will be executed once the timer is over
                        // It will update the UI along with setting value in chart
                        DebugUtils.debug(DetailFragment.this.getClass(), selectedAQI.getAqi() + " ");
                        DetailFragment.this.addEntryInChart(selectedAQI);
                    }
                });
            }
            mmChartHandler.postDelayed(mRunnableCode, 30000);

        }
    };

    @Override
    public void onStop() {
        super.onStop();
        if (mmChartHandler.hasCallbacks(mRunnableCode)) {
            mmChartHandler.removeCallbacks(mRunnableCode);
        }
    }
}
