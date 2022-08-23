package br.com.ecge.ecgefoods.service;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import br.com.ecge.ecgefoods.domain.Usuario;
import br.com.ecge.ecgefoods.utils.ConfigUtils;
import br.com.ecge.ecgefoods.utils.HttpUtils;

public class LoginService {

    private static final String URL_AUTENTICA = "/usuario";
    private static final String CHARSET= "UTF-8";

    public static Usuario autenticar(Context contexto, String usuario, String senha) throws IOException {
        HttpUtils http = new HttpUtils();
        String json = http.doGet(ConfigUtils.getEndpoint(contexto)+ URL_AUTENTICA, getParametros(usuario, senha), CHARSET);
        Usuario user = parserUsuarioJSON(json);
        return user;
    }

    private static Map<String, String> getParametros(String usuario, String senha) {
        Map<String , String> parametros = new HashMap<>();
        parametros.put("login", usuario);
        parametros.put("senha", senha);
        return parametros;
    }

    private static Usuario parserUsuarioJSON(String json) {
        Usuario usuario = new Gson().fromJson(json, Usuario.class);
        return usuario;
    }

}
