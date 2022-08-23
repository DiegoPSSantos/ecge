package br.com.ecge.ecgefoods.domain;

import org.parceler.Parcel;

import java.math.BigDecimal;
import java.util.List;

@Parcel
public class Receita {

    protected Integer id;
    protected Cliente cliente;
    protected String status;
    protected Integer idEntidade;
    protected PessoaExpedicao pessoaExpedicao;
    protected ReceitaVenda receitaVenda;
    protected ReceitaServico receitaServico;
    protected List<ReceitaCentroCusto> listaReceitaCentroCusto;
    protected List<ItemReceita> itensReceita;
    protected List<ItemReceita> itensCanceladosReceita;
    protected List<ReceitaVendaProduto> listaReceitaProdutoCancelado;
    protected BigDecimal valorTotalProdutos;
    protected BigDecimal valorTotalServicos;
    protected BigDecimal valorTotal;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PessoaExpedicao getPessoaExpedicao() {
        return pessoaExpedicao;
    }

    public void setPessoaExpedicao(PessoaExpedicao pessoaExpedicao) {
        this.pessoaExpedicao = pessoaExpedicao;
    }

    public List<ItemReceita> getItensReceita() {
        return itensReceita;
    }

    public void setItensReceita(List<ItemReceita> itensReceita) {
        this.itensReceita = itensReceita;
    }

    public List<ItemReceita> getItensCanceladosReceita() {
        return itensCanceladosReceita;
    }

    public void setItensCanceladosReceita(List<ItemReceita> itensCanceladosReceita) {
        this.itensCanceladosReceita = itensCanceladosReceita;
    }

    public BigDecimal getValorTotalProdutos() {
        return valorTotalProdutos;
    }

    public void setValorTotalProdutos(BigDecimal valorTotalProdutos) {
        this.valorTotalProdutos = valorTotalProdutos;
    }

    public BigDecimal getValorTotalServicos() {
        return valorTotalServicos;
    }

    public void setValorTotalServicos(BigDecimal valorTotalServicos) {
        this.valorTotalServicos = valorTotalServicos;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Integer getIdEntidade() {
        return idEntidade;
    }

    public void setIdEntidade(Integer idEntidade) {
        this.idEntidade = idEntidade;
    }

    public ReceitaVenda getReceitaVenda() {
        return receitaVenda;
    }

    public void setReceitaVenda(ReceitaVenda receitaVenda) {
        this.receitaVenda = receitaVenda;
    }

    public ReceitaServico getReceitaServico() {
        return receitaServico;
    }

    public void setReceitaServico(ReceitaServico receitaServico) {
        this.receitaServico = receitaServico;
    }

    public List<ReceitaCentroCusto> getListaReceitaCentroCusto() {
        return listaReceitaCentroCusto;
    }

    public void setListaReceitaCentroCusto(List<ReceitaCentroCusto> listaReceitaCentroCusto) {
        this.listaReceitaCentroCusto = listaReceitaCentroCusto;
    }

    public List<ReceitaVendaProduto> getListaReceitaProdutoCancelado() {
        return listaReceitaProdutoCancelado;
    }

    public void setListaReceitaProdutoCancelado(List<ReceitaVendaProduto> listaReceitaProdutoCancelado) {
        this.listaReceitaProdutoCancelado = listaReceitaProdutoCancelado;
    }
}

