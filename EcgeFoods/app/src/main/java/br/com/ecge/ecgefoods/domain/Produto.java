package br.com.ecge.ecgefoods.domain;

import org.parceler.Parcel;

import java.math.BigDecimal;

@Parcel
public class Produto {

    protected Long id;
    protected String nome;
    protected boolean ativo;
    protected boolean enviadoProducao;
    protected boolean selecionado;
    protected BigDecimal precoAtual;
    protected UnidadeMedida unidadeMedida;
    protected String codigo;
    protected int idGrupoProduto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public boolean isEnviadoProducao() {
        return enviadoProducao;
    }

    public void setEnviadoProducao(boolean enviadoProducao) {
        this.enviadoProducao = enviadoProducao;
    }

    public boolean isSelecionado() {
        return selecionado;
    }

    public void setSelecionado(boolean selecionado) {
        this.selecionado = selecionado;
    }

    public BigDecimal getPrecoAtual() {
        return precoAtual;
    }

    public void setPrecoAtual(BigDecimal precoAtual) {
        this.precoAtual = precoAtual;
    }

    public UnidadeMedida getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getIdGrupoProduto() {
        return idGrupoProduto;
    }

    public void setIdGrupoProduto(int idGrupoProduto) {
        this.idGrupoProduto = idGrupoProduto;
    }
}
