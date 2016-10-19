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
@Table(name = "Demande")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Demande.findAll", query = "SELECT d FROM Demande d"),
    @NamedQuery(name = "Demande.findByIdD", query = "SELECT d FROM Demande d WHERE d.idDemande = :idD"),
    @NamedQuery(name = "Demande.findByTitre", query = "SELECT d FROM Demande d WHERE d.titre = :titre"),
    @NamedQuery(name = "Demande.findByStatut", query = "SELECT d FROM Demande d WHERE d.statut = :statut"),
    @NamedQuery(name = "Demande.findByPriorite", query = "SELECT d FROM Demande d WHERE d.priorite = :priorite"),
    @NamedQuery(name = "Demande.findByDateModification", query = "SELECT d FROM Demande d WHERE d.dateModification = :dateModification"),
    @NamedQuery(name = "Demande.findByDateCreation", query = "SELECT d FROM Demande d WHERE d.dateCreation = :dateCreation")})
public class Demande implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idD")
    private Integer idDemande;
    
    @Basic(optional = false)
    @Column(name = "titre")
    private String titre;
    
    @Lob
    @Column(name = "contenu")
    private String contenu;
    
    @Basic(optional = false)
    @Column(name = "statut")
    private String statut;
    
    @Basic(optional = false)
    @Column(name = "priorite")
    private String priorite;
    
    @Basic(optional = false)
    @Column(name = "dateModification")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModification;
    
    @Basic(optional = false)
    @Column(name = "dateCreation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    
    @JoinColumn(name = "idU_User", referencedColumnName = "idU")
    @ManyToOne(optional = false)
    private User userCreateur;
    
    @JoinColumn(name = "idP", referencedColumnName = "idP")
    @ManyToOne(optional = false)
    private Projet projet;
    
    @JoinColumn(name = "idU", referencedColumnName = "idU")
    @ManyToOne(optional = false)
    private User userAssigne;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "demande")
    private Collection<Commentaire> commentaireCollection;

    public Demande() {
    }

    public Demande(Integer idD, String titre, String statut, String priorite, Date dateModification, Date dateCreation, 
            User userCreateur, User userAssigne, Projet projet) {
        this.idDemande = idD;
        this.titre = titre;
        this.statut = statut;
        this.priorite = priorite;
        this.dateModification = dateModification;
        this.dateCreation = dateCreation;
        this.userAssigne = userAssigne;
        this.userCreateur = userCreateur;
        this.projet = projet;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getPriorite() {
        return priorite;
    }

    public void setPriorite(String priorite) {
        this.priorite = priorite;
    }

    public Date getDateModification() {
        return dateModification;
    }

    public void setDateModification(Date dateModification) {
        this.dateModification = dateModification;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public User getUserCreateur() {
        return userCreateur;
    }

    public void setUserCreateur(User userCreateur) {
        this.userCreateur = userCreateur;
    }

    public Projet getProjet() {
        return projet;
    }

    public void setProjet(Projet projet) {
        this.projet = projet;
    }

    public User getUserAssigne() {
        return userAssigne;
    }

    public void setUserAssigne(User userAssigne) {
        this.userAssigne = userAssigne;
    }

    @XmlTransient
    public Collection<Commentaire> getCommentaireCollection() {
        return commentaireCollection;
    }

    public void setCommentaireCollection(Collection<Commentaire> commentaireCollection) {
        this.commentaireCollection = commentaireCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDemande != null ? idDemande.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Demande)) {
            return false;
        }
        Demande other = (Demande) object;
        if ((this.idDemande == null && other.idDemande != null) || (this.idDemande != null && !this.idDemande.equals(other.idDemande))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.Demande[ idD=" + idDemande + " ]";
    }
    
}
