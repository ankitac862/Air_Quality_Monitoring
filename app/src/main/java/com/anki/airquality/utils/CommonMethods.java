package com.anki.airquality.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.anki.airquality.AppConstant;
import com.anki.airquality.R;
import com.anki.airquality.ui.main.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
/*
* This class consist common methods
* */
public class CommonMethods {
    public static String getCurrentDate() {
        SimpleDateFormat df = new SimpleDateFormat(AppConstant.DATE_FORMATE, Locale.getDefault());
        Date currentTime = Calendar.getInstance().getTime();
        return df.format(currentTime);
    }

    /*
    * Get the color acc to AQI
    * */
    public static int getColorAccToStatus(Context context, int status) {
        int colorCode;
        switch (status) {
            case 1:
                colorCode = ContextCompat.getColor(context, R.color.green);
                break;
            case 2:
                colorCode = ContextCompat.getColor(context, R.color.lightgreen);
                break;
            case 3:
                colorCode = ContextCompat.getColor(context, R.color.yellow);
                break;
            case 4:
                colorCode = ContextCompat.getColor(context, R.color.orange);
                break;
            case 5:
                colorCode = ContextCompat.getColor(context, R.color.red);
                break;
            case 6:
                colorCode = ContextCompat.getColor(context, R.color.darkred);
                break;
            default:
                colorCode = ContextCompat.getColor(context, R.color.green);
                break;

        }

        return colorCode;

    }

    /*
    * Back stack change Listener
    * */
    public static void addBackStackChangeListener(AppCompatActivity appCompatActivity) {
        appCompatActivity.getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            int index = appCompatActivity.getSupportFragmentManager().getBackStackEntryCount() - 1;
            String tag = null;
            Fragment fragment = null;
            if (index >= 0) {
                FragmentManager.BackStackEntry backEntry = appCompatActivity.getSupportFragmentManager().getBackStackEntryAt(index);
                tag = backEntry.getName();
                fragment = appCompatActivity.getSupportFragmentManager().findFragmentByTag(tag);

            }
            if (tag != null) {
                if (tag.equalsIgnoreCase(AppConstant.MAINFRAGMMENT)) {
                    ((MainActivity)appCompatActivity).showToolbar(true, "");
                } else {
                    ((MainActivity)appCompatActivity).showToolbar(false, "Detail");
                }
            }else{
                ((MainActivity)appCompatActivity).showToolbar(true, "");
            }

        });
    }

}
