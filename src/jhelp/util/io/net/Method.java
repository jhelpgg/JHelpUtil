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