package org.rmj.guanzongroup.onlinecreditapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.simple.JSONObject;
import org.rmj.g3appdriver.GRider.Etc.GToast;
import org.rmj.g3appdriver.GRider.Etc.MessageBox;
import org.rmj.guanzongroup.onlinecreditapplication.Etc.TextFormatter;
import org.rmj.guanzongroup.onlinecreditapplication.Model.ViewModelCallBack;
import org.rmj.guanzongroup.onlinecreditapplication.R;
import org.rmj.guanzongroup.onlinecreditapplication.ViewModel.VMIntroductoryQuestion;

import java.util.Objects;

    public class CreditApplicationIntroduction extends AppCompatActivity implements ViewModelCallBack {
    public static final String TAG = CreditApplicationIntroduction.class.getSimpleName();
    private VMIntroductoryQuestion mViewModel;

    private AutoCompleteTextView txtBranchNm, txtBrandNm, txtModelNm;
    private TextInputEditText txtDownPymnt, txtAmort;
    private Spinner spnApplType, spnCustomerType, spnTerm;
    private MaterialButton btnCreate;
    public static CreditApplicationIntroduction newInstance() {
        return new CreditApplicationIntroduction();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_application_introduction);
        Toolbar toolbar = findViewById(R.id.toolbar_introduction);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        txtBranchNm = findViewById(R.id.txt_branchName);
        txtBrandNm = findViewById(R.id.txt_brandName);
        txtModelNm = findViewById(R.id.txt_modelName);
        txtDownPymnt = findViewById(R.id.txt_downpayment);
        txtAmort = findViewById(R.id.txt_monthlyAmort);

        spnApplType = findViewById(R.id.spn_applicationType);
        spnCustomerType = findViewById(R.id.spn_customerType);
        spnTerm = findViewById(R.id.spn_installmentTerm);

        btnCreate = findViewById(R.id.btn_createCreditApp);
        txtDownPymnt.addTextChangedListener(new TextFormatter.OnTextChangedCurrencyFormatter(txtDownPymnt));
        btnCreate.setOnClickListener(view -> mViewModel.CreateNewApplication(CreditApplicationIntroduction.this));
        mViewModel = new ViewModelProvider(this).get(VMIntroductoryQuestion.class);

        mViewModel.getApplicationType().observe(this, stringArrayAdapter -> spnApplType.setAdapter(stringArrayAdapter));

        mViewModel.getCustomerType().observe(this, stringArrayAdapter -> spnCustomerType.setAdapter(stringArrayAdapter));

        mViewModel.getAllBranchNames().observe(this, strings -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, strings);
            txtBranchNm.setAdapter(adapter);
        });

        txtBranchNm.setOnItemClickListener((adapterView, view, i, l) -> mViewModel.getAllBranchInfo().observe(this, eBranchInfos -> {
            for(int x = 0; x < eBranchInfos.size(); x++){
                if(txtBranchNm.getText().toString().equalsIgnoreCase(eBranchInfos.get(x).getBranchNm())){
                    mViewModel.setBanchCde(eBranchInfos.get(x).getBranchCd());
                    break;
                }
            }
        }));

        mViewModel.getAllBrandNames().observe(this, strings -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, strings);
            txtBrandNm.setAdapter(adapter);
        });

        txtBrandNm.setOnItemClickListener((adapterView, view, i, l) -> mViewModel.getAllMcBrand().observe(this, eMcBrands -> {
            for(int x = 0; x < eMcBrands.size(); x++){
                if(txtBrandNm.getText().toString().equalsIgnoreCase(eMcBrands.get(x).getBrandNme())){
                    mViewModel.setLsBrandID(eMcBrands.get(x).getBrandIDx());
                    break;
                }
            }
        }));

        mViewModel.getBrandID().observe(this, s -> mViewModel.getAllBrandModelName(s).observe(this, strings -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, strings);
            txtModelNm.setAdapter(adapter);
            Log.e(TAG, "Array Adapter has been updated.");
        }));

        txtModelNm.setOnItemClickListener((adapterView, view, i, l) -> mViewModel.getAllBrandModelInfo().observe(this, eMcModels -> {
            for(int x = 0; x < eMcModels.size(); x++){
                if(txtModelNm.getText().toString().equalsIgnoreCase(eMcModels.get(x).getModelNme() +" "+ eMcModels.get(x).getModelCde())){
                    mViewModel.setLsModelCd(eMcModels.get(x).getModelIDx());
                    break;
                }
            }
        }));

        mViewModel.getModelID().observe(this, s -> {
            mViewModel.getDpInfo(s).observe(this, mcDPInfo -> {
                try{
                    JSONObject loJson = new JSONObject();
                    loJson.put("sModelIDx", mcDPInfo.ModelIDx);
                    loJson.put("sModelNme", mcDPInfo.ModelNme);
                    loJson.put("nRebatesx", mcDPInfo.Rebatesx);
                    loJson.put("nMiscChrg", mcDPInfo.MiscChrg);
                    loJson.put("nEndMrtgg", mcDPInfo.EndMrtgg);
                    loJson.put("nMinDownx", mcDPInfo.MinDownx);
                    loJson.put("nSelPrice", mcDPInfo.SelPrice);
                    loJson.put("nLastPrce", mcDPInfo.LastPrce);
                    mViewModel.setLoDpInfo(loJson);
                } catch (Exception e){
                    e.printStackTrace();
                }
            });

            mViewModel.getMonthlyAmortInfo(s).observe(this, mcAmortInfo -> {
                try{
                    JSONObject loJson = new JSONObject();
                    loJson.put("nSelPrice", mcAmortInfo.SelPrice);
                    loJson.put("nMinDownx", mcAmortInfo.MinDownx);
                    loJson.put("nMiscChrg", mcAmortInfo.MiscChrg);
                    loJson.put("nRebatesx", mcAmortInfo.Rebatesx);
                    loJson.put("nEndMrtgg", mcAmortInfo.EndMrtgg);
                    loJson.put("nAcctThru", mcAmortInfo.AcctThru);
                    loJson.put("nFactorRt", mcAmortInfo.FactorRt);
                    mViewModel.setLoMonthlyInfo(loJson);
                } catch (Exception e){
                    e.printStackTrace();
                }
            });
        });

        mViewModel.getDownpayment().observe(this, s -> txtDownPymnt.setText(s));

        txtDownPymnt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try{
                    mViewModel.calculateMonthlyPayment(Objects.requireNonNull(txtDownPymnt.getText()).toString().replace(",", ""));
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {            }
        });

        mViewModel.getInstallmentTerm().observe(this, stringArrayAdapter -> spnTerm.setAdapter(stringArrayAdapter));

        mViewModel.getSelectedInstallmentTerm().observe(this, integer -> mViewModel.calculateMonthlyPayment());

        spnCustomerType.setOnItemSelectedListener(new CreditApplicationIntroduction.SpinnerSelectionListener(mViewModel));

        spnTerm.setOnItemSelectedListener(new CreditApplicationIntroduction.SpinnerSelectionListener(mViewModel));

        mViewModel.getMonthlyAmort().observe(this, s -> txtAmort.setText(s));
    }

    @Override
    public void onSaveSuccessResult(String args) {
        Intent loIntent = new Intent(this, Activity_CreditApplication.class);
        loIntent.putExtra("transno", args);
        startActivity(loIntent);
    }

    @Override
    public void onFailedResult(String message) {
        GToast.CreateMessage(this, message, GToast.ERROR).show();
    }

    private static class SpinnerSelectionListener implements AdapterView.OnItemSelectedListener{
        private final VMIntroductoryQuestion vm;

        SpinnerSelectionListener(VMIntroductoryQuestion viewModel){
            this.vm = viewModel;
        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if(adapterView.getId() == R.id.spn_customerType){
                String type = "";
                switch (i){
                    case 0:
                        break;
                    case 1:
                        type = "0";
                        break;
                    case 2:
                        type = "1";
                        break;
                }
                vm.setCustomerType(type);
            }
            if(adapterView.getId() == R.id.spn_installmentTerm) {
                int term = 0;
                switch (i) {
                    case 0:
                    case 1:
                        term = 36;
                        break;
                    case 2:
                        term = 24;
                        break;
                    case 3:
                        term = 18;
                        break;
                    case 4:
                        term = 12;
                        break;
                    case 5:
                        term = 6;
                        break;
                }
                vm.setLsIntTerm(term);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            MessageBox loMessage = new MessageBox(CreditApplicationIntroduction.this);
            loMessage.setTitle("Credit Application");
            loMessage.setMessage("Exit credit online application?");
            loMessage.setPositiveButton("Yes", (view, dialog) -> {
                dialog.dismiss();
                finish();
            });
            loMessage.setNegativeButton("No", (view, dialog) -> dialog.dismiss());
            loMessage.show();
        }
        return super.onOptionsItemSelected(item);
    }
}