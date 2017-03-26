package jhelp.util.reflection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import jhelp.util.debug.Debug;
import jhelp.util.debug.DebugLevel;

/**
 * A fake version of a class.<br>
 * All methods return dummy things.
 */
public class FalseClass
{
    /**
     * Invocation handler that returns dummy values.
     */
    private static final DummyInvocationHandler DUMMY_INVOCATION_HANDLER = new DummyInvocationHandler();

    /**
     * Create a fake instance of an interface.
     *
     * @param interfaceClass Interface to fake
     * @param <INTERFACE>    Interface type
     * @return Fake instance
     */
    @SuppressWarnings("unchecked")
    public static <INTERFACE> INTERFACE createFalseClass(final Class<INTERFACE> interfaceClass)
    {
        return (INTERFACE) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]
                {
                        interfaceClass
                }, FalseClass.DUMMY_INVOCATION_HANDLER);
    }

    /**
     * Invocation handler that returns dummy values.
     */
    private static class DummyInvocationHandler implements InvocationHandler
    {
        @Override
        public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable
        {
            Debug.println(DebugLevel.VERBOSE, "Implements invoke : ", method.getDeclaringClass()
                                                                            .getName());
            Debug.println(DebugLevel.VERBOSE, "Implements invoke : ", method.getReturnType(), " ", method.getName(),
                          "(", method.getParameterTypes(), ")", args);

            Class returnClass = method.getReturnType();

            if (returnClass.isPrimitive()
                    || Number.class.isAssignableFrom(returnClass)
                    || returnClass.isPrimitive()
                    || returnClass.equals(String.class)
                    || returnClass.isEnum())
            {
                return Reflector.newInstance(returnClass);
            }

            return null;
        }
    }
}