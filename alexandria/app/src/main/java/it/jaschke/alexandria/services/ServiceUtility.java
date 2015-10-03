package it.jaschke.alexandria.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ServiceUtility {
    private Context context;

    public ServiceUtility(Context context) {
        this.context = context;
    }

    public boolean hasNetworkConnection() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) return false;
        NetworkInfo.State network = info.getState();
        return (network == NetworkInfo.State.CONNECTED || network == NetworkInfo.State.CONNECTING);
    }
}
