package gr.aueb.cf.pharmapp;

import gr.aueb.cf.pharmapp.security.SecurityUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class App {

    private static EntityManagerFactory emf ;



    public static void main(String[] args) {

        emf = Persistence.createEntityManagerFactory("phtradingPU");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        System.out.println("Database connected: " + em.isOpen());
        em.close();

            String testPass = "54321";
            String hash = SecurityUtil.hashPassword(testPass);
            System.out.println("Generated hash: " + hash);
            System.out.println("Verification: " +
                    SecurityUtil.isPasswordValid(testPass, hash));

    }

    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("phtradingPU");
        }
        return emf;
    }
}
