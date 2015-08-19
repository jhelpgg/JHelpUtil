package jhelp.util.gui;

/**
 * Pixel combination
 * 
 * @author JHelp
 */
public enum PixelCombination
{
   /** AND pixel algorithm : It apply AND with Red, Green and Blue of drawing image (over) and image where draw (source) */
   AND()
   {
      /**
       * AND pixel algorithm : It apply AND with Red, Green and Blue of drawing image (over) and image where draw (source) <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       * 
       * @param source
       *           Source pixel
       * @param over
       *           Over over pixel
       * @return Combination result
       * @see jhelp.util.gui.PixelCombination#combine(int, int)
       */
      @Override
      public int combine(final int source, final int over)
      {
         return (source & 0xFF000000) | ((source & 0x00FFFFFF) & (over & 0x00FFFFFF));
      }
   },
   /**
    * AND pixel algorithm with alpha : It apply AND with Alpha, Red, Green and Blue of drawing image (over) and image where draw
    * (source)
    */
   AND_ALPHA()
   {
      /**
       * AND pixel algorithm with alpha : It apply AND with Alpha, Red, Green and Blue of drawing image (over) and image where
       * draw (source) <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       * 
       * @param source
       *           Source pixel
       * @param over
       *           Over pixel
       * @return Combination result
       * @see jhelp.util.gui.PixelCombination#combine(int, int)
       */
      @Override
      public int combine(final int source, final int over)
      {
         return source & over;
      }
   },
   /** OR pixel algorithm : It apply OR with Red, Green and Blue of drawing image (over) and image where draw (source) */
   OR()
   {
      /**
       * OR pixel algorithm : It apply OR with Red, Green and Blue of drawing image (over) and image where draw (source) <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       * 
       * @param source
       *           Source pixel
       * @param over
       *           Over over pixel
       * @return Combination result
       * @see jhelp.util.gui.PixelCombination#combine(int, int)
       */
      @Override
      public int combine(final int source, final int over)
      {
         return (source & 0xFF000000) | ((source & 0x00FFFFFF) | (over & 0x00FFFFFF));
      }
   },
   /**
    * OR pixel algorithm with alpha : It apply OR with Alpha, Red, Green and Blue of drawing image (over) and image where draw
    * (source)
    */
   OR_ALPHA()
   {
      /**
       * OR pixel algorithm with alpha : It apply OR with Alpha, Red, Green and Blue of drawing image (over) and image where
       * draw (source) <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       * 
       * @param source
       *           Source pixel
       * @param over
       *           Over pixel
       * @return Combination result
       * @see jhelp.util.gui.PixelCombination#combine(int, int)
       */
      @Override
      public int combine(final int source, final int over)
      {
         return source | over;
      }
   },
   /**
    * XOR pixel algorithm with alpha : It apply XOR with Alpha, Red, Green and Blue of drawing image (over) and image where draw
    * (source)
    */
   XOR()
   {
      /**
       * XOR pixel algorithm : It apply XOR with Red, Green and Blue of drawing image (over) and image where draw (source) <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       * 
       * @param source
       *           Source pixel
       * @param over
       *           Over over pixel
       * @return Combination result
       * @see jhelp.util.gui.PixelCombination#combine(int, int)
       */
      @Override
      public int combine(final int source, final int over)
      {
         return (source & 0xFF000000) | ((source & 0x00FFFFFF) ^ (over & 0x00FFFFFF));
      }
   },
   /**
    * XOR pixel algorithm with alpha : It apply XOR with Alpha, Red, Green and Blue of drawing image (over) and image where draw
    * (source)
    */
   XOR_ALPHA()
   {
      /**
       * XOR pixel algorithm with alpha : It apply XOR with Alpha, Red, Green and Blue of drawing image (over) and image where
       * draw (source) <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       * 
       * @param source
       *           Source pixel
       * @param over
       *           Over pixel
       * @return Combination result
       * @see jhelp.util.gui.PixelCombination#combine(int, int)
       */
      @Override
      public int combine(final int source, final int over)
      {
         return source ^ over;
      }
   };

   /**
    * Combine image where draw (source) and image to draw (over)
    * 
    * @param source
    *           Source pixel
    * @param over
    *           Over pixel
    * @return Combination result
    */
   public int combine(final int source, final int over)
   {
      return source;
   }
}