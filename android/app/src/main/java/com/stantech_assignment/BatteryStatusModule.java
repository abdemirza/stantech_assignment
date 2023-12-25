// BatteryStatusModule.java

package com.stantech_assignment;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Build;
import android.os.PowerManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class BatteryStatusModule extends ReactContextBaseJavaModule {

  final int LOCATION_PERMISSION_REQUEST_CODE = 1;
  private ReactApplicationContext reactContext;

  public BatteryStatusModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;

    // Registering the BroadcastReceiver to listen for changes in power save mode
    IntentFilter filter = new IntentFilter();
    filter.addAction(PowerManager.ACTION_POWER_SAVE_MODE_CHANGED);
    reactContext.registerReceiver(batteryReceiver, filter);
  }

  @Override
  public String getName() {
    return "BatteryStatus";
  }

  private final BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
        PowerManager powerManager = (PowerManager) context.getSystemService(
          Context.POWER_SERVICE
        );
        boolean isPowerSaveMode = powerManager.isPowerSaveMode();
        emitBatteryStatus(isPowerSaveMode);
      }
    }
  };

  private void emitBatteryStatus(boolean isPowerSaveMode) {
    reactContext
      .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
      .emit("BatteryStatusChanged", isPowerSaveMode);
  }

  private boolean checkLocationPermission() {
    int permissionResult = ContextCompat.checkSelfPermission(
      getReactApplicationContext(),
      Manifest.permission.ACCESS_FINE_LOCATION
    );
    return permissionResult == PackageManager.PERMISSION_GRANTED;
  }

  @ReactMethod
  public void requestLocationPermission() {
    if (!checkLocationPermission()) {
      ActivityCompat.requestPermissions(
        getCurrentActivity(),
        new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
        LOCATION_PERMISSION_REQUEST_CODE
      );
    }
  }
}
