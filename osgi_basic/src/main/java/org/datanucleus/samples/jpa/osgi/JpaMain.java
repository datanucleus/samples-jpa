/**********************************************************************
Copyright (c) 2012 Andy Jefferson and others. All rights reserved.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Contributors:
    ...
**********************************************************************/
package org.datanucleus.samples.jpa.osgi;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Persistence;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.datanucleus.util.NucleusLogger;

/**
 * Sample JPA main to demonstrate the creation of EMF, persistence, query, and delete.
 */
public class JpaMain
{
    private int MAX_NUM_ITERATIONS = 10;    

    public JpaMain()
    {
        super();        
    }

    /**
     * spring bean as init method
     */
    public void performJpaPersistence()
    {
        System.out.println("DataNucleus:OSGi:JPA - starting");

        // Create EMF using additional properties needed that can't be specified in persistence.xml
        Map<String, Object> overrideProps = new HashMap<String, Object>();
        overrideProps.put("datanucleus.primaryClassLoader", this.getClass().getClassLoader());
        overrideProps.put("datanucleus.plugin.pluginRegistryClassName", "org.datanucleus.plugin.OSGiPluginRegistry");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PU", overrideProps);
        System.out.println("DataNucleus:OSGi:JPA - EMF created");

        // Create EM to persist a number of objects
        EntityManager em = emf.createEntityManager();
        System.out.println("DataNucleus:OSGi:JPA - EM created");

        EntityTransaction tx = em.getTransaction();
        try
        {
            tx.begin();
            Person p;
            for (int i=0; i<MAX_NUM_ITERATIONS; i++)
            {
                p = new Person(i, "Name"+i, "Address"+i, 20+i);
                em.persist(p);
            }
            tx.commit();
            System.out.println("DataNucleus:OSGi:JPA - " + MAX_NUM_ITERATIONS + " Person objects have been persisted");
        }
        catch (Exception e)
        {
            NucleusLogger.GENERAL.info(">> Exception in query", e);
            e.printStackTrace();
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            em.close();
            System.out.println("DataNucleus:OSGi:JPA - EM closed");
        }

        // Create EM to query the objects
        em = emf.createEntityManager();
        System.out.println("DataNucleus:OSGi:JPA - EM created");
        tx = em.getTransaction();
        try
        {
            tx.begin();
            Query q = em.createQuery("SELECT max(p.id) FROM Person p");
            Object result = q.getSingleResult();
            System.out.println("DataNucleus:OSGi:JPA - max(id)=" + result);
            NucleusLogger.GENERAL.info(">> Query done, max=" + result);
            tx.commit();
        }
        catch (Exception e)
        {
            NucleusLogger.GENERAL.info(">> Exception in query", e);
            e.printStackTrace();
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            em.close();
            System.out.println("DataNucleus:OSGi:JPA - EM closed");
        }

        // Create EM to delete the objects
        em = emf.createEntityManager();
        System.out.println("DataNucleus:OSGi:JPA - EM created");
        tx = em.getTransaction();
        try
        {
            tx.begin();
            Query query = em.createQuery("DELETE FROM Person p");
            int numberInstancesDeleted = query.executeUpdate();
            System.out.println("DataNucleus:OSGi:JPA - number of objects deleted=" + numberInstancesDeleted);
            tx.commit();
        }
        catch (Exception e)
        {
            NucleusLogger.GENERAL.info(">> Exception in query", e);
            e.printStackTrace();
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            em.close();
            System.out.println("DataNucleus:OSGi:JPA - EM closed");
        }

        System.out.println("DataNucleus:OSGi:JPA - ended");
    }
}
