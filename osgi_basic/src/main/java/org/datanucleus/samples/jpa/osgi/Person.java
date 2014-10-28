/**********************************************************************
Copyright (c) 2012 Andy Jefferson and others. All rights reserved.
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
package org.datanucleus.samples.jpa.osgi;

import java.io.Serializable;
import javax.persistence.Id;
import javax.persistence.Basic;
import javax.persistence.Entity;

@Entity
public class Person implements Serializable
{
    private static final long serialVersionUID = 3756735282867141477L;

    @Id
    private long id;
    
 	@Basic
    private String name;
    
 	@Basic
    private String address;
    
 	@Basic
    private long age;

	public Person(long id, String name, String address, long age)
	{
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.age = age;
	}
       
	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getAddress()
	{
		return address;
	}

	public void setStudentAdd(String address)
	{
		this.address = address;
	}

	public long getAge()
	{
		return age;
	}

	public void setAge(long age)
	{
		this.age = age;
	}
}
