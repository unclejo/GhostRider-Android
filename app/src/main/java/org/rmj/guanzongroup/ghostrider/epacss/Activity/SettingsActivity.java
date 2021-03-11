package org.rmj.guanzongroup.ghostrider.epacss.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;
import org.rmj.g3appdriver.GRider.Constants.AppConstants;
import org.rmj.g3appdriver.GRider.Etc.GeoLocator;
import org.rmj.g3appdriver.GRider.Etc.MessageBox;
import org.rmj.guanzongroup.ghostrider.epacss.R;
import org.rmj.guanzongroup.ghostrider.epacss.ViewModel.VMSettings;
import org.rmj.guanzongroup.ghostrider.epacss.themeController.ThemeHelper;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;

import static android.Manifest.permission.CAMERA;
import static android.net.wifi.WifiConfiguration.Status.strings;
import static org.rmj.g3appdriver.GRider.Constants.AppConstants.CAMERA_REQUEST;
import static org.rmj.g3appdriver.GRider.Constants.AppConstants.LOCATION_REQUEST;

public class SettingsActivity extends AppCompatActivity {

    private VMSettings mViewModel;
    private MessageBox loMessage;
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({GRANTED, DENIED, BLOCKED_OR_NEVER_ASKED })
    public @interface PermissionStatus {}

    public static final int GRANTED = 0;
    public static final int DENIED = 1;
    public static final int BLOCKED_OR_NEVER_ASKED = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        Toolbar toolbar = findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loMessage = new MessageBox(this);
        mViewModel = new ViewModelProvider(this).get(VMSettings.class);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        private SwitchPreferenceCompat themePreference;
        private Preference locationPref, cameraPref, phonePref;
        private VMSettings mViewModel;
        private GeoLocator poLocator;
        private boolean isContinue = false;
        private boolean isGPS = false;
        private int PERMISION_PHONE_REQUEST_CODE = 103, PERMISION_CAMERA_REQUEST_CODE = 102, PERMISION_LOCATION_REQUEST_CODE = 104;

        private MessageBox loMessage;
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences, rootKey);
            themePreference = getPreferenceManager().findPreference("themePrefs");
            cameraPref = getPreferenceManager().findPreference("cameraPrefs");
            locationPref = getPreferenceManager().findPreference("locationPrefs");
            phonePref = getPreferenceManager().findPreference("phonePrefs");

            loMessage = new MessageBox(getActivity());
            mViewModel = new ViewModelProvider(this).get(VMSettings.class);

            mViewModel.isLocPermissionGranted().observe(this, isGranted -> {
                GetLocation();
            });
            mViewModel.isCamPermissionGranted().observe(this, isGranted -> {
                checkPermissionCamera();
            });
            mViewModel.getCameraSummary().observe(this, s -> cameraPref.setSummary(s));
            if (themePreference != null) {
                themePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        Boolean themeOption = (Boolean) newValue;
                        if (themeOption) {
                            themePreference.getSummaryOn();
                        } else {
                            themePreference.getSummaryOff();
                        }
                        ThemeHelper.applyTheme(themeOption);
                        return true;
                    }
                });
            }
            if (cameraPref != null) {
                cameraPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        if ((ActivityCompat.checkSelfPermission(getActivity(), CAMERA) != PackageManager.PERMISSION_GRANTED)) {
                            mViewModel.getCamPermissions().observe(getViewLifecycleOwner(), strings -> {
                                ActivityCompat.requestPermissions(getActivity(),strings, CAMERA_REQUEST);

                            });
                        }else {
                            loMessage.setNegativeButton("Okay", (view, dialog) -> dialog.dismiss());
                            loMessage.setTitle("GhostRider Permissions");
                            loMessage.setMessage("You have already granted this permission.");
                            loMessage.show();
                        }
                        return true;
                    }
                });
            }

            if (locationPref != null) {
                locationPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    public boolean onPreferenceClick(Preference preference) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),LOCATION_REQUEST);
                        return true;
                    }
                });
            }

        }


        boolean checkPermissionCamera() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    cameraPref.setSummary("Camera Permission Enabled");
                    mViewModel.getCameraSummary().observe(getViewLifecycleOwner(), s -> cameraPref.setSummary(s));
                    return true;
                } else {
                    cameraPref.setSummary("Camera Permission Disabled");
                    mViewModel.getCameraSummary().observe(getViewLifecycleOwner(), s -> cameraPref.setSummary(s));

                    return false;
                }
            }
            return true;
        }

        @Override
        public void onActivityResult(int requestCode, int requestResult, Intent data) {
            if(requestCode == LOCATION_REQUEST){
                mViewModel.setLocationPermissionsGranted(true);
            }
        }
        public void GetLocation(){


            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);


            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                locationPref.setSummary(R.string.location_off_summary);

            } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){


                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                        (getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);

                } else {
                    locationPref.setSummary(R.string.location_on_summary);
                }
            }
        }


    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CAMERA_REQUEST:{
                for(int x = 0 ; x < grantResults.length; x++){
                    getPermissionStatus(this,permissions[x]);
                }
                int index = getPermissionStatus(this, CAMERA);
                if (index == 0){
                    mViewModel.setCameraSummary("Camera Permission Enabled");
                    mViewModel.setCamPermissionsGranted(true);
                }
                if (index == 1){
                    mViewModel.setCameraSummary("Camera Permission Disabled");
                    mViewModel.setCamPermissionsGranted(false);
                }
                if (index == 2){
                    mViewModel.setCameraSummary("Camera Permission Disabled");
                    mViewModel.setCamPermissionsGranted(false);
                    showDialogNeverAsk();
                }
                Log.e("Permission Selected", String.valueOf(getPermissionStatus(this, CAMERA)));
            }
//            case CONTACT_REQUEST: {
//                for(int x = 0 ; x < grantResults.length; x++){
//                    getPermissionStatus(this,permissions[x]);
//                }
//                int index = getPermissionStatusPhoneState(this, READ_PHONE_STATE);
//                if (index == 0){
//                    mViewModel.setCameraSummary("Phone State Permission Enabled");
//                    mViewModel.setPhonePermissionsGranted(true);
//                }
//                if (index == 1){
//                    mViewModel.setCameraSummary("Phone State Permission Disabled");
//                    mViewModel.setPhonePermissionsGranted(false);
//                }
//                if (index == 2){
//                    mViewModel.setCameraSummary("Phone State Permission Disabled");
//                    mViewModel.setPhonePermissionsGranted(false);
//                    showDialogNeverAsk();
//                }
//                Log.e("Permission Selected", String.valueOf(getPermissionStatus(this, READ_PHONE_STATE)));
//            }
        }
    }

    @PermissionStatus
    public static int getPermissionStatus(Activity activity, String androidPermissionName) {
        if(ContextCompat.checkSelfPermission(activity, androidPermissionName) != PackageManager.PERMISSION_GRANTED) {
            if(!ActivityCompat.shouldShowRequestPermissionRationale(activity, androidPermissionName)){
                return BLOCKED_OR_NEVER_ASKED;
            }
            return DENIED;
        }
        return GRANTED;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public void showDialogNeverAsk(){
        loMessage.initDialog();
        loMessage.setNegativeButton("Okay", (view, dialog) -> {
            dialog.dismiss();
            Intent intent = new Intent();
            intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, CAMERA_REQUEST);
        });
        loMessage.setTitle("Never Ask");
        loMessage.setMessage("Without this permission the app is unable to complete some process in the app. Please allow camera in app settings permissions. ");
        loMessage.show();
    }

}

