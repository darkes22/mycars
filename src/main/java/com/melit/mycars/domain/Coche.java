package com.melit.mycars.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Coche.
 */
@Entity
@Table(name = "coche")
public class Coche implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "marca")
    private String marca;

    @Column(name = "modelo")
    private String modelo;

    
    @Column(name = "matricula", unique = true)
    private String matricula;

    @Column(name = "precio")
    private Float precio;

    @Column(name = "vendido")
    private Boolean vendido;

    @Column(name = "fechaventa")
    private LocalDate fechaventa;
    
    @Column(name = "owner")
    private String owner;

    public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	// jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public Coche marca(String marca) {
        this.marca = marca;
        return this;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public Coche modelo(String modelo) {
        this.modelo = modelo;
        return this;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMatricula() {
        return matricula;
    }

    public Coche matricula(String matricula) {
        this.matricula = matricula;
        return this;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Float getPrecio() {
        return precio;
    }

    public Coche precio(Float precio) {
        this.precio = precio;
        return this;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public Boolean isVendido() {
        return vendido;
    }

    public Coche vendido(Boolean vendido) {
        this.vendido = vendido;
        return this;
    }

    public void setVendido(Boolean vendido) {
        this.vendido = vendido;
    }

    public LocalDate getFechaventa() {
        return fechaventa;
    }

    public Coche fechaventa(LocalDate fechaventa) {
        this.fechaventa = fechaventa;
        return this;
    }

    public void setFechaventa(LocalDate fechaventa) {
        this.fechaventa = fechaventa;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Coche)) {
            return false;
        }
        return id != null && id.equals(((Coche) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Coche{" +
            "id=" + getId() +
            ", marca='" + getMarca() + "'" +
            ", modelo='" + getModelo() + "'" +
            ", matricula='" + getMatricula() + "'" +
            ", precio=" + getPrecio() +
            ", vendido='" + isVendido() + "'" +
            ", fechaventa='" + getFechaventa() + "'" +
            ", owner='" + getOwner() + "'" +
            "}";
    }
}
