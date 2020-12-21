package ImageHoster.repository;

import ImageHoster.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.*;

//The annotation is a special type of @Component annotation which describes that the class defines a data repository
@Repository
public class UserRepository {

    // Get an instance of EntityManagerFactory from persistence unit with name as 'imageHoster'
    @PersistenceUnit(unitName = "imageHoster")
    private EntityManagerFactory emf;

    /**
     * Method to persist new user object in the database
     * The transaction needs to be rolled back in case of any failure/ unsuccessful transaction
     * @param newUser   User object to be persisted in the database
     */
    public void registerUser(User newUser) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            //persist() method changes the state of the model object from transient state to persistence state
            em.persist(newUser);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            em.close();
        }
    }


    /**
     * Method to check if User exists in the database with the username and password passed
     * @param username  String that represents username to be verified in the database
     * @param password  String that represents password to be verified in the database
     * @return          User object if user exists in the database, else return null
     */
    public User checkUser(String username, String password) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<User> typedQuery = em.createQuery("SELECT u FROM User u WHERE u.username = :username" +
                    " AND u.password = :password", User.class);
            typedQuery.setParameter("username", username);
            typedQuery.setParameter("password", password);

            return typedQuery.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        } finally {
            em.close();
        }
    }
}