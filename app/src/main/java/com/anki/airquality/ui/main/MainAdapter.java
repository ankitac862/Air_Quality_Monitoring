package com.anki.airquality.ui.main;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anki.airquality.databinding.ItemLayoutBinding;
import com.anki.airquality.pojo.AQIModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/*
* Adapter class
* */
public class MainAdapter extends  RecyclerView.Adapter<MainAdapter.ViewHolder>{

    private List<AQIModel> aqiModelArrayList = new ArrayList<>();
    private Context context;
    private onDetailItemClick onDetailItemClick;

    public MainAdapter(Context context, onDetailItemClick onDetailItemClick) {
        this.context = context;
        this.onDetailItemClick = onDetailItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(ItemLayoutBinding.inflate(layoutInflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AQIModel aqiModel = aqiModelArrayList.get(position);
        holder.bind(aqiModel);
    }

    @Override
    public int getItemCount() {
        return aqiModelArrayList.size();
    }

    /*
    * get the updated Value and set the value
    * */
    public void diffInList(List<AQIModel> newAqiData) {
        this.aqiModelArrayList.clear();
        this.aqiModelArrayList.addAll(newAqiData);
        notifyDataSetChanged();
    }


    public interface onDetailItemClick{
        void onItemClick(AQIModel data);
    }

/*
* View holder class
* */
    public class ViewHolder extends RecyclerView.ViewHolder {
       private final ItemLayoutBinding binding;

        public ViewHolder(ItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(AQIModel aqiModel) {
            binding.setAqiData(aqiModel);
            binding.executePendingBindings();
            binding.getRoot().setOnClickListener(v ->{
                onDetailItemClick.onItemClick(aqiModel);
            });

        }
    }
}