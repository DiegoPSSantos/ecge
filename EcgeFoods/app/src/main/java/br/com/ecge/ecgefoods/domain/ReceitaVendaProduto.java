package br.com.ecge.ecgefoods.domain;

import org.parceler.Parcel;

import java.math.BigDecimal;

@Parcel
public class ReceitaVendaProduto {

    protected Integer id;
    protected Produto produto;
    protected Double quantidade;
    protected String quantidadeStr;
    protected BigDecimal valor;
    protected String valorStr;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }

    public String getQuantidadeStr() {
        return quantidadeStr;
    }

    public void setQuantidadeStr(String quantidadeStr) {
        this.quantidadeStr = quantidadeStr;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getValorStr() {
        return valorStr;
    }

    public void setValorStr(String valorStr) {
        this.valorStr = valorStr;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }
}
