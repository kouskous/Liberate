/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Luc Di Sanza
 */
@Entity
@Table(name = "User")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findByIdU", query = "SELECT u FROM User u WHERE u.idUser = :idU"),
    @NamedQuery(name = "User.findByPseudo", query = "SELECT u FROM User u WHERE u.pseudo = :pseudo"),
    @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
    @NamedQuery(name = "User.findByNom", query = "SELECT u FROM User u WHERE u.nom = :nom"),
    @NamedQuery(name = "User.findByPrenom", query = "SELECT u FROM User u WHERE u.prenom = :prenom"),
    @NamedQuery(name = "User.findByDateCreation", query = "SELECT u FROM User u WHERE u.dateCreation = :dateCreation"),
    @NamedQuery(name = "User.findByDateModification", query = "SELECT u FROM User u WHERE u.dateModification = :dateModification"),
    @NamedQuery(name = "Pseudo.search", query = "SELECT pseudo FROM User u WHERE u.pseudo LIKE :name "),
    @NamedQuery(name = "Pseudo.getAll", query = "SELECT pseudo FROM User")})
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idU")
    private Integer idUser;
    
    @Basic(optional = false)
    @Column(name = "pseudo")
    private String pseudo;
    
    @Basic(optional = false)
    @Column(name = "email")
    private String email;
    
    @Basic(optional = false)
    @Column(name = "nom")
    private String nom;
    
    @Basic(optional = false)
    @Column(name = "prenom")
    private String prenom;
    
    @Basic(optional = false)
    @Column(name = "cleMotDePasse")
    private String cleMotDePasse;
    
    @Basic(optional = false)
    @Column(name = "dateCreation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    
    @Basic(optional = false)
    @Column(name = "dateModification")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModification;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Collection<UserProjet> userProjetCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userCreateur")
    private Collection<Demande> demandeCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userAssigne")
    private Collection<Demande> demandeCollection1;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch= FetchType.EAGER)
    private Collection<FichiersUsers> fichiersUsersCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Collection<Message> messageCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Collection<Version> versionCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Collection<Article> articleCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Collection<Commentaire> commentaireCollection;

    public User() {
    }

    public User(String pseudo, String email, String nom, String prenom, Date dateCreation, 
            Date dateModification) {
        this.pseudo = pseudo;
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
        this.dateCreation = dateCreation;
        this.dateModification = dateModification;
    }
    
    // Getters and setters
    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
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

    public String getCleMotDePasse() {
        return cleMotDePasse;
    }

    public void setCleMotDePasse(String cleMotDePasse) {
        this.cleMotDePasse = cleMotDePasse;
    }

    public Integer getIdUser() {
        return idUser;
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
    public Collection<Demande> getDemandeCollection1() {
        return demandeCollection1;
    }

    public void setDemandeCollection1(Collection<Demande> demandeCollection1) {
        this.demandeCollection1 = demandeCollection1;
    }

    @XmlTransient
    public Collection<FichiersUsers> getFichiersUsersCollection() {
        return fichiersUsersCollection;
    }

    public void setFichiersUsersCollection(Collection<FichiersUsers> fichiersUsersCollection) {
        this.fichiersUsersCollection = fichiersUsersCollection;
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
        hash += (idUser != null ? idUser.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.idUser == null && other.idUser != null) || (this.idUser != null && !this.idUser.equals(other.idUser))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.User[ idU=" + idUser + " ]";
    }
    
}
