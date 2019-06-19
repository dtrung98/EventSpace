package com.spacer.event.ui.main.root;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.spacer.event.model.UserInfo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;
import com.spacer.event.R;
import com.spacer.event.ui.widget.fragmentnavigationcontroller.PresentStyle;
import com.spacer.event.ui.widget.fragmentnavigationcontroller.SupportFragment;

import java.util.Calendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailProfileFragment extends SupportFragment {
    private static final String TAG="ProfileDetail";
    UserInfo userInfo;
    FirebaseAuth mAuth;
    FirebaseFirestore mDatabase;
    FirebaseUser mFirebaseUser;

    @BindView(R.id.btn_back)
    Button btnBack;

    @BindView(R.id.btn_save)
    Button btnSave;

    @BindView(R.id.avatar)
    RoundedImageView avatar;

    @BindView(R.id.txtBalance)
    TextView balance;

    @BindView(R.id.txt_fullname)
    TextInputEditText edtFullName;

    @BindView(R.id.txt_email)
    TextInputEditText edtEmail;

    @BindView(R.id.rad_group_gender)
    RadioGroup rdgGender;
    @BindView(R.id.rad_male)
    RadioButton radMale;
    @BindView(R.id.rad_female)
    RadioButton radFemale;
    @BindView(R.id.rad_other)
    RadioButton radOther;

    @BindView(R.id.txt_birthday)
    TextView txtBirthday;

    @BindView(R.id.edi_phonenumber)
    TextInputEditText edtPhoneNumber;

    @BindView(R.id.edi_address)
    TextInputEditText edtAddress;

    public static DetailProfileFragment newInstance() {

        Bundle args = new Bundle();

        DetailProfileFragment fragment = new DetailProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.profile_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mAuth =FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        mDatabase = getMainActivity().mDatabase;
        getUserInfoFromFirebae();
    }

    
    @Override
    public int defaultDuration() {
        return PresentStyle.ROTATE_DOWN_LEFT;
    }

    @Override
    public int defaultOpenExitTransition() {
        return PresentStyle.SLIDE_LEFT;
    }

    @OnClick(R.id.txt_birthday)
    void setBirthday() {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        String str_day = Integer.toString(day);
                        String str_month = Integer.toString(month + 1);
                        if (1 == str_day.length()) {
                            str_day = "0" + str_day;
                        }
                        if (1 == str_month.length()) {
                            str_month = "0" + str_month;
                        }
                        txtBirthday.setText(str_day + "/" + str_month + "/" + year);
                    }
                },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),  newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    @OnClick(R.id.btn_back)
     void back(){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(R.layout.bottom_alert);
        bottomSheetDialog.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                getMainActivity().dismiss();
            }
        });
        bottomSheetDialog.show();;
    }

    @OnClick(R.id.btn_save)
    void saveUserInfo(){
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Confỉlm");
        alertDialog.setMessage("Do you want to save change?");
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editUserInfo();
                Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_SHORT).show();
                getMainActivity().dismiss();
            }
        });

        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private void editUserInfo() {
        String name = Objects.requireNonNull(edtFullName.getText()).toString().trim();
        String email = Objects.requireNonNull(edtEmail.getText()).toString().trim();
        String phone = Objects.requireNonNull(edtPhoneNumber.getText()).toString().trim();
        String address = Objects.requireNonNull(edtAddress.getText()).toString().trim();
        String birthday = Objects.requireNonNull(txtBirthday.getText()).toString().trim();
        int gender = -1;

        if(radMale.isChecked()) gender = 0;
        else if (radFemale.isChecked()) gender = 1;
        else  gender = 2;

        userInfo = new UserInfo();
        userInfo.setId(mFirebaseUser.getUid());
        userInfo.setFullName(name);
        userInfo.setEmail(email);
        userInfo.setPhoneNumber(phone);
        userInfo.setAddress(address);
        userInfo.setBirthDay(birthday);
        userInfo.setGender(gender);
        sendUserInfoToFirebase();
    }

    private void getUserInfoFromFirebae() {
        String id = mFirebaseUser.getUid();

        DocumentReference documentReference = mDatabase.collection("user_infos").document(id);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userInfo = documentSnapshot.toObject(UserInfo.class);
                displayUserProfile();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void displayUserProfile() {
        if(userInfo.getAvaUrl().matches("")){
            Glide.with(this).load(R.drawable.user_male).into(avatar);
        }
        else {
            Glide.with(this).load(userInfo.getAvaUrl()).into(avatar);
        }
        balance.setText(Integer.toString(userInfo.getBalance()));
        edtFullName.setText(userInfo.getFullName());
        edtEmail.setText(userInfo.getEmail());
        edtPhoneNumber.setText(userInfo.getPhoneNumber());
        edtAddress.setText(userInfo.getAddress());
        txtBirthday.setText(userInfo.getBirthDay());

        if(userInfo.getGender()== 0){
            radMale.toggle();
        }
        else if(userInfo.getGender()== 1){
            radFemale.toggle();
        }
        else if(userInfo.getGender() == 2){
            radOther.toggle();
        }
    }

    private void sendUserInfoToFirebase() {
        mDatabase.collection("user_infos").document(mFirebaseUser.getUid())
                .set(mFirebaseUser)
                .addOnSuccessListener(aVoid -> {
                    Log.w(TAG, "addUserToDatabase:success");
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "addUserToDatabase:failure", e);
                });
    }
}
