package jhelp.util.reflection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import jhelp.util.debug.Debug;
import jhelp.util.debug.DebugLevel;

public class FalseClass
{
   public static <INTERFACE> INTERFACE createFalseClass(final Class<INTERFACE> interfaceClass)
   {
      final InvocationHandler handler = new InvocationHandler()
      {

         @Override
         public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable
         {
            Debug.println(DebugLevel.VERBOSE, "Implements invoke : ", method.getDeclaringClass().getName());
            Debug.println(DebugLevel.VERBOSE, "Implements invoke : ", method.getReturnType(), " ", method.getName(), "(", method.getParameterTypes(), ")", args);
            return null;
         }
      };

      final INTERFACE interfaceInstance = (INTERFACE) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]
      {
         interfaceClass
      }, handler);

      return interfaceInstance;
   }
}