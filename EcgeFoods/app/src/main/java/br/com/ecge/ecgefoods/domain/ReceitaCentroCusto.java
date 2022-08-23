package br.com.ecge.ecgefoods.domain;

import org.parceler.Parcel;

@Parcel
public class ReceitaCentroCusto {

    protected Integer id;
    protected Integer idCentroCusto;
    protected Double percentual;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdCentroCusto() {
        return idCentroCusto;
    }

    public void setIdCentroCusto(Integer idCentroCusto) {
        this.idCentroCusto = idCentroCusto;
    }

    public Double getPercentual() {
        return percentual;
    }

    public void setPercentual(Double percentual) {
        this.percentual = percentual;
    }
}
