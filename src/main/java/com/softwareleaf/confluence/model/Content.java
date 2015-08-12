package com.softwareleaf.confluence.model;

import java.util.Objects;

import com.google.gson.GsonBuilder;

/**
 * Represents a piece of content.
 * <p>
 * <p>Example
 * <pre>{@literal
 *      {
 *          "id": "22217244",
 *          "type": "page",
 *          "space": {
 *              "key": "TST"
 *          },
 *          "title": "A title",
 *          "body": {
 *              "storage": {
 *                  "value": "<p>This is a new page</p>",
 *                  "representation": "storage"
 *              }
 *          }
 *      }
 * }
 * </pre>
 *
 * @author Jonathon Hope
 * @since 28/05/2015
 */
public class Content
{
    /**
     * The id of the page or blog post.
     */
    private String id;
    /**
     * The type of content: blog post or page.
     */
    private String type;
    /**
     * The ancestors of this blog post or page.
     */
    private Parent[] ancestors;

    /**
     * The space object holds the space key, that is used to identify the location of
     * the piece of content.
     */
    private Space space;
    /**
     * The title of the page or blog post.
     */
    private String title;
    /**
     * The body object holds the stored data of the page or blog post.
     */
    private Body body;

    /**
     * Default Constructor.
     */

    public Content()
    {
    }

    /**
     * Constructor.
     *
     * @param id    the id of the piece of content.
     * @param type  the type of piece of content.
     * @param space the space object for the piece of content.
     * @param title the title of the piece of content.
     * @param body  the body of the piece of content.
     */
    public Content(final String id,
                   final String type,
                   final Space space,
                   final String title,
                   final Body body)
    {
        this.id = id;
        this.type = type;
        this.title = title;
        this.space = space;
        this.body = body;
    }

    // getters and setters

    public String getType()
    {
        return type;
    }

    public void setType(final Type type)
    {
        this.type = type.toString();
    }

    public Parent[] getAncestors()
    {
        return ancestors;
    }

    public void setAncestors(final Parent[] ancestors)
    {
        this.ancestors = ancestors;
    }

    public Space getSpace()
    {
        return space;
    }

    public void setSpace(final Space space)
    {
        this.space = space;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(final String title)
    {
        this.title = title;
    }

    public Body getBody()
    {
        return body;
    }

    public void setBody(final Body body)
    {
        this.body = body;
    }

    public String getId()
    {
        return id;
    }

    public void setId(final String id)
    {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return new GsonBuilder().disableHtmlEscaping().create().toJson(this);
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || !(o instanceof Content))
        {
            return false;
        }
        final Content content = (Content) o;
        return Objects.equals(id, content.id) &&
                Objects.equals(type, content.type) &&
                Objects.equals(ancestors, content.ancestors) &&
                Objects.equals(space, content.space) &&
                Objects.equals(title, content.title) &&
                Objects.equals(body, content.body);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, type, ancestors, space, title, body);
    }
}