# many_to_many_attributed (JPA)

The prerequisite is that you need the DataNucleus Maven2 plugin installed.
You can download this plugin from the DataNucleus downloads area.

* Set the database configuration in "src/main/resources/META-INF/persistence.xml"
* Make sure you have the JDBC driver jar specified in the "pom.xml" file
* Run the command: _mvn clean compile_. This builds everything, and enhances the classes
* Run the command: _mvn datanucleus:schema-create_. This creates the schema
* Run some persistence code. _mvn clean exec:java_.
* Run the command: _mvn datanucleus:schema-delete_. This deletes the schema



# Guide 1 (Derived Identity)

DataNucleus provides support for standard JPA M-N relations where we have a relation between, for example, _Customer_ and _Supplier_, 
where a _Customer_ has many _Suppliers_ and a _Supplier_ has many _Customers_. 
A slight modification on this is where you have the relation carrying some additional attributes of the relation. Let's take some classes

```
@Entity
@IdClass(org.datanucleus.samples.jpa.many_many_attributed.Customer.PK.class)
public class Customer
{
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE)
    private long id;

    private String name;

    @OneToMany(mappedBy="customer")
    private Set supplierRelations = new HashSet();

    ...
}
```

```
@Entity
@IdClass(org.datanucleus.samples.jpa.many_many_attributed.Supplier.PK.class)
public class Supplier
{
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE)
    private long id;

    private String name;

    @OneToMany(mappedBy="supplier")
    private Set customerRelations = new HashSet();

    ...
}
```

Now we obviously can't define an "attributed relation" using Java and just these classes so we invent an intermediate "associative" class, that will also contain the attributes.

```
@Entity
@IdClass(org.datanucleus.samples.jpa.many_many_attributed.BusinessRelation.PK.class)
public class BusinessRelation
{
    @Id
    @ManyToOne
    private Customer customer;

    @Id
    @ManyToOne
    private Supplier supplier;

    private String relationLevel;
    private String meetingLocation;

    public BusinessRelation(Customer cust, Supplier supp, String level, String meeting)
    {
        this.customer = cust;
        this.supplier = supp;
        this.relationLevel = level;
        this.meetingLocation = meeting;
    }
    ...
}
```

The key thing to note is that we mark the related fields as being `@Id` fields, and we use our own defined `@IdClass` for each side of the relation.

So we've used a 1-N "Derived Identity" relation between _Customer_ and _BusinessRelation_, and similarly between _Supplier_ and _BusinessRelation_ 
meaning that _BusinessRelation_ has a composite PK define like this

```
public class BusinessRelation
{
    ...

    public static class PK implements Serializable
    {
        public Customer.PK customer; // Use same name as field in class
        public Supplier.PK supplier; // Use same name as field in class

        public PK()
        {
        }

        public PK(String s)
        {
            StringTokenizer st = new StringTokenizer(s, "::");
            this.customer = new Customer.PK(st.nextToken());
            this.supplier = new Supplier.PK(st.nextToken());
        }

        public String toString()
        {
            return (customer.toString() + "::" + supplier.toString());
        }

        public int hashCode()
        {
            return customer.hashCode() ^ supplier.hashCode();
        }

        public boolean equals(Object other)
        {
            if (other != null && (other instanceof PK))
            {
                PK otherPK = (PK)other;
                return this.customer.equals(otherPK.customer) && this.supplier.equals(otherPK.supplier);
            }
            return false;
        }
    }
}
```

This arrangement will result in the following schema

![schema_image](docs/many_to_many_attributed.jpg)


So all we need to do now is persist some objects using these classes

```
EntityManagerFactory pmf = JDOHelper.getEntityManagerFactory("MyTest");
EntityManager em = emf.createEntityManager();
EntityTransaction tx = em.getTransaction();
Object holderId = null;
try
{
    tx.begin();

    Customer cust1 = new Customer("Web design Inc");
    Supplier supp1 = new Supplier("DataNucleus Corporation");
    em.persist(cust1);
    em.persist(supp1);

    BusinessRelation rel_1_1 = new BusinessRelation(cust1, supp1, "Very Friendly", "Hilton Hotel, London");
    cust1.addRelation(rel_1_1);
    supp1.addRelation(rel_1_1);
    em.persist(rel_1_1);

    tx.commit();
}
finally
{
    if (tx.isActive())
    {
        tx.rollback();
    }
    em.close();
}
```

This will now have persisted an entry in table "CUSTOMER", an entry in table "SUPPLIER", and an entry in table "BUSINESSRELATION". 
We can now utilise the _BusinessRelation_ objects to update the attributes of the M-N relation as we wish.



# Guide 2 (Best Practice, using separate Identity)

Clearly the above example requires the definition of primary key classes, and additionally complicates the relationships.
To change this to use *best practice* you should get rid of the primary key classes, and change the intermediate _BusinessRelation_
to use a separate identity field with generated value. This will now be simpler to understand and run more efficiently.


So we update the classes like this

```
@Entity
public class Customer2
{
    @Id
    @GeneratedValue
    long id;

    String name = null;

    @OneToMany(mappedBy="customer")
    Set<BusinessRelation2> supplierRelations = new HashSet();

    ...
}
```

```
@Entity
public class Supplier2
{
    @Id
    @GeneratedValue
    long id;

    String name = null;

    @OneToMany(mappedBy="supplier")
    Set<BusinessRelation2> customerRelations = new HashSet();

    ...
}
```

```
@Entity
public class BusinessRelation2
{
    @Id
    @GeneratedValue
    long id;

    @ManyToOne
    private Customer2 customer;

    @ManyToOne
    private Supplier2 supplier;

    ...
}
```

The persistence code is identical, and the only change to the persistable classes is to remove the PK classes.
