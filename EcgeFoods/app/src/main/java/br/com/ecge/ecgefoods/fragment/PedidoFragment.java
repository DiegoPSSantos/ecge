package br.com.ecge.ecgefoods.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.annimon.stream.Stream;

import org.parceler.Parcels;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.ecge.ecgefoods.R;
import br.com.ecge.ecgefoods.activity.PedidoActivity;
import br.com.ecge.ecgefoods.adapter.PedidoAdapter;
import br.com.ecge.ecgefoods.domain.Mesa;
import br.com.ecge.ecgefoods.domain.Pedido;
import br.com.ecge.ecgefoods.domain.Pessoa;
import br.com.ecge.ecgefoods.domain.Receita;
import br.com.ecge.ecgefoods.domain.Servico;
import br.com.ecge.ecgefoods.service.MesaService;
import br.com.ecge.ecgefoods.service.PedidoService;
import br.com.ecge.ecgefoods.service.PessoaService;
import br.com.ecge.ecgefoods.utils.AnimUtils;
import br.com.ecge.ecgefoods.utils.ECGEFoodsUtils;
import br.com.ecge.ecgefoods.utils.InfoUtils;
import br.com.ecge.ecgefoods.utils.StringUtils;

public class PedidoFragment extends BaseFragment {

    protected RecyclerView recyclerView;
    private PedidoAdapter pedidoAdapter;
    private SwipeRefreshLayout swipeRefresh;
    private TextView labelServico;
    private TextView labelSubtotal;
    private TextView numeroMesa;
    private TextView garcom;
    private TextView subtotal;
    private TextView servico;
    private TextView total;
    private View view;
    private List<Pedido> pedidos;
    private List<Pedido> pedidosOriginal;
    private Map<Long, Pedido> pedidosCancelados;
    private static PedidoFragment fragment;
    private Mesa mesa;
    private Servico servicoMesa;
    private BigDecimal valorServico;
    private Pessoa pessoaGarcom;
    private Pedido novoPedido;
    private static Pedido pedidoCancelado;
    private static int deleteIndex;
    private final int CODE_CANCELAR = 1;
    public static final String TAG = "PedidoFragment";

    public static PedidoFragment newInstance() {
        if (fragment == null) {
            fragment = new PedidoFragment();
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mesa = Parcels.unwrap(getArguments().getParcelable("mesa"));
            this.novoPedido = getArguments().getParcelable("pedido") != null ? Parcels.unwrap(getArguments().getParcelable("pedido")) : null;
        }
        pedidosOriginal = new ArrayList<>();
        pedidosCancelados = new HashMap<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pedido, container, false);
        recyclerView = view.findViewById(R.id.rvPedidos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        swipeRefresh = view.findViewById(R.id.swipeToRefresh);
        swipeRefresh.setOnRefreshListener(onRefreshListener());
        swipeRefresh.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);
        labelServico = view.findViewById(R.id.labelServico);
        labelSubtotal = view.findViewById(R.id.labelSubtotal);
        numeroMesa = view.findViewById(R.id.txtMesa);
        garcom = view.findViewById(R.id.txtGarcom);
        subtotal = view.findViewById(R.id.txtSubtotal);
        servico = view.findViewById(R.id.txtServico);
        total = view.findViewById(R.id.txtTotal);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adicionarNovoPedido();
        taskServico();
        if (this.mesa.getId() != null) {
            taskMesaPedidosOriginal();
            taskMesaPedidos();
        } else {
            setPedido();
        }

    }

    public List<Pedido> getPedidos() {
        return this.pedidos;
    }

    public Mesa getMesa() {
        return this.mesa;
    }

    private void setNumeroMesa() {
        numeroMesa.setText("MESA " + this.mesa.getNumeroMesa());
    }

    private void setGarcom() {
        if (pessoaGarcom != null && pessoaGarcom.getNome() != null) {
            garcom.setText(pessoaGarcom.getNome());
        } else {
            garcom.setText(ECGEFoodsUtils.getUsuarioLogado(getContext()).getPessoa().getNome());
        }
    }

    private void adicionarNovoPedido() {
        pedidos = new ArrayList<>();
        if (novoPedido != null) {
            pedidos.add(novoPedido);
        }
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener() {
        return () -> {
            if (novoPedido == null) {
                taskMesaPedidos();
            } else {
                swipeRefresh.setRefreshing(false);
            }
        };
    }

    private void taskServico() {
        startTask("servico", new GetServicoTask(), R.id.swipeToRefresh);
    }

    public void taskEnviarPedido() {
        startTask("enviarPedido", new SalvarMesaTask(), R.id.swipeToRefresh);
    }

    public void taskFecharConta() {
        startTask("fechaConta", new FecharMesaTask(), R.id.swipeToRefresh);
    }

    public void taskMesaPedidos() {
        startTask("mesaPedidos", new MesaPedidosTask(), R.id.swipeToRefresh);
    }

    public void taskMesaPedidosOriginal() {
        startTask("mesaPedidosOriginal", new MesaPedidosOriginalTask(), R.id.swipeToRefresh);
    }

    private void taskCancelarPedido() {
        startTask("cancelarPedido", new CancelarPedidoTask(), R.id.swipeToRefresh);
    }

    private PedidoAdapter.PedidoOnClickListener onClickPedido() {
        return new PedidoAdapter.PedidoOnClickListener() {
            @Override
            public void onClickPedido(View view, int idx) {
//                pedidoCancelado = pedidos.get(idx);
//                deleteIndex = idx;
//                setQuantidade(getActivity().getSupportFragmentManager());
            }

            @Override
            public void adicionarQuantidade(TextView tvQuantidade, int idx) {
                int qtd = Integer.parseInt(tvQuantidade.getText().toString());
                if (qtd < 999) {
                    atualizarQuantidade(qtd + 1, idx, true);
                    tvQuantidade.setText(String.valueOf(qtd + 1));
                }
            }

            @Override
            public void diminuirQuantidade(TextView tvQuantidade, int idx) {
                int qtd = Integer.parseInt(tvQuantidade.getText().toString());
                Pedido p = pedidos.get(idx);
                if (p.getCodigo() != null && Stream.of(pedidosOriginal).anyMatch(pedido -> pedido.getIdProduto().intValue() == p.getIdProduto().intValue() && (pedido.getQuantidade() == p.getQuantidade() || pedido.getQuantidade() + 1 == p.getQuantidade()))) {
                    p.setEnviadoProducao(true);
                    p.setQuantidade(pedidosOriginal.get(idx).getQuantidade());
                    recyclerView.getAdapter().notifyItemChanged(idx);
                    InfoUtils.longToast(getContext(), getString(R.string.falha_diminuir));
                    return;
                }
                if (qtd > 1) {
                    atualizarQuantidade(qtd - 1, idx, false);
                    tvQuantidade.setText(String.valueOf(qtd - 1));
                } else {
                    AnimUtils.slideDown(((PedidoActivity)getActivity()).getBnv());
                    InfoUtils.snack(view, getContext().getString(R.string.cancelar_produto)).addCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar transientBottomBar, int event) {
                            if (event == CODE_CANCELAR) {
                                pedidoCancelado = pedidos.get(idx);
                                if (pedidoCancelado.getEnviadoProducao()) {
                                    taskCancelarPedido();
                                } else {
                                    pedidos.remove(idx);
                                    recyclerView.getAdapter().notifyDataSetChanged();
                                    atualizarTotalizadores();
                                }
                            }
                            AnimUtils.slideUp(((PedidoActivity)getActivity()).getBnv());
                        }
                    });
                }
            }

//            @Override
//            public void exibirObservacoes(View view, int idx) {
//                Pedido p = pedidos.get(idx);
//                Produto produto = new Produto();
//                produto.setId(p.getIdProduto());
//                produto.se
//                produtoSelecionado.setIdGrupoProduto(Integer.parseInt(categoria.getId()));
//                observacaoDialog = ObservacaoDialogFragment.newInstance();
//                if (observacaoDialog.getArguments() == null) {
//                    observacaoDialog.setArguments(getBundle(produtoSelecionado));
//                } else {
//                    observacaoDialog.getArguments().putAll(getBundle(produtoSelecionado));
//                }
//                observacaoDialog.show(getActivity().getSupportFragmentManager(), ObservacaoDialogFragment.TAG);
//            }

        };
    }

    private void atualizarQuantidade(int quantidade, int idx, boolean adiciona) {
        Pedido pa = pedidos.get(idx);
        pa.setQuantidade(quantidade);
        pa.setEnviadoProducao(false);
        pa.setTotal(pa.getPreco().multiply(new BigDecimal(pa.getQuantidade())));
        if (!adiciona) {
            pedidosCancelados.put(pa.getIdProduto(), pa);
        }
        recyclerView.getAdapter().notifyItemChanged(idx);
        atualizarTotalizadores();
    }

    private Mesa cancelarPedido(Pedido pedido) throws IOException {
        return PedidoService.deletarPedido(getContext(), pedido, mesa);
    }

    public View.OnClickListener desfazerExclusao(Pedido pedido, int deleteIndex) {
        return view -> pedidoAdapter.restaurarPedido(pedido, deleteIndex);
    }

    private class GetServicoTask implements TaskListener<Servico> {

        @Override
        public Servico execute() throws Exception {
            return MesaService.getServico(getContext());
        }

        @Override
        public void updateView(Servico servico) {
            if (servico != null) {
                PedidoFragment.this.servicoMesa = servico;
                atualizarTotalizadores();
            }
        }

        @Override
        public void onError(Exception exception) {

        }

        @Override
        public void onCancelled(String cod) {

        }
    }

    private class MesaPedidosTask implements TaskListener<List<Pedido>> {

        @Override
        public List<Pedido> execute() throws Exception {
            Mesa m = MesaService.getMesa(getContext(), Integer.parseInt(mesa.getNumeroMesa()));
            if (m.getNumeroMesa() != null) {
                mesa = m;
                pessoaGarcom = PessoaService.getPessoa(getContext(), mesa.getIdGarcon());
            } else {
                pessoaGarcom = PessoaService.getPessoa(getContext(), ECGEFoodsUtils.getUsuarioLogado(getContext()).getId().intValue());
            }

            return PedidoService.getPedidos(getContext(), mesa);
        }

        @Override
        public void updateView(List<Pedido> pedidos) {
            if (pedidos != null) {
                PedidoFragment.this.pedidos.clear();
                if (novoPedido != null) {
                    pedidos.add(novoPedido);
                }
                PedidoFragment.this.pedidos.addAll(pedidos);
                swipeRefresh.setRefreshing(false);
            }
            setPedido();
            atualizarTotalizadores();
        }

        @Override
        public void onError(Exception exception) {

        }

        @Override
        public void onCancelled(String cod) {

        }

    }

    private class MesaPedidosOriginalTask implements TaskListener<List<Pedido>> {

        @Override
        public List<Pedido> execute() throws Exception {
            Mesa m = MesaService.getMesa(getContext(), Integer.parseInt(mesa.getNumeroMesa()));
            mesa = m;
            return PedidoService.getPedidos(getContext(), mesa);
        }

        @Override
        public void updateView(List<Pedido> pedidos) {
            if (pedidos != null) {
                PedidoFragment.this.pedidosOriginal.clear();
                PedidoFragment.this.pedidosOriginal.addAll(pedidos);
            }
        }

        @Override
        public void onError(Exception exception) {

        }

        @Override
        public void onCancelled(String cod) {

        }

    }

    private class FecharMesaTask implements  TaskListener<Mesa> {

        @Override
        public Mesa execute() throws Exception {
            if (MesaService.fecharMesa(getContext(), mesa, servicoMesa)) {
                return mesa;
            } else {
                return null;
            }
        }

        @Override
        public void updateView(Mesa mesa) {
            swipeRefresh.setRefreshing(false);
            if (mesa != null) {
                InfoUtils.toast(getContext(), getString(R.string.mesa_fecha_conta_enviado));
                getActivity().onBackPressed();
            } else {
                InfoUtils.toast(getContext(), getString(R.string.falha_fecha_conta));
            }
            atualizarTotalizadores();
        }

        @Override
        public void onError(Exception exception) {

        }

        @Override
        public void onCancelled(String cod) {

        }
    }

    private class SalvarMesaTask implements TaskListener<Mesa> {

        @Override
        public Mesa execute() throws Exception {
            setPedidoEnviadoProducaoCancelado(Stream.of(pedidosCancelados.values()).toList());
//            setPedidoAdicionadosEnviadoProducao(pedidos);
//            PedidoService.enviarPedido(getContext(), pedidosOriginal, Stream.of(pedidosCancelados.values()).toList(), mesa, servicoMesa);
            PedidoService.enviarPedido(getContext(), pedidos, Stream.of(pedidosCancelados.values()).toList(), mesa, getServicoEnviar());
            return mesa;
        }

        @Override
        public void updateView(Mesa mesa) {
            swipeRefresh.setRefreshing(false);
            pedidosCancelados = new HashMap<>();
            novoPedido = null;
            if (mesa != null) {
                InfoUtils.toast(getContext(), getString(R.string.pedido_enviado));
                getActivity().onBackPressed();
            } else {
                InfoUtils.toast(getContext(), getString(R.string.falha_envio_pedido));
            }
            atualizarTotalizadores();
        }

        @Override
        public void onError(Exception exception) {

        }

        @Override
        public void onCancelled(String cod) {

        }
    }

    private class CancelarPedidoTask implements TaskListener<Mesa> {

        @Override
        public Mesa execute() throws Exception {
            return cancelarPedido(pedidoCancelado);
        }

        @Override
        public void updateView(Mesa mesa) {
            if (mesa.getId() == null) {
                desfazerExclusao(pedidoCancelado, deleteIndex);
                InfoUtils.toast(getContext(), getString(R.string.falha_exclusao_produto));
            } else {
                taskMesaPedidos();
            }
            atualizarTotalizadores();
            swipeRefresh.setRefreshing(false);
        }

        @Override
        public void onError(Exception exception) {

        }

        @Override
        public void onCancelled(String cod) {

        }
    }

    private void setPedidoEnviadoProducaoCancelado(List<Pedido> pedidosCancelados) {
        Stream.of(pedidosCancelados).forEach(pedidoCancelado ->  {
            if (Stream.of(pedidos).anyMatch(pedido -> pedido.getIdProduto().intValue() == pedidoCancelado.getIdProduto().intValue())) {
                Stream.of(pedidos).filter(p -> p.getIdProduto().intValue() == pedidoCancelado.getIdProduto().intValue()).single().setEnviadoProducao(true);
            }
        });
    }

    private void setPedidoAdicionadosEnviadoProducao(List<Pedido> pedidosAdicionados) {
        Stream.of(pedidosAdicionados).forEach(pedido -> {
           if (Stream.of(pedidosOriginal).anyMatch(po -> po.getIdProduto().intValue() == pedido.getIdProduto().intValue())) {
               Pedido pedidoOriginal = Stream.of(pedidosOriginal).filter(po -> po.getIdProduto().intValue() == pedido.getIdProduto().intValue()).single();
               pedidoOriginal.setQuantidade(pedido.getQuantidade() - pedidoOriginal.getQuantidade());
               pedidoOriginal.setEnviadoProducao(false);
           } else {
               pedidosOriginal.add(pedido);
           }
        });
    }

    protected void atualizarTotalizadores() {
        List<Pedido> pedidosNaoEnviado = Stream.of(this.pedidos).filter(pedido -> !pedido.getEnviadoProducao()).toList();
        BigDecimal subtotal = calcularTotal().setScale(2, BigDecimal.ROUND_HALF_EVEN);
        valorServico = subtotal.multiply(new BigDecimal(this.servicoMesa.getPrecoAtual().doubleValue()/100)).setScale(2,BigDecimal.ROUND_HALF_EVEN);
//        if (pedidosNaoEnviado != null && pedidosNaoEnviado.size() > 0) {
            this.subtotal.setText("R$ " + subtotal.setScale(2, BigDecimal.ROUND_HALF_EVEN));
            this.servico.setText("R$ " + valorServico.setScale(2, BigDecimal.ROUND_HALF_EVEN));
            this.total.setText("R$ " + subtotal.add(valorServico).setScale(2, BigDecimal.ROUND_HALF_EVEN));
//        } else {
//            this.subtotal.setText("R$ " + getValorSubtotal(mesa.getReceita()));
//            this.servico.setText("R$ " + (mesa.getReceita().getReceitaServico().getListaReceitaServicoServico() != null ? mesa.getReceita().getReceitaServico().getListaReceitaServicoServico().get(0).getValorStr() : "0,00"));
//            this.total.setText("R$ " + (mesa.getReceita().getReceitaVenda().getListaReceitaVendaProduto() != null ? mesa.getReceita().getReceitaVenda().getListaReceitaVendaProduto().get(0).getValorStr() : "0,00"));
//        }
    }

    private String getValorSubtotal(Receita receita) {
        if (receita.getReceitaVenda().getListaReceitaVendaProduto() != null && receita.getReceitaServico().getListaReceitaServicoServico() != null) {
            BigDecimal subtotal = receita.getReceitaVenda().getListaReceitaVendaProduto().get(0).getValor().add(receita.getReceitaServico().getListaReceitaServicoServico().get(0).getValor());
            return StringUtils.getValorFormatoDinheiro(subtotal);
        }
        return "0,00";
    }

    private BigDecimal calcularTotal() {
        BigDecimal total = new BigDecimal(0);
        for (Pedido pedido : this.pedidos) {
            total = total.add(pedido.getTotal());
        }
        return total;
    }

    private void setPedido() {
        setNumeroMesa();
        setGarcom();
        pedidoAdapter = new PedidoAdapter(getContext(), PedidoFragment.this.pedidos, onClickPedido());
        recyclerView.setAdapter(pedidoAdapter);
        swipeRefresh.setRefreshing(false);
    }

    private Servico getServicoEnviar() {
        servicoMesa.setValorServico(valorServico);
        return servicoMesa;
    }
}
