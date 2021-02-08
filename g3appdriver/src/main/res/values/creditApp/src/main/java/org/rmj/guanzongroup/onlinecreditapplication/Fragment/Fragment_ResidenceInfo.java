package org.rmj.guanzongroup.onlinecreditapplication.Fragment;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.rmj.g3appdriver.GRider.Etc.GToast;
import org.rmj.guanzongroup.onlinecreditapplication.Activity.Activity_CreditApplication;
import org.rmj.guanzongroup.onlinecreditapplication.Model.ResidenceInfoModel;
import org.rmj.guanzongroup.onlinecreditapplication.Model.ViewModelCallBack;
import org.rmj.guanzongroup.onlinecreditapplication.R;
import org.rmj.guanzongroup.onlinecreditapplication.ViewModel.VMResidenceInfo;

import java.util.Objects;

public class Fragment_ResidenceInfo extends Fragment implements ViewModelCallBack {

    private VMResidenceInfo mViewModel;
    private ResidenceInfoModel infoModel;
    private TextInputEditText txtLandMark,
            txtHouseNox,
            txtAddress1,
            txtAddress2,
            txtRelationship,
            txtLgnthStay,
            txtMonthlyExp,
            txtPLandMark,
            txtPHouseNox,
            txtPAddress1,
            txtPAddress2;
    private AutoCompleteTextView txtBarangay,
            txtMunicipality,
            txtProvince,
            txtPBarangay,
            txtPMunicipl,
            txtPProvince;
    private CheckBox cbOneAddress;
    private Spinner spnLgnthStay,
            spnHouseHold,
            spnHouseType;
    private TextInputLayout tilRelationship;
    private LinearLayout lnOtherInfo, lnPermaAddx;
    private Button btnNext;
    private Button btnPrvs;

    private String TransNox = "";

    public static Fragment_ResidenceInfo newInstance() {
        return new Fragment_ResidenceInfo();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_residence_info, container, false);
        infoModel = new ResidenceInfoModel();
        TransNox = Activity_CreditApplication.getInstance().getTransNox();
        initWidgets(view);
        return view;
    }

    private void initWidgets(View v){
        cbOneAddress = v.findViewById(R.id.cb_oneAddress);
        txtLandMark = v.findViewById(R.id.txt_landmark);
        txtHouseNox = v.findViewById(R.id.txt_houseNox);
        txtAddress1 = v.findViewById(R.id.txt_address);
        txtAddress2 = v.findViewById(R.id.txt_address2);
        txtBarangay = v.findViewById(R.id.txt_barangay);
        txtMunicipality = v.findViewById(R.id.txt_town);
        txtProvince = v.findViewById(R.id.txt_province);
        txtRelationship = v.findViewById(R.id.txt_relationship);
        txtLgnthStay = v.findViewById(R.id.txt_lenghtStay);
        txtMonthlyExp = v.findViewById(R.id.txt_monthlyExp);
        txtPLandMark = v.findViewById(R.id.txt_perm_landmark);
        txtPHouseNox = v.findViewById(R.id.txt_perm_houseNox);
        txtPAddress1 = v.findViewById(R.id.txt_perm_address);
        txtPAddress2 = v.findViewById(R.id.txt_perm_address2);
        txtPBarangay = v.findViewById(R.id.txt_perm_barangay);
        txtPMunicipl = v.findViewById(R.id.txt_perm_town);
        txtPProvince = v.findViewById(R.id.txt_perm_province);

        spnLgnthStay = v.findViewById(R.id.spn_lenghtStay);
        spnHouseHold = v.findViewById(R.id.spn_houseHold);
        spnHouseType = v.findViewById(R.id.spn_houseType);
        RadioGroup rgOwnsership = v.findViewById(R.id.rg_ownership);
        RadioGroup rgGarage = v.findViewById(R.id.rg_garage);
        tilRelationship = v.findViewById(R.id.til_relationship);
        lnOtherInfo = v.findViewById(R.id.linear_otherInfo);
        lnPermaAddx = v.findViewById(R.id.linear_permanentAdd);

        cbOneAddress.setOnCheckedChangeListener(new OnAddressSetListener());
        rgOwnsership.setOnCheckedChangeListener(new OnHouseOwnershipSelectListener());
        rgGarage.setOnCheckedChangeListener(new OnHouseOwnershipSelectListener());
        btnNext = v.findViewById(R.id.btn_creditAppNext);
        btnPrvs = v.findViewById(R.id.btn_creditAppPrvs);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VMResidenceInfo.class);
        mViewModel.setTransNox(TransNox);
        mViewModel.getCreditApplicationInfo().observe(getViewLifecycleOwner(), eCreditApplicantInfo -> mViewModel.setGOCasDetailInfo(eCreditApplicantInfo.getDetlInfo()));

        mViewModel.getProvinceNameList().observe(getViewLifecycleOwner(), strings -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, strings);
            txtProvince.setAdapter(adapter);
            txtPProvince.setAdapter(adapter);
        });

        txtProvince.setOnItemClickListener((adapterView, view, i, l) -> mViewModel.getProvinceInfoList().observe(getViewLifecycleOwner(), eProvinceInfos -> {
            for(int x = 0; x < eProvinceInfos.size(); x++){
                if(txtProvince.getText().toString().equalsIgnoreCase(eProvinceInfos.get(x).getProvName())){
                    mViewModel.setProvinceID(eProvinceInfos.get(x).getProvIDxx());
                    break;
                }
            }
            mViewModel.getTownNameList().observe(getViewLifecycleOwner(), strings -> {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, strings);
                txtMunicipality.setAdapter(adapter);
            });
        }));

        txtMunicipality.setOnItemClickListener((adapterView, view, i, l) -> mViewModel.getTownInfoList().observe(getViewLifecycleOwner(), eTownInfos -> {
            for(int x = 0; x < eTownInfos.size(); x++){
                if(txtMunicipality.getText().toString().equalsIgnoreCase(eTownInfos.get(x).getTownName())){
                    mViewModel.setTownID(eTownInfos.get(x).getTownIDxx());
                    break;
                }
            }

            mViewModel.getBarangayNameList().observe(getViewLifecycleOwner(), strings -> {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, strings);
                txtBarangay.setAdapter(adapter);
            });
        }));

        txtBarangay.setOnItemClickListener((adapterView, view, i, l) -> mViewModel.getBarangayInfoList().observe(getViewLifecycleOwner(), eBarangayInfos -> {
            for(int x = 0; x < eBarangayInfos.size(); x++){
                if(txtBarangay.getText().toString().equalsIgnoreCase(eBarangayInfos.get(x).getBrgyName())){
                    mViewModel.setBarangayID(eBarangayInfos.get(x).getBrgyIDxx());
                    break;
                }
            }
        }));

        txtPProvince.setOnItemClickListener((adapterView, view, i, l) -> mViewModel.getProvinceInfoList().observe(getViewLifecycleOwner(), eProvinceInfos -> {
            for(int x = 0; x < eProvinceInfos.size(); x++){
                if(txtPProvince.getText().toString().equalsIgnoreCase(eProvinceInfos.get(x).getProvName())){
                    mViewModel.setPermanentProvinceID(eProvinceInfos.get(x).getProvIDxx());
                    break;
                }
            }

            mViewModel.getPermanentTownNameList().observe(getViewLifecycleOwner(), strings -> {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, strings);
                txtPMunicipl.setAdapter(adapter);
            });
        }));

        txtPMunicipl.setOnItemClickListener((adapterView, view, i, l) -> mViewModel.getPermanentTownInfoList().observe(getViewLifecycleOwner(), eTownInfos -> {
            for(int x = 0; x < eTownInfos.size(); x++){
                if(txtPMunicipl.getText().toString().equalsIgnoreCase(eTownInfos.get(x).getTownName())){
                    mViewModel.setPermanentTownID(eTownInfos.get(x).getTownIDxx());
                    break;
                }
            }

            mViewModel.getPermanentBarangayNameList().observe(getViewLifecycleOwner(), strings -> {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, strings);
                txtPBarangay.setAdapter(adapter);
            });
        }));

        txtPBarangay.setOnItemClickListener((adapterView, view, i, l) -> mViewModel.getPermanentBarangayInfoList().observe(getViewLifecycleOwner(), eBarangayInfos -> {
            for(int x = 0; x < eBarangayInfos.size(); x++){
                if(txtPBarangay.getText().toString().equalsIgnoreCase(eBarangayInfos.get(x).getBrgyName())){
                    mViewModel.setPermanentBarangayID(eBarangayInfos.get(x).getBrgyIDxx());
                    break;
                }
            }
        }));

        mViewModel.getHouseHolds().observe(getViewLifecycleOwner(), stringArrayAdapter -> spnHouseHold.setAdapter(stringArrayAdapter));

        mViewModel.getHouseType().observe(getViewLifecycleOwner(), stringArrayAdapter -> spnHouseType.setAdapter(stringArrayAdapter));

        mViewModel.getLenghtOfStay().observe(getViewLifecycleOwner(), stringArrayAdapter -> spnLgnthStay.setAdapter(stringArrayAdapter));

        btnNext.setOnClickListener(view -> SaveResidenceInfo());
        btnPrvs.setOnClickListener(view -> Activity_CreditApplication.getInstance().moveToPageNumber(0));
    }

    private void SaveResidenceInfo(){
        infoModel.setOneAddress(cbOneAddress.isChecked());
        infoModel.setLandMark(Objects.requireNonNull(txtLandMark.getText()).toString());
        infoModel.setHouseNox(Objects.requireNonNull(txtHouseNox.getText()).toString());
        infoModel.setAddress1(Objects.requireNonNull(txtAddress1.getText()).toString());
        infoModel.setAddress2(Objects.requireNonNull(txtAddress2.getText()).toString());
        infoModel.setProvinceNm(txtProvince.getText().toString());
        infoModel.setMunicipalNm(txtMunicipality.getText().toString());
        infoModel.setBarangayName(txtBarangay.getText().toString());
        infoModel.setHouseHold(String.valueOf(spnHouseHold.getSelectedItemPosition() - 1));
        infoModel.setHouseType(String.valueOf(spnHouseType.getSelectedItemPosition() - 1));
        infoModel.setOwnerRelation(Objects.requireNonNull(txtRelationship.getText()).toString());
        infoModel.setLenghtOfStay(Objects.requireNonNull(txtLgnthStay.getText()).toString());
        infoModel.setMonthlyExpenses(Objects.requireNonNull(txtMonthlyExp.getText()).toString());
        infoModel.setIsYear(String.valueOf(spnLgnthStay.getSelectedItemPosition() - 1));
        infoModel.setPermanentLandMark(Objects.requireNonNull(txtPLandMark.getText()).toString());
        infoModel.setPermanentHouseNo(Objects.requireNonNull(txtPHouseNox.getText()).toString());
        infoModel.setPermanentAddress1(Objects.requireNonNull(txtPAddress1.getText()).toString());
        infoModel.setPermanentAddress2(Objects.requireNonNull(txtPAddress2.getText()).toString());
        infoModel.setPermanentProvinceNm(txtPProvince.getText().toString());
        infoModel.setPermanentMunicipalNm(txtPMunicipl.getText().toString());
        infoModel.setPermanentBarangayName(txtPBarangay.getText().toString());
        mViewModel.SaveResidenceInfo(infoModel, Fragment_ResidenceInfo.this);
    }

    @Override
    public void onSaveSuccessResult(String args) {
        Activity_CreditApplication.getInstance().moveToPageNumber(2);
    }

    @Override
    public void onFailedResult(String message) {
        GToast.CreateMessage(getActivity(), message, GToast.ERROR).show();
    }

    private class OnHouseOwnershipSelectListener implements RadioGroup.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            if(radioGroup.getId() == R.id.rg_ownership){
                if(i == R.id.rb_owned){
                    lnOtherInfo.setVisibility(View.GONE);
                    infoModel.setHouseOwn("0");
                }
                if(i == R.id.rb_rent){
                    lnOtherInfo.setVisibility(View.VISIBLE);
                    tilRelationship.setVisibility(View.GONE);
                    infoModel.setHouseOwn("1");
                }
                if(i == R.id.rb_careTaker){
                    lnOtherInfo.setVisibility(View.VISIBLE);
                    tilRelationship.setVisibility(View.VISIBLE);
                    infoModel.setHouseOwn("2");
                }
            } else {
                if(i == R.id.rb_yes){
                    infoModel.setHasGarage("1");
                }
                if(i == R.id.rb_no){
                    infoModel.setHasGarage("0");
                }
            }
        }
    }

    private class OnAddressSetListener implements CompoundButton.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if(b){
                lnPermaAddx.setVisibility(View.GONE);
            } else {
                lnPermaAddx.setVisibility(View.VISIBLE);
            }
        }
    }
}