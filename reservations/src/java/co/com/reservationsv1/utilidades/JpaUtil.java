/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.reservationsv1.utilidades;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 *
 * @author ctg114
 */
public class JpaUtil {
    private static final String PERSISTENCE_UNIT_NAME = "reservationsPU";

    private static ThreadLocal<EntityManager> manager = new ThreadLocal<EntityManager>();

    private static EntityManagerFactory factory;

    public JpaUtil() {
    }

    public  boolean isEntityManagerOpen(){
            return JpaUtil.manager.get() != null && JpaUtil.manager.get().isOpen();
    }

    public EntityManager getEntityManager() {
            if (JpaUtil.factory == null) {
                    JpaUtil.factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            }
            EntityManager em = JpaUtil.manager.get();
            if (em == null || !em.isOpen()) {
                    em = JpaUtil.factory.createEntityManager();
                    JpaUtil.manager.set(em);
            }
            return em;
    }

    public void closeEntityManager() {
            EntityManager em = JpaUtil.manager.get();
            if (em != null) {
                    EntityTransaction tx = em.getTransaction();
                    if (tx.isActive()) { 
                            tx.commit();
                    }
                    em.close();
                    JpaUtil.manager.set(null);
            }
    }

    public void closeEntityManagerFactory(){
            closeEntityManager();
            JpaUtil.factory.close();
    }
}
