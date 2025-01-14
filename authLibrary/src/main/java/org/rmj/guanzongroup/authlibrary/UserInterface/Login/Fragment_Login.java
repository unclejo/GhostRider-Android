/*
 * Created by Android Team MIS-SEG Year 2021
 * Copyright (c) 2021. Guanzon Central Office
 * Guanzon Bldg., Perez Blvd., Dagupan City, Pangasinan 2400
 * Project name : GhostRider_Android
 * Module : GhostRider_Android.authLibrary
 * Electronic Personnel Access Control Security System
 * project file created : 4/24/21 3:19 PM
 * project file last modified : 4/24/21 3:17 PM
 */

package org.rmj.guanzongroup.authlibrary.UserInterface.Login;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.rmj.g3appdriver.GRider.Etc.LoadDialog;
import org.rmj.g3appdriver.GRider.Etc.MessageBox;
import org.rmj.g3appdriver.etc.AppConfigPreference;
import org.rmj.guanzongroup.authlibrary.R;

import java.util.Objects;

public class Fragment_Login extends Fragment implements LoginCallback{
    private static final String TAG = Fragment_Login.class.getSimpleName();
    private VMLogin mViewModel;
    private LoadDialog dialog;
    private TextInputEditText tieEmail, tiePassword, tieMobileNo;
    private TextInputLayout tilMobileNo;

    private TextView tvForgotPassword, tvCreateAccount, tvTerms, lblVersion;
    private MaterialButton btnLogin;
    private NavController navController;
    private CheckBox cbAgree;

    private AppConfigPreference poConfigx;

    public static Fragment_Login newInstance() {
        return new Fragment_Login();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        dialog = new LoadDialog(getActivity());
        poConfigx = AppConfigPreference.getInstance(getActivity());
        navController = Navigation.findNavController(getActivity(), R.id.fragment_auth_container);
        tieEmail = v.findViewById(R.id.tie_loginEmail);
        tiePassword = v.findViewById(R.id.tie_loginPassword);
        tieMobileNo = v.findViewById(R.id.tie_loginMobileNo);
        tilMobileNo = v.findViewById(R.id.til_loginMobileNo);
        tvForgotPassword = v.findViewById(R.id.tvForgotPassword);
        tvTerms = v.findViewById(R.id.tvTerms);
        tvCreateAccount = v.findViewById(R.id.tvCreateAccount);
        lblVersion = v.findViewById(R.id.lbl_versionInfo);
        cbAgree = v.findViewById(R.id.cbAgree);
        btnLogin = v.findViewById(R.id.btn_login);
        return v;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VMLogin.class);

        tieMobileNo.setText(mViewModel.getMobileNo());
        tilMobileNo.setVisibility(mViewModel.hasMobileNo());

        cbAgree.setChecked(mViewModel.isAgreed());

        cbAgree.setOnCheckedChangeListener((buttonView, isChecked) -> mViewModel.setAgreedOnTerms(isChecked));

        btnLogin.setOnClickListener(view -> {
            String email = Objects.requireNonNull(tieEmail.getText()).toString();
            String password = Objects.requireNonNull(tiePassword.getText()).toString();
            String mobileNo = tieMobileNo.getText().toString();
            mViewModel.Login(new UserAuthInfo(email,password, mobileNo), Fragment_Login.this);
        });

        lblVersion.setText(poConfigx.getVersionInfo());

        tvCreateAccount.setOnClickListener(view -> navController.navigate(R.id.action_fragment_Login_to_fragment_CreateAccount));
        tvTerms.setOnClickListener(view -> navController.navigate(R.id.action_fragment_Login_to_fragment_TermsAndConditions));
        tvForgotPassword.setOnClickListener(view -> navController.navigate(R.id.action_fragment_Login_to_fragment_ForgotPassword));
    }

    @Override
    public void OnAuthenticationLoad(String Title, String Message) {
        dialog.initDialog(Title, Message, false);
        dialog.show();
    }

    @Override
    public void OnSuccessLoginResult() {
        dialog.dismiss();
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void OnFailedLoginResult(String message) {
        dialog.dismiss();
        MessageBox loMessage = new MessageBox(getActivity());
        loMessage.initDialog();
        loMessage.setTitle("GhostRider");
        loMessage.setMessage(message);
        loMessage.setPositiveButton("Okay", (view, dialog) -> dialog.dismiss());
        loMessage.show();
    }
}