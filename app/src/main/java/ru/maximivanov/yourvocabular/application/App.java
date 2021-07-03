package ru.maximivanov.yourvocabular.application;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

public class App extends Application {
    private static Application application;
    private static Resources resources;

    public static Context getContext() {
        return application.getApplicationContext();
    }

    public static String getStrRes(int resID) {
        return resources.getString(resID);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        resources = super.getResources();
    }
}
