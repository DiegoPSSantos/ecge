package br.com.ecge.ecgefoods.domain;

import java.math.BigDecimal;
import java.util.List;

public class Item {

    private int nr_item;
    private boolean enviadoProducao;
    private int idGarcomLancamento;
    private int idItem;
    private String nome;
    private BigDecimal preco;
    private String unidadeMedida;
    private int quantidade;
    private BigDecimal subTotal;
    private String tipoItem;
    private boolean cancelado;
    private String codigoItem;
    private String observacao;
    private List<Observacao> listaObservacoes;

    public List<Observacao> getListaObservacoes() {
        return listaObservacoes;
    }

    public void setListaObservacoes(List<Observacao> listaObservacoes) {
        this.listaObservacoes = listaObservacoes;
    }

    public int getNr_item() {
        return nr_item;
    }

    public void setNr_item(int nr_item) {
        this.nr_item = nr_item;
    }

    public boolean isEnviadoProducao() {
        return enviadoProducao;
    }

    public void setEnviadoProducao(boolean enviadoProducao) {
        this.enviadoProducao = enviadoProducao;
    }

    public int getIdGarcomLancamento() {
        return idGarcomLancamento;
    }

    public void setIdGarcomLancamento(int idGarcomLancamento) {
        this.idGarcomLancamento = idGarcomLancamento;
    }

    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public String getTipoItem() {
        return tipoItem;
    }

    public void setTipoItem(String tipoItem) {
        this.tipoItem = tipoItem;
    }

    public boolean isCancelado() {
        return cancelado;
    }

    public void setCancelado(boolean cancelado) {
        this.cancelado = cancelado;
    }

    public String getCodigoItem() {
        return codigoItem;
    }

    public void setCodigoItem(String codigoItem) {
        this.codigoItem = codigoItem;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

}
