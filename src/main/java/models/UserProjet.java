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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "User_Projet")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserProjet.findAll", query = "SELECT u FROM UserProjet u"),
    @NamedQuery(name = "UserProjet.findByTypeDroit", query = "SELECT u FROM UserProjet u WHERE u.typeDroit = :typeDroit"),
    @NamedQuery(name = "UserProjet.findByDateCreation", query = "SELECT u FROM UserProjet u WHERE u.dateCreation = :dateCreation"),
    @NamedQuery(name = "UserProjet.findByDateModification", query = "SELECT u FROM UserProjet u WHERE u.dateModification = :dateModification"),
    @NamedQuery(name = "UserProjet.findByIdU", query = "SELECT u FROM UserProjet u WHERE u.userProjetPK.idUser = :idU"),
    @NamedQuery(name = "UserProjet.findByIdP", query = "SELECT u FROM UserProjet u WHERE u.userProjetPK.idProjet = :idP"),
    @NamedQuery(name = "UserProjet.findNotInProject", query = "SELECT u FROM UserProjet u WHERE u.userProjetPK.idProjet <> :idP"),
    @NamedQuery(name = "UserProjet.findByIdUAndIdP", query = "SELECT u FROM UserProjet u WHERE u.userProjetPK.idProjet = :idP AND u.userProjetPK.idUser = :idU")
})
public class UserProjet implements Serializable {

    @EmbeddedId
    protected UserProjetPK userProjetPK;
    
    @Basic(optional = false)
    @Column(name = "type_droit")
    private String typeDroit;
    
    @Basic(optional = false)
    @Column(name = "date_creation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    
    @Basic(optional = false)
    @Column(name = "date_modification")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModification;
    
    @JoinColumn(name = "idP", referencedColumnName = "idP", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Projet projet;
    
    @JoinColumn(name = "idU", referencedColumnName = "idU", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private User user;

    public UserProjet() {
    }

    public UserProjet(String typeDroit, Date dateCreation, Date dateModification, 
            User user, Projet projet) {
        this.typeDroit = typeDroit;
        this.dateCreation = dateCreation;
        this.dateModification = dateModification;
        this.user = user;
        this.projet = projet;
        this.userProjetPK = new UserProjetPK(user.getIdUser(), projet.getIdProjet());
    }
    
    public void setUserProjetPK(int idProj, int idUser){
        userProjetPK.setIdProjet(idProj);
        userProjetPK.setIdUser(idUser);
    }
            
    public String getTypeDroit() {
        return typeDroit;
    }

    public void setTypeDroit(String typeDroit) {
        this.typeDroit = typeDroit;
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

    public Projet getProjet() {
        return projet;
    }

    public void setProjet(Projet projet) {
        this.projet = projet;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userProjetPK != null ? userProjetPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserProjet)) {
            return false;
        }
        UserProjet other = (UserProjet) object;
        if ((this.userProjetPK == null && other.userProjetPK != null) || (this.userProjetPK != null && !this.userProjetPK.equals(other.userProjetPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.UserProjet[ userProjetPK=" + userProjetPK + " ]";
    }
    
}
