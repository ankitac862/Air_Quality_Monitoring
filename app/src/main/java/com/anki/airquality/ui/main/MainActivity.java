package com.anki.airquality.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.anki.airquality.AppConstant;
import com.anki.airquality.AppController;
import com.anki.airquality.R;
import com.anki.airquality.databinding.ActivityMainBinding;
import com.anki.airquality.socket.SocketListner;

import static com.anki.airquality.utils.CommonMethods.addBackStackChangeListener;

public class MainActivity extends AppCompatActivity {
    public ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_main);
        AppController.setInForeground(true);
        addBackStackChangeListener(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = MainFragment.newInstance();
        fragmentManager.beginTransaction()
                .add(R.id.fragment , fragment , AppConstant.MAINFRAGMMENT).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppController.setInForeground(false);
    }

    public void showToolbar(boolean isHomeToolBar, String titleName) {
        invalidateOptionsMenu();
        if (isHomeToolBar) {
            binding.txtViewTitle.setText(R.string.app_name);
            if (binding.toolbar != null) {
                binding.toolbar.setTitle("");
                binding.toolbar.setNavigationIcon(R.drawable.transparent_image);
                binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        }else{
            binding.txtViewTitle.setText(titleName);
            if (binding.toolbar != null) {
                binding.toolbar.setNavigationIcon(R.drawable.transparent_image);
                binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getSupportFragmentManager().popBackStack();
                    }
                });
                binding.toolbar.setNavigationIcon(R.drawable.ic_back);
            }
        }
    }

}