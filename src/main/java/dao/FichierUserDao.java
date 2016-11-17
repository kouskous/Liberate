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
public class FichierUserDao {
    
    EntityManager em;

    public FichierUserDao(){
    
    }
    
    public FichierUserDao(EntityManager em_){
        em = em_;
    }
    
    public EntityManager getEntityManager(){
        return this.em;
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
    
    public String getPathByPathLogique(User user,String pathLogique){
        TypedQuery<FichiersUsers> query = em.createNamedQuery("FichiersUsers.findByUserAndPath", FichiersUsers.class);
        query.setParameter("user", user);
        query.setParameter("pathLogique", pathLogique);
        List<FichiersUsers> results = query.getResultList();
        
        if(results.size()==1){
            if(results.get(0).getType())
             return results.get(0).getNomPhysique();
        }
        return null;
    }

    /**
     * @author Florian
     *
     * Cherche l'arborescence de fichiers d'un utilisateur depuis sa racine
     * @param user l'utilisateur dont on veux l'arborescence
     * @return Renvoie l'arborescence complete depuis la racine de l'utilisateur
     */
    public Map<String, Boolean> getArborescence(User user){
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
    public Map<String, Boolean> getArborescence(User user, FichiersUsers dossier){
        //TODO changer le Type de fichier en enumeration
        try {
            Map<String, Boolean> arborescence = new Hashtable<>();
            Collection<FichiersUsers> fichiers;

            if(dossier == null) {
                fichiers = user.getFichiersUsersCollection();
            }

            //si le fichier n'est pas un dossier
            else if (dossier.getType() == true) {
                throw new IllegalArgumentException();
            }
            else {
                // Recupération des fichiers user descendants du dossier
                TypedQuery<FichiersUsers> query = em.createNamedQuery("FichiersUsers.findDescendantsDossier", FichiersUsers.class);
                query.setParameter("pathLogiqueDossier", dossier.getPathLogique() + "/%");
                fichiers = query.getResultList();
            }

            for (FichiersUsers fichier : fichiers) {
                arborescence.put(fichier.getPathLogique(), fichier.getType());
            }

            return arborescence;
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
                                     boolean type, User user, int verrou){

        // Création nouvel utilisateur
        FichiersUsers newFichierUsers = new FichiersUsers(pathLogique, nomPhysique, nomReel, dateCreation, type, user, verrou);

        // On essaye d'ajouter l'utilisateur à la persistence
        try{
            em.getTransaction().begin();
            em.persist(newFichierUsers);
            em.getTransaction().commit();
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
                em.getTransaction().begin();
                em.remove(fichiersUsersToDelete);
                em.getTransaction().commit();
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
    @Transactional
    public boolean changePathLogique(FichiersUsers fichierToChange, String newPathLogique){
        if(fichierToChange != null){
            fichierToChange.setPathLogique(newPathLogique);
            em.getTransaction().begin();
            em.persist(fichierToChange);
            em.getTransaction().commit();
            return true;
        }
        else{
            return false;
        }
    }

    // TODO: toutes les autres fonctions de modification qu'on aura besoin.

}
