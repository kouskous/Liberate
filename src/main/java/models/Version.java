/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Luc Di Sanza
 */
@Entity
@Table(name = "Version")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Version.findAll", query = "SELECT v FROM Version v"),
    @NamedQuery(name = "Version.findByIdV", query = "SELECT v FROM Version v WHERE v.idVersion = :idV"),
    @NamedQuery(name = "Version.findByNumVersion", query = "SELECT v FROM Version v WHERE v.numVersion = :numVersion"),
    @NamedQuery(name = "Version.findByDateCreation", query = "SELECT v FROM Version v WHERE v.dateCreation = :dateCreation"),
    @NamedQuery(name = "Version.findByUser", query = "SELECT v FROM Version v WHERE v.idU = :idU"),
    @NamedQuery(name = "Version.findByProjet", query = "SELECT v FROM Version v WHERE v.idP = :idP")})
public class Version implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idV")
    private Integer idVersion;
    
    @Lob
    @Column(name = "message_commit")
    private String messageCommit;
    
    @Basic(optional = false)
    @Column(name = "numVersion")
    private int numVersion;
    
    @Basic(optional = false)
    @Column(name = "dateCreation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "version")
    private Collection<FichiersVersion> fichiersVersionCollection;
    
    @JoinColumn(name = "idU", referencedColumnName = "idU")
    @ManyToOne(optional = false)
    private User user;
    
    @JoinColumn(name = "idP", referencedColumnName = "idP")
    @ManyToOne(optional = false)
    private Projet projet;

    public Version() {
    }

    public Version( int numVersion, Date dateCreation, User user, Projet projet) {
        this.numVersion = numVersion;
        this.dateCreation = dateCreation;
        this.user = user;
        this.projet = projet;
    }

    public String getMessageCommit() {
        return messageCommit;
    }

    public void setMessageCommit(String messageCommit) {
        this.messageCommit = messageCommit;
    }

    public int getNumVersion() {
        return numVersion;
    }

    public void setNumVersion(int numVersion) {
        this.numVersion = numVersion;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    @XmlTransient
    public Collection<FichiersVersion> getFichiersVersionCollection() {
        return fichiersVersionCollection;
    }

    public void setFichiersVersionCollection(Collection<FichiersVersion> fichiersVersionCollection) {
        this.fichiersVersionCollection = fichiersVersionCollection;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Projet getProjet() {
        return projet;
    }

    public void setProjet(Projet projet) {
        this.projet = projet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idVersion != null ? idVersion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Version)) {
            return false;
        }
        Version other = (Version) object;
        if ((this.idVersion == null && other.idVersion != null) || (this.idVersion != null && !this.idVersion.equals(other.idVersion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.Version[ idV=" + idVersion + " ]";
    }
    
}
