/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any
 * damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.util.io.json;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jhelp.util.HashCode;
import jhelp.util.list.Pair;
import jhelp.util.text.UtilText;

/**
 * JSON array.<br>
 * It is a list of values
 *
 * @author JHelp
 */
public class ArrayJSON
        implements Iterable<ValueJSON>
{
    /**
     * List of values
     */
    private final List<ValueJSON> values;

    /**
     * Create a new instance of ArrayJSON
     */
    public ArrayJSON()
    {
        this.values = new ArrayList<ValueJSON>();
    }

    /**
     * Parse array from stream
     *
     * @param inputStream Stream to parse
     * @return Read array
     * @throws IOException On reading issue
     */
    public static ArrayJSON parse(final InputStream inputStream) throws IOException
    {
        final StringBuilder  stringBuilder  = new StringBuilder();
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String               line           = bufferedReader.readLine();

        while (line != null)
        {
            stringBuilder.append(line);
            stringBuilder.append('\n');
            line = bufferedReader.readLine();
        }

        line = stringBuilder.toString()
                            .trim();
        final Pair<ArrayJSON, Integer> pair = ArrayJSON.parse(line, 0, line.length() - 1);

        if (pair == null)
        {
            return null;
        }

        return pair.element1;
    }

    /**
     * Parse a part of String to JSON array and next index to read.<br>
     * It returns {@code null} if String part not a JSON array
     *
     * @param string String to parse
     * @param first  First index for start parse
     * @param last   Last index for end parse
     * @return JSON array and next index OR {@code null} if String part not a JSON array
     */
    static Pair<ArrayJSON, Integer> parse(final String string, int first, int last)
    {
        first = string.indexOf('[', first);
        last = string.lastIndexOf(']', last);

        if ((first < 0) || (last < 0) || (first >= last))
        {
            return null;
        }

        final ArrayJSON arrayJSON = new ArrayJSON();

        while ((first >= 0) && (first < last))
        {
            final Pair<ValueJSON, Integer> pair = ValueJSON.parse(string, first + 1, last);

            if (pair == null)
            {
                break;
            }

            arrayJSON.addValue(pair.element1);
            first = string.indexOf(',', pair.element2);
        }

        return new Pair<ArrayJSON, Integer>(arrayJSON, last);
    }

    /**
     * Add value in the array
     *
     * @param value Value to add
     */
    public void addValue(final ValueJSON value)
    {
        if (value == null)
        {
            throw new NullPointerException("value MUST NOT be null");
        }

        this.values.add(value);
    }

    /***
     * Clear the array
     */
    public void clear()
    {
        this.values.clear();
    }

    /**
     * Obtain a JSON value
     *
     * @param index Value index
     * @return The value
     */
    public ValueJSON getValue(final int index)
    {
        return this.values.get(index);
    }

    /**
     * Array hash code <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Array hash code
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return HashCode.computeHashCode(this.values);
    }

    /**
     * Indicates if an Object is equals to this array <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param object Object to compare with
     * @return {@code true} if equals
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object object)
    {
        if (this == object)
        {
            return true;
        }

        if (null == object)
        {
            return false;
        }

        if (!(object instanceof ArrayJSON))
        {
            return false;
        }

        final ArrayJSON array = (ArrayJSON) object;

        final int length = this.values.size();

        if (length != array.values.size())
        {
            return false;
        }

        for (int i = 0; i < length; i++)
        {
            if (!this.values.get(i)
                            .equals(array.values.get(i)))
            {
                return false;
            }
        }

        return true;
    }

    /**
     * String representation <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return String representation
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        final StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append('[');

        final int length = this.values.size();

        if (length > 0)
        {
            stringBuilder.append(this.values.get(0));

            for (int i = 1; i < length; i++)
            {
                stringBuilder.append(",");
                stringBuilder.append(this.values.get(i));
            }
        }

        stringBuilder.append(']');

        return stringBuilder.toString();
    }

    /**
     * Obtain a value index.<br>
     * It returns -1 if value not inside the array
     *
     * @param valueJSON Value searched
     * @return Value index OR -1 if value not inside the array
     */
    public int indexOf(final ValueJSON valueJSON)
    {
        return this.values.indexOf(valueJSON);
    }

    /**
     * Insert a value inside the array
     *
     * @param value Value to insert
     * @param index Insert index
     */
    public void insertValue(final ValueJSON value, final int index)
    {
        if (value == null)
        {
            throw new NullPointerException("value MUST NOT be null");
        }

        this.values.add(index, value);
    }

    /**
     * Iterator on values <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Values iterator
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<ValueJSON> iterator()
    {
        return this.values.iterator();
    }

    /**
     * Number of value
     *
     * @return Number of value
     */
    public int numberOfValue()
    {
        return this.values.size();
    }

    /**
     * Remove a value from array
     *
     * @param index Value index to remove
     */
    public void removeValue(final int index)
    {
        this.values.remove(index);
    }

    /**
     * Remove a value from array
     *
     * @param value Value to remove
     */
    public void removeValue(final ValueJSON value)
    {
        this.values.remove(value);
    }

    /**
     * Serialize inside a stream
     *
     * @param outputStream Stream where write
     * @param compact      Indicates if compact version
     * @throws IOException On writing issue
     */
    public void serialize(final OutputStream outputStream, final boolean compact) throws IOException
    {
        this.serialize(new BufferedWriter(new OutputStreamWriter(outputStream)), compact);
    }

    /**
     * Serialize inside a stream
     *
     * @param bufferedWriter Stream where write
     * @param compact        Indicates if compact version
     * @throws IOException On writing issue
     */
    public void serialize(final BufferedWriter bufferedWriter, final boolean compact) throws IOException
    {
        this.serialize(bufferedWriter, compact, 0);
    }

    /**
     * Serialize inside a stream
     *
     * @param bufferedWriter Stream where write
     * @param compact        Indicates if compact version
     * @param headerSize     Header size
     * @throws IOException On writing issue
     */
    void serialize(final BufferedWriter bufferedWriter, final boolean compact, int headerSize) throws IOException
    {
        String head = UtilText.repeat('\t', headerSize);

        if (!compact)
        {
            bufferedWriter.write(head);
        }

        bufferedWriter.write("[");

        if (!compact)
        {
            bufferedWriter.newLine();
            headerSize++;
            head = UtilText.repeat('\t', headerSize);
        }

        final int length = this.values.size();

        if (length > 0)
        {
            ValueJSON valueJSON = this.values.get(0);
            ValueType valueType = valueJSON.getType();

            if ((!compact) && (valueType != ValueType.ARRAY) && (valueType != ValueType.OBJECT))
            {
                bufferedWriter.write(head);
            }

            valueJSON.serialize(bufferedWriter, compact, headerSize);

            for (int i = 1; i < length; i++)
            {
                bufferedWriter.write(",");
                valueJSON = this.values.get(i);
                valueType = valueJSON.getType();

                if (!compact)
                {
                    bufferedWriter.newLine();

                    if ((valueType != ValueType.ARRAY) && (valueType != ValueType.OBJECT))
                    {
                        bufferedWriter.write(head);
                    }
                }

                valueJSON.serialize(bufferedWriter, compact, headerSize);
            }
        }

        if (!compact)
        {
            if (length > 0)
            {
                bufferedWriter.newLine();
            }

            headerSize--;
            bufferedWriter.write(UtilText.repeat('\t', headerSize));
        }

        bufferedWriter.write("]");
        bufferedWriter.flush();
    }
}