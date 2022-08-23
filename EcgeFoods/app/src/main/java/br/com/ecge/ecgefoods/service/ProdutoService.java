package br.com.ecge.ecgefoods.service;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import br.com.ecge.ecgefoods.domain.Categoria;
import br.com.ecge.ecgefoods.domain.Observacao;
import br.com.ecge.ecgefoods.domain.Produto;
import br.com.ecge.ecgefoods.utils.ConfigUtils;
import br.com.ecge.ecgefoods.utils.ECGEFoodsUtils;
import br.com.ecge.ecgefoods.utils.HttpUtils;

public class ProdutoService {


    private static final String URL_PRODUTOS_GRUPO = "/produto/grupo/";
    private static final String URL_OBSERVACOES = "/produto/grupos/observacoesPorGrupo/";


    public static List<Produto> getProdutos(Categoria categoria, Context contexto) throws IOException {
        HttpUtils http = new HttpUtils();
        String json = http.doGet(ConfigUtils.getEndpoint(contexto) + URL_PRODUTOS_GRUPO + categoria.getId());
        List<Produto> produtos = parserProdutoJSON(json);
        return produtos;
    }

    public static List<Observacao> getObservacoes(Produto produto, Context contexto) throws IOException {
        HttpUtils http = new HttpUtils();
        String json = http.doGet(ConfigUtils.getEndpoint(contexto) + URL_OBSERVACOES + produto.getIdGrupoProduto());
        List<Observacao> observacoes = parsetObservacoesJSON(json);
        return observacoes;
    }

    private static List<Produto> parserProdutoJSON(String json) {
        Type listaProduto = new TypeToken<List<Produto>>() {
        }.getType();
        List<Produto> produtos = new Gson().fromJson(json, listaProduto);
        produtos = ECGEFoodsUtils.ordenarProdutos(produtos);
        return produtos;
    }

    private static List<Observacao> parsetObservacoesJSON(String json) {
        Type listaObservacoes = new TypeToken<List<Observacao>>() {}.getType();
        List<Observacao> observacoes = new Gson().fromJson(json, listaObservacoes);
        observacoes = ECGEFoodsUtils.ordenarObservacoes(observacoes);
        return observacoes;
    }
}
