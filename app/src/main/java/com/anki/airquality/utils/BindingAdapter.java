package com.anki.airquality.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anki.airquality.R;

import androidx.core.content.ContextCompat;
/*
* Binding Adapter to set data from databinding
* */
public class BindingAdapter {

    @androidx.databinding.BindingAdapter("txtColor")
    public static void setTextColor(TextView textView, int status) {
        Context context = textView.getContext();
        switch (status) {
            case 1:
                textView.setTextColor(ContextCompat.getColor(context, R.color.green));
                break;
            case 2:
                textView.setTextColor(ContextCompat.getColor(context, R.color.lightgreen));
                break;
            case 3:
                textView.setTextColor(ContextCompat.getColor(context, R.color.yellow));
                break;
            case 4:
                textView.setTextColor(ContextCompat.getColor(context, R.color.orange));
                break;
            case 5:
                textView.setTextColor(ContextCompat.getColor(context, R.color.red));
                break;
            case 6:
                textView.setTextColor(ContextCompat.getColor(context, R.color.darkred));
                break;
            default:
                textView.setTextColor(ContextCompat.getColor(context, R.color.orange));
                break;

        }
    }

    @androidx.databinding.BindingAdapter("src")
    public static void setImageSrc(ImageView imageView, int status) {
        Context context = imageView.getContext();
        switch (status) {
            case 1:
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_grinning));
                break;
            case 2:
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_smile));
                break;
            case 3:
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_sad_face));
                break;
            case 4:
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_anger));
                break;
            case 5:
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_medical_mask));
                break;
            case 6:
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_dead));
                break;
            default:
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_anger));
                break;
        }
    }

    @androidx.databinding.BindingAdapter("background")
    public static void setBackgroundSrc(View view, int status) {
        Context context = view.getContext();
        switch (status) {
            case 1:
                view.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_wave_green));
                break;
            case 2:
                view.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_wave_light_green));
                break;
            case 3:
                view.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_wave_yellow));
                break;
            case 4:
                view.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_wave_orage));
                break;
            case 5:
                view.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_wave_red));
                break;
            case 6:
                view.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_wave_darkred));
                break;
            default:
                view.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_wave_grey));
                break;
        }
    }
}
