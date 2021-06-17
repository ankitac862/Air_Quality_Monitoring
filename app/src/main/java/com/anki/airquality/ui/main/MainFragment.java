package com.anki.airquality.ui.main;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anki.airquality.AppConstant;
import com.anki.airquality.R;
import com.anki.airquality.databinding.FragmentMainBinding;
import com.anki.airquality.pojo.AQIModel;
import com.anki.airquality.ui.detail.DetailFragment;
import com.anki.airquality.ui.viewModel.MainViewModel;
import com.anki.airquality.utils.CommonMethods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements MainAdapter.onDetailItemClick{

    private FragmentMainBinding binding;
    private MainViewModel mainViewModel;
    private MainAdapter mainAdapter;
    private HashMap<String , AQIModel> aqiModelHashMap;
    private ArrayList<AQIModel> aqiModelArrayList;
    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        MainFragment myFragment = new MainFragment();
        return myFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =  DataBindingUtil.inflate(
                inflater, R.layout.fragment_main, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity()!= null)
            ((MainActivity)getActivity()).showToolbar(true , "");
        aqiModelHashMap = new HashMap<>();
        aqiModelArrayList = new ArrayList<>();
        init();
    }

    private void init() {
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getSocketLiveData().observe(this, socketEventModelObserver);
        mainViewModel.getSocketLiveData().connect();
        mainAdapter = new MainAdapter(getActivity() , this);
        binding.rvAir.setAdapter(mainAdapter);
        binding.rvAir.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


/*
*  Observe the updated value
* */
    private final Observer<List<AQIModel>> socketEventModelObserver = new Observer<List<AQIModel>>() {
        @Override
        public void onChanged(List<AQIModel> aqiModels) {
            String date = CommonMethods.getCurrentDate();
            for (AQIModel aqiModel : aqiModels){
                aqiModel.setTime(date);
                aqiModelHashMap.put(aqiModel.getCity() , aqiModel);
            }
            updateAQIItem(aqiModelHashMap);
        }
    };

    public void updateAQIItem(HashMap<String , AQIModel> newAqiData) {

        for (Map.Entry<String, AQIModel> stringAQIModelEntry : newAqiData.entrySet()) {
            AQIModel model = (AQIModel) stringAQIModelEntry.getValue();
            if (aqiModelArrayList != null && aqiModelArrayList.size()>0) {
                boolean isItemPresent = false;
                for (int i = 0; i < aqiModelArrayList.size(); i++) {
                    if (aqiModelArrayList.get(i).getCity().equals(model.getCity())){
                        aqiModelArrayList.get(i).setAqi(model.getAqi());
                        isItemPresent = true;
                        break;
                    }
                }
                if (!isItemPresent){
                    aqiModelArrayList.add(model);
                }
            }else{
                aqiModelArrayList.add(model);
            }
        }
        if (binding.rvAir.getVisibility() == View.GONE){
            binding.rvAir.setVisibility(View.VISIBLE);
            binding.tvNodata.setVisibility(View.GONE);
        }
        mainAdapter.diffInList(aqiModelArrayList);
    }

    @Override
    public void onItemClick(AQIModel aqiModel) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment fragment = DetailFragment.newInstance(aqiModel.getCity(), aqiModel);
        fragmentManager.beginTransaction()
                .add(R.id.fragment , fragment , AppConstant.DETAIL)
                .addToBackStack(AppConstant.DETAIL).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.executePendingBindings();
    }

    @Override
    public void onStop() {
        super.onStop();
        mainViewModel.getSocketLiveData().disconnect();
    }
}
