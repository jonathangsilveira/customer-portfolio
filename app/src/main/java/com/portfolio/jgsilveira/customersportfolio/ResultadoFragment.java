package com.portfolio.jgsilveira.customersportfolio;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.portfolio.jgsilveira.customersportfolio.model.Cliente;
import com.portfolio.jgsilveira.customersportfolio.util.DateUtil;
import com.portfolio.jgsilveira.customersportfolio.viewmodel.RelatorioViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResultadoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultadoFragment extends Fragment {

    private RecyclerView mRecyclerViewResultado;

    private Button mButtonFechar;

    private RelatorioViewModel mViewModel;

    public ResultadoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ResultadoFragment.
     */
    public static ResultadoFragment newInstance() {
        ResultadoFragment fragment = new ResultadoFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(RelatorioViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_resultado, container, false);
        initReferences(view);
        initListeners();
        setupRecyclerView();
        return view;
    }

    private void initReferences(View container) {
        mRecyclerViewResultado = container.findViewById(R.id.fragment_resultado_lista);
        mButtonFechar = container.findViewById(R.id.fragment_resultado_fechar);
    }

    private void initListeners() {
        mButtonFechar.setOnClickListener(new OnClosePressedListener());
    }

    private void showCadastro(long id) {
        Intent intent = ManterClienteActivity.newIntent(getActivity(), id);
        startActivity(intent);
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerViewResultado.setLayoutManager(linearLayoutManager);
        List<Cliente> clientes = mViewModel.getResultado().getValue();
        mRecyclerViewResultado.setAdapter(new RelatorioAdapter(clientes));
    }

    private class OnClosePressedListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

        }

    }

    private class RelatorioAdapter extends RecyclerView.Adapter<RelatorioAdapter.RelatorioViewHolder> {

        private List<Cliente> mClientes = new ArrayList<>();

        RelatorioAdapter(List<Cliente> clientes) {
            mClientes.addAll(clientes);
        }

        @NonNull
        @Override
        public RelatorioViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int position) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View layout = inflater.inflate(R.layout.item_relatorio, parent, false);
            RelatorioViewHolder viewHolder = new RelatorioViewHolder(layout);
            viewHolder.setData(getItemId(position));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RelatorioViewHolder viewHolder, int position) {
            Cliente cliente = mClientes.get(position);
            viewHolder.mTextViewNome.setText(cliente.getNome());
            viewHolder.mTextViewCadastrado
                    .setText(DateUtil.formatarData(cliente.getDataHoraCadastro()));
            viewHolder.mTextViewUf.setText(cliente.getUf());
        }

        @Override
        public int getItemCount() {
            return mClientes.size();
        }

        @Override
        public long getItemId(int position) {
            return mClientes.get(position).getId();
        }

        class RelatorioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private TextView mTextViewNome;

            private TextView mTextViewCadastrado;

            private TextView mTextViewUf;

            private long mId;

            RelatorioViewHolder(@NonNull View itemView) {
                super(itemView);
                init(itemView);
            }

            private void init(@NonNull View itemView) {
                mTextViewNome = itemView.findViewById(R.id.item_relatorio_nome);
                mTextViewCadastrado = itemView.findViewById(R.id.item_relatorio_cadastrado);
                mTextViewUf = itemView.findViewById(R.id.item_relatorio_uf);
                itemView.setOnClickListener(this);
            }

            private void setData(long id) {
                mId = id;
            }

            @Override
            public void onClick(View view) {
                showCadastro(mId);
            }

        }

    }

}
