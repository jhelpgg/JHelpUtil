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
package jhelp.util.gui;

import java.awt.Shape;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jhelp.util.math.UtilMath;

/**
 * Describe a path
 * 
 * @author JHelp
 */
public class Path
      implements Iterable<Segment>
{
   /** List of segments */
   private final List<Segment> segments;

   /**
    * Create a new instance of Path empty
    */
   public Path()
   {
      this.segments = new ArrayList<Segment>();
   }

   /**
    * Draw path in image
    * 
    * @param parent
    *           Image where draw (MUST be in draw mode)
    * @param element
    *           Image to repeat
    * @param alphaMix
    *           Indicates if mix alpha
    * @param percentStart
    *           Percent start
    * @param percentEnd
    *           Percent end
    */
   void drawPath(final JHelpImage parent, final JHelpImage element, final boolean alphaMix, final double percentStart, final double percentEnd)
   {
      final int size = this.segments.size();
      final int start = UtilMath.limit((int) (size * Math.min(percentStart, percentEnd)), 0, size);
      final int limit = UtilMath.limit((int) (size * Math.max(percentStart, percentEnd)), 0, size);
      final double xElement = element.getWidth() / 2d;
      final double yElement = element.getHeight() / 2d;
      Segment segment;

      for(int i = start; i < limit; i++)
      {
         segment = this.segments.get(i);
         parent.repeatOnLine((int) Math.round(segment.x1 - xElement), (int) Math.round(segment.y1 - yElement), (int) Math.round(segment.x2 - xElement),
               (int) Math.round(segment.y2 - yElement), element, alphaMix);
      }
   }

   /**
    * Append one segment
    * 
    * @param x1
    *           X start
    * @param y1
    *           Y start
    * @param x2
    *           X end
    * @param y2
    *           Y end
    */
   public void append(final double x1, final double y1, final double x2, final double y2)
   {
      this.segments.add(new Segment(x1, y1, x2, y2));
   }

   /**
    * Add an other path
    * 
    * @param path
    *           Path to add
    */
   public void append(final Path path)
   {
      this.segments.addAll(path.segments);
   }

   /**
    * Add a shape
    * 
    * @param shape
    *           Shape to add
    */
   public void append(final Shape shape)
   {
      final PathIterator pathIterator = shape.getPathIterator(ConstantsGUI.AFFINE_TRANSFORM, ConstantsGUI.FLATNESS);
      double xStart = 0, yStart = 0, x = 0, y = 0, xx = 0, yy = 0;
      final double[] elements = new double[6];
      int type;

      while(!pathIterator.isDone())
      {
         type = pathIterator.currentSegment(elements);

         switch(type)
         {
            case PathIterator.SEG_MOVETO:
               xStart = x = elements[0];
               yStart = y = elements[1];
            break;
            case PathIterator.SEG_LINETO:
               xx = elements[0];
               yy = elements[1];

               this.append(x, y, xx, yy);

               x = xx;
               y = yy;
            break;
            case PathIterator.SEG_CLOSE:
               this.append(x, y, xStart, yStart);

               x = xStart;
               y = yStart;
            break;
         }

         pathIterator.next();
      }
   }

   /**
    * Append a text
    * 
    * @param text
    *           Text to write
    * @param font
    *           Font to use
    * @param x
    *           X
    * @param y
    *           Y
    */
   public void appendText(final String text, final JHelpFont font, final int x, final int y)
   {
      this.append(font.computeShape(text, x, y));
   }

   /**
    * Obtain one segment
    * 
    * @param index
    *           Segment index
    * @return Segment
    */
   public Segment getSegment(final int index)
   {
      return this.segments.get(index);
   }

   /**
    * Iterator on segments <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return Iterator on segments
    * @see java.lang.Iterable#iterator()
    */
   @Override
   public Iterator<Segment> iterator()
   {
      return this.segments.iterator();
   }

   /**
    * Number of segments
    * 
    * @return Number of segments
    */
   public int numberOfSegment()
   {
      return this.segments.size();
   }
}