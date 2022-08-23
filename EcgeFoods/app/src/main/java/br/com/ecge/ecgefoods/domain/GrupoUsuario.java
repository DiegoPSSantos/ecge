package br.com.ecge.ecgefoods.domain;

import org.parceler.Parcel;

@Parcel
public class GrupoUsuario {

    protected Long id;
    protected String descricao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
