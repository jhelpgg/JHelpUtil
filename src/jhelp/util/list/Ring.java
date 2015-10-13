package jhelp.util.list;

/**
 * Represents a ring of elements
 * 
 * @author JHelp
 * @param <TYPE>
 *           Elements type
 */
public class Ring<TYPE>
{
   /**
    * Element of the ring
    * 
    * @author JHelp
    * @param <ELEMENT>
    *           Element type
    */
   static class Element<ELEMENT>
   {
      /** Carry element */
      public ELEMENT          element;
      /** Next ring element */
      public Element<ELEMENT> next;
      /** Previous ring element */
      public Element<ELEMENT> previous;

      /**
       * Create a new instance of Element
       * 
       * @param element
       *           Element to carry
       */
      public Element(final ELEMENT element)
      {
         this.element = element;
      }
   }

   /** Current element of the ring */
   private Element<TYPE> current;
   /** Ring size */
   private int           size;

   /**
    * Create a new empty Ring
    */
   public Ring()
   {
      this.size = 0;
   }

   /**
    * Add element to ring
    * 
    * @param element
    *           Element to add
    */
   public void add(final TYPE element)
   {
      if(element == null)
      {
         throw new NullPointerException("element musn't be null");
      }

      if(this.current == null)
      {
         this.current = new Element<TYPE>(element);
         this.current.next = this.current;
         this.current.previous = this.current;
         this.size = 1;

         return;
      }

      final Element<TYPE> next = this.current.next;

      final Element<TYPE> elt = new Element<TYPE>(element);
      elt.previous = this.current;
      elt.next = next;

      this.current.next = elt;

      next.previous = elt;
      this.size++;
   }

   /**
    * Clear the ring
    */
   public void clear()
   {
      while(this.current != null)
      {
         this.remove();
      }

      this.size = 0;
   }

   /**
    * Indicates if given element inside the ring
    * 
    * @param element
    *           Element searched
    * @return {@code true} if given element inside the ring
    */
   public boolean contains(final TYPE element)
   {
      if((element == null) || (this.current == null))
      {
         return false;
      }

      if(this.current.element.equals(element) == true)
      {
         return true;
      }

      Element<TYPE> elementLocal = this.current.next;

      while(elementLocal != this.current)
      {
         if(elementLocal.element.equals(element) == true)
         {
            return true;
         }

         elementLocal = elementLocal.next;
      }

      return false;
   }

   /**
    * Current element or {@code null} if ring is empty
    * 
    * @return Current element or {@code null} if ring is empty
    */
   public TYPE get()
   {
      if(this.current == null)
      {
         return null;
      }

      return this.current.element;
   }

   /**
    * Number of elements inside the ring
    * 
    * @return Number of elements inside the ring
    */
   public int getSize()
   {
      return this.size;
   }

   /**
    * Indicates if ring is empty
    * 
    * @return {@code true} if ring is empty
    */
   public boolean isEmpty()
   {
      return this.current == null;
   }

   /**
    * Pass to next element
    */
   public void next()
   {
      if(this.current != null)
      {
         this.current = this.current.next;
      }
   }

   /**
    * Pass to previous element
    */
   public void previous()
   {
      if(this.current != null)
      {
         this.current = this.current.previous;
      }
   }

   /**
    * Try to reach ring element that equals to given element.<br>
    * If succeed the stored instance equals to given is returned and the ring is on its position
    * 
    * @param element
    *           Element search
    * @return The element stored equals to given element and ring position on it OR {@code null} if not found and ring position
    *         not changed
    */
   public TYPE reach(final TYPE element)
   {
      if(element == null)
      {
         throw new NullPointerException("element musn't be null");
      }

      if(this.current == null)
      {
         return null;
      }

      if(this.current.element.equals(element) == true)
      {
         return this.current.element;
      }

      Element<TYPE> elementLocal = this.current.next;

      while(elementLocal != this.current)
      {
         if(elementLocal.element.equals(element) == true)
         {
            this.current = elementLocal;
            return this.current.element;
         }

         elementLocal = elementLocal.next;
      }

      return null;
   }

   /**
    * Remove current element of the ring
    */
   public void remove()
   {
      if(this.current == null)
      {
         this.size = 0;
         return;
      }

      if((this.current.next == this.current) && (this.current.previous == this.current))
      {
         this.size = 0;
         this.current = null;

         return;
      }

      this.current.previous.next = this.current.next;
      this.current.next.previous = this.current.previous;

      this.current = this.current.next;
      this.size--;
   }

   /**
    * Ring string representation <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return Ring string representation
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString()
   {
      final StringBuilder stringBuilder = new StringBuilder("Ring[");

      if(this.current != null)
      {
         stringBuilder.append(this.current.element.toString());

         Element<TYPE> element = this.current.next;
         while(element != this.current)
         {
            stringBuilder.append(", ");
            stringBuilder.append(element.element.toString());

            element = element.next;
         }
      }

      stringBuilder.append(']');

      return stringBuilder.toString();
   }
}