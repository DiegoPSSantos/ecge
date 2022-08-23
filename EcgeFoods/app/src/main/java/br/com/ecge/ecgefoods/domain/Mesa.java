package br.com.ecge.ecgefoods.domain;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class Mesa {

    private static final long serialVersionUID = 1L;
    protected Integer id;
    protected String numeroMesa;
    protected String status;
    protected Integer idGarcon;
    protected Boolean cancelamentoTotal;
    protected Receita receita;
    protected List<Pedido> pedidos;
    protected long dataAlteracao;
    protected long dataAbertura;
    protected boolean mesaEnviadaBackend;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumeroMesa() {
        return numeroMesa;
    }

    public void setNumeroMesa(String numeroMesa) {
        this.numeroMesa = numeroMesa;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getIdGarcon() {
        return idGarcon;
    }

    public void setIdGarcon(Integer idGarcon) {
        this.idGarcon = idGarcon;
    }

    public Boolean getCancelamentoTotal() {
        return cancelamentoTotal;
    }

    public void setCancelamentoTotal(Boolean cancelamentoTotal) {
        this.cancelamentoTotal = cancelamentoTotal;
    }

    public Receita getReceita() {
        return receita;
    }

    public void setReceita(Receita receita) {
        this.receita = receita;
    }

    public boolean isMesaEnviadaBackend() {
        return mesaEnviadaBackend;
    }

    public void setMesaEnviadaBackend(boolean mesaEnviadaBackend) {
        this.mesaEnviadaBackend = mesaEnviadaBackend;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public long getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataAlteracao(long dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    public long getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(long dataAbertura) {
        this.dataAbertura = dataAbertura;
    }
}
