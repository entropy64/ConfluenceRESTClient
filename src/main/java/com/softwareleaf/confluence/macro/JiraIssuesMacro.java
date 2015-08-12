package com.softwareleaf.confluence.macro;

import java.net.URL;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.softwareleaf.confluence.ConfluenceClient;
import com.softwareleaf.confluence.model.Storage;

/**
 * A class for building the requisite markup for a JIRA Issues Macro.
 *
 * @author Jonathon Hope
 * @see <a href="https://confluence.atlassian.com/display/DOC/JIRA+Issues+Macro">JIRA Issues Macro</a>
 * @since 18/06/2015
 */
public class JiraIssuesMacro
{
    /**
     * Parameters are options that you can set to control the content or format of the macro output.
     */
    public enum Parameters
    {
        /**
         * If this parameter is set to 'true', JIRA will return only the issues which allow
         * unrestricted viewing. That is, the issues which are visible to anonymous viewers,
         * as determined by JIRA's viewing restrictions. If this parameter is omitted or set
         * to 'false', then the results depend on how your administrator has configured the
         * communication between JIRA and Confluence. By default, Confluence will show only
         * the JIRA issues which the user is authorised to view.
         */
        ANONYMOUS,
        /**
         * If you specify a 'baseurl', then the link in the header, pointing to your JIRA site,
         * will use this base URL instead of the value of the 'url' parameter. This is useful
         * when Confluence connects to JIRA with a different URL from the one used by other users.
         */
        BASEURL,
        /**
         * A list of JIRA column names, separated by semi-colons (;). You can include any columns
         * recognised by your JIRA site, including custom columns.
         *
         * @see <a href="https://confluence.atlassian.com/display/JIRA/Displaying+Search+Results+in+XML">
         * JIRA documentation</a> for a list of names.
         */
        COLUMNS,
        /**
         * If this parameter is set to 'true', the issue list will show the number of issues in
         * JIRA. The count will be linked to your JIRA site.
         */
        COUNT,
        /**
         * The macro maintains a cache of the issues which result from the JIRA query. If the 'cache'
         * parameter is set to 'off', the relevant part of the cache is cleared each time the macro
         * is reloaded. (The value 'false' also works and has the same effect as 'off'.)
         */
        CACHE,
        /**
         * The height in pixels of the table displaying the JIRA issues.
         * Note: that this height specification is ignored in the following situations:
         * <ul>
         * <li>If the 'renderMode' parameter (see below) is set to 'static'.</li>
         * <li>When the JIRA issues are displayed in a PDF or Word document, in an email message
         * or in an RSS feed.</li>
         * </ul>
         */
        HEIGHT,
        /**
         * If the value is 'dynamic', the JIRA Issues macro offers an interactive display which
         * people can manipulate as follows:
         * <ul>
         * <li>Click the column headers to sort the output.</li>
         * <li>Drag and drop the columns into a different order.</li>
         * <li>Temporarily remove a column from the display.</li>
         * <li>View a page of issues at a time, for faster response times.</li>
         * </ul>
         * A value of 'static' will disable the dynamic display features.
         */
        RENDER_MODE,
        /**
         * You can customise the title text at the top of the JIRA issues table with this
         * parameter. For instance, setting the title to 'Bugs-to-fix' will replace the
         * default 'JIRA Issues' text. This can help provide more context to the list of
         * issues displayed.
         */
        TITLE,
        /**
         * The URL of the XML view of your selected issues in JIRA Issue Navigator.
         * <p>For example:
         * <pre>{@literal
         * http://jira.atlassian.com/sr/jira.issueviews:searchrequest-xml/temp/SearchRequest.xml?jqlQuery=project+%3D+CONF+AND+%28summary+%7E+jiraissues+OR+description+%7E+jiraissues+OR+comment+%7E+jiraissues%29&amp;tempMax=10
         * }</pre>
         */
        URL,
        /**
         * The width of the table displaying the JIRA issues. Can be indicated either as
         * a percentage {@literal (%)} or in pixels (px).
         */
        WIDTH;

        /**
         * Motivation: Necessary to keep consistent with UPPER CASE naming convention for enums,
         * whilst outputting the expected format of the macro value.
         *
         * @param string the String to convert.
         * @return a String in Camel Case format.
         */
        private static String convertToCamelCase(final String string)
        {
            final StringBuilder sb = new StringBuilder();
            final String[] parts = string.split("_");
            sb.append(parts[0].toLowerCase());
            for (int i = 1; i <= parts.length - 1; i++)
            {
                sb.append(toProperCase(parts[i]));
            }
            return sb.toString();
        }

        private static String toProperCase(final String s)
        {
            return s.substring(0, 1).toUpperCase() +
                    s.substring(1).toLowerCase();
        }

        @Override
        public String toString()
        {
            return convertToCamelCase(name());
        }

    }

    /**
     * To be used with the {@link Builder#renderMode(RenderMode)}
     */
    public enum RenderMode
    {
        STATIC,
        DYNAMIC;

        @Override
        public String toString()
        {
            return name().toLowerCase();
        }
    }

    /**
     * To be used with the {@link Builder#cache(Cache)}
     */
    public enum Cache
    {
        ON,
        OFF;

        @Override
        public String toString()
        {
            return name().toLowerCase();
        }
    }

    /**
     * To be used with {@link Builder#count(boolean)}
     */
    public enum Columns
    {
        TYPE,
        KEY,
        SUMMARY,
        ASSIGNEE,
        REPORTER,
        PRIORITY,
        STATUS,
        RESOLUTION,
        CREATED,
        UPDATED,
        DUE;

        @Override
        public String toString()
        {
            return name().toLowerCase();
        }
    }

    /**
     * Stores the parameters of this code block macro.
     */
    private final EnumMap<Parameters, String> parameters;

    /**
     * Constructor
     *
     * @param builder the builder factory used to build this {@code JiraIssuesMacro}
     */
    protected JiraIssuesMacro(final Builder builder)
    {
        parameters = builder.myParameters;
    }

    /**
     * Converts this {@code JiraIssuesMacro} into confluence WIKI markup form.
     *
     * @return a String conversion of the markup for this macro.
     */
    public String toWikiMarkup()
    {
        final StringBuilder sb = new StringBuilder();
        sb.append("{jiraissues:");
        for (final Map.Entry<Parameters, String> entry : parameters.entrySet())
        {
            sb.append(entry.getKey().toString());
            sb.append("=");
            sb.append(entry.getValue());
            sb.append("|");
        }
        sb.deleteCharAt(sb.length() - 1); // delete the trailing '|' char
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert wiki markup produced by this macro to storage representation.
     *
     * @param client the {@code ConfluenceClient} to use to convert to storage form.
     * @return a String containing the wiki markup of this macro, to storage representation.
     * @throws NullPointerException if {@param client} is null.
     */
    public String toStorageRepresentation(ConfluenceClient client)
    {
        client = Objects.requireNonNull(client);
        final Storage wikiStorage = new Storage(toWikiMarkup(), Storage.Representation.WIKI);
        return client.convertContent(wikiStorage, Storage.Representation.STORAGE).getValue();
    }

    /**
     * Builder factory method.
     *
     * @return a {@code Builder} instance for chain-building a CodeBlockMacro.
     */
    public static Builder builder()
    {
        return new Builder();
    }

    /**
     * A class for implementing the Builder Pattern for {@code CodeBlockMacro}.
     */
    public static class Builder
    {
        private final EnumMap<Parameters, String> myParameters;

        // constructor
        private Builder()
        {
            myParameters = new EnumMap<>(Parameters.class);
        }

        /**
         * Factory method for creating a new instance of {@code JiraIssuesMacro}
         *
         * @return a new instance of {@code JiraIssuesMacro} configured by
         * the parameters set by this {@code Builder}
         */
        public JiraIssuesMacro build()
        {
            checkParameters();
            return new JiraIssuesMacro(this);
        }

        /**
         * @param columns the array of columns to include in the macro.
         * @return {@code this}.
         * @see JiraIssuesMacro.Columns
         */
        public Builder columns(final Columns[] columns)
        {
            final String cols = Arrays.stream(columns)
                    .map(Columns::toString).collect(Collectors.joining(","));
            myParameters.putIfAbsent(Parameters.COLUMNS, cols);
            return this;
        }

        /**
         * @param count predicate for enabling the number of issues to be displayed.
         * @return {@code this}.
         * @see Parameters#COUNT
         */
        public Builder count(final boolean count)
        {
            myParameters.putIfAbsent(Parameters.COUNT, Boolean.toString(count));
            return this;
        }

        /**
         * @param cache the case flag.
         * @return {@code this}
         * @see JiraIssuesMacro.Parameters#CACHE
         */
        public Builder cache(final Cache cache)
        {
            myParameters.putIfAbsent(Parameters.CACHE, cache.toString());
            return this;
        }

        /**
         * Sets the height parameter.
         *
         * @param height the height value.
         * @return {@code this}
         */
        public Builder height(final int height)
        {
            if (height >= 200)
            {
                myParameters.putIfAbsent(Parameters.HEIGHT, Integer.toString(height));
            }
            return this;
        }

        /**
         * @param renderMode the {@link JiraIssuesMacro.RenderMode} to set.
         * @return {@code this}
         */
        public Builder renderMode(final RenderMode renderMode)
        {
            myParameters.putIfAbsent(Parameters.RENDER_MODE, renderMode.toString());
            return this;
        }

        /**
         * Sets the title for the {@code JiraIssuesMacro}, for example, here we call it {@literal "JIRA Issues"}:
         * <pre>{@literal
         *      <ac:parameter ac:name=\"title\">JIRA Issues</ac:parameter>
         * }</pre>
         *
         * @param title the title to set.
         * @return {@code this}.
         */
        public Builder title(final String title)
        {
            myParameters.putIfAbsent(Parameters.TITLE, title);
            return this;
        }

        /**
         * Sets the URL for the selected issues. This can include a JQL query string.
         *
         * @param url the URL to set.
         * @return {@code this}.
         */
        public Builder url(final URL url)
        {
            myParameters.putIfAbsent(Parameters.URL, url.toString());
            return this;
        }

        /**
         * Sets the width of the JIRA issue macro.
         *
         * @param percentage a value must be greater or equal to {@code 500}.
         * @return {@code this}.
         */
        public Builder width(final int percentage)
        {
            if (percentage >= 500)
            {
                myParameters.putIfAbsent(Parameters.WIDTH, Integer.toString(percentage));
            }
            return this;
        }

        /**
         * Validates that certain required parameters are set.
         */
        private void checkParameters()
        {
            if (myParameters.get(Parameters.URL) == null)
            {
                throw new IllegalStateException("URL is required.");
            }
        }

    }
}
