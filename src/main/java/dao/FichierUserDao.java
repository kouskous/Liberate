package dao;

import models.FichiersUsers;
import models.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by Florian on 20/10/2016.
 *
 * @author Florian
 */
public class FichierUserDao {
    EntityManager em;

    public FichierUserDao(){}

    public FichierUserDao(EntityManager em){
        this.em = em;
    }

    // Cherche les fichiers d'un utilisateur dans la BDD
    // - renvoie null s'il n'y a pas de fichiers pour cet user.
    // - renvoie la liste des fichiers de l'user si il y est
    public Collection<FichiersUsers> getFichiersByUser(User user) {

        // Recherche des fichiers
        TypedQuery<FichiersUsers> query = em.createNamedQuery("FichiersUsers.findByUser", FichiersUsers.class);
        query.setParameter("user", user);
        Collection<FichiersUsers> results = query.getResultList();

        // Si aucun fichier n'est trouvé avec cet user
        if(results.isEmpty()){
            return null;
        }
        // Des fichiers ont été trouvés
        return results;
    }
    
    // Cherche les fichiers d'un utilisateur dans la BDD
    // - renvoie null s'il n'a pas été trouvé
    // - renvoie le fichier si il a été trouvé
    public FichiersUsers getFichiersByUserAndPath(User user, String pathLogique){

        // Recherche des fichiers
        TypedQuery<FichiersUsers> query = em.createNamedQuery("FichiersUsers.findByUserAndPath", FichiersUsers.class);
        query.setParameter("user", user);
        List<FichiersUsers> results = query.getResultList();

        // Si aucun fichier n'est trouvé avec cet user et ce path
        if(results.isEmpty()){
            return null;
        }
        else if(results.size() == 1){
            return results.get(0);
        }
        else{ // Anomalie, plus d'un résultat
            return null;
        }
    }

    // Cherche un fichier dans la BDD par son nom
    // - renvoie null si il n'y est pas.
    // - renvoie le fichier si il y est
    public FichiersUsers getFichierUserByNomPhysique(String nomPhysique) throws Exception {

        // Recherche de l'user par pseudo (unique)
        TypedQuery<FichiersUsers> query = em.createNamedQuery("FichiersUsers.findByNomPhysique", FichiersUsers.class);
        query.setParameter("nomPhysique", nomPhysique);
        List<FichiersUsers> results = query.getResultList();

        // Si aucun fichier n'a été trouvé avec ce nom
        if(results.isEmpty()){
            return null;
        }
        // Un fichier a été trouvé
        else if(results.size() == 1){
            return results.get(0);
        }

        // Anomalie: plusieurs fichiers ont été trouvé avec le même nom physique
        else{
            throw new Exception("Erreur BDD: plusieurs fichiers d'utilisateur ont le même nom physique");
        }
    }

    // Création d'un fichier
    // Renvoie le fichier si réussite
    // Renvoie null sinon
    public FichiersUsers createNewFichierUser(String pathLogique, String nomPhysique, String nomReel, Date dateCreation,
                                     boolean type, User user){

        // Création nouvel utilisateur
        FichiersUsers newFichierUsers = new FichiersUsers(pathLogique, nomPhysique, nomReel, dateCreation, type, user);

        // On essaye d'ajouter l'utilisateur à la persistence
        try{
            em.persist(newFichierUsers);
            return newFichierUsers;
        }
        catch(Exception e){
            System.out.println("Erreur lors de l'ajout d'un nouveau fichierUsers :");
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Suppression d'un fichier de la base de données
    // Renvoie vrai si la suppression a réussie
    // Renvoie faux sinon.
    public boolean deleteFichierUserByNomPhysique(String nomPhysique){

        try{
            // On cherche le fichier
            FichiersUsers fichiersUsersToDelete = this.getFichierUserByNomPhysique(nomPhysique);

            // Si on l'a trouvé, on le supprime
            if(fichiersUsersToDelete != null){
                em.remove(fichiersUsersToDelete);
                return true;
            }
            else{
                // On n'a pas trouvé le fichier dans la BDD
                return false;
            }
        }
        catch(Exception e){
            System.out.println("Erreur dans la suppression d'un fichier d'utilisateur :");
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * @author Florian
     *
     * Modifie le chemin logique d'un fichier d'utilisateur
     * @param fichierToChange le fichier qu'on souhaite modifier
     * @param newPathLogique le nouveau chemin physique
     * @return Renvoie vrai si réussi, faux sinon
     */
    public boolean changePathLogique(FichiersUsers fichierToChange, String newPathLogique){
        if(fichierToChange != null){
            fichierToChange.setPathLogique(newPathLogique);
            em.persist(fichierToChange);
            return true;
        }
        else{
            return false;
        }
    }

    // TODO: toutes les autres fonctions de modification qu'on aura besoin.

}
