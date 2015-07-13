package com.softwareleaf.confluence;

import com.google.common.collect.ImmutableMap;
import com.google.gson.GsonBuilder;
import com.softwareleaf.confluence.model.*;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * A class that is capable of making requests to the confluence API.
 * <p>
 * Example Usage:
 * <pre>
 *     String myURL = "http://confluence.organisation.org";
 *     String myUsername = "...";
 *     String myPassword = "...";
 *
 *     ConfluenceClient client = ConfluenceClient.builder()
 *          .baseURL(myURL)
 *          .username(myUsername)
 *          .password(myPassword)
 *          .build();
 *
 *     // search confluence instance by title and space key
 *     ContentResultList search =
 *     confluenceClient.getContentBySpaceKeyAndTitle("DEV", "A page or blog in DEV");
 * </pre>
 *
 * @author Jonathon Hope
 * @since 28/05/2015
 */
public class ConfluenceClient
{
    /**
     * The default base url is the production confluence instance.
     */
    public static final String THE_BASE_URL = "http://localhost:8090";
    // default account credentials
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin";

    /**
     * the Logger instance used by this class.
     */
    private static final Logger theLogger = Logger.getLogger( ConfluenceClient.class.getName() );

    /**
     * The ConfluenceAPI endpoint.
     */
    private ConfluenceAPI myConfluenceAPI;

    /**
     * Constructor.
     */
    private ConfluenceClient( Builder builder )
    {
        this.myConfluenceAPI = builder.myConfluenceAPI;
    }

    /**
     * Fetch a single piece of content.
     *
     * @param id the id of the page or blog post to fetch.
     * @return the Content instance.
     */
    public Content getContentById( String id )
    {
        return myConfluenceAPI.getContentById( id );
    }

    /**
     * Fetch a results object containing a paginated list of content.
     *
     * @return an instance of {@code getContentResults} wrapping the list
     * of {@code Content} instances obtained from the API call.
     */
    public ContentResultList getContentResults()
    {
        return myConfluenceAPI.getContentResults();
    }

    /**
     * Perform a search for content, by space key and title.
     *
     * @param key   the space key to search under.
     * @param title the title of the piece of content to search for.
     * @return an instance of {@code getContentResults} wrapping the list
     * of {@code Content} instances obtained from the API call.
     */
    public ContentResultList getContentBySpaceKeyAndTitle( String key, String title )
    {
        return myConfluenceAPI.getContentBySpaceKeyAndTitle( key, title );
    }

    /**
     * Used for converting the storage format of a piece of content.
     *
     * @param storage   the storage instance to convert.
     * @param convertTo the representation to convert to.
     * @return an instance of {@code Storage} that contains the result of
     * the conversion request.
     * @see <a href="https://confluence.atlassian.com/display/DOC/Confluence+Storage+Format">
     * Confluence Storage Format</a>
     */
    public Storage convertContent( Storage storage, Storage.Representation convertTo )
    {
        return myConfluenceAPI.postContentConverstion( storage, convertTo.toString() );
    }

    /**
     * Performs a POST request with the body of the request containing the
     * {@param content}, thus creating a new page or blog post on confluence.
     *
     * @param content  the content to post to confluence.
     * @param callback this handle provides a means of inquiring about
     *                 the success or failure of the invocation.
     */
    public void postContentWithCallback( Content content, Callback<Content> callback )
    {
        myConfluenceAPI.postContentWithCallback( content, callback );
    }

    /**
     * Performs a POST request with the body of the request containing the
     * {@param content}, thus creating a new page or blog post on confluence.
     *
     * @param content the content to post to confluence.
     */
    public Content postContent( Content content )
    {
        System.out.println( content.toString() );
        return myConfluenceAPI.postContent( content );
    }

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
    public void deleteContentById( String id )
    {
        NoContent noContent = myConfluenceAPI.deleteContentById( id );
        theLogger.fine( "Response: " + noContent );
    }

    /**
     * Obtain a list of available spaces.
     *
     * @return a list of spaces available on confluence.
     */
    public List<Space> getSpaces()
    {
        Space[] results = myConfluenceAPI.getSpaces().getSpaces();
        return Arrays.stream( results ).collect( Collectors.toList() );
    }

    /**
     * Fetch all content from a confluence space.
     *
     * @param spaceKey the key that identifies the target Space.
     * @return a list of all content in the given Space identified by {@param spaceKey}.
     */
    public List<Content> getAllSpaceContent( String spaceKey )
    {
        Content[] results = myConfluenceAPI.getAllSpaceContent( spaceKey,
                ImmutableMap.of(
                        "expand", "ancestors,body.storage",
                        "limit", "1000" ) )
                .getContents();
        return Arrays.stream( results ).collect( Collectors.toList() );
    }

    /**
     * Obtain a list of root content of a space.
     *
     * @param spaceKey    the space key of the Space.
     * @param contentType the type of content to return.
     * @return a list of Content instances obtained from the root.
     */
    public List<Content> getRootContentBySpaceKey( String spaceKey, Type contentType )
    {
        Content[] resultList = myConfluenceAPI
                .getRootContentBySpaceKey( spaceKey, contentType.toString() )
                .getContents();
        return Arrays.stream( resultList ).collect( Collectors.toList() );
    }

    /**
     * Factory object for chaining the construction of a {@code ConfluenceClient}.
     *
     * @return an instance of the internal Builder class.
     */
    public static Builder builder()
    {
        return new Builder();
    }

    /**
     * A Builder factory for implementing the Builder Pattern.
     */
    public static class Builder
    {
        /**
         * This is the reference to the concrete REST API Client generated by Retrofit.
         */
        private ConfluenceAPI myConfluenceAPI;
        /**
         * The username forms the first part of the credential used to authenticate requests.
         */
        private String myUsername;
        /**
         * The password forms the second part of the credential used to authenticate requests.
         */
        private String myPassword;
        /**
         * By default, {@link #THE_BASE_URL} will be used as the url of the confluence instance; when
         * this is set, requests will be made to this base URL instead.
         */
        private String myAlternativeBaseURL;

        // prevent direct instantiation by external classes.
        private Builder()
        {
        }

        /**
         * Set the username parameter.
         *
         * @param username the username.
         * @return {@code this}.
         */
        public Builder username( String username )
        {
            this.myUsername = username;
            return this;
        }

        /**
         * Sets the password parameter.
         *
         * @param password the password matching the username.
         * @return {@code this}.
         */
        public Builder password( String password )
        {
            this.myPassword = password;
            return this;
        }

        /**
         * By default, {@link #THE_BASE_URL BaseURL} will be used as the url of the confluence instance; when
         * this is set, requests will be made to this base URL instead.
         *
         * @param url the alternative Base url of the confluence instance to make requests to.
         * @return {@code this}.
         */
        public Builder baseURL( String url )
        {
            this.myAlternativeBaseURL = url;
            return this;
        }

        /**
         * Build and return a configured ConfluenceClient instance.
         *
         * @return a configured {@code ConfluenceClient} instance.
         */
        public ConfluenceClient build()
        {
            // determine if we are using the production confluence or not.
            final String URL = myAlternativeBaseURL == null ? THE_BASE_URL : myAlternativeBaseURL;
            // determine the user credentials to use.
            String username = myUsername == null ? DEFAULT_USERNAME : myUsername;
            String password = myPassword == null ? DEFAULT_PASSWORD : myPassword;
            /*
             * The Confluence REST API requires HTTP Basic authentication using a
             * username and password pair, for a given Confluence user.
             * We therefore need to first encode the credentials using a Base64 encoder
             * and set up an interceptor that adds the requisite HTTP headers to each request.
             */
            final String credentials = username + ":" + password;
            // encode in base64.
            final String encodedCredentials = "Basic " + Base64.getEncoder().encodeToString( credentials.getBytes() );

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint( URL )
                    .setConverter( new GsonConverter( new GsonBuilder().disableHtmlEscaping().create() ) )
                    .setLogLevel( RestAdapter.LogLevel.FULL )
                    .setLog( System.out::println )
                    .setRequestInterceptor(
                            request -> {
                                request.addHeader( "Accept", "application/json" );
                                request.addHeader( "Authorization", encodedCredentials );
                            }
                    )
                    .build();

            // Create an implementation of the API defined by the specified ConfluenceAPI interface
            this.myConfluenceAPI = restAdapter.create( ConfluenceAPI.class );

            return new ConfluenceClient( this );
        }

    }

}
