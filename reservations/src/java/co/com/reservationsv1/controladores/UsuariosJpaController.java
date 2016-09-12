/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.reservationsv1.controladores;

import co.com.reservationsv1.controladores.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import co.com.reservationsv1.modelo.Reservas;
import co.com.reservationsv1.modelo.Usuarios;
import co.com.reservationsv1.utilidades.JpaUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 *
 * @author disable
 */
public class UsuariosJpaController implements Serializable {

    public UsuariosJpaController() {
        
    }
    private EntityManager emf = null;
    private static final Logger LOGGER = Logger.getLogger("Controlador usuarios");

    public EntityManager getEntityManager() {
        JpaUtil jpa = new JpaUtil();
        emf = jpa.getEntityManager();
        return emf;
    }

    public void create(Usuarios usuarios) {
        if (usuarios.getReservasList() == null) {
            usuarios.setReservasList(new ArrayList<Reservas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Reservas> attachedReservasList = new ArrayList<Reservas>();
            for (Reservas reservasListReservasToAttach : usuarios.getReservasList()) {
                reservasListReservasToAttach = em.getReference(reservasListReservasToAttach.getClass(), reservasListReservasToAttach.getId());
                attachedReservasList.add(reservasListReservasToAttach);
            }
            usuarios.setReservasList(attachedReservasList);
            em.persist(usuarios);
            for (Reservas reservasListReservas : usuarios.getReservasList()) {
                Usuarios oldIdusuarioOfReservasListReservas = reservasListReservas.getIdusuario();
                reservasListReservas.setIdusuario(usuarios);
                reservasListReservas = em.merge(reservasListReservas);
                if (oldIdusuarioOfReservasListReservas != null) {
                    oldIdusuarioOfReservasListReservas.getReservasList().remove(reservasListReservas);
                    oldIdusuarioOfReservasListReservas = em.merge(oldIdusuarioOfReservasListReservas);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuarios usuarios) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios persistentUsuarios = em.find(Usuarios.class, usuarios.getId());
            List<Reservas> reservasListOld = persistentUsuarios.getReservasList();
            List<Reservas> reservasListNew = usuarios.getReservasList();
            List<Reservas> attachedReservasListNew = new ArrayList<Reservas>();
            for (Reservas reservasListNewReservasToAttach : reservasListNew) {
                reservasListNewReservasToAttach = em.getReference(reservasListNewReservasToAttach.getClass(), reservasListNewReservasToAttach.getId());
                attachedReservasListNew.add(reservasListNewReservasToAttach);
            }
            reservasListNew = attachedReservasListNew;
            usuarios.setReservasList(reservasListNew);
            usuarios = em.merge(usuarios);
            for (Reservas reservasListOldReservas : reservasListOld) {
                if (!reservasListNew.contains(reservasListOldReservas)) {
                    reservasListOldReservas.setIdusuario(null);
                    reservasListOldReservas = em.merge(reservasListOldReservas);
                }
            }
            for (Reservas reservasListNewReservas : reservasListNew) {
                if (!reservasListOld.contains(reservasListNewReservas)) {
                    Usuarios oldIdusuarioOfReservasListNewReservas = reservasListNewReservas.getIdusuario();
                    reservasListNewReservas.setIdusuario(usuarios);
                    reservasListNewReservas = em.merge(reservasListNewReservas);
                    if (oldIdusuarioOfReservasListNewReservas != null && !oldIdusuarioOfReservasListNewReservas.equals(usuarios)) {
                        oldIdusuarioOfReservasListNewReservas.getReservasList().remove(reservasListNewReservas);
                        oldIdusuarioOfReservasListNewReservas = em.merge(oldIdusuarioOfReservasListNewReservas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuarios.getId();
                if (findUsuarios(id) == null) {
                    throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.");
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
            Usuarios usuarios;
            try {
                usuarios = em.getReference(Usuarios.class, id);
                usuarios.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.", enfe);
            }
            List<Reservas> reservasList = usuarios.getReservasList();
            for (Reservas reservasListReservas : reservasList) {
                reservasListReservas.setIdusuario(null);
                reservasListReservas = em.merge(reservasListReservas);
            }
            em.remove(usuarios);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuarios> findUsuariosEntities() {
        return findUsuariosEntities(true, -1, -1);
    }

    public List<Usuarios> findUsuariosEntities(int maxResults, int firstResult) {
        return findUsuariosEntities(false, maxResults, firstResult);
    }

    private List<Usuarios> findUsuariosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuarios.class));
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

    public Usuarios findUsuarios(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuarios.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuariosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuarios> rt = cq.from(Usuarios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    public boolean login(Usuarios usuario) {
        boolean a = false;
        try {
            
            Usuarios user = new Usuarios();
            EntityManager em = getEntityManager();
            TypedQuery<Usuarios> query = em.createNamedQuery("Usuarios.login", Usuarios.class);
            query.setParameter("nombre", usuario.getNombre());
            query.setParameter("password", usuario.getPassword());
            user = query.getSingleResult();
            if(user.getNombre().equalsIgnoreCase(usuario.getNombre()) && user.getPassword().equalsIgnoreCase(usuario.getPassword())){
                a = true;
                return a;
            }
        } catch (NoResultException e) {
            LOGGER.log(Level.WARNING, "Usuario no existe");
        }
        return a;
    }
}
