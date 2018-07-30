package com.portfolio.jgsilveira.customersportfolio;


import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.portfolio.jgsilveira.customersportfolio.model.ReportFilters;
import com.portfolio.jgsilveira.customersportfolio.settings.EnumStates;
import com.portfolio.jgsilveira.customersportfolio.util.DateUtil;
import com.portfolio.jgsilveira.customersportfolio.util.DialogUtil;
import com.portfolio.jgsilveira.customersportfolio.util.StringUtil;
import com.portfolio.jgsilveira.customersportfolio.viewmodel.ReportViewModel;

import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportFiltersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportFiltersFragment extends Fragment {

    private static final String TAG = "ReportFiltersFragment";

    private ReportViewModel mViewModel;

    private EditText mEditTextName;

    private TextView mTextViewBornedFrom;

    private ImageButton mImageButtonBornedFromCalendar;

    private TextView mTextViewRegisteredFrom;

    private ImageButton mImageButtonRegisteredFromCalendar;

    private TextView mTextViewRegisteredTo;

    private ImageButton mImageButtonRegisteredToCalendar;

    private TextView mTextViewBornedTo;

    private ImageButton mImageButtomBornedToCalendar;

    private Spinner mSpinnerState;

    private Button mButtonVisuelize;

    private EditText mEditTextDocument;

    public ReportFiltersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ReportFiltersFragment.
     */
    public static ReportFiltersFragment newInstance() {
        ReportFiltersFragment fragment = new ReportFiltersFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = Objects.requireNonNull(getActivity());
        mViewModel = ViewModelProviders.of(activity).get(ReportViewModel.class);
        mViewModel.getHasResultado().observe(this, new HasResultObserver());
        mViewModel.getLiveDataFilters().observe(this, new FiltersObserver());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_report_filters, container, false);
        initReferences(rootView);
        initListeners();
        mSpinnerState.setAdapter(newAdapter(mViewModel.getStates()));
        return rootView;
    }

    @NonNull
    private ArrayAdapter<EnumStates> newAdapter(List<EnumStates> values) {
        FragmentActivity context = Objects.requireNonNull(getActivity());
        return new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item,
                android.R.id.text1, values);
    }

    private void initReferences(View container) {
        mEditTextName = container.findViewById(R.id.fragment_filters_name);
        mEditTextDocument = container.findViewById(R.id.fragment_filters_document);
        mSpinnerState = container.findViewById(R.id.fragment_filters_state);
        mTextViewBornedFrom = container.findViewById(R.id.fragment_filters_borned_from);
        mTextViewBornedTo = container.findViewById(R.id.fragment_filters_borned_to);
        mTextViewRegisteredFrom = container.findViewById(R.id.fragment_filters_registered_from);
        mTextViewRegisteredTo = container.findViewById(R.id.fragment_filters_registered_to);
        mImageButtonBornedFromCalendar =
                container.findViewById(R.id.fragment_filters_borned_from_calendar);
        mImageButtomBornedToCalendar =
                container.findViewById(R.id.fragment_filters_borned_to_calendar);
        mImageButtonRegisteredFromCalendar =
                container.findViewById(R.id.fragment_filters_registered_from_calendar);
        mImageButtonRegisteredToCalendar =
                container.findViewById(R.id.fragment_filters_registered_to_calendar);
        mButtonVisuelize = container.findViewById(R.id.fragment_filters_visualize);
    }

    private void initListeners() {
        mEditTextName.addTextChangedListener(new NameTextWatcher());
        mEditTextDocument.addTextChangedListener(new DocumentTextWatcher());
        mSpinnerState.setOnItemSelectedListener(new OnStateSelected());
        mImageButtonBornedFromCalendar.setOnClickListener(new OnBirthdateFromCalendarClick());
        mImageButtomBornedToCalendar.setOnClickListener(new OnBirthdateToCalendarClick());
        mImageButtonRegisteredFromCalendar.setOnClickListener(new OnRegisteredFromCalendarClick());
        mImageButtonRegisteredToCalendar.setOnClickListener(new OnRegisteredToCalendarClick());
        mButtonVisuelize.setOnClickListener(new OnVisualizeClick());
    }

    private void displayFilters() {
        ReportFilters filters = mViewModel.getFilters();
        mEditTextName.setText(filters.getName());
        mEditTextDocument.setText(filters.getDocument());
        mSpinnerState.setSelection(mViewModel.getStateSelectedPosition());
        if (filters.getBornedFrom() == null) {
            mTextViewBornedFrom.setText(StringUtil.VAZIO);
        } else {
            mTextViewBornedFrom.setText(DateUtil.formatDateMedium(filters.getBornedFrom()));
        }
        if (filters.getBornedTo() == null) {
            mTextViewBornedTo.setText(StringUtil.VAZIO);
        } else {
            mTextViewBornedTo.setText(DateUtil.formatDateMedium(filters.getBornedTo()));
        }
        if (filters.getStartDate() == null) {
            mTextViewRegisteredFrom.setText(StringUtil.VAZIO);
        } else {
            mTextViewRegisteredFrom.setText(DateUtil.formatDateMedium(filters.getStartDate()));
        }
        if (filters.getEndDate() == null) {
            mTextViewRegisteredTo.setText(StringUtil.VAZIO);
        } else {
            mTextViewRegisteredTo.setText(DateUtil.formatDateMedium(filters.getEndDate()));
        }
    }

    private void showBirthdateFromCalendar() {
        ReportFilters filters = mViewModel.getFilters();
        Date birthdate;
        if (filters.getBornedFrom() == null) {
            birthdate = new Date();
        } else {
            birthdate = filters.getBornedFrom();
        }
        DialogUtil.showDatePickerDialog(getActivity(), birthdate,
                new OnBirthdateFromSetListener(), new OnClearBirthdateFromListener());
    }

    private void showBirthdateToCalendar() {
        ReportFilters filters = mViewModel.getFilters();
        Date birthdate;
        if (filters.getBornedTo() == null) {
            birthdate = new Date();
        } else {
            birthdate = filters.getBornedTo();
        }
        DialogUtil.showDatePickerDialog(getActivity(), birthdate,
                new OnBirthdateToSetListener(), new OnClearBirthdateToListener());
    }

    private void showRegisteredFromCalendar() {
        ReportFilters filters = mViewModel.getFilters();
        Date startDate;
        if (filters.getStartDate() == null) {
            startDate = new Date();
        } else {
            startDate = filters.getStartDate();
        }
        DialogUtil.showDatePickerDialog(getActivity(), startDate,
                new OnRegisteredFromDateSetListener(), new OnClearRegisteredFromDateListener());
    }

    private void showRegisteredToCalendar() {
        ReportFilters filters = mViewModel.getFilters();
        Date endDate;
        if (filters.getEndDate() == null) {
            endDate = new Date();
        } else {
            endDate = filters.getEndDate();
        }
        DialogUtil.showDatePickerDialog(getActivity(), endDate,
                new OnRegisteredToDateSetListener(), new OnClearRegisteredToDateListener());
    }

    private class OnBirthdateFromSetListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
            ReportFilters filtro = mViewModel.getFilters();
            Date date = DateUtil.createDate(year, month, dayOfMonth);
            filtro.setBornedFrom(date);
            mTextViewBornedFrom.setText(DateUtil.formatDateMedium(filtro.getBornedFrom()));
        }

    }

    private class OnBirthdateToSetListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
            ReportFilters filters = mViewModel.getFilters();
            Date date = DateUtil.createDate(year, month, dayOfMonth);
            filters.setBornedTo(date);
            mTextViewBornedTo.setText(DateUtil.formatDateMedium(filters.getBornedTo()));
        }

    }

    private class OnRegisteredFromDateSetListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
            ReportFilters filtro = mViewModel.getFilters();
            Date date = DateUtil.createDate(year, month, dayOfMonth);
            filtro.setStartDate(date);
            mTextViewRegisteredFrom.setText(DateUtil.formatDateMedium(filtro.getStartDate()));
        }

    }

    private class OnRegisteredToDateSetListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
            ReportFilters filtro = mViewModel.getFilters();
            Date date = DateUtil.createDate(year, month, dayOfMonth, 23, 59, 59);
            filtro.setEndDate(date);
            mTextViewRegisteredTo.setText(DateUtil.formatDateMedium(filtro.getEndDate()));
        }

    }

    private class OnClearBirthdateFromListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            mTextViewBornedFrom.setText(StringUtil.VAZIO);
            ReportFilters filtro = mViewModel.getFilters();
            filtro.setBornedFrom(null);
            dialogInterface.dismiss();
        }

    }

    private class OnClearBirthdateToListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            mTextViewBornedTo.setText(StringUtil.VAZIO);
            ReportFilters filtro = mViewModel.getFilters();
            filtro.setBornedTo(null);
            dialogInterface.dismiss();
        }

    }

    private class OnClearRegisteredFromDateListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            mTextViewRegisteredFrom.setText(StringUtil.VAZIO);
            ReportFilters filtro = mViewModel.getFilters();
            filtro.setStartDate(null);
            dialogInterface.dismiss();
        }

    }

    private class OnClearRegisteredToDateListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            mTextViewRegisteredTo.setText(StringUtil.VAZIO);
            ReportFilters filtro = mViewModel.getFilters();
            filtro.setEndDate(null);
            dialogInterface.dismiss();
        }

    }

    private class OnBirthdateFromCalendarClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            showBirthdateFromCalendar();
        }

    }

    private class OnBirthdateToCalendarClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            showBirthdateToCalendar();
        }

    }

    private class OnRegisteredFromCalendarClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            showRegisteredFromCalendar();
        }

    }

    private class OnRegisteredToCalendarClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            showRegisteredToCalendar();
        }

    }

    private class OnVisualizeClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            ReportResultFragment fragment = ReportResultFragment.newInstance();
            FragmentActivity activity = Objects.requireNonNull(getActivity());
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.activity_relatorio_container, fragment)
                    .addToBackStack(TAG)
                    .commit();
        }

    }

    private class NameTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence text, int i, int i1, int i2) {
            ReportFilters filter = mViewModel.getFilters();
            filter.setName(text.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }

    }

    private class DocumentTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence text, int i, int i1, int i2) {
            ReportFilters filter = mViewModel.getFilters();
            filter.setDocument(text.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }

    }

    private class OnStateSelected implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
            mViewModel.selectState(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

    }

    private class HasResultObserver implements Observer<Boolean> {

        @Override
        public void onChanged(@Nullable Boolean aBoolean) {
            boolean hasResult = aBoolean != null && aBoolean;
            mButtonVisuelize.setEnabled(hasResult);
        }

    }

    private class FiltersObserver implements Observer<ReportFilters> {

        @Override
        public void onChanged(@Nullable ReportFilters filters) {
            boolean nonNull = filters != null;
            if (nonNull) {
                displayFilters();
            }
        }

    }

}
