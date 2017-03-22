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

public final class LockManager extends ReactContextBaseJavaModule {
    private static final String TAG = "LockManager";

    private ReactContext mReactContext;

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
                    int flags = WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | 
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
                    
                    activity.getWindow().addFlags(flags);
                }
            });
        }
    }
}
