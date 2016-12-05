package dao;

import java.io.File;
import models.FichiersUsers;
import models.User;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.*;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 * Created by Florian on 20/10/2016.
 *
 * @author Florian
 */
@Transactional
public class FichierUserDao {

    @PersistenceContext
    EntityManager em;

    /**
     *
     * Cherche les fichiers d'un utilisateur dans la BDD
     * @param user L'utilisateur dont on veut les fichiers
     * @return Renvoie la liste des fichiers de l'utilisateur.
     */
    public List<FichiersUsers> getFichiersByUser(User user) {
        // Recherche des fichiers
        TypedQuery<FichiersUsers> query = em.createNamedQuery("FichiersUsers.findByUser", FichiersUsers.class);
        query.setParameter("user", user);
        List<FichiersUsers> results = query.getResultList();

        // Des fichiers ont été trouvés
        return results;
    }
    
    /**
     *
     * Cherche les fichiers d'un utilisateur qui ont un certain pathLogique dans la BDD
     * @param user L'utilisateur dont on veut les fichiers
     * @param pathLogique Le path logique à rechercher
     * @return Renvoie la liste des fichiers trouvés ou null si echec.
     */
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

    /**
     *
     * Cherche un fichier dans la BDD par son nom physique
     * @param nomPhysique Nom physique du fichier à rechercher
     * @return Renvoie la liste des fichiers trouvés ou null si echec.
     */
    public FichiersUsers getFichierUserByNomPhysique(String nomPhysique) throws IllegalArgumentException {

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
            return null;
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
    public List<FichiersUsers> getArborescence(User user, FichiersUsers dossier){
        try {
            List<FichiersUsers> fichiers;

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
            System.out.println("Erreur pour récupérer l'arborescence de fichiers de l'utilisateur : le dossier est un fichier");
            System.out.println(e);
            return null;
        }
    }

     /**
     *
     * Création d'un fichier utilisateur en base de données
     * @param pathLogique Path logique du fichier à créer
     * @param nomPhysique Nom physique du fichier à créer
     * @param nomReel Nom du fichier à créer
     * @param dateCreation Date actuelle
     * @param type Type du fichier (fichier ou dossier)
     * @param user Utilisateur auquel le fichier est rataché
     * @param verrou Etat de verouillage du fichier
     * @return Renvoie le fichier si la création est réussie, null sinon.
     */
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
            System.out.println(e);
            return null;
        }
    }

     /**
     *
     * Suppression d'un fichier de la base de données
     * @param nomPhysique Nom physique du fichier à supprimer
     * @return Renvoie vrai si la suppression réussie, faux sinon
     */
    public boolean deleteFichierUserByNomPhysique(String nomPhysique){

        try{
            // On cherche le fichier
            FichiersUsers fichiersUsersToDelete = this.getFichierUserByNomPhysique(nomPhysique);

            // Si on l'a trouvé, on le supprime
            if(fichiersUsersToDelete != null){
                if (!em.contains(fichiersUsersToDelete)){
                    fichiersUsersToDelete = em.merge(fichiersUsersToDelete);
                }
                fichiersUsersToDelete.getUser().getFichiersUsersCollection().remove(fichiersUsersToDelete);
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
            System.out.println(e);
            return false;
        }
    }

    /**
     * TODO
     */
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

    /**
     * TODO
     */
    public List<FichiersUsers> getPathsByPathLogique(User user,String pathLogique){
        TypedQuery<FichiersUsers> query = em.createNamedQuery("FichiersUsers.findByNotUserAndPath", FichiersUsers.class);
        query.setParameter("user", user);
        query.setParameter("pathLogique", pathLogique);
        List<FichiersUsers> results = query.getResultList();

        if(!results.isEmpty()){
            if(results.get(0).getType() == FichiersUsers.Type.FICHIER){
                System.out.println(results.size());
             return results;
            }
        }
        return null;
    }

    public List<FichiersUsers> getLockedByUserAndProjet(User user,String projet){
        TypedQuery<FichiersUsers> query = em.createNamedQuery("FichiersUsers.findLockedByUserAndProjet", FichiersUsers.class);
        query.setParameter("user", user);
         String proj ="/"+projet+"/";
        query.setParameter("projet", proj);
        List<FichiersUsers> results = query.getResultList();
        
        if(results.size()!=0){
            
             return results;
        }
        return null;
    }
    
    public List<FichiersUsers> getByUserAndProjet(User user,String projet){
       System.out.println(1);
        TypedQuery<FichiersUsers> query = em.createNamedQuery("FichiersUsers.findByUserAndProjet", FichiersUsers.class);
        
        query.setParameter("user", user);
        
        String proj ="/"+projet+"/";
        
        query.setParameter("projet",proj);
        
        
        List<FichiersUsers> results = query.getResultList();
        
        if(results.size()!=0){
            
             return results;
        }
        return null;
    }
    
    /**
     * TODO (duplication avec fichier = getFichierByUserAndPath, fichier.getVerrou)
     */
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
    
    public List<FichiersUsers> getLockedByUser(User user){
        TypedQuery<FichiersUsers> query = em.createNamedQuery("FichiersUsers.findLockedByUser", FichiersUsers.class);
        query.setParameter("user", user);
        List<FichiersUsers> results = query.getResultList();
        
        if(results.size()!=0){
           
             return results;
        }
        return null;
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

    /**
     *
     * Modification de l'état de verouillage d'un fichier
     * @param fichierToChange Fichier à modifier
     * @param verrou Etat de verouillage à appliquer au fichier
     * @return Renvoie vrai si la modification réussie, faux sinon
     */
    public boolean changeVerrou(FichiersUsers fichierToChange, int verrou){
        if(fichierToChange != null){
            fichierToChange.setVerrou(verrou);
            try{
             em.merge(fichierToChange);
             return true;
            }catch(Exception e){
            System.out.println("Erreur lors du changement de verrou a la valeur "+verrou);
            System.out.println(e.getMessage());
                return false;
            }
        }
        else{
            return false;
        }
    }

    /**
     *
     * Modification de l'état de verouillage d'une liste de fichiers
     * @param fichiersToChange Liste des fichiers à modifier
     * @param verrou Etat de vérouillage à appliquer à tous les fichiers
     * @return Renvoie vrai si la modification réussie, faux sinon
     */
    public boolean changeVerrouAutre(List<FichiersUsers> fichiersToChange, int verrou){
        if(fichiersToChange != null){
            try{
                for(int i=0;i<fichiersToChange.size();i++){
                fichiersToChange.get(i).setVerrou(verrou);
                em.merge(fichiersToChange.get(i));
                }
            }catch(Exception e){
                    System.out.println("Erreur lors du changement du verrou sur le fichier. verrou= "+verrou);
                    System.out.println(e.getMessage());
                    return false;
                    }
            return true;
        }
        else{
            return false;
        }
    }
    
    public boolean renameFichier(FichiersUsers fichier, String newName){
        
        // Mise à jour du fichier
        fichier.setNomReel(newName); // Nouveau nom
        
        // Mise à jour du pathLogique
        String oldPath = fichier.getPathLogique();
        String path = new File(oldPath).getParent();
        fichier.setPathLogique(path + "/" + newName);
        
        try{
            em.merge(fichier);
            return true;
        }
        catch(Exception e){
            System.out.println("Erreur pendant le renommage d'un fichier");
            System.out.println(e.getMessage());
            return false;
        }
    }

}
