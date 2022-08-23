package br.com.ecge.ecgefoods.domain;

import java.util.List;

public class MesaProduto {

    private boolean cancelamentoTotal;
    private String numeroMesa;
    private String impressoraFechamento;
    private int idGarcon;
    private List<Item> itensReceita;
    private List<Item> itensCanceladosReceita;
    private String status;

    public boolean isCancelamentoTotal() {
        return cancelamentoTotal;
    }

    public void setCancelamentoTotal(boolean cancelamentoTotal) {
        this.cancelamentoTotal = cancelamentoTotal;
    }

    public String getNumeroMesa() {
        return numeroMesa;
    }

    public void setNumeroMesa(String numeroMesa) {
        this.numeroMesa = numeroMesa;
    }

    public int getIdGarcon() {
        return idGarcon;
    }

    public void setIdGarcon(int idGarcon) {
        this.idGarcon = idGarcon;
    }

    public List<Item> getItensReceita() {
        return itensReceita;
    }

    public void setItensReceita(List<Item> itensReceita) {
        this.itensReceita = itensReceita;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Item> getItensCanceladosReceita() {
        return itensCanceladosReceita;
    }

    public void setItensCanceladosReceita(List<Item> itensCanceladosReceita) {
        this.itensCanceladosReceita = itensCanceladosReceita;
    }

    public String getImpressoraFechamento() {
        return impressoraFechamento;
    }

    public void setImpressoraFechamento(String impressoraFechamento) {
        this.impressoraFechamento = impressoraFechamento;
    }
}
