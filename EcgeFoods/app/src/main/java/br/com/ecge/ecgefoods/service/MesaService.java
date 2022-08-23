package br.com.ecge.ecgefoods.service;

import android.content.Context;
import android.util.Log;

import com.annimon.stream.Stream;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.ecge.ecgefoods.R;
import br.com.ecge.ecgefoods.domain.Item;
import br.com.ecge.ecgefoods.domain.ItemReceita;
import br.com.ecge.ecgefoods.domain.Mesa;
import br.com.ecge.ecgefoods.domain.MesaProduto;
import br.com.ecge.ecgefoods.domain.ReceitaServico;
import br.com.ecge.ecgefoods.domain.ReceitaServicoServico;
import br.com.ecge.ecgefoods.domain.ReceitaVendaProduto;
import br.com.ecge.ecgefoods.domain.Servico;
import br.com.ecge.ecgefoods.utils.ConfigUtils;
import br.com.ecge.ecgefoods.utils.ECGEFoodsUtils;
import br.com.ecge.ecgefoods.utils.HttpUtils;
import br.com.ecge.ecgefoods.utils.StringUtils;

public class MesaService {

    public static final String URL_MESA_SAVE_UPDATE = "/mesa/salvar";
    private static final String URL_MESA_SERVICO = "/mesa/servicoMesa";
    private static final String URL_MESAS = "/mesa/listar";
    private static final String URL_MESA = "/mesa/";
    public static final String PARAM_MESA = "mesaDto";
    private static final String TAG = "MesaService";

    public static List<Mesa> getMesas(Context contexto) throws IOException {
        HttpUtils http = new HttpUtils();
        String json = http.doGet(ConfigUtils.getEndpoint(contexto) + URL_MESAS);
        List<Mesa> mesas = new ArrayList<>();
//        if (!json.contains(contexto.getString(R.string.lista_mesas_vazia))) {
            mesas = parserMesasJSON(json);
//        }
        return mesas;
    }

    public static Mesa getMesa(Context contexto, Integer numeroMesa) throws IOException {
        HttpUtils http = new HttpUtils();
        String json = http.doGet(ConfigUtils.getEndpoint(contexto) + URL_MESA + numeroMesa);
        Mesa mesa = parserMesaJSON(json);
        if (mesa == null) {
            mesa = new Mesa();
        }
        return mesa;
    }

    public static Servico getServico(Context contexto) throws IOException {
        HttpUtils http = new HttpUtils();
        String json = http.doGet(ConfigUtils.getEndpoint(contexto) + URL_MESA_SERVICO);
        Servico servico = parserServicoJSON(json);
        return servico;
    }

    public static boolean cancelarMesaProduto(Context contexto, Mesa mesa) throws IOException {
        HttpUtils http = new HttpUtils();
        http.setContentType(StringUtils.TYPE_JSON);
        mesa.setCancelamentoTotal(true);
        String json = parserJSONMesaCancelamento(contexto, mesa);
        Map<String, String> parametros = getParametros(json);
        String retorno = http.doPost(ConfigUtils.getEndpoint(contexto) + URL_MESA_SAVE_UPDATE, parametros, StringUtils.ENCODE_UTF8, true);
        if (retorno != null) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean fecharMesa(Context contexto, Mesa mesa, Servico servico) throws IOException {
        HttpUtils http = new HttpUtils();
        http.setContentType(StringUtils.TYPE_JSON);
        mesa = dividirPreco(mesa);
        String json = parserJSONMesa(contexto, mesa, servico);
        Map<String, String> parametros = getParametros(json);
        String retorno = http.doPost(ConfigUtils.getEndpoint(contexto) + URL_MESA_SAVE_UPDATE, parametros, StringUtils.ENCODE_UTF8, true);
        if (retorno != null) {
            return true;
        } else {
            return false;
        }
    }

    private static List<Mesa> parserMesasJSON(String json) {
        Type listaMesas = new TypeToken<List<Mesa>>(){}.getType();
        List<Mesa> mesas = new Gson().fromJson(json, listaMesas);
        return mesas;
    }

    private static Mesa parserMesaJSON(String json) {
        try {
            return new Gson().fromJson(json, Mesa.class);
        } catch (Exception ex) {
            Log.e(TAG, json);
        }
        return new Mesa();
    }

    private static Servico parserServicoJSON(String json) {
        try {
            return new Gson().fromJson(json, Servico.class);
        } catch (Exception ex) {
            Log.e(TAG, json);
        }
        return new Servico();
    }

    private static String parserJSONMesa(Context contexto, Mesa mesa) {
        return parserJSONMesa(contexto, mesa, null);
    }

    private static String parserJSONMesaCancelamento(Context contexto, Mesa mesa) {
        List<Item> itens = new ArrayList<>();
        if (mesa.getReceita() != null) {
            for (ReceitaVendaProduto rvp : mesa.getReceita().getListaReceitaProdutoCancelado()) {
                Item item = getItem(rvp);
                item.setCancelado(true);
                itens.add(item);
            }
            for (ReceitaServicoServico rss : mesa.getReceita().getReceitaServico().getListaReceitaServicoServico()) {
                Item item = getItem(rss);
                item.setCancelado(true);
                itens.add(item);
            }
        }
        MesaProduto mp = getMesaProduto(contexto, mesa, itens);
        return new Gson().toJson(mp);
    }

    private static Mesa dividirPreco(Mesa mesa) {
        for (ReceitaVendaProduto rvp : mesa.getReceita().getReceitaVenda().getListaReceitaVendaProduto()) {
             rvp.setValor(rvp.getValor().divide(BigDecimal.valueOf(rvp.getQuantidade())));
        }
        return mesa;
    }

    public static String parserJSONMesa(Context contexto, Mesa mesa, Servico servico) {
        List<Item> itens = new ArrayList<>();
        if (mesa.getReceita() != null) {
            for (ReceitaVendaProduto rvp : mesa.getReceita().getReceitaVenda().getListaReceitaVendaProduto()) {
                Item item = getItem(rvp);
                itens.add(item);
            }
            for (ReceitaServicoServico rss : mesa.getReceita().getReceitaServico().getListaReceitaServicoServico()) {
                Item item = servico != null ? getItem(rss, servico) : getItem(rss);
                itens.add(item);
            }
        }
        MesaProduto mp = getMesaProduto(contexto, mesa, itens);
        return new Gson().toJson(mp);
    }

    public static Map<String, String> getParametros(String json) {
        Map<String, String> parametros = new HashMap<>();
        parametros.put(PARAM_MESA, json);
        return parametros;
    }

    private static Item getItem(ReceitaVendaProduto rvp) {
        Item item = new Item();
        item.setEnviadoProducao(rvp.getProduto().isEnviadoProducao());
        item.setIdItem(rvp.getProduto().getId().intValue());
        item.setNome(rvp.getProduto().getNome());
        item.setPreco(rvp.getValor().setScale(2, BigDecimal.ROUND_HALF_EVEN));
        item.setUnidadeMedida(rvp.getProduto().getUnidadeMedida().getSigla());
        item.setTipoItem(StringUtils.TIPO_PRODUTO);
        item.setQuantidade(rvp.getQuantidade().intValue());
        item.setSubTotal(rvp.getValor().setScale(2, BigDecimal.ROUND_HALF_EVEN));
        return item;
    }

    private static Item getItem(ReceitaServicoServico rss, Servico servico) {
        Item item = getItem(rss);
        item.setIdItem(Integer.parseInt(servico.getId()));
        return item;
    }

    private static Item getItem(ReceitaServicoServico rss) {
        Item item = new Item();
        item.setIdItem(rss.getId());
        item.setPreco(rss.getValor().setScale(2, BigDecimal.ROUND_HALF_EVEN));
        item.setTipoItem(StringUtils.TIPO_SERVICO);
        item.setQuantidade(1);
        item.setSubTotal(rss.getValor().setScale(2, BigDecimal.ROUND_HALF_EVEN));
        return item;
    }

    private static MesaProduto getMesaProduto(Context contexto, Mesa mesa, List<Item> itensReceita) {
        MesaProduto mp = new MesaProduto();
        mp.setCancelamentoTotal(mesa.getCancelamentoTotal() != null ? mesa.getCancelamentoTotal() : false);
        mp.setNumeroMesa(mesa.getNumeroMesa());
        mp.setIdGarcon(mesa.getIdGarcon() != null ? mesa.getIdGarcon() : ECGEFoodsUtils.getUsuarioLogado(contexto).getId().intValue());
        mp.setItensReceita(itensReceita);
        mp.setStatus(mesa.getStatus());
        return mp;
    }
}
