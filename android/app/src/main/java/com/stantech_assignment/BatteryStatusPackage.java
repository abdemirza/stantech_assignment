// BatteryStatusPackage.java

package com.stantech_assignment;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import java.util.Collections;
import java.util.List;

public class BatteryStatusPackage implements ReactPackage {

  @Override
  public List<NativeModule> createNativeModules(
    ReactApplicationContext reactContext
  ) {
    return Collections.singletonList(new BatteryStatusModule(reactContext));
  }

  @Override
  public List<ViewManager> createViewManagers(
    ReactApplicationContext reactContext
  ) {
    return Collections.emptyList();
  }
}
