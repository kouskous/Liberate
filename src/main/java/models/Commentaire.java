/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Luc Di Sanza
 */
@Entity
@Table(name = "Commentaire")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Commentaire.findAll", query = "SELECT c FROM Commentaire c"),
    @NamedQuery(name = "Commentaire.findByIdCt", query = "SELECT c FROM Commentaire c WHERE c.idCommentaire = :idCt"),
    @NamedQuery(name = "Commentaire.findByDateEnvoie", query = "SELECT c FROM Commentaire c WHERE c.dateEnvoi = :dateEnvoie")})
public class Commentaire implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idCt")
    private Integer idCommentaire;
    
    @Basic(optional = false)
    @Lob
    @Column(name = "message")
    private String message;
    
    @Basic(optional = false)
    @Column(name = "dateEnvoie")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEnvoi;
    
    @JoinColumn(name = "idU", referencedColumnName = "idU")
    @ManyToOne(optional = false)
    private User user;
    
    @JoinColumn(name = "idD", referencedColumnName = "idD")
    @ManyToOne(optional = false)
    private Demande demande;

    public Commentaire() {
    }

    public Commentaire(Integer idCt, String message, Date dateEnvoi, User user, Demande demande) {
        this.idCommentaire = idCt;
        this.message = message;
        this.dateEnvoi = dateEnvoi;
        this.user = user;
        this.demande = demande;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(Date dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Demande getDemande() {
        return demande;
    }

    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCommentaire != null ? idCommentaire.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Commentaire)) {
            return false;
        }
        Commentaire other = (Commentaire) object;
        if ((this.idCommentaire == null && other.idCommentaire != null) || (this.idCommentaire != null && !this.idCommentaire.equals(other.idCommentaire))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.Commentaire[ idCt=" + idCommentaire + " ]";
    }
    
}
