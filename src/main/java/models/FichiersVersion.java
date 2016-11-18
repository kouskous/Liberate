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
@Table(name = "FichiersVersion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FichiersVersion.findAll", query = "SELECT f FROM FichiersVersion f"),
    @NamedQuery(name = "FichiersVersion.findByIdF", query = "SELECT f FROM FichiersVersion f WHERE f.idFichierVersion = :idF"),
    @NamedQuery(name = "FichiersVersion.findByPathLogique", query = "SELECT f FROM FichiersVersion f WHERE f.pathLogique = :pathLogique"),
    @NamedQuery(name = "FichiersVersion.findByNomPhysique", query = "SELECT f FROM FichiersVersion f WHERE f.nomPhysique = :nomPhysique"),
    @NamedQuery(name = "FichiersVersion.findByNomReel", query = "SELECT f FROM FichiersVersion f WHERE f.nomReel = :nomReel"),
    @NamedQuery(name = "FichiersVersion.findByDateCreation", query = "SELECT f FROM FichiersVersion f WHERE f.dateCreation = :dateCreation"),
    @NamedQuery(name = "FichiersVersion.findByType", query = "SELECT f FROM FichiersVersion f WHERE f.type = :type")})
public class FichiersVersion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idF")
    private Integer idFichierVersion;
    
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
    private Boolean type;
    
    @JoinColumn(name = "idV", referencedColumnName = "idV")
    @ManyToOne(optional = false)
    private Version version;

    public FichiersVersion() {
    }

    public FichiersVersion( String pathLogique, String nomPhysique, String nomReel, Date dateCreation,
            boolean type, Version version) {
        this.pathLogique = pathLogique;
        this.nomPhysique = nomPhysique;
        this.nomReel = nomReel;
        this.dateCreation = dateCreation;
        this.type = type;
        this.version = version;
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

    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFichierVersion != null ? idFichierVersion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FichiersVersion)) {
            return false;
        }
        FichiersVersion other = (FichiersVersion) object;
        if ((this.idFichierVersion == null && other.idFichierVersion != null) || (this.idFichierVersion != null && !this.idFichierVersion.equals(other.idFichierVersion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.FichiersVersion[ idF=" + idFichierVersion + " ]";
    }
    
}
