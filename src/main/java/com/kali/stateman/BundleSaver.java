package com.kali.stateman;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;

import static com.kali.stateman.Util.TAG;
import static com.kali.stateman.Util.prm;

public class BundleSaver {

    /* --- Fields --- */

    private Bundle state;

    /* --- API --- */

    /**
     * Constructs a new BundleSaver.
     *
     * @param state the non-null state to saves the values into.
     */
    public BundleSaver(@NonNull Bundle state) {
        this.state = state;
    }

    /**
     * Saves all given views with their default value getters.
     *
     * @param views the views to save into the bundle state.
     */
    public void saveAll(View... views) {
        for (View view : views)
            saveView(view);
    }

    /**
     * Saves the given view with it's default value getter.
     *
     * @param view the view to save into the bundle state.
     * @return this bundle after saving.
     */
    public BundleSaver saveView(View view) {
        if (view instanceof TextView)
            saveString((TextView) view, BundleSaver::textViewGetter);
        else throw new UnsupportedOperationException("Saving " + prm(view.getClass().getName()) +
                " type is not implemented.");
        return this;
    }

    /* --- Types --- */

    /**
     * A functional interface for a view getter data.
     *
     * @param <T> The type of the view.
     * @param <S> The type of the value.
     */
    public interface ViewGetter<T, S> {
        /**
         * @param view the view to extract the data from.
         * @return the value wanted from the view.
         */
        S getValue(T view);
    }

    /**
     * A functional interface for a bundle value put function.
     *
     * @param <T> the type of the value to put.
     */
    public interface BundleSetter<T> {
        /**
         * Puts the given value with the given key in the given bundle.
         *
         * @param state the bundle to put the value into.
         * @param key   the key to use.
         * @param value the value to be saved.
         */
        void setValue(Bundle state, String key, T value);
    }

    /* --- Savers --- */

    /**
     * Saves a value from a view into the bundle.
     *
     * @param view   the view to save it's data.
     * @param getter the value getter from the view.
     * @param setter the value setter into the bundle.
     * @param type   the value's type class.
     * @param <T>    the type of the view.
     * @param <S>    the type of the value.
     * @return this instance after saving.
     */
    private <T extends View, S> BundleSaver save(T view, ViewGetter<T, S> getter,
                                                 BundleSetter<S> setter, Class<S> type) {
        S value = type.cast(getter.getValue(view));
        String key = String.valueOf(view.getId());
        Log.i(TAG, "Saving value " + prm(value) + " from view " + prm(key) + ".");
        setter.setValue(state, key, value);
        return this;
    }

    /**
     * Saves a Serializable value received from a view into the bundle.
     *
     * @param view   the view to extract the Serializable value from.
     * @param getter the view Serializable value getter.
     * @param type   the value's type class.
     * @return this instance after saving.
     */
    public <T extends View, S extends Serializable> BundleSaver saveSerializable(T view, ViewGetter<T, S> getter, Class<S> type) {
        return save(view, getter, Bundle::putSerializable, type);
    }

    /**
     * Saves a String value from a view into the bundle.
     *
     * @param view   the view to extract the value from.
     * @param getter the view's value getter.
     * @param <T>    the type of the view.
     * @return this instance after saving.
     */
    public <T extends View> BundleSaver saveString(T view, ViewGetter<T, String> getter) {
        return save(view, getter, Bundle::putString, String.class);
    }

    /**
     * Saves a Double value into the bundle.
     *
     * @param view   the view to extract the value from.
     * @param getter the value getter from the view.
     * @param <T>    the type of the view.
     * @return this instance after saving.
     */
    public <T extends View> BundleSaver saveDouble(T view, ViewGetter<T, Double> getter) {
        return save(view, getter, Bundle::putDouble, Double.class);
    }

    /* --- Value Getters --- */

    /**
     * @param v a TextView to get the text from.
     * @return the text written inside a text view.
     */
    public static String textViewGetter(TextView v) {
        return v.getText().toString();
    }

}