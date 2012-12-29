package jhelp.util.gui;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.Ellipse2D;
import java.awt.geom.PathIterator;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Stack;
import java.util.concurrent.locks.ReentrantLock;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import jhelp.util.debug.Debug;
import jhelp.util.gui.JHelpAnimatedImage.AnimationMode;
import jhelp.util.list.Pair;
import jhelp.util.list.SortedArray;
import jhelp.util.math.UtilMath;

/**
 * Represents an image.<br>
 * You can draw on image only this image if it is on draw mode, see {@link #startDrawMode()}, {@link #endDrawMode()} and
 * {@link #isDrawMode()}.<br>
 * You can also create {@link JHelpSprite}, that are small image that can be easy animated.<br>
 * The image is refresh at screen , only if exit of draw mode {@link #endDrawMode()} or call {@link #update()}
 * 
 * @author JHelp
 */
public class JHelpImage
      implements ConstantsGUI
{
   /** Palette to use */
   private static final int[] PALETTE      =
                                           {
         0xFFFFFFFF, 0xFFFFFFC0, 0xFFFFFF80, 0xFFFFFF40, 0xFFFFFF00,//
         0xFFFFC0FF, 0xFFFFC0C0, 0xFFFC0F80, 0xFFFFC040, 0xFFFFC000, //
         0xFFFF80FF, 0xFFFF80C0, 0xFFFF8080, 0xFFFF8040, 0xFFFF8000, //
         0xFFFF40FF, 0xFFFF40C0, 0xFFFF4080, 0xFFFF4040, 0xFFFF4000, //
         0xFFFF00FF, 0xFFFF00C0, 0xFFFF0080, 0xFFFF0040, 0xFFFF0000, //
         //
         0xFFC0FFFF, 0xFFC0FFC0, 0xFFC0FF80, 0xFFC0FF40, 0xFFC0FF00,//
         0xFFC0C0FF, 0xFFC0C0C0, 0xFFFC0F80, 0xFFC0C040, 0xFFC0C000, //
         0xFFC080FF, 0xFFC080C0, 0xFFC08080, 0xFFC08040, 0xFFC08000, //
         0xFFC040FF, 0xFFC040C0, 0xFFC04080, 0xFFC04040, 0xFFC04000, //
         0xFFC000FF, 0xFFC000C0, 0xFFC00080, 0xFFC00040, 0xFFC00000, //
         //
         0xFF80FFFF, 0xFF80FFC0, 0xFF80FF80, 0xFF80FF40, 0xFF80FF00,//
         0xFF80C0FF, 0xFF80C0C0, 0xFFFC0F80, 0xFF80C040, 0xFF80C000, //
         0xFF8080FF, 0xFF8080C0, 0xFF808080, 0xFF808040, 0xFF808000, //
         0xFF8040FF, 0xFF8040C0, 0xFF804080, 0xFF804040, 0xFF804000, //
         0xFF8000FF, 0xFF8000C0, 0xFF800080, 0xFF800040, 0xFF800000, //
         //
         0xFF40FFFF, 0xFF40FFC0, 0xFF40FF80, 0xFF40FF40, 0xFF40FF00,//
         0xFF40C0FF, 0xFF40C0C0, 0xFFFC0F80, 0xFF40C040, 0xFF40C000, //
         0xFF4080FF, 0xFF4080C0, 0xFF408080, 0xFF408040, 0xFF408000, //
         0xFF4040FF, 0xFF4040C0, 0xFF404080, 0xFF404040, 0xFF404000, //
         0xFF4000FF, 0xFF4000C0, 0xFF400080, 0xFF400040, 0xFF400000, //
         //
         0xFF00FFFF, 0xFF00FFC0, 0xFF00FF80, 0xFF00FF40, 0xFF00FF00,//
         0xFF00C0FF, 0xFF00C0C0, 0xFFFC0F80, 0xFF00C040, 0xFF00C000, //
         0xFF0080FF, 0xFF0080C0, 0xFF008080, 0xFF008040, 0xFF008000, //
         0xFF0040FF, 0xFF0040C0, 0xFF004080, 0xFF004040, 0xFF004000, //
         0xFF0000FF, 0xFF0000C0, 0xFF000080, 0xFF000040, 0xFF000000
                                           };
   /** Pallete size */
   public static final int    PALETTE_SIZE = JHelpImage.PALETTE.length;

   /**
    * Compute blue part of color from YUV<br>
    * B = Y + 1.7790 * (U - 128)
    * 
    * @param y
    *           Y
    * @param u
    *           U
    * @param v
    *           V
    * @return Blue part
    */
   private static int computeBlue(final double y, final double u, final double v)
   {
      return UtilMath.limit0_255((int) (y + (1.7721604 * (u - 128)) + (0.0009902 * (v - 128))));
   }

   /**
    * Compute green part of color from YUV<br>
    * G = Y - 0.3455 * (U - 128) - (0.7169 * (V - 128))
    * 
    * @param y
    *           Y
    * @param u
    *           U
    * @param v
    *           V
    * @return Green part
    */
   private static int computeGreen(final double y, final double u, final double v)
   {
      return UtilMath.limit0_255((int) (y - (0.3436954 * (u - 128)) - (0.7141690 * (v - 128))));
   }

   /**
    * Compute red part of color from YUV<br>
    * R = Y + 1.4075 * (V - 128)
    * 
    * @param y
    *           Y
    * @param u
    *           U
    * @param v
    *           V
    * @return Red part
    */
   private static int computeRed(final double y, final double u, final double v)
   {
      return UtilMath.limit0_255((int) ((y - (0.0009267 * (u - 128))) + (1.4016868 * (v - 128))));
   }

   /**
    * Compute U of a color<br>
    * U = R * -.168736 + G * -.331264 + B * .500000 + 128
    * 
    * @param red
    *           Red part
    * @param green
    *           Green part
    * @param blue
    *           Blue part
    * @return U
    */
   private static double computeU(final int red, final int green, final int blue)
   {
      return ((-0.169 * red) - (0.331 * green)) + (0.500 * blue) + 128.0;
   }

   /**
    * Compute V of a color<br>
    * V = R * .500000 + G * -.418688 + B * -.081312 + 128
    * 
    * @param red
    *           Red part
    * @param green
    *           Green part
    * @param blue
    *           Blue part
    * @return V
    */
   private static double computeV(final int red, final int green, final int blue)
   {
      return ((0.500 * red) - (0.419 * green) - (0.081 * blue)) + 128.0;
   }

   /**
    * Compute Y of a color<br>
    * Y = R * .299000 + G * .587000 + B * .114000
    * 
    * @param red
    *           Red part
    * @param green
    *           Green part
    * @param blue
    *           Blue part
    * @return Y
    */
   private static double computeY(final int red, final int green, final int blue)
   {
      return (red * 0.299) + (green * 0.587) + (blue * 0.114);
   }

   /**
    * Load a buffered image
    * 
    * @param image
    *           Image file
    * @return Buffered image loaded
    * @throws IOException
    *            On reading file issue
    */
   private static BufferedImage loadBufferedImage(final File image) throws IOException
   {
      final String name = image.getName().toLowerCase();

      final int index = name.lastIndexOf('.');

      if(index > 0)
      {
         ImageInputStream stream = null;
         ImageReader imageReader = null;
         BufferedImage bufferedImage;
         try
         {
            stream = ImageIO.createImageInputStream(image);
            final Iterator<ImageReader> imagesReaders = ImageIO.getImageReadersBySuffix(name.substring(index + 1));

            while(imagesReaders.hasNext() == true)
            {
               imageReader = imagesReaders.next();

               imageReader.setInput(stream);
               bufferedImage = imageReader.read(0);
               imageReader.dispose();

               return bufferedImage;
            }
         }
         catch(final Exception exception)
         {
            Debug.printException(exception);
         }
         finally
         {
            if(stream != null)
            {
               try
               {
                  stream.close();
               }
               catch(final Exception exception)
               {
               }
            }

            if(imageReader != null)
            {
               imageReader.dispose();
            }
         }
      }

      return ImageIO.read(image);
   }

   /**
    * Create a bump image with 0.75 contrast, 12 dark, 1 shift X and 1 shift Y
    * 
    * @param source
    *           Image source
    * @param bump
    *           Image used for bump
    * @return Bumped image
    */
   public static JHelpImage createBumpedImage(final JHelpImage source, final JHelpImage bump)
   {
      return JHelpImage.createBumpedImage(source, bump, 0.75, 12, 1, 1);
   }

   /**
    * Create a bump image
    * 
    * @param source
    *           Image source
    * @param bump
    *           Image used for bump
    * @param contrast
    *           Contrast to use in [0, 1].
    * @param dark
    *           Dark to use. in [0, 255].
    * @param shiftX
    *           Shift X [-3, 3].
    * @param shiftY
    *           Shift Y [-3, 3].
    * @return Bumped image
    */
   public static JHelpImage createBumpedImage(final JHelpImage source, final JHelpImage bump, double contrast, final int dark, final int shiftX, final int shiftY)
   {
      final int width = source.getWidth();
      final int height = source.getHeight();

      if((width != bump.getWidth()) || (height != bump.getHeight()))
      {
         throw new IllegalArgumentException("Images must have the same size");
      }

      if(contrast < 0.5)
      {
         contrast *= 2;
      }
      else
      {
         contrast = (contrast * 18) - 8;
      }

      source.update();
      bump.update();
      final JHelpImage bumped = new JHelpImage(width, height);
      final JHelpImage temp = new JHelpImage(width, height);

      bumped.startDrawMode();
      temp.startDrawMode();

      bumped.copy(bump);
      bumped.gray();
      bumped.contrast(contrast);

      temp.copy(bumped);
      temp.multiply(source);
      temp.darker(dark);

      bumped.invertColors();
      bumped.multiply(source);
      bumped.darker(dark);
      bumped.shift(shiftX, shiftY);
      bumped.addition(temp);

      bumped.endDrawMode();
      temp.endDrawMode();

      return bumped;
   }

   /**
    * Create a bump image with 0.75 contrast, 12 dark, -1 shift X and -1 shift Y
    * 
    * @param source
    *           Image source
    * @param bump
    *           Image used for bump
    * @return Bumped image
    */
   public static JHelpImage createBumpedImage2(final JHelpImage source, final JHelpImage bump)
   {
      return JHelpImage.createBumpedImage(source, bump, 0.75, 12, -1, -1);
   }

   /**
    * Extract the border of the objects inside the image. Width 1, step 1
    * 
    * @param source
    *           Image source
    * @return Image border
    */
   public static JHelpImage extractBorder(final JHelpImage source)
   {
      return JHelpImage.extractBorder(source, 1);
   }

   /**
    * Extract the border of the objects inside the image. Step 1
    * 
    * @param source
    *           Image source
    * @param width
    *           Line width
    * @return Image border
    */
   public static JHelpImage extractBorder(final JHelpImage source, final int width)
   {
      return JHelpImage.extractBorder(source, width, 1);
   }

   /**
    * Extract the border of the objects inside the image.
    * 
    * @param source
    *           Image source
    * @param width
    *           Line width
    * @param step
    *           Step to jump between width : [1, width]
    * @return Image border
    */
   public static JHelpImage extractBorder(final JHelpImage source, final int width, final int step)
   {
      if(width < 0)
      {
         throw new IllegalArgumentException("width can't be negative");
      }

      if(step < 1)
      {
         throw new IllegalArgumentException("step must be >=1");
      }

      source.update();

      final JHelpImage result = source.createCopy();

      result.startDrawMode();

      result.gray();
      final JHelpImage temporary = result.createCopy();
      result.invertColors();
      final JHelpImage temp = result.createCopy();
      temp.startDrawMode();

      result.shift(1, 1);
      result.addition(temporary);
      final JHelpImage image = temp.createCopy();

      for(int y = -width; y <= width; y += step)
      {
         for(int x = -width; x <= width; x += step)
         {
            temp.copy(image);
            temp.shift(x, y);
            temp.addition(temporary);
            result.minimum(temp);
         }
      }

      temp.endDrawMode();
      result.endDrawMode();

      return result;
   }

   /**
    * Extract the border of the objects inside the image. Width 2, step 1
    * 
    * @param source
    *           Image source
    * @return Image border
    */
   public static JHelpImage extractBorder2(final JHelpImage source)
   {
      return JHelpImage.extractBorder(source, 2);
   }

   /**
    * Load an image from file
    * 
    * @param image
    *           Image file
    * @return Loaded image
    * @throws IOException
    *            On file reading issue
    */
   public static JHelpImage loadImage(final File image) throws IOException
   {
      BufferedImage bufferedImage = JHelpImage.loadBufferedImage(image);

      final int width = bufferedImage.getWidth();
      final int height = bufferedImage.getHeight();
      int[] pixels = new int[width * height];

      pixels = bufferedImage.getRGB(0, 0, width, height, pixels, 0, width);

      final JHelpImage imageLoaded = new JHelpImage(width, height, pixels);

      pixels = null;
      bufferedImage.flush();
      bufferedImage = null;

      return imageLoaded;
   }

   /**
    * Load an image from a stream
    * 
    * @param inputStream
    *           Stream to read
    * @return Read image
    * @throws IOException
    *            On reading issue
    */
   public static JHelpImage loadImage(final InputStream inputStream) throws IOException
   {
      BufferedImage bufferedImage = ImageIO.read(inputStream);

      final int width = bufferedImage.getWidth();
      final int height = bufferedImage.getHeight();
      int[] pixels = new int[width * height];

      pixels = bufferedImage.getRGB(0, 0, width, height, pixels, 0, width);

      final JHelpImage image = new JHelpImage(width, height, pixels);

      pixels = null;
      bufferedImage.flush();
      bufferedImage = null;

      return image;
   }

   /**
    * Load an image and resize it to have specific dimension
    * 
    * @param image
    *           Image file
    * @param width
    *           Final width
    * @param height
    *           Final height
    * @return Loaded image resized to corresponds to specified dimension
    * @throws IOException
    *            On reading file issue
    */
   public static JHelpImage loadImageThumb(final File image, final int width, final int height) throws IOException
   {
      BufferedImage bufferedImage = JHelpImage.loadBufferedImage(image);

      if(bufferedImage == null)
      {
         return null;
      }

      final int imageWidth = bufferedImage.getWidth();
      final int imageHeight = bufferedImage.getHeight();

      if((imageWidth != width) || (imageHeight != height))
      {
         final BufferedImage bufferedImageTemp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

         final Graphics2D graphics2d = bufferedImageTemp.createGraphics();

         graphics2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
         graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         graphics2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
         graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
         graphics2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

         graphics2d.drawImage(bufferedImage, 0, 0, width, height, null);

         bufferedImage.flush();
         bufferedImage = bufferedImageTemp;
      }

      int[] pixels = new int[width * height];

      pixels = bufferedImage.getRGB(0, 0, width, height, pixels, 0, width);

      final JHelpImage imageLoaded = new JHelpImage(width, height, pixels);

      pixels = null;
      bufferedImage.flush();
      bufferedImage = null;

      return imageLoaded;
   }

   /**
    * Load an image and resize it to have specific dimension
    * 
    * @param inputStream
    *           Stream where lies the image
    * @param width
    *           Final width
    * @param height
    *           Final height
    * @return Loaded image resized to corresponds to specified dimension
    * @throws IOException
    *            On reading stream issue
    */
   public static JHelpImage loadImageThumb(final InputStream inputStream, final int width, final int height) throws IOException
   {
      BufferedImage bufferedImage = ImageIO.read(inputStream);

      if(bufferedImage == null)
      {
         return null;
      }

      final int imageWidth = bufferedImage.getWidth();
      final int imageHeight = bufferedImage.getHeight();

      if((imageWidth != width) || (imageHeight != height))
      {
         final BufferedImage bufferedImageTemp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

         final Graphics2D graphics2d = bufferedImageTemp.createGraphics();

         graphics2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
         graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         graphics2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
         graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
         graphics2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

         graphics2d.drawImage(bufferedImage, 0, 0, width, height, null);

         bufferedImage.flush();
         bufferedImage = bufferedImageTemp;
      }

      int[] pixels = new int[width * height];

      pixels = bufferedImage.getRGB(0, 0, width, height, pixels, 0, width);

      final JHelpImage image = new JHelpImage(width, height, pixels);

      pixels = null;
      bufferedImage.flush();
      bufferedImage = null;

      return image;
   }

   /**
    * Save an image to a stream
    * 
    * @param outputStream
    *           Stream where write
    * @param image
    *           Image to save
    * @throws IOException
    *            On writing issue
    */
   public static void saveImage(final OutputStream outputStream, final JHelpImage image) throws IOException
   {
      BufferedImage bufferedImage = new BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_ARGB);

      bufferedImage.setRGB(0, 0, image.width, image.height, image.pixels, 0, image.width);

      ImageIO.write(bufferedImage, "PNG", outputStream);

      outputStream.flush();
      bufferedImage.flush();

      bufferedImage = null;
   }

   /** List of registered components to alert if image update */
   private ArrayList<Component>   componentsListeners;
   /** Actaul draw mode */
   private boolean                drawMode;
   /** Image height */
   private final int              height;
   /** Image for draw in a swing component */
   private Image                  image;
   /** Image source */
   private MemoryImageSource      memoryImageSource;
   /** Image name */
   private String                 name;

   /** Image pixels */
   private int[]                  pixels;

   /** For synchronize */
   private final ReentrantLock    reentrantLock = new ReentrantLock(true);

   /** List of sprite */
   private ArrayList<JHelpSprite> sprites;

   /**
    * Last sprite visibility information collected on {@link #startDrawMode()} to resitute sprite in good state when
    * {@link #endDrawMode()} is call
    */
   private boolean[]              visibilities;

   /** Image width */
   private final int              width;

   /**
    * Create a new instance of JHelpImage empty
    * 
    * @param width
    *           Width
    * @param height
    *           Height
    */
   public JHelpImage(final int width, final int height)
   {
      this(width, height, new int[width * height]);
   }

   /**
    * Create a new instance of JHelpImage full of one color
    * 
    * @param width
    *           Width
    * @param height
    *           Height
    * @param color
    *           Background color to use
    */
   public JHelpImage(final int width, final int height, final int color)
   {
      this(width, height);

      this.drawMode = true;
      this.clear(color);
      this.drawMode = false;
   }

   /**
    * Create a new instance of JHelpImage
    * 
    * @param width
    *           Width
    * @param height
    *           Height
    * @param pixels
    *           Image pixels
    */
   public JHelpImage(final int width, final int height, final int[] pixels)
   {
      if((width < 1) || (height < 1))
      {
         throw new IllegalArgumentException("width and height must be > 1, but it is specify : " + width + "x" + height);
      }

      if((width * height) != pixels.length)
      {
         throw new IllegalArgumentException("The pixels array size must be width*height, but it is specify width=" + width + " height=" + height + " pixels.length=" + pixels.length);
      }

      this.width = width;
      this.height = height;

      this.pixels = pixels;

      this.memoryImageSource = new MemoryImageSource(width, height, pixels, 0, width);
      this.memoryImageSource.setAnimated(true);
      this.memoryImageSource.setFullBufferUpdates(true);

      this.image = Toolkit.getDefaultToolkit().createImage(this.memoryImageSource);

      this.sprites = new ArrayList<JHelpSprite>();
      this.componentsListeners = new ArrayList<Component>();

      this.drawMode = false;
   }

   /**
    * Draw a shape on center it
    * 
    * @param shape
    *           Shape to draw
    * @param color
    *           Color to use
    * @param doAlphaMix
    *           Indicates if alpha mix is on
    */
   private void drawShapeCenter(final Shape shape, final int color, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      final PathIterator pathIterator = shape.getPathIterator(ConstantsGUI.AFFINE_TRANSFORM, ConstantsGUI.FLATNESS);

      final double[] info = new double[6];
      int x = 0;
      int y = 0;
      int xStart = 0;
      int yStart = 0;
      int xx, yy;

      final Rectangle bounds = shape.getBounds();
      final int vx = bounds.width >> 1;
      final int vy = bounds.height >> 1;

      while(pathIterator.isDone() == false)
      {
         switch(pathIterator.currentSegment(info))
         {
            case PathIterator.SEG_MOVETO:
               xStart = x = (int) Math.round(info[0]);
               yStart = y = (int) Math.round(info[1]);

            break;
            case PathIterator.SEG_LINETO:
               xx = (int) Math.round(info[0]);
               yy = (int) Math.round(info[1]);

               this.drawLine(x - vx, y - vy, xx - vx, yy - vy, color, doAlphaMix);

               x = xx;
               y = yy;

            break;
            case PathIterator.SEG_CLOSE:
               this.drawLine(x - vx, y - vy, xStart - vx, yStart - vy, color, doAlphaMix);

               x = xStart;
               y = yStart;

            break;
         }

         pathIterator.next();
      }
   }

   /**
    * Change a sprite visibility
    * 
    * @param index
    *           Sprite index
    * @param visible
    *           New visibility state
    * @param forced
    *           Indicated that the changes is forced
    */
   void changeSpriteVisibiliy(final int index, final boolean visible, final boolean forced)
   {
      if(this.drawMode == true)
      {
         this.visibilities[index] = visible;

         return;
      }

      final int length = this.sprites.size();
      JHelpSprite sprite;

      final boolean[] visibles = new boolean[length];
      for(int i = 0; i < length; i++)
      {
         visibles[i] = false;
      }
      boolean visi;

      for(int i = length - 1; i > index; i--)
      {
         sprite = this.sprites.get(i);
         visi = visibles[i] = sprite.isVisible();

         if(visi == true)
         {
            sprite.changeVisible(false, false);
         }
      }

      this.sprites.get(index).changeVisible(visible, forced);

      for(int i = index + 1; i < length; i++)
      {
         if(visibles[i] == true)
         {
            this.sprites.get(i).changeVisible(true, false);
         }
      }
   }

   /**
    * Draw a part of an image on this image
    * 
    * @param x
    *           X on this image
    * @param y
    *           Y on this image
    * @param image
    *           Image to draw
    * @param xImage
    *           X on given image
    * @param yImage
    *           Y on given image
    * @param width
    *           Part width
    * @param height
    *           Part height
    * @param doAlphaMix
    *           Indicates if we do the mixing {@code true}, or we just override {@code false}
    */
   void drawImageInternal(int x, int y, final JHelpImage image, int xImage, int yImage, int width, int height, final boolean doAlphaMix)
   {
      if(doAlphaMix == false)
      {
         this.drawImageOver(x, y, image, xImage, yImage, width, height);
         return;
      }

      if(x < 0)
      {
         xImage -= x;
         width += x;
         x = 0;
      }

      if(xImage < 0)
      {
         x -= xImage;
         width += xImage;
         xImage = 0;
      }

      if(y < 0)
      {
         yImage -= y;
         height += y;
         y = 0;
      }

      if(yImage < 0)
      {
         y -= yImage;
         height += yImage;
         yImage = 0;
      }

      final int w = UtilMath.minIntegers(this.width - x, image.width - xImage, width);
      final int h = UtilMath.minIntegers(this.height - y, image.height - yImage, height);

      if((w <= 0) || (h <= 0))
      {
         return;
      }

      int lineThis = x + (y * this.width);
      int pixThis;

      int lineImage = xImage + (yImage * image.width);
      int pixImage;

      int colorThis;
      int colorImage;
      int alpha;
      int ahpla;

      for(int yy = 0; yy < h; yy++)
      {
         pixThis = lineThis;
         pixImage = lineImage;

         for(int xx = 0; xx < w; xx++)
         {
            colorImage = image.pixels[pixImage];

            alpha = (colorImage >> 24) & 0xFF;

            if(alpha == 255)
            {
               this.pixels[pixThis] = colorImage;
            }
            else if(alpha > 0)
            {
               ahpla = 256 - alpha;

               colorThis = this.pixels[pixThis];

               this.pixels[pixThis] = (Math.min(255, alpha + ((colorThis >> 24) & 0xFF)) << 24) | //
                     ((((((colorImage >> 16) & 0xFF) * alpha) + (((colorThis >> 16) & 0xFF) * ahpla)) >> 8) << 16) | //
                     ((((((colorImage >> 8) & 0xFF) * alpha) + (((colorThis >> 8) & 0xFF) * ahpla)) >> 8) << 8) | //
                     ((((colorImage & 0xFF) * alpha) + ((colorThis & 0xFF) * ahpla)) >> 8);
            }

            pixThis++;
            pixImage++;
         }

         lineThis += this.width;
         lineImage += image.width;
      }
   }

   /**
    * Draw a part of image on using a specific alpha value
    * 
    * @param x
    *           X to draw image
    * @param y
    *           Y to draw image
    * @param image
    *           Image to draw
    * @param xImage
    *           Start X of image part
    * @param yImage
    *           Start Y of image part
    * @param width
    *           Width of image part
    * @param height
    *           Height of image part
    * @param alpha
    *           Alpha to use
    */
   void drawImageInternal(int x, int y, final JHelpImage image, int xImage, int yImage, int width, int height, final int alpha)
   {
      if(alpha == 255)
      {
         this.drawImageOver(x, y, image, xImage, yImage, width, height);
         return;
      }

      if(alpha == 0)
      {
         return;
      }

      if(x < 0)
      {
         xImage -= x;
         width += x;
         x = 0;
      }

      if(xImage < 0)
      {
         x -= xImage;
         width += xImage;
         xImage = 0;
      }

      if(y < 0)
      {
         yImage -= y;
         height += y;
         y = 0;
      }

      if(yImage < 0)
      {
         y -= yImage;
         height += yImage;
         yImage = 0;
      }

      final int w = UtilMath.minIntegers(this.width - x, image.width - xImage, width);
      final int h = UtilMath.minIntegers(this.height - y, image.height - yImage, height);

      if((w <= 0) || (h <= 0))
      {
         return;
      }

      int lineThis = x + (y * this.width);
      int pixThis;

      int lineImage = xImage + (yImage * image.width);
      int pixImage;

      int colorThis;
      int colorImage;

      final int ahpla = 256 - alpha;

      for(int yy = 0; yy < h; yy++)
      {
         pixThis = lineThis;
         pixImage = lineImage;

         for(int xx = 0; xx < w; xx++)
         {
            colorImage = image.pixels[pixImage];

            colorThis = this.pixels[pixThis];

            this.pixels[pixThis] = (Math.min(255, alpha + ((colorThis >> 24) & 0xFF)) << 24) | //
                  ((((((colorImage >> 16) & 0xFF) * alpha) + (((colorThis >> 16) & 0xFF) * ahpla)) >> 8) << 16) | //
                  ((((((colorImage >> 8) & 0xFF) * alpha) + (((colorThis >> 8) & 0xFF) * ahpla)) >> 8) << 8) | //
                  ((((colorImage & 0xFF) * alpha) + ((colorThis & 0xFF) * ahpla)) >> 8);

            pixThis++;
            pixImage++;
         }

         lineThis += this.width;
         lineImage += image.width;
      }
   }

   /**
    * Draw apart of image over this image 'just override)
    * 
    * @param x
    *           X on this image
    * @param y
    *           Y on this image
    * @param image
    *           Image to draw
    * @param xImage
    *           X on image
    * @param yImage
    *           Y on image
    * @param width
    *           Part width
    * @param height
    *           Part height
    */
   void drawImageOver(int x, int y, final JHelpImage image, int xImage, int yImage, int width, int height)
   {
      if(x < 0)
      {
         xImage -= x;
         width += x;
         x = 0;
      }

      if(xImage < 0)
      {
         x -= xImage;
         width += xImage;
         xImage = 0;
      }

      if(y < 0)
      {
         yImage -= y;
         height += y;
         y = 0;
      }

      if(yImage < 0)
      {
         y -= yImage;
         height += yImage;
         yImage = 0;
      }

      final int w = UtilMath.minIntegers(this.width - x, image.width - xImage, width);
      final int h = UtilMath.minIntegers(this.height - y, image.height - yImage, height);

      if((w <= 0) || (h <= 0))
      {
         return;
      }

      int lineThis = x + (y * this.width);
      int lineImage = xImage + (yImage * image.width);

      for(int yy = 0; yy < h; yy++)
      {
         System.arraycopy(image.pixels, lineImage, this.pixels, lineThis, w);

         lineThis += this.width;
         lineImage += image.width;
      }
   }

   /**
    * Refresh the image
    */
   void refresh()
   {
      this.memoryImageSource.newPixels();
   }

   /**
    * Call by garbage collector to free some memory <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @throws Throwable
    *            On issue
    * @see java.lang.Object#finalize()
    */
   @Override
   protected void finalize() throws Throwable
   {
      this.pixels = null;
      this.memoryImageSource = null;

      if(this.image != null)
      {
         this.image.flush();
      }
      this.image = null;

      if(this.sprites != null)
      {
         this.sprites.clear();
      }
      this.sprites = null;

      this.visibilities = null;

      this.reentrantLock.lock();

      try
      {
         if(this.componentsListeners != null)
         {
            this.componentsListeners.clear();
         }
         this.componentsListeners = null;
      }
      finally
      {
         this.reentrantLock.unlock();
      }

      super.finalize();
   }

   /**
    * Add an other image
    * 
    * @param image
    *           Image to add
    */
   public void addition(final JHelpImage image)
   {
      if((this.width != image.width) || (this.height != image.height))
      {
         throw new IllegalArgumentException("We can only multiply with an image of same size");
      }

      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      int colorThis, colorImage;

      for(int pix = this.pixels.length - 1; pix >= 0; pix--)
      {
         colorThis = this.pixels[pix];
         colorImage = image.pixels[pix];

         this.pixels[pix] = (colorThis & 0xFF000000) | //
               (UtilMath.limit0_255(((colorThis >> 16) & 0xFF) + ((colorImage >> 16) & 0xFF)) << 16) | //
               (UtilMath.limit0_255(((colorThis >> 8) & 0xFF) + ((colorImage >> 8) & 0xFF)) << 8) | //
               UtilMath.limit0_255((colorThis & 0xFF) + (colorImage & 0xFF));
      }
   }

   /**
    * Fill with the pallete different area
    * 
    * @param precision
    *           Precision to use for distinguish 2 area
    */
   public void applyPalette(final int precision)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      final SortedArray<Color> colors = new SortedArray<Color>(Color.class);
      final int size = this.pixels.length - 1;
      Color.precision = precision;
      int index, col;
      Color color;

      for(int i = size; i >= 0; i--)
      {
         color = new Color(this.pixels[i]);
         index = colors.indexOf(color);

         if(index < 0)
         {
            col = color.info = colors.getSize();
            colors.add(color);
         }
         else
         {
            col = colors.getElement(index).info;
         }

         this.pixels[i] = JHelpImage.PALETTE[col % JHelpImage.PALETTE_SIZE];
      }
   }

   /**
    * Put the image brighter
    * 
    * @param factor
    *           Factor of bright
    */
   public void brighter(final int factor)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      int color;

      for(int pix = this.pixels.length - 1; pix >= 0; pix--)
      {
         color = this.pixels[pix];

         this.pixels[pix] = (color & 0xFF000000) | //
               (UtilMath.limit0_255(((color >> 16) & 0xFF) + factor) << 16) | //
               (UtilMath.limit0_255(((color >> 8) & 0xFF) + factor) << 8) | //
               UtilMath.limit0_255((color & 0xFF) + factor);
      }
   }

   /**
    * Change image brightness
    * 
    * @param factor
    *           Brightness factor
    */
   public void brightness(final double factor)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      int color;
      int red, green, blue;
      double y, u, v;

      for(int pix = this.pixels.length - 1; pix >= 0; pix--)
      {
         color = this.pixels[pix];

         red = (color >> 16) & 0xFF;
         green = (color >> 8) & 0xFF;
         blue = color & 0xFF;

         y = JHelpImage.computeY(red, green, blue) * factor;
         u = JHelpImage.computeU(red, green, blue);
         v = JHelpImage.computeV(red, green, blue);

         this.pixels[pix] = (color & 0xFF000000) | //
               (JHelpImage.computeRed(y, u, v) << 16) | //
               (JHelpImage.computeGreen(y, u, v) << 8) | //
               JHelpImage.computeBlue(y, u, v);
      }
   }

   /**
    * Colorize all near color with same color
    * 
    * @param precision
    *           Precision to use
    */
   public void categorizeByColor(final int precision)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      final SortedArray<Color> colors = new SortedArray<Color>(Color.class);
      final int size = this.pixels.length - 1;
      Color.precision = precision;
      Color color;
      int index;

      for(int i = size; i >= 0; i--)
      {
         color = new Color(this.pixels[i]);

         index = colors.indexOf(color);

         if(index < 0)
         {
            colors.add(color);
            this.pixels[i] = color.color;
         }
         else
         {
            this.pixels[i] = colors.getElement(index).color;
         }
      }
   }

   /**
    * Colorize with 3 colors, one used for "dark" colors, one for "gray" colors and last for "white" colors
    * 
    * @param colorLow
    *           Color for dark
    * @param colorMiddle
    *           Color for gray
    * @param colorHigh
    *           Color for whte
    * @param precision
    *           Precision for decide witch are gray
    */
   public void categorizeByY(final int colorLow, final int colorMiddle, final int colorHigh, final double precision)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      int color, red, green, blue, index;
      double yAverage, y;

      index = this.pixels.length - 1;
      color = this.pixels[index];
      red = (color >> 16) & 0xFF;
      green = (color >> 8) & 0xFF;
      blue = color & 0xFF;

      yAverage = JHelpImage.computeY(red, green, blue);

      index--;
      while(index >= 0)
      {
         color = this.pixels[index];
         red = (color >> 16) & 0xFF;
         green = (color >> 8) & 0xFF;
         blue = color & 0xFF;

         yAverage += JHelpImage.computeY(red, green, blue);

         index--;
      }

      final double ymil = yAverage / this.pixels.length;

      for(int i = this.pixels.length - 1; i >= 0; i--)
      {
         color = this.pixels[i];
         red = (color >> 16) & 0xFF;
         green = (color >> 8) & 0xFF;
         blue = color & 0xFF;

         y = JHelpImage.computeY(red, green, blue);

         if(Math.abs(y - ymil) <= precision)
         {
            this.pixels[i] = colorMiddle;
         }
         else if(y < ymil)
         {
            this.pixels[i] = colorLow;
         }
         else
         {
            this.pixels[i] = colorHigh;
         }
      }
   }

   /**
    * Fill the entire image with same color
    * 
    * @param color
    *           Color to use
    */
   public void clear(final int color)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      for(int pix = this.pixels.length - 1; pix >= 0; pix--)
      {
         this.pixels[pix] = color;
      }
   }

   /**
    * Colorize with automatic palette
    * 
    * @param precision
    *           Precision to use
    * @return Number of different color
    */
   public int colorizeWithPalette(final int precision)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      final int size = this.pixels.length;
      final int[] result = new int[size];
      int indexPalette = 0xFF000000;// 0;
      int color, reference, red, green, blue;
      int p;
      final Stack<Point> stack = new Stack<Point>();
      Point point;
      int x = this.width - 1;
      int y = this.height - 1;

      for(int pix = size - 1; pix >= 0; pix--)
      {
         if(result[pix] == 0)
         {
            color = indexPalette;// JHelpImage.PALETTE[indexPalette];
            indexPalette++;// = (indexPalette + 1) % JHelpImage.PALETTE_SIZE;

            reference = this.pixels[pix];
            red = (reference >> 16) & 0xFF;
            green = (reference >> 8) & 0xFF;
            blue = reference & 0xFF;

            stack.push(new Point(x, y));

            while(stack.isEmpty() == false)
            {
               point = stack.pop();
               p = point.x + (point.y * this.width);

               result[p] = color;

               if((point.x > 0) && (result[p - 1] == 0) && (Color.isNear(red, green, blue, this.pixels[p - 1], precision) == true))
               {
                  stack.push(new Point(point.x - 1, point.y));
               }

               if((point.y > 0) && (result[p - this.width] == 0) && (Color.isNear(red, green, blue, this.pixels[p - this.width], precision) == true))
               {
                  stack.push(new Point(point.x, point.y - 1));
               }

               if((point.x < (this.width - 1)) && (result[p + 1] == 0) && (Color.isNear(red, green, blue, this.pixels[p + 1], precision) == true))
               {
                  stack.push(new Point(point.x + 1, point.y));
               }

               if((point.y < (this.height - 1)) && (result[p + this.width] == 0) && (Color.isNear(red, green, blue, this.pixels[p + this.width], precision) == true))
               {
                  stack.push(new Point(point.x, point.y + 1));
               }
            }
         }

         x--;
         if(x < 0)
         {
            x = this.width - 1;
            y--;
         }
      }

      System.arraycopy(result, 0, this.pixels, 0, size);

      return indexPalette & 0x00FFFFFF;
   }

   /**
    * Change image contrast by using the middle of the minmum and maximum
    * 
    * @param factor
    *           Factor to apply to the contrast
    */
   public void contrast(final double factor)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      int color, red, green, blue, index;
      double ymin, ymax, y;

      index = this.pixels.length - 1;
      color = this.pixels[index];
      red = (color >> 16) & 0xFF;
      green = (color >> 8) & 0xFF;
      blue = color & 0xFF;

      ymin = ymax = JHelpImage.computeY(red, green, blue);

      index--;
      while(index >= 0)
      {
         color = this.pixels[index];
         red = (color >> 16) & 0xFF;
         green = (color >> 8) & 0xFF;
         blue = color & 0xFF;

         y = JHelpImage.computeY(red, green, blue);

         ymin = Math.min(ymin, y);
         ymax = Math.max(ymax, y);

         index--;
      }

      final double ymil = (ymin + ymax) / 2;
      double u, v;

      for(int i = this.pixels.length - 1; i >= 0; i--)
      {
         color = this.pixels[i];
         red = (color >> 16) & 0xFF;
         green = (color >> 8) & 0xFF;
         blue = color & 0xFF;

         y = JHelpImage.computeY(red, green, blue);
         u = JHelpImage.computeU(red, green, blue);
         v = JHelpImage.computeV(red, green, blue);

         y = (ymil + (factor * (y - ymil)));

         this.pixels[i] = (color & 0xFF000000) | (JHelpImage.computeRed(y, u, v) << 16) | (JHelpImage.computeGreen(y, u, v) << 8) | JHelpImage.computeBlue(y, u, v);
      }
   }

   /**
    * Change image contrast by using the average of all values
    * 
    * @param factor
    *           Factor to apply to the contrast
    */
   public void contrastAvreage(final double factor)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      int color, red, green, blue, index;
      double yAverage, y;

      index = this.pixels.length - 1;
      color = this.pixels[index];
      red = (color >> 16) & 0xFF;
      green = (color >> 8) & 0xFF;
      blue = color & 0xFF;

      yAverage = JHelpImage.computeY(red, green, blue);

      index--;
      while(index >= 0)
      {
         color = this.pixels[index];
         red = (color >> 16) & 0xFF;
         green = (color >> 8) & 0xFF;
         blue = color & 0xFF;

         yAverage += JHelpImage.computeY(red, green, blue);

         index--;
      }

      final double ymil = yAverage / this.pixels.length;
      double u, v;

      for(int i = this.pixels.length - 1; i >= 0; i--)
      {
         color = this.pixels[i];
         red = (color >> 16) & 0xFF;
         green = (color >> 8) & 0xFF;
         blue = color & 0xFF;

         y = JHelpImage.computeY(red, green, blue);
         u = JHelpImage.computeU(red, green, blue);
         v = JHelpImage.computeV(red, green, blue);

         y = (ymil + (factor * (y - ymil)));

         this.pixels[i] = (color & 0xFF000000) | (JHelpImage.computeRed(y, u, v) << 16) | (JHelpImage.computeGreen(y, u, v) << 8) | JHelpImage.computeBlue(y, u, v);
      }
   }

   /**
    * Copy the image is this one
    * 
    * @param image
    *           Image to copy
    */
   public void copy(final JHelpImage image)
   {
      if((this.width != image.width) || (this.height != image.height))
      {
         throw new IllegalArgumentException("We can only multiply with an image of same size");
      }

      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      System.arraycopy(image.pixels, 0, this.pixels, 0, this.pixels.length);
   }

   /**
    * Create a couple of sprite and associated animated image
    * 
    * @param x
    *           X position
    * @param y
    *           Y position
    * @param width
    *           Sprite width
    * @param height
    *           Sprite height
    * @param animationMode
    *           Animation mode to use
    * @return Created couple
    */
   public Pair<JHelpSprite, JHelpAnimatedImage> createAnimatedSprite(final int x, final int y, final int width, final int height, final AnimationMode animationMode)
   {
      final JHelpSprite sprite = this.createSprite(x, y, width, height);

      final JHelpAnimatedImage animatedImage = new JHelpAnimatedImage(sprite.getImage(), animationMode);

      return new Pair<JHelpSprite, JHelpAnimatedImage>(sprite, animatedImage);
   }

   /**
    * Create an image copy
    * 
    * @return The copy
    */
   public JHelpImage createCopy()
   {
      final JHelpImage copy = new JHelpImage(this.width, this.height);

      copy.startDrawMode();
      copy.copy(this);
      copy.endDrawMode();

      return copy;
   }

   /**
    * Create a sprite
    * 
    * @param x
    *           Start X of sprite
    * @param y
    *           Start Y of sprite
    * @param width
    *           Sprite width
    * @param height
    *           Sprite height
    * @return Created sprite
    */
   public JHelpSprite createSprite(final int x, final int y, final int width, final int height)
   {
      if(this.drawMode == true)
      {
         throw new IllegalStateException("Musn't be in draw mode !");
      }

      final int index = this.sprites.size();

      final JHelpSprite sprite = new JHelpSprite(x, y, width, height, this, index);

      this.sprites.add(sprite);

      return sprite;
   }

   /**
    * Create sprite with initial image inside
    * 
    * @param x
    *           X
    * @param y
    *           Y
    * @param source
    *           Initial image
    * @return Created sprite
    */
   public JHelpSprite createSprite(final int x, final int y, final JHelpImage source)
   {
      if(this.drawMode == true)
      {
         throw new IllegalStateException("Musn't be in draw mode !");
      }

      if(source == null)
      {
         throw new NullPointerException("source musn't be null");
      }

      final int index = this.sprites.size();

      final JHelpSprite sprite = new JHelpSprite(x, y, source, this, index);

      this.sprites.add(sprite);

      return sprite;
   }

   /**
    * Make image darker
    * 
    * @param factor
    *           Darker factor in [0, 255]
    */
   public void darker(final int factor)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      int color;

      for(int pix = this.pixels.length - 1; pix >= 0; pix--)
      {
         color = this.pixels[pix];

         this.pixels[pix] = (color & 0xFF000000) | //
               (UtilMath.limit0_255(((color >> 16) & 0xFF) - factor) << 16) | //
               (UtilMath.limit0_255(((color >> 8) & 0xFF) - factor) << 8) | //
               UtilMath.limit0_255((color & 0xFF) - factor);
      }
   }

   /**
    * Divide an other image
    * 
    * @param image
    *           Image to divide with
    */
   public void divide(final JHelpImage image)
   {
      if((this.width != image.width) || (this.height != image.height))
      {
         throw new IllegalArgumentException("We can only multiply with an image of same size");
      }

      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      int colorThis, colorImage;

      for(int pix = this.pixels.length - 1; pix >= 0; pix--)
      {
         colorThis = this.pixels[pix];
         colorImage = image.pixels[pix];

         this.pixels[pix] = (colorThis & 0xFF000000) | //
               ((((((colorThis >> 16) & 0xFF) * 256) / (((colorImage >> 16) & 0xFF) + 1))) << 16) | //
               ((((((colorThis >> 8) & 0xFF) * 256) / (((colorImage >> 8) & 0xFF) + 1))) << 8) | //
               (((colorThis & 0xFF) * 256) / ((colorImage & 0xFF) + 1));
      }
   }

   /**
    * Draw an ellipse
    * 
    * @param x
    *           X of upper left corner
    * @param y
    *           Y of upper left corner
    * @param width
    *           Width
    * @param height
    *           Height
    * @param color
    *           Color to use
    */
   public void drawEllipse(final int x, final int y, final int width, final int height, final int color)
   {
      this.drawEllipse(x, y, width, height, color, true);
   }

   /**
    * Draw an ellipse
    * 
    * @param x
    *           X of upper left corner
    * @param y
    *           Y of upper left corner
    * @param width
    *           Width
    * @param height
    *           Height
    * @param color
    *           Color to use
    * @param doAlphaMix
    *           Indicates if we do the mixing {@code true}, or we just override {@code false}
    */
   public void drawEllipse(final int x, final int y, final int width, final int height, final int color, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      this.drawShape(new Ellipse2D.Double(x, y, width, height), color, doAlphaMix);
   }

   /**
    * Draw an horizontal line
    * 
    * @param x1
    *           Start X
    * @param x2
    *           End X
    * @param y
    *           Y
    * @param color
    *           Color to use
    */
   public void drawHorizontalLine(final int x1, final int x2, final int y, final int color)
   {
      this.drawHorizontalLine(x1, x2, y, color, true);
   }

   /**
    * Drzw horizontal line
    * 
    * @param x1
    *           X start
    * @param x2
    *           End X
    * @param y
    *           Y
    * @param color
    *           Color to use
    * @param doAlphaMix
    *           Indicates if we do the mixing {@code true}, or we just override {@code false}
    */
   public void drawHorizontalLine(final int x1, final int x2, final int y, final int color, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      if((y < 0) || (y >= this.height))
      {
         return;
      }

      int start = Math.max(0, Math.min(x1, x2));
      int end = Math.min(this.width - 1, Math.max(x1, x2));

      if(start > end)
      {
         return;
      }

      final int alpha = (color >> 24) & 0xFF;

      if((alpha == 0) && (doAlphaMix == true))
      {
         return;
      }

      final int yy = y * this.width;
      start += yy;
      end += yy;

      if((alpha == 255) || (doAlphaMix == false))
      {
         for(int pix = start; pix <= end; pix++)
         {
            this.pixels[pix] = color;
         }

         return;
      }

      final int ahpla = 256 - alpha;
      final int red = ((color >> 16) & 0xFF) * alpha;
      final int green = ((color >> 8) & 0xFF) * alpha;
      final int blue = (color & 0xFF) * alpha;
      int col;

      for(int pix = start; pix <= end; pix++)
      {
         col = this.pixels[pix];

         this.pixels[pix] = (Math.min(255, alpha + ((col >> 24) & 0xFF)) << 24) | //
               (((red + (((col >> 16) & 0xFF) * ahpla)) >> 8) << 16) | //
               (((green + (((col >> 8) & 0xFF) * ahpla)) >> 8) << 8) | //
               ((blue + ((col & 0xFF) * ahpla)) >> 8);
      }
   }

   /**
    * Draw an image
    * 
    * @param x
    *           X
    * @param y
    *           Y
    * @param image
    *           image to draw
    */
   public void drawImage(final int x, final int y, final JHelpImage image)
   {
      this.drawImage(x, y, image, true);
   }

   /**
    * Draw an image
    * 
    * @param x
    *           X
    * @param y
    *           Y
    * @param image
    *           Image to draw
    * @param doAlphaMix
    *           Indicates if we do the mixing {@code true}, or we just override {@code false}
    */
   public void drawImage(final int x, final int y, final JHelpImage image, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      this.drawImage(x, y, image, 0, 0, image.width, image.height, doAlphaMix);
   }

   /**
    * Draw an image over this image with specific alpha
    * 
    * @param x
    *           X position
    * @param y
    *           Y position
    * @param image
    *           Image to draw
    * @param alpha
    *           Alpha to use
    */
   public void drawImage(final int x, final int y, final JHelpImage image, final int alpha)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      this.drawImage(x, y, image, 0, 0, image.width, image.height, alpha);
   }

   /**
    * Draw a part off image
    * 
    * @param x
    *           X on this
    * @param y
    *           Y on this
    * @param image
    *           Image to draw
    * @param xImage
    *           X on image
    * @param yImage
    *           Y on image
    * @param width
    *           Part width
    * @param height
    *           Part height
    */
   public void drawImage(final int x, final int y, final JHelpImage image, final int xImage, final int yImage, final int width, final int height)
   {
      this.drawImage(x, y, image, xImage, yImage, width, height, true);
   }

   /**
    * Draw a part off image
    * 
    * @param x
    *           X on this
    * @param y
    *           Y on this
    * @param image
    *           Image to draw
    * @param xImage
    *           X on image
    * @param yImage
    *           Y on image
    * @param width
    *           Part width
    * @param height
    *           Part height
    * @param doAlphaMix
    *           Indicates if we do the mixing {@code true}, or we just override {@code false}
    */
   public void drawImage(final int x, final int y, final JHelpImage image, final int xImage, final int yImage, final int width, final int height, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      this.drawImageInternal(x, y, image, xImage, yImage, width, height, doAlphaMix);
   }

   /**
    * Draw a part of image with a specific alpha
    * 
    * @param x
    *           X position
    * @param y
    *           Y position
    * @param image
    *           Image to draw
    * @param xImage
    *           X of image part
    * @param yImage
    *           Y of image part
    * @param width
    *           Image part width
    * @param height
    *           Image part height
    * @param alpha
    *           Alpha to use
    */
   public void drawImage(final int x, final int y, final JHelpImage image, final int xImage, final int yImage, final int width, final int height, final int alpha)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      this.drawImageInternal(x, y, image, xImage, yImage, width, height, alpha);
   }

   /**
    * Draw a line
    * 
    * @param x1
    *           X of first point
    * @param y1
    *           Y first point
    * @param x2
    *           X second point
    * @param y2
    *           Y second point
    * @param color
    *           Color to use
    */
   public void drawLine(final int x1, final int y1, final int x2, final int y2, final int color)
   {
      this.drawLine(x1, y1, x2, y2, color, true);
   }

   /**
    * Draw a line
    * 
    * @param x1
    *           X of first point
    * @param y1
    *           Y first point
    * @param x2
    *           X second point
    * @param y2
    *           Y second point
    * @param color
    *           Color to use
    * @param doAlphaMix
    *           Indicates if we do the mixing {@code true}, or we just override {@code false}
    */
   public void drawLine(final int x1, final int y1, final int x2, final int y2, final int color, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      if(y1 == y2)
      {
         this.drawHorizontalLine(x1, x2, y1, color, doAlphaMix);

         return;
      }

      if(x1 == x2)
      {
         this.drawVerticalLine(x1, y1, y2, color, doAlphaMix);

         return;
      }

      final int alpha = (color >> 24) & 0xFF;

      if((alpha == 0) && (doAlphaMix == true))
      {
         return;
      }

      int error = 0;
      final int dx = Math.abs(x2 - x1);
      final int sx = UtilMath.sign(x2 - x1);
      final int dy = Math.abs(y2 - y1);
      final int sy = UtilMath.sign(y2 - y1);
      int x = x1;
      int y = y1;

      if(dx >= dy)
      {
         while(((x < 0) || (x >= this.width) || (y < 0) || (y >= this.height)) && ((x != x2) || (y != y2)))
         {
            x += sx;

            error += dy;
            if(error >= dx)
            {
               y += sy;

               error -= dx;
            }
         }
      }
      else
      {
         while(((x < 0) || (x >= this.width) || (y < 0) || (y >= this.height)) && ((x != x2) || (y != y2)))
         {
            y += sy;

            error += dx;
            if(error >= dy)
            {
               x += sx;

               error -= dy;
            }
         }
      }

      if(((x < 0) || (x >= this.width) || (y < 0) || (y >= this.height)) && (x == x2) && (y == y2))
      {
         return;
      }

      int pix = x + (y * this.width);
      final int moreY = sy * this.width;

      if((alpha == 255) || (doAlphaMix == false))
      {
         if(dx >= dy)
         {
            while((x >= 0) && (x < this.width) && (x != x2) && (y >= 0) && (y < this.height) && (y != y2))
            {
               this.pixels[pix] = color;

               pix += sx;
               x += sx;

               error += dy;
               if(error >= dx)
               {
                  pix += moreY;
                  y += sy;

                  error -= dx;
               }
            }
         }
         else
         {
            while((x >= 0) && (x < this.width) && (x != x2) && (y >= 0) && (y < this.height) && (y != y2))
            {
               this.pixels[pix] = color;

               pix += moreY;
               y += sy;

               error += dx;
               if(error >= dy)
               {
                  pix += sx;
                  x += sx;

                  error -= dy;
               }
            }
         }

         return;
      }

      final int ahpla = 256 - alpha;
      final int red = ((color >> 16) & 0xFF) * alpha;
      final int green = ((color >> 8) & 0xFF) * alpha;
      final int blue = (color & 0xFF) * alpha;
      int col;

      if(dx >= dy)
      {
         while((x >= 0) && (x < this.width) && (y >= 0) && (y < this.height) && ((x != x2) || (y != y2)))
         {
            col = this.pixels[pix];

            this.pixels[pix] = (Math.min(255, alpha + ((col >> 24) & 0xFF)) << 24) | //
                  (((red + (((col >> 16) & 0xFF) * ahpla)) >> 8) << 16) | //
                  (((green + (((col >> 8) & 0xFF) * ahpla)) >> 8) << 8) | //
                  ((blue + ((col & 0xFF) * ahpla)) >> 8);

            pix += sx;
            x += sx;

            error += dy;
            if(error >= dx)
            {
               pix += moreY;
               y += sy;

               error -= dx;
            }
         }
      }
      else
      {
         while((x >= 0) && (x < this.width) && (y >= 0) && (y < this.height) && ((x != x2) || (y != y2)))
         {
            col = this.pixels[pix];

            this.pixels[pix] = (Math.min(255, alpha + ((col >> 24) & 0xFF)) << 24) | //
                  (((red + (((col >> 16) & 0xFF) * ahpla)) >> 8) << 16) | //
                  (((green + (((col >> 8) & 0xFF) * ahpla)) >> 8) << 8) | //
                  ((blue + ((col & 0xFF) * ahpla)) >> 8);

            pix += moreY;
            y += sy;

            error += dx;
            if(error >= dy)
            {
               pix += sx;
               x += sx;

               error -= dy;
            }
         }
      }
   }

   /**
    * Draw a polygon
    * 
    * @param xs
    *           Polygon X list
    * @param offsetX
    *           Where start read the X list
    * @param ys
    *           Polygon Y list
    * @param offsetY
    *           Where start read the Y list
    * @param length
    *           Number of point
    * @param color
    *           Color to use
    */
   public void drawPolygon(final int[] xs, final int offsetX, final int[] ys, final int offsetY, final int length, final int color)
   {
      this.drawPolygon(xs, offsetX, ys, offsetY, length, color, true);
   }

   /**
    * Draw a polygon
    * 
    * @param xs
    *           Polygon X list
    * @param offsetX
    *           Where start read the X list
    * @param ys
    *           Polygon Y list
    * @param offsetY
    *           Where start read the Y list
    * @param length
    *           Number of point
    * @param color
    *           Color to use
    * @param doAlphaMix
    *           Indicates if we do the mixing {@code true}, or we just override {@code false}
    */
   public void drawPolygon(final int[] xs, int offsetX, final int[] ys, int offsetY, int length, final int color, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      if(offsetX < 0)
      {
         length += offsetX;

         offsetX = 0;
      }

      if(offsetY < 0)
      {
         length += offsetY;

         offsetY = 0;
      }

      length = UtilMath.minIntegers(length, xs.length - offsetX, ys.length - offsetY);

      if(length < 3)
      {
         return;
      }

      int x = xs[offsetX];
      final int xStart = x;
      int y = ys[offsetY];
      final int yStart = y;
      int xx, yy;

      for(int i = 1; i < length; i++)
      {
         offsetX++;
         offsetY++;

         xx = xs[offsetX];
         yy = ys[offsetY];

         this.drawLine(x, y, xx, yy, color, doAlphaMix);

         x = xx;
         y = yy;
      }

      this.drawLine(x, y, xStart, yStart, color, doAlphaMix);
   }

   /**
    * Draw a polygon
    * 
    * @param xs
    *           X list
    * @param ys
    *           Y list
    * @param color
    *           Color to use
    */
   public void drawPolygon(final int[] xs, final int[] ys, final int color)
   {
      this.drawPolygon(xs, ys, color, true);
   }

   /**
    * Draw a polygon
    * 
    * @param xs
    *           X list
    * @param ys
    *           Y list
    * @param color
    *           Color to use
    * @param doAlphaMix
    *           Indicates if we do the mixing {@code true}, or we just override {@code false}
    */
   public void drawPolygon(final int[] xs, final int[] ys, final int color, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      this.drawPolygon(xs, 0, ys, 0, Math.min(xs.length, ys.length), color, doAlphaMix);
   }

   /**
    * Draw an empty rectangle
    * 
    * @param x
    *           X of top-left
    * @param y
    *           Y of top-left
    * @param width
    *           Rectangle width
    * @param height
    *           Rectangle height
    * @param color
    *           Color to use
    */
   public void drawRectangle(final int x, final int y, final int width, final int height, final int color)
   {
      this.drawRectangle(x, y, width, height, color, true);
   }

   /**
    * Draw an empty rectangle
    * 
    * @param x
    *           X of top-left
    * @param y
    *           Y of top-left
    * @param width
    *           Rectangle width
    * @param height
    *           Rectangle height
    * @param color
    *           Color to use
    * @param doAlphaMix
    *           Indicates if we do the mixing {@code true}, or we just override {@code false}
    */
   public void drawRectangle(final int x, final int y, final int width, final int height, final int color, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      if((width <= 0) || (height <= 0))
      {
         return;
      }

      final int x1 = x;
      final int y1 = y;
      final int x2 = (x + width) - 1;
      final int y2 = (y + height) - 1;

      this.drawHorizontalLine(x1, x2, y1, color, doAlphaMix);
      this.drawHorizontalLine(x1, x2, y2, color, doAlphaMix);
      this.drawVerticalLine(x1, y1, y2, color, doAlphaMix);
      this.drawVerticalLine(x2, y1, y2, color, doAlphaMix);
   }

   /**
    * Draw round corner rectangle
    * 
    * @param x
    *           X
    * @param y
    *           Y
    * @param width
    *           Width
    * @param height
    *           Height
    * @param arcWidth
    *           Arc width
    * @param arcHeight
    *           Arc height
    * @param color
    *           Color to use
    */
   public void drawRoundRectangle(final int x, final int y, final int width, final int height, final int arcWidth, final int arcHeight, final int color)
   {
      this.drawRoundRectangle(x, y, width, height, arcWidth, arcHeight, color, true);
   }

   /**
    * Draw round corner rectangle
    * 
    * @param x
    *           X
    * @param y
    *           Y
    * @param width
    *           Width
    * @param height
    *           Height
    * @param arcWidth
    *           Arc width
    * @param arcHeight
    *           Arc height
    * @param color
    *           Color to use
    * @param doAlphaMix
    *           Indicates if do alpha mixing or just overwrite
    */
   public void drawRoundRectangle(final int x, final int y, final int width, final int height, final int arcWidth, final int arcHeight, final int color, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      this.drawShape(new RoundRectangle2D.Double(x, y, width, height, arcWidth, arcHeight), color, doAlphaMix);
   }

   /**
    * Draw an empty shape
    * 
    * @param shape
    *           Shape to draw
    * @param color
    *           Color to use
    */
   public void drawShape(final Shape shape, final int color)
   {
      this.drawShape(shape, color, true);
   }

   /**
    * Draw an empty shape
    * 
    * @param shape
    *           Shape to draw
    * @param color
    *           Color to use
    * @param doAlphaMix
    *           Indicates if we do the mixing {@code true}, or we just override {@code false}
    */
   public void drawShape(final Shape shape, final int color, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      final PathIterator pathIterator = shape.getPathIterator(ConstantsGUI.AFFINE_TRANSFORM, ConstantsGUI.FLATNESS);

      final double[] info = new double[6];
      int x = 0;
      int y = 0;
      int xStart = 0;
      int yStart = 0;
      int xx, yy;

      while(pathIterator.isDone() == false)
      {
         switch(pathIterator.currentSegment(info))
         {
            case PathIterator.SEG_MOVETO:
               xStart = x = (int) Math.round(info[0]);
               yStart = y = (int) Math.round(info[1]);

            break;
            case PathIterator.SEG_LINETO:
               xx = (int) Math.round(info[0]);
               yy = (int) Math.round(info[1]);

               this.drawLine(x, y, xx, yy, color, doAlphaMix);

               x = xx;
               y = yy;

            break;
            case PathIterator.SEG_CLOSE:
               this.drawLine(x, y, xStart, yStart, color, doAlphaMix);

               x = xStart;
               y = yStart;

            break;
         }

         pathIterator.next();
      }
   }

   /**
    * Draw a string
    * 
    * @param x
    *           X of top-left
    * @param y
    *           Y of top-left
    * @param string
    *           String to draw
    * @param font
    *           Font to use
    * @param color
    *           Color to use
    */
   public void drawString(final int x, final int y, final String string, final JHelpFont font, final int color)
   {
      this.drawString(x, y, string, font, color, true);
   }

   /**
    * Draw a string
    * 
    * @param x
    *           X of top-left
    * @param y
    *           Y of top-left
    * @param string
    *           String to draw
    * @param font
    *           Font to use
    * @param color
    *           Color to use
    * @param doAlphaMix
    *           Indicates if we do the mixing {@code true}, or we just override {@code false}
    */
   public void drawString(final int x, final int y, final String string, final JHelpFont font, final int color, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      final Shape shape = font.computeShape(string, x, y);
      final Rectangle bounds = shape.getBounds();

      this.drawShape(shape, color, doAlphaMix);

      if(font.isUnderline() == true)
      {
         this.drawHorizontalLine(x, x + bounds.width, font.underlinePosition(string, y), color, doAlphaMix);
      }
   }

   /**
    * Draw a string center on given point
    * 
    * @param x
    *           String center X
    * @param y
    *           String center Y
    * @param string
    *           String to draw
    * @param font
    *           Font to use
    * @param color
    *           Color to use
    */
   public void drawStringCenter(final int x, final int y, final String string, final JHelpFont font, final int color)
   {
      this.drawStringCenter(x, y, string, font, color, true);
   }

   /**
    * Draw a string center on given point
    * 
    * @param x
    *           String center X
    * @param y
    *           String center Y
    * @param string
    *           String to draw
    * @param font
    *           Font to use
    * @param color
    *           Color to use
    * @param doAlphaMix
    *           Indicates if use alpha mix
    */
   public void drawStringCenter(final int x, final int y, final String string, final JHelpFont font, final int color, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      final Shape shape = font.computeShape(string, x, y);
      final Rectangle bounds = shape.getBounds();

      this.drawShapeCenter(shape, color, doAlphaMix);

      if(font.isUnderline() == true)
      {
         this.drawHorizontalLine(x - (bounds.width >> 1), x + (bounds.width >> 1), //
               font.underlinePosition(string, y - (bounds.height >> 1)), color, doAlphaMix);
      }
   }

   /**
    * Draw a vertical line
    * 
    * @param x
    *           X
    * @param y1
    *           Start Y
    * @param y2
    *           End Y
    * @param color
    *           Color to use
    */
   public void drawVerticalLine(final int x, final int y1, final int y2, final int color)
   {
      this.drawVerticalLine(x, y1, y2, color, true);
   }

   /**
    * Draw a vertical line
    * 
    * @param x
    *           X
    * @param y1
    *           Start Y
    * @param y2
    *           End Y
    * @param color
    *           Color to use
    * @param doAlphaMix
    *           Indicates if we do the mixing {@code true}, or we just override {@code false}
    */
   public void drawVerticalLine(final int x, final int y1, final int y2, final int color, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      if((x < 0) || (x >= this.width))
      {
         return;
      }

      final int start = (Math.max(0, Math.min(y1, y2)) * this.width) + x;
      final int end = (Math.min(this.height - 1, Math.max(y1, y2)) * this.width) + x;

      if(start > end)
      {
         return;
      }

      final int alpha = (color >> 24) & 0xFF;

      if((alpha == 0) && (doAlphaMix == true))
      {
         return;
      }

      if((alpha == 255) || (doAlphaMix == false))
      {
         for(int pix = start; pix <= end; pix += this.width)
         {
            this.pixels[pix] = color;
         }

         return;
      }

      final int ahpla = 256 - alpha;
      final int red = ((color >> 16) & 0xFF) * alpha;
      final int green = ((color >> 8) & 0xFF) * alpha;
      final int blue = (color & 0xFF) * alpha;
      int col;

      for(int pix = start; pix <= end; pix += this.width)
      {
         col = this.pixels[pix];

         this.pixels[pix] = (Math.min(255, alpha + ((col >> 24) & 0xFF)) << 24) | //
               (((red + (((col >> 16) & 0xFF) * ahpla)) >> 8) << 16) | //
               (((green + (((col >> 8) & 0xFF) * ahpla)) >> 8) << 8) | //
               ((blue + ((col & 0xFF) * ahpla)) >> 8);
      }
   }

   /**
    * Stop the draw mode and refresh the image
    */
   public void endDrawMode()
   {
      if(this.drawMode == true)
      {
         this.drawMode = false;

         final int length = this.sprites.size();

         for(int index = 0; index < length; index++)
         {
            if(this.visibilities[index] == true)
            {
               this.sprites.get(index).changeVisible(true, false);
            }
         }
      }

      this.update();
   }

   /**
    * Fill an ellipse
    * 
    * @param x
    *           X of bounds top-left
    * @param y
    *           Y of bounds top-left
    * @param width
    *           Ellipse width
    * @param height
    *           Ellipse height
    * @param color
    *           Color to use
    */
   public void fillEllipse(final int x, final int y, final int width, final int height, final int color)
   {
      this.fillEllipse(x, y, width, height, color, true);
   }

   /**
    * Fill an ellipse
    * 
    * @param x
    *           X of bounds top-left
    * @param y
    *           Y of bounds top-left
    * @param width
    *           Ellipse width
    * @param height
    *           Ellipse height
    * @param color
    *           Color to use
    * @param doAlphaMix
    *           Indicates if we do the mixing {@code true}, or we just override {@code false}
    */
   public void fillEllipse(final int x, final int y, final int width, final int height, final int color, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      this.fillShape(new Ellipse2D.Double(x, y, width, height), color, doAlphaMix);
   }

   /**
    * Fill ellipse with a texture
    * 
    * @param x
    *           X of bounds top-left
    * @param y
    *           Y of bounds top-left
    * @param width
    *           Ellipse width
    * @param height
    *           Ellipse height
    * @param texture
    *           Texture to use
    */
   public void fillEllipse(final int x, final int y, final int width, final int height, final JHelpImage texture)
   {
      this.fillEllipse(x, y, width, height, texture, true);
   }

   /**
    * Fill ellipse with a texture
    * 
    * @param x
    *           X of bounds top-left
    * @param y
    *           Y of bounds top-left
    * @param width
    *           Ellipse width
    * @param height
    *           Ellipse height
    * @param texture
    *           Texture to use
    * @param doAlphaMix
    *           Indicates if we do the mixing {@code true}, or we just override {@code false}
    */
   public void fillEllipse(final int x, final int y, final int width, final int height, final JHelpImage texture, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      this.fillShape(new Ellipse2D.Double(x, y, width, height), texture, doAlphaMix);
   }

   /**
    * Fill an ellipse
    * 
    * @param x
    *           X
    * @param y
    *           Y
    * @param width
    *           Width
    * @param height
    *           Height
    * @param paint
    *           Paint to use
    */
   public void fillEllipse(final int x, final int y, final int width, final int height, final JHelpPaint paint)
   {
      this.fillEllipse(x, y, width, height, paint, true);
   }

   /**
    * Fill an ellipse
    * 
    * @param x
    *           X
    * @param y
    *           Y
    * @param width
    *           Width
    * @param height
    *           Height
    * @param paint
    *           Paint to use
    * @param doAlphaMix
    *           Indicates if do alpha mixing or just overwrite
    */
   public void fillEllipse(final int x, final int y, final int width, final int height, final JHelpPaint paint, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      this.fillShape(new Ellipse2D.Double(x, y, width, height), paint, doAlphaMix);
   }

   /**
    * Fill a polygon
    * 
    * @param xs
    *           X list
    * @param offsetX
    *           X list start offset
    * @param ys
    *           Y list
    * @param offsetY
    *           Y list start offset
    * @param length
    *           Number of points
    * @param color
    *           Color to use
    */
   public void fillPolygon(final int[] xs, final int offsetX, final int[] ys, final int offsetY, final int length, final int color)
   {
      this.fillPolygon(xs, offsetX, ys, offsetY, length, color, true);
   }

   /**
    * Fill a polygon
    * 
    * @param xs
    *           X list
    * @param offsetX
    *           X list start offset
    * @param ys
    *           Y list
    * @param offsetY
    *           Y list start offset
    * @param length
    *           Number of points
    * @param color
    *           Color to use
    * @param doAlphaMix
    *           Indicates if we do the mixing {@code true}, or we just override {@code false}
    */
   public void fillPolygon(final int[] xs, int offsetX, final int[] ys, int offsetY, int length, final int color, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      if(offsetX < 0)
      {
         length += offsetX;

         offsetX = 0;
      }

      if(offsetY < 0)
      {
         length += offsetY;

         offsetY = 0;
      }

      length = UtilMath.minIntegers(length, xs.length - offsetX, ys.length - offsetY);

      if(length < 3)
      {
         return;
      }

      final Polygon polygon = new Polygon(Arrays.copyOfRange(xs, offsetX, offsetX + length),//
            Arrays.copyOfRange(ys, offsetY, offsetY + length), length);

      this.fillShape(polygon, color, doAlphaMix);
   }

   /**
    * Fill a polygon
    * 
    * @param xs
    *           X list
    * @param offsetX
    *           X list start offset
    * @param ys
    *           Y list
    * @param offsetY
    *           Y list offset
    * @param length
    *           Number of points
    * @param texture
    *           Texture to use
    */
   public void fillPolygon(final int[] xs, final int offsetX, final int[] ys, final int offsetY, final int length, final JHelpImage texture)
   {
      this.fillPolygon(xs, offsetX, ys, offsetY, length, texture, true);
   }

   /**
    * Fill a polygon
    * 
    * @param xs
    *           X list
    * @param offsetX
    *           X list start offset
    * @param ys
    *           Y list
    * @param offsetY
    *           Y list offset
    * @param length
    *           Number of points
    * @param texture
    *           Texture to use
    * @param doAlphaMix
    *           Indicates if we do the mixing {@code true}, or we just override {@code false}
    */
   public void fillPolygon(final int[] xs, int offsetX, final int[] ys, int offsetY, int length, final JHelpImage texture, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      if(offsetX < 0)
      {
         length += offsetX;

         offsetX = 0;
      }

      if(offsetY < 0)
      {
         length += offsetY;

         offsetY = 0;
      }

      length = UtilMath.minIntegers(length, xs.length - offsetX, ys.length - offsetY);

      if(length < 3)
      {
         return;
      }

      final Polygon polygon = new Polygon(Arrays.copyOfRange(xs, offsetX, offsetX + length),//
            Arrays.copyOfRange(ys, offsetY, offsetY + length), length);

      this.fillShape(polygon, texture, doAlphaMix);
   }

   /**
    * Fill a polygon
    * 
    * @param xs
    *           X coordinates
    * @param offsetX
    *           Start read offset of xs
    * @param ys
    *           Y coordinates
    * @param offsetY
    *           Start read offset of ys
    * @param length
    *           Number of point
    * @param paint
    *           Paint to use
    */
   public void fillPolygon(final int[] xs, final int offsetX, final int[] ys, final int offsetY, final int length, final JHelpPaint paint)
   {
      this.fillPolygon(xs, offsetX, ys, offsetY, length, paint, true);
   }

   /**
    * Fill a polygon
    * 
    * @param xs
    *           X coordinates
    * @param offsetX
    *           Start read offset of xs
    * @param ys
    *           Y coordinates
    * @param offsetY
    *           Start read offset of ys
    * @param length
    *           Number of point
    * @param paint
    *           Paint to use
    * @param doAlphaMix
    *           Indicates if do alpha mixing or just overwrite
    */
   public void fillPolygon(final int[] xs, int offsetX, final int[] ys, int offsetY, int length, final JHelpPaint paint, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      if(offsetX < 0)
      {
         length += offsetX;

         offsetX = 0;
      }

      if(offsetY < 0)
      {
         length += offsetY;

         offsetY = 0;
      }

      length = UtilMath.minIntegers(length, xs.length - offsetX, ys.length - offsetY);

      if(length < 3)
      {
         return;
      }

      final Polygon polygon = new Polygon(Arrays.copyOfRange(xs, offsetX, offsetX + length),//
            Arrays.copyOfRange(ys, offsetY, offsetY + length), length);

      this.fillShape(polygon, paint, doAlphaMix);
   }

   /**
    * Fill a polygon
    * 
    * @param xs
    *           X list
    * @param ys
    *           Y list
    * @param color
    *           Color to use
    */
   public void fillPolygon(final int[] xs, final int[] ys, final int color)
   {
      this.fillPolygon(xs, ys, color, true);
   }

   /**
    * Fill a polygon
    * 
    * @param xs
    *           X list
    * @param ys
    *           Y list
    * @param color
    *           Color to use
    * @param doAlphaMix
    *           Indicates if we do the mixing {@code true}, or we just override {@code false}
    */
   public void fillPolygon(final int[] xs, final int[] ys, final int color, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      this.fillPolygon(xs, 0, ys, 0, Math.min(xs.length, ys.length), color, doAlphaMix);
   }

   /**
    * Fill a polygon
    * 
    * @param xs
    *           X list
    * @param ys
    *           Y list
    * @param texture
    *           Texture to use
    */
   public void fillPolygon(final int[] xs, final int[] ys, final JHelpImage texture)
   {
      this.fillPolygon(xs, ys, texture, true);
   }

   /**
    * Fill a polygon
    * 
    * @param xs
    *           X list
    * @param ys
    *           Y list
    * @param texture
    *           Texture to use
    * @param doAlphaMix
    *           Indicates if we do the mixing {@code true}, or we just override {@code false}
    */
   public void fillPolygon(final int[] xs, final int[] ys, final JHelpImage texture, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      this.fillPolygon(xs, 0, ys, 0, Math.min(xs.length, ys.length), texture, doAlphaMix);
   }

   /**
    * Fill a polygon
    * 
    * @param xs
    *           X coordinates
    * @param ys
    *           Y coordinates
    * @param paint
    *           Paint to use
    */
   public void fillPolygon(final int[] xs, final int[] ys, final JHelpPaint paint)
   {
      this.fillPolygon(xs, ys, paint, true);
   }

   /**
    * Fill a polygon
    * 
    * @param xs
    *           X coordinates
    * @param ys
    *           Y coordinates
    * @param paint
    *           Paint to use
    * @param doAlphaMix
    *           Indicates if we do the mixing {@code true}, or we just override {@code false}
    */
   public void fillPolygon(final int[] xs, final int[] ys, final JHelpPaint paint, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      this.fillPolygon(xs, 0, ys, 0, Math.min(xs.length, ys.length), paint, doAlphaMix);
   }

   /**
    * Fill a rectangle
    * 
    * @param x
    *           X top-left
    * @param y
    *           U top-left
    * @param width
    *           Rectangle width
    * @param height
    *           Rectangle height
    * @param color
    *           Color to use
    */
   public void fillRectangle(final int x, final int y, final int width, final int height, final int color)
   {
      this.fillRectangle(x, y, width, height, color, true);
   }

   /**
    * Fill a rectangle
    * 
    * @param x
    *           X top-left
    * @param y
    *           U top-left
    * @param width
    *           Rectangle width
    * @param height
    *           Rectangle height
    * @param color
    *           Color to use
    * @param doAlphaMix
    *           Indicates if we do the mixing {@code true}, or we just override {@code false}
    */
   public void fillRectangle(final int x, final int y, final int width, final int height, final int color, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      if((width <= 0) || (height <= 0))
      {
         return;
      }

      final int x1 = x;
      final int y1 = y;
      final int x2 = (x + width) - 1;
      final int y2 = (y + height) - 1;

      final int startX = Math.max(0, x1);
      final int endX = Math.min(this.width - 1, x2);
      final int startY = Math.max(0, y1);
      final int endY = Math.min(this.height - 1, y2);

      if((startX > endX) || (startY > endY))
      {
         return;
      }

      final int alpha = (color >> 24) & 0xFF;

      if((alpha == 0) && (doAlphaMix == true))
      {
         return;
      }

      int line = startX + (startY * this.width);
      int pix;

      if((alpha == 255) || (doAlphaMix == false))
      {
         for(int yy = startY; yy <= endY; yy++)
         {
            pix = line;

            for(int xx = startX; xx < endX; xx++)
            {
               this.pixels[pix] = color;

               pix++;
            }

            line += this.width;
         }

         return;
      }

      final int ahpla = 256 - alpha;
      final int red = ((color >> 16) & 0xFF) * alpha;
      final int green = ((color >> 8) & 0xFF) * alpha;
      final int blue = (color & 0xFF) * alpha;
      int col;

      for(int yy = startY; yy <= endY; yy++)
      {
         pix = line;

         for(int xx = startX; xx < endX; xx++)
         {
            col = this.pixels[pix];

            this.pixels[pix] = (Math.min(255, alpha + ((col >> 24) & 0xFF)) << 24) | //
                  (((red + (((col >> 16) & 0xFF) * ahpla)) >> 8) << 16) | //
                  (((green + (((col >> 8) & 0xFF) * ahpla)) >> 8) << 8) | //
                  ((blue + ((col & 0xFF) * ahpla)) >> 8);

            pix++;
         }

         line += this.width;
      }
   }

   /**
    * Fill a rectangle
    * 
    * @param x
    *           X top-left
    * @param y
    *           Y top-left
    * @param width
    *           Rectangle width
    * @param height
    *           Rectangle height
    * @param texture
    *           Texture to use
    */
   public void fillRectangle(final int x, final int y, final int width, final int height, final JHelpImage texture)
   {
      this.fillRectangle(x, y, width, height, texture, true);
   }

   /**
    * Fill a rectangle
    * 
    * @param x
    *           X top-left
    * @param y
    *           Y top-left
    * @param width
    *           Rectangle width
    * @param height
    *           Rectangle height
    * @param texture
    *           Texture to use
    * @param doAlphaMix
    *           Indicates if we do the mixing {@code true}, or we just override {@code false}
    */
   public void fillRectangle(final int x, final int y, final int width, final int height, final JHelpImage texture, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      if((width <= 0) || (height <= 0))
      {
         return;
      }

      final int x1 = x;
      final int y1 = y;
      final int x2 = (x + width) - 1;
      final int y2 = (y + height) - 1;

      final int startX = Math.max(0, x1);
      final int endX = Math.min(this.width - 1, x2);
      final int startY = Math.max(0, y1);
      final int endY = Math.min(this.height - 1, y2);

      if((startX > endX) || (startY > endY))
      {
         return;
      }

      int line = startX + (startY * this.width);
      int pix, color;

      int yTexture = 0;
      int pixTexture, colorTexture;

      int alpha, ahpla;

      for(int yy = startY; yy <= endY; yy++, yTexture = (yTexture + 1) % texture.height)
      {
         pixTexture = yTexture * texture.width;
         pix = line;

         for(int xx = startX, xTexture = 0; xx < endX; xx++, xTexture = (xTexture + 1) % texture.width)
         {
            colorTexture = texture.pixels[pixTexture + xTexture];

            alpha = (colorTexture >> 24) & 0xFF;

            if((alpha == 255) || (doAlphaMix == false))
            {
               this.pixels[pix] = colorTexture;
            }
            else if(alpha > 0)
            {
               ahpla = 256 - alpha;

               color = this.pixels[pix];

               this.pixels[pix] = (Math.min(255, alpha + ((color >> 24) & 0xFF)) << 24) | //
                     ((((((colorTexture >> 16) & 0xFF) * alpha) + (((color >> 16) & 0xFF) * ahpla)) >> 8) << 16) | //
                     ((((((colorTexture >> 8) & 0xFF) * alpha) + (((color >> 8) & 0xFF) * ahpla)) >> 8) << 8) | //
                     ((((colorTexture & 0xFF) * alpha) + ((color & 0xFF) * ahpla)) >> 8);
            }

            pix++;
         }

         line += this.width;
      }
   }

   /**
    * Fill a rectangle
    * 
    * @param x
    *           X
    * @param y
    *           Y
    * @param width
    *           Width
    * @param height
    *           Height
    * @param paint
    *           Paint to use
    */
   public void fillRectangle(final int x, final int y, final int width, final int height, final JHelpPaint paint)
   {
      this.fillRectangle(x, y, width, height, paint, true);
   }

   /**
    * Fill a rectangle
    * 
    * @param x
    *           X
    * @param y
    *           Y
    * @param width
    *           Width
    * @param height
    *           Height
    * @param paint
    *           Paint to use
    * @param doAlphaMix
    *           Indicates if we do the mixing {@code true}, or we just override {@code false}
    */
   public void fillRectangle(final int x, final int y, final int width, final int height, final JHelpPaint paint, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      if((width <= 0) || (height <= 0))
      {
         return;
      }

      final int x1 = x;
      final int y1 = y;
      final int x2 = (x + width) - 1;
      final int y2 = (y + height) - 1;

      final int startX = Math.max(0, x1);
      final int endX = Math.min(this.width - 1, x2);
      final int startY = Math.max(0, y1);
      final int endY = Math.min(this.height - 1, y2);

      if((startX > endX) || (startY > endY))
      {
         return;
      }

      final int w = (endX - startX) + 1;
      final int h = (endY - startY) + 1;
      paint.initializePaint(w, h);

      int line = startX + (startY * this.width);
      int pix, color;

      int yPaint = 0;
      int colorPaint;

      int alpha, ahpla;

      for(int yy = startY; yy <= endY; yy++, yPaint++)
      {
         pix = line;

         for(int xx = startX, xPaint = 0; xx <= endX; xx++, xPaint++)
         {
            colorPaint = paint.obtainColor(xPaint, yPaint);

            alpha = (colorPaint >> 24) & 0xFF;

            if((alpha == 255) || (doAlphaMix == false))
            {
               this.pixels[pix] = colorPaint;
            }
            else if(alpha > 0)
            {
               ahpla = 256 - alpha;

               color = this.pixels[pix];

               this.pixels[pix] = (Math.min(255, alpha + ((color >> 24) & 0xFF)) << 24) | //
                     ((((((colorPaint >> 16) & 0xFF) * alpha) + (((color >> 16) & 0xFF) * ahpla)) >> 8) << 16) | //
                     ((((((colorPaint >> 8) & 0xFF) * alpha) + (((color >> 8) & 0xFF) * ahpla)) >> 8) << 8) | //
                     ((((colorPaint & 0xFF) * alpha) + ((color & 0xFF) * ahpla)) >> 8);
            }

            pix++;
         }

         line += this.width;
      }
   }

   /**
    * Fill a rectangle with an image.<br>
    * The image is scaled to fit rectangle size
    * 
    * @param x
    *           X
    * @param y
    *           Y
    * @param width
    *           Width
    * @param height
    *           Height
    * @param texture
    *           Image to draw
    */
   public void fillRectangleScale(final int x, final int y, final int width, final int height, final JHelpImage texture)
   {
      this.fillRectangleScale(x, y, width, height, texture, true);
   }

   /**
    * Fill a rectangle with an image.<br>
    * The image is scaled to fit rectangle size
    * 
    * @param x
    *           X
    * @param y
    *           Y
    * @param width
    *           Width
    * @param height
    *           Height
    * @param texture
    *           Image to draw
    * @param doAlphaMix
    *           Indicates if we do the mixing {@code true}, or we just override {@code false}
    */
   public void fillRectangleScale(final int x, final int y, final int width, final int height, final JHelpImage texture, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      if((width <= 0) || (height <= 0))
      {
         return;
      }

      final int x1 = x;
      final int y1 = y;
      final int x2 = (x + width) - 1;
      final int y2 = (y + height) - 1;

      final int startX = Math.max(0, x1);
      final int endX = Math.min(this.width - 1, x2);
      final int startY = Math.max(0, y1);
      final int endY = Math.min(this.height - 1, y2);

      if((startX > endX) || (startY > endY))
      {
         return;
      }

      int line = startX + (startY * this.width);
      int pix, color;

      int yTexture = 0;
      int pixTexture, colorTexture;

      int alpha, ahpla;

      final int w = (endX - startX) + 1;
      final int h = (endY - startY) + 1;

      for(int yy = startY, yt = 0; yy <= endY; yy++, yt++, yTexture = (yt * texture.height) / h)
      {
         pixTexture = yTexture * texture.width;
         pix = line;

         for(int xx = startX, xt = 0, xTexture = 0; xx < endX; xx++, xt++, xTexture = (xt * texture.width) / w)
         {
            colorTexture = texture.pixels[pixTexture + xTexture];

            alpha = (colorTexture >> 24) & 0xFF;

            if((alpha == 255) || (doAlphaMix == false))
            {
               this.pixels[pix] = colorTexture;
            }
            else if(alpha > 0)
            {
               ahpla = 256 - alpha;

               color = this.pixels[pix];

               this.pixels[pix] = (Math.min(255, alpha + ((color >> 24) & 0xFF)) << 24) | //
                     ((((((colorTexture >> 16) & 0xFF) * alpha) + (((color >> 16) & 0xFF) * ahpla)) >> 8) << 16) | //
                     ((((((colorTexture >> 8) & 0xFF) * alpha) + (((color >> 8) & 0xFF) * ahpla)) >> 8) << 8) | //
                     ((((colorTexture & 0xFF) * alpha) + ((color & 0xFF) * ahpla)) >> 8);
            }

            pix++;
         }

         line += this.width;
      }
   }

   /**
    * Fill a rectangle with an image.<br>
    * The image is scaled to fit rectangle size.<br>
    * The result is nicer than {@link #fillRectangleScale(int, int, int, int, JHelpImage)} but it is slower and take temporary
    * more memory
    * 
    * @param x
    *           X
    * @param y
    *           Y
    * @param width
    *           Width
    * @param height
    *           Height
    * @param texture
    *           Image to draw
    */
   public void fillRectangleScaleBetter(final int x, final int y, final int width, final int height, final JHelpImage texture)
   {
      this.fillRectangleScaleBetter(x, y, width, height, texture, true);
   }

   /**
    * Fill a rectangle with an image.<br>
    * The image is scaled to fit rectangle size.<br>
    * The result is nicer than {@link #fillRectangleScale(int, int, int, int, JHelpImage, boolean)} but it is slower and take
    * temporary more memory
    * 
    * @param x
    *           X
    * @param y
    *           Y
    * @param width
    *           Width
    * @param height
    *           Height
    * @param texture
    *           Image to draw
    * @param doAlphaMix
    *           Indicates if we do the mixing {@code true}, or we just override {@code false}
    */
   public void fillRectangleScaleBetter(final int x, final int y, final int width, final int height, final JHelpImage texture, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      if((width <= 0) || (height <= 0))
      {
         return;
      }

      final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
      final Graphics2D graphics2d = bufferedImage.createGraphics();

      graphics2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
      graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      graphics2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
      graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
      graphics2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

      graphics2d.drawImage(texture.getImage(), 0, 0, width, height, null);

      int[] pixels = new int[width * height];
      pixels = bufferedImage.getRGB(0, 0, width, height, pixels, 0, width);

      final JHelpImage image = new JHelpImage(width, height, pixels);

      bufferedImage.flush();

      this.fillRectangle(x, y, width, height, image, doAlphaMix);
   }

   /**
    * Fill a round rectangle
    * 
    * @param x
    *           X
    * @param y
    *           Y
    * @param width
    *           Width
    * @param height
    *           Height
    * @param arcWidth
    *           Arc width
    * @param arcHeight
    *           Arc height
    * @param color
    *           Color to use
    */
   public void fillRoundRectangle(final int x, final int y, final int width, final int height, final int arcWidth, final int arcHeight, final int color)
   {
      this.fillRoundRectangle(x, y, width, height, arcWidth, arcHeight, color, true);
   }

   /**
    * Fill a round rectangle
    * 
    * @param x
    *           X
    * @param y
    *           Y
    * @param width
    *           Width
    * @param height
    *           Height
    * @param arcWidth
    *           Arc width
    * @param arcHeight
    *           Arc height
    * @param color
    *           Color to use
    * @param doAlphaMix
    *           Indicates if we do the mixing {@code true}, or we just override {@code false}
    */
   public void fillRoundRectangle(final int x, final int y, final int width, final int height, final int arcWidth, final int arcHeight, final int color, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      this.fillShape(new RoundRectangle2D.Double(x, y, width, height, arcWidth, arcHeight), color, doAlphaMix);
   }

   /**
    * Fill a round rectangle
    * 
    * @param x
    *           X
    * @param y
    *           Y
    * @param width
    *           Width
    * @param height
    *           Height
    * @param arcWidth
    *           Arc width
    * @param arcHeight
    *           Arc height
    * @param texture
    *           Texture to use
    */
   public void fillRoundRectangle(final int x, final int y, final int width, final int height, final int arcWidth, final int arcHeight, final JHelpImage texture)
   {
      this.fillRoundRectangle(x, y, width, height, arcWidth, arcHeight, texture, true);
   }

   /**
    * Fill a round rectangle
    * 
    * @param x
    *           X
    * @param y
    *           Y
    * @param width
    *           Width
    * @param height
    *           Height
    * @param arcWidth
    *           Arc width
    * @param arcHeight
    *           Arc height
    * @param texture
    *           Texture to use
    * @param doAlphaMix
    *           Indicates if we do the mixing {@code true}, or we just override {@code false}
    */
   public void fillRoundRectangle(final int x, final int y, final int width, final int height, final int arcWidth, final int arcHeight, final JHelpImage texture, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      this.fillShape(new RoundRectangle2D.Double(x, y, width, height, arcWidth, arcHeight), texture, doAlphaMix);
   }

   /**
    * Fill a round rectangle
    * 
    * @param x
    *           X
    * @param y
    *           Y
    * @param width
    *           Width
    * @param height
    *           Height
    * @param arcWidth
    *           Arc width
    * @param arcHeight
    *           Arc height
    * @param paint
    *           Paint to use
    */
   public void fillRoundRectangle(final int x, final int y, final int width, final int height, final int arcWidth, final int arcHeight, final JHelpPaint paint)
   {
      this.fillRoundRectangle(x, y, width, height, arcWidth, arcHeight, paint, true);
   }

   /**
    * Fill a round rectangle
    * 
    * @param x
    *           X
    * @param y
    *           Y
    * @param width
    *           Width
    * @param height
    *           Height
    * @param arcWidth
    *           Arc width
    * @param arcHeight
    *           Arc height
    * @param paint
    *           Paint to use
    * @param doAlphaMix
    *           Indicates if we do the mixing {@code true}, or we just override {@code false}
    */
   public void fillRoundRectangle(final int x, final int y, final int width, final int height, final int arcWidth, final int arcHeight, final JHelpPaint paint, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      this.fillShape(new RoundRectangle2D.Double(x, y, width, height, arcWidth, arcHeight), paint, doAlphaMix);
   }

   /**
    * Fill a shape
    * 
    * @param shape
    *           Shape to fill
    * @param color
    *           Color to use
    */
   public void fillShape(final Shape shape, final int color)
   {
      this.fillShape(shape, color, true);
   }

   /**
    * Fill a shape
    * 
    * @param shape
    *           Shape to fill
    * @param color
    *           Color to use
    * @param doAlphaMix
    *           Indicates if we do the mixing {@code true}, or we just override {@code false}
    */
   public void fillShape(final Shape shape, final int color, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      final Rectangle rectangle = shape.getBounds();

      final int x = rectangle.x;
      final int y = rectangle.y;
      final int width = rectangle.width;
      final int height = rectangle.height;

      if((width <= 0) || (height <= 0))
      {
         return;
      }

      final int x1 = x;
      final int y1 = y;
      final int x2 = (x + width) - 1;
      final int y2 = (y + height) - 1;

      final int startX = Math.max(0, x1);
      final int endX = Math.min(this.width - 1, x2);
      final int startY = Math.max(0, y1);
      final int endY = Math.min(this.height - 1, y2);

      if((startX > endX) || (startY > endY))
      {
         return;
      }

      final int alpha = (color >> 24) & 0xFF;

      if((alpha == 0) && (doAlphaMix == true))
      {
         return;
      }

      int line = startX + (startY * this.width);
      int pix;

      if((alpha == 255) || (doAlphaMix == false))
      {
         for(int yy = startY; yy <= endY; yy++)
         {
            pix = line;

            for(int xx = startX; xx <= endX; xx++)
            {
               if(shape.contains(xx, yy) == true)
               {
                  this.pixels[pix] = color;
               }

               pix++;
            }

            line += this.width;
         }

         return;
      }

      final int ahpla = 256 - alpha;
      final int red = ((color >> 16) & 0xFF) * alpha;
      final int green = ((color >> 8) & 0xFF) * alpha;
      final int blue = (color & 0xFF) * alpha;
      int col;

      for(int yy = startY; yy <= endY; yy++)
      {
         pix = line;

         for(int xx = startX; xx <= endX; xx++)
         {
            if(shape.contains(xx, yy) == true)
            {
               col = this.pixels[pix];

               this.pixels[pix] = (Math.min(255, alpha + ((col >> 24) & 0xFF)) << 24) | //
                     (((red + (((col >> 16) & 0xFF) * ahpla)) >> 8) << 16) | //
                     (((green + (((col >> 8) & 0xFF) * ahpla)) >> 8) << 8) | //
                     ((blue + ((col & 0xFF) * ahpla)) >> 8);
            }

            pix++;
         }

         line += this.width;
      }
   }

   /**
    * Fill a shape
    * 
    * @param shape
    *           Shape to fill
    * @param texture
    *           Texture to use
    */
   public void fillShape(final Shape shape, final JHelpImage texture)
   {
      this.fillShape(shape, texture, true);
   }

   /**
    * Fill a shape
    * 
    * @param shape
    *           Shape to fill
    * @param texture
    *           Texture to use
    * @param doAlphaMix
    *           Indicates if we do the mixing {@code true}, or we just override {@code false}
    */
   public void fillShape(final Shape shape, final JHelpImage texture, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      final Rectangle rectangle = shape.getBounds();

      final int x = rectangle.x;
      final int y = rectangle.y;
      final int width = rectangle.width;
      final int height = rectangle.height;

      if((width <= 0) || (height <= 0))
      {
         return;
      }

      final int x1 = x;
      final int y1 = y;
      final int x2 = (x + width) - 1;
      final int y2 = (y + height) - 1;

      final int startX = Math.max(0, x1);
      final int endX = Math.min(this.width - 1, x2);
      final int startY = Math.max(0, y1);
      final int endY = Math.min(this.height - 1, y2);

      if((startX > endX) || (startY > endY))
      {
         return;
      }

      int line = startX + (startY * this.width);
      int pix, color;

      int yTexture = 0;
      int pixTexture, colorTexture;

      int alpha, ahpla;

      for(int yy = startY; yy <= endY; yy++, yTexture = (yTexture + 1) % texture.height)
      {
         pixTexture = yTexture * texture.width;
         pix = line;

         for(int xx = startX, xTexture = 0; xx <= endX; xx++, xTexture = (xTexture + 1) % texture.width)
         {
            if(shape.contains(xx, yy) == true)
            {
               colorTexture = texture.pixels[pixTexture + xTexture];

               alpha = (colorTexture >> 24) & 0xFF;

               if((alpha == 255) || (doAlphaMix == false))
               {
                  this.pixels[pix] = colorTexture;
               }
               else if(alpha > 0)
               {
                  ahpla = 256 - alpha;

                  color = this.pixels[pix];

                  this.pixels[pix] = (Math.min(255, alpha + ((color >> 24) & 0xFF)) << 24) | //
                        ((((((colorTexture >> 16) & 0xFF) * alpha) + (((color >> 16) & 0xFF) * ahpla)) >> 8) << 16) | //
                        ((((((colorTexture >> 8) & 0xFF) * alpha) + (((color >> 8) & 0xFF) * ahpla)) >> 8) << 8) | //
                        ((((colorTexture & 0xFF) * alpha) + ((color & 0xFF) * ahpla)) >> 8);
               }
            }

            pix++;
         }

         line += this.width;
      }
   }

   /**
    * Fill a shape
    * 
    * @param shape
    *           Shape to fill
    * @param paint
    *           Paint to use
    */
   public void fillShape(final Shape shape, final JHelpPaint paint)
   {
      this.fillShape(shape, paint, true);
   }

   /**
    * Fill a shape
    * 
    * @param shape
    *           Shape to fill
    * @param paint
    *           Paint to use
    * @param doAlphaMix
    *           Indicates if we do the mixing {@code true}, or we just override {@code false}
    */
   public void fillShape(final Shape shape, final JHelpPaint paint, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      final Rectangle rectangle = shape.getBounds();

      final int x = rectangle.x;
      final int y = rectangle.y;
      final int width = rectangle.width;
      final int height = rectangle.height;

      if((width <= 0) || (height <= 0))
      {
         return;
      }

      final int x1 = x;
      final int y1 = y;
      final int x2 = (x + width) - 1;
      final int y2 = (y + height) - 1;

      final int startX = Math.max(0, x1);
      final int endX = Math.min(this.width - 1, x2);
      final int startY = Math.max(0, y1);
      final int endY = Math.min(this.height - 1, y2);

      if((startX > endX) || (startY > endY))
      {
         return;
      }

      final int w = (endX - startX) + 1;
      final int h = (endY - startY) + 1;
      paint.initializePaint(w, h);

      int line = startX + (startY * this.width);
      int pix, color;

      int yPaint = 0;
      int colorPaint;

      int alpha, ahpla;

      for(int yy = startY; yy <= endY; yy++, yPaint++)
      {
         pix = line;

         for(int xx = startX, xPaint = 0; xx <= endX; xx++, xPaint++)
         {
            if(shape.contains(xx, yy) == true)
            {
               colorPaint = paint.obtainColor(xPaint, yPaint);

               alpha = (colorPaint >> 24) & 0xFF;

               if((alpha == 255) || (doAlphaMix == false))
               {
                  this.pixels[pix] = colorPaint;
               }
               else if(alpha > 0)
               {
                  ahpla = 256 - alpha;

                  color = this.pixels[pix];

                  this.pixels[pix] = (Math.min(255, alpha + ((color >> 24) & 0xFF)) << 24) | //
                        ((((((colorPaint >> 16) & 0xFF) * alpha) + (((color >> 16) & 0xFF) * ahpla)) >> 8) << 16) | //
                        ((((((colorPaint >> 8) & 0xFF) * alpha) + (((color >> 8) & 0xFF) * ahpla)) >> 8) << 8) | //
                        ((((colorPaint & 0xFF) * alpha) + ((color & 0xFF) * ahpla)) >> 8);
               }
            }

            pix++;
         }

         line += this.width;
      }
   }

   /**
    * Fill a string
    * 
    * @param x
    *           X top-left
    * @param y
    *           Y top-left
    * @param string
    *           String to draw
    * @param font
    *           Font to use
    * @param color
    *           Color for fill
    */
   public void fillString(final int x, final int y, final String string, final JHelpFont font, final int color)
   {
      this.fillString(x, y, string, font, color, true);
   }

   /**
    * Fill a string
    * 
    * @param x
    *           X top-left
    * @param y
    *           Y top-left
    * @param string
    *           String to draw
    * @param font
    *           Font to use
    * @param color
    *           Color for fill
    * @param doAlphaMix
    *           Indicates if we do the mixing {@code true}, or we just override {@code false}
    */
   public void fillString(final int x, final int y, final String string, final JHelpFont font, final int color, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      final Shape shape = font.computeShape(string, x, y);

      this.fillShape(shape, color, doAlphaMix);

      if(font.isUnderline() == true)
      {
         this.drawHorizontalLine(x, x + shape.getBounds().width, font.underlinePosition(string, y), color, doAlphaMix);
      }
   }

   /**
    * Fill a string
    * 
    * @param x
    *           X top-left
    * @param y
    *           Y top-left
    * @param string
    *           String to fill
    * @param font
    *           Font to use
    * @param texture
    *           Texture to use
    * @param color
    *           Color if underline
    */
   public void fillString(final int x, final int y, final String string, final JHelpFont font, final JHelpImage texture, final int color)
   {
      this.fillString(x, y, string, font, texture, color, true);
   }

   /**
    * Fill a string
    * 
    * @param x
    *           X top-left
    * @param y
    *           Y top-left
    * @param string
    *           String to fill
    * @param font
    *           Font to use
    * @param texture
    *           Texture to use
    * @param color
    *           Color if underline
    * @param doAlphaMix
    *           Indicates if we do the mixing {@code true}, or we just override {@code false}
    */
   public void fillString(final int x, final int y, final String string, final JHelpFont font, final JHelpImage texture, final int color, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      final Shape shape = font.computeShape(string, x, y);

      this.fillShape(shape, texture, doAlphaMix);

      if(font.isUnderline() == true)
      {
         this.drawHorizontalLine(x, x + shape.getBounds().width, font.underlinePosition(string, y), color, doAlphaMix);
      }
   }

   /**
    * Fill a string
    * 
    * @param x
    *           X
    * @param y
    *           Y
    * @param string
    *           String to fill
    * @param font
    *           Font to use
    * @param paint
    *           Paint to use
    * @param color
    *           Color for underline
    */
   public void fillString(final int x, final int y, final String string, final JHelpFont font, final JHelpPaint paint, final int color)
   {
      this.fillString(x, y, string, font, paint, color, true);
   }

   /**
    * Fill a string
    * 
    * @param x
    *           X
    * @param y
    *           Y
    * @param string
    *           String to fill
    * @param font
    *           Font to use
    * @param paint
    *           Paint to use
    * @param color
    *           Color for underline
    * @param doAlphaMix
    *           Indicates if we do the mixing {@code true}, or we just override {@code false}
    */
   public void fillString(final int x, final int y, final String string, final JHelpFont font, final JHelpPaint paint, final int color, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      final Shape shape = font.computeShape(string, x, y);

      this.fillShape(shape, paint, doAlphaMix);

      if(font.isUnderline() == true)
      {
         this.drawHorizontalLine(x, x + shape.getBounds().width, font.underlinePosition(string, y), color, doAlphaMix);
      }
   }

   /**
    * Filter image on blue channel
    */
   public void filterBlue()
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      int color, blue;

      for(int pix = this.pixels.length - 1; pix >= 0; pix--)
      {
         color = this.pixels[pix];
         blue = color & 0xFF;

         this.pixels[pix] = (color & 0xFF0000FF) | (blue << 16) | (blue << 8);
      }
   }

   /**
    * Filter image on green channel
    */
   public void filterGreen()
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      int color, green;

      for(int pix = this.pixels.length - 1; pix >= 0; pix--)
      {
         color = this.pixels[pix];
         green = (color >> 8) & 0xFF;

         this.pixels[pix] = (color & 0xFF00FF00) | (green << 16) | green;
      }
   }

   /**
    * Filter image on a specific color
    * 
    * @param color
    *           Color search
    * @param colorOK
    *           Color to use if corresponds
    * @param colorKO
    *           Colo to use if failed
    */
   public void filterOn(final int color, final int colorOK, final int colorKO)
   {
      for(int i = this.pixels.length - 1; i >= 0; i--)
      {
         if(color == this.pixels[i])
         {
            this.pixels[i] = colorOK;
         }
         else
         {
            this.pixels[i] = colorKO;
         }
      }
   }

   /**
    * filter image on a specific color
    * 
    * @param color
    *           Color search
    * @param precision
    *           Precision to use
    * @param colorOK
    *           Color if corresponds
    * @param colorKO
    *           Color if not corresponds
    */
   public void filterOn(final int color, final int precision, final int colorOK, final int colorKO)
   {
      final Color refrence = new Color(color);

      for(int i = this.pixels.length - 1; i >= 0; i--)
      {
         if(refrence.isNear(new Color(this.pixels[i]), precision) == true)
         {
            this.pixels[i] = colorOK;
         }
         else
         {
            this.pixels[i] = colorKO;
         }
      }
   }

   /**
    * Filter on using a palette color
    * 
    * @param index
    *           Palette color indes
    * @param colorOK
    *           Color if match
    * @param colorKO
    *           Color if not match
    */
   public void filterPalette(final int index, final int colorOK, final int colorKO)
   {
      this.filterOn(JHelpImage.PALETTE[index % JHelpImage.PALETTE_SIZE], 0x10, colorOK, colorKO);
   }

   /**
    * Filter image on red channel
    */
   public void filterRed()
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      int color, red;

      for(int pix = this.pixels.length - 1; pix >= 0; pix--)
      {
         color = this.pixels[pix];
         red = (color >> 16) & 0xFF;

         this.pixels[pix] = (color & 0xFFFF0000) | (red << 8) | red;
      }
   }

   /**
    * Filter image on U part
    */
   public void filterU()
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      int color;
      int u;

      for(int pix = this.pixels.length - 1; pix >= 0; pix--)
      {
         color = this.pixels[pix];

         u = UtilMath.limit0_255((int) JHelpImage.computeU((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF));

         this.pixels[pix] = (color & 0xFF000000) | (u << 16) | (u << 8) | u;
      }
   }

   /**
    * Filter image on V part
    */
   public void filterV()
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      int color;
      int v;

      for(int pix = this.pixels.length - 1; pix >= 0; pix--)
      {
         color = this.pixels[pix];

         v = UtilMath.limit0_255((int) JHelpImage.computeV((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF));

         this.pixels[pix] = (color & 0xFF000000) | (v << 16) | (v << 8) | v;
      }
   }

   /**
    * Filter image on Y part
    */
   public void filterY()
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      int color;
      int y;

      for(int pix = this.pixels.length - 1; pix >= 0; pix--)
      {
         color = this.pixels[pix];

         y = UtilMath.limit0_255((int) JHelpImage.computeY((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF));

         this.pixels[pix] = (color & 0xFF000000) | (y << 16) | (y << 8) | y;
      }
   }

   /**
    * Image height
    * 
    * @return Image height
    */
   public int getHeight()
   {
      return this.height;
   }

   /**
    * Image for draw in graphics environment
    * 
    * @return Image for draw in graphics environment
    */
   public Image getImage()
   {
      return this.image;
   }

   /**
    * Image name
    * 
    * @return Image name
    */
   public String getName()
   {
      return this.name;
   }

   /**
    * Image width
    * 
    * @return Image width
    */
   public int getWidth()
   {
      return this.width;
   }

   /**
    * Convert image in gray version
    */
   public void gray()
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      int color;
      int y;
      for(int i = this.pixels.length - 1; i >= 0; i--)
      {
         color = this.pixels[i];

         y = UtilMath.limit0_255((int) (JHelpImage.computeY((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF)));

         this.pixels[i] = (color & 0xFF000000) | (y << 16) | (y << 8) | y;
      }
   }

   /**
    * Convert image in gray invert version
    */
   public void grayInvert()
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      int color;
      int y;
      for(int i = this.pixels.length - 1; i >= 0; i--)
      {
         color = this.pixels[i];

         y = 255 - UtilMath.limit0_255((int) (JHelpImage.computeY((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF)));

         this.pixels[i] = (color & 0xFF000000) | (y << 16) | (y << 8) | y;
      }
   }

   /**
    * Invert image colors
    */
   public void invertColors()
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      int color;
      for(int i = this.pixels.length - 1; i >= 0; i--)
      {
         color = this.pixels[i];

         this.pixels[i] = (color & 0xFF000000) | //
               ((255 - ((color >> 16) & 0xFF)) << 16) | //
               ((255 - ((color >> 8) & 0xFF)) << 8) | //
               (255 - (color & 0xFF));
      }
   }

   /**
    * Invert U and V parts
    */
   public void invertUV()
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      int color, red, green, blue;
      double y, u, v;

      for(int i = this.pixels.length - 1; i >= 0; i--)
      {
         color = this.pixels[i];
         red = (color >> 16) & 0xFF;
         green = (color >> 8) & 0xFF;
         blue = color & 0xFF;

         y = JHelpImage.computeY(red, green, blue);
         u = JHelpImage.computeU(red, green, blue);
         v = JHelpImage.computeV(red, green, blue);

         this.pixels[i] = (color & 0xFF000000) | (JHelpImage.computeRed(y, v, u) << 16) | (JHelpImage.computeGreen(y, v, u) << 8) | JHelpImage.computeBlue(y, v, u);
      }
   }

   /**
    * Indicates if we are in draw mode
    * 
    * @return Draw mode status
    */
   public boolean isDrawMode()
   {
      return this.drawMode;
   }

   /**
    * Take the maximum between this image and given one
    * 
    * @param image
    *           Image reference
    */
   public void maximum(final JHelpImage image)
   {
      if((this.width != image.width) || (this.height != image.height))
      {
         throw new IllegalArgumentException("We can only multiply with an image of same size");
      }

      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      int colorThis, colorImage;

      for(int pix = this.pixels.length - 1; pix >= 0; pix--)
      {
         colorThis = this.pixels[pix];
         colorImage = image.pixels[pix];

         this.pixels[pix] = (colorThis & 0xFF000000) | //
               (Math.max((colorThis >> 16) & 0xFF, (colorImage >> 16) & 0xFF) << 16) | //
               (Math.max((colorThis >> 8) & 0xFF, (colorImage >> 8) & 0xFF) << 8) | //
               Math.max(colorThis & 0xFF, colorImage & 0xFF);
      }
   }

   /**
    * Take the middle between this image and given one
    * 
    * @param image
    *           Image reference
    */
   public void middle(final JHelpImage image)
   {
      if((this.width != image.width) || (this.height != image.height))
      {
         throw new IllegalArgumentException("We can only multiply with an image of same size");
      }

      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      int colorThis, colorImage;

      for(int pix = this.pixels.length - 1; pix >= 0; pix--)
      {
         colorThis = this.pixels[pix];
         colorImage = image.pixels[pix];

         this.pixels[pix] = (colorThis & 0xFF000000) | //
               (((((colorThis >> 16) & 0xFF) + ((colorImage >> 16) & 0xFF)) >> 1) << 16) | //
               (((((colorThis >> 8) & 0xFF) + ((colorImage >> 8) & 0xFF)) >> 1) << 8) | //
               (((colorThis & 0xFF) + (colorImage & 0xFF)) >> 1);
      }
   }

   /**
    * Take the minimum between this image and given one
    * 
    * @param image
    *           Image reference
    */
   public void minimum(final JHelpImage image)
   {
      if((this.width != image.width) || (this.height != image.height))
      {
         throw new IllegalArgumentException("We can only multiply with an image of same size");
      }

      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      int colorThis, colorImage;

      for(int pix = this.pixels.length - 1; pix >= 0; pix--)
      {
         colorThis = this.pixels[pix];
         colorImage = image.pixels[pix];

         this.pixels[pix] = (colorThis & 0xFF000000) | //
               (Math.min((colorThis >> 16) & 0xFF, (colorImage >> 16) & 0xFF) << 16) | //
               (Math.min((colorThis >> 8) & 0xFF, (colorImage >> 8) & 0xFF) << 8) | //
               Math.min(colorThis & 0xFF, colorImage & 0xFF);
      }
   }

   /**
    * Multiply the image with an other one
    * 
    * @param image
    *           Image to multiply
    */
   public void multiply(final JHelpImage image)
   {
      if((this.width != image.width) || (this.height != image.height))
      {
         throw new IllegalArgumentException("We can only multiply with an image of same size");
      }

      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      int colorThis, colorImage;

      for(int pix = this.pixels.length - 1; pix >= 0; pix--)
      {
         colorThis = this.pixels[pix];
         colorImage = image.pixels[pix];

         this.pixels[pix] = (colorThis & 0xFF000000) | //
               (((((colorThis >> 16) & 0xFF) * ((colorImage >> 16) & 0xFF)) / 255) << 16) | //
               (((((colorThis >> 8) & 0xFF) * ((colorImage >> 8) & 0xFF)) / 255) << 8) | //
               (((colorThis & 0xFF) * (colorImage & 0xFF)) / 255);
      }
   }

   /**
    * Paint a mask
    * 
    * @param x
    *           X
    * @param y
    *           Y
    * @param mask
    *           Mask to paint
    * @param foreground
    *           Foreground color
    * @param background
    *           Background color
    * @param doAlphaMix
    *           Indicates if do alpha mixing ({@code true}) or just overwrite ({@code false})
    */
   public void paintMask(int x, int y, final JHelpMask mask, final int foreground, final int background, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      int w = this.width;
      int xx = 0;
      if(x < 0)
      {
         xx = -x;
         w += x;
         x = 0;
      }

      int h = this.height;
      int yy = 0;
      if(y < 0)
      {
         yy = -y;
         h += y;
         y = 0;
      }

      final int width = UtilMath.minIntegers(w - x, mask.getWidth());
      final int height = UtilMath.minIntegers(h - y, mask.getHeight());

      if((width < 1) || (height < 1))
      {
         return;
      }

      int line = x + (y * this.width);
      int pix, color, col, alpha, ahpla, red, blue, green;

      final int alphaFore = (foreground >> 24) & 0xFF;
      final int redFore = ((foreground >> 16) & 0xFF) * alphaFore;
      final int greenFore = ((foreground >> 8) & 0xFF) * alphaFore;
      final int blueFore = (foreground & 0xFF) * alphaFore;

      final int alphaBack = (background >> 24) & 0xFF;
      final int redBack = ((background >> 16) & 0xFF) * alphaBack;
      final int greenBack = ((background >> 8) & 0xFF) * alphaBack;
      final int blueBack = (background & 0xFF) * alphaBack;

      for(int yyy = yy; yyy < height; yyy++)
      {
         pix = line;

         for(int xxx = xx; xxx < width; xxx++)
         {
            if(mask.getValue(xxx, yyy) == true)
            {
               color = foreground;
               alpha = alphaFore;
               red = redFore;
               green = greenFore;
               blue = blueFore;
            }
            else
            {
               color = background;
               alpha = alphaBack;
               red = redBack;
               green = greenBack;
               blue = blueBack;
            }

            if((alpha == 255) || (doAlphaMix == false))
            {
               this.pixels[pix] = color;
            }
            else if(alpha > 0)
            {
               ahpla = 256 - alpha;
               col = this.pixels[pix];

               this.pixels[pix] = (Math.min(255, alpha + ((col >> 24) & 0xFF)) << 24) | //
                     (((red + (((col >> 16) & 0xFF) * ahpla)) >> 8) << 16) | //
                     (((green + (((col >> 8) & 0xFF) * ahpla)) >> 8) << 8) | //
                     ((blue + ((col & 0xFF) * ahpla)) >> 8);
            }

            pix++;
         }

         line += this.width;
      }
   }

   /**
    * Paint a mask with unify foreground color and part of image in background
    * 
    * @param x
    *           X
    * @param y
    *           Y
    * @param mask
    *           Mask to paint
    * @param foreground
    *           Foreground color
    * @param background
    *           Background image
    * @param backgroundX
    *           X start in background image
    * @param backgroundY
    *           Y start in background image
    * @param doAlphaMix
    *           Indicates if do alpha mixing ({@code true}) or just overwrite ({@code false})
    */
   public void paintMask(int x, int y, final JHelpMask mask, final int foreground, final JHelpImage background, int backgroundX, int backgroundY, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      int bw = background.width;
      if(backgroundX < 0)
      {
         bw += backgroundX;
         backgroundX = 0;
      }

      int bh = background.height;
      if(backgroundY < 0)
      {
         bh += backgroundY;
         backgroundY = 0;
      }

      int w = this.width;
      int xx = 0;
      if(x < 0)
      {
         xx = -x;
         w += x;
         x = 0;
      }

      int h = this.height;
      int yy = 0;
      if(y < 0)
      {
         yy = -y;
         h += y;
         y = 0;
      }

      final int width = UtilMath.minIntegers(w - x, mask.getWidth(), bw - backgroundX);
      final int height = UtilMath.minIntegers(h - y, mask.getHeight(), bh - backgroundY);

      if((width < 1) || (height < 1))
      {
         return;
      }

      int lineBack = backgroundX + (backgroundY * background.width);
      int line = x + (y * this.width);
      int pixBack, pix, color, col, alpha, ahpla;

      for(int yyy = yy; yyy < height; yyy++)
      {
         pixBack = lineBack;
         pix = line;

         for(int xxx = xx; xxx < width; xxx++)
         {
            color = mask.getValue(xxx, yyy)
                  ? foreground
                  : background.pixels[pixBack];
            alpha = (color >> 24) & 0xFF;

            if((alpha == 255) || (doAlphaMix == false))
            {
               this.pixels[pix] = color;
            }
            else if(alpha > 0)
            {
               ahpla = 256 - alpha;
               col = this.pixels[pix];

               this.pixels[pix] = (Math.min(255, alpha + ((col >> 24) & 0xFF)) << 24) | //
                     ((((((color >> 16) & 0xFF) * alpha) + (((col >> 16) & 0xFF) * ahpla)) >> 8) << 16) | //
                     ((((((color >> 8) & 0xFF) * alpha) + (((col >> 8) & 0xFF) * ahpla)) >> 8) << 8) | //
                     ((((color & 0xFF) * alpha) + ((col & 0xFF) * ahpla)) >> 8);
            }

            pixBack++;
            pix++;
         }

         lineBack += background.width;
         line += this.width;
      }
   }

   /**
    * Paint a mask with unify color as foreground and paint as background
    * 
    * @param x
    *           X
    * @param y
    *           Y
    * @param mask
    *           Mask to paint
    * @param foreground
    *           Foreground color
    * @param background
    *           Background paint
    * @param doAlphaMix
    *           Indicates if do alpha mixing ({@code true}) or just overwrite ({@code false})
    */
   public void paintMask(int x, int y, final JHelpMask mask, final int foreground, final JHelpPaint background, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      int w = this.width;
      int xx = 0;
      if(x < 0)
      {
         xx = -x;
         w += x;
         x = 0;
      }

      int h = this.height;
      int yy = 0;
      if(y < 0)
      {
         yy = -y;
         h += y;
         y = 0;
      }

      final int width = UtilMath.minIntegers(w - x, mask.getWidth());
      final int height = UtilMath.minIntegers(h - y, mask.getHeight());

      if((width < 1) || (height < 1))
      {
         return;
      }

      background.initializePaint(width, height);

      int line = x + (y * this.width);
      int pix, color, col, alpha, ahpla;

      for(int yyy = yy; yyy < height; yyy++)
      {
         pix = line;

         for(int xxx = xx; xxx < width; xxx++)
         {
            color = mask.getValue(xxx, yyy)
                  ? foreground
                  : background.obtainColor(xxx, yyy);
            alpha = (color >> 24) & 0xFF;

            if((alpha == 255) || (doAlphaMix == false))
            {
               this.pixels[pix] = color;
            }
            else if(alpha > 0)
            {
               ahpla = 256 - alpha;
               col = this.pixels[pix];

               this.pixels[pix] = (Math.min(255, alpha + ((col >> 24) & 0xFF)) << 24) | //
                     ((((((color >> 16) & 0xFF) * alpha) + (((col >> 16) & 0xFF) * ahpla)) >> 8) << 16) | //
                     ((((((color >> 8) & 0xFF) * alpha) + (((col >> 8) & 0xFF) * ahpla)) >> 8) << 8) | //
                     ((((color & 0xFF) * alpha) + ((col & 0xFF) * ahpla)) >> 8);
            }

            pix++;
         }

         line += this.width;
      }
   }

   /**
    * Paint a mask with part of image as foreground and unify color as background
    * 
    * @param x
    *           X
    * @param y
    *           Y
    * @param mask
    *           Mask to paint
    * @param foreground
    *           Foreground image
    * @param foregroundX
    *           X start on foreground image
    * @param foregroundY
    *           Y start on foreground image
    * @param background
    *           Background color
    * @param doAlphaMix
    *           Indicates if do alpha mixing ({@code true}) or just overwrite ({@code false})
    */
   public void paintMask(int x, int y, final JHelpMask mask, final JHelpImage foreground, int foregroundX, int foregroundY, final int background, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      int fw = foreground.width;
      if(foregroundX < 0)
      {
         fw += foregroundX;
         foregroundX = 0;
      }

      int fh = foreground.height;
      if(foregroundY < 0)
      {
         fh += foregroundY;
         foregroundY = 0;
      }

      int w = this.width;
      int xx = 0;
      if(x < 0)
      {
         xx = -x;
         w += x;
         x = 0;
      }

      int h = this.height;
      int yy = 0;
      if(y < 0)
      {
         yy = -y;
         h += y;
         y = 0;
      }

      final int width = UtilMath.minIntegers(w - x, mask.getWidth(), fw - foregroundX);
      final int height = UtilMath.minIntegers(h - y, mask.getHeight(), fh - foregroundY);

      if((width < 1) || (height < 1))
      {
         return;
      }

      int lineFore = foregroundX + (foregroundY * foreground.width);
      int line = x + (y * this.width);
      int pixFore, pix, color, col, alpha, ahpla;

      for(int yyy = yy; yyy < height; yyy++)
      {
         pixFore = lineFore;
         pix = line;

         for(int xxx = xx; xxx < width; xxx++)
         {
            color = mask.getValue(xxx, yyy)
                  ? foreground.pixels[pixFore]
                  : background;
            alpha = (color >> 24) & 0xFF;

            if((alpha == 255) || (doAlphaMix == false))
            {
               this.pixels[pix] = color;
            }
            else if(alpha > 0)
            {
               ahpla = 256 - alpha;
               col = this.pixels[pix];

               this.pixels[pix] = (Math.min(255, alpha + ((col >> 24) & 0xFF)) << 24) | //
                     ((((((color >> 16) & 0xFF) * alpha) + (((col >> 16) & 0xFF) * ahpla)) >> 8) << 16) | //
                     ((((((color >> 8) & 0xFF) * alpha) + (((col >> 8) & 0xFF) * ahpla)) >> 8) << 8) | //
                     ((((color & 0xFF) * alpha) + ((col & 0xFF) * ahpla)) >> 8);
            }

            pixFore++;
            pix++;
         }

         lineFore += foreground.width;
         line += this.width;
      }
   }

   /**
    * Paint a mask with 2 images, one for "foreground" pixels, one for "background" ones
    * 
    * @param x
    *           X position for the mask
    * @param y
    *           Y position for the mask
    * @param mask
    *           Mask to paint
    * @param foreground
    *           Foreground image
    * @param foregroundX
    *           X start position in foreground image
    * @param foregroundY
    *           Y start position in foreground image
    * @param background
    *           Background image
    * @param backgroundX
    *           X start position in background image
    * @param backgroundY
    *           Y start position in background image
    * @param doAlphaMix
    *           Indicates if do alpha mixing ({@code true}) or just overwrite ({@code false})
    */
   public void paintMask(int x, int y, final JHelpMask mask, final JHelpImage foreground, int foregroundX, int foregroundY, final JHelpImage background, int backgroundX, int backgroundY, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      int fw = foreground.width;
      if(foregroundX < 0)
      {
         fw += foregroundX;
         foregroundX = 0;
      }

      int fh = foreground.height;
      if(foregroundY < 0)
      {
         fh += foregroundY;
         foregroundY = 0;
      }

      int bw = background.width;
      if(backgroundX < 0)
      {
         bw += backgroundX;
         backgroundX = 0;
      }

      int bh = background.height;
      if(backgroundY < 0)
      {
         bh += backgroundY;
         backgroundY = 0;
      }

      int w = this.width;
      int xx = 0;
      if(x < 0)
      {
         xx = -x;
         w += x;
         x = 0;
      }

      int h = this.height;
      int yy = 0;
      if(y < 0)
      {
         yy = -y;
         h += y;
         y = 0;
      }

      final int width = UtilMath.minIntegers(w - x, mask.getWidth(), fw - foregroundX, bw - backgroundX);
      final int height = UtilMath.minIntegers(h - y, mask.getHeight(), fh - foregroundY, bh - backgroundY);

      if((width < 1) || (height < 1))
      {
         return;
      }

      int lineFore = foregroundX + (foregroundY * foreground.width);
      int lineBack = backgroundX + (backgroundY * background.width);
      int line = x + (y * this.width);
      int pixFore, pixBack, pix, color, col, alpha, ahpla;

      for(int yyy = yy; yyy < height; yyy++)
      {
         pixFore = lineFore;
         pixBack = lineBack;
         pix = line;

         for(int xxx = xx; xxx < width; xxx++)
         {
            color = mask.getValue(xxx, yyy)
                  ? foreground.pixels[pixFore]
                  : background.pixels[pixBack];
            alpha = (color >> 24) & 0xFF;

            if((alpha == 255) || (doAlphaMix == false))
            {
               this.pixels[pix] = color;
            }
            else if(alpha > 0)
            {
               ahpla = 256 - alpha;
               col = this.pixels[pix];

               this.pixels[pix] = (Math.min(255, alpha + ((col >> 24) & 0xFF)) << 24) | //
                     ((((((color >> 16) & 0xFF) * alpha) + (((col >> 16) & 0xFF) * ahpla)) >> 8) << 16) | //
                     ((((((color >> 8) & 0xFF) * alpha) + (((col >> 8) & 0xFF) * ahpla)) >> 8) << 8) | //
                     ((((color & 0xFF) * alpha) + ((col & 0xFF) * ahpla)) >> 8);
            }

            pixFore++;
            pixBack++;
            pix++;
         }

         lineFore += foreground.width;
         lineBack += background.width;
         line += this.width;
      }
   }

   /**
    * Paint a mask with image in foreground and paint in background
    * 
    * @param x
    *           X where paint the mask
    * @param y
    *           Y where paint the mask
    * @param mask
    *           Mask to paint
    * @param foreground
    *           Image in foreground
    * @param foregroundX
    *           X start on foreground image
    * @param foregroundY
    *           Y start on foreground image
    * @param background
    *           Background paint
    * @param doAlphaMix
    *           Indicates if do alpha mixing ({@code true}) or just overwrite ({@code false})
    */
   public void paintMask(int x, int y, final JHelpMask mask, final JHelpImage foreground, int foregroundX, int foregroundY, final JHelpPaint background, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      int fw = foreground.width;
      if(foregroundX < 0)
      {
         fw += foregroundX;
         foregroundX = 0;
      }

      int fh = foreground.height;
      if(foregroundY < 0)
      {
         fh += foregroundY;
         foregroundY = 0;
      }

      int w = this.width;
      int xx = 0;
      if(x < 0)
      {
         xx = -x;
         w += x;
         x = 0;
      }

      int h = this.height;
      int yy = 0;
      if(y < 0)
      {
         yy = -y;
         h += y;
         y = 0;
      }

      final int width = UtilMath.minIntegers(w - x, mask.getWidth(), fw - foregroundX);
      final int height = UtilMath.minIntegers(h - y, mask.getHeight(), fh - foregroundY);

      if((width < 1) || (height < 1))
      {
         return;
      }

      int lineFore = foregroundX + (foregroundY * foreground.width);
      background.initializePaint(width, height);
      int line = x + (y * this.width);
      int pixFore, pix, color, col, alpha, ahpla;

      for(int yyy = yy; yyy < height; yyy++)
      {
         pixFore = lineFore;
         pix = line;

         for(int xxx = xx; xxx < width; xxx++)
         {
            color = mask.getValue(xxx, yyy)
                  ? foreground.pixels[pixFore]
                  : background.obtainColor(xxx, yyy);
            alpha = (color >> 24) & 0xFF;

            if((alpha == 255) || (doAlphaMix == false))
            {
               this.pixels[pix] = color;
            }
            else if(alpha > 0)
            {
               ahpla = 256 - alpha;
               col = this.pixels[pix];

               this.pixels[pix] = (Math.min(255, alpha + ((col >> 24) & 0xFF)) << 24) | //
                     ((((((color >> 16) & 0xFF) * alpha) + (((col >> 16) & 0xFF) * ahpla)) >> 8) << 16) | //
                     ((((((color >> 8) & 0xFF) * alpha) + (((col >> 8) & 0xFF) * ahpla)) >> 8) << 8) | //
                     ((((color & 0xFF) * alpha) + ((col & 0xFF) * ahpla)) >> 8);
            }

            pixFore++;
            pix++;
         }

         lineFore += foreground.width;
         line += this.width;
      }
   }

   /**
    * Paint mask with paint in foreground and color in background
    * 
    * @param x
    *           X where paint the mask
    * @param y
    *           Y where paint the mask
    * @param mask
    *           Mask to paint
    * @param foreground
    *           Foreground paint
    * @param background
    *           Background color
    * @param doAlphaMix
    *           Indicates if do alpha mixing ({@code true}) or just overwrite ({@code false})
    */
   public void paintMask(int x, int y, final JHelpMask mask, final JHelpPaint foreground, final int background, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      int w = this.width;
      int xx = 0;
      if(x < 0)
      {
         xx = -x;
         w += x;
         x = 0;
      }

      int h = this.height;
      int yy = 0;
      if(y < 0)
      {
         yy = -y;
         h += y;
         y = 0;
      }

      final int width = UtilMath.minIntegers(w - x, mask.getWidth());
      final int height = UtilMath.minIntegers(h - y, mask.getHeight());

      if((width < 1) || (height < 1))
      {
         return;
      }

      foreground.initializePaint(width, height);
      int line = x + (y * this.width);
      int pix, color, col, alpha, ahpla;

      for(int yyy = yy; yyy < height; yyy++)
      {
         pix = line;

         for(int xxx = xx; xxx < width; xxx++)
         {
            color = mask.getValue(xxx, yyy)
                  ? foreground.obtainColor(xxx, yyy)
                  : background;
            alpha = (color >> 24) & 0xFF;

            if((alpha == 255) || (doAlphaMix == false))
            {
               this.pixels[pix] = color;
            }
            else if(alpha > 0)
            {
               ahpla = 256 - alpha;
               col = this.pixels[pix];

               this.pixels[pix] = (Math.min(255, alpha + ((col >> 24) & 0xFF)) << 24) | //
                     ((((((color >> 16) & 0xFF) * alpha) + (((col >> 16) & 0xFF) * ahpla)) >> 8) << 16) | //
                     ((((((color >> 8) & 0xFF) * alpha) + (((col >> 8) & 0xFF) * ahpla)) >> 8) << 8) | //
                     ((((color & 0xFF) * alpha) + ((col & 0xFF) * ahpla)) >> 8);
            }

            pix++;
         }

         line += this.width;
      }
   }

   /**
    * Paint a mask with paint in foreground and image in background
    * 
    * @param x
    *           X position for mask
    * @param y
    *           Y position for mask
    * @param mask
    *           Mask to paint
    * @param foreground
    *           Foreground paint
    * @param background
    *           Background image
    * @param backgroundX
    *           X start in background image
    * @param backgroundY
    *           Y start in background image
    * @param doAlphaMix
    *           Indicates if do alpha mixing ({@code true}) or just overwrite ({@code false})
    */
   public void paintMask(int x, int y, final JHelpMask mask, final JHelpPaint foreground, final JHelpImage background, int backgroundX, int backgroundY, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      int bw = background.width;
      if(backgroundX < 0)
      {
         bw += backgroundX;
         backgroundX = 0;
      }

      int bh = background.height;
      if(backgroundY < 0)
      {
         bh += backgroundY;
         backgroundY = 0;
      }

      int w = this.width;
      int xx = 0;
      if(x < 0)
      {
         xx = -x;
         w += x;
         x = 0;
      }

      int h = this.height;
      int yy = 0;
      if(y < 0)
      {
         yy = -y;
         h += y;
         y = 0;
      }

      final int width = UtilMath.minIntegers(w - x, mask.getWidth(), bw - backgroundX);
      final int height = UtilMath.minIntegers(h - y, mask.getHeight(), bh - backgroundY);

      if((width < 1) || (height < 1))
      {
         return;
      }

      foreground.initializePaint(width, height);
      int lineBack = backgroundX + (backgroundY * background.width);
      int line = x + (y * this.width);
      int pixBack, pix, color, col, alpha, ahpla;

      for(int yyy = yy; yyy < height; yyy++)
      {
         pixBack = lineBack;
         pix = line;

         for(int xxx = xx; xxx < width; xxx++)
         {
            color = mask.getValue(xxx, yyy)
                  ? foreground.obtainColor(xxx, yyy)
                  : background.pixels[pixBack];
            alpha = (color >> 24) & 0xFF;

            if((alpha == 255) || (doAlphaMix == false))
            {
               this.pixels[pix] = color;
            }
            else if(alpha > 0)
            {
               ahpla = 256 - alpha;
               col = this.pixels[pix];

               this.pixels[pix] = (Math.min(255, alpha + ((col >> 24) & 0xFF)) << 24) | //
                     ((((((color >> 16) & 0xFF) * alpha) + (((col >> 16) & 0xFF) * ahpla)) >> 8) << 16) | //
                     ((((((color >> 8) & 0xFF) * alpha) + (((col >> 8) & 0xFF) * ahpla)) >> 8) << 8) | //
                     ((((color & 0xFF) * alpha) + ((col & 0xFF) * ahpla)) >> 8);
            }

            pixBack++;
            pix++;
         }

         lineBack += background.width;
         line += this.width;
      }
   }

   /**
    * Paint mask with paint in foreground and background
    * 
    * @param x
    *           X position for mask
    * @param y
    *           Y position for mask
    * @param mask
    *           Mask to paint
    * @param foreground
    *           Foreground paint
    * @param background
    *           Background paint
    * @param doAlphaMix
    *           Indicates if do alpha mixing ({@code true}) or just overwrite ({@code false})
    */
   public void paintMask(int x, int y, final JHelpMask mask, final JHelpPaint foreground, final JHelpPaint background, final boolean doAlphaMix)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      int w = this.width;
      int xx = 0;
      if(x < 0)
      {
         xx = -x;
         w += x;
         x = 0;
      }

      int h = this.height;
      int yy = 0;
      if(y < 0)
      {
         yy = -y;
         h += y;
         y = 0;
      }

      final int width = UtilMath.minIntegers(w - x, mask.getWidth());
      final int height = UtilMath.minIntegers(h - y, mask.getHeight());

      if((width < 1) || (height < 1))
      {
         return;
      }

      foreground.initializePaint(width, height);
      background.initializePaint(width, height);
      int line = x + (y * this.width);
      int pix, color, col, alpha, ahpla;

      for(int yyy = yy; yyy < height; yyy++)
      {
         pix = line;

         for(int xxx = xx; xxx < width; xxx++)
         {
            color = mask.getValue(xxx, yyy)
                  ? foreground.obtainColor(xxx, yyy)
                  : background.obtainColor(xxx, yyy);
            alpha = (color >> 24) & 0xFF;

            if((alpha == 255) || (doAlphaMix == false))
            {
               this.pixels[pix] = color;
            }
            else if(alpha > 0)
            {
               ahpla = 256 - alpha;
               col = this.pixels[pix];

               this.pixels[pix] = (Math.min(255, alpha + ((col >> 24) & 0xFF)) << 24) | //
                     ((((((color >> 16) & 0xFF) * alpha) + (((col >> 16) & 0xFF) * ahpla)) >> 8) << 16) | //
                     ((((((color >> 8) & 0xFF) * alpha) + (((col >> 8) & 0xFF) * ahpla)) >> 8) << 8) | //
                     ((((color & 0xFF) * alpha) + ((col & 0xFF) * ahpla)) >> 8);
            }

            pix++;
         }

         line += this.width;
      }
   }

   /**
    * Pick a color inside the image
    * 
    * @param x
    *           X position
    * @param y
    *           Y position
    * @return Picked color
    */
   public int pickColor(final int x, final int y)
   {
      if((x < 0) || (x >= this.width) || (y < 0) || (y >= this.height))
      {
         throw new IllegalArgumentException("Coordinates of peek point must be in [0, " + this.width + "[ x [0, " + this.height + "[ not (" + x + ", " + y + ")");
      }

      return this.pixels[x + (y * this.width)];
   }

   /**
    * Register a component to update on image change
    * 
    * @param component
    *           Component to register
    */
   public void register(final Component component)
   {
      this.reentrantLock.lock();

      try
      {
         if(this.componentsListeners.contains(component) == false)
         {
            this.componentsListeners.add(component);
         }
      }
      finally
      {
         this.reentrantLock.unlock();
      }
   }

   /**
    * Remove a sprite from linked sprites.<br>
    * The sprite is no more usable
    * 
    * @param sprite
    *           Sprite to remove
    */
   public void removeSprite(final JHelpSprite sprite)
   {
      if(this.drawMode == true)
      {
         throw new IllegalStateException("Musn't be in draw mode !");
      }

      sprite.setVisible(false);

      if(this.sprites.remove(sprite) == true)
      {
         final int index = sprite.getSpriteIndex();
         System.arraycopy(this.visibilities, index + 1, this.visibilities, index, this.visibilities.length - index - 1);

         for(int i = this.sprites.size() - 1; i >= 0; i--)
         {
            this.sprites.get(i).setSpriteIndex(i);
         }

         this.update();
      }
   }

   /**
    * Replace all pixels near a color by an other color
    * 
    * @param colorToReplace
    *           Color searched
    * @param newColor
    *           New color
    * @param near
    *           Distance maximum from color searched to consider to color is near
    */
   public void replaceColor(final int colorToReplace, final int newColor, final int near)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      int color;
      for(int i = this.pixels.length - 1; i >= 0; i--)
      {
         color = this.pixels[i];

         if((Math.abs(((colorToReplace >> 24) & 0xFF) - ((color >> 24) & 0xFF)) <= near) && (Math.abs(((colorToReplace >> 16) & 0xFF) - ((color >> 16) & 0xFF)) <= near)
               && (Math.abs(((colorToReplace >> 8) & 0xFF) - ((color >> 8) & 0xFF)) <= near) && (Math.abs((colorToReplace & 0xFF) - (color & 0xFF)) <= near))
         {
            this.pixels[i] = newColor;
         }
      }
   }

   /**
    * Change image name
    * 
    * @param name
    *           New name
    */
   public void setName(final String name)
   {
      this.name = name;
   }

   /**
    * Shift (translate) the image
    * 
    * @param x
    *           X shift
    * @param y
    *           Y shift
    */
   public void shift(final int x, final int y)
   {
      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      int index = x + (y * this.width);
      final int size = this.pixels.length;

      while(index < 0)
      {
         index += size;
      }

      index = index % size;

      final int[] temp = new int[size];
      System.arraycopy(this.pixels, 0, temp, 0, size);

      for(int i = 0; i < size; i++)
      {
         this.pixels[i] = temp[index];

         index = (index + 1) % size;
      }
   }

   /**
    * Start the draw mode
    */
   public void startDrawMode()
   {
      if(this.drawMode == false)
      {
         this.drawMode = true;

         final int length = this.sprites.size();

         if((this.visibilities == null) || (this.visibilities.length != length))
         {
            this.visibilities = new boolean[length];
         }

         boolean visible;
         JHelpSprite sprite;

         for(int index = length - 1; index >= 0; index--)
         {
            sprite = this.sprites.get(index);
            visible = this.visibilities[index] = sprite.isVisible();

            if(visible == true)
            {
               sprite.changeVisible(false, false);
            }
         }
      }
   }

   /**
    * Subtract the image by an other one
    * 
    * @param image
    *           Image to subtract
    */
   public void subtract(final JHelpImage image)
   {
      if((this.width != image.width) || (this.height != image.height))
      {
         throw new IllegalArgumentException("We can only multiply with an image of same size");
      }

      if(this.drawMode == false)
      {
         throw new IllegalStateException("Must be in draw mode !");
      }

      int colorThis, colorImage;

      for(int pix = this.pixels.length - 1; pix >= 0; pix--)
      {
         colorThis = this.pixels[pix];
         colorImage = image.pixels[pix];

         this.pixels[pix] = (colorThis & 0xFF000000) | //
               (UtilMath.limit0_255(((colorThis >> 16) & 0xFF) - ((colorImage >> 16) & 0xFF)) << 16) | //
               (UtilMath.limit0_255(((colorThis >> 8) & 0xFF) - ((colorImage >> 8) & 0xFF)) << 8) | //
               UtilMath.limit0_255((colorThis & 0xFF) - (colorImage & 0xFF));
      }
   }

   /**
    * Give all sprites of this image to an other image
    * 
    * @param image
    *           Image will receive this image sprites
    */
   public void transfertSpritesTo(final JHelpImage image)
   {
      final boolean drawMode = this.drawMode;

      image.endDrawMode();
      boolean visible;

      for(final JHelpSprite sprite : this.sprites)
      {
         visible = sprite.isVisible();
         sprite.setVisible(false);
         sprite.setParent(image);
         image.sprites.add(sprite);
         sprite.setVisible(visible);
      }

      this.sprites.clear();

      image.update();

      if(drawMode == true)
      {
         image.startDrawMode();
      }
   }

   /**
    * Unregister a component
    * 
    * @param component
    *           Component to unregister
    */
   public void unregister(final Component component)
   {
      this.reentrantLock.lock();

      try
      {
         this.componentsListeners.remove(component);
      }
      finally
      {
         this.reentrantLock.unlock();
      }
   }

   /**
    * Update the image, to see last changes
    */
   public void update()
   {
      final boolean onDraw = this.drawMode;

      if(onDraw == true)
      {
         this.endDrawMode();
      }

      this.memoryImageSource.newPixels();

      if(onDraw == true)
      {
         this.startDrawMode();
      }

      this.reentrantLock.lock();

      try
      {
         for(final Component component : this.componentsListeners)
         {
            component.invalidate();
            component.validate();
            component.repaint();
         }
      }
      finally
      {
         this.reentrantLock.unlock();
      }
   }
}