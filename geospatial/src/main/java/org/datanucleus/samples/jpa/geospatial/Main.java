package org.datanucleus.samples.jpa.geospatial;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.*;

import org.datanucleus.util.NucleusLogger;

import org.postgis.Point;

public class Main
{
    public static void main(String args[])
    throws SQLException
    {
        // Create an EntityManagerFactory for this datastore
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MyTest");

        System.out.println("DataNucleus JPA Geospatial Sample");
        System.out.println("=================================");

        // Persist several Position objects
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx=em.getTransaction();
        try
        {
            //create objects
            tx.begin();

            Position[] sps = new Position[3];
            sps[0] = new Position("market", new Point("SRID=4326;POINT(5 0)"));
            sps[1] = new Position("rent-a-car", new Point("SRID=4326;POINT(10 0)"));
            sps[2] = new Position("pizza shop", new Point("SRID=4326;POINT(20 0)"));

            Point homepoint = new Point("SRID=4326;POINT(0 0)");
            Position home = new Position("home", homepoint);

            System.out.println("Persisting spatial data...");
            System.out.println(home);
            System.out.println(sps[0]);
            System.out.println(sps[1]);
            System.out.println(sps[2]);
            System.out.println("");

            em.persist(sps);
            em.persist(home);

            tx.commit();
 
            // Query for all Positions within a distance
            tx.begin();

            Double distance = new Double(12.0);
            System.out.println("Retrieving Positions where distance to home is less than \"" + distance + "\" ... Found:");

            // TODO Make this more elaborate, demonstrating more of the power of spatial method querying
            Query query = em.createQuery("SELECT p FROM Position p WHERE p.name <> 'home' AND Spatial.distance(p.point, :homepoint) < :distance");
            query.setParameter("homepoint", homepoint);
            query.setParameter("distance", distance);
            List<Position> results = query.getResultList();
            for (Position pos : results)
            {
                System.out.println(pos);
            }

//          Query query = em.createQuery("SELECT p FROM Position p WHERE p.name <> 'home' && (p.point.getX() > 10) AND (p.point.getY() = 0)");
//          List<Position> results = query.getResultList();

            //clean up database
            em.createQuery("DELETE FROM Position p").executeUpdate();

            tx.commit();
        }
        catch (Exception e)
        {
            NucleusLogger.GENERAL.error(">> Exception during tutorial", e);
            System.err.println("Exception thrown during sample execution, consult the log for details : " + e.getMessage());
            return;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            em.close();
        }

        System.out.println("");
        emf.close();
        System.out.println("End of Sample");
    }
}
