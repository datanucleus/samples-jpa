package org.datanucleus.test;

import java.util.*;
import org.junit.*;
import javax.persistence.*;

import static org.junit.Assert.*;
import mydomain.model.*;
import org.datanucleus.util.NucleusLogger;

public class SimpleTest
{
    @Test
    public void testSimple()
    {
        NucleusLogger.GENERAL.info(">> test START");

        // Enable this to specify the tenant name via the "provider"
        Map props = new HashMap();
//      props.put("datanucleus.TenantProvider", new MyTenancyProvider());
  
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MyTest", props);

        EntityManager em = emf.createEntityManager();

        // Enable this to specify the tenant name per PM
//      em.setProperty("datanucleus.tenantID", "First");
        EntityTransaction tx = em.getTransaction();
        try
        {
            tx.begin();

            NucleusLogger.GENERAL.info(">> pm.makePersistent");
            Person p = new Person(1, "First Person");
            em.persist(p);
            NucleusLogger.GENERAL.info(">> pm.makePersistent DONE");

            tx.commit();
        }
        catch (Throwable thr)
        {
            NucleusLogger.GENERAL.error(">> Exception in test", thr);
            fail("Failed test : " + thr.getMessage());
        }
        finally 
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            em.close();
        }
        emf.getCache().evictAll();

        em = emf.createEntityManager();

        // Enable this to specify the tenant name per PM
//      em.setProperty("datanucleus.tenantID", "Second");
        tx = em.getTransaction();
        try
        {
            tx.begin();

            NucleusLogger.GENERAL.info(">> pm.getObjectById SECOND");
            Person p = em.find(Person.class, 1);
            NucleusLogger.GENERAL.info(">> pm.getObjectById returned " + p);
            NucleusLogger.GENERAL.info(">> UPDATE field");
            p.setName("Second Person");
            NucleusLogger.GENERAL.info(">> UPDATE done");

            tx.commit();
        }
        catch (Throwable thr)
        {
            NucleusLogger.GENERAL.error(">> Exception in test", thr);
            fail("Failed test : " + thr.getMessage());
        }
        finally 
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            em.close();
        }
        emf.getCache().evictAll();

        em = emf.createEntityManager();
        tx = em.getTransaction();
        try
        {
            tx.begin();

            NucleusLogger.GENERAL.info(">> pm.newQuery");
            Query q = em.createQuery("SELECT p FROM Person p");
            List<Person> people = q.getResultList();
            NucleusLogger.GENERAL.info(">> query.execute returned " + people.size());
            for (Person p : people)
            {
                NucleusLogger.GENERAL.info(">> query.result=" + p);
            }

            tx.commit();
        }
        catch (Throwable thr)
        {
            NucleusLogger.GENERAL.error(">> Exception in test", thr);
            fail("Failed test : " + thr.getMessage());
        }
        finally 
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            em.close();
        }

        emf.close();
        NucleusLogger.GENERAL.info(">> test END");
    }
}
