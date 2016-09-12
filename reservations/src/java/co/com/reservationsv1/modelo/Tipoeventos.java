/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.reservationsv1.modelo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author disable
 */
@Entity
@Table(name = "tipoeventos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tipoeventos.findAll", query = "SELECT t FROM Tipoeventos t"),
    @NamedQuery(name = "Tipoeventos.findById", query = "SELECT t FROM Tipoeventos t WHERE t.id = :id"),
    @NamedQuery(name = "Tipoeventos.findByNombre", query = "SELECT t FROM Tipoeventos t WHERE t.nombre = :nombre"),
    @NamedQuery(name = "Tipoeventos.findByEstado", query = "SELECT t FROM Tipoeventos t WHERE t.estado = :estado")})
public class Tipoeventos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "estado")
    private int estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idtipoevento")
    private List<Reservas> reservasList;

    public Tipoeventos() {
    }

    public Tipoeventos(Integer id) {
        this.id = id;
    }

    public Tipoeventos(Integer id, int estado) {
        this.id = id;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    @XmlTransient
    public List<Reservas> getReservasList() {
        return reservasList;
    }

    public void setReservasList(List<Reservas> reservasList) {
        this.reservasList = reservasList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tipoeventos)) {
            return false;
        }
        Tipoeventos other = (Tipoeventos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.reservationsv1.modelo.Tipoeventos[ id=" + id + " ]";
    }
    
}
