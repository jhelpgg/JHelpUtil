/**
 */
package jhelp.util.samples.common.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.WithFixedSize;

/**
 * Label with a {@link JHelpImage}<br>
 * <br>
 * 
 * @author JHelp
 */
public class SampleLabelJHelpImage
      extends JComponent
      implements WithFixedSize
{
   /** Selected color */
   private static final Color SELECTED = new Color(255, 128, 64, 32);

   /** Label height */
   private int                height;
   /** Image to draw */
   private JHelpImage         image;
   /** Selected state */
   private boolean            selected = false;
   /** Label width */
   private int                width;

   /**
    * Constructs LabelBufferedImage
    */
   public SampleLabelJHelpImage()
   {
      this(128, 128);
   }

   /**
    * Create a new instance of LabelBufferedImage with specified width and height
    * 
    * @param width
    *           Width
    * @param height
    *           Height
    */
   public SampleLabelJHelpImage(final int width, final int height)
   {
      this.width = width;
      this.height = height;

      final Dimension dimension = new Dimension(width, height);
      this.setSize(dimension);
      this.setPreferredSize(dimension);
      this.setMaximumSize(dimension);
      this.setMinimumSize(dimension);
   }

   /**
    * Create a new instance of LabelJHelpImage with image
    * 
    * @param image
    *           Image to draw
    */
   public SampleLabelJHelpImage(final JHelpImage image)
   {
      if(image == null)
      {
         throw new NullPointerException("The image couldn't be null");
      }

      this.image = image;

      this.width = image.getWidth();
      this.height = image.getHeight();

      final Dimension dimension = new Dimension(this.width, this.height);
      this.setSize(dimension);
      this.setPreferredSize(dimension);
      this.setMaximumSize(dimension);
      this.setMinimumSize(dimension);
   }

   /**
    * Paint the label
    * 
    * @param g
    *           Graphics environment
    * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
    */
   @Override
   protected void paintComponent(final Graphics g)
   {
      final int width = this.getWidth();
      final int height = this.getHeight();

      final int px = Math.max(1, Math.min(20, width / 10));
      final int py = Math.max(1, Math.min(20, height / 10));

      boolean blackX = true;
      boolean blackY = true;

      for(int y = height; y > 0; y -= py)
      {
         blackX = true;

         for(int x = width; x > 0; x -= px)
         {
            g.setColor(blackX == blackY
                  ? Color.BLACK
                  : Color.WHITE);
            g.fillRect(x - px, y - py, px, py);

            blackX = !blackX;
         }

         blackY = !blackY;
      }

      if(this.image != null)
      {
         g.drawImage(this.image.getImage(), 0, 0, this);
      }

      if(this.selected == true)
      {
         g.setColor(SampleLabelJHelpImage.SELECTED);
         g.fillRect(0, 0, width, height);
      }
   }

   /**
    * Give the label size <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return Label size
    * @see jhelp.util.gui.WithFixedSize#getFixedSize()
    */
   @Override
   public Dimension getFixedSize()
   {
      return new Dimension(this.width, this.height);
   }

   /**
    * Image to draw
    * 
    * @return Image to draw
    */
   public JHelpImage getJHelpImage()
   {
      return this.image;
   }

   /**
    * 
    Indicates if label is selected
    * 
    * @return {@code true} if label is selected
    */
   public boolean isSelected()
   {
      return this.selected;
   }

   /**
    * Refresh the label
    */
   public void refresh()
   {
      if(this.image != null)
      {
         this.image.update();
      }

      this.repaint();
   }

   /**
    * Remove current image
    */
   public void removeImage()
   {
      this.image = null;
      this.width = 128;
      this.height = 128;
      final Dimension dimension = new Dimension(128, 128);
      this.setSize(dimension);
      this.setPreferredSize(dimension);
      this.setMaximumSize(dimension);
      this.setMinimumSize(dimension);
      this.repaint();
   }

   /**
    * Remove current image, but keep label current size
    */
   public void removeImageWithoutChangeSize()
   {
      this.image = null;
      this.repaint();
   }

   /**
    * Change the image to draw
    * 
    * @param image
    *           New image to draw
    */
   public void setJHelpImage(final JHelpImage image)
   {
      if(image == null)
      {
         throw new NullPointerException("bufferedImage musn't be null");
      }

      this.image = image;
      this.width = image.getWidth();
      this.height = image.getHeight();

      final Dimension dimension = new Dimension(this.width, this.height);
      this.setSize(dimension);
      this.setPreferredSize(dimension);
      this.setMaximumSize(dimension);
      this.setMinimumSize(dimension);
      this.repaint();
   }

   /**
    * Change label selection state
    * 
    * @param selected
    *           New selection state
    */
   public void setSelected(final boolean selected)
   {
      this.selected = selected;

      this.refresh();
   }
}