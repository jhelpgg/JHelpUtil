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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jhelp.util.debug.Debug;
import jhelp.util.debug.DebugLevel;
import jhelp.util.reflection.Reflector;

/**
 * Read a JSON, previously wrote by {@link JSONWriter}, and parse it to fill an object.<br>
 * The object read must be annotated {@link JSONObject} and serializable/parcelable (See {@link JSONObject}
 * documentation).<br>
 * The parsing mode can be strict (Bot accept removed field from last serialization) or not (Accept some changes)
 */
public class JSONReader
{
    /**
     * Parse stream of JSON to get serialized object.<br>
     * The class MUST ba a valid annotated {@link JSONObject}<br>
     * If strict mode enable, only same version of given class serialized can be parsed. If not strict mode some
     * modification can be tolerate.<br>
     * Modifications tolerate are adding/removing fields annotated {@link JSONElement}, but not accept the type
     * modification.
     *
     * @param clas        Object class to get
     * @param inputStream Stream to read
     * @param strict      Indicates if strict mode or not
     * @param <OBJECT>    Class of object to parse
     * @return Parsed object
     * @throws IOException   On reading issue
     * @throws JSONException If stream not valid JSON or not corresponds to desired class
     */
    @SuppressWarnings("unchecked")
    public static <OBJECT> OBJECT readJSON(Class<OBJECT> clas, InputStream inputStream,
                                           boolean strict) throws
            IOException, JSONException
    {
        final JSONObject jsonObject = clas.getAnnotation(JSONObject.class);

        if (jsonObject == null)
        {
            throw new JSONException(clas.getName(), " not annotated as JSONObject");
        }

        ObjectJSON objectJSON = ObjectJSON.parse(inputStream);
        OBJECT     object     = (OBJECT) Reflector.newInstance(clas);
        JSONReader.fillObject(clas, object, objectJSON, strict);
        return object;
    }

    /**
     * Fill object fields from JSON object
     *
     * @param clas       Object class to fill
     * @param object     Object to fill
     * @param objectJSON JSON to parse
     * @param strict     Indicates if strict mode
     * @throws JSONException If JSON not valid for object to fill
     */
    @SuppressWarnings("unchecked")
    private static void fillObject(Class clas, Object object, ObjectJSON objectJSON, boolean strict)
            throws JSONException
    {
        if (object == null)
        {
            return;
        }

        Field       field;
        JSONElement jsonElement;
        Class       fieldClass;

        for (String key : objectJSON.getKeys())
        {
            try
            {
                field = clas.getDeclaredField(key);
                field.setAccessible(true);
                jsonElement = field.getAnnotation(JSONElement.class);

                if (jsonElement == null)
                {
                    if (strict)
                    {
                        throw new JSONException("The field : ", key, " in ", clas.getName(),
                                                " is not annotated JSONElement");
                    }

                    Debug.println(DebugLevel.WARNING, "The field : ", key, " in ", clas.getName(),
                                  " is not annotated JSONElement");
                    continue;
                }

                fieldClass = field.getType();

                if (boolean.class.equals(fieldClass))
                {
                    field.setBoolean(object, objectJSON.getBoolean(key, false));
                }
                else if (char.class.equals(fieldClass))
                {
                    field.setChar(object, objectJSON.getString(key, " ")
                                                    .charAt(0));
                }
                else if (byte.class.equals(fieldClass))
                {
                    field.setByte(object, (byte) (objectJSON.getInt(key, 0) & 0xFF));
                }
                else if (short.class.equals(fieldClass))
                {
                    field.setShort(object, (short) (objectJSON.getInt(key, 0) & 0xFFFF));
                }
                else if (int.class.equals(fieldClass))
                {
                    field.setInt(object, objectJSON.getInt(key, 0));
                }
                else if (long.class.equals(fieldClass))
                {
                    field.setLong(object, objectJSON.getLong(key, 0));
                }
                else if (float.class.equals(fieldClass))
                {
                    field.setFloat(object, objectJSON.getFloat(key, 0));
                }
                else if (double.class.equals(fieldClass))
                {
                    field.setDouble(object, objectJSON.getDouble(key, 0));
                }
                else //noinspection StatementWithEmptyBody
                    if (objectJSON.get(key)
                                   .getType() == ValueType.NULL)
                {
                    //TODO : do something ?
                    // field.set(object, null);
                }
                else if (Boolean.class.equals(fieldClass))
                {
                    field.set(object, objectJSON.getBoolean(key, false));
                }
                else if (Character.class.equals(fieldClass))
                {
                    field.set(object, objectJSON.getString(key, " ")
                                                .charAt(0));
                }
                else if (Byte.class.equals(fieldClass))
                {
                    field.set(object, (byte) (objectJSON.getInt(key, 0) & 0xFF));
                }
                else if (Short.class.equals(fieldClass))
                {
                    field.set(object, (short) (objectJSON.getInt(key, 0) & 0xFFFF));
                }
                else if (Integer.class.equals(fieldClass))
                {
                    field.set(object, objectJSON.getInt(key, 0));
                }
                else if (Long.class.equals(fieldClass))
                {
                    field.set(object, objectJSON.getLong(key, 0));
                }
                else if (Float.class.equals(fieldClass))
                {
                    field.set(object, objectJSON.getFloat(key, 0));
                }
                else if (Double.class.equals(fieldClass))
                {
                    field.set(object, objectJSON.getDouble(key, 0));
                }
                else if (String.class.equals(fieldClass))
                {
                    field.set(object, objectJSON.getString(key, ""));
                }
                else if (fieldClass.isEnum())
                {
                    if (!objectJSON.get(key)
                                   .isNull())
                    {
                        final String name = objectJSON.getString(key, "");

                        try
                        {
                            final Method valueOf = fieldClass.getMethod("valueOf", String.class);
                            valueOf.setAccessible(true);
                            field.set(object, valueOf.invoke(null, name));
                        }
                        catch (NoSuchMethodException | InvocationTargetException exception)
                        {
                            //Since it is a enum this exception MUST never happen
                            throw new RuntimeException(
                                    "A very strange enum without a valid valueOf : " + fieldClass.getName(),
                                    exception);
                        }
                        catch (Exception exception)
                        {
                            throw new JSONException(exception, "Can't set '", name, "' to enum ", fieldClass.getName(),
                                                    " in ", field, " of ", clas.getName());
                        }
                    }
                }
                else
                {
                    Object    instance;
                    ValueJSON valueJSON = objectJSON.get(key);

                    switch (valueJSON.getType())
                    {
                        case BOOLEAN:
                            instance = objectJSON.getBoolean(key, false);
                            break;
                        case NUMBER:
                            instance = valueJSON.getNumber();
                            break;
                        case ARRAY:
                        case NULL:
                            continue;
                        case STRING:
                            instance = objectJSON.getString(key, "");
                            break;
                        case OBJECT:
                        default:
                            instance = Reflector.newInstance(fieldClass);
                            JSONReader.fillObject(fieldClass, instance, objectJSON.getObject(key),
                                                  strict);
                            break;
                    }

                    if (instance != null)
                    {
                        field.set(object, instance);
                    }
                }
            }
            catch (NoSuchFieldException e)
            {
                if (strict)
                {
                    throw new JSONException(e, "Not found the field : ", key, " in ",
                                            clas.getName());
                }

                Debug.printException(e, "Not found the field : ", key, " in ", clas.getName());
            }
            catch (IllegalAccessException e)
            {
                throw new JSONException(e, "Can't write inside the field : ", key, " in ",
                                        clas.getName());
            }
        }
    }
}
