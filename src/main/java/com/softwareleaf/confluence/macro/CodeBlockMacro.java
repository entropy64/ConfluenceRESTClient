package com.softwareleaf.confluence.macro;

import java.util.EnumMap;
import java.util.Map;

/**
 * Represents a confluence Code Block Macro.
 *
 * @author Jonathon Hope
 * @see <a href="https://confluence.atlassian.com/display/DOC/Code+Block+Macro">
 * Confluence Page describing the Macro</a>
 * @since 28/05/2015
 */
public class CodeBlockMacro
{

    /**
     * Stores the parameters of this code block macro.
     */
    private EnumMap<Parameters, String> myParameters;
    /**
     * The code of this CodeBlockMacro.
     */
    private String myBody;

    /**
     * Parameters are options that you can set to control the content or format of the macro output.
     */
    public enum Parameters
    {
        /**
         * Can be either {@literal true} or {@literal false}
         * Default: false
         */
        COLLAPSE,
        /**
         * When Show line numbers is selected, this value defines the number of the first line of code.
         * Default: 1
         */
        FIRSTlINE,
        /**
         * Specifies the language (or environment) for syntax highlighting. The default language is Java but you can
         * choose from one of the following languages/environments:
         *
         * @see CodeBlockMacro.Languages
         */
        LANGUAGE,
        /**
         * If selected, line numbers will be shown to the left of the lines of code.
         * Default: false
         */
        LINENUMBERS,
        /**
         * Specifies the colour scheme used for displaying your code block. Many of these themes are based on the
         * default colour schemes of popular integrated development environments (IDEs). The default theme is
         * Confluence (also known as Default), which is typically black and coloured text on a blank background.
         * However, you can also choose from one of the following other popular themes:
         *
         * @see CodeBlockMacro.Themes
         */
        THEME,
        /**
         * Adds a title to the code block. If specified, the title will be displayed in a header row at the top
         * of the code block.
         * Default: none
         */
        TITLE;

        @Override
        public String toString()
        {
            // replace the underscore with a slash and print as lower case
            return this.name().replace("_", "/").toLowerCase();
        }

    }

    /**
     * The enumerated list of languages supported by the confluence code macro.
     */
    public enum Languages
    {
        ACTION_SCRIPT_3("actionscript3"),
        BASH("bash"),
        C_SHARP("c#"),     // (C#)
        COLD_FUSION("coldfusion"),
        CPP("cpp"),        //(C++)
        CSS("css"),
        DELPHI("delphi"),
        DIFF("diff"),
        ERLANG("erlang"),
        GROOVY("groovy"),
        HTML_XML("xml"),   // html/xml
        JAVA("java"),
        JAVA_FX("javafx"),
        JAVA_SCRIPT("js"),       // JavaScript
        NONE("none"),       // (no syntax highlighting)
        PERL("perl"),
        PHP("php"),
        POWER_SHELL("powershell"),
        PYTHON("python"),
        RUBY("ruby"),
        SCALA("scala"),
        SQL("sql"),
        VB("vb");

        public final String value;

        Languages(String value)
        {
            this.value = value;
        }

        @Override
        public String toString()
        {
            return this.value;
        }
    }

    /**
     * The available values for the Theme parameter.
     *
     * @see CodeBlockMacro.Parameters#THEME
     */
    public enum Themes
    {
        D_JANGO,
        EMACS,
        FADE_TO_GREY,
        MIDNIGHT,
        R_DARK,
        ECLIPSE,
        CONFLUENCE;

        /**
         * Motivation: Necessary to keep consistent with UPPER CASE naming convention for enums,
         * whilst outputting the expected format of the macro value.
         *
         * @param theme the String to convert.
         * @return a String in Upper Camel Case format.
         */
        private String convertToUpperCamel(String theme)
        {
            StringBuilder sb = new StringBuilder();
            for (String s : theme.split("_"))
            {
                sb.append(Character.toUpperCase(s.charAt(0)));
                if (s.length() > 1)
                {
                    sb.append(s.substring(1, s.length()).toLowerCase());
                }
            }
            return sb.toString();
        }

        @Override
        public String toString()
        {
            return convertToUpperCamel(super.toString());
        }
    }


    /**
     * Constructor.
     *
     * @param builder the builder instance to use as a factory.
     * @see CodeBlockMacro.Builder
     */
    protected CodeBlockMacro(Builder builder)
    {
        this.myParameters = builder.myParameters;
        this.myBody = builder.myCode;
    }

    /**
     * Converts this CodeBlockMacro into confluence markup form.
     *
     * @see <a href="https://confluence.atlassian.com/display/DOC/Code+Block+Macro">Confluence Page describing the Macro</a>
     */
    public String toMarkup()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("<ac:structured-macro ac:name=\"code\">");
        for (Map.Entry<Parameters, String> entry : myParameters.entrySet())
        {
            sb.append("<ac:parameter ac:name=\"");
            sb.append(entry.getKey().toString());
            sb.append("\">");
            sb.append(entry.getValue());
            sb.append("</ac:parameter>");
        }
        sb.append("<ac:plain-text-body>");
        sb.append(myBody);
        sb.append("</ac:plain-text-body>");
        sb.append("</ac:structured-macro>");
        return sb.toString();
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
        private EnumMap<Parameters, String> myParameters;
        private String myCode;

        /**
         * Constructor.
         */
        private Builder()
        {
            myParameters = new EnumMap<>(Parameters.class);
        }

        /**
         * @return a new instance of {@code CodeBlockMacro}.
         */
        public CodeBlockMacro build()
        {
            return new CodeBlockMacro(this);
        }

        /**
         * <p>Example
         * <pre>{@literal
         *      <ac:parameter ac:name=\"language\">xml</ac:parameter>
         * }</pre>
         * <p> would be represented as:
         * <pre>{@code
         *      CodeBlockMacro.builder().language(Languages.HTML_XML)
         * }</pre>
         *
         * @param l the Languages enum.
         * @see CodeBlockMacro.Languages
         */
        public Builder language(Languages l)
        {
            myParameters.putIfAbsent(Parameters.LANGUAGE, l.value);
            return this;
        }

        /**
         * <p>Makes the macro collapsible:
         * <pre>{@literal
         *      <ac:parameter ac:name=\"collapse\">true</ac:parameter>
         * }</pre>
         */
        public Builder collapse()
        {
            myParameters.putIfAbsent(Parameters.COLLAPSE, "true");
            return this;
        }

        /**
         * <p>Enables linenumbers:
         * <pre>{@literal
         *      <ac:parameter ac:name=\"linenumbers\">true</ac:parameter>
         * }</pre>
         */
        public Builder showLineNumbers()
        {
            myParameters.putIfAbsent(Parameters.LINENUMBERS, "true");
            return this;
        }

        /**
         * <p>If {@code Parameters.LINENUMBERS} is set, sets the first line number to start at {@param first}:
         * <pre>{@literal
         *      <ac:parameter ac:name=\"linenumbers\">true</ac:parameter><ac:parameter ac:name=\"firstline\">1</ac:parameter>
         * }</pre>
         */
        public Builder firstline(int first)
        {

            if (myParameters.containsKey(Parameters.LINENUMBERS))
            {
                myParameters.putIfAbsent(Parameters.FIRSTlINE, Integer.toString(first));
            }

            return this;
        }

        /**
         * <p>Adds a theme, for example; adding eclipse theme:
         * <pre>{@literal
         *      <ac:parameter ac:name=\"theme\">Eclipse</ac:parameter>
         * }</pre>
         */
        public Builder theme(Themes theme)
        {
            myParameters.putIfAbsent(Parameters.THEME, theme.toString());
            return this;
        }

        /**
         * <p>Sets the title for the code block, for example, here we call it {@literal "Request"}:
         * <pre>{@literal
         *      <ac:parameter ac:name=\"title\">Request</ac:parameter>
         * }</pre>
         */
        public Builder title(String title)
        {
            myParameters.putIfAbsent(Parameters.TITLE, title);
            return this;
        }

        /**
         * The code to be displayed by this code body.
         *
         * @param code the code contents, as a String.
         * @return {@code this}.
         */
        public Builder code(String code)
        {
            // escape code in <![CDATA[ ... ]]> wrapper
            this.myCode = "<![CDATA[" + code + "]]>";
            return this;
        }
    }

}
