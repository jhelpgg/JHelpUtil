package jhelp.util.samples.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.geom.Ellipse2D;

import javax.swing.JFrame;

import jhelp.util.debug.Debug;
import jhelp.util.debug.DebugLevel;
import jhelp.util.gui.ConstantsGUI;
import jhelp.util.gui.JHelpGradient;
import jhelp.util.gui.JHelpImage;
import jhelp.util.list.Queue;
import jhelp.util.samples.common.gui.SampleLabelJHelpImage;
import jhelp.util.time.LapsTime;
import jhelp.util.time.TimeDebug;

public class SpeedTest
{
   public static void main(final String[] args)
   {
      final JHelpGradient gradient = new JHelpGradient(0xFFFFFFFF, 0XFFFF0000, 0xFF00FF00, 0xFF0000FF);

      final TimeDebug timeDebug = new TimeDebug("Speed test");

      {
         final JHelpImage image = new JHelpImage(1024, 1024);
         final JHelpImage mask = new JHelpImage(1024, 1024);
         mask.startDrawMode();
         mask.drawEllipse(0, 0, 1024, 1024, 0xFFFFFFFF, false);
         mask.fillColor(512, 512, 0xFFFFFFFF, 0, false);
         mask.endDrawMode();
         image.startDrawMode();
         image.paintAlphaMask(0, 0, mask, gradient);
         image.endDrawMode();
      }
      timeDebug.add("Mask create by fill color");

      {
         final JHelpImage image = new JHelpImage(1024, 1024);
         final JHelpImage mask = new JHelpImage(1024, 1024);
         mask.startDrawMode();
         mask.fillEllipse(0, 0, 1024, 1024, 0xFFFFFFFF, false);
         mask.endDrawMode();
         image.startDrawMode();
         image.paintAlphaMask(0, 0, mask, gradient);
         image.endDrawMode();
      }
      timeDebug.add("Mask create by directly fill");

      {
         final JHelpImage image = new JHelpImage(1024, 1024);
         image.startDrawMode();
         image.fillEllipse(0, 0, 1024, 1024, gradient, true);
         image.endDrawMode();
      }
      timeDebug.add("Normal way");

      timeDebug.dump();

      final Font font = new Font("Arial", Font.BOLD, 123);
      final GlyphVector glyphVector = font.createGlyphVector(ConstantsGUI.FONT_RENDER_CONTEXT, "Salut");
      glyphVector.getOutline(10, 10);

      final int sides = 5;
      final int[] xs = new int[sides];
      final int[] ys = new int[sides];
      final int cx = 512;
      final int cy = 512;
      double angle = 0;
      final double step = (2d * Math.PI) / sides;
      final double ray = 510;

      for(int index = 0; index < sides; index++)
      {
         xs[index] = (int) (cx + (ray * Math.cos(angle)));
         ys[index] = (int) (cy + (ray * Math.sin(angle)));
         angle += 2 * step;
      }

      final Shape shape = //
      // new Polygon(new int[]
      // {
      // 0, 1024, 0, 1024
      // }, new int[]
      // {
      // 0, 0, 1024, 1024
      // }, 4);
      // new Rectangle(0, 0, 1023, 1023);
      // new RoundRectangle2D.Double(0, 0, 1023, 1023,128, 128);
      // new RoundRectangle2D.Double(0, 0, 1024, 1024, 128, 128);
      // new Ellipse2D.Double(0, 0, 1023, 1023);
      new Ellipse2D.Double(0, 0, 1024, 1024);
      // new Ellipse2D.Double(10, 20, 500, 750);
      // new QuadCurve2D.Double(0, 0, 128, 512, 1024, 1024);
      // new CubicCurve2D.Double(0, 0, 128, 512, 750, 16, 1024, 1024);
      // glyphVector.getOutline(10, 100);
      // new Polygon(xs, ys, sides);

      final JHelpImage image = new JHelpImage(1024, 1024);
      final JFrame frame = new JFrame();
      final SampleLabelJHelpImage labelJHelpImage = new SampleLabelJHelpImage(image);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setLayout(new BorderLayout());
      frame.add(labelJHelpImage, BorderLayout.CENTER);
      frame.pack();
      frame.setVisible(true);
      final int p = 1;

      LapsTime.startMeasure();
      Rectangle rectangle = shape.getBounds();
      final Queue<Rectangle> stack = new Queue<Rectangle>();
      stack.inQueue(rectangle);
      int mx, my, w, h, w2, h2;
      image.startDrawMode();

      while(stack.isEmpty() == false)
      {
         rectangle = stack.outQueue();

         if(shape.contains(rectangle) == true)
         {
            image.fillRectangle(rectangle.x, rectangle.y, rectangle.width, rectangle.height,//
                  0xFFFF0000);
            // 0xFF000000 | JHelpRandom.random(0xFFFFFF));
            // gradient);
         }
         else
         {
            w = rectangle.width >> 1;
            h = rectangle.height >> 1;
            w2 = rectangle.width - w;
            h2 = rectangle.height - h;

            if((w >= p) && (h >= p))
            {
               mx = rectangle.x + w;
               my = rectangle.y + h;

               if(shape.intersects(rectangle.x, rectangle.y, w, h) == true)
               {
                  stack.inQueue(new Rectangle(rectangle.x, rectangle.y, w, h));
               }

               if(shape.intersects(mx, rectangle.y, w2, h) == true)
               {
                  stack.inQueue(new Rectangle(mx, rectangle.y, w2, h));
               }

               if(shape.intersects(rectangle.x, my, w, h2) == true)
               {
                  stack.inQueue(new Rectangle(rectangle.x, my, w, h2));
               }

               if(shape.intersects(mx, my, w2, h2) == true)
               {
                  stack.inQueue(new Rectangle(mx, my, w2, h2));
               }
            }
         }
      }

      // image.fillString(0, 200, "Salut", new JHelpFont(font, false), 0xFFFF0000);

      image.endDrawMode();

      Debug.printMark(DebugLevel.INFORMATION, LapsTime.endMeasure().shortString());
   }
}