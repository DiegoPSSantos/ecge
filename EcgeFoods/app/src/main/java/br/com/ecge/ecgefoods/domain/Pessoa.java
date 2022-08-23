package br.com.ecge.ecgefoods.domain;

import org.parceler.Parcel;

@Parcel
public class Pessoa {

    protected Integer id;
    protected String nome;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
