package jhelp.util.io.link;

/**
 * Represents <b>Uniform Resource Identifier</b> <br>
 * 
 * @see <a href="https://www.ietf.org/rfc/rfc3986.txt">Uniform Resource Identifier specification</a>
 * @author JHelp
 */
public class UniformResourceIdentifier
{
   /** Authority */
   private final Authority     authority;
   /** Fragment */
   private final String        fragment;
   /** Path */
   private final ReferencePath path;
   /** Query */
   private final String        query;
   /** Scheme */
   private final String        scheme;

   /**
    * Create a new instance of UniformResourceIdentifier
    * 
    * @param authority
    *           Authority, can be {@code null} if none
    * @param path
    *           Path
    */
   public UniformResourceIdentifier(final Authority authority, final ReferencePath path)
   {
      this(null, authority, path, null, null);
   }

   /**
    * Create a new instance of UniformResourceIdentifier
    * 
    * @param authority
    *           Authority, can be {@code null} if none
    * @param path
    *           Path
    * @param query
    *           Query, can be {@code null} if none
    */
   public UniformResourceIdentifier(final Authority authority, final ReferencePath path, final String query)
   {
      this(null, authority, path, query, null);
   }

   /**
    * Create a new instance of UniformResourceIdentifier
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
   public UniformResourceIdentifier(final Authority authority, final ReferencePath path, final String query, final String fragment)
   {
      this(null, authority, path, query, fragment);
   }

   /**
    * Create a new instance of UniformResourceIdentifier
    * 
    * @param path
    *           Path
    */
   public UniformResourceIdentifier(final ReferencePath path)
   {
      this(null, null, path, null, null);
   }

   /**
    * Create a new instance of UniformResourceIdentifier
    * 
    * @param path
    *           Path
    * @param query
    *           Query, can be {@code null} if none
    */
   public UniformResourceIdentifier(final ReferencePath path, final String query)
   {
      this(null, null, path, query, null);
   }

   /**
    * Create a new instance of UniformResourceIdentifier
    * 
    * @param path
    *           Path
    * @param query
    *           Query, can be {@code null} if none
    * @param fragment
    *           Fragment, can be {@code null} if none
    */
   public UniformResourceIdentifier(final ReferencePath path, final String query, final String fragment)
   {
      this(null, null, path, query, fragment);
   }

   /**
    * Create a new instance of UniformResourceIdentifier by parsing a description string
    * 
    * @param uriString
    *           String represents a Uniform Resource Identifier
    */
   public UniformResourceIdentifier(String uriString)
   {
      int index = uriString.indexOf(':');

      if(index >= 0)
      {
         this.scheme = uriString.substring(0, index);
         uriString = uriString.substring(index + 1);
      }
      else
      {
         this.scheme = null;
      }

      if(uriString.startsWith("//") == true)
      {
         index = uriString.indexOf('/', 2);

         if(index < 0)
         {
            this.authority = new Authority(uriString.substring(2));
            uriString = "";
         }
         else
         {
            this.authority = new Authority(uriString.substring(2, index));
            uriString = uriString.substring(index + 1);
         }
      }
      else
      {
         this.authority = null;
      }

      index = uriString.indexOf('?');

      if(index >= 0)
      {
         this.path = ReferencePath.parseReferencePath(uriString.substring(0, index));
         uriString = uriString.substring(index + 1);

         index = uriString.indexOf('#');

         if(index >= 0)
         {
            this.query = uriString.substring(0, index);
            this.fragment = uriString.substring(index + 1);
         }
         else
         {
            this.query = uriString;
            this.fragment = null;
         }

         return;
      }
      else
      {
         this.query = null;
      }

      index = uriString.indexOf('#');

      if(index >= 0)
      {
         this.path = ReferencePath.parseReferencePath(uriString.substring(0, index));
         this.fragment = uriString.substring(index + 1);
         return;
      }

      this.fragment = null;
      this.path = ReferencePath.parseReferencePath(uriString);
   }

   /**
    * Create a new instance of UniformResourceIdentifier
    * 
    * @param scheme
    *           Scheme, can be {@code null} if none
    * @param authority
    *           Authority, can be {@code null} if none
    * @param path
    *           Path
    */
   public UniformResourceIdentifier(final String scheme, final Authority authority, final ReferencePath path)
   {
      this(scheme, authority, path, null, null);
   }

   /**
    * Create a new instance of UniformResourceIdentifier
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
   public UniformResourceIdentifier(final String scheme, final Authority authority, final ReferencePath path, final String query)
   {
      this(scheme, authority, path, query, null);
   }

   /**
    * Create a new instance of UniformResourceIdentifier
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
   public UniformResourceIdentifier(final String scheme, final Authority authority, final ReferencePath path, final String query, final String fragment)
   {
      if(path == null)
      {
         throw new NullPointerException("path musn't be null");
      }

      this.scheme = scheme;
      this.authority = authority;
      this.path = path;
      this.query = query;
      this.fragment = fragment;
   }

   /**
    * Create a new instance of UniformResourceIdentifier
    * 
    * @param scheme
    *           Scheme, can be {@code null} if none
    * @param path
    *           Path
    */
   public UniformResourceIdentifier(final String scheme, final ReferencePath path)
   {
      this(scheme, null, path, null, null);
   }

   /**
    * Create a new instance of UniformResourceIdentifier
    * 
    * @param scheme
    *           Scheme, can be {@code null} if none
    * @param path
    *           Path
    * @param query
    *           Query, can be {@code null} if none
    */
   public UniformResourceIdentifier(final String scheme, final ReferencePath path, final String query)
   {
      this(scheme, null, path, query, null);
   }

   /**
    * Create a new instance of UniformResourceIdentifier
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
   public UniformResourceIdentifier(final String scheme, final ReferencePath path, final String query, final String fragment)
   {
      this(scheme, null, path, query, fragment);
   }

   /**
    * Indicates if given object equals to this Uniform Resource Identifier <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param object
    *           Object to compare with
    * @return {@code true} if given object equals to this Uniform Resource Identifier
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals(final Object object)
   {
      if(object == this)
      {
         return true;
      }

      if(object == null)
      {
         return false;
      }

      if((object instanceof UniformResourceIdentifier) == false)
      {
         return false;
      }

      final UniformResourceIdentifier uri = (UniformResourceIdentifier) object;

      if(this.scheme == null)
      {
         if(uri.scheme != null)
         {
            return false;
         }
      }
      else if(this.scheme.equals(uri.scheme) == false)
      {
         return false;
      }

      if(this.authority == null)
      {
         if(uri.authority != null)
         {
            return false;
         }
      }
      else if(this.authority.equals(uri.authority) == false)
      {
         return false;
      }

      if(ReferencePath.areSamePath(this.path, uri.path) == false)
      {
         return false;
      }

      if(this.query == null)
      {
         if(uri.query != null)
         {
            return false;
         }
      }
      else if(this.query.equals(uri.query) == false)
      {
         return false;
      }

      if(this.fragment == null)
      {
         return uri.fragment == null;
      }

      return this.fragment.equals(uri.fragment) == true;
   }

   /**
    * Authority.<br>
    * {@code null} if none
    * 
    * @return Authority OR {@code null} if none
    */
   public Authority getAuthority()
   {
      return this.authority;
   }

   /**
    * Fragment.<br>
    * {@code null} if none
    * 
    * @return Fragment OR {@code null} if none
    */
   public String getFragment()
   {
      return this.fragment;
   }

   /**
    * Path
    * 
    * @return Path
    */
   public ReferencePath getPath()
   {
      return this.path;
   }

   /**
    * Query.<br>
    * {@code null} if none
    * 
    * @return Query OR {@code null} if none
    */
   public String getQuery()
   {
      return this.query;
   }

   /**
    * Scheme.<br>
    * {@code null} if none
    * 
    * @return Scheme OR {@code null} if none
    */
   public String getScheme()
   {
      return this.scheme;
   }

   /**
    * String representation.<br>
    * Can be used to be parse back with {@link #UniformResourceIdentifier(String)} <br>
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

      if(this.scheme != null)
      {
         stringBuilder.append(this.scheme);
         stringBuilder.append(':');
      }

      if(this.authority != null)
      {
         stringBuilder.append("//");
         stringBuilder.append(this.authority.toString());

         if(this.path.getPathName().length() > 0)
         {
            stringBuilder.append('/');
         }
      }

      stringBuilder.append(this.path.toString());

      if(this.query != null)
      {
         stringBuilder.append('?');
         stringBuilder.append(this.query);
      }

      if(this.fragment != null)
      {
         stringBuilder.append('#');
         stringBuilder.append(this.fragment);
      }

      return stringBuilder.toString();
   }
}