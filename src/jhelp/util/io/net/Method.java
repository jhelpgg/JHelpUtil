/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 * 
 * @author JHelp
 */
package jhelp.util.io.net;

/**
 * HTTP connection method
 * 
 * @author JHelp
 */
public enum Method
{
   /** Delete connection type */
   DELETE("DELETE"),
   /** Get connection type */
   GET("GET"),
   /** Head connection type */
   HEAD("HEAD"),
   /** Options connection type */
   OPTIONS("OPTIONS"),
   /** Post connection type */
   POST("POST"),
   /** Put connection type */
   PUT("PUT"),
   /** Trace connection type */
   TRACE("TRACE");
   /** Method name */
   private final String method;

   /**
    * Create a new instance of Method
    * 
    * @param method
    *           Method name
    */
   Method(final String method)
   {
      this.method = method;
   }

   /**
    * Method name
    * 
    * @return Method name
    */
   public String getMethod()
   {
      return this.method;
   }
}