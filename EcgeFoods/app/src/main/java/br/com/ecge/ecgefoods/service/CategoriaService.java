package br.com.ecge.ecgefoods.service;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import br.com.ecge.ecgefoods.domain.Categoria;
import br.com.ecge.ecgefoods.utils.ConfigUtils;
import br.com.ecge.ecgefoods.utils.ECGEFoodsUtils;
import br.com.ecge.ecgefoods.utils.HttpUtils;

public class CategoriaService {

    private static final String URL_CATEGORIAS = "/produto/grupos";

    public static List<Categoria> getCategorias(Context contexto) throws IOException {
        HttpUtils http = new HttpUtils();
        String json = http.doGet(ConfigUtils.getEndpoint(contexto) + URL_CATEGORIAS);
        List<Categoria> categorias = parserCategoriaJSON(json);
        return categorias;
    }

    private static List<Categoria> parserCategoriaJSON(String json) {
        Type listaCategoria = new TypeToken<List<Categoria>>() {
        }.getType();
        List<Categoria> categorias = new Gson().fromJson(json, listaCategoria);
        categorias = ECGEFoodsUtils.ordenarCategorias(categorias);
        return categorias;
    }
}
