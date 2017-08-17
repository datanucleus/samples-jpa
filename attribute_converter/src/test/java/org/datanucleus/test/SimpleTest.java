package org.datanucleus.test;

import org.junit.*;
import java.util.*;
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
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MyTest");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try
        {
            tx.begin();

            Person p1 = new Person(1, "First Person");
            p1.setMyBool1(Boolean.TRUE);
            p1.setMyBool2(Boolean.TRUE);
            p1.setAddress(new SimpleAddress("Main Street", "Edinburgh"));
            em.persist(p1);

            Person p2 = new Person(2, "Second Person");
            p2.setMyBool1(Boolean.FALSE);
            p2.setMyBool2(Boolean.FALSE);
            p2.setAddress(new SimpleAddress("Church Street", "Glasgow"));
            em.persist(p2);

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

            Query q = em.createQuery("SELECT p.myBool1,p.address FROM Person p");
            List results = q.getResultList();
            for (Object result : results)
            {
                Object[] row = (Object[])result;
                NucleusLogger.GENERAL.info(">> result 0=" + row[0] + " 1=" + row[1]);
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
