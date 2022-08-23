package br.com.ecge.ecgefoods.domain;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class ReceitaVenda {

    protected Integer id;
    protected List<ReceitaVendaProduto> listaReceitaVendaProduto;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<ReceitaVendaProduto> getListaReceitaVendaProduto() {
        return listaReceitaVendaProduto;
    }

    public void setListaReceitaVendaProduto(List<ReceitaVendaProduto> listaReceitaVendaProduto) {
        this.listaReceitaVendaProduto = listaReceitaVendaProduto;
    }
}
