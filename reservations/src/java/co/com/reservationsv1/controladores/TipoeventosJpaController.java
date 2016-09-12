/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.reservationsv1.controladores;

import co.com.reservationsv1.controladores.exceptions.IllegalOrphanException;
import co.com.reservationsv1.controladores.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import co.com.reservationsv1.modelo.Reservas;
import co.com.reservationsv1.modelo.Tipoeventos;
import co.com.reservationsv1.utilidades.JpaUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author disable
 */
public class TipoeventosJpaController implements Serializable {

    public TipoeventosJpaController() {
        
    }
    private EntityManager emf = null;
    private static final Logger LOGGER = Logger.getLogger("Controlador Reservas");

    public EntityManager getEntityManager() {
        JpaUtil jpa = new JpaUtil();
        emf = jpa.getEntityManager();
        return emf;
    }

    public void create(Tipoeventos tipoeventos) {
        if (tipoeventos.getReservasList() == null) {
            tipoeventos.setReservasList(new ArrayList<Reservas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Reservas> attachedReservasList = new ArrayList<Reservas>();
            for (Reservas reservasListReservasToAttach : tipoeventos.getReservasList()) {
                reservasListReservasToAttach = em.getReference(reservasListReservasToAttach.getClass(), reservasListReservasToAttach.getId());
                attachedReservasList.add(reservasListReservasToAttach);
            }
            tipoeventos.setReservasList(attachedReservasList);
            em.persist(tipoeventos);
            for (Reservas reservasListReservas : tipoeventos.getReservasList()) {
                Tipoeventos oldIdtipoeventoOfReservasListReservas = reservasListReservas.getIdtipoevento();
                reservasListReservas.setIdtipoevento(tipoeventos);
                reservasListReservas = em.merge(reservasListReservas);
                if (oldIdtipoeventoOfReservasListReservas != null) {
                    oldIdtipoeventoOfReservasListReservas.getReservasList().remove(reservasListReservas);
                    oldIdtipoeventoOfReservasListReservas = em.merge(oldIdtipoeventoOfReservasListReservas);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tipoeventos tipoeventos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tipoeventos persistentTipoeventos = em.find(Tipoeventos.class, tipoeventos.getId());
            List<Reservas> reservasListOld = persistentTipoeventos.getReservasList();
            List<Reservas> reservasListNew = tipoeventos.getReservasList();
            List<String> illegalOrphanMessages = null;
            for (Reservas reservasListOldReservas : reservasListOld) {
                if (!reservasListNew.contains(reservasListOldReservas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Reservas " + reservasListOldReservas + " since its idtipoevento field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Reservas> attachedReservasListNew = new ArrayList<Reservas>();
            for (Reservas reservasListNewReservasToAttach : reservasListNew) {
                reservasListNewReservasToAttach = em.getReference(reservasListNewReservasToAttach.getClass(), reservasListNewReservasToAttach.getId());
                attachedReservasListNew.add(reservasListNewReservasToAttach);
            }
            reservasListNew = attachedReservasListNew;
            tipoeventos.setReservasList(reservasListNew);
            tipoeventos = em.merge(tipoeventos);
            for (Reservas reservasListNewReservas : reservasListNew) {
                if (!reservasListOld.contains(reservasListNewReservas)) {
                    Tipoeventos oldIdtipoeventoOfReservasListNewReservas = reservasListNewReservas.getIdtipoevento();
                    reservasListNewReservas.setIdtipoevento(tipoeventos);
                    reservasListNewReservas = em.merge(reservasListNewReservas);
                    if (oldIdtipoeventoOfReservasListNewReservas != null && !oldIdtipoeventoOfReservasListNewReservas.equals(tipoeventos)) {
                        oldIdtipoeventoOfReservasListNewReservas.getReservasList().remove(reservasListNewReservas);
                        oldIdtipoeventoOfReservasListNewReservas = em.merge(oldIdtipoeventoOfReservasListNewReservas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoeventos.getId();
                if (findTipoeventos(id) == null) {
                    throw new NonexistentEntityException("The tipoeventos with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tipoeventos tipoeventos;
            try {
                tipoeventos = em.getReference(Tipoeventos.class, id);
                tipoeventos.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoeventos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Reservas> reservasListOrphanCheck = tipoeventos.getReservasList();
            for (Reservas reservasListOrphanCheckReservas : reservasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tipoeventos (" + tipoeventos + ") cannot be destroyed since the Reservas " + reservasListOrphanCheckReservas + " in its reservasList field has a non-nullable idtipoevento field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipoeventos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tipoeventos> findTipoeventosEntities() {
        return findTipoeventosEntities(true, -1, -1);
    }

    public List<Tipoeventos> findTipoeventosEntities(int maxResults, int firstResult) {
        return findTipoeventosEntities(false, maxResults, firstResult);
    }

    private List<Tipoeventos> findTipoeventosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tipoeventos.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Tipoeventos findTipoeventos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tipoeventos.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoeventosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tipoeventos> rt = cq.from(Tipoeventos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
