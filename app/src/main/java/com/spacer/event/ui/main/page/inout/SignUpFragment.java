package com.spacer.event.ui.main.page.inout;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.FirebaseFirestore;
import com.spacer.event.R;
import com.spacer.event.listener.FireBaseSetDocumentResultListener;
import com.spacer.event.listener.FireBasePushDocumentResultListener;
import com.spacer.event.model.UserInfo;
import com.spacer.event.ui.main.MainActivity;
import com.spacer.event.ui.main.page.LoadingScreenDialog;
import com.spacer.event.ui.widget.fragmentnavigationcontroller.PresentStyle;
import com.spacer.event.ui.widget.fragmentnavigationcontroller.SupportFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class SignUpFragment extends SupportFragment {
    public static final String TAG = "SignUpFragment";
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public static final String FIREBASE_USER_INFOS = "user_infos";


    //@BindView
    @BindView(R.id.edt_name_signup)
    EditText edtName;

    @BindView(R.id.edt_email_signup)
    EditText edtEmail;

    @BindView(R.id.edt_pass_signup)
    EditText edtPass;

    @BindView(R.id.edt_re_pass_signup)
    EditText edtRePass;

    @BindView(R.id.chb_terms_privacy)
    CheckBox mTermsCheckBox;

    @BindView(R.id.btn_create)
    View mCreateButton;


    private String name = "";
    private String email = "";
    private String pass = "";
    private String rePass = "";

    // bind onclick
    //Sign up with email
    @OnClick(R.id.btn_create)
    void signUpWithEmail() {
        if (edtName != null) name = edtName.getText().toString();
        if (edtEmail != null) email = edtEmail.getText().toString();
        if (edtPass != null) pass = edtPass.getText().toString();
        if (edtRePass != null) rePass = edtRePass.getText().toString();

        if (validateAccount(name, email, pass, rePass)){
            showLoading();
            FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(email,pass)
                    .addOnSuccessListener(mFirebaseAuthListener)
                    .addOnFailureListener(mFirebaseAuthListener);
        }
    }
    LoadingScreenDialog mLoadingDialog = null;
    private void showLoading() {
        mLoadingDialog = LoadingScreenDialog.newInstance(getContext());
        mLoadingDialog.show(getChildFragmentManager(),"LoadingDialog");
    }

    private void successDismissLoading(FirebaseUser user) {
        Log.d(TAG, "successDismissLoading: current " + FirebaseAuth.getInstance().getCurrentUser());
        Log.d(TAG, "successDismissLoading: parameter "+user);

        // cái lúc này là lúc mà kết quả trả về thành công á

        // phần dưới này là giống nhau giữa 2 cái sign in và signup
        // bỏ cái loading đi
        // chở 1250 s thì đóng fragment này lại, và thông báo cho activity là đã đăng nhập
        mLoadingDialog.showSuccessThenDismiss("Welcome to Event Space!");
        mCreateButton.postDelayed(() -> {

            if(getActivity() instanceof MainActivity)
                ((MainActivity)getActivity()).justSignIn(user);
            getNavigationController().dismissFragment();
        }, 1250);

    }
    private void failureDismissLoading(String error) {

        mLoadingDialog.showFailureThenDismiss(error);
        mLoadingDialog = null;
    }

    private boolean validateAccount(String name, String email, String password, String rePassword) {
        edtName.setError(null);
        edtEmail.setError(null);
        edtPass.setError(null);
        edtRePass.setError(null);

        if (name.isEmpty()) {
            edtName.setError(getString(R.string.name_empty));
            return false;
        }

        if (email.isEmpty()) {
            edtEmail.setError(getString(R.string.email_empty));
            return false;
        }

        if (!isValidEmail(email)) {
            edtEmail.setError(getString(R.string.email_invalid));
            return false;
        }

        if (password.isEmpty()) {
            edtPass.setError(getString(R.string.password_empty));
            return false;
        }

        if (password.length() < 6) {
            edtPass.setError(getString(R.string.password_length));
            return false;
        }

        if (rePassword.isEmpty()) {
            edtRePass.setError(getString(R.string.password_empty));
            return false;
        }

        if (rePassword.length() < 6) {
            edtRePass.setError(getString(R.string.password_length));
            return false;
        }

        if(!rePassword.equals(password)) {
            edtRePass.setError(getString(R.string.retype_match));
            return false;
        }

        if(!mTermsCheckBox.isChecked()) {
            Toasty.error(mTermsCheckBox.getContext(), getString(R.string.terms_confirm)).show();
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

    @BindView(R.id.root)
    View mRoot;

    @OnClick({R.id.root, R.id.close})
    void back() {
        getNavigationController().dismissFragment();
    }

    @OnClick(R.id.sign_in)
    void goToSignIn() {
        getNavigationController().dismissFragment();
        getNavigationController().presentFragment(SignInFragment.newInstance());
    }

    @Nullable
    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public int defaultTransition() {
        return PresentStyle.ROTATE_DOWN_LEFT;
    }

    @Override
    public int defaultOpenExitTransition() {
        return PresentStyle.SLIDE_LEFT;
    }


    // khi on success cái đăng ký
    // thì gọi tiếp cái này
    private void createUserInfoAndPush(FirebaseUser user) {
//        UserInfo userInfo = new UserInfo();
//        userInfo.setId(user.getUid());

        // điền thông cho info từ form cho đủ
        UserInfo userInfo = new UserInfo();
        userInfo.setBalance(0);
        userInfo.setFullName(name);
        userInfo.setEmail(email);
        userInfo.setId(user.getUid());
        userInfo.setUserType(UserInfo.CUSTOMER);
        if(user.getPhotoUrl()!=null)
        userInfo.setAvaUrl(user.getPhotoUrl().getPath());
        // push
        pushUserInfoToFirebase(userInfo);
    }

    // Tạo một cái document
    private void pushUserInfoToFirebase(UserInfo userInfo) {
        FirebaseFirestore.getInstance()
                .collection(FIREBASE_USER_INFOS)
                .document(userInfo.getId())
                .set(userInfo)
                .addOnSuccessListener(mPushUserInfoListener)
                .addOnFailureListener(mPushUserInfoListener);

    }
    private FireBaseSetDocumentResultListener mFirebaseAuthListener = new FireBaseSetDocumentResultListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            failureDismissLoading(e.getMessage());
        }

        @Override
        public void onSuccess(AuthResult authResult) {
            createUserInfoAndPush(authResult.getUser());
        }
    };

    private FireBasePushDocumentResultListener mPushUserInfoListener = new FireBasePushDocumentResultListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            failureDismissLoading(e.getMessage());
        }

        @Override
        public void onSuccess(Void aVoid) {
            // try to create user info collection
            successDismissLoading(FirebaseAuth.getInstance().getCurrentUser());
        }

    };
}
