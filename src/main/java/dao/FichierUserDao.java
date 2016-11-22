package dao;

import models.FichiersUsers;
import models.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.*;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;

/**
 * Created by Florian on 20/10/2016.
 *
 * @author Florian
 */
@Transactional
public class FichierUserDao {

    @PersistenceContext
    EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }

    // Cherche les fichiers d'un utilisateur dans la BDD
    // - renvoie la liste des fichiers de l'user si il y est
    // - renvoie une liste vide sinon
    public Collection<FichiersUsers> getFichiersByUser(User user) {

        // Recherche des fichiers
        TypedQuery<FichiersUsers> query = em.createNamedQuery("FichiersUsers.findByUser", FichiersUsers.class);
        query.setParameter("user", user);
        Collection<FichiersUsers> results = query.getResultList();

        // Si aucun fichier n'est trouvé avec cet user
        if(results.isEmpty()){
            return new ArrayList<>();
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
        query.setParameter("pathLogique", pathLogique);
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
    


    /**
     * @author Florian
     *
     * Cherche l'arborescence de fichiers d'un utilisateur depuis sa racine
     * @param user l'utilisateur dont on veux l'arborescence
     * @return Renvoie l'arborescence complete depuis la racine de l'utilisateur
     */
    public Collection<FichiersUsers> getArborescence(User user){
        return getArborescence(user, null);
    }

    /**
     * @author Florian
     *
     * Cherche l'arborescence de fichiers d'un utilisateur
     * @param user l'utilisateur dont on veux l'arborescence
     * @param dossier le dossier à partir duquel on veux l'arborescence,
     *                si null depuis la racine de l'utilisateur
     * @return Renvoie l'arborescence depuis le dossier en question, ou null si le dossier est un fichier
     */
    public Collection<FichiersUsers> getArborescence(User user, FichiersUsers dossier){
        try {
            Map<String, FichiersUsers.Type> arborescence = new Hashtable<>();
            Collection<FichiersUsers> fichiers;

            if(dossier == null) {
                fichiers = getFichiersByUser(user);
            }

            //si le fichier n'est pas un dossier
            else if (dossier.getType() != FichiersUsers.Type.DOSSIER) {
                throw new IllegalArgumentException();
            }
            else {
                // Recupération des fichiers user descendants du dossier
                TypedQuery<FichiersUsers> query = em.createNamedQuery("FichiersUsers.findDescendantsDossier", FichiersUsers.class);
                query.setParameter("pathLogiqueDossier", dossier.getPathLogique() + "/%");
                fichiers = query.getResultList();
            }


            return fichiers;
        }
        catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
            System.out.println("Erreur pour récupérer l'arborescence de fichiers de l'utilisateur : le dossier est un fichier");
            return null;
        }
    }

    // Création d'un fichier
    // Renvoie le fichier si réussite
    // Renvoie null sinon
    public FichiersUsers createNewFichierUser(String pathLogique, String nomPhysique, String nomReel, Date dateCreation,
                                              FichiersUsers.Type type, User user, int verrou){

        // Création nouvel utilisateur
        FichiersUsers newFichierUsers = new FichiersUsers(pathLogique, nomPhysique, nomReel, dateCreation, type, user, verrou);

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

    public FichiersUsers getPathByPathLogique(User user,String pathLogique){
        TypedQuery<FichiersUsers> query = em.createNamedQuery("FichiersUsers.findByUserAndPath", FichiersUsers.class);
        query.setParameter("user", user);
        query.setParameter("pathLogique", pathLogique);
        List<FichiersUsers> results = query.getResultList();

        if(results.size()==1){
            if(results.get(0).getType() == FichiersUsers.Type.FICHIER)
             return results.get(0);
        }
        return null;
    }

    public List<FichiersUsers> getPathsByPathLogique(User user,String pathLogique){
        TypedQuery<FichiersUsers> query = em.createNamedQuery("FichiersUsers.findByNotUserAndPath", FichiersUsers.class);
        query.setParameter("user", user);
        query.setParameter("pathLogique", pathLogique);
        List<FichiersUsers> results = query.getResultList();

        if(results.size()!=0){
            if(results.get(0).getType() == FichiersUsers.Type.FICHIER)
             return results;
        }
        return null;
    }

    public int getVerrouByPathLogique(User user,String pathLogique){
        TypedQuery<FichiersUsers> query = em.createNamedQuery("FichiersUsers.findByUserAndPath", FichiersUsers.class);
        query.setParameter("user", user);
        query.setParameter("pathLogique", pathLogique);
        List<FichiersUsers> results = query.getResultList();

        if(results.size()==1){
            if(results.get(0).getType() == FichiersUsers.Type.FICHIER)
             return results.get(0).getVerrou();
        }
        return 5;
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
    public boolean changeVerrou(FichiersUsers fichierToChange, int verrou){
        if(fichierToChange != null){
            fichierToChange.setVerrou(verrou);
            em.persist(fichierToChange);
            return true;
        }
        else{
            return false;
        }
    }

    public boolean changeVerrouAutre(List<FichiersUsers> fichiersToChange, int verrou){
        if(fichiersToChange != null){
            for(int i=0;i<fichiersToChange.size();i++){
            fichiersToChange.get(i).setVerrou(verrou);
            em.persist(fichiersToChange.get(i));
            }
            return true;
        }
        else{
            return false;
        }
    }

}
