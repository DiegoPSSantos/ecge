package br.com.ecge.ecgefoods.domain;

import java.math.BigDecimal;

public class Servico {

    protected String id;
    protected String nome;
    protected boolean ativo;
    protected BigDecimal valorServico;
    protected boolean valorEstabelecido;
    protected boolean valorPercentual;
    protected Integer versao;
    protected String codigo;
    protected BigDecimal precoAtual;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public BigDecimal getValorServico() {
        return valorServico;
    }

    public void setValorServico(BigDecimal valorServico) {
        this.valorServico = valorServico;
    }

    public boolean isValorEstabelecido() {
        return valorEstabelecido;
    }

    public void setValorEstabelecido(boolean valorEstabelecido) {
        this.valorEstabelecido = valorEstabelecido;
    }

    public boolean isValorPercentual() {
        return valorPercentual;
    }

    public void setValorPercentual(boolean valorPercentual) {
        this.valorPercentual = valorPercentual;
    }

    public Integer getVersao() {
        return versao;
    }

    public void setVersao(Integer versao) {
        this.versao = versao;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public BigDecimal getPrecoAtual() {
        return precoAtual;
    }

    public void setPrecoAtual(BigDecimal precoAtual) {
        this.precoAtual = precoAtual;
    }
}
