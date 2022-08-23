package br.com.ecge.ecgefoods.domain;

import org.parceler.Parcel;

@Parcel
public class UnidadeMedida {

    protected Long id;
    protected String nome;
    protected String sigla;

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

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }
}
