package br.com.ecge.ecgefoods.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

public class ConfigUtils {

    private static final String PREF_ID = "ecge_foods";


    public static String getProperty(String chave, Context contexto, String arquivo) throws IOException {
        Properties properties = new Properties();
        AssetManager am = contexto.getAssets();
        InputStream is = am.open(arquivo);
        properties.load(is);
        return properties.getProperty(chave);
    }

    public static void setIPPortaBackend(Context contexto, String ip, String porta) {
        SharedPreferences preferences = contexto.getSharedPreferences(PREF_ID, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ip", ip);
        editor.putString("porta", porta);
        editor.commit();
    }

    public static String getIPBackend(Context contexto) {
        SharedPreferences preferences = contexto.getSharedPreferences(PREF_ID, 0);
        return preferences.getString("ip", StringUtils.VAZIO);
    }

    public static String getPortaBackend(Context contexto) {
        SharedPreferences preferences = contexto.getSharedPreferences(PREF_ID, 0);
        return preferences.getString("porta", StringUtils.VAZIO);
    }

    public static String getEndpoint(Context contexto) {
        return getIPBackend(contexto) + ":" + getPortaBackend(contexto);
    }

    public static String getImpressora(Context contexto) {
        SharedPreferences preferences = contexto.getSharedPreferences(PREF_ID, 0);
        return preferences.getString(StringUtils.IMPRESSORA_CONFIGURACAO, StringUtils.VAZIO);
    }

    public static void setUsuario(Context contexto, Integer id, String nome) {
        SharedPreferences preferences = contexto.getSharedPreferences(PREF_ID, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(StringUtils.ID, id);
        editor.putString(StringUtils.NOME, nome);
        editor.commit();
    }

    public static void removerUsuario(Context contexto) {
        SharedPreferences preferences = contexto.getSharedPreferences(PREF_ID, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(StringUtils.ID);
        editor.remove(StringUtils.NOME);
        editor.remove(StringUtils.ACOES);
        editor.commit();
    }

    public static void setImpressora(Context contexto, String configuracao) {
        SharedPreferences preferences = contexto.getSharedPreferences(PREF_ID, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString (StringUtils.IMPRESSORA_CONFIGURACAO, configuracao);
        editor.commit();
    }

    public static void removerImpressora(Context contexto) {
        SharedPreferences preferences = contexto.getSharedPreferences(PREF_ID, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(StringUtils.IMPRESSORA_CONFIGURACAO);
        editor.commit();
    }

    public static void setListaSession(Context contexto, List<String> lista, String chave) {
        SharedPreferences preferences = contexto.getSharedPreferences(PREF_ID, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(chave, new HashSet<>(lista));
    }

    public static List<String> getListaSession(Context contexto, String chave) {
        SharedPreferences preferences = contexto.getSharedPreferences(PREF_ID, 0);
        return new ArrayList<>(preferences.getStringSet(chave, new HashSet<>()));
    }

    public static String getString(Context contexto, String chave) {
        SharedPreferences preferences = contexto.getSharedPreferences(PREF_ID, 0);
        return preferences.getString(chave, StringUtils.VAZIO);
    }

    public static int getInt(Context contexto, String chave) {
        SharedPreferences preferences = contexto.getSharedPreferences(PREF_ID, 0);
        return preferences.getInt(chave, StringUtils.ZERO);
    }

    public static boolean verificarConexao(Context contexto) {
        ConnectivityManager manager = (ConnectivityManager) contexto.getSystemService(Context.CONNECTIVITY_SERVICE);

        return manager.getActiveNetworkInfo() != null &&
                manager.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
