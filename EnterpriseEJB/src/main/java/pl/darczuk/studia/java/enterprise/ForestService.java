package pl.darczuk.studia.java.enterprise;

import lombok.extern.java.Log;
import pl.darczuk.studia.java.enterprise.entities.Elf;
import pl.darczuk.studia.java.enterprise.entities.Forest;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;

//@ManagedBean
//@ViewScoped
//@Log
@Stateless
public class ForestService implements Serializable {

    @PersistenceContext
    EntityManager em;

//    @Resource
//    UserTransaction utx;

    public ForestService() {

    }

    public List<Forest> findAllForest() {
        return em.createNamedQuery(Forest.FIND_ALL, Forest.class).getResultList();
    }

    public Forest findForest(int forestId) {
        return em.find(Forest.class, forestId);
    }

    public Elf findElf(int elfId) {
        return em.find(Elf.class, elfId);
    }

    public void reinforcement(int numberBow) {
        em.createNamedQuery(Elf.REINFORCEMENT).setParameter("numberBow",numberBow).executeUpdate();
    }

    public void saveForest(Forest forest) {
//        transactional(()-> {
//            if (forest.getId() == null) {
//                em.persist(forest);
//            } else {
//                em.merge(forest);
//            }
//        });
        if (forest.getId() == null) {
            em.persist(forest);
        } else {
            em.merge(forest);
        }
    }

    public void saveElf(Elf elf) {
//        transactional(() -> {
//            if (elf.getId() == null) {
//                em.persist(elf);
//            } else {
//                em.merge(elf);
//            }
//        });
        if (elf.getId() == null) {
            em.persist(elf);
        } else {
            em.merge(elf);
        }

    }

    public String removeForest(Forest forest) {
        //transactional(()-> em.remove(em.merge(forest)));
        em.remove(em.merge(forest));
        return "index?faces-redirect=true";
    }

    public String removeElf(Elf elf) {
        //transactional(()-> em.remove(em.merge(elf)));
        em.remove(em.merge(elf));
        return "index?faces-redirect=true";
    }

//    public void transactional(Runnable runnable) {
//        try {
//            utx.begin();
//            runnable.run();
//            utx.commit();
//        } catch (Exception e) {
//            log.log(Level.SEVERE, e.getMessage(), e);
//            try {
//                utx.rollback();
//            } catch (Exception e1) {
//                log.log(Level.SEVERE, e1.getMessage(), e1);
//            }
//        }
//    }
}
