/**
 * Project : JHelpUtil<br>
 * Package : jhelp.util.reflect<br>
 * Class : Reflector<br>
 * Date : 6 mars 2009<br>
 * By JHelp
 */
package jhelp.util.reflection;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Stack;

import jhelp.util.debug.Debug;
import jhelp.util.text.UtilText;

/**
 * Reflection utilities <br>
 * <br>
 * Last modification : 6 mars 2009<br>
 * Version 0.0.0<br>
 *
 * @author JHelp
 */
public final class Reflector
{
    /**
     * Boolean primitive class name
     */
    public static final String PRIMITIVE_BOOLEAN = "boolean";
    /**
     * Byte primitive class name
     */
    public static final String PRIMITIVE_BYTE    = "byte";
    /**
     * Char primitive class name
     */
    public static final String PRIMITIVE_CHAR    = "char";
    /**
     * Double primitive class name
     */
    public static final String PRIMITIVE_DOUBLE  = "double";
    /**
     * Float primitive class name
     */
    public static final String PRIMITIVE_FLOAT   = "float";
    /**
     * Int primitive class name
     */
    public static final String PRIMITIVE_INT     = "int";
    /**
     * Long primitive class name
     */
    public static final String PRIMITIVE_LONG    = "long";
    /**
     * Short primitive class name
     */
    public static final String PRIMITIVE_SHORT   = "short";

    /**
     * Force change a boolean field value
     *
     * @param object    Object where lies the field
     * @param fieldName Field name
     * @param newValue  New value
     * @throws NoSuchFieldException     If field not exists
     * @throws IllegalArgumentException If field isn't boolean
     * @throws IllegalAccessException   If force failed
     */
    public static void forceChangeField(final Object object, final String fieldName, final boolean newValue)
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException
    {
        Field    field  = null;
        Class<?> clazz  = object.getClass();
        Field[]  fields = clazz.getDeclaredFields();

        while ((field == null) && (clazz != null))
        {
            if (fields != null)
            {
                Field f;
                for (final Field field2 : fields)
                {
                    f = field2;
                    if (f.getName()
                         .equals(fieldName))
                    {
                        field = f;
                        break;
                    }
                }
            }

            if ((field == null) && ((clazz = clazz.getSuperclass()) != null))
            {
                fields = clazz.getDeclaredFields();
            }
        }

        if (field == null)
        {
            throw new NoSuchFieldException(UtilText.concatenate("The field ", fieldName, " not found in ",
                                                                object.getClass()
                                                                      .getName()));
        }

        field.setAccessible(true);
        field.setBoolean(object, newValue);
    }

    /**
     * Force change a field value.
     *
     * @param object    Object where lies the field
     * @param fieldName Field name
     * @param newValue  New value
     * @throws NoSuchFieldException     If field doesn't exists
     * @throws IllegalArgumentException If value is not valid
     * @throws IllegalAccessException   If failed to change the field
     */
    public static void forceChangeField(final Object object, final String fieldName, final Object newValue)
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException
    {
        Field    field  = null;
        Class<?> clazz  = object.getClass();
        Field[]  fields = clazz.getDeclaredFields();

        while ((field == null) && (clazz != null))
        {
            if (fields != null)
            {
                Field f;
                for (final Field field2 : fields)
                {
                    f = field2;
                    if (f.getName()
                         .equals(fieldName))
                    {
                        field = f;
                        break;
                    }
                }
            }

            if (field == null)
            {
                if ((clazz = clazz.getSuperclass()) != null)
                {
                    fields = clazz.getDeclaredFields();
                }
            }
        }

        if (field == null)
        {
            throw new NoSuchFieldException(UtilText.concatenate("The field ", fieldName, " not found in ",
                                                                object.getClass()
                                                                      .getName()));
        }

        field.setAccessible(true);
        field.set(object, newValue);
    }

    /**
     * Force to obtain a field value
     *
     * @param object    Object where lies the field
     * @param fieldName Field name
     * @return Field value
     * @throws NoSuchFieldException     If field not exists
     * @throws IllegalArgumentException Should never append
     * @throws IllegalAccessException   If access cant be force
     */
    public static Object forceGetField(final Object object, final String fieldName)
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException
    {
        Field    field  = null;
        Class<?> clazz  = object.getClass();
        Field[]  fields = clazz.getDeclaredFields();

        while ((field == null) && (clazz != null))
        {
            if (fields != null)
            {
                Field f;
                for (final Field field2 : fields)
                {
                    f = field2;
                    if (f.getName()
                         .equals(fieldName))
                    {
                        field = f;
                        break;
                    }
                }
            }

            if (field == null)
            {
                if ((clazz = clazz.getSuperclass()) != null)
                {
                    fields = clazz.getDeclaredFields();
                }
            }
        }

        if (field == null)
        {
            throw new NoSuchFieldException(UtilText.concatenate("The field ", fieldName, " not found in ",
                                                                object.getClass()
                                                                      .getName()));
        }

        field.setAccessible(true);
        return field.get(object);
    }

    /**
     * Get a static field value
     *
     * @param claz      Class where lies the field
     * @param fieldName Filed name
     * @return Field value
     * @throws NoSuchFieldException     If field not exists
     * @throws IllegalArgumentException If field isn't static
     * @throws IllegalAccessException   If force failed
     */
    public static Object forceGetStaticField(final Class<?> claz, final String fieldName)
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException
    {
        Field    field  = null;
        Field[]  fields = claz.getDeclaredFields();
        Class<?> clazz  = claz;

        while ((field == null) && (clazz != null))
        {
            if (fields != null)
            {
                Field f;
                for (final Field field2 : fields)
                {
                    f = field2;
                    if (f.getName()
                         .equals(fieldName))
                    {
                        field = f;
                        break;
                    }
                }
            }

            if (field == null)
            {
                if ((clazz = clazz.getSuperclass()) != null)
                {
                    fields = clazz.getDeclaredFields();
                }
            }
        }

        if (field == null)
        {
            throw new NoSuchFieldException(
                    UtilText.concatenate("The field ", fieldName, " not found in ", claz.getName()));
        }

        field.setAccessible(true);
        return field.get(null);
    }

    /**
     * Force invoke a method
     *
     * @param object     Object where lies the method
     * @param methodName Method name
     * @param types      Types of arguments
     * @param values     Parameters value
     * @throws NoSuchMethodException     If method not exists
     * @throws IllegalArgumentException  If one of argument not valid
     * @throws IllegalAccessException    If force failed
     * @throws InvocationTargetException If launch method failed
     */
    public static void forceInvokeMethod(final Object object, final String methodName, final Class<?>[] types,
                                         final Object[] values)
            throws NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
            InvocationTargetException
    {
        Method   method     = null;
        Class<?> clazz      = object.getClass();
        Method[] allMethods = clazz.getDeclaredMethods();

        while ((method == null) && (clazz != null))
        {
            if (allMethods != null)
            {
                for (final Method m : allMethods)
                {
                    if ((m.getName()
                          .equals(methodName)) && (Reflector.typeMatch(types, m.getParameterTypes())))
                    {
                        method = m;
                        break;
                    }
                }
            }

            if ((method == null) && ((clazz = clazz.getSuperclass()) != null))
            {
                allMethods = clazz.getDeclaredMethods();
            }
        }

        if (method == null)
        {
            final StringBuilder stringBuffer = new StringBuilder();
            stringBuffer.append(object.getClass()
                                      .getName());
            stringBuffer.append('.');
            stringBuffer.append(methodName);
            stringBuffer.append('(');
            if ((types != null) && (types.length > 0))
            {
                if (types[0] == null)
                {
                    stringBuffer.append("null");
                }
                else
                {
                    stringBuffer.append(types[0].getName());

                }
                for (int i = 1; i < types.length; i++)
                {
                    stringBuffer.append(", ");
                    if (types[i] == null)
                    {
                        stringBuffer.append("null");
                    }
                    else
                    {
                        stringBuffer.append(types[i].getName());

                    }
                }
            }
            stringBuffer.append(')');
            throw new NoSuchMethodException(stringBuffer.toString());
        }

        method.setAccessible(true);
        method.invoke(object, values);
    }

    /**
     * Invoke a public method from a class.<br>
     * It is recommends to call static method with this, for non static, prefer use
     * {@link #invokePublicMethod(Object, String, Object...)}<br>
     * If the method is non static, this method first try create an instance of the specified class with a "little"
     * constructor,
     * it use {@link #newInstance(Class)} to do it.<br>
     * <b>NOTE :</b>The method must be public, this method haven't rights to invoke other methods, so if not public, not
     * found
     * exception was throw
     *
     * @param clazz      Class where the method lies
     * @param methodName Method name to invoke
     * @param parameters Methods parameters
     * @return method result or {@code null}
     * @throws NoSuchMethodException     If the given method is not public, or not exists with the parameters
     * @throws IllegalArgumentException  When invoke failed
     * @throws IllegalAccessException    When invoke failed
     * @throws InvocationTargetException When invoke failed
     */
    public static Object invokePublicMethod(final Class<?> clazz, final String methodName, final Object... parameters)
            throws NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        if (clazz == null)
        {
            throw new NullPointerException("clazz MUST NOT be null");
        }
        if (methodName == null)
        {
            throw new NullPointerException("methodName MUST NOT be null");
        }
        final Class<?>[] types  = Reflector.obtainTypes(parameters);
        final Method     method = Reflector.obtainPublicMethod(clazz, methodName, types);

        if (Modifier.isStatic(method.getModifiers()))
        {
            return method.invoke(null, parameters);
        }
        else
        {
            return method.invoke(Reflector.newInstance(clazz), parameters);
        }
    }

    /**
     * Invoke a public method form a class represents by its instance.<br>
     * We use this instance for invoke the method.<br>
     * <b>NOTE :</b>The method must be public, this method haven't rights to invoke other methods, so if not public, not
     * found
     * exception was throw
     *
     * @param instance   Instance of the class where method lies
     * @param methodName Method name to invoke
     * @param parameters Methods parameters
     * @return method result
     * @throws NoSuchMethodException     If the given method is not public, or not exists with the parameters
     * @throws IllegalArgumentException  When invoke failed
     * @throws IllegalAccessException    When invoke failed
     * @throws InvocationTargetException When invoke failed
     */
    public static Object invokePublicMethod(final Object instance, final String methodName, final Object... parameters)
            throws NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        if (instance == null)
        {
            throw new NullPointerException("instance MUST NOT be null");
        }
        if (methodName == null)
        {
            throw new NullPointerException("methodName MUST NOT be null");
        }
        final Class<?>[] types  = Reflector.obtainTypes(parameters);
        final Method     method = Reflector.obtainPublicMethod(instance.getClass(), methodName, types);
        return method.invoke(instance, parameters);
    }

    /**
     * Indicates if a class extends an other one
     *
     * @param test   Class to test
     * @param parent Class to extends
     * @return {@code true} if class extends an other one
     */
    public static boolean isInherit(Class<?> test, final Class<?> parent)
    {
        if (test.equals(parent))
        {
            return true;
        }

        if ((test.isPrimitive()) || (test.isArray()))
        {
            return false;
        }

        final Stack<Class<?>> stack = new Stack<Class<?>>();
        stack.push(test);

        while (!stack.isEmpty())
        {
            test = stack.pop();

            if (test.equals(parent))
            {
                return true;
            }

            if (test.getSuperclass() != null)
            {
                stack.push(test.getSuperclass());
            }

            for (final Class<?> interf : test.getInterfaces())
            {
                stack.push(interf);
            }
        }

        return false;
    }

    /**
     * Indicates if a class extends or implements directly or not a class or a interface
     *
     * @param testedClass       Class tested
     * @param parentOrInterface Class or interface to extends or implements
     * @return {@code true} if the class extends or implements directly or not the class or interface
     */
    public static boolean isSubTypeOf(Class<?> testedClass, final Class<?> parentOrInterface)
    {
        while (testedClass != null)
        {
            if (testedClass.equals(parentOrInterface))
            {
                return true;
            }

            for (final Class<?> interf : testedClass.getInterfaces())
            {
                if (Reflector.isSubTypeOf(interf, parentOrInterface))
                {
                    return true;
                }
            }

            testedClass = testedClass.getSuperclass();
        }

        return false;
    }

    /**
     * Try create an instance of a class.<br>
     * It first look for "little" public constructor, that is to say a constructor with few parameters, if it founds
     * default
     * constructor without parameters, it use it.<br>
     * If the constructor founds have parameters, it creates a instance of each parameters (on calling itself) an the use
     * them
     * for construct the instance.<br>
     * <b>BEWARE:</b>
     * <ul>
     * <li>In some case, it can falls in infinite loop</li>
     * <li>It is not human, so can't read documentation and may use illegal argument for create the instance</li>
     * </ul>
     *
     * @param type Class type to create
     * @return Created instance
     */
    public static Object newInstance(Class<?> type)
    {
        if (type == null)
        {
            throw new NullPointerException("type MUST NOT be null");
        }
        if (type.isPrimitive())
        {
            final String nom = type.getName();
            if (Reflector.PRIMITIVE_BOOLEAN.equals(nom))
            {
                return Boolean.TRUE;
            }
            if (Reflector.PRIMITIVE_BYTE.equals(nom))
            {
                return new Byte((byte) 0);
            }
            if (Reflector.PRIMITIVE_SHORT.equals(nom))
            {
                return new Short((short) 0);
            }
            if (Reflector.PRIMITIVE_INT.equals(nom))
            {
                return new Integer(0);
            }
            if (Reflector.PRIMITIVE_FLOAT.equals(nom))
            {
                return new Float(0.0F);
            }
            if (Reflector.PRIMITIVE_LONG.equals(nom))
            {
                return new Long(0L);
            }
            if (Reflector.PRIMITIVE_DOUBLE.equals(nom))
            {
                return new Double(0.0D);
            }
            if (Reflector.PRIMITIVE_CHAR.equals(nom))
            {
                return new Character(' ');
            }
        }

        if (type.getName()
                .equals(String.class.getName()))
        {
            return "";
        }

        if (type.isEnum())
        {
            try
            {
                return Array.get(Reflector.invokePublicMethod(type, "values"), 0);
            }
            catch (final Exception exception)
            {
                Debug.printException(exception);

                return null;
            }
        }

        if (type.isArray())
        {
            int n = 1;
            for (type = type.getComponentType(); type.isArray(); type = type.getComponentType())
            {
                n++;
            }

            final int dim[] = new int[n];
            for (int i = 0; i < n; i++)
            {
                dim[i] = 1;
            }

            final Object o = Array.newInstance(type, dim);
            if (type.isPrimitive())
            {
                return o;
            }

            Object val = o;
            for (int i = 1; i < n; i++)
            {
                val = Array.get(val, 0);
            }

            Array.set(val, 0, Reflector.newInstance(type));

            return o;
        }

        final Constructor<?> constructeurs[] = type.getConstructors();
        Constructor<?>       c               = null;
        int                  nb              = 0x7fffffff;
        Class<?>             types[]         = null;
        for (final Constructor<?> constructeur : constructeurs)
        {
            final Class<?> typ[] = constructeur.getParameterTypes();
            if ((typ == null) || (typ.length < nb) || (c == null))
            {
                types = typ;
                c = constructeur;
                if (types == null)
                {
                    nb = 0;
                }
                else
                {
                    nb = types.length;
                }
            }
        }

        if (c != null)
        {
            final Object arguments[] = new Object[nb];
            for (int i = 0; i < nb; i++)
            {
                arguments[i] = Reflector.newInstance(types[i]);
            }

            try
            {
                return c.newInstance(arguments);
            }
            catch (final IllegalArgumentException | InvocationTargetException | IllegalAccessException |
                    InstantiationException ignored)
            {
            }
        }

        return null;
    }

    /**
     * Try create an instance of a class.<br>
     * It first look for "little" public constructor, that is to say a constructor with few parameters, if it founds
     * default
     * constructor without parameters, it use it.<br>
     * If the constructor founds have parameters, it creates a instance of each parameters (on calling itself) an the use
     * them
     * for construct the instance.<br>
     * <b>BEWARE:</b>
     * <ul>
     * <li>In some case, it can falls in infinite loop</li>
     * <li>It is not human, so can't read documentation and can use illegal argument for create the instance</li>
     * </ul>
     *
     * @param typeName Class name
     * @return Instance created
     * @throws ClassNotFoundException If the class can't be resolve
     */
    public static Object newInstance(final String typeName) throws ClassNotFoundException
    {
        if (typeName == null)
        {
            throw new NullPointerException("typeName MUST NOT be null");
        }
        return Reflector.newInstance(Class.forName(typeName));
    }

    /**
     * Try create an instance of a class.<br>
     * It first look for "little" public constructor, that is to say a constructor with few parameters, if it founds
     * default
     * constructor without parameters, it use it.<br>
     * If the constructor founds have parameters, it creates a instance of each parameters (on calling itself) an the use
     * them
     * for construct the instance.<br>
     * <b>BEWARE:</b>
     * <ul>
     * <li>In some case, it can falls in infinite loop</li>
     * <li>It is not human, so can't read documentation and can use illegal argument for create the instance</li>
     * </ul>
     *
     * @param typeName    Type name
     * @param classLoader Class loader to use
     * @return Instance created
     * @throws ClassNotFoundException If the class can't be resolve
     */
    public static Object newInstance(final String typeName, final ClassLoader classLoader) throws ClassNotFoundException
    {
        if (typeName == null)
        {
            throw new NullPointerException("typeName MUST NOT be null");
        }
        return Reflector.newInstance(classLoader.loadClass(typeName));
    }

    /**
     * Obatin the class corresponds to a full name (package + name or primitive name like int, byte, ...)
     *
     * @param className Class name
     * @return Correspoing class
     * @throws ClassNotFoundException If the class can't be found
     */
    public static Class<?> obtainClass(final String className) throws ClassNotFoundException
    {
        if (Reflector.PRIMITIVE_BOOLEAN.equals(className))
        {
            return boolean.class;
        }

        if (Reflector.PRIMITIVE_BYTE.equals(className))
        {
            return byte.class;
        }

        if (Reflector.PRIMITIVE_CHAR.equals(className))
        {
            return char.class;
        }

        if (Reflector.PRIMITIVE_DOUBLE.equals(className))
        {
            return double.class;
        }

        if (Reflector.PRIMITIVE_FLOAT.equals(className))
        {
            return float.class;
        }

        if (Reflector.PRIMITIVE_INT.equals(className))
        {
            return int.class;
        }

        if (Reflector.PRIMITIVE_LONG.equals(className))
        {
            return long.class;
        }

        if (Reflector.PRIMITIVE_SHORT.equals(className))
        {
            return short.class;
        }

        return Class.forName(className);
    }

    /**
     * Obtain a field
     *
     * @param object    Object where lies the field
     * @param fieldName Field name
     * @return Filed
     * @throws NoSuchFieldException   If field not exists
     * @throws IllegalAccessException If force failed
     */
    public static Field obtainField(final Object object, final String fieldName) throws NoSuchFieldException,
            IllegalAccessException
    {
        Field    field  = null;
        Class<?> clazz  = object.getClass();
        Field[]  fields = clazz.getDeclaredFields();

        while ((field == null) && (clazz != null))
        {
            if (fields != null)
            {
                Field f;
                for (final Field field2 : fields)
                {
                    f = field2;
                    if (f.getName()
                         .equals(fieldName))
                    {
                        field = f;
                        break;
                    }
                }
            }

            if ((field == null) && ((clazz = clazz.getSuperclass()) != null))
            {
                fields = clazz.getDeclaredFields();
            }
        }

        if (field == null)
        {
            return null;
        }

        field.setAccessible(true);
        return field;
    }

    /**
     * Obtain public method from a class
     *
     * @param clazz      Class where method lies
     * @param methodName Method name
     * @param types      Method parameters type
     * @return The method
     * @throws NoSuchMethodException If the method not public or not exists with specified types
     */
    public static Method obtainPublicMethod(final Class<?> clazz, final String methodName, final Class<?>... types)
            throws NoSuchMethodException
    {
        Method         method        = null;
        final Method[] publicMethods = clazz.getMethods();
        if (publicMethods != null)
        {
            for (int i = 0; (i < publicMethods.length) && (method == null); i++)
            {
                final Method m = publicMethods[i];
                if ((m.getName()
                      .equals(methodName)) && (Reflector.typeMatch(types, m.getParameterTypes())))
                {
                    method = m;
                }
            }
        }
        if (method == null)
        {
            final StringBuilder stringBuffer = new StringBuilder();
            stringBuffer.append(clazz.getName());
            stringBuffer.append('.');
            stringBuffer.append(methodName);
            stringBuffer.append('(');
            if ((types != null) && (types.length > 0))
            {
                if (types[0] == null)
                {
                    stringBuffer.append("null");
                }
                else
                {
                    stringBuffer.append(types[0].getName());

                }
                for (int i = 1; i < types.length; i++)
                {
                    stringBuffer.append(", ");
                    if (types[i] == null)
                    {
                        stringBuffer.append("null");
                    }
                    else
                    {
                        stringBuffer.append(types[i].getName());

                    }
                }
            }
            stringBuffer.append(')');
            throw new NoSuchMethodException(stringBuffer.toString());
        }
        return method;
    }

    /**
     * Extract all types of given parameters.<br>
     * If the parameter is {@code null}, just {@code null} is return.<br>
     * If some parameters are {@code null}, the corresponding type was also {@code null}
     *
     * @param parameters Parameters to extract is types
     * @return Extracted types.
     */
    public static Class<?>[] obtainTypes(final Object... parameters)
    {
        if (parameters == null)
        {
            return null;
        }
        final int        length = parameters.length;
        final Class<?>[] types  = new Class[length];
        for (int i = 0; i < length; i++)
        {
            if (parameters[i] != null)
            {
                types[i] = parameters[i].getClass();
            }
        }
        return types;
    }

    /**
     * Indicates if 2 types are similar.<br>
     * {@code null} is accepted for parameters
     *
     * @param class1 First type
     * @param class2 Second type
     * @return {@code true} if types are similar
     */
    public static boolean typeMatch(final Class<?> class1, final Class<?> class2)
    {
        if (class1 == null)
        {
            if (class2 == null)
            {
                return true;
            }
            return !class2.isPrimitive();
        }
        if (class2 == null)
        {
            return !class1.isPrimitive();
        }
        if (class1.equals(class2))
        {
            return true;
        }
        if (class1.getName()
                  .equals(class2.getName()))
        {
            return true;
        }
        if (class1.isPrimitive())
        {
            final String name = class1.getName();
            if (Reflector.PRIMITIVE_BOOLEAN.equals(name))
            {
                return class2.getName()
                             .equals(Boolean.class.getName());
            }
            if (Reflector.PRIMITIVE_BYTE.equals(name))
            {
                return class2.getName()
                             .equals(Byte.class.getName());
            }
            if (Reflector.PRIMITIVE_SHORT.equals(name))
            {
                return class2.getName()
                             .equals(Short.class.getName());
            }
            if (Reflector.PRIMITIVE_INT.equals(name))
            {
                return class2.getName()
                             .equals(Integer.class.getName());
            }
            if (Reflector.PRIMITIVE_FLOAT.equals(name))
            {
                return class2.getName()
                             .equals(Float.class.getName());
            }
            if (Reflector.PRIMITIVE_LONG.equals(name))
            {
                return class2.getName()
                             .equals(Long.class.getName());
            }
            if (Reflector.PRIMITIVE_DOUBLE.equals(name))
            {
                return class2.getName()
                             .equals(Double.class.getName());
            }
            if (Reflector.PRIMITIVE_CHAR.equals(name))
            {
                return class2.getName()
                             .equals(Character.class.getName());
            }
        }
        if (class2.isPrimitive())
        {
            final String name = class2.getName();
            if (Reflector.PRIMITIVE_BOOLEAN.equals(name))
            {
                return class1.getName()
                             .equals(Boolean.class.getName());
            }
            if (Reflector.PRIMITIVE_BYTE.equals(name))
            {
                return class1.getName()
                             .equals(Byte.class.getName());
            }
            if (Reflector.PRIMITIVE_SHORT.equals(name))
            {
                return class1.getName()
                             .equals(Short.class.getName());
            }
            if (Reflector.PRIMITIVE_INT.equals(name))
            {
                return class1.getName()
                             .equals(Integer.class.getName());
            }
            if (Reflector.PRIMITIVE_FLOAT.equals(name))
            {
                return class1.getName()
                             .equals(Float.class.getName());
            }
            if (Reflector.PRIMITIVE_LONG.equals(name))
            {
                return class1.getName()
                             .equals(Long.class.getName());
            }
            if (Reflector.PRIMITIVE_DOUBLE.equals(name))
            {
                return class1.getName()
                             .equals(Double.class.getName());
            }
            if (Reflector.PRIMITIVE_CHAR.equals(name))
            {
                return class1.getName()
                             .equals(Character.class.getName());
            }
        }
        if ((class1.isArray()) && (class2.isArray()))
        {
            return Reflector.typeMatch(class1.getComponentType(), class2.getComponentType());
        }
        return false;
    }

    /**
     * Indicates if 2 arrays of types are similar.<br>
     * Array says similar if they have same length and each type of arrays are one to one similar<br>
     * {@code null} are consider like zero length array<br>
     * Each array can contains {@code null} elements.<br>
     * For the compare 2 types, it use {@link #typeMatch(Class, Class)}
     *
     * @param types1 First array
     * @param types2 Second array
     * @return {@code true} if arrays are similar
     */
    public static boolean typeMatch(final Class<?>[] types1, final Class<?>[] types2)
    {
        if (types1 == null)
        {
            return (types2 == null) || (types2.length == 0);
        }
        if (types2 == null)
        {
            return types1.length == 0;
        }
        if (types1.length != types2.length)
        {
            return false;
        }
        for (int i = 0; i < types1.length; i++)
        {
            if (!Reflector.typeMatch(types1[i], types2[i]))
            {
                return false;
            }
        }
        return true;
    }
}