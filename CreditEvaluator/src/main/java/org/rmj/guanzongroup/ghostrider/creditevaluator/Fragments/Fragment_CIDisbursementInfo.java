package org.rmj.guanzongroup.ghostrider.creditevaluator.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.rmj.g3appdriver.GRider.Database.Entities.ECIEvaluation;
import org.rmj.g3appdriver.GRider.Etc.FormatUIText;
import org.rmj.g3appdriver.GRider.Etc.GToast;
import org.rmj.guanzongroup.ghostrider.creditevaluator.Activity.Activity_CIApplication;
import org.rmj.guanzongroup.ghostrider.creditevaluator.Etc.ViewModelCallBack;
import org.rmj.guanzongroup.ghostrider.creditevaluator.Model.CIDisbursementInfoModel;
import org.rmj.guanzongroup.ghostrider.creditevaluator.R;
import org.rmj.guanzongroup.ghostrider.creditevaluator.ViewModel.VMCIDisbursement;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Objects;
import java.util.StringTokenizer;

public class Fragment_CIDisbursementInfo extends Fragment implements ViewModelCallBack {

    private VMCIDisbursement mViewModel;
    private final DecimalFormat currency_total = new DecimalFormat("###,###,###.###");
    private CIDisbursementInfoModel infoModel;
    private TextInputEditText tieWater,
            tieElctx,
            tieFoodx,
            tieLoans,
            tieEducation,
            tieOthers,
            tieTotalExpenses;
    private MaterialButton btnPrevious, btnNext;

    private TextView sCompnyNm;
    private TextView dTransact;
    private TextView sModelNm;
    private TextView nTerm;
    private TextView nMobile;
    private TextView sTransNox;
    public static Fragment_CIDisbursementInfo newInstance() {
        return new Fragment_CIDisbursementInfo();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_ci_disbursement_info, container, false);
        infoModel = new CIDisbursementInfoModel();
        initWidgets(root);
        initClientInfo();
        return root;
    }
    void initWidgets(View view){
        tieWater = view.findViewById(R.id.tie_ci_dbmWater);
        tieElctx = view.findViewById(R.id.tie_ci_dbmElectricity);
        tieFoodx = view.findViewById(R.id.tie_ci_dbmFood);
        tieLoans = view.findViewById(R.id.tie_ci_dbmLoans);
        tieEducation = view.findViewById(R.id.tie_ci_dbmEducation);
        tieOthers = view.findViewById(R.id.tie_ci_dbmOthers);
        tieTotalExpenses = view.findViewById(R.id.tie_ci_dbmExpenses);

        btnPrevious = view.findViewById(R.id.btn_ciAppPrvs);
        btnNext = view.findViewById(R.id.btn_ciAppNext);

        sCompnyNm = view.findViewById(R.id.lbl_ci_applicantName);
        dTransact = view.findViewById(R.id.lbl_ci_applicationDate);
        sModelNm = view.findViewById(R.id.lbl_ci_modelName);
        nTerm = view.findViewById(R.id.lbl_ci_accntTerm);
        nMobile = view.findViewById(R.id.lbl_ci_mobileNo);
        sTransNox = view.findViewById(R.id.lbl_ci_transNox);

    }

    public void initClientInfo(){
        sTransNox.setText(Activity_CIApplication.getInstance().getTransNox());
        sCompnyNm.setText(Activity_CIApplication.getInstance().getsCompnyNm());
        dTransact.setText(Activity_CIApplication.getInstance().getdTransact());
        sModelNm.setText(Activity_CIApplication.getInstance().getsModelNm());
        nTerm.setText(Activity_CIApplication.getInstance().getnTerm());
        nMobile.setText(Activity_CIApplication.getInstance().getnMobile());
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VMCIDisbursement.class);
        mViewModel.getCIByTransNox(Activity_CIApplication.getInstance().getTransNox()).observe(getViewLifecycleOwner(), eciEvaluation -> {
            mViewModel.setCurrentCIDetail(eciEvaluation);
            initData(eciEvaluation);
        });
        mViewModel.getTotalAmount().observe(getViewLifecycleOwner(), total-> tieTotalExpenses.setText(String.valueOf(total)));
        tieWater.addTextChangedListener(new OnAmountEnterTextWatcher(tieWater));
        tieElctx.addTextChangedListener(new OnAmountEnterTextWatcher(tieElctx));
        tieFoodx.addTextChangedListener(new OnAmountEnterTextWatcher(tieFoodx));
        tieLoans.addTextChangedListener(new OnAmountEnterTextWatcher(tieWater));
        tieEducation.addTextChangedListener(new OnAmountEnterTextWatcher(tieEducation));
        tieOthers.addTextChangedListener(new OnAmountEnterTextWatcher(tieOthers));
        btnNext.setOnClickListener(v -> {
            infoModel.setCiDbmWater(tieWater.getText().toString());
            infoModel.setCiDbmElectricity(tieElctx.getText().toString());
            infoModel.setCiDbmLoans(tieLoans.getText().toString());
            infoModel.setCiDbmEducation(tieEducation.getText().toString());
            infoModel.setCiDbmFood(tieFoodx.getText().toString());
            infoModel.setCiDbmOthers(tieOthers.getText().toString());
            mViewModel.saveCIDisbursement(infoModel, Fragment_CIDisbursementInfo.this);
        });
        btnPrevious.setOnClickListener(v ->
                Activity_CIApplication.getInstance().moveToPageNumber(0));
        // TODO: Use the ViewModel
    }
    public void initData(ECIEvaluation eciEvaluation){
        try {
            tieWater.setText("");
            tieElctx.setText("");
            tieFoodx.setText("");
            tieLoans.setText("");
            tieEducation.setText("");
            tieOthers.setText("");

            if (eciEvaluation.getWaterBil() != null){
                tieWater.setText(eciEvaluation.getWaterBil());
                mViewModel.setnWater(Double.valueOf(tieWater.getText().toString().replace(",", "")));
            }

            if (eciEvaluation.getElctrcBl() != null){
                tieElctx.setText(eciEvaluation.getElctrcBl());
                mViewModel.setnElctx(Double.valueOf(tieElctx.getText().toString().replace(",", "")));
            }

            if (eciEvaluation.getFoodAllw() != null){
                tieFoodx.setText(eciEvaluation.getFoodAllw());
                mViewModel.setnFoodx(Double.valueOf(tieFoodx.getText().toString().replace(",", "")));
            }

            if (eciEvaluation.getLoanAmtx() != null){
                tieLoans.setText(eciEvaluation.getLoanAmtx());
                mViewModel.setnLoans(Double.valueOf(tieLoans.getText().toString().replace(",", "")));
            }

            if (eciEvaluation.getEducExpn() != null){
                tieEducation.setText(eciEvaluation.getEducExpn());
                mViewModel.setnEducation(Double.valueOf(tieEducation.getText().toString().replace(",", "")));
            }

            if (eciEvaluation.getOthrExpn() != null){
                tieOthers.setText(eciEvaluation.getOthrExpn());
                mViewModel.setnEducation(Double.valueOf(tieOthers.getText().toString().replace(",", "")));
            }
        }catch (NullPointerException e){

        }catch (NumberFormatException e){

        }

    };

    @SuppressLint("RestrictedApi")
    @Override
    public void onSaveSuccessResult(String args) {
        Activity_CIApplication.getInstance().moveToPageNumber(2);

    }

    @Override
    public void onFailedResult(String message) {
        GToast.CreateMessage(getActivity(), message, GToast.ERROR).show();

    }

    private class OnAmountEnterTextWatcher implements TextWatcher{
        private final TextInputEditText inputEditText;

        OnAmountEnterTextWatcher(TextInputEditText inputEditText){
            this.inputEditText = inputEditText;
        }


        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            try {
                inputEditText.removeTextChangedListener(this);
                if(!Objects.requireNonNull(inputEditText.getText()).toString().trim().isEmpty()) {
                    if (inputEditText.getId() == R.id.tie_ci_dbmWater) {
                        mViewModel.setnWater(Double.valueOf(inputEditText.getText().toString().replace(",", "")));
                    } else if (inputEditText.getId() == R.id.tie_ci_dbmElectricity) {
                        mViewModel.setnElctx(Double.valueOf(inputEditText.getText().toString().replace(",", "")));
                    } else if (inputEditText.getId() == R.id.tie_ci_dbmFood) {
                        mViewModel.setnFoodx(Double.valueOf(inputEditText.getText().toString().replace(",", "")));
                    }else if (inputEditText.getId() == R.id.tie_ci_dbmLoans) {
                        mViewModel.setnLoans(Double.valueOf(inputEditText.getText().toString().replace(",", "")));
                    } else if (inputEditText.getId() == R.id.tie_ci_dbmEducation) {
                        mViewModel.setnEducation(Double.valueOf(inputEditText.getText().toString().replace(",", "")));
                    } else if (inputEditText.getId() == R.id.tie_ci_dbmOthers) {
                        mViewModel.setOthers(Double.valueOf(inputEditText.getText().toString().replace(",", "")));
                    }
                }
                inputEditText.addTextChangedListener(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            FormatCurrency(inputEditText);
        }
        private void FormatCurrency(TextInputEditText txt){
            try
            {
                txt.removeTextChangedListener(this);
                String value = Objects.requireNonNull(txt.getText()).toString();

                if (!value.equals(""))
                {

                    if(value.startsWith(".")){
                        txt.setText("0.");
                    }
                    if(value.startsWith("0") && !value.startsWith("0.")){
                        txt.setText("");

                    }

                    String str = txt.getText().toString().replaceAll(",", "");
                    txt.setText(getDecimalFormattedString(str));
                    txt.setSelection(txt.getText().toString().length());
                }
                txt.addTextChangedListener(this);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                txt.addTextChangedListener(this);
            }

        }
    }

    private static String getDecimalFormattedString(String value)
    {
        StringTokenizer lst = new StringTokenizer(value, ".");
        String str1 = value;
        String str2 = "";
        if (lst.countTokens() > 1)
        {
            str1 = lst.nextToken();
            str2 = lst.nextToken();
        }
        StringBuilder str3 = new StringBuilder();
        int i = 0;
        int j = -1 + str1.length();
        if (str1.charAt( -1 + str1.length()) == '.')
        {
            j--;
            str3 = new StringBuilder(".");
        }
        for (int k = j;; k--)
        {
            if (k < 0)
            {
                if (str2.length() > 0)
                    str3.append(".").append(str2);
                return str3.toString();
            }
            if (i == 3)
            {
                str3.insert(0, ",");
                i = 0;
            }
            str3.insert(0, str1.charAt(k));
            i++;
        }

    }
}