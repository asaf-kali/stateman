package com.kali.stateman;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class BundleLoader {

    private static final String TAG = "BundleSaver";

    /* --- Fields --- */

    private Bundle state;

    /* --- API --- */

    public BundleLoader(Bundle state) {
        if (state == null)
            throw new IllegalArgumentException("State should not be null.");
        this.state = state;
    }

    public void loadAll(View... views) {
        for (View view : views)
            loadView(view);
    }

    public BundleLoader loadView(View view) {
        Log.i(TAG, "Loading into " + view.getId());
        if (view instanceof TextView)
            loadString((TextView) view, BundleLoader::textViewSetter);
        else throw new UnsupportedOperationException("Loading [" + view.getClass().getName() +
                "] type is not implemented.");
        return this;

    }

    /* --- Types --- */

    public interface ViewSetter<T, S> {
        void setValue(T view, S value);
    }

    public interface BundleGetter<T> {
        T getValue(Bundle state, String key);
    }

    /* --- Savers --- */

    private <T extends View, S> void load(T view, ViewSetter<T, S> setter,
                                          BundleGetter<S> getter, Class<S> valueClass) {
        String key = String.valueOf(view.getId());
        S value = valueClass.cast(getter.getValue(state, key));
        setter.setValue(view, value);
    }

    public <T extends View> void loadString(T view, ViewSetter<T, String> setter) {
        load(view, setter, Bundle::getString, String.class);
    }

    public <T extends View> void loadDouble(T view, ViewSetter<T, Double> setter) {
        load(view, setter, Bundle::getDouble, Double.class);
    }

    /* --- Value Setters --- */

    public static void textViewSetter(TextView v, String value) {
        v.setText(value);
    }

}