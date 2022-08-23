package br.com.ecge.ecgefoods.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.annimon.stream.Stream;

import org.parceler.Parcels;

import java.math.BigDecimal;
import java.util.List;

import br.com.ecge.ecgefoods.R;
import br.com.ecge.ecgefoods.activity.PedidoActivity;
import br.com.ecge.ecgefoods.adapter.ProdutoAdapter;
import br.com.ecge.ecgefoods.domain.Categoria;
import br.com.ecge.ecgefoods.domain.Mesa;
import br.com.ecge.ecgefoods.domain.Pedido;
import br.com.ecge.ecgefoods.domain.Produto;
import br.com.ecge.ecgefoods.service.MesaService;
import br.com.ecge.ecgefoods.service.ProdutoService;
import br.com.ecge.ecgefoods.utils.AnimUtils;
import br.com.ecge.ecgefoods.utils.ECGEFoodsUtils;
import br.com.ecge.ecgefoods.utils.InfoUtils;
import br.com.ecge.ecgefoods.utils.StringUtils;

public class ProdutoDialogFragment extends DialogFragment implements ObservacaoDialogFragment.ObservacaoListerner {

    private List<Produto> produtos;
    private Produto produtoSelecionado;
    private Mesa mesa;
    private Categoria categoria;
    private RecyclerView rvProdutos;
    private SwipeRefreshLayout srProdutos;
    private CardView cvProdutoSelecionado;
    private TextView tvProdutoSelecionado;
    private EditText quantidadeProduto;
    private String observacoes;
    private ImageButton adiciona;
    private ImageButton remove;
    private ImageButton exibirObservacoes;
    private ImageButton removeSelecao;
    private Button concluirPedido;
    private Button cancelar;
    private ObservacaoDialogFragment observacaoDialog;
    public static final String TAG = "ProdutoDialogFragment";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.observacoes = StringUtils.VAZIO;
        if (getArguments() != null) {
            mesa = Parcels.unwrap(getArguments().getParcelable("mesa"));
            categoria = Parcels.unwrap(getArguments().getParcelable("categoria"));
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertStyle);
        View body = getActivity().getLayoutInflater().inflate(R.layout.dialog_produto, null);
        View head = getActivity().getLayoutInflater().inflate(R.layout.head_dialog_produto, null);
        findViewsById(body);
        ocultarProdutoSelecionado();
        setRvProdutos();
        setSrProdutos();
        setAcoesListeners();
        builder.setView(body);
        builder.setCustomTitle(head);
        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.borda_arredondada);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        taskProdutos();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void setObservacao(String observacoes) {
        if (!observacoes.isEmpty()) {
            this.observacoes = observacoes;
        }
    }

    private void ocultarProdutoSelecionado() {
        AnimUtils.slideDown(cvProdutoSelecionado);
    }

    private void findViewsById(View view) {
        rvProdutos = view.findViewById(R.id.recyclerViewProduto);
        srProdutos = view.findViewById(R.id.swipeToRefreshProduto);
        cvProdutoSelecionado = view.findViewById(R.id.cvProdutoSelecionado);
        tvProdutoSelecionado = view.findViewById(R.id.tvProdutoSelecionado);
        quantidadeProduto = view.findViewById(R.id.edtQuantidadeProduto);
        adiciona = view.findViewById(R.id.btnMais);
        remove = view.findViewById(R.id.btnMenos);
        removeSelecao = view.findViewById(R.id.btnDesfazerSelecao);
        exibirObservacoes = view.findViewById(R.id.btnExibirObservacoes);
        concluirPedido = view.findViewById(R.id.btnAdicionarProduto);
        cancelar = view.findViewById(R.id.btnCancelarProduto);
    }

    private void setRvProdutos() {
        rvProdutos.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvProdutos.setItemAnimator(new DefaultItemAnimator());
        rvProdutos.setHasFixedSize(true);
        rvProdutos.setNestedScrollingEnabled(false);
    }

    private void setSrProdutos() {
        srProdutos.setOnRefreshListener(onRefreshListener());
        srProdutos.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener() {
        return () -> taskProdutos();
    }

    private void taskMesa() { startTask("mesa", new GetMesaTask(), R.id.swipeToRefresh); }

    public void taskProdutos() {
        startTask("produtos", new GetProdutosTask(), R.id.swipeToRefreshProduto);
    }

    private View.OnClickListener adicionar() {
        return v -> {
            if (quantidadeProduto.getText() != null) {
                int quantidade = Integer.parseInt(quantidadeProduto.getText().toString());
                quantidade++;
                quantidadeProduto.setText(String.valueOf(quantidade));
            }
        };
    }

    private View.OnClickListener diminuir() {
        return v -> {
            if (quantidadeProduto.getText() != null && Integer.parseInt(quantidadeProduto.getText().toString()) > 1) {
                int quantidade = Integer.parseInt(quantidadeProduto.getText().toString());
                quantidade--;
                quantidadeProduto.setText(String.valueOf(quantidade));
            } else {
                quantidadeProduto.setText("1");
            }
        };
    }

    private View.OnClickListener adicionarProdutoPedido() {
        return v -> {
            // ADICIONAR VALIDAÇÃO DE CAMPOS OBRIGATÓRIOS
            if (validarCamposObrigatorios()) {
                taskMesa();
            }
        };
    }

    private boolean validarCamposObrigatorios() {
        if (produtoSelecionado == null || produtoSelecionado.getId() == null) {
            InfoUtils.toast(getContext(), getString(R.string.selecione_produto));
            return false;
        }

        if (quantidadeProduto == null || quantidadeProduto.getText().toString().equalsIgnoreCase(StringUtils.VAZIO) || Integer.parseInt(quantidadeProduto.getText().toString()) < 1) {
            InfoUtils.toast(getContext(), getString(R.string.valor_maior_um));
            return false;
        }

        return true;
    }

    private void setProdutoMesa(Mesa mesa) {
        mesa.setMesaEnviadaBackend(false);
        Pedido pedido = new Pedido();
        pedido.setProduto(produtoSelecionado.getNome());
        pedido.setIdProduto(produtoSelecionado.getId());
        pedido.setPreco(produtoSelecionado.getPrecoAtual());
        pedido.setUnidadeMedida(produtoSelecionado.getUnidadeMedida().getSigla());
        pedido.setTotal(produtoSelecionado.getPrecoAtual().multiply(new BigDecimal(quantidadeProduto.getText().toString())));
        pedido.setQuantidade(Integer.parseInt(quantidadeProduto.getText().toString()));
        pedido.setObservacao(observacoes);
        mesa.setIdGarcon(ECGEFoodsUtils.getUsuarioLogado(getContext()).getId().intValue());
        if (mesa.getId() == null && mesa.getPedidos() == null) {
            dismiss();
            goToPedidos(mesa, pedido);
        } else {
            dismiss();
            PedidoFragment pf = (PedidoFragment) getActivity().getSupportFragmentManager().findFragmentByTag(PedidoFragment.TAG);
            if (pf != null) {
                pf.getPedidos().add(pedido);
                pf.recyclerView.getAdapter().notifyDataSetChanged();
                pf.atualizarTotalizadores();
            } else {
                goToPedidos(mesa, pedido);
            }
        }
    }

    private void goToPedidos(Mesa mesa, Pedido pedido) {
        Intent intent = new Intent(getContext(), PedidoActivity.class);
        intent.putExtra("pedido", Parcels.wrap(pedido));
        intent.putExtra("mesa", Parcels.wrap(mesa));
        ActivityCompat.startActivity(getContext(), intent, null);
        getActivity().overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }

    private View.OnClickListener removerSelecao() {
        return v -> {
            AnimUtils.slideDown(cvProdutoSelecionado);
            tvProdutoSelecionado.setText(StringUtils.VAZIO);
            AnimUtils.slideUp(this.srProdutos);
        };
    }

    private View.OnClickListener exibirObservacoes() {
        return v -> {
            produtoSelecionado.setIdGrupoProduto(Integer.parseInt(categoria.getId()));
            observacaoDialog = ObservacaoDialogFragment.newInstance();
            if (observacaoDialog.getArguments() == null) {
                observacaoDialog.setArguments(getBundle(produtoSelecionado));
            } else {
                observacaoDialog.getArguments().putAll(getBundle(produtoSelecionado));
            }
            observacaoDialog.show(getActivity().getSupportFragmentManager(), ObservacaoDialogFragment.TAG);
        };
    }

    private Bundle getBundle(Produto produto) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("produto", Parcels.wrap(produto));
        return bundle;
    }

    private void setAcoesListeners() {
        exibirObservacoes.setOnClickListener(exibirObservacoes());
        removeSelecao.setOnClickListener(removerSelecao());
        adiciona.setOnClickListener(adicionar());
        remove.setOnClickListener(diminuir());
        concluirPedido.setOnClickListener(adicionarProdutoPedido());
        cancelar.setOnClickListener(cancelar());
    }

    private ProdutoAdapter.ProdutoOnClickListener onClickListener() {
        return (view, idx) -> {
            AnimUtils.slideUp(this.cvProdutoSelecionado);
            String produto = ((TextView) view.findViewById(R.id.descricaoProduto)).getText().toString();
            this.tvProdutoSelecionado.setText(produto);
            setProdutoSelecionado(produto);
            AnimUtils.slideDown(this.srProdutos);
        };
    }

    private void setProdutoSelecionado(String descricao) {
        produtoSelecionado = Stream.of(produtos).filter(produto -> produto.getNome().equalsIgnoreCase(descricao)).single();
    }

    private class GetMesaTask implements TaskListener<Mesa> {

        @Override
        public Mesa execute() throws Exception {
            return MesaService.getMesa(getContext(), Integer.parseInt(mesa.getNumeroMesa()));
        }

        @Override
        public void updateView(Mesa mesa) {
            if (mesa == null) {
                setProdutoMesa(mesa);
            } else {
                setProdutoMesa(ProdutoDialogFragment.this.mesa);
            }
        }

        @Override
        public void onError(Exception exception) {

        }

        @Override
        public void onCancelled(String cod) {

        }
    }

    private class GetProdutosTask implements TaskListener<List<Produto>> {

        @Override
        public List<Produto> execute() throws Exception {
            produtos = ProdutoService.getProdutos(categoria, getContext());
            return produtos;
        }

        @Override
        public void updateView(List<Produto> produtos) {
            if (produtos != null && !produtos.isEmpty()) {
                rvProdutos.setAdapter(new ProdutoAdapter(getContext(), produtos, onClickListener()));
                srProdutos.setRefreshing(false);
            } else {
                InfoUtils.toast(getContext(), getContext().getString(R.string.mesas_sem_lancamento));
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
