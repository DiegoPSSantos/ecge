package br.com.ecge.ecgefoods.service;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;

import br.com.ecge.ecgefoods.domain.Pessoa;
import br.com.ecge.ecgefoods.utils.ConfigUtils;
import br.com.ecge.ecgefoods.utils.HttpUtils;

public class PessoaService {

    private static final String URL_PESSOA = "/pessoa/";

    public static Pessoa getPessoa(Context contexto, Integer id) throws IOException {
        HttpUtils http = new HttpUtils();
        String json = http.doGet(ConfigUtils.getEndpoint(contexto) + URL_PESSOA + id);
        Pessoa pessoa = parserPessoaJSON(json);
        if (pessoa == null) {
            pessoa = new Pessoa();
        }
        return pessoa;
    }

    private static Pessoa parserPessoaJSON(String json) {
        return new Gson().fromJson(json, Pessoa.class);
    }
}
