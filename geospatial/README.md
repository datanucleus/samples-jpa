# Geospatial


This requires the DataNucleus Maven plugin. You can download this plugin from the DataNucleus downloads area.

* Set the database configuration in the "src/main/resources/META-INF/persistence.xml" file.
* Make sure you have the JDBC driver jar specified in the "pom.xml" file
* Run the command: "mvn clean compile". This builds everything, and enhances the classes
* Run the command: "mvn datanucleus:schema-create". This creates the schema for this sample.
* Run the command: "mvn exec:java". This runs the tutorial
* Run the command: "mvn datanucleus:schema-delete". This deletes the schema for this sample.


# Guide : Persistence of Geospatial Data using JPA


_dataNucleus-geospatial_ allows the use of DataNucleus as persistence layer for geospatial applications in an environment that supports the OGC SFA specification. 
It allows the persistence of the Java geometry types from the JTS topology suite as well as those from the PostGIS project.

In this tutorial, we perform the basic persistence operations over geospatial types using MySQL/MariaDB and Postgis products.

* Step 1 : Install the database server and geospatial extensions.
* Step 2 : Download DataNucleus and PostGis libraries.
* Step 3 : Design and implement the persistent data model.
* Step 4 : Design and implement the persistent code.
* Step 5 : Run your application.



## Step 1 : Install the database server and geospatial extensions

Download MySQL/MariaDB database and PostGIS. Install MySQL/MariaDB and PostGis. 
During PostGis installation, you will be asked to select the database schema where the geospatial extensions will be enabled. 
You will use this schema to run the tutorial application.


## Step 2 : Download DataNucleus and PostGis libraries

Download DataNucleus core, RDBMS and Geospatial jars and any dependencies. 
Configure your development environment by adding the PostGIS and JPA jars to the classpath.


## Step 3 : Design and implement the persistent data model

```
package org.datanucleus.samples.jpa.geospatial;

import org.postgis.Point;
import javax.persistence.*;
import org.datanucleus.api.jpa.annotations.*;

@Entity
@Table(name="spatial_positions")
@Extension(key="spatial-dimension", value="2")
@Extension(key="spatial-srid", value="4326")
public class Position
{
    private String name;

    private Point point;

    public Position(String name, Point point)
    {
        this.name = name;
        this.point = point;
    }

    public String getName()
    {
        return name;
    }
    
    public Point getPoint()
    {
        return point;
    }
    
    public String toString()
    {
        return "[name] "+ name + " [point] "+point;
    }
}
```

The above JPA metadata has two extensions _spatial-dimension_ and _spatial-srid_. 
These settings specifies the format of the geospatial data. _SRID_ stands for spatial referencing system identifier and _Dimension_ the number of coordinates.


## Step 4 : Design and implement the persistent code

In this tutorial, we query for all locations where the X coordinate is greater than 10 and Y coordinate is 0.

```
package org.datanucleus.samples.jpa.geospatial;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.*;

import org.postgis.Point;

public class Main
{
    public static void main(String args[]) throws SQLException
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
            
            //query for the distance
            tx.begin();

            Double distance = new Double(12.0);
            System.out.println("Retrieving Positions where distance to home is less than \"" + distance + "\" ... Found:");

            Query query = em.createQuery("SELECT p FROM Position p WHERE p.name <> 'home' AND Spatial.distance(p.point, :homepoint) < :distance");
            query.setParameter("homepoint", homepoint);
            query.setParameter("distance", distance);
            List<Position> results = query.getResultList();
            for (Position pos : results)
            {
                System.out.println(pos);
            }

            // clean up database
            em.createQuery("DELETE FROM Position p").executeUpdate();

            tx.commit();
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }

        System.out.println("");
        emf.close();
        System.out.println("End of Tutorial");
    }
}
```

We define a `persistence.xml` file with connection properties to MySQL

```
<?xml version="1.0" encoding="UTF-8" ?>
<persistence xmlns="http://java.sun.com/xml/ns/persistence"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_1_0.xsd"
    version="1.0">

    <persistence-unit name="MyTest">
        <mapping-file>org/datanucleus/samples/jdo/spatial/package.jdo</mapping-file>
        <exclude-unlisted-classes />
        <properties>
            <property name="javax.jdo.option.ConnectionURL" value="jdbc:mysql://127.0.0.1/nucleus"/>
            <property name="javax.jdo.option.ConnectionDriverName" value="com.mysql.jdbc.Driver"/>
            <property name="javax.jdo.option.ConnectionUserName" value="mysql"/>
            <property name="javax.jdo.option.ConnectionPassword" value=""/>

            <property name="datanucleus.schema.autoCreateAll" value="true"/>
            <property name="datanucleus.schema.autoCreateColumns" value="true"/>
        </properties>
    </persistence-unit>

</persistence>
```


## Step 5 : Run your application

Before running the application, you must enhance the persistent classes.
Finally, configure the application classpath with the DataNucleus Core, DataNucleus RDBMS, DataNucleus Geospatial, JDO, MySQL and PostGis libraries 
and run the application as any other java application.

The output for the application is:

```
DataNucleus JDO Spatial Sample
==============================
Persisting spatial data...
[name] home [point] SRID=4326;POINT(0 0)
[name] market [point] SRID=4326;POINT(5 0)
[name] rent-a-car [point] SRID=4326;POINT(10 0)
[name] pizza shop [point] SRID=4326;POINT(20 0)

Retrieving position where X position is > 10 and Y position is 0 ... Found:
[name] pizza shop [point] SRID=4326;POINT(20 0)

End of Sample
```
