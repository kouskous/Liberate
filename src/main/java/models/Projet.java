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
@Table(name = "Projet")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Projet.findAll", query = "SELECT p FROM Projet p"),
    @NamedQuery(name = "Projet.findByIdP", query = "SELECT p FROM Projet p WHERE p.idProjet = :idP"),
    @NamedQuery(name = "Projet.findByNom", query = "SELECT p FROM Projet p WHERE p.nom = :nom"),
    @NamedQuery(name = "Projet.findByDateCreation", query = "SELECT p FROM Projet p WHERE p.dateCreation = :dateCreation"),
    @NamedQuery(name = "Projet.findByDateModification", query = "SELECT p FROM Projet p WHERE p.dateModification = :dateModification"),
    @NamedQuery(name = "Projet.findByLangage", query = "SELECT p FROM Projet p WHERE p.langage = :langage")})
public class Projet implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idP")
    private Integer idProjet;
    
    @Basic(optional = false)
    @Column(name = "nom")
    private String nom;
    
    @Basic(optional = false)
    @Column(name = "dateCreation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    
    @Basic(optional = false)
    @Column(name = "dateModification")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModification;
    
    @Basic(optional = false)
    @Column(name = "langage")
    private String langage;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "projet")
    private Collection<UserProjet> userProjetCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "projet")
    private Collection<Demande> demandeCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "projet")
    private Collection<Message> messageCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "projet")
    private Collection<Version> versionCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "projet")
    private Collection<Article> articleCollection;

    public Projet() {
    }

    public Projet(String nom, Date dateCreation, Date dateModification, String langage) {
        this.nom = nom;
        this.dateCreation = dateCreation;
        this.dateModification = dateModification;
        this.langage = langage;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Date getDateModification() {
        return dateModification;
    }

    public void setDateModification(Date dateModification) {
        this.dateModification = dateModification;
    }

    public String getLangage() {
        return langage;
    }

    public void setLangage(String langage) {
        this.langage = langage;
    }

    @XmlTransient
    public Collection<UserProjet> getUserProjetCollection() {
        return userProjetCollection;
    }

    public void setUserProjetCollection(Collection<UserProjet> userProjetCollection) {
        this.userProjetCollection = userProjetCollection;
    }

    @XmlTransient
    public Collection<Demande> getDemandeCollection() {
        return demandeCollection;
    }

    public void setDemandeCollection(Collection<Demande> demandeCollection) {
        this.demandeCollection = demandeCollection;
    }

    @XmlTransient
    public Collection<Message> getMessageCollection() {
        return messageCollection;
    }

    public void setMessageCollection(Collection<Message> messageCollection) {
        this.messageCollection = messageCollection;
    }

    @XmlTransient
    public Collection<Version> getVersionCollection() {
        return versionCollection;
    }

    public void setVersionCollection(Collection<Version> versionCollection) {
        this.versionCollection = versionCollection;
    }

    @XmlTransient
    public Collection<Article> getArticleCollection() {
        return articleCollection;
    }

    public void setArticleCollection(Collection<Article> articleCollection) {
        this.articleCollection = articleCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProjet != null ? idProjet.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Projet)) {
            return false;
        }
        Projet other = (Projet) object;
        if ((this.idProjet == null && other.idProjet != null) || (this.idProjet != null && !this.idProjet.equals(other.idProjet))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.Projet[ idP=" + idProjet + " ]";
    }
    
}
