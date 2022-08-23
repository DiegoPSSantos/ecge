package br.com.ecge.ecgefoods.domain;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class Usuario {

    protected Long id;
    protected String login;
    protected String senha;
    protected Pessoa pessoa;
    protected List<DescontoMaximo> descontosMaximo;
    protected String acoesPermitidas;
    protected List<String> acoes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public List<DescontoMaximo> getDescontosMaximo() {
        return descontosMaximo;
    }

    public void setDescontosMaximo(List<DescontoMaximo> descontosMaximo) {
        this.descontosMaximo = descontosMaximo;
    }

    public String getAcoesPermitidas() {
        return acoesPermitidas;
    }

    public void setAcoesPermitidas(String acoesPermitidas) {
        this.acoesPermitidas = acoesPermitidas;
    }

    public List<String> getAcoes() {
        return acoes;
    }

    public void setAcoes(List<String> acoes) {
        this.acoes = acoes;
    }
}
