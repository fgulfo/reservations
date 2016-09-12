/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.reservationsv1.controladores;

import co.com.reservationsv1.controladores.exceptions.NonexistentEntityException;
import co.com.reservationsv1.modelo.Reservas;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import co.com.reservationsv1.modelo.Tipoeventos;
import co.com.reservationsv1.modelo.Usuarios;
import co.com.reservationsv1.utilidades.JpaUtil;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author disable
 */
public class ReservasJpaController implements Serializable {

    public ReservasJpaController() {
        
    }
    private EntityManager emf = null;
    private static final Logger LOGGER = Logger.getLogger("Controlador Reservas");

    public EntityManager getEntityManager() {
        JpaUtil jpa = new JpaUtil();
        emf = jpa.getEntityManager();
        return emf;
    }

    public void create(Reservas reservas) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tipoeventos idtipoevento = reservas.getIdtipoevento();
            if (idtipoevento != null) {
                idtipoevento = em.getReference(idtipoevento.getClass(), idtipoevento.getId());
                reservas.setIdtipoevento(idtipoevento);
            }
            Usuarios idusuario = reservas.getIdusuario();
            if (idusuario != null) {
                idusuario = em.getReference(idusuario.getClass(), idusuario.getId());
                reservas.setIdusuario(idusuario);
            }
            em.persist(reservas);
            if (idtipoevento != null) {
                idtipoevento.getReservasList().add(reservas);
                idtipoevento = em.merge(idtipoevento);
            }
            if (idusuario != null) {
                idusuario.getReservasList().add(reservas);
                idusuario = em.merge(idusuario);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Reservas reservas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Reservas persistentReservas = em.find(Reservas.class, reservas.getId());
            Tipoeventos idtipoeventoOld = persistentReservas.getIdtipoevento();
            Tipoeventos idtipoeventoNew = reservas.getIdtipoevento();
            Usuarios idusuarioOld = persistentReservas.getIdusuario();
            Usuarios idusuarioNew = reservas.getIdusuario();
            if (idtipoeventoNew != null) {
                idtipoeventoNew = em.getReference(idtipoeventoNew.getClass(), idtipoeventoNew.getId());
                reservas.setIdtipoevento(idtipoeventoNew);
            }
            if (idusuarioNew != null) {
                idusuarioNew = em.getReference(idusuarioNew.getClass(), idusuarioNew.getId());
                reservas.setIdusuario(idusuarioNew);
            }
            reservas = em.merge(reservas);
            if (idtipoeventoOld != null && !idtipoeventoOld.equals(idtipoeventoNew)) {
                idtipoeventoOld.getReservasList().remove(reservas);
                idtipoeventoOld = em.merge(idtipoeventoOld);
            }
            if (idtipoeventoNew != null && !idtipoeventoNew.equals(idtipoeventoOld)) {
                idtipoeventoNew.getReservasList().add(reservas);
                idtipoeventoNew = em.merge(idtipoeventoNew);
            }
            if (idusuarioOld != null && !idusuarioOld.equals(idusuarioNew)) {
                idusuarioOld.getReservasList().remove(reservas);
                idusuarioOld = em.merge(idusuarioOld);
            }
            if (idusuarioNew != null && !idusuarioNew.equals(idusuarioOld)) {
                idusuarioNew.getReservasList().add(reservas);
                idusuarioNew = em.merge(idusuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = reservas.getId();
                if (findReservas(id) == null) {
                    throw new NonexistentEntityException("The reservas with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Reservas reservas;
            try {
                reservas = em.getReference(Reservas.class, id);
                reservas.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The reservas with id " + id + " no longer exists.", enfe);
            }
            Tipoeventos idtipoevento = reservas.getIdtipoevento();
            if (idtipoevento != null) {
                idtipoevento.getReservasList().remove(reservas);
                idtipoevento = em.merge(idtipoevento);
            }
            Usuarios idusuario = reservas.getIdusuario();
            if (idusuario != null) {
                idusuario.getReservasList().remove(reservas);
                idusuario = em.merge(idusuario);
            }
            em.remove(reservas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Reservas> findReservasEntities() {
        return findReservasEntities(true, -1, -1);
    }

    public List<Reservas> findReservasEntities(int maxResults, int firstResult) {
        return findReservasEntities(false, maxResults, firstResult);
    }

    private List<Reservas> findReservasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Reservas.class));
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

    public Reservas findReservas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Reservas.class, id);
        } finally {
            em.close();
        }
    }

    public int getReservasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Reservas> rt = cq.from(Reservas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
