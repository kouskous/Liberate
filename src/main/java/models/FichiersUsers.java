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
@Table(name = "FichiersUsers")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FichiersUsers.findAll", query = "SELECT f FROM FichiersUsers f"),
    @NamedQuery(name = "FichiersUsers.findByIdF", query = "SELECT f FROM FichiersUsers f WHERE f.idFichierUser = :idF"),
    @NamedQuery(name = "FichiersUsers.findByPathLogique", query = "SELECT f FROM FichiersUsers f WHERE f.pathLogique = :pathLogique"),
    @NamedQuery(name = "FichiersUsers.findByNomPhysique", query = "SELECT f FROM FichiersUsers f WHERE f.nomPhysique = :nomPhysique"),
    @NamedQuery(name = "FichiersUsers.findByNomReel", query = "SELECT f FROM FichiersUsers f WHERE f.nomReel = :nomReel"),
    @NamedQuery(name = "FichiersUsers.findByDateCreation", query = "SELECT f FROM FichiersUsers f WHERE f.dateCreation = :dateCreation"),
    @NamedQuery(name = "FichiersUsers.findByType", query = "SELECT f FROM FichiersUsers f WHERE f.type = :type"),
    @NamedQuery(name = "FichiersUsers.findByUser", query = "SELECT f FROM FichiersUsers f WHERE f.user = :user"),
    @NamedQuery(name = "FichiersUsers.findDescendantsDossier", query = "SELECT f FROM FichiersUsers f WHERE f.pathLogique LIKE :pathLogiqueDossier"),
    @NamedQuery(name = "FichiersUsers.findByUserAndPath", query = "SELECT f FROM FichiersUsers f WHERE f.user = :user AND f.pathLogique = :pathLogique"),
    @NamedQuery(name = "FichiersUsers.findByNotUserAndPath", query = "SELECT f FROM FichiersUsers f WHERE f.user != :user AND f.pathLogique = :pathLogique")})
public class FichiersUsers implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idF")
    private Integer idFichierUser;
    
    @Basic(optional = false)
    @Column(name = "pathLogique")
    private String pathLogique;
    
    @Basic(optional = true)
    @Column(name = "nomPhysique")
    private String nomPhysique;
    
    @Basic(optional = false)
    @Column(name = "nomReel")
    private String nomReel;
    
    @Basic(optional = false)
    @Column(name = "dateCreation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    
    @Basic(optional = false)
    @Column(name = "type")
    private boolean type;
    
    @JoinColumn(name = "idU", referencedColumnName = "idU")
    @ManyToOne(optional = false)
    private User user;

    @Basic(optional = false)
    @Column(name = "verrou")
    private int verrou;
    
    public FichiersUsers() {
    }

    public FichiersUsers(String pathLogique, String nomPhysique, String nomReel, Date dateCreation,
            boolean type, User user,int verrou) {
        this.pathLogique = pathLogique;
        this.nomPhysique = nomPhysique;
        this.nomReel = nomReel;
        this.dateCreation = dateCreation;
        this.type = type;
        this.user = user;
        this.verrou=verrou;
    }

    public String getPathLogique() {
        return pathLogique;
    }

    public void setPathLogique(String pathLogique) {
        this.pathLogique = pathLogique;
    }

    public String getNomPhysique() {
        return nomPhysique;
    }

    public void setNomPhysique(String nomPhysique) {
        this.nomPhysique = nomPhysique;
    }

    public String getNomReel() {
        return nomReel;
    }

    public void setNomReel(String nomReel) {
        this.nomReel = nomReel;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public boolean getType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public int getVerrou() {
        return verrou;
    }

    public void setVerrou(int verrou) {
        this.verrou = verrou;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFichierUser != null ? idFichierUser.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FichiersUsers)) {
            return false;
        }
        FichiersUsers other = (FichiersUsers) object;
        if ((this.idFichierUser == null && other.idFichierUser != null) || (this.idFichierUser != null && !this.idFichierUser.equals(other.idFichierUser))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.FichiersUsers[ idF=" + idFichierUser + " ]";
    }
    
}
