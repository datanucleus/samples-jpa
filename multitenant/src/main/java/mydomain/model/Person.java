package mydomain.model;

import javax.persistence.*;
import org.datanucleus.api.jpa.annotations.*;

@Entity
@MultiTenant(column="TENANT") // Provide column name for multitenant discrim
public class Person
{
    @Id
    Long id;

    String name;

    public Person(long id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    public String getName()
    {
        return name;
    }

    public Long getId()
    {
        return id;
    }
}
