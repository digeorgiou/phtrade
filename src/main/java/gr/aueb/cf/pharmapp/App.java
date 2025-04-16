package gr.aueb.cf.pharmapp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

//public class App {
//
//    private static EntityManagerFactory emf ;
//
//
//
//    public static void main(String[] args) {
//
//        emf = Persistence.createEntityManagerFactory("phtradingPU");
//        EntityManager em = emf.createEntityManager();
//
//        em.getTransaction().begin();
//        System.out.println("Database connected: " + em.isOpen());
//        em.close();
//
//    }
//
//    public static EntityManagerFactory getEntityManagerFactory() {
//        if (emf == null) {
//            emf = Persistence.createEntityManagerFactory("phtradingPU");
//        }
//        return emf;
//    }
//}
