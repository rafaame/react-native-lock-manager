package me.rafaa.react;

import android.support.annotation.Nullable;
import android.util.Base64;

import com.facebook.common.logging.FLog;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.GuardedAsyncTask;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Inet6Address;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.view.WindowManager;
import android.os.PowerManager;
import android.net.wifi.WifiManager;

public final class LockManager extends ReactContextBaseJavaModule {
    private static final String TAG = "LockManager";

    private ReactContext mReactContext;
    private PowerManager.WakeLock wakeLock;
    private WifiManager.WifiLock wifiLock;

    public LockManager(ReactApplicationContext reactContext) {
        super(reactContext);
        mReactContext = reactContext;
    }

    @Override
    public String getName() {
        return TAG;
    }

    @ReactMethod
    public void unlock() {
        KeyguardManager myKM = (KeyguardManager) mReactContext.getSystemService(Context.KEYGUARD_SERVICE);

        final Activity activity = this.getCurrentActivity();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    PowerManager powerManager = (PowerManager) mReactContext.getSystemService(Context.POWER_SERVICE);
                    WifiManager wifiManager = (WifiManager) mReactContext.getSystemService(Context.WIFI_SERVICE);

                    if (powerManager != null) {
                        if (wakeLock == null) {
                            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
                        }

                        if (wakeLock != null && ! wakeLock.isHeld()) {
                            wakeLock.acquire();
                        }
                    }

                    if (wifiManager != null) {
                        if (wifiLock == null) {
                            wifiLock = wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL, TAG);
                        }
                        
                        if (wifiLock != null && ! wifiLock.isHeld()) {
                            wifiLock.acquire();
                        }
                    }

                    int flags = WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | 
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
                    
                    activity.getWindow().addFlags(flags);
                }
            });
        }
    }
}
