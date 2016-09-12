/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.reservationsv1.modelo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author disable
 */
@Entity
@Table(name = "reservas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Reservas.findAll", query = "SELECT r FROM Reservas r"),
    @NamedQuery(name = "Reservas.findById", query = "SELECT r FROM Reservas r WHERE r.id = :id"),
    @NamedQuery(name = "Reservas.findByNombredependencia", query = "SELECT r FROM Reservas r WHERE r.nombredependencia = :nombredependencia"),
    @NamedQuery(name = "Reservas.findByTituloevento", query = "SELECT r FROM Reservas r WHERE r.tituloevento = :tituloevento"),
    @NamedQuery(name = "Reservas.findByFechaeventodesde", query = "SELECT r FROM Reservas r WHERE r.fechaeventodesde = :fechaeventodesde"),
    @NamedQuery(name = "Reservas.findByFechaeventohasta", query = "SELECT r FROM Reservas r WHERE r.fechaeventohasta = :fechaeventohasta"),
    @NamedQuery(name = "Reservas.findByEstadoevento", query = "SELECT r FROM Reservas r WHERE r.estadoevento = :estadoevento"),
    @NamedQuery(name = "Reservas.findByEstado", query = "SELECT r FROM Reservas r WHERE r.estado = :estado")})
public class Reservas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "nombredependencia")
    private String nombredependencia;
    @Column(name = "tituloevento")
    private String tituloevento;
    @Column(name = "fechaeventodesde")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaeventodesde;
    @Column(name = "fechaeventohasta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaeventohasta;
    @Basic(optional = false)
    @Column(name = "estadoevento")
    private int estadoevento;
    @Basic(optional = false)
    @Column(name = "estado")
    private int estado;
    @JoinColumn(name = "idtipoevento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Tipoeventos idtipoevento;
    @JoinColumn(name = "idusuario", referencedColumnName = "id")
    @ManyToOne
    private Usuarios idusuario;

    public Reservas() {
    }

    public Reservas(Integer id) {
        this.id = id;
    }

    public Reservas(Integer id, int estadoevento, int estado) {
        this.id = id;
        this.estadoevento = estadoevento;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombredependencia() {
        return nombredependencia;
    }

    public void setNombredependencia(String nombredependencia) {
        this.nombredependencia = nombredependencia;
    }

    public String getTituloevento() {
        return tituloevento;
    }

    public void setTituloevento(String tituloevento) {
        this.tituloevento = tituloevento;
    }

    public Date getFechaeventodesde() {
        return fechaeventodesde;
    }

    public void setFechaeventodesde(Date fechaeventodesde) {
        this.fechaeventodesde = fechaeventodesde;
    }

    public Date getFechaeventohasta() {
        return fechaeventohasta;
    }

    public void setFechaeventohasta(Date fechaeventohasta) {
        this.fechaeventohasta = fechaeventohasta;
    }

    public int getEstadoevento() {
        return estadoevento;
    }

    public void setEstadoevento(int estadoevento) {
        this.estadoevento = estadoevento;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public Tipoeventos getIdtipoevento() {
        return idtipoevento;
    }

    public void setIdtipoevento(Tipoeventos idtipoevento) {
        this.idtipoevento = idtipoevento;
    }

    public Usuarios getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(Usuarios idusuario) {
        this.idusuario = idusuario;
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
        if (!(object instanceof Reservas)) {
            return false;
        }
        Reservas other = (Reservas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.reservationsv1.modelo.Reservas[ id=" + id + " ]";
    }
    
}
