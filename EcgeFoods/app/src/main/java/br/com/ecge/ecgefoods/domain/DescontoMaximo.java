package br.com.ecge.ecgefoods.domain;

import org.parceler.Parcel;

import java.math.BigDecimal;
import java.util.List;

@Parcel
public class DescontoMaximo {

    protected Long id;
    protected BigDecimal valorMaximoDesconto;
    protected String valorMaximoDescontoString;
    protected List<GrupoUsuario> grupos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValorMaximoDesconto() {
        return valorMaximoDesconto;
    }

    public void setValorMaximoDesconto(BigDecimal valorMaximoDesconto) {
        this.valorMaximoDesconto = valorMaximoDesconto;
    }

    public List<GrupoUsuario> getGrupos() {
        return grupos;
    }

    public void setGrupos(List<GrupoUsuario> grupos) {
        this.grupos = grupos;
    }

    public String getValorMaximoDescontoString() {
        return valorMaximoDescontoString;
    }

    public void setValorMaximoDescontoString(String valorMaximoDescontoString) {
        this.valorMaximoDescontoString = valorMaximoDescontoString;
    }
}
