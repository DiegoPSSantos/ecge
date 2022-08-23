package br.com.ecge.ecgefoods.service;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import br.com.ecge.ecgefoods.domain.Impressora;
import br.com.ecge.ecgefoods.utils.ConfigUtils;
import br.com.ecge.ecgefoods.utils.HttpUtils;

public class ImpressoraService {

    private static final String URL_IMPRESSORA_LISTA = "/mesa/listarImpressoras";

    public static List<Impressora> getImpressoras(Context contexto) throws IOException {
        HttpUtils http = new HttpUtils();
        String json = http.doGet(ConfigUtils.getEndpoint(contexto) + URL_IMPRESSORA_LISTA);
        List<Impressora> impressoras = parserImpressorasJSON(json);
        return impressoras;
    }

    private static List<Impressora> parserImpressorasJSON(String json) {
        Type listaImpressoras = new TypeToken<List<Impressora>>(){}.getType();
        List<Impressora> impressoras = new Gson().fromJson(json, listaImpressoras);
        return impressoras;
    }
}
