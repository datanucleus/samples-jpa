package mydomain.model;

import javax.persistence.*;

@Entity
public class Person
{
    @Id
    Long id;

    String name;

    /** Persist this as either 1 or 0. */
    @Convert(converter=Boolean10Converter.class)
    Boolean myBool1;

    /** Persist this as either Y or N. */
    @Convert(converter=BooleanYNConverter.class)
    Boolean myBool2;

    @Basic
//  @Convert(converter=SimpleAddressStringConverter.class)
    SimpleAddress address;

    public Person(long id, String name)
    {
        this.id = id;
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

    public void setMyBool1(Boolean bool)
    {
        this.myBool1 = bool;
    }
    public Boolean getMyBool1()
    {
        return myBool1;
    }
    public void setMyBool2(Boolean bool)
    {
        this.myBool2 = bool;
    }
    public Boolean getMyBool2()
    {
        return myBool2;
    }

    public void setAddress(SimpleAddress addr)
    {
        this.address = addr;
    }
    public SimpleAddress getSimpleAddress()
    {
        return this.address;
    }
}
