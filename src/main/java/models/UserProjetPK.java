/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Luc Di Sanza
 */
@Embeddable
public class UserProjetPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "idU")
    private int idUser;
    
    @Basic(optional = false)
    @Column(name = "idP")
    private int idProjet;

    public UserProjetPK() {
    }

    public UserProjetPK(int idU, int idP) {
        this.idUser = idU;
        this.idProjet = idP;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdProjet() {
        return idProjet;
    }

    public void setIdProjet(int idProjet) {
        this.idProjet = idProjet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idUser;
        hash += (int) idProjet;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserProjetPK)) {
            return false;
        }
        UserProjetPK other = (UserProjetPK) object;
        if (this.idUser != other.idUser) {
            return false;
        }
        if (this.idProjet != other.idProjet) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.UserProjetPK[ idU=" + idUser + ", idP=" + idProjet + " ]";
    }
    
}
