package com.melit.mycars.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Incidente.
 */
@Entity
@Table(name = "incidente")
public class Incidente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fallo")
    private String fallo;

    @Column(name = "descripcion")
    private String descripcion;

    @ManyToOne
    @JsonIgnoreProperties("incidentes")
    private Coche coche;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFallo() {
        return fallo;
    }

    public Incidente fallo(String fallo) {
        this.fallo = fallo;
        return this;
    }

    public void setFallo(String fallo) {
        this.fallo = fallo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Incidente descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Coche getCoche() {
        return coche;
    }

    public Incidente coche(Coche coche) {
        this.coche = coche;
        return this;
    }

    public void setCoche(Coche coche) {
        this.coche = coche;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Incidente)) {
            return false;
        }
        return id != null && id.equals(((Incidente) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Incidente{" +
            "id=" + getId() +
            ", fallo='" + getFallo() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
