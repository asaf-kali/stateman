package com.kali.stateman;

import android.os.BaseBundle;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class BundleSaver {

    private static final String TAG = "BundleSaver";

    /* --- Fields --- */

    private Bundle state;

    /* --- API --- */

    public BundleSaver(Bundle state) {
        if (state == null)
            throw new IllegalArgumentException("State should not be null.");
        this.state = state;
    }

    public void saveAll(android.widget.TextView main_text, android.widget.EditText text_box, View... views) {
        for (View view : views)
            saveView(view);
    }

    public BundleSaver saveView(View view) {
        Log.i(TAG, "Saving into " + view.getId());
        if (view instanceof TextView)
            saveString((TextView) view, BundleSaver::textViewGetter);
        else throw new UnsupportedOperationException("Saving [" + view.getClass().getName() +
                "] type is not implemented.");
        return this;
    }

    /* --- Types --- */

    public interface ViewGetter<T, S> {
        S getValue(T view);
    }

    public interface BundleSetter<T> {
        void setValue(Bundle state, String key, T value);
    }

    /* --- Savers --- */

    private <T extends View, S> void save(T view, ViewGetter<T, S> getter,
                                          BundleSetter<S> setter, Class<S> valueClass) {
        S value = valueClass.cast(getter.getValue(view));
        String key = String.valueOf(view.getId());
        setter.setValue(state, key, value);
    }

    public <T extends View> void saveString(T view, ViewGetter<T, String> getter) {
        save(view, getter, BaseBundle::putString, String.class);
    }

    public <T extends View> void saveDouble(T view, ViewGetter<T, Double> getter) {
        save(view, getter, BaseBundle::putDouble, Double.class);
    }

    /* --- Value Getters --- */

    public static String textViewGetter(TextView v) {
        return v.getText().toString();
    }

}