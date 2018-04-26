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
    @Id
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
