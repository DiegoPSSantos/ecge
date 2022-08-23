package br.com.ecge.ecgefoods.domain;

public class Impressora {

    private static final long serialVersionUID = 1L;
    private String descricao;
    private String configuracao;


    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getConfiguracao() {
        return configuracao;
    }

    public void setConfiguracao(String configuracao) {
        this.configuracao = configuracao;
    }
}
