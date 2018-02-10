many_to_many_attributed (JPA)
=============================
The prerequisite is that you need the DataNucleus Maven2 plugin installed.
You can download this plugin from the DataNucleus downloads area.

1. Set the database configuration in "src/main/resources/META-INF/persistence.xml"

2. Make sure you have the JDBC driver jar specified in the "pom.xml" file

3. Run the command: "mvn clean compile"
   This builds everything, and enhances the classes

4. Run the command: "mvn datanucleus:schema-create"
   This creates the schema

5. Run some persistence code. "mvn clean exec:java"

6. Run the command: "mvn datanucleus:schema-delete"
   This deletes the schema



This sample contains 2 alternates.
The basic attributed relation using derived identity is in package _org.datanucleus.samples.jpa.many_many_attributed_.
A "best practice" alternate using a separate identity field in the intermediate class is in package _org.datanucleus.samples.jpa.many_many_attributed2_.

The "best practice" removes the need for separate "id" classes, and would arguably operate more efficiently