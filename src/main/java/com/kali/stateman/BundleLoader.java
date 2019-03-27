package com.kali.stateman;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import static com.kali.stateman.Util.TAG;
import static com.kali.stateman.Util.prm;

public class BundleLoader {

    /* --- Fields --- */

    private Bundle state;

    /* --- API --- */

    /**
     * Constructs a new BundleLoader.
     *
     * @param state the state to load the values from. Should not be null.
     */
    public BundleLoader(Bundle state) {
        if (state == null)
            throw new IllegalArgumentException("State should not be null.");
        this.state = state;
    }

    /**
     * Loads all given views with their default value setters.
     *
     * @param views the views to load from the bundle state.
     */
    public void loadAll(View... views) {
        for (View view : views)
            loadView(view);
    }

    /**
     * Loads the given view with it's default value setter.
     *
     * @param view the view to load from the bundle state.
     * @return this bundle after loading.
     */
    public BundleLoader loadView(View view) {
        if (view instanceof TextView)
            loadString((TextView) view, BundleLoader::textViewSetter);
        else throw new UnsupportedOperationException("Loading " + prm(view.getClass().getName()) +
                " type is not implemented.");
        return this;

    }

    /* --- Types --- */

    /**
     * A functional interface for a view value setter.
     *
     * @param <T> The type of the view.
     * @param <S> The type of the value.
     */
    public interface ViewSetter<T, S> {
        /**
         * Sets a view's value field.
         *
         * @param view  the view to set the value into.
         * @param value the value to put in the view.
         */
        void setValue(T view, S value);
    }

    /**
     * A functional interface for a bundle value getter.
     *
     * @param <T> the type of the value to set.
     */
    public interface BundleGetter<T> {
        /**
         * Gets the given value with the given key from the given bundle.
         *
         * @param state the bundle to get the value from.
         * @param key   the key to use.
         */
        T getValue(Bundle state, String key);
    }

    /* --- Loaders --- */

    /**
     * Loads a value into a view from the bundle.
     *
     * @param view       the view to load the value into.
     * @param setter     the view's value setter.
     * @param getter     the bundle's value getter.
     * @param valueClass the class of the value.
     * @param <T>        the type of the view.
     * @param <S>        the type of the value.
     */
    private <T extends View, S> void load(T view, ViewSetter<T, S> setter,
                                          BundleGetter<S> getter, Class<S> valueClass) {
        String key = String.valueOf(view.getId());
        S value = valueClass.cast(getter.getValue(state, key));
        Log.i(TAG, "Loading value " + prm(value) + " into view " + prm(key) + ".");
        setter.setValue(view, value);
    }

    /**
     * Loads a String value into a view from the bundle.
     *
     * @param view   the view to load the value into.
     * @param setter the view's value setter.
     * @param <T>    the type of the view.
     */
    public <T extends View> void loadString(T view, ViewSetter<T, String> setter) {
        load(view, setter, Bundle::getString, String.class);
    }

    /**
     * Loads a Double value into a view from the bundle.
     *
     * @param view   the view to load the value into.
     * @param setter the view's value setter.
     * @param <T>    the type of the view.
     */
    public <T extends View> void loadDouble(T view, ViewSetter<T, Double> setter) {
        load(view, setter, Bundle::getDouble, Double.class);
    }

    /* --- Value Setters --- */

    /**
     * Sets a TextView's text value.
     *
     * @param v     the TextView to set it's text.
     * @param value the string value to show in the TextView.
     */
    public static void textViewSetter(TextView v, String value) {
        v.setText(value);
    }

}