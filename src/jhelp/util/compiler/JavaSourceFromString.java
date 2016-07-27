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
package jhelp.util.compiler;

import java.net.URI;

import javax.tools.SimpleJavaFileObject;

/**
 * From Sun Javadoc of {@link javax.tools#JavaCompiler} : <br>
 * A file object used to represent source coming from a string. <br>
 * <br>
 * Last modification : 24 nov. 2009<br>
 * Version 0.0.0<br>
 */
public class JavaSourceFromString
      extends SimpleJavaFileObject
{
   /**
    * The source code of this "file".
    */
   final String code;

   /**
    * Constructs a new JavaSourceFromString.
    * 
    * @param name
    *           the name of the compilation unit represented by this file object
    * @param code
    *           the source code for the compilation unit represented by this file object
    */
   public JavaSourceFromString(final String name, final String code)
   {
      super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
      this.code = code;
   }

   /**
    * Source code
    * 
    * @param ignoreEncodingErrors
    *           Ignore here
    * @return Source code
    * @see javax.tools.SimpleJavaFileObject#getCharContent(boolean)
    */
   @Override
   public CharSequence getCharContent(final boolean ignoreEncodingErrors)
   {
      return this.code;
   }
}