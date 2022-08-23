package br.com.ecge.ecgefoods.domain;

import org.parceler.Parcel;

import java.math.BigDecimal;

@Parcel
public class ItemReceita {

    protected String tipoItem;
    protected int nr_item;
    protected Long idItem;
    protected String precoStr;
    protected String nome;
    protected String codigoItem;
    protected boolean cancelado;
    protected boolean enviadoProducao;
    protected Integer quantidade;
    protected BigDecimal desconto;
    protected BigDecimal subTotal;
    protected BigDecimal preco;
    protected String unidadeMedida;


    public String getTipoItem() {
        return tipoItem;
    }

    public void setTipoItem(String tipoItem) {
        this.tipoItem = tipoItem;
    }

    public int getNr_item() {
        return nr_item;
    }

    public void setNr_item(int nr_item) {
        this.nr_item = nr_item;
    }

    public Long getIdItem() {
        return idItem;
    }

    public void setIdItem(Long idItem) {
        this.idItem = idItem;
    }

    public String getPrecoStr() {
        return precoStr;
    }

    public void setPrecoStr(String precoStr) {
        this.precoStr = precoStr;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigoItem() {
        return codigoItem;
    }

    public void setCodigoItem(String codigoItem) {
        this.codigoItem = codigoItem;
    }

    public boolean isCancelado() {
        return cancelado;
    }

    public void setCancelado(boolean cancelado) {
        this.cancelado = cancelado;
    }

    public boolean isEnviadoProducao() {
        return enviadoProducao;
    }

    public void setEnviadoProducao(boolean enviadoProducao) {
        this.enviadoProducao = enviadoProducao;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
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
}
