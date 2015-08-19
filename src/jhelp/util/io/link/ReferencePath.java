package jhelp.util.io.link;

import jhelp.util.text.StringCutter;

/**
 * Represents a path
 * 
 * @author JHelp
 */
public class ReferencePath
{
   /** Special path name to indicates current "directory" */
   public static final String ACTUAL_PATH   = ".";
   /** Special path to say previous path (If their one) */
   public static final String PREVIOUS_PATH = "..";

   /**
    * Indicates if 2 paths goes to same place.<br>
    * {@link #ACTUAL_PATH} and {@link #PREVIOUS_PATH} are resolved to do this
    * 
    * @param referencePath1
    *           First path
    * @param referencePath2
    *           Second path
    * @return {@code true} if given paths goes to same location
    */
   public static boolean areSamePath(final ReferencePath referencePath1, final ReferencePath referencePath2)
   {
      return ReferencePath.simplify(referencePath1).equals(ReferencePath.simplify(referencePath2)) == true;
   }

   /**
    * Parse string to path with default separator character /
    * 
    * @param string
    *           String to parse
    * @return Path result
    */
   public static ReferencePath parseReferencePath(final String string)
   {
      return ReferencePath.parseReferencePath(string, '/');
   }

   /**
    * Parse string to reference path, using the given character as path separator
    * 
    * @param string
    *           String to parse
    * @param pathSeparator
    *           Path separator character
    * @return Parsed path
    */
   public static ReferencePath parseReferencePath(final String string, final char pathSeparator)
   {
      final StringCutter stringCutter = new StringCutter(string, pathSeparator);
      String pathName = stringCutter.next();

      if(pathName == null)
      {
         return null;
      }

      final ReferencePath referencePath = new ReferencePath(pathName);
      ReferencePath currentPath = referencePath;
      pathName = stringCutter.next();

      while(pathName != null)
      {
         currentPath.setChild(new ReferencePath(pathName));
         currentPath = currentPath.getChild();
         pathName = stringCutter.next();
      }

      return referencePath;
   }

   /**
    * Simplify (Resolve {@link #ACTUAL_PATH} AND {@link #PREVIOUS_PATH}) given path
    * 
    * @param referencePath
    *           Path to simplify
    * @return Simplified path
    */
   public static ReferencePath simplify(ReferencePath referencePath)
   {
      final boolean hasSomething = referencePath != null;
      ReferencePath simpilifiedPath = null;
      String pathName;

      while(referencePath != null)
      {
         pathName = referencePath.getPathName();

         if(ReferencePath.PREVIOUS_PATH.equals(pathName) == true)
         {
            if(simpilifiedPath != null)
            {
               simpilifiedPath = simpilifiedPath.getParent();

               if(simpilifiedPath != null)
               {
                  simpilifiedPath.removeChild();
               }
            }
         }
         else if(ReferencePath.ACTUAL_PATH.equals(pathName) == false)
         {
            if(simpilifiedPath == null)
            {
               simpilifiedPath = new ReferencePath(pathName);
            }
            else
            {
               simpilifiedPath.setChild(new ReferencePath(pathName));
               simpilifiedPath = simpilifiedPath.getChild();
            }
         }

         referencePath = referencePath.getChild();
      }

      if((hasSomething == true) && (simpilifiedPath == null))
      {
         return simpilifiedPath = new ReferencePath("");
      }

      return simpilifiedPath.getRoot();
   }

   /** Path child/next */
   private ReferencePath child;
   /** Path parent/previous */
   private ReferencePath parent;
   /** Path name */
   private final String  pathName;

   /**
    * Create a new instance of ReferencePath
    * 
    * @param pathName
    *           Path name
    */
   public ReferencePath(final String pathName)
   {
      if(pathName == null)
      {
         throw new NullPointerException("pathName musn't be null");
      }

      this.pathName = pathName;
   }

   /**
    * Indicates if given path have exact same descendants
    * 
    * @param referencePath
    *           Path to compare with
    * @return {@code true} if given path have exact same descendants
    */
   private boolean asSameChildAs(final ReferencePath referencePath)
   {
      if(this.child == null)
      {
         return referencePath.child == null;
      }

      if(referencePath.child == null)
      {
         return false;
      }

      if(this.child.pathName.equals(referencePath.child.pathName) == false)
      {
         return false;
      }

      return this.child.asSameChildAs(referencePath.child);
   }

   /**
    * Indicates if given path have exact same ancestors
    * 
    * @param referencePath
    *           Path to compare with
    * @return {@code true} if given path have exact same ancestors
    */
   private boolean asSameParentAs(final ReferencePath referencePath)
   {
      if(this.parent == null)
      {
         return referencePath.parent == null;
      }

      if(referencePath.parent == null)
      {
         return false;
      }

      if(this.parent.pathName.equals(referencePath.parent.pathName) == false)
      {
         return false;
      }

      return this.parent.asSameParentAs(referencePath.parent);
   }

   /**
    * Indicates if given path is same as this path (Same ancestors and descendants)
    * 
    * @param referencePath
    *           Path to compare with
    * @return {@code true} if given path is same as this path (Same ancestors and descendants)
    */
   private boolean isSamePathAs(final ReferencePath referencePath)
   {
      if(this.pathName.equals(referencePath.pathName) == false)
      {
         return false;
      }

      return (this.asSameParentAs(referencePath) == true) && this.asSameChildAs(referencePath);
   }

   /**
    * Indicates if given object is equals to this path <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param object
    *           Object to compare with
    * @return {@code true} if given object is equals to this path
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

      if((object instanceof ReferencePath) == false)
      {
         return false;
      }

      return this.isSamePathAs((ReferencePath) object);
   }

   /**
    * Path child/next.<br>
    * {@code null} if none
    * 
    * @return Path child/next OR {@code null} if none
    */
   public ReferencePath getChild()
   {
      return this.child;
   }

   /**
    * Path parent/previous.<br>
    * {@code null} if none
    * 
    * @return Path parent/previous OR {@code null} if none
    */
   public ReferencePath getParent()
   {
      return this.parent;
   }

   /**
    * Path name
    * 
    * @return Path name
    */
   public String getPathName()
   {
      return this.pathName;
   }

   /**
    * Obtain the root of the path.<br>
    * That is to say the ancestor with no ancestor
    * 
    * @return Path root
    */
   public ReferencePath getRoot()
   {
      ReferencePath root = this;

      while(root.parent != null)
      {
         root = root.parent;
      }

      return root;
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
      int hashCode = this.pathName.hashCode();
      ReferencePath referencePath = this.parent;

      while(referencePath != null)
      {
         hashCode *= 31;
         hashCode += referencePath.pathName.hashCode();
         referencePath = referencePath.parent;
      }

      referencePath = this.child;

      while(referencePath != null)
      {
         hashCode *= 31;
         hashCode += referencePath.pathName.hashCode();
         referencePath = referencePath.child;
      }

      return hashCode;
   }

   /**
    * Remove the child/next
    */
   public void removeChild()
   {
      if(this.child != null)
      {
         this.child.parent = null;
         this.child = null;
      }
   }

   /**
    * Define the child/next.<br>
    * If path already have a child, the actual child is removed and given child will replace it.<br>
    * If given child already have a parent it detach form his parent to be attach to this one<br>
    * Beware of loop reference !
    * 
    * @param referencePath
    *           Path to put has child/next
    * @throws IllegalArgumentException
    *            if add reference will do a loop reference loop reference
    */
   public void setChild(final ReferencePath referencePath)
   {
      // Detect loop reference
      ReferencePath thisAncestor = this;
      ReferencePath givenDescendant = referencePath;

      while(thisAncestor != null)
      {
         givenDescendant = referencePath;

         while(givenDescendant != null)
         {
            if(thisAncestor == givenDescendant)
            {
               throw new IllegalArgumentException("Reference loop are forbidden");
            }

            givenDescendant = givenDescendant.child;
         }

         thisAncestor = thisAncestor.parent;
      }

      // Remove given from current parent
      if(referencePath.parent != null)
      {
         referencePath.parent.child = null;
      }

      // Detach this current child
      if(this.child != null)
      {
         this.child.parent = null;
      }

      // Do correct links
      referencePath.parent = this;
      this.child = referencePath;
   }

   /**
    * String representation of path and use / as path separator.<br>
    * Given string can be parse back with {@link #parseReferencePath(String)} <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return String representation of path
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString()
   {
      return this.toString('/');
   }

   /**
    * String representation of path and use given character as path separator.<br>
    * Given string can be parse back with {@link #parseReferencePath(String, char)}
    * 
    * @param pathSeparator
    *           Path separator
    * @return String representation of path
    */
   public String toString(final char pathSeparator)
   {
      final StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(this.pathName);
      ReferencePath referencePath = this.child;

      while(referencePath != null)
      {
         stringBuilder.append(pathSeparator);
         stringBuilder.append(referencePath.pathName);
         referencePath = referencePath.child;
      }

      return stringBuilder.toString();
   }
}