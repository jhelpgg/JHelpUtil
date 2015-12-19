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
package jhelp.util.io.link;

/**
 * Represents <b>Internationalized Resource Identifier</b> <br>
 * 
 * @see <a href="http://www.ietf.org/rfc/rfc3987.txt">Internationalized Resource Identifier specification</a>
 * @author JHelp
 */
public class InternationalizedResourceIdentifier
      extends UniformResourceIdentifier
{
   /**
    * Create a new instance of InternationalizedResourceIdentifier
    * 
    * @param authority
    *           Authority, can be {@code null} if none
    * @param path
    *           Path
    */
   public InternationalizedResourceIdentifier(final Authority authority, final ReferencePath path)
   {
      super(authority, path);
   }

   /**
    * Create a new instance of InternationalizedResourceIdentifier
    * 
    * @param authority
    *           Authority, can be {@code null} if none
    * @param path
    *           Path
    * @param query
    *           Query, can be {@code null} if none
    */
   public InternationalizedResourceIdentifier(final Authority authority, final ReferencePath path, final String query)
   {
      super(authority, path, query);
   }

   /**
    * Create a new instance of InternationalizedResourceIdentifier
    * 
    * @param authority
    *           Authority, can be {@code null} if none
    * @param path
    *           Path
    * @param query
    *           Query, can be {@code null} if none
    * @param fragment
    *           Fragment, can be {@code null} if none
    */
   public InternationalizedResourceIdentifier(final Authority authority, final ReferencePath path, final String query, final String fragment)
   {
      super(authority, path, query, fragment);
   }

   /**
    * Create a new instance of InternationalizedResourceIdentifier
    * 
    * @param path
    *           Path
    */
   public InternationalizedResourceIdentifier(final ReferencePath path)
   {
      super(path);
   }

   /**
    * Create a new instance of InternationalizedResourceIdentifier
    * 
    * @param path
    *           Path
    * @param query
    *           Query, can be {@code null} if none
    */
   public InternationalizedResourceIdentifier(final ReferencePath path, final String query)
   {
      super(path, query);
   }

   /**
    * Create a new instance of InternationalizedResourceIdentifier
    * 
    * @param path
    *           Path
    * @param query
    *           Query, can be {@code null} if none
    * @param fragment
    *           Fragment, can be {@code null} if none
    */
   public InternationalizedResourceIdentifier(final ReferencePath path, final String query, final String fragment)
   {
      super(path, query, fragment);
   }

   /**
    * Create a new instance of InternationalizedResourceIdentifier by parsing a string
    * 
    * @param uriString
    *           String represents the Internationalized Resource Identifier
    */
   public InternationalizedResourceIdentifier(final String uriString)
   {
      super(uriString);
   }

   /**
    * Create a new instance of InternationalizedResourceIdentifier
    * 
    * @param scheme
    *           Scheme, can be {@code null} if none
    * @param authority
    *           Authority, can be {@code null} if none
    * @param path
    *           Path
    */
   public InternationalizedResourceIdentifier(final String scheme, final Authority authority, final ReferencePath path)
   {
      super(scheme, authority, path);
   }

   /**
    * Create a new instance of InternationalizedResourceIdentifier
    * 
    * @param scheme
    *           Scheme, can be {@code null} if none
    * @param authority
    *           Authority, can be {@code null} if none
    * @param path
    *           Path
    * @param query
    *           Query, can be {@code null} if none
    */
   public InternationalizedResourceIdentifier(final String scheme, final Authority authority, final ReferencePath path, final String query)
   {
      super(scheme, authority, path, query);
   }

   /**
    * Create a new instance of InternationalizedResourceIdentifier
    * 
    * @param scheme
    *           Scheme, can be {@code null} if none
    * @param authority
    *           Authority, can be {@code null} if none
    * @param path
    *           Path
    * @param query
    *           Query, can be {@code null} if none
    * @param fragment
    *           Fragment, can be {@code null} if none
    */
   public InternationalizedResourceIdentifier(final String scheme, final Authority authority, final ReferencePath path, final String query,
         final String fragment)
   {
      super(scheme, authority, path, query, fragment);
   }

   /**
    * Create a new instance of InternationalizedResourceIdentifier
    * 
    * @param scheme
    *           Scheme, can be {@code null} if none
    * @param path
    *           Path
    */
   public InternationalizedResourceIdentifier(final String scheme, final ReferencePath path)
   {
      super(scheme, path);
   }

   /**
    * Create a new instance of InternationalizedResourceIdentifier
    * 
    * @param scheme
    *           Scheme, can be {@code null} if none
    * @param path
    *           Path
    * @param query
    *           Query, can be {@code null} if none
    */
   public InternationalizedResourceIdentifier(final String scheme, final ReferencePath path, final String query)
   {
      super(scheme, path, query);
   }

   /**
    * Create a new instance of InternationalizedResourceIdentifier
    * 
    * @param scheme
    *           Scheme, can be {@code null} if none
    * @param path
    *           Path
    * @param query
    *           Query, can be {@code null} if none
    * @param fragment
    *           Fragment, can be {@code null} if none
    */
   public InternationalizedResourceIdentifier(final String scheme, final ReferencePath path, final String query, final String fragment)
   {
      super(scheme, path, query, fragment);
   }
}