package jhelp.util.text.grammar;

import java.util.ArrayList;
import java.util.List;

/**
 * Structure of a stream extracted by a Grammar with {@link Grammar#extractStructure(java.io.InputStream)}
 *
 * @author JHelp
 */
public class Structure
{
    /**
     * Children : used if structure represents a tree
     */
    private final List<Structure> children;
    /**
     * Name : used if structure represents a tree
     */
    private final String          name;
    /**
     * Word : used if structure represents a single word
     */
    private final String          word;

    /**
     * Create a new instance of Structure
     *
     * @param string Associated string (Word or name)
     * @param isWord Indicates if structure is a single word
     */
    Structure(final String string, final boolean isWord)
    {
        if (isWord)
        {
            this.name = null;
            this.word = string;
            this.children = null;
        }
        else
        {
            this.name = string;
            this.word = null;
            this.children = new ArrayList<Structure>();
        }
    }

    /**
     * Add a child
     *
     * @param child Child to add
     */
    void addChild(final Structure child)
    {
        if (this.children != null)
        {
            this.children.add(child);
        }
    }

    /**
     * Obtain a child, if the structure {@link #isWord() is not a word}
     *
     * @param index Child index
     * @return The child
     */
    public Structure getChild(final int index)
    {
        if (this.children == null)
        {
            return null;
        }

        return this.children.get(index);
    }

    /**
     * Structure name.<br>
     * {@code null} if structure {@link #isWord() is a word}
     *
     * @return Structure name or {@code null}
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Number of children<br>
     * -1 if structure {@link #isWord() is a word}
     *
     * @return Number of children or -1
     */
    public int getNumberOfChildren()
    {
        if (this.children == null)
        {
            return -1;
        }

        return this.children.size();
    }

    /**
     * Embed word.<br>
     * {@code null} if structure {@link #isWord() is not a word}
     *
     * @return Embed word or {@code null}
     */
    public String getWord()
    {
        return this.word;
    }

    /**
     * Indicates if structure is a word.<br>
     * Word can be obtain by {@link #getWord()}
     *
     * @return {@code true} if it is a single word
     */
    public boolean isWord()
    {
        return this.word != null;
    }
}