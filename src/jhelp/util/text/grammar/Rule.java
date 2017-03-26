package jhelp.util.text.grammar;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jhelp.util.debug.Debug;
import jhelp.util.debug.DebugLevel;
import jhelp.util.list.Pair;

/**
 * Grammar rule
 *
 * @author JHelp
 */
public class Rule
{
    /**
     * Definitions list
     */
    private final List<SimpleDefinition> definitions;
    /**
     * Rule name
     */
    private final String                 name;

    /**
     * Create a new instance of Rule
     *
     * @param name Rule name
     */
    public Rule(final String name)
    {
        this.name = name;
        this.definitions = new ArrayList<SimpleDefinition>();
    }

    /**
     * Add a definition
     *
     * @param definition Definition to add
     */
    public void addDefinition(final SimpleDefinition definition)
    {
        if (definition == null)
        {
            throw new NullPointerException("definition MUST NOT be null");
        }

        this.definitions.add(definition);
    }

    /**
     * Clear the rule.<br>
     * No more definition inside
     */
    public void clear()
    {
        for (final SimpleDefinition definition : this.definitions)
        {
            definition.clear();
        }

        this.definitions.clear();
    }

    /**
     * Obtain a definition
     *
     * @param index Definition index
     * @return The definition
     */
    public SimpleDefinition getDefinition(final int index)
    {
        return this.definitions.get(index);
    }

    /**
     * Number of definition
     *
     * @return Number of definition
     */
    public int getDefinitionCount()
    {
        return this.definitions.size();
    }

    /**
     * Rule name
     *
     * @return Rule name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Search the list of definitions that a String match
     *
     * @param string String test
     * @return List of Pair of the definition that match and the matcher (Matcher can be used to extract captured groups)
     */
    public List<Pair<SimpleDefinition, Matcher>> searchMatchingDefinition(final String string)
    {
        Pattern                                     pattern;
        Matcher                                     matcher;
        final List<Pair<SimpleDefinition, Matcher>> list = new ArrayList<Pair<SimpleDefinition, Matcher>>();

        for (final SimpleDefinition definition : this.definitions)
        {
            pattern = Pattern.compile(definition.toRegex());
            matcher = pattern.matcher(string);

            Debug.println(DebugLevel.VERBOSE, "<", this.name, "> := ", definition.toRegex(), "\n ON \n", string);

            if (matcher.matches())
            {
                Debug.println(DebugLevel.VERBOSE, "SUCCEED !");
                list.add(new Pair<SimpleDefinition, Matcher>(definition, matcher));
            }
            else
            {
                Debug.println(DebugLevel.VERBOSE, "FAILED !");
            }
        }

        return list;
    }
}