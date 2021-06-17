package com.anki.airquality.pojo;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anki.airquality.BR;
import com.anki.airquality.R;

import java.text.DecimalFormat;

import androidx.core.content.ContextCompat;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

// Pojo class for AQI model
public class AQIModel extends BaseObservable implements Parcelable {
    private String city;
    private float aqi;
    private int status;

    public AQIModel() {
    }


    protected AQIModel(Parcel in) {
        city = in.readString();
        aqi = in.readFloat();
        status = in.readInt();
        time = in.readString();
    }

    public static final Creator<AQIModel> CREATOR = new Creator<AQIModel>() {
        @Override
        public AQIModel createFromParcel(Parcel in) {
            return new AQIModel(in);
        }

        @Override
        public AQIModel[] newArray(int size) {
            return new AQIModel[size];
        }
    };
    @Bindable
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
        notifyPropertyChanged(BR.time);
    }

    private String time;

    @Bindable
    public int getStatus() {
        return status;
    }


    public void setStatus(float aqi) {
        if (aqi == 0 || aqi < 51) {
            status = 1;
        } else if (aqi == 51 || aqi < 101) {
            status = 2;
        } else if (aqi == 101 || aqi < 201) {
            status = 3;
        } else if (aqi == 201 || aqi < 301) {
            status = 4;
        } else if (aqi == 301 || aqi < 401) {
            status = 5;
        } else {
            status = 6;
        }
        notifyPropertyChanged(BR.status);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Bindable
    public float getAqi() {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return Float.parseFloat(decimalFormat.format(aqi));
    }

    public void setAqi(float aqi) {
        this.aqi = aqi;
        setStatus(aqi);
       notifyPropertyChanged(BR.aqi);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(city);
        parcel.writeFloat(aqi);
        parcel.writeInt(status);
        parcel.writeString(time);
    }
}
