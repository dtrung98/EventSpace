package com.spacer.event.ui.main.page;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.spacer.event.R;
import com.spacer.event.ui.widget.fragmentnavigationcontroller.SupportFragment;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SamplePage extends SupportFragment {
    private static final String TAG = "SamplePage";

    @OnClick(R.id.back)
    void back() {
        getMainActivity().dismiss();
    }
    @OnClick(R.id.button)
    void goToSomeWhere() {
        String text = mEditText.getText().toString();
        int value = -1; try {
        value =Integer.parseInt(text);
        getMainActivity().presentFragment(SamplePageTwo.newInstance(value));
        } catch (Exception e) {
            getMainActivity().presentFragment(new SamplePageTwo());
        }
    }

    @BindView(R.id.edit_text)
    EditText mEditText;

    @Nullable
    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.sample_page,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
    }

    int p = -1;
    @Override
    public int defaultTransition() {
        if(p==-1) {
            Random r = new Random();
            p = r.nextInt(39) + 1; //exclude NONE present style
        }
        return p;
    }
}
