package dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import models.Projet;
import models.User;
import models.UserProjet;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Luc Di Sanza
 */
@Transactional
public class UserProjetDao {

    @PersistenceContext
    EntityManager em;
    
    @Autowired
    ProjetDao projetDao;
    
    // 
    // - renvoie null si il n'y est pas.
    // - renvoie le UserProjet si il y est
    // - exception si il y a plusieurs UserProjets avec la même paire user/projet.

    /**
     * @author Luc Di Sanza
     * Cherche un userProjet en BDD
     * @param idUser ID de l'utilisateur à rechercher
     * @param idProjet ID du projet à rechercher
     * @return Renvoie l'userProjet si réussite, null sinon
     * @throws IllegalArgumentException
     */
    public UserProjet getUserProjetByUIdPId(int idUser, int idProjet) throws IllegalArgumentException{
        
        // Recherche du UserProjet par idUser et idProjet (unique)
        TypedQuery<UserProjet> query = em.createNamedQuery("UserProjet.findByIdUAndIdP", UserProjet.class);
        query.setParameter("idP", idProjet);
        query.setParameter("idU", idUser);
        List<UserProjet> results = query.getResultList();
        
        // Si aucun UserProjet n'est trouvé avec ces ID
        if(results.isEmpty()){
            return null;
        }
        // Un UserProjet a été trouvé
        else if(results.size() == 1){
            return results.get(0);
        }
        // Anomalie: plusieurs UserProjet ont été trouvé avec le même nom
        else{
            return null;
        }
    }

    /**
     * Récupération de tous les projets d'un utilisateur
     * @param user l'utilisateur
     * @return une collection vide si il n'y a rien.
     *          les projets de l'utilisateur sinon.
     */
    public Collection<Projet> getProjetsByUser(User user){

        // Recherche du UserProjet par idUser et idProjet (unique)
        TypedQuery<UserProjet> query = em.createNamedQuery("UserProjet.findByIdU", UserProjet.class);
        query.setParameter("idU", user.getIdUser());
        List<UserProjet> userProjets = query.getResultList();

        Collection<Projet> result = new ArrayList<>();
        // Si aucun UserProjet n'est trouvé avec ces ID
        if(userProjets.isEmpty()){
            return result;
        }
        // Un UserProjet a été trouvé
        else {
            for (UserProjet userProjet : userProjets) {
                try {
                    result.add(projetDao.getProjetByName(userProjet.getProjet().getNom()));
                }
                catch (Exception e) {
                    System.err.println(e);
                }
            }
        }

        return result;
    }
       
    /**
     * @author Luc Di Sanza
     * Création d'un nouveau UserProjet
     * @param typeDroit Droits de l'utilisateur relatifs au projet
     * @param dateCreation Date actuelle
     * @param dateModification Date actuelle
     * @param user Utilisateur considéré
     * @param projet Projet considéré
     * @return Renvoie l'userProjet si réussite, null sinon
     */
    public UserProjet createNewUserProjet(String typeDroit, Date dateCreation, Date dateModification, 
            User user, Projet projet){
        
        if (user != null && projet != null){
            
            if (!(typeDroit.equals("admin") || typeDroit.equals("developpeur") || typeDroit.equals("reporteur"))){
                return null;
            }
            
            // Création nouveau UserProjet
            UserProjet newUserProjet = new UserProjet(typeDroit, dateCreation, dateModification, user, projet);
            
            // On essaye d'ajouter le UserProjet à la persistence
            try{

                em.persist(newUserProjet);
                return newUserProjet;
            }
            catch(Exception e){
                System.out.println("Erreur lors de l'ajout d'un nouveau UserProjet");
                System.out.println(e);
                return null;
            }
        }
        return null;
    }
    
    /**
     * @author Luc Di Sanza
     * Suppression d'un userProjet 
     * @param user Utilisateur considéré
     * @param projet Projet considéré
     * @return Renvoie vrai si réussite, faux sinon
     */
    public boolean deleteUserProjet(User user, Projet projet){
        
        try{
            UserProjet userProjetToDelete;
            
            // On cherche le userProjet
            userProjetToDelete = getUserProjetByUIdPId(user.getIdUser(), projet.getIdProjet());
            
            // Si on l'a trouvé, on le supprime
            if(userProjetToDelete != null){
                em.remove(userProjetToDelete);
                return true;
            }
            else{
                // On n'a pas trouvé le userProjet dans la BDD
                return false;
            }
        }
        catch(Exception e){
            System.out.println("Erreur dans la suppression d'un UserProjet");
            System.out.println(e);
            return false;
        }
    }
    
    /**
     * Change les droits d'un userProjet existant, ou le créé avec ces droits sinon.
     * @param typeDroit Droits à attribuer à l'userProjet
     * @param idUser Utilisateur considéré
     * @param idProjet Projet considéré
     * @return Renvoie vrai si réussite, faux sinon
     */
    public boolean changeDroitsUserProjet(String typeDroit, int idUser, int idProjet){
        
        // On teste que typeDroit est une chaine de caractère valide
        if (typeDroit.equals("admin") || typeDroit.equals("developpeur") || typeDroit.equals("reporteur")){
            try{
                UserProjet userProjet = getUserProjetByUIdPId(idUser, idProjet);

                if(userProjet == null) // Le userProjet n'a pas été trouvé
                    return false;
                else{
                    return changeDroitsUserProjet(typeDroit, userProjet);
                }
            }
            catch(Exception e){
                System.out.println("Erreur lors du changement de droits d'utilisateur");
                System.out.println(e);
                return false;
            }
        }
        else{ // La chaine typeDroit n'est pas valide
            return false;
        }
    }
    
    /**
     * Change les droits d'un userProjet existant, ou le créé avec ces droits sinon.
     * @param typeDroit Droits à attribuer à l'userProjet
     * @param userProjet UserProjet considéré
     * @return Renvoie vrai si réussite, faux sinon
     */
    public boolean changeDroitsUserProjet(String typeDroit, UserProjet userProjet){

        
        // Vérification que typeDroit est une chaine de caractère valide
        if (typeDroit.equals("admin") || typeDroit.equals("developpeur") || typeDroit.equals("reporteur")){
            userProjet.setTypeDroit(typeDroit);
      
            try{
                em.persist(userProjet);
                return true;
            }
            catch (Exception e){
                System.out.println("Erreur lors du changement de droits utilisateur projet");
                System.out.println(e);
                return false;
            }
        }
        else{ // typeDroit n'est pas une chaine de caractères valide
            return false;
        }
    }

    /**
     * @author Luc Di Sanza
     * Renvoi la liste de tous les utilisateurs qui participent à un projet
     * @param projet Projet considéré
     * @return Renvoie la liste des utilisateurs qui participent au projet
     */
    public List<User> getAllUsersByProjet(Projet projet)
    {      
        // Recherche de users du projet
        TypedQuery<UserProjet> query = em.createNamedQuery("UserProjet.findByIdP", UserProjet.class);
        query.setParameter("idP", projet.getIdProjet());
        List<UserProjet> results = query.getResultList();
        List<User> listUsers = new ArrayList<>();
        for (int i = 0; i < results.size(); i++){
            listUsers.add(results.get(i).getUser());
        }
        return listUsers;
    }
    
    /**
     * @author Luc Di Sanza
     * Renvoi la liste de tous les utilisateurs qui ne participent pas à un projet
     * @param projet Projet considéré
     * @return Renvoie la liste des utilisateurs qui ne participent pas au projet
     */
    public List<User> getAllUsersNotInProjet(Projet projet)
    {            
        // Recherche de tous les users
        TypedQuery<User> query = em.createNamedQuery("User.findAll", User.class);
        List<User> allUsers = query.getResultList();
        
        // Recherche des users du projet
        List<User> usersProjet = getAllUsersByProjet(projet);
        
        // Différence entre les deux
        allUsers.removeAll(usersProjet);
        
        return allUsers;
    }

    /**
     * Renvoi les droits de l'utilisateurs donné associé au projet donné
     * @param user Utilisateur considéré
     * @param projet Projet considéré
     * @return Renvoie les droits de l'utilisateur si réussite, null sinon
     */
    public String getDroits(User user, Projet projet)
    {
        try{
            UserProjet userProjet = getUserProjetByUIdPId(user.getIdUser(), projet.getIdProjet());
            if(userProjet == null){
                return null;
            }
            else{
                return userProjet.getTypeDroit();
            }
        }
        catch(Exception e){
            System.out.println("Erreur pendant la récupération des droits d'un user");
            System.out.println(e);
            return null;
        }
    }
    
}
