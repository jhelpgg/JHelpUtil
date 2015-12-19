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

import jhelp.util.HashCode;

/**
 * Represents an authority in reference to distant source
 * 
 * @author JHelp
 */
public class Authority
{
   /** Port value when not defined */
   public static final int PORT_UNDEFINED = -1;
   /** Distant host */
   private final String    host;
   /** Port */
   private final int       port;
   /** User information */
   private final String    userInfo;

   /**
    * Create a new instance of Authority on parsing given string
    * 
    * @param authorityString
    *           String to parse
    */
   public Authority(String authorityString)
   {
      int index = authorityString.indexOf('@');

      if(index >= 0)
      {
         this.userInfo = authorityString.substring(0, index);
         authorityString = authorityString.substring(index + 1);
      }
      else
      {
         this.userInfo = null;
      }

      index = authorityString.indexOf(':');

      if(index >= 0)
      {
         this.host = authorityString.substring(0, index);
         this.port = Integer.parseInt(authorityString.substring(index + 1));
      }
      else
      {
         this.host = authorityString;
         this.port = Authority.PORT_UNDEFINED;
      }
   }

   /**
    * Create a new instance of Authority
    * 
    * @param host
    *           Host
    * @param port
    *           Port OR {@link #PORT_UNDEFINED}
    */
   public Authority(final String host, final int port)
   {
      this(null, host, port);
   }

   /**
    * Create a new instance of Authority
    * 
    * @param userInfo
    *           User information, can be {@code null} if no user information
    * @param host
    *           Host
    */
   public Authority(final String userInfo, final String host)
   {
      this(userInfo, host, Authority.PORT_UNDEFINED);
   }

   /**
    * Create a new instance of Authority
    * 
    * @param userInfo
    *           User information, can be {@code null} if no user information
    * @param host
    *           Host
    * @param port
    *           Port OR {@link #PORT_UNDEFINED}
    */
   public Authority(final String userInfo, final String host, final int port)
   {
      if(host == null)
      {
         throw new NullPointerException("host musn't be null");
      }

      if((port != Authority.PORT_UNDEFINED) && (port < 0))
      {
         throw new IllegalArgumentException("port must be >=0 OR PORT_UNDEFINED not " + port);
      }

      if((userInfo != null) && ((userInfo.indexOf('@') >= 0) || (userInfo.indexOf(':') >= 0) || (userInfo.indexOf('/') >= 0)))
      {
         throw new IllegalArgumentException("@ : AND / are reserved characters and can't be used in user information ->" + userInfo);
      }

      if((host.indexOf('@') >= 0) || (host.indexOf(':') >= 0) || (host.indexOf('/') >= 0))
      {
         throw new IllegalArgumentException("@ : AND / are reserved characters and can't be used in host ->" + host);
      }

      this.userInfo = userInfo;
      this.host = host;
      this.port = port;
   }

   /**
    * Indicates if given object equals to this authority <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param object
    *           Object to test
    * @return {@code true} if given object equals to this authority
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

      if((object instanceof Authority) == false)
      {
         return false;
      }

      final Authority authority = (Authority) object;

      if(this.userInfo == null)
      {
         if(authority.userInfo != null)
         {
            return false;
         }
      }

      if(authority.userInfo == null)
      {
         if(this.userInfo != null)
         {
            return false;
         }
      }

      if((this.userInfo != null) && (this.userInfo.equals(authority.userInfo) == false))
      {
         return false;
      }

      return (this.host.equals(authority.host) == true) && (this.port == authority.port);
   }

   /**
    * Host
    * 
    * @return Host
    */
   public String getHost()
   {
      return this.host;
   }

   /**
    * Port or {@link #PORT_UNDEFINED}
    * 
    * @return Port or {@link #PORT_UNDEFINED}
    */
   public int getPort()
   {
      return this.port;
   }

   /**
    * User information.<br>
    * {@code null} if no user information
    * 
    * @return User information OR {@code null} if no user information
    */
   public String getUserInfo()
   {
      return this.userInfo;
   }

   /**
    * Hash code <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return Hash code
    * @see java.lang.Object#hashCode()
    */
   @Override
   public int hashCode()
   {
      return HashCode.computeHashCode(this.userInfo, this.host, this.port);
   }

   /**
    * String representation.<br>
    * Can be used with {@link #Authority(String)} later to retrieve the authority <br>
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

      if(this.userInfo != null)
      {
         stringBuilder.append(this.userInfo);
         stringBuilder.append('@');
      }

      stringBuilder.append(this.host);

      if(this.port >= 0)
      {
         stringBuilder.append(':');
         stringBuilder.append(this.port);
      }

      return stringBuilder.toString();
   }
}