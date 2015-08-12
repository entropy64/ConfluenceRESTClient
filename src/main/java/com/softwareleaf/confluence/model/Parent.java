package com.softwareleaf.confluence.model;

import java.util.Objects;

/**
 * Represents the ancestors nested json object, used to set the parents of a
 * piece of content.
 *
 * @author Jonathon Hope
 * @since 18/06/2015
 */
public class Parent
{
    /**
     * The id of the parent.
     */
    private String id;
    /**
     * The type of the parent.
     */
    private String type;

    /**
     * Constructor.
     */
    public Parent()
    {
    }

    /**
     * Constructor.
     *
     * @param id   the id of the parent.
     * @param type the type of the parent.
     */
    public Parent(final String id, final String type)
    {
        this.id = id;
        this.type = type;
    }


    public String getId()
    {
        return id;
    }

    public void setId(final String id)
    {
        this.id = id;
    }

    public String getType()
    {
        return type;
    }

    public void setType(final String type)
    {
        this.type = type;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        final Parent parent = (Parent) o;
        return Objects.equals(id, parent.id) &&
                Objects.equals(type, parent.type);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, type);
    }
}
