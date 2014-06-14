/**********************************************************************
Copyright (c) 2014 Andy Jefferson and others. All rights reserved.
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
package org.datanucleus.samples.jpa.many_many_attributed;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.datanucleus.util.NucleusLogger;

public class Main
{
    public static void main(String[] args)
    {
        System.out.println("DataNucleus Samples : M-N Relation (attributed)");
        System.out.println("===============================================");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MyTest");

        Customer cust1 = null;
        Customer cust2 = null;
        Supplier supp1 = null;
        Supplier supp2 = null;
        Supplier supp3 = null;

        // Persist some objects
        System.out.println(">> Persisting some Customers and Suppliers");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try
        {
            tx.begin();

            cust1 = new Customer("DFG Stores");
            cust2 = new Customer("Kevins Cards");
            supp1 = new Supplier("Stationery Direct");
            supp2 = new Supplier("Grocery Wholesale");
            supp3 = new Supplier("Makro");

            em.persist(cust1);
            em.persist(cust2);
            em.persist(supp1);
            em.persist(supp2);
            em.persist(supp3);

            // We have javax.jdo.option.DetachAllOnCommit set, so all get detached at this point
            tx.commit();
        }
        catch (Exception e)
        {
            NucleusLogger.GENERAL.error(">> Exception in persist", e);
            System.exit(1);
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            em.close();
        }

        // Establish the relationships
        System.out.println(">> Adding relationships between Customers and Suppliers");

        // Establish the relation customer1 uses supplier2
        BusinessRelation rel1 = new BusinessRelation(cust1, supp2, "Friendly", "Hilton Hotel, London");
        cust1.addRelation(rel1);
        supp2.addRelation(rel1);

        // Establish the relation customer2 uses supplier1
        BusinessRelation rel2 = new BusinessRelation(cust2, supp1, "Frosty", "M61 motorway service station junction 23");
        cust2.addRelation(rel2);
        supp1.addRelation(rel2);

        em = emf.createEntityManager();
        tx = em.getTransaction();
        try
        {
            tx.begin();

            // Reattach the changed objects
            em.persist(rel1);
            em.persist(rel2);

            // detach all on commit, so all get (re)detached at this point
            tx.commit();
        }
        catch (Exception e)
        {
            NucleusLogger.GENERAL.error(">> Exception in reattach", e);
            System.exit(2);
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            em.close();
        }

        // Print out the results
        System.out.println(">> Customer1 has " + cust1.getNumberOfRelations() + " relations");
        System.out.println(">> Customer2 has " + cust2.getNumberOfRelations() + " relations");
        System.out.println(">> Supplier1 has " + supp1.getNumberOfRelations() + " relations");
        System.out.println(">> Supplier2 has " + supp2.getNumberOfRelations() + " relations");
    }
}
