package com.softwareleaf.confluence;

import com.softwareleaf.confluence.model.*;
import retrofit.Callback;
import retrofit.http.*;
import retrofit.http.Body;

import java.util.Map;

/**
 * Specifies the API paths that are so far supported with this confluence client.
 *
 * @author Jonathon Hope
 * @since 29/05/2015
 */
public interface ConfluenceAPI
{

    /**
     * Fetch a results object containing a paginated list of content.
     *
     * @return an instance of {@code getContentResults} wrapping the list
     * of {@code Content} instances obtained from the API call.
     */
    @GET("/rest/api/content")
    ContentResultList getContentResults();

    /**
     * Perform a search for content, by space key and title.
     *
     * @param key   the space key to search under.
     * @param title the title of the piece of content to search for.
     * @return an instance of {@code getContentResults} wrapping the list
     * of {@code Content} instances obtained from the API call.
     */
    @GET("/rest/api/content")
    ContentResultList getContentBySpaceKeyAndTitle(@Query("key") String key, @Query("title") String title);

    /**
     * GET Content
     *
     * @param id the id of the page or blog post to fetch.
     * @return the Content instance representing the JSON response.
     */
    @GET("/rest/api/content/{id}" + QueryParams.EXPAND_BODY_STORAGE)
    Content getContentById(@Path("id") String id);

    /**
     * POST Conversion request. Used for converting between storage formats.
     *
     * @param storage         the storage instance to convert.
     * @param convertToFormat the representation to convert to.
     * @return an instance of {@code Storage} that contains the result of
     * the conversion request.
     */
    @POST("/rest/api/contentbody/convert/{to}")
    Storage postContentConverstion(@Body Storage storage, @Path("to") String convertToFormat);

    /**
     * POST Content.
     *
     * @param content the piece of Content to be included in the body of the request.
     */
    @POST("/rest/api/content")
    Content postContent(@Body Content content);

    /**
     * Same as {@link #postContent} but clients can provide a callback with success
     * and failure hooks.
     *
     * @param content  the piece of Content to be included in the body of the request.
     * @param callback this handle provides a means of inquiring about
     *                 the success or failure of the invocation.
     */
    @POST("/rest/api/content")
    void postContentWithCallback(@Body Content content, Callback<Content> callback);

    /**
     * DELETE Content
     * <p>
     * Trashes or purges a piece of Content, based on its {@literal ContentType} and {@literal ContentStatus}.
     * <p>
     * There are three cases:
     * If the content is trashable and its status is {@literal ContentStatus#CURRENT}, it will be trashed.
     * If the content is trashable, its status is {@literal ContentStatus#TRASHED} and the "status" query parameter in
     * the request is "trashed", the content will be purged from the trash and deleted permanently.
     * If the content is not trashable it will be deleted permanently without being trashed.
     *
     * @param id the id of the page of blog post to be deleted.
     */
    @DELETE("/rest/api/content/{id}")
    NoContent deleteContentById(@Path("id") String id);

    /**
     * Obtain a result list of available spaces.
     *
     * @return an instance of {@code SpaceResultList} that can be used to
     * obtain a list of spaces available on confluence.
     */
    @GET("/rest/api/space")
    SpaceResultList getSpaces();

    /**
     * Fetch all content from a confluence space.
     *
     * @param spaceKey the key that identifies the target Space.
     * @param params  the query parameters.
     * @return a list of all content in the given Space identified by {@param spaceKey}.
     */
    @GET("/rest/api/space/{spaceKey}/content/page")
    ContentResultList getAllSpaceContent(@Path("spaceKey") String spaceKey, @QueryMap Map<String, String> params);

    /**
     * Obtain paginated results of root content available from a given space.
     *
     * @param spaceKey    the space key of the space to search.
     * @param contentType the type of content to return.
     * @return a wrapper model around the {@link ContentResultList} resulting from this call.
     */
    @GET("/rest/api/space/{spaceKey}/content/{type}")
    ContentResultList getRootContentBySpaceKey(@Path("spaceKey") String spaceKey, @Path("type") String contentType);

}
