package pl.darczuk.studia.java;

import lombok.extern.java.Log;
import pl.darczuk.studia.java.entities.Elf;
import pl.darczuk.studia.java.entities.Forest;
import pl.darczuk.studia.java.entities.Bow;

import javax.annotation.Resource;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;


@ManagedBean
@ViewScoped
@Log
public class ForestService implements Serializable {

    @PersistenceContext(name = "JEE_PU")
    EntityManager em;

    @Resource
    UserTransaction utx;

    public ForestService() {

    }

    public List<Forest> findAllForest() {
        return em.createNamedQuery(Forest.FIND_ALL, Forest.class).getResultList();
    }

    public Forest findForest(int forestId) {
//      em.createNamedQuery(Forest.REMOVE_BY_ID, Forest.class).setParameter(forestId, 'id');
        return em.find(Forest.class, forestId);
    }

    public void saveForest(Forest forest) {
        transactional(()-> {
            if (forest.getId() == null) {
                em.persist(forest);
            } else {
                em.merge(forest);
            }
        });
    }

    public void saveElf(Elf elf, int oldForestId, int forestId) {
        transactional(() -> {
            if (elf.getId() == null) {
                em.persist(elf);
            } else {
                em.merge(elf);
            }
        });


//        // check if elf need to be move to another forest
//        Forest f;
//        // new elf or stay in this same forest
//        if (forestId == oldForestId || oldForestId == 0)
//            f = findForest(forestId);
//            transactional(()->);
//        // move to another forest
//        else {
//            Elf celf = new Elf(elf.getId(), elf.getName(), elf.getNumberArrows(), elf.getTypeBow());
//            findForest(forestId).saveElf(celf, oldForestId);
//            findForest(oldForestId).removeElf(elf);
//        }


//
//        if (forestId == oldForestId || oldForestId == 0)
//            findForest(forestId).saveElf(elf, forestId);
//        else {
//            Elf celf = new Elf(elf.getId(), elf.getName(), elf.getNumberArrows(), elf.getTypeBow());
//            findForest(forestId).saveElf(celf, oldForestId);
//            findForest(oldForestId).removeElf(elf);
//        }
    }

    public String removeForest(Forest forest) {
//      em.createNamedQuery(Forest.REMOVE_BY_ID, Forest.class).setParameter(forest.getId(), "id"); --> not corect without transaction
//        List<Elf> elfs = em.createNamedQuery(Elf.FIND_BY_ID_FOREST, Elf.class).setParameter(forest.getId(), "id").getResultList();
//        for (Elf elf : elfs) {
//            em.remove(elf);
//        }
        try {
            for (Elf elf : forest.getElfs()) {
                transactional(() -> em.remove(em.merge(elf)));
            }
        }
        finally {
            transactional(()-> em.remove(em.merge(forest)));
        }


        return "index?faces-redirect=true";
    }


    public String removeElf(Elf elf) {
        transactional(()-> em.remove(em.merge(elf)));
        return "index?faces-redirect=true";
    }

    public void transactional(Runnable runnable) {
        try {
            utx.begin();
            runnable.run();
            utx.commit();
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            try {
                utx.rollback();
            } catch (Exception e1) {
                log.log(Level.SEVERE, e1.getMessage(), e1);
            }
        }
    }
}
