package com.spacer.event.ui.main.page.inout;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.spacer.event.R;
import com.spacer.event.ui.widget.fragmentnavigationcontroller.PresentStyle;
import com.spacer.event.ui.widget.fragmentnavigationcontroller.SupportFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class SignUpFragment extends SupportFragment {
    public static final String TAG = "SignUpFragment";

    public static SignUpFragment newInstance() {

        Bundle args = new Bundle();

        SignUpFragment fragment = new SignUpFragment();
        fragment.setArguments(args);
        return fragment;
    }

 /*   @OnClick(R.id.back)
    void back() {
        getMainActivity().dismiss();
    }*/

    @BindView(R.id.root) View mRoot;


    @BindView(R.id.edi_fullname)
    TextInputLayout mEditFullName;

    @BindView(R.id.edit_email) TextInputLayout mEdiEmail;

    @BindView(R.id.edit_password) TextInputLayout mEdiPassword;

    @BindView(R.id.edit_retype) TextInputLayout mEdiRetype;

    @BindView(R.id.chb_terms_privacy)
    CheckBox mCheckbox;


    @OnClick({R.id.root,R.id.close})
    void back() {
        getNavigationController().dismissFragment();
    }

    @OnClick(R.id.sign_up)
    void signUp() {
        String fullname ="";
        if(mEditFullName.getEditText()!=null)
        fullname = mEditFullName.getEditText().getText().toString().trim();

        String email = "";
        if(mEdiEmail.getEditText()!=null)
            email = mEdiEmail.getEditText().getText().toString().trim();

        String password="";
        if(mEdiPassword.getEditText()!=null)
        password = mEdiPassword.getEditText().getText().toString().trim();

        String retype="";
        if(mEdiRetype.getEditText()!=null)
        retype = mEdiRetype.getEditText().getText().toString().trim();

       /* FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword().*/
    }

    private boolean validateInfo(String fullname, String email, String password, String retype) {

        mEditFullName.setError(null);
        mEdiEmail.setError(null);
        mEdiPassword.setError(null);
        mEdiRetype.setError(null);


        if(fullname.isEmpty()){
            mEditFullName.setError(getString(R.string.fullname_empty));
            return false;
        }

        if(email.isEmpty()){
            mEdiEmail.setError(getString(R.string.email_empty));
            return false;
        }

        if(!isValidEmail(email)){
            mEdiEmail.setError(getString(R.string.email_invalid));
            return false;
        }

        if(password.isEmpty()){
            mEdiPassword.setError(getString(R.string.password_empty));
            return false;
        }

        if(password.length()<6){
            mEdiPassword.setError(getString(R.string.password_length));
            return false;
        }

        if(retype.isEmpty()){
            mEdiRetype.setError(getString(R.string.retype_empty));
            return false;
        }

        if(!retype.matches(password)){
            mEdiRetype.setError(getString(R.string.retype_match));
            return false;
        }

            if(!mCheckbox.isChecked()){
                if(getContext()!=null)
                Toasty.warning(getContext(),R.string.terms_confirm).show();
                return false;
            }

        return true;
    }

    public boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    @OnClick(R.id.sign_in)
    void goToSignIn() {
        getNavigationController().dismissFragment();
        getNavigationController().presentFragment(SignInFragment.newInstance());
    }


    @Nullable
    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.sign_up,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
    }

    @Override
    public int defaultTransition() {
        return PresentStyle.ROTATE_DOWN_LEFT;
    }

    @Override
    public int defaultOpenExitTransition() {
        return PresentStyle.SLIDE_LEFT;
    }

}
