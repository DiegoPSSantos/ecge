package br.com.ecge.ecgefoods.domain;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class ReceitaServico {

    protected Integer id;
    protected List<ReceitaServicoServico> listaReceitaServicoServico;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<ReceitaServicoServico> getListaReceitaServicoServico() {
        return listaReceitaServicoServico;
    }

    public void setListaReceitaServicoServico(List<ReceitaServicoServico> listaReceitaServicoServico) {
        this.listaReceitaServicoServico = listaReceitaServicoServico;
    }
}
