package com.sjtu.idoctor.utils.task;

import java.util.concurrent.Executor;

import android.app.Activity;
import android.app.AlertDialog;

import com.sjtu.idoctor.view.dialog.LightProgressDialog;

public abstract class ProgressDialogTask<ResultT> extends SafeAsyncTask<ResultT>{

	private final Activity activity;
	/**
     * Progress dialog last displayed
     */
    protected AlertDialog progress;
	
	/**
     * @param context
     */
    protected ProgressDialogTask(Activity activity){
        super();
        this.activity = activity;
    }

    /**
     * @param context
     * @param executor
     */
    protected ProgressDialogTask(Activity activity, Executor executor){
        super(executor);
        this.activity = activity;
    }
    
    
    /**
     * Dismiss and clear progress dialog field
     */
    protected void dismissProgress() {
        if (progress != null) {
            progress.dismiss();
            progress = null;
        }
    }

    /**
     * Show indeterminate progress dialog with given message
     *
     * @param message
     */
    protected void showIndeterminate(final CharSequence message) {
        dismissProgress();

        progress = LightProgressDialog.create(activity, message);
        progress.show();
    }

    /**
     * Show indeterminate progress dialog with given message
     *
     * @param resId
     */
    protected void showIndeterminate(final int resId) {
        dismissProgress();

        progress = LightProgressDialog.create(activity, resId);
        progress.show();
    }
    
    /**
     * Sub-classes may override but should always call super to ensure the
     * progress dialog is dismissed
     */
    @Override
    protected void onSuccess(ResultT e) throws Exception {
        //dismissProgress();
    }

    /**
     * Sub-classes may override but should always call super to ensure the
     * progress dialog is dismissed
     */
    @Override
    protected void onException(Exception e) throws RuntimeException {
        //dismissProgress();
    }
    
    /**
     * Sub-classes may override but should always call super to ensure the
     * progress dialog is dismissed
     */
    @Override
    protected void onFinally() throws RuntimeException {
        dismissProgress();
    }

    /**
     * Get string from context resources
     *
     * @param resId
     * @return string
     */
    protected String getString(int resId) {
        return activity.getString(resId);
    }
}
