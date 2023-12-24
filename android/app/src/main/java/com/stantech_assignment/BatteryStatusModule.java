// BatteryStatusModule.java

package com.stantech_assignment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class BatteryStatusModule extends ReactContextBaseJavaModule {

  public BatteryStatusModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public String getName() {
    return "BatteryStatus";
  }

  @ReactMethod
  public void getBatteryOptimizationStatus(Promise promise) {
    Context context = getReactApplicationContext();

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
      PowerManager powerManager = (PowerManager) context.getSystemService(
        Context.POWER_SERVICE
      );
      boolean isPowerSaveMode = powerManager.isPowerSaveMode();
      promise.resolve(isPowerSaveMode);
    } else {
      promise.reject(
        "UNSUPPORTED_ANDROID_VERSION",
        "This feature requires Android Lollipop MR1 or higher"
      );
    }
  }
}
