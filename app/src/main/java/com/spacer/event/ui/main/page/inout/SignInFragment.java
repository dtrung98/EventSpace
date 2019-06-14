package com.spacer.event.ui.main.page.inout;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.spacer.event.R;
import com.spacer.event.listener.FireBaseDocumentSetListener;
import com.spacer.event.ui.main.MainActivity;
import com.spacer.event.ui.main.page.LoadingScreenDialog;
import com.spacer.event.ui.widget.fragmentnavigationcontroller.PresentStyle;
import com.spacer.event.ui.widget.fragmentnavigationcontroller.SupportFragment;
import com.spacer.event.util.PreferenceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInFragment extends SupportFragment {
    public static final String TAG = "SignInFragment";

    public static SignInFragment newInstance() {

        Bundle args = new Bundle();

        SignInFragment fragment = new SignInFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.root) View mRoot;

    @BindView(R.id.btn_sign_in)
    TextView btnSignIn;

    @BindView(R.id.btn_google) TextView btnGoogle;

    @BindView(R.id.btn_facebook) TextView btnFacebook;

    @BindView(R.id.edit_email)
    TextInputLayout mEditMail;

    @BindView(R.id.txt_email)
    TextInputEditText txtEmail;

    @BindView(R.id.edit_password) TextInputLayout mEditPassword;

    @BindView(R.id.txt_password) TextInputEditText txtPassword;
    @BindView(R.id.chb_remember)
    CheckBox mRememberCheckBox;

    @OnClick(R.id.forgot_password)
    void goToPasswordRecovery() {

    }

    @OnClick(R.id.panel)
    void clickPanel(View view){
        if(mEditPassword.getEditText()!=null)
        mEditPassword.getEditText().clearFocus();

        if(mEditMail.getEditText()!=null)
        mEditMail.getEditText().clearFocus();
    }

    private boolean validateAccount(String email, String password){

        mEditMail.setError(null);
        mEditPassword.setError(null);

        if(email.isEmpty()){
            mEditMail.setError(getString(R.string.email_empty));
            return false;
        }

        if(!isValidEmail(email)){
            mEditMail.setError(getString(R.string.email_invalid));
            return false;
        }

        if(password.isEmpty()){
            mEditPassword.setError(getString(R.string.password_empty));
           return false;
        }

        if(password.length()<6){
            mEditPassword.setError(getString(R.string.password_length));
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


    @OnClick({R.id.root,R.id.close})
    void back() {
        getNavigationController().dismissFragment();
    }



    /*@OnClick(R.id.back)
    void back() {
        getMainActivity().dismiss();
    }*/


    @Nullable
    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.sign_in,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        autoFilled();
    }
    private void autoFilled() {
        if(PreferenceUtil.getInstance().isRememberAccount())
        {
            String email = PreferenceUtil.getInstance().getSavedAccount();
            if(email!=null&&!email.isEmpty()&&mEditMail.getEditText()!=null) {
                mEditMail.getEditText().setText(email);

                if(mEditPassword.getEditText()!=null)
                mEditPassword.getEditText().requestFocus();
            }

            mRememberCheckBox.setChecked(true);
        }
    }

    @OnClick(R.id.btn_google)
    void sigInWithGoogle() {

    }

    @OnClick(R.id.btn_facebook)
    void signInWithFaceBook() {
        // lúc nhấn nút facebôk thì nó vô đây nè
        // ông cứ làm giống như hồi đồ án ý

        showLoading(); // nó sẽ show ra cái loading xoay tròm
        // khi pit ket qua r thi
        successDismissLoading(null);
        // hình như kết quả nó trả về ở onActivityResult đúng hong
    }
    LoadingScreenDialog mLoadingDialog = null;

    private void showLoading() {

        mLoadingDialog = LoadingScreenDialog.newInstance(getContext());
        mLoadingDialog.show(getChildFragmentManager(),"LoadingScreenDialog");
    }

    private void successDismissLoading(FirebaseUser user) {
        Log.d(TAG, "successDismissLoading: current " + FirebaseAuth.getInstance().getCurrentUser());
        Log.d(TAG, "successDismissLoading: parameter "+user);

        // cái lúc này là lúc mà kết quả trả về thành công á

        // phần dưới này là giống nhau giữa 2 cái sign in và signup
        // bỏ cái loading đi
        // chở 1250 s thì đóng fragment này lại, và thông báo cho activity là đã đăng nhập
        mLoadingDialog.showSuccessThenDismiss("Hi there, welcome back!");
        btnSignIn.postDelayed(() -> {

            if(getActivity() instanceof MainActivity)
            ((MainActivity)getActivity()).justSignIn(user);
            getNavigationController().dismissFragment();
        }, 1250);

    }
    private void failureDismissLoading(String error) {

        mLoadingDialog.showFailureThenDismiss(error);
        mLoadingDialog = null;
    }

    @OnClick(R.id.btn_sign_in)
    void signInWithForm() {
        String email="",password="";

        EditText editText = mEditMail.getEditText();
        if(editText!=null) email = editText.getText().toString();

        editText = mEditPassword.getEditText();
        if(editText!=null) password = editText.getText().toString();

        PreferenceUtil.getInstance().setRememberAccount(mRememberCheckBox.isChecked());
        PreferenceUtil.getInstance().setSavedAccount(email);

        if(validateAccount(email,password)) {
            // đây nè
            // lúc ngta nhấn nút
            // và form hợp lẹ
            showLoading(); // cái này show ra 1 cái loading (nó che màn hinh đi)
            FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(email,password)
                    .addOnSuccessListener(mSignInWithFormListener) // cái listener
                    .addOnFailureListener(mSignInWithFormListener);
        }

    }

    @Override
    public int defaultTransition() {
        return PresentStyle.ROTATE_DOWN_LEFT;
    }

    @Override
    public int defaultOpenExitTransition() {
        return PresentStyle.SLIDE_LEFT;
    }

    private FireBaseDocumentSetListener mSignInWithFormListener = new FireBaseDocumentSetListener() {
        @Override
        public void onSuccess(AuthResult authResult) {
            successDismissLoading(authResult.getUser());
        }

        @Override
        public void onFailure(@NonNull Exception e) {
            failureDismissLoading(e.getMessage().toString());
        }
    };

    // vi du cai signup cx tuong tu cai sign in nay
}
