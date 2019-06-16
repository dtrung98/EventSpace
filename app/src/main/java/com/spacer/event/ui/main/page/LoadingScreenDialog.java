package com.spacer.event.ui.main.page;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spacer.event.R;
import com.spacer.event.ui.widget.SuccessTickView;
import com.tuyenmonkey.mkloader.MKLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class LoadingScreenDialog extends DialogFragment {

    public static LoadingScreenDialog newInstance(Context context) {
        LoadingScreenDialog dialog = new LoadingScreenDialog();
        return dialog;
    }

    @BindView(R.id.success_view)
    SuccessTickView mSuccessView;

    @BindView(R.id.mkloader)
    MKLoader mLoader;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.loading_layout,container,false);
    }

    @Override
    public int getTheme() {
        return R.style.DialogDimDisabled;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        ButterKnife.bind(this,view);
        setCancelable(false);
    }

    public void showSuccessThenDismiss(String message) {
        mLoader.setVisibility(View.GONE);
        mSuccessView.setVisibility(View.VISIBLE);
        mSuccessView.startTickAnim();

        if(message!=null&&!message.isEmpty())
        Toasty.success(getContext(),message).show();

    }

    public void showFailureThenDismiss(String error) {
        if(isResumed())
        try {
            mLoader.setVisibility(View.GONE);
            if(getContext()!=null)
            Toasty.error(getContext(), error).show();
            this.dismiss();
        } catch (Exception ignored) {}
    }

    DialogInterface.OnCancelListener mOnCancelListener;

    public void setOnCancelListener(DialogInterface.OnCancelListener listener) {
        mOnCancelListener = listener;
    }

    public void removeOnCancelListener() {
        mOnCancelListener = null;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if(mOnCancelListener!=null) mOnCancelListener.onCancel(dialog);
        super.onCancel(dialog);
    }
}
