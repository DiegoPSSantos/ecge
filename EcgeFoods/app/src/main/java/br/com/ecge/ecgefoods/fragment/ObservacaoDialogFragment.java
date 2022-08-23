package br.com.ecge.ecgefoods.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.annimon.stream.Stream;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import br.com.ecge.ecgefoods.R;
import br.com.ecge.ecgefoods.adapter.ObservacaoAdapter;
import br.com.ecge.ecgefoods.domain.Observacao;
import br.com.ecge.ecgefoods.domain.Pedido;
import br.com.ecge.ecgefoods.domain.Produto;
import br.com.ecge.ecgefoods.service.ProdutoService;
import br.com.ecge.ecgefoods.utils.InfoUtils;
import br.com.ecge.ecgefoods.utils.StringUtils;

public class ObservacaoDialogFragment extends DialogFragment {

    private static ObservacaoDialogFragment dialog;
    private List<Observacao> observacoes;
    private List<Observacao> obsSelecionadas;
    private RecyclerView rvObservacoes;
    private SwipeRefreshLayout srObservacoes;
    private EditText novaObservacao;
    private Pedido pedido;
    private Produto produto;
    private Button adicionar;
    private Button cancelar;
    private static String observacoesConcatenadas;
    private static final String CHAVE_NOVA_OBSERVACAO = "nova_observacao";
    public static final String TAG = "ObservacaoDialogFragment";

    @SuppressLint("ValidFragment")
    private ObservacaoDialogFragment() {
    }

    public static ObservacaoDialogFragment newInstance() {
        observacoesConcatenadas = StringUtils.VAZIO;
        if (dialog == null)
            dialog = new ObservacaoDialogFragment();
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        obsSelecionadas = new ArrayList<>();
        if (getArguments() != null) {
            produto = Parcels.unwrap(getArguments().getParcelable("produto"));
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity(), R.style.AlertStyle);
        View body = getActivity().getLayoutInflater().inflate(R.layout.dialog_observacao, null);
        View head = getActivity().getLayoutInflater().inflate(R.layout.head_dialog_observacao, null);
        findViewsById(body);
        setRvObservacoes();
        setSrObservacoes();
        setAdicionar();
        setCancelar();
        builder.setView(body);
        builder.setCustomTitle(head);
        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.borda_arredondada);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        taskObservacoes();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    interface ObservacaoListerner {
        void setObservacao(String observacao);
    }

    private void findViewsById(View view) {
        rvObservacoes = view.findViewById(R.id.recyclerViewObservacao);
        srObservacoes = view.findViewById(R.id.swipeToRefreshObservacao);
        novaObservacao = view.findViewById(R.id.edtObservacoesProduto);
        adicionar = view.findViewById(R.id.btnAdicionarObservacoes);
        cancelar = view.findViewById(R.id.btnCancelarObservacao);
    }

    private void setRvObservacoes() {
        rvObservacoes.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvObservacoes.setItemAnimator(new DefaultItemAnimator());
        rvObservacoes.setHasFixedSize(true);
        rvObservacoes.setNestedScrollingEnabled(false);
    }

    private void setSrObservacoes() {
        srObservacoes.setOnRefreshListener(onRefreshListener());
        srObservacoes.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);
    }

    private void setAdicionar() {
        adicionar.setOnClickListener(v -> {
            ProdutoDialogFragment fragment = (ProdutoDialogFragment) getActivity().getSupportFragmentManager().findFragmentByTag(ProdutoDialogFragment.TAG);
            fragment.setObservacao(getObservacoes());
            this.dismiss();
        });
    }

    private String getObservacoes() {
        StringBuilder observacoes = new StringBuilder();
        Stream.of(obsSelecionadas).forEach(observacao -> observacoes.append(observacao.getDescricao()).append(StringUtils.PONTO_VIRGULA));
        if (novaObservacao != null && novaObservacao.getText() != null && !novaObservacao.getText().toString().isEmpty()) {
            observacoes.append(novaObservacao.getText().toString());
        }
        return observacoes.toString();
    }

    private void setCancelar() {
        cancelar.setOnClickListener(v -> {
            ProdutoDialogFragment fragment = (ProdutoDialogFragment) getActivity().getSupportFragmentManager().findFragmentByTag(ProdutoDialogFragment.TAG);
            fragment.setObservacao(StringUtils.VAZIO);
            this.dismiss();
        });
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener() {
        return () -> taskObservacoes();
    }

    private void taskObservacoes() {
        startTask("observacoes", new GetObservacoesTask(), R.id.swipeToRefreshObservacao);
    }

    private ObservacaoAdapter.ObservacaoOnClickListener onClickListener() {
        return (view, idx) -> {
            Observacao obsSelecionada = observacoes.get(idx);
            if (((CheckBox)view).isChecked() && !obsSelecionadas.contains(obsSelecionada)) {
                obsSelecionadas.add(obsSelecionada);
            } else if (!((CheckBox)view).isChecked() && obsSelecionadas.contains(obsSelecionada)) {
                obsSelecionadas.remove(obsSelecionada);
            }
        };
    }

    private class GetObservacoesTask implements TaskListener<List<Observacao>> {

        @Override
        public List<Observacao> execute() throws Exception {
            observacoes = ProdutoService.getObservacoes(produto, getContext());
            return observacoes;
        }

        @Override
        public void updateView(List<Observacao> observacoes) {
            if (observacoes != null && !observacoes.isEmpty()) {
                rvObservacoes.setAdapter(new ObservacaoAdapter(getContext(), observacoes, onClickListener()));
                srObservacoes.setRefreshing(false);
            } else {
                InfoUtils.longToast(getContext(), getContext().getString(R.string.produto_sem_observacoes));
            }
        }

        @Override
        public void onError(Exception exception) {

        }

        @Override
        public void onCancelled(String cod) {

        }
    }
}
