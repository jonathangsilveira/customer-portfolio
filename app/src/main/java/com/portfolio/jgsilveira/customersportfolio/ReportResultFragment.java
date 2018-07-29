package com.portfolio.jgsilveira.customersportfolio;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.portfolio.jgsilveira.customersportfolio.components.DeviderItemDecoration;
import com.portfolio.jgsilveira.customersportfolio.model.Customer;
import com.portfolio.jgsilveira.customersportfolio.util.DateUtil;
import com.portfolio.jgsilveira.customersportfolio.util.StringUtil;
import com.portfolio.jgsilveira.customersportfolio.viewmodel.ReportViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportResultFragment extends Fragment {

    public static final String TAG = "ReportResultFragment";

    private RecyclerView mRecyclerViewResult;

    private Button mButtonClose;

    private ReportViewModel mViewModel;

    public ReportResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ReportResultFragment.
     */
    public static ReportResultFragment newInstance() {
        ReportResultFragment fragment = new ReportResultFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = Objects.requireNonNull(getActivity());
        mViewModel = ViewModelProviders.of(activity).get(ReportViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report_result, container, false);
        initReferences(view);
        initListeners();
        setupRecyclerView();
        return view;
    }

    private void initReferences(View container) {
        mRecyclerViewResult = container.findViewById(R.id.fragment_resultado_lista);
        mButtonClose = container.findViewById(R.id.fragment_resultado_fechar);
    }

    private void initListeners() {
        mButtonClose.setOnClickListener(new OnClosePressedListener());
    }

    private void showRegister(long id) {
        Intent intent = ManterClienteActivity.newIntent(getActivity(), id);
        startActivity(intent);
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerViewResult.setLayoutManager(linearLayoutManager);
        FragmentActivity context = Objects.requireNonNull(getActivity());
        mRecyclerViewResult.addItemDecoration(
                new DeviderItemDecoration(context, LinearLayoutManager.VERTICAL, 16));
        mRecyclerViewResult.setAdapter(new ReportAdapter(mViewModel.getResults()));
    }

    private class OnClosePressedListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            FragmentActivity activity = Objects.requireNonNull(getActivity());
            activity.getSupportFragmentManager().popBackStackImmediate();
        }

    }

    private class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

        private List<Customer> mClientes = new ArrayList<>();

        ReportAdapter(List<Customer> clientes) {
            mClientes.addAll(clientes);
        }

        @NonNull
        @Override
        public ReportViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int position) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View layout = inflater.inflate(R.layout.item_report, parent, false);
            return new ReportViewHolder(layout);
        }

        @Override
        public void onBindViewHolder(@NonNull ReportViewHolder viewHolder, int position) {
            String placeholder = getString(R.string.placeholder);
            String dateMask = getString(R.string.mascara_data);
            Customer cliente = mClientes.get(position);
            viewHolder.setData(getItemId(position));
            viewHolder.mTextViewName.setText(cliente.getName());
            viewHolder.mTextViewRegisteredAt
                    .setText(DateUtil.formatDateTimeMedium(cliente.getRegisterDate()));
            viewHolder.mTextViewState.setText(cliente.getState());
            viewHolder.mTextViewDocument.setText(cliente.getDocument());
            viewHolder.mTextViewId.setText(StringUtil.getString(cliente.getDocumentoId(), placeholder));
            if (cliente.getBirthdate() == null) {
                viewHolder.mTextViewBirthdate.setText(dateMask);
            } else {
                viewHolder.mTextViewBirthdate.setText(
                        DateUtil.formatDateMedium(cliente.getBirthdate()));
            }
            viewHolder.mTextViewTelephone.setText(
                    StringUtil.getString(cliente.getTelephone(), placeholder));
        }

        @Override
        public int getItemCount() {
            return mClientes.size();
        }

        @Override
        public long getItemId(int position) {
            return mClientes.get(position).getId();
        }

        class ReportViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private TextView mTextViewName;

            private TextView mTextViewDocument;

            private TextView mTextViewId;

            private TextView mTextViewBirthdate;

            private TextView mTextViewTelephone;

            private TextView mTextViewRegisteredAt;

            private TextView mTextViewState;

            private long mId;

            ReportViewHolder(@NonNull View itemView) {
                super(itemView);
                init(itemView);
            }

            private void init(@NonNull View itemView) {
                mTextViewName = itemView.findViewById(R.id.item_report_name);
                mTextViewRegisteredAt = itemView.findViewById(R.id.item_report_registered_at);
                mTextViewState = itemView.findViewById(R.id.item_report_state);
                mTextViewDocument = itemView.findViewById(R.id.item_report_document);
                mTextViewId = itemView.findViewById(R.id.item_report_id);
                mTextViewBirthdate = itemView.findViewById(R.id.item_report_birthdate);
                mTextViewTelephone = itemView.findViewById(R.id.item_report_telephone);
                itemView.setOnClickListener(this);
            }

            private void setData(long id) {
                mId = id;
            }

            @Override
            public void onClick(View view) {

            }

        }

    }

}
