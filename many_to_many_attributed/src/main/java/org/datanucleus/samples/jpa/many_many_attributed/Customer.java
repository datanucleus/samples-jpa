/**********************************************************************
Copyright (c) 2006 Andy Jefferson and others. All rights reserved.
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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.OneToMany;

@Entity
@IdClass(org.datanucleus.samples.jpa.many_many_attributed.Customer.PK.class)
public class Customer
{
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE)
    long id;

    String name = null;

    @OneToMany(mappedBy="customer")
    Set<BusinessRelation> supplierRelations = new HashSet();

    public Customer(String name)
    {
        this.name = name;
    }

    public void addRelation(BusinessRelation rel)
    {
        supplierRelations.add(rel);
    }

    public void removeRelation(BusinessRelation rel)
    {
        supplierRelations.remove(rel);
    }

    public Set<BusinessRelation> getRelations()
    {
        return supplierRelations;
    }

    public int getNumberOfRelations()
    {
        return supplierRelations.size();
    }

    public String toString()
    {
        return "Customer : " + name + " - " + supplierRelations.size() + " suppliers";
    }

    public static class PK implements Serializable
    {
        private static final long serialVersionUID = 7596143906460627568L;
        public long id;

        public PK()
        {
        }

        public PK(java.lang.String str)
        {
            java.util.StringTokenizer token = new java.util.StringTokenizer(str, "::");
            token.nextToken(); // Class name
            this.id = Long.valueOf(token.nextToken());
        }

        public java.lang.String toString()
        {
            return Customer.class.getName() + "::" + java.lang.String.valueOf(this.id);
        }

        public int hashCode()
        {
            return (int) id;
        }

        public boolean equals(Object o)
        {
            if (this == o)
            {
                return true;
            }
            if (o == null)
            {
                return false;
            }
            if (o.getClass() != getClass())
            {
                return false;
            }
            PK objToCompare = (PK) o;
            return ((this.id == objToCompare.id));
        }
    }
}
