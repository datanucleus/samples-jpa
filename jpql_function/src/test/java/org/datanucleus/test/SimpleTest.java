package org.datanucleus.test;

import java.util.List;

import org.junit.*;
import javax.persistence.*;

import static org.junit.Assert.*;

import org.datanucleus.samples.SampleHolder;
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

            SampleHolder holder1 = new SampleHolder(1, "45");
            em.persist(holder1);
            SampleHolder holder2 = new SampleHolder(2, "523");
            em.persist(holder2);

            tx.commit();
        }
        catch (Throwable thr)
        {
            NucleusLogger.GENERAL.error(">> Exception thrown persisting data", thr);
            fail("Failed to persist data : " + thr.getMessage());
        }
        finally 
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            em.close();
        }

        em = emf.createEntityManager();
        tx = em.getTransaction();
        try
        {
            tx.begin();

            Query q = em.createQuery("SELECT h FROM " + SampleHolder.class.getName() + " h WHERE LENGTHOFSTRING(stringHoldingLong) = 2");
            List<SampleHolder> holders = q.getResultList();
            assertNotNull(holders);
            assertEquals(1, holders.size());
            SampleHolder holder = holders.get(0);
            assertEquals(1, holder.getId());
            assertEquals("45", holder.getStringHoldingLong());

            tx.commit();
        }
        catch (Throwable thr)
        {
            NucleusLogger.GENERAL.error(">> Exception thrown querying data", thr);
            fail("Failed to query data : " + thr.getMessage());
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
