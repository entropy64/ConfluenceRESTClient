package com.softwareleaf.confluence.macro;

/**
 * Represents an expandable body of rich text.
 *
 * @author Jonathon Hope
 * @see <a href="https://confluence.atlassian.com/display/DOC/Expand+Macro"> Expand Macro docs </a>
 * @since 3/07/2015
 */
public class ExpandMacro
{
    /**
     * The title of the expandable.
     */
    private final String myTitle;
    /**
     * The contents of the expandable.
     */
    private final String myBody;

    /**
     * @param builder the builder factory to use.
     */
    protected ExpandMacro(final Builder builder)
    {
        myTitle = builder.myTitle;
        myBody = builder.myBody;
    }

    /**
     * @return converts this instance to confluence markup.
     */
    public String toMarkup()
    {
        final StringBuilder sb = new StringBuilder();
        sb.append("<ac:structured-macro ac:name=\"expand\">");
        sb.append("<ac:parameter ac:name=\"title\">");
        sb.append(myTitle);
        sb.append("</ac:parameter>");

        sb.append("<ac:rich-text-body>");
        sb.append(myBody);
        sb.append("</ac:rich-text-body>");
        sb.append("</ac:structured-macro>");
        return sb.toString();
    }

    /**
     * Builder factory method.
     *
     * @return a {@code Builder} instance for chain-building a {@code ExpandMacro}.
     */
    public static Builder builder()
    {
        return new Builder();
    }

    /**
     * A class for implementing the Builder Pattern for {@code ExpandMacro}.
     */
    public static class Builder
    {

        private String myTitle;
        private String myBody;

        public Builder title(final String title)
        {
            myTitle = title;
            return this;
        }

        public Builder body(final String body)
        {
            myBody = body;
            return this;
        }

        public ExpandMacro build()
        {
            return new ExpandMacro(this);
        }

    }
}
