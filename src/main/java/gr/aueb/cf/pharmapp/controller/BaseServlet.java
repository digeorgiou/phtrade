package gr.aueb.cf.pharmapp.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.servlet.http.HttpServlet;

import java.util.function.Consumer;

public abstract class BaseServlet extends HttpServlet {

    protected EntityManagerFactory getEntityManagerFactory() {
        return (EntityManagerFactory) getServletContext().getAttribute("emf");
    }

    protected EntityManager createEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    protected void executeInTransaction(Consumer<EntityManager> operation) {
        EntityManager em = createEntityManager();
        EntityTransaction tx = null;

        try {
            tx = em.getTransaction();
            tx.begin();

            operation.accept(em);

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Transaction failed", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}