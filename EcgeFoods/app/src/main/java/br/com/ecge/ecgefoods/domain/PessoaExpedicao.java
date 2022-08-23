package br.com.ecge.ecgefoods.domain;

import org.parceler.Parcel;

@Parcel
public class PessoaExpedicao {

    protected Long id;
    protected String nome;

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
}
