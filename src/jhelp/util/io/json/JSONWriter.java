/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not
 * responsible about any damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that
 * avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.util.io.json;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;

/**
 * Serialize valid object annotated {@link JSONObject} into JSON. <br>
 * To retrieve the instance just use {@link JSONReader} for parse the JSON.
 */
public class JSONWriter
{
    /**
     * Write/serialize valid object annotated {@link JSONObject} into JSON
     *
     * @param object       Object to serialize
     * @param outputStream Stream where write. Note: stream is not close, it is caller responsibility
     * @param compressed   Indicates if use compress mode or not.
     * @throws IOException   On writing issue
     * @throws JSONException If given object class is not a valid annotated {@link JSONObject}
     */
    public static void writeJSON(Object object, OutputStream outputStream, boolean compressed)
            throws IOException, JSONException
    {
        JSONWriter.writeJSON(object,
                             new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8")),
                             compressed);
    }

    /**
     * Write/serialize valid object annotated {@link JSONObject} into JSON
     *
     * @param object         Object to serialize
     * @param bufferedWriter Stream where write. Note: stream is not close, it is caller responsibility
     * @param compressed     Indicates if use compress mode or not.
     * @throws IOException   On writing issue
     * @throws JSONException If given object class is not a valid annotated {@link JSONObject}
     */
    public static void writeJSON(Object object, BufferedWriter bufferedWriter, boolean compressed)
            throws IOException, JSONException
    {
        JSONWriter.writeJSON(compressed
                             ? Integer.MIN_VALUE
                             : 0, object, bufferedWriter);
    }

    /**
     * Write/serialize a part of JSON for given object
     *
     * @param header         Header to add
     * @param object         Object to serialize
     * @param bufferedWriter
     * @throws IOException   On writing issue
     * @throws JSONException If given object class is not a valid annotated {@link JSONObject} AND not a primitive AND
     * not an object represents a primitive AND not a String AND not an enum
     */
    private static void writeJSON(int header, Object object, BufferedWriter bufferedWriter)
            throws IOException, JSONException
    {
        final Class<?>   clas       = object.getClass();
        final JSONObject jsonObject = clas.getAnnotation(JSONObject.class);

        if (jsonObject == null)
        {
            if (boolean.class.equals(clas)
                    || byte.class.equals(clas)
                    || short.class.equals(clas)
                    || int.class.equals(clas)
                    || long.class.equals(clas)
                    || float.class.equals(clas)
                    || double.class.equals(clas)
                    || Boolean.class.equals(clas)
                    || Byte.class.equals(clas)
                    || Short.class.equals(clas)
                    || Integer.class.equals(clas)
                    || Long.class.equals(clas)
                    || Float.class.equals(clas)
                    || Double.class.equals(clas))
            {
                bufferedWriter.write(object.toString());
                return;
            }

            if (String.class.equals(clas)
                    || Character.class.equals(clas)
                    || char.class.equals(clas))
            {
                bufferedWriter.write("\"" + object.toString() + "\"");
                return;
            }

            if (clas.isEnum())
            {
                bufferedWriter.write("\"" + ((Enum) object).name() + "\"");
                return;
            }

            throw new JSONException(clas.getName(), " not annotated as JSONObject");
        }

        JSONWriter.writeHeader(header, bufferedWriter);
        bufferedWriter.write("{");
        header++;
        boolean     hasPrevious = false;
        JSONElement jsonElement;

        for (Field field : clas.getDeclaredFields())
        {
            jsonElement = field.getAnnotation(JSONElement.class);

            if (jsonElement != null)
            {
                if (hasPrevious)
                {
                    bufferedWriter.write(",");
                }

                if (header >= 0)
                {
                    bufferedWriter.newLine();
                }

                JSONWriter.writeHeader(header, bufferedWriter);
                bufferedWriter.write("\"" + field.getName() + "\":");
                JSONWriter.writeValue(header, bufferedWriter, object, field);
                hasPrevious = true;
            }
        }

        header--;

        if (header >= 0)
        {
            bufferedWriter.newLine();
        }

        JSONWriter.writeHeader(header, bufferedWriter);
        bufferedWriter.write("}");

        if (header >= 0)
        {
            bufferedWriter.newLine();
        }

        bufferedWriter.flush();
    }

    /**
     * Write the header
     *
     * @param header         Header size
     * @param bufferedWriter Stream where write
     * @throws IOException On write issue
     */
    private static void writeHeader(int header, BufferedWriter bufferedWriter) throws IOException
    {
        for (int i = 0; i < header; i++)
        {
            bufferedWriter.write("   ");
        }
    }

    /**
     * Write a JSON value
     *
     * @param header         Header size
     * @param bufferedWriter Stream where write
     * @param object         Object to serialize
     * @param field          Field to write
     * @throws IOException   On write issue
     * @throws JSONException If field class is not a valid annotated {@link JSONObject} AND not a primitive AND not an
     * object represents a primitive AND not a String AND not an enum
     */
    private static void writeValue(int header, BufferedWriter bufferedWriter, Object object,
                                   Field field)
            throws IOException, JSONException
    {
        field.setAccessible(true);
        Class<?> clas = field.getType();

        try
        {
            if (boolean.class.equals(clas))
            {
                bufferedWriter.write(String.valueOf(field.getBoolean(object)));
                return;
            }

            if (char.class.equals(clas))
            {
                bufferedWriter.write("\"" + String.valueOf(field.getChar(object)) + "\"");
                return;
            }

            if (byte.class.equals(clas))
            {
                bufferedWriter.write(String.valueOf(field.getByte(object)));
                return;
            }

            if (short.class.equals(clas))
            {
                bufferedWriter.write(String.valueOf(field.getShort(object)));
                return;
            }

            if (int.class.equals(clas))
            {
                bufferedWriter.write(String.valueOf(field.getInt(object)));
                return;
            }

            if (long.class.equals(clas))
            {
                bufferedWriter.write(String.valueOf(field.getLong(object)));
                return;
            }

            if (float.class.equals(clas))
            {
                bufferedWriter.write(String.valueOf(field.getFloat(object)));
                return;
            }

            if (double.class.equals(clas))
            {
                bufferedWriter.write(String.valueOf(field.getDouble(object)));
                return;
            }

            Object value = field.get(object);

            if (value == null)
            {
                bufferedWriter.write("null");
                return;
            }

            if (Boolean.class.equals(clas)
                    || Byte.class.equals(clas)
                    || Short.class.equals(clas)
                    || Integer.class.equals(clas)
                    || Long.class.equals(clas)
                    || Float.class.equals(clas)
                    || Double.class.equals(clas))
            {
                bufferedWriter.write(value.toString());
                return;
            }

            if (String.class.equals(clas) || Character.class.equals(clas))
            {
                bufferedWriter.write("\"" + value.toString() + "\"");
                return;
            }

            if (clas.isEnum())
            {
                bufferedWriter.write("\"" + ((Enum) object).name() + "\"");
                return;
            }

            if (header >= 0)
            {
                bufferedWriter.newLine();
            }

            JSONWriter.writeJSON(header, value, bufferedWriter);
        }
        catch (IOException | JSONException exception)
        {
            throw exception;
        }
        catch (Exception exception)
        {
            throw new JSONException(exception, "Failed to convert field : '", field,
                                    "' for object ", object);
        }
    }
}
