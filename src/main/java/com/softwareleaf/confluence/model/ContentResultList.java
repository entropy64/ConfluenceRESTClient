package com.softwareleaf.confluence.model;

import com.google.gson.annotations.SerializedName;

/**
 * A representation of the results object returned by the
 * GET {@literal /rest/api/content/} API call.
 *
 * @author Jonathon Hope
 * @since 9/06/2015
 */
public class ContentResultList
{
    @SerializedName("results")
    private Content[] contents;

    /**
     * Constructor.
     *
     * @param contents the array of Content instances.
     */
    public ContentResultList(Content[] contents)
    {
        this.contents = contents;
    }

    // getter and setter

    public Content[] getContents()
    {
        return contents;
    }

    public void setContents(Content[] contents)
    {
        this.contents = contents;
    }
}
