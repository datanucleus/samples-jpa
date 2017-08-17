package mydomain.model;

public class SimpleAddress
{
    String street;

    String city;

    public SimpleAddress(String street, String city)
    {
        this.city = city;
        this.street = street;
    }

    public String getStreet()
    {
        return street;
    }

    public String getCity()
    {
        return city;
    }

    public String toString()
    {
        return street + ", " + city;
    }
}
