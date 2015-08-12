package com.softwareleaf.confluence.model;

import java.util.Objects;

/**
 * Represents the storage container within a {@code Content.body}
 *
 * @author Jonathon Hope
 * @see <a href="https://confluence.atlassian.com/display/DOC/Confluence+Storage+Format">
 * Confluence Storage Format</a>
 * @since 28/05/2015
 */
public class Storage
{
    /**
     * The markup or html of this {@code Storage}.
     */
    private String value;

    /**
     * The representation. The confluence REST API docs are unclear as to what the
     * full set of possible values for this are; but so far {@literal "view","page","storage"}.
     * <p>
     * NOTE: {@literal "storage"} should almost always be used.
     */
    private Representation representation = Representation.STORAGE;

    /**
     * @see <a href="https://confluence.atlassian.com/display/DOC/Confluence+Storage+Format">
     * Confluence Storage Format</a>
     */
    public enum Representation
    {
        VIEW,
        PAGE,
        STORAGE,
        WIKI;

        @Override
        public String toString()
        {
            return name().toLowerCase();
        }
    }

    /**
     * Constructor.
     *
     * @param value          the markup of HTML.
     * @param representation the type of representation.
     */
    public Storage(final String value, final Representation representation)
    {
        this.value = value;
        this.representation = representation;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(final String value)
    {
        this.value = value;
    }

    public Representation getRepresentation()
    {
        return representation;
    }

    public void setRepresentation(final Representation representation)
    {
        this.representation = representation;
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
        final Storage storage = (Storage) o;
        return Objects.equals(value, storage.value) &&
                Objects.equals(representation, storage.representation);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(value, representation);
    }
}
