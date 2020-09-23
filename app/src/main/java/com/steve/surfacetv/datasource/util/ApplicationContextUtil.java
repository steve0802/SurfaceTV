package com.steve.surfacetv.datasource.util;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.util.Log;

import androidx.annotation.NonNull;

public class ApplicationContextUtil extends Application {
    public static final String APP_LOG_TAG = "Surface TV";
    private static Context context;
    private static boolean isNetworkAvailable = false;

    public void onCreate() {
        super.onCreate();
        ApplicationContextUtil.context = getApplicationContext();

        //init network status monitor
        ConnectivityManager connMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr != null) {
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            NetworkRequest request = builder.build();
            connMgr.registerNetworkCallback(request, new ConnectivityManager.NetworkCallback() {
                    @Override
                    public void onAvailable(@NonNull Network network) {
                        super.onAvailable(network);
                        Log.d(ApplicationContextUtil.APP_LOG_TAG, "ApplicationContextUtil - network onAvailable");
                        ApplicationContextUtil.isNetworkAvailable = true;
                    }

                    @Override
                    public void onLost(@NonNull Network network) {
                        super.onLost(network);
                        Log.d(ApplicationContextUtil.APP_LOG_TAG, "ApplicationContextUtil - network onLost");
                        ApplicationContextUtil.isNetworkAvailable = false;
                    }
                });
        }

        //init preference util
        SharedPreferenceUtil.getInstance().init(
                ApplicationContextUtil.context.getSharedPreferences(SharedPreferenceUtil.PRE_XML_NAME, MODE_PRIVATE)
            );
    }

    public static Context getAppContext() {
        return ApplicationContextUtil.context;
    }

    public static boolean isNetworkAvailable() {
        return ApplicationContextUtil.isNetworkAvailable;
    }
}
