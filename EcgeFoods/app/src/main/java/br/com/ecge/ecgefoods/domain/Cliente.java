package br.com.ecge.ecgefoods.domain;

import org.parceler.Parcel;

@Parcel
public class Cliente {

    protected Integer id;
    protected long dataCadatro;
    protected boolean ativo;
    protected boolean excluido;
    protected String tipoPessoa;
    protected String nome;
    protected String cpf;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public long getDataCadatro() {
        return dataCadatro;
    }

    public void setDataCadatro(long dataCadatro) {
        this.dataCadatro = dataCadatro;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public boolean isExcluido() {
        return excluido;
    }

    public void setExcluido(boolean excluido) {
        this.excluido = excluido;
    }

    public String getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(String tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
