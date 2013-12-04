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
package org.datanucleus.samples.jpa.tutorial;

import javax.persistence.Basic;
import javax.persistence.Entity;

/**
 * Definition of a Book. Extends basic Product class.
 *
 * @version $Revision: 1.2 $
 **/
@Entity
public class Book extends Product
{
    /** Author of the Book. */
    @Basic
    protected String author = null;

    /** ISBN number of the book. */
    @Basic
    protected String isbn = null;

    /** Publisher of the Book. */
    @Basic
    protected String publisher = null;

    /**
     * Default Constructor.
     **/
    protected Book()
    {
        super();
    }

    /**
     * Constructor.
     * @param name name of product
     * @param description description of product
     * @param price Price
     * @param author Author of the book
     * @param isbn ISBN number of the book
     * @param publisher Name of publisher of the book 
     **/
    public Book(String name,
                String description,
                double price,
                String author,
                String isbn,
                String publisher)
    {
        super(name,description,price);

        this.author = author;
        this.isbn = isbn;
        this.publisher = publisher;
    }

    /**
     * Accessor for the author of the book.
     * @return Author of the book.
     */
    public String getAuthor()
    {
        return author;
    }

    /**
     * Accessor for the isbn of the book.
     * @return ISBN of the book.
     */
    public String getIsbn()
    {
        return isbn;
    }

    /**
     * Accessor for the publisher of the book.
     * @return Publisher of the book.
     */
    public String getPublisher()
    {
        return publisher;
    }

    /**
     * Mutator for the author of the book.
     * @param author Author of the book.
     */
    public void setAuthor(String author)
    {
        this.author = author;
    }

    /**
     * Mutator for the ISBN of the book.
     * @param isbn ISBN of the book.
     */
    public void setIsbn(String isbn)
    {
        this.isbn = isbn;
    }

    /**
     * Mutator for the publisher of the book.
     * @param publisher Publisher of the book.
     */
    public void setPublisher(String publisher)
    {
        this.publisher = publisher;
    }

    public String toString()
    {
        return "Book : " + author + " - " + name;
    }
}
