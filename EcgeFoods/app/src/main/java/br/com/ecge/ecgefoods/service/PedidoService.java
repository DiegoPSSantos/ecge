package br.com.ecge.ecgefoods.service;

import android.content.Context;

import com.annimon.stream.Stream;
import com.google.gson.Gson;
import com.loopj.android.http.*;

import org.json.JSONArray;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.ecge.ecgefoods.domain.Item;
import br.com.ecge.ecgefoods.domain.ItemReceita;
import br.com.ecge.ecgefoods.domain.Mesa;
import br.com.ecge.ecgefoods.domain.MesaProduto;
import br.com.ecge.ecgefoods.domain.Observacao;
import br.com.ecge.ecgefoods.domain.Pedido;
import br.com.ecge.ecgefoods.domain.ReceitaVendaProduto;
import br.com.ecge.ecgefoods.domain.Servico;
import br.com.ecge.ecgefoods.domain.Usuario;
import br.com.ecge.ecgefoods.fragment.MesaFragment;
import br.com.ecge.ecgefoods.utils.ConfigUtils;
import br.com.ecge.ecgefoods.utils.ECGEFoodsUtils;
import br.com.ecge.ecgefoods.utils.HttpUtils;
import br.com.ecge.ecgefoods.utils.StringUtils;
import cz.msebera.android.httpclient.Header;

public class PedidoService {

    private static final String TIPO_ITEM_PRODUTO = "PRODUTO";
    private static final String TIPO_ITEM_SERVICO = "SERVICO";
    private static final String URL_PEDIDO_ENVIAR = MesaService.URL_MESA_SAVE_UPDATE;
    private static final String MSG_INVALIDA = "Mensagem inváida";

    public static List<Pedido> getPedidos(Context contexto, Mesa mesaConsultar) throws IOException {
        List<Pedido> pedidos = new ArrayList<>();
        Mesa mesaConsultada = MesaService.getMesa(contexto, Integer.parseInt(mesaConsultar.getNumeroMesa()));
        parserMesaPedido(mesaConsultada);
        pedidos.addAll(parserMesaPedido(mesaConsultada));
        return pedidos;
    }

    public static boolean enviarPedido(Context contexto, List<Pedido> pedidos, List<Pedido> pedidosCancelados, Mesa mesa, Servico servico) throws IOException {
        HttpUtils http = new HttpUtils();
        String json = parserPedidosMesaJSON(contexto, pedidos, pedidosCancelados, mesa, servico);
        Map<String, String> parametros = MesaService.getParametros(json);
        String retorno = http.doPost(ConfigUtils.getEndpoint(contexto) + URL_PEDIDO_ENVIAR, parametros, StringUtils.ENCODE_UTF8, true);
        if (!retorno.contains(MSG_INVALIDA)) {
            return true;
        } else {
            return false;
        }
    }

    public static Mesa deletarPedido(Context contexto, Pedido pedido, Mesa mesa) throws IOException {
        if (Stream.of(mesa.getReceita().getItensReceita()).anyMatch(item -> item.getIdItem().intValue() == pedido.getIdProduto().intValue())) {
            ItemReceita itemCancelado = Stream.of(mesa.getReceita().getItensReceita()).filter(item -> item.getIdItem().intValue() == pedido.getIdProduto().intValue()).single();
            itemCancelado.setCancelado(true);
            if (MesaService.cancelarMesaProduto(contexto, mesa)) {
                return mesa;
            } else {
                return new Mesa();
            }
        } else {
            return new Mesa();
        }

    }

    private static RequestParams getParamentros(String json) {
        RequestParams parametros = new RequestParams();
        parametros.put(MesaService.PARAM_MESA, json);
        return parametros;
    }

    private static List<Pedido> parserMesaPedido(Mesa m) {
        List<Pedido> pedidos = new ArrayList<>();
        if (m.getReceita() != null) {
            for (ReceitaVendaProduto rvp : m.getReceita().getReceitaVenda().getListaReceitaVendaProduto()) {
                Pedido pedido = new Pedido();
                pedido.setIdProduto(rvp.getProduto().getId());
                pedido.setProduto(rvp.getProduto().getNome());
                pedido.setQuantidade(rvp.getQuantidade().intValue());
                pedido.setUnidadeMedida(rvp.getProduto().getUnidadeMedida().getSigla());
                pedido.setCodigo(rvp.getProduto().getCodigo());
                pedido.setPreco(rvp.getProduto().getPrecoAtual());
                pedido.setEnviadoProducao(rvp.getProduto().isEnviadoProducao());
                pedido.setTotal(rvp.getValor());
                pedidos.add(pedido);
            }
        }
        return pedidos;
    }

    private static String parserPedidosMesaJSON(Context contexto, List<Pedido> pedidos, List<Pedido> cancelados, Mesa mesa, Servico servico) {
        MesaProduto mp = getMesaProduto(contexto, pedidos, cancelados, mesa, servico);
        String json = new Gson().toJson(mp);
        return json;
    }

    private static MesaProduto getMesaProduto(Context contexto, List<Pedido> pedidos, List<Pedido> cancelados, Mesa mesa, Servico servico) {
        MesaProduto mp = new MesaProduto();
        mp.setStatus(MesaFragment.STATUS_OCUPADA);
        if (mesa.getIdGarcon() == null) {
            Usuario usuario = ECGEFoodsUtils.getUsuarioLogado(contexto);
            mesa.setIdGarcon(usuario.getId().intValue());
        }
        mp.setIdGarcon(mesa.getIdGarcon());
        mp.setNumeroMesa(mesa.getNumeroMesa());
        mp.setCancelamentoTotal(false);
        mp.setItensReceita(setItensReceita(pedidos, mesa));
        mp.setImpressoraFechamento(ECGEFoodsUtils.getImpressora(contexto));
        if (servico != null) {
            setServico(mp, servico);
        }
        if (cancelados != null && !cancelados.isEmpty()) {
            mp.setItensCanceladosReceita(setItensCancelados(cancelados, mesa));
        }
        return mp;
    }

    private static void setServico(MesaProduto mp, Servico servico) {
        Item itemServico = new Item();
        itemServico.setIdItem(Integer.parseInt(servico.getId()));
        itemServico.setNr_item(1);
        itemServico.setTipoItem(TIPO_ITEM_SERVICO);
        itemServico.setCodigoItem(servico.getCodigo());
        itemServico.setPreco(servico.getValorServico());
        itemServico.setSubTotal(servico.getValorServico());
        itemServico.setQuantidade(1);
        mp.getItensReceita().add(itemServico);
    }

    private static List<Item> setItensReceita(List<Pedido> pedidos, Mesa mesa) {
        Item item;
        List<Item> itens = new ArrayList<>();
        int index = 0;
        for (Pedido pedido : pedidos) {
            item = new Item();
            item.setIdItem(pedido.getIdProduto().intValue());
            item.setTipoItem(TIPO_ITEM_PRODUTO);
            item.setNome(pedido.getProduto());
            item.setNr_item(++index);
            item.setCancelado(false);
            item.setPreco(pedido.getPreco().setScale(2, BigDecimal.ROUND_HALF_EVEN));
            item.setUnidadeMedida(pedido.getUnidadeMedida());
            item.setQuantidade(pedido.getQuantidade());
            item.setEnviadoProducao(pedido.getEnviadoProducao());
            item.setIdGarcomLancamento(mesa.getIdGarcon());
            item.setObservacao(pedido.getObservacao());
            item.setSubTotal(pedido.getTotal());
            item.setCodigoItem(pedido.getCodigo());
            item.setListaObservacoes(getObservacoes(pedido.getObservacao()));
            itens.add(item);
        }
        return itens;
    }

    private static List<Observacao> getObservacoes(String observacoes) {
        List<Observacao> lista = new ArrayList<>();
        if (observacoes != null && !observacoes.isEmpty()) {
            lista.addAll(Stream.of(observacoes.split(StringUtils.PONTO_VIRGULA)).map(obs -> new Observacao(obs)).toList());
        }
        return lista;
    }

    private static List<Item> setItensCancelados(List<Pedido> cancelados, Mesa mesa) {
        Item item;
        List<Item> itens = new ArrayList<>();
        int index = 0;
        for (Pedido pedido : cancelados) {
            item = new Item();
            item.setIdItem(pedido.getIdProduto().intValue());
            item.setTipoItem(TIPO_ITEM_PRODUTO);
            item.setNome(pedido.getProduto());
            item.setNr_item(++index);
            item.setCancelado(true);
            item.setQuantidade(pedido.getQuantidade());
            item.setCodigoItem(pedido.getCodigo());
            itens.add(item);
        }
        return itens;
    }
}
