package com.upnetix.imagesearch.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProviders;

/**
 * The base activity class which is responsible for setting up the binding
 * @param <VM> the view model for the activity
 */
public abstract class BaseActivity<B extends ViewDataBinding, VM extends BaseViewModel> extends AppCompatActivity {

    /**
     * The view model object for the activity.
     */
    protected VM viewModel;
    /**
     * The binding object of the activity. This can be used for accessing views.
     */
    protected B binding;

    /**
     * @return the view model class
     */
    protected abstract Class<VM> getViewModelClass();

    /**
     * @return the layout resource identifier for the given view
     */
    protected abstract int getLayoutResId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutResId = getLayoutResId();
        setContentView(layoutResId);

        viewModel = ViewModelProviders.of(this).get(getViewModelClass());
        binding = DataBindingUtil.setContentView(this, layoutResId);

        onPrepare();
    }

    /**
     * Called in onCreate. Override to prepare the screen.
     */
    protected void onPrepare(){

    }
}
