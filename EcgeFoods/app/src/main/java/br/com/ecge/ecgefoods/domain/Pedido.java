package br.com.ecge.ecgefoods.domain;

import org.parceler.Parcel;

import java.math.BigDecimal;

@Parcel
public class Pedido {

    protected Long id;
    protected String produto;
    protected int quantidade;
    protected BigDecimal preco;
    protected BigDecimal total;
    protected Long idProduto;
    protected Integer nr_mesa;
    protected String unidadeMedida;
    protected boolean enviadoProducao;
    protected String codigo;
    protected String observacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Long idProduto) {
        this.idProduto = idProduto;
    }

    public Integer getNr_mesa() {
        return nr_mesa;
    }

    public void setNr_mesa(Integer nr_mesa) {
        this.nr_mesa = nr_mesa;
    }

    public boolean isEnviadoProducao() {
        return enviadoProducao;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public boolean getEnviadoProducao() {
        return enviadoProducao;
    }

    public void setEnviadoProducao(boolean enviadoProducao) {
        this.enviadoProducao = enviadoProducao;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
