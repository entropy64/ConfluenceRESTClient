package com.softwareleaf.confluence.model;

import com.google.gson.GsonBuilder;

/**
 * Represents the JSON response when Content is not found.
 *
 * @author Jonathon Hope
 * @since 11/06/2015
 */
public class NoContent
{
    private int statusCode;
    private String message;

    public NoContent()
    {
    }

    public int getStatusCode()
    {
        return statusCode;
    }

    public void setStatusCode(final int statusCode)
    {
        this.statusCode = statusCode;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(final String message)
    {
        this.message = message;
    }

    @Override
    public String toString()
    {
        return new GsonBuilder().disableHtmlEscaping().create().toJson(this);
    }
}
