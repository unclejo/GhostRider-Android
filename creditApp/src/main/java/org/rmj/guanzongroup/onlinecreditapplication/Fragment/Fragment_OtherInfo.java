package org.rmj.guanzongroup.onlinecreditapplication.Fragment;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.rmj.g3appdriver.GRider.Etc.GToast;
import org.rmj.guanzongroup.onlinecreditapplication.Activity.Activity_CreditApplication;
import org.rmj.guanzongroup.onlinecreditapplication.Adapter.PersonalReferencesAdapter;
import org.rmj.guanzongroup.onlinecreditapplication.Model.OtherInfoModel;
import org.rmj.guanzongroup.onlinecreditapplication.Model.PersonalReferenceInfoModel;
import org.rmj.guanzongroup.onlinecreditapplication.Model.ViewModelCallBack;
import org.rmj.guanzongroup.onlinecreditapplication.R;
import org.rmj.guanzongroup.onlinecreditapplication.ViewModel.VMOtherInfo;

import java.util.Objects;

public class Fragment_OtherInfo extends Fragment implements ViewModelCallBack {

    private static final String TAG = Fragment_OtherInfo.class.getSimpleName();
    private VMOtherInfo mViewModel;

    private String TownID = "";

    private PersonalReferencesAdapter adapter;
    private OtherInfoModel otherInfo;
    private AutoCompleteTextView spnUnitUser;
    private AutoCompleteTextView spnUnitPrps;
    private AutoCompleteTextView spnUnitPayr;
    private AutoCompleteTextView spnSourcexx;
    private AutoCompleteTextView spnUserBuyr;
    private AutoCompleteTextView spnPayrBuyr;

    private TextInputEditText tieSpcfSrc;
    private TextInputEditText tieRefAdd1;
    private TextInputEditText tieRefName;
    private TextInputEditText tieRefCntc;
    private AutoCompleteTextView tieAddProv;
    private AutoCompleteTextView tieAddTown;
    private RecyclerView recyclerView;
    private MaterialButton btnAddReferencex;
    private Button btnPrevs;
    private Button btnNext;

    public Fragment_OtherInfo() {
        // Required empty public constructor
    }

    public static Fragment_OtherInfo newInstance() {
        return new Fragment_OtherInfo();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_other_info, container, false);
        otherInfo = new OtherInfoModel();
        setupWidgets(v);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(VMOtherInfo.class);
        mViewModel.setTransNox(Activity_CreditApplication.getInstance().getTransNox());
        mViewModel.getCreditApplicationInfo().observe(getViewLifecycleOwner(), eCreditApplicantInfo -> mViewModel.setCreditApplicantInfo(eCreditApplicantInfo));
        mViewModel.getReferenceList().observe(getViewLifecycleOwner(), personalReferenceInfoModels -> {
            adapter = new PersonalReferencesAdapter(personalReferenceInfoModels);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapter);
        });
        spnUnitUser.setAdapter(mViewModel.getUnitUser());
        spnUnitPrps.setAdapter(mViewModel.getUnitPurpose());
        spnSourcexx.setAdapter(mViewModel.getIntCompanyInfoSource());
        spnUserBuyr.setAdapter(mViewModel.getUnitPayer());
        spnPayrBuyr.setAdapter(mViewModel.getPayerBuyer());

        spnUnitUser.setOnItemClickListener(new Fragment_OtherInfo.SpinnerSelectionListener(spnUnitUser));
        spnUnitPayr.setOnItemClickListener(new Fragment_OtherInfo.SpinnerSelectionListener(spnUnitPayr));
        spnSourcexx.setOnItemClickListener(new Fragment_OtherInfo.SpinnerSelectionListener(spnSourcexx));

        spnUnitPrps.setOnItemClickListener(new Fragment_OtherInfo.SpinnerSelectionListener(spnUnitPrps));
        spnUserBuyr.setOnItemClickListener(new Fragment_OtherInfo.SpinnerSelectionListener(spnUserBuyr));
        spnPayrBuyr.setOnItemClickListener(new Fragment_OtherInfo.SpinnerSelectionListener(spnPayrBuyr));
        mViewModel.getProvinceNameList().observe(getViewLifecycleOwner(), strings -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, strings);
            tieAddProv.setAdapter(adapter);
        });

        tieAddProv.setOnItemClickListener((adapterView, view, i, l) -> mViewModel.getProvinceInfoList().observe(getViewLifecycleOwner(), provinceInfos -> {
            for(int x = 0; x < provinceInfos.size(); x++){
                if(tieAddProv.getText().toString().equalsIgnoreCase(provinceInfos.get(x).getProvName())){
                    mViewModel.setProvID(provinceInfos.get(x).getProvIDxx());
                    break;
                }
            }

            mViewModel.getAllTownNames().observe(getViewLifecycleOwner(), strings -> {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, strings);
                tieAddTown.setAdapter(adapter);
            });
        }));

        tieAddTown.setOnItemClickListener((adapterView, view, i, l) -> mViewModel.getTownInfoList().observe(getViewLifecycleOwner(), townInfoList -> {
            for(int x = 0; x < townInfoList.size(); x++){
                if(tieAddTown.getText().toString().equalsIgnoreCase(townInfoList.get(x).getTownName())){
                    TownID =  townInfoList.get(x).getTownIDxx();
                    break;
                }
            }
        }));
    }

    private void setupWidgets(View view){
        spnUnitUser = view.findViewById(R.id.spinner_cap_unitUser);
        spnUnitPrps = view.findViewById(R.id.spinner_cap_purposeOfBuying);
        spnUnitPayr = view.findViewById(R.id.spinner_cap_monthlyPayer);
        spnUserBuyr = view.findViewById(R.id.spinner_cap_sUsr2buyr);
        spnPayrBuyr = view.findViewById(R.id.spinner_cap_sPyr2Buyr);
        spnSourcexx = view.findViewById(R.id.spinner_cap_source);
        tieSpcfSrc = view.findViewById(R.id.tie_cap_CompanyInfoSource);
        tieRefName = view.findViewById(R.id.tie_cap_referenceName);
        tieRefCntc = view.findViewById(R.id.tie_cap_refereceContact);
        tieRefAdd1 = view.findViewById(R.id.tie_cap_refereceAddress);
        tieAddProv = view.findViewById(R.id.tie_cap_referenceAddProv);
        tieAddTown = view.findViewById(R.id.tie_cap_referenceAddTown);
        recyclerView = view.findViewById(R.id.recyclerview_references);
        btnAddReferencex = view.findViewById(R.id.btn_fragment_others_addReference);

        btnNext = view.findViewById(R.id.btn_creditAppNext);
        btnPrevs = view.findViewById(R.id.btn_creditAppPrvs);

        btnPrevs.setOnClickListener(v -> Activity_CreditApplication.getInstance().moveToPageNumber(13));
        btnAddReferencex.setOnClickListener(v -> {
            addReference();
            adapter.notifyDataSetChanged();
        });
        btnNext.setOnClickListener(v -> {
            otherInfo.setCompanyInfoSource(Objects.requireNonNull(tieSpcfSrc.getText()).toString());
            mViewModel.SubmitOtherInfo(otherInfo, Fragment_OtherInfo.this);
        });
    }

    private void addReference(){
        try {
            String refName = (Objects.requireNonNull(tieRefName.getText()).toString());
            String refContact = (Objects.requireNonNull(tieRefCntc.getText()).toString());
            String refAddress = (Objects.requireNonNull(tieRefAdd1.getText()).toString());
            PersonalReferenceInfoModel poRefInfo = new PersonalReferenceInfoModel(refName, refAddress, TownID, refContact);
            mViewModel.addReference(poRefInfo, new VMOtherInfo.AddPersonalInfoListener() {
                @Override
                public void OnSuccess() {
                    tieRefName.setText("");
                    tieRefCntc.setText("");
                    tieRefAdd1.setText("");
                    tieAddProv.setText("");
                    tieAddTown.setText("");
                    TownID = "";
                }

                @Override
                public void onFailed(String message) {
                    GToast.CreateMessage(getContext(), message, GToast.ERROR).show();
                }
            });
        }catch (NullPointerException e){
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveSuccessResult(String args) {
        Activity_CreditApplication.getInstance().moveToPageNumber(16);
    }

    @Override
    public void onFailedResult(String message) {
        GToast.CreateMessage(getActivity(), message, GToast.ERROR).show();
    }

    class SpinnerSelectionListener implements AdapterView.OnItemClickListener{
        private final AutoCompleteTextView poView;
        SpinnerSelectionListener(AutoCompleteTextView view){
            this.poView = view;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            if(spnUnitUser.equals(poView)){
                otherInfo.setUnitUser(String.valueOf(i));
            }
            if(spnUnitPayr.equals(poView)){
                otherInfo.setUnitPayr(String.valueOf(i));
            }
            if(spnSourcexx.equals(poView)){
                otherInfo.setSource(String.valueOf(i));
            }
            if(spnUnitPrps.equals(poView)){
                otherInfo.setUnitPrps(String.valueOf(i));
            }
            if(spnUserBuyr.equals(poView)){
                otherInfo.setPayrRltn(String.valueOf(i));
            }
            if(spnPayrBuyr.equals(poView)){
                otherInfo.setUnitUser(String.valueOf(i));
            }
        }
    }
}