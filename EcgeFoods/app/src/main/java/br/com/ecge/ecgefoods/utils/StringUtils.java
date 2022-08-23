package br.com.ecge.ecgefoods.utils;

import com.annimon.stream.Stream;

import java.math.BigDecimal;
import java.util.List;

public class StringUtils {

    public static final String PONTO_VIRGULA = ";";
    public static final String ESPACO = " ";
    public static final String VAZIO = "";
    public static final String DOIS_PONTOS = ":";
    public static final String IGUAL = "=";
    public static final int ZERO = 0;
    public static final String ACOES = "acoesUsuario";
    public static final String ADMIN_USER = "admin";
    public static final String ADMIN_PASSWORD = "@dminscge2016";
    public static final String ID = "idUsuario";
    public static final String NOME = "nomeUsuario";
    public static final String IMPRESSORA_CONFIGURACAO = "configImpressora";
    public static final String ENCODE_UTF8 = "UTF-8";
    public static final String TIPO_PRODUTO = "PRODUTO";
    public static final String TIPO_SERVICO = "SERVICO";
    public static final String TYPE_JSON = "application/json";

    public static final Double convertDouble(String valor) {
        valor = valor.trim().replaceAll(",", ".");
        return Double.parseDouble(valor);
    }

    public static final BigDecimal convertValorMonetario(String valor) {
        Double doubleValor = convertDouble(valor);
        return new BigDecimal(doubleValor);
    }

    public static List<String> getListString(String valor, String divisor) {
        return Stream.of(valor.split(divisor)).sorted().toList();
    }

    public static String getProtocoloIP(String urlFull) {
        int lastIndexDoisPontos = urlFull.lastIndexOf(DOIS_PONTOS);
        return urlFull.substring(ZERO, lastIndexDoisPontos);
    }

    public static String getPorta(String urlFull) {
        int lastIndexDoisPontos = urlFull.lastIndexOf(DOIS_PONTOS) + 1;
        return urlFull.substring(lastIndexDoisPontos);
    }

    public static String getMapaSemChave(String mapa) {
        if (mapa.contains(IGUAL)) {
            return mapa.trim().split(IGUAL)[1];
        } else {
            return mapa;
        }
    }

    public static String getValorFormatoDinheiro(BigDecimal valor) {
        BigDecimal novoValor = valor.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        return novoValor.toString().replace(".", ",");
    }
}
