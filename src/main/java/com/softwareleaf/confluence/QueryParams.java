package com.softwareleaf.confluence;

/**
 * An easy way to access common query params.
 *
 * @author Jonathon Hope
 * @since 29/05/2015
 */
public interface QueryParams
{
    /**
     * This will expand the body.storage object of a request for particular content.
     * <p>
     * For example:
     * <p><pre>{@literal
     *     GET /rest/api/content/757575775?expand=body.storage
     * }
     * </pre>
     * <p> would expand the actual content stored in the page or blog post with the id 757575775.
     */
    String EXPAND_BODY_STORAGE = "?expand=body.storage";
}
