package com.softwareleaf.confluence.model;

/**
 * Represents the type of a piece of content.
 * <p>Example
 * <pre>{@literal
 *  {
 *      "id": "2345678",
 *      "type": "page",
 *      "space": {
 *          ...
 *      }
 *      ...
 *  }
 * }</pre>
 *
 * @author Jonathon Hope
 * @since 28/05/2015
 */
public enum Type
{
    BLOGPOST,
    PAGE;

    /**
     * We override this here to
     */
    @Override
    public String toString()
    {
        return super.toString().toLowerCase();
    }
}
