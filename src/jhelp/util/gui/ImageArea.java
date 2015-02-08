package jhelp.util.gui;

import java.awt.Cursor;
import java.awt.Point;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import jhelp.util.io.UtilIO;

/**
 * Image with sensitive areas
 * 
 * @author JHelp
 */
public class ImageArea
{
   /**
    * Describe an area
    * 
    * @author JHelp
    */
   class Area
   {
      /** Part of image inside the area */
      private JHelpImage part;
      /** Height */
      int                height;
      /** Width */
      int                width;
      /** X */
      int                x;
      /** Y */
      int                y;

      /**
       * Create a new instance of Area
       * 
       * @param x
       *           X
       * @param y
       *           Y
       * @param width
       *           Width
       * @param height
       *           Height
       */
      Area(final int x, final int y, final int width, final int height)
      {
         this.x = x;
         this.y = y;
         this.width = width;
         this.height = height;
      }

      /**
       * Area height
       * 
       * @return Area height
       */
      public int getHeight()
      {
         return this.height;
      }

      /**
       * Image part associated to the area
       * 
       * @return Image part associated to the area
       */
      public JHelpImage getPart()
      {
         if(this.part == null)
         {
            this.part = ImageArea.this.obtainPart(this);
         }

         return this.part;
      }

      /**
       * Area width
       * 
       * @return Area width
       */
      public int getWidth()
      {
         return this.width;
      }

      /**
       * Area X
       * 
       * @return X
       */
      public int getX()
      {
         return this.x;
      }

      /**
       * Area Y
       * 
       * @return Y
       */
      public int getY()
      {
         return this.y;
      }
   }

   /**
    * Position relative to an area
    * 
    * @author JHelp
    */
   public static enum OverPosition
   {
      /** Indicates that position is on down edge of the area */
      DOWN_EDGE(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR)),
      /** Indicates that position is on down left corner of the area */
      DOWN_LEFT_CORNER(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR)),
      /** Indicates that position is on down right corner of the area */
      DOWN_RIGHT_CORNER(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR)),
      /** Indicates that position is inside the area */
      INSIDE(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)),
      /** Indicates that position is on left edge of the area */
      LEFT_EDGE(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR)),
      /** Indicates that position is outside the area */
      OUTSIDE(Cursor.getDefaultCursor()),
      /** Indicates that position is on right edge of the area */
      RIGHT_EDGE(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR)),
      /** Indicates that position is on up edge of the area */
      UP_EDGE(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR)),
      /** Indicates that position is on up left corner of the area */
      UP_LEFT_CORNER(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR)),
      /** Indicates that position is on up right corner of the area */
      UP_RIGHT_CORNER(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
      /** Suggested cursor to use for area manipulation */
      private final Cursor cursor;

      /**
       * Create a new instance of OverPosition
       * 
       * @param cursor
       *           Suggested cursor to use for area manipulation
       */
      OverPosition(final Cursor cursor)
      {
         this.cursor = cursor;
      }

      /**
       * Suggested cursor to use for area manipulation
       * 
       * @return Suggested cursor to use for area manipulation
       */
      public Cursor getCursor()
      {
         return this.cursor;
      }
   }

   /**
    * Rectangle
    * 
    * @author JHelp
    */
   public class Rectangle
   {
      /** Down points Y */
      int down;
      /** Left points X */
      int left;
      /** Right points X */
      int right;
      /** Up points Y */
      int up;

      /**
       * Create a new instance of Rectangle
       * 
       * @param x1
       *           First corner X
       * @param y1
       *           First corner Y
       * @param x2
       *           Second corner X
       * @param y2
       *           Second corner Y
       */
      Rectangle(final int x1, final int y1, final int x2, final int y2)
      {
         this.left = Math.min(x1, x2);
         this.up = Math.min(y1, y2);
         this.right = Math.max(x1, x2);
         this.down = Math.max(y1, y2);
      }

      /**
       * Height
       * 
       * @return Height
       */
      public int getHeight()
      {
         return 1 + Math.abs(this.up - this.down);
      }

      /**
       * Width
       * 
       * @return Width
       */
      public int getWidth()
      {
         return 1 + Math.abs(this.left - this.right);
      }

      /**
       * X up left corner
       * 
       * @return X up left corner
       */
      public int getX()
      {
         return Math.min(this.left, this.right);
      }

      /**
       * Y up left corner
       * 
       * @return Y up left corner
       */
      public int getY()
      {
         return Math.min(this.up, this.down);
      }
   }

   /** Edge/corner thick size */
   private static final int   THICK      = 5;
   /** Area color */
   static final int           COLOR_AREA = 0x87654321;
   /** Over color */
   static final int           COLOR_OVER = 0x42100124;
   /** Image area file extension */
   public static final String EXTENTION  = "jha";

   /**
    * Load an image area from a stream
    * 
    * @param inputStream
    *           Stream to read
    * @return Loaded image area
    * @throws IOException
    *            On reading exception or stream not a valid image area
    */
   public static ImageArea loadImageArea(final InputStream inputStream) throws IOException
   {
      final ImageArea imageArea = new ImageArea();
      imageArea.load(inputStream);
      return imageArea;
   }

   /** Areas list */
   private final List<Area> areas;
   /** Base image */
   private JHelpImage       base;
   /** Grid height */
   private int              gridHeight;
   /** Grid width */
   private int              gridWidth;
   /** Rectangle over */
   private Rectangle        overRectangle;
   /** Sprite over */
   private JHelpSprite      spriteOver;

   /**
    * Create a new instance of ImageArea
    */
   private ImageArea()
   {
      this.areas = new ArrayList<Area>();
      this.gridWidth = 1;
      this.gridHeight = 1;
   }

   /**
    * Create a new instance of ImageArea
    * 
    * @param base
    *           Image base
    */
   public ImageArea(final JHelpImage base)
   {
      this();
      this.base = base;
      this.initializeSpriteOver();
   }

   /**
    * Initialize sprite over area
    */
   private void initializeSpriteOver()
   {
      final boolean drawMode = this.base.isDrawMode();
      this.base.endDrawMode();
      this.spriteOver = this.base.createSprite(0, 0, this.base.getWidth() - 1, this.base.getHeight() - 1);
      this.spriteOver.setVisible(true);

      if(drawMode == true)
      {
         this.base.startDrawMode();
      }
   }

   /**
    * Load image area from stream
    * 
    * @param inputStream
    *           Stream to read
    * @throws IOException
    *            On read issue or stream not a valid image area
    */
   private void load(final InputStream inputStream) throws IOException
   {
      final int size = UtilIO.readInteger(inputStream);
      int x, y, width, height;

      for(int i = 0; i < size; i++)
      {
         x = UtilIO.readInteger(inputStream);
         y = UtilIO.readInteger(inputStream);
         width = UtilIO.readInteger(inputStream);
         height = UtilIO.readInteger(inputStream);
         this.areas.add(new Area(x, y, width, height));
      }

      this.base = JHelpImage.loadImage(inputStream);
      this.initializeSpriteOver();
      this.updateOver();
   }

   /**
    * Update over sprite
    */
   private void updateOver()
   {
      if(this.overRectangle != null)
      {
         if(this.overRectangle.left > this.overRectangle.right)
         {
            final int temp = this.overRectangle.left;
            this.overRectangle.left = this.overRectangle.right;
            this.overRectangle.right = temp;
         }

         if(this.overRectangle.up > this.overRectangle.down)
         {
            final int temp = this.overRectangle.up;
            this.overRectangle.up = this.overRectangle.down;
            this.overRectangle.down = temp;
         }

         this.overRectangle.left = Math.max(0, this.overRectangle.left);
         this.overRectangle.right = Math.min(this.base.getWidth() - 1, this.overRectangle.right);
         this.overRectangle.up = Math.max(0, this.overRectangle.up);
         this.overRectangle.down = Math.min(this.base.getHeight() - 1, this.overRectangle.down);
      }

      final JHelpImage image = this.spriteOver.getImage();
      this.spriteOver.setVisible(false);
      image.startDrawMode();
      image.clear(0);

      for(final Area area : this.areas)
      {
         image.fillRectangle(area.x, area.y, area.width, area.height, ImageArea.COLOR_AREA);
      }

      if(this.overRectangle != null)
      {
         final int x = this.overRectangle.getX();
         final int y = this.overRectangle.getY();
         int width = this.overRectangle.getWidth();
         int height = this.overRectangle.getHeight();
         final int w = width / this.gridWidth;
         final int h = height / this.gridHeight;
         width = w * this.gridWidth;
         height = h * this.gridHeight;
         final int more = ImageArea.THICK >> 1;
         final int thick = more << 1;

         image.fillRectangle(x, y, width, height, ImageArea.COLOR_OVER);
         image.drawThickRectangle(x - more, y - more, width + thick, height + thick, ImageArea.THICK, 0x44FFFFFF);

         int yy = y;
         int xx = x;
         final int x2 = x + width;
         final int y2 = y + height;

         for(int hh = 0; hh <= this.gridHeight; hh++)
         {
            image.drawHorizontalLine(x, x2, yy, 0xFF000000);
            yy += h;
         }

         for(int ww = 0; ww <= this.gridWidth; ww++)
         {
            image.drawVerticalLine(xx, y, y2, 0xFF000000);
            xx += w;
         }
      }

      image.endDrawMode();
      this.spriteOver.setVisible(true);
   }

   /**
    * Compute image part of an area
    * 
    * @param area
    *           Area to get its image part
    * @return Image part
    */
   JHelpImage obtainPart(final Area area)
   {
      this.spriteOver.setVisible(false);
      final JHelpImage part = this.base.extractSubImage(area.x, area.y, area.width, area.height);
      this.spriteOver.setVisible(true);

      return part;
   }

   /**
    * Add actual selected rectangle/grid as areas
    */
   public void addActualOverAsArea()
   {
      if(this.overRectangle == null)
      {
         return;
      }

      final int x = this.overRectangle.getX();
      final int y = this.overRectangle.getY();
      final int width = this.overRectangle.getWidth();
      final int height = this.overRectangle.getHeight();
      final int w = width / this.gridWidth;
      final int h = height / this.gridHeight;
      int yy = y;
      int xx = x;

      for(int hh = 0; hh < this.gridHeight; hh++)
      {
         xx = x;

         for(int ww = 0; ww < this.gridWidth; ww++)
         {
            this.areas.add(new Area(xx, yy, w, h));
            xx += w;
         }

         yy += h;
      }
   }

   /**
    * Add an area
    * 
    * @param x
    *           X
    * @param y
    *           Y
    * @param width
    *           Width
    * @param height
    *           Height
    */
   public void addArea(final int x, final int y, final int width, final int height)
   {
      this.areas.add(new Area(x, y, width, height));
      this.updateOver();
   }

   /**
    * Divide image in grid selection
    * 
    * @param horizontal
    *           Number horizontal cell
    * @param vertical
    *           Number vertical cell
    */
   public void divide(final int horizontal, final int vertical)
   {
      final int width = this.base.getWidth() / Math.max(1, horizontal);
      final int height = this.base.getHeight() / Math.max(1, vertical);
      final int x = (this.base.getWidth() - width) >> 1;
      final int y = (this.base.getHeight() - height) >> 1;
      this.setOverRectangle(x, y, (x + width) - 1, (y + height) - 1);
   }

   /**
    * Obtain an area
    * 
    * @param index
    *           Area index
    * @return The area
    */
   public Area getArea(final int index)
   {
      return this.areas.get(index);
   }

   /**
    * Image base
    * 
    * @return Image base
    */
   public JHelpImage getBase()
   {
      return this.base;
   }

   /**
    * Grid height
    * 
    * @return Grid height
    */
   public int getGridHeight()
   {
      return this.gridHeight;
   }

   /**
    * Grid width
    * 
    * @return Grid width
    */
   public int getGridWidth()
   {
      return this.gridWidth;
   }

   /**
    * Over rectangle
    * 
    * @return Over rectangle
    */
   public Rectangle getOverRectangle()
   {
      return this.overRectangle;
   }

   /**
    * Selection down right coordinate
    * 
    * @return Selection down right coordinate
    */
   public Point getPointDownRight()
   {
      if(this.overRectangle == null)
      {
         return null;
      }

      this.updateOver();
      return new Point(this.overRectangle.right, this.overRectangle.down);
   }

   /**
    * Selection up left coordinate
    * 
    * @return Selection up left coordinate
    */
   public Point getPointUpLeft()
   {
      if(this.overRectangle == null)
      {
         return null;
      }

      this.updateOver();
      return new Point(this.overRectangle.left, this.overRectangle.up);
   }

   /**
    * Hide over rectangle
    */
   public void hideOverRectangle()
   {
      this.overRectangle = null;
      this.updateOver();
   }

   /**
    * Make a color transparent
    * 
    * @param x
    *           X pixel
    * @param y
    *           Y pixel
    */
   public void makeTransparent(final int x, final int y)
   {
      this.base.startDrawMode();
      this.base.replaceColor(this.base.pickColor(x, y), 0, 1);
      this.base.endDrawMode();
   }

   /**
    * Number of areas
    * 
    * @return Number of areas
    */
   public int numberOfArea()
   {
      return this.areas.size();
   }

   /**
    * Obtain relative position from current selection
    * 
    * @param x
    *           X
    * @param y
    *           Y
    * @return Relative position
    */
   public OverPosition obtainPosition(final int x, final int y)
   {
      this.updateOver();

      if(this.overRectangle == null)
      {
         return OverPosition.OUTSIDE;
      }

      final int xMin = Math.min(this.overRectangle.left, this.overRectangle.right);
      final int xMax = Math.max(this.overRectangle.left, this.overRectangle.right);
      final int yMin = Math.min(this.overRectangle.up, this.overRectangle.down);
      final int yMax = Math.max(this.overRectangle.up, this.overRectangle.down);

      if((x < xMin) || (x > xMax) || (y < yMin) || (y > yMax))
      {
         return OverPosition.OUTSIDE;
      }

      if((x - xMin) < ImageArea.THICK)
      {
         if((y - yMin) < ImageArea.THICK)
         {
            return OverPosition.UP_LEFT_CORNER;
         }

         if((yMax - y) < ImageArea.THICK)
         {
            return OverPosition.UP_RIGHT_CORNER;
         }

         return OverPosition.LEFT_EDGE;
      }

      if((xMax - x) < ImageArea.THICK)
      {
         if((y - yMin) < ImageArea.THICK)
         {
            return OverPosition.DOWN_LEFT_CORNER;
         }

         if((yMax - y) < ImageArea.THICK)
         {
            return OverPosition.DOWN_RIGHT_CORNER;
         }

         return OverPosition.RIGHT_EDGE;
      }

      if((y - yMin) < ImageArea.THICK)
      {
         return OverPosition.UP_EDGE;
      }

      if((yMax - y) < ImageArea.THICK)
      {
         return OverPosition.DOWN_EDGE;
      }

      return OverPosition.INSIDE;
   }

   /**
    * Save image area
    * 
    * @param outputStream
    *           Stream where write
    * @throws IOException
    *            On writing issue
    */
   public void saveImageArea(final OutputStream outputStream) throws IOException
   {
      this.spriteOver.setVisible(false);
      UtilIO.writeInteger(this.areas.size(), outputStream);

      for(final Area area : this.areas)
      {
         UtilIO.writeInteger(area.x, outputStream);
         UtilIO.writeInteger(area.y, outputStream);
         UtilIO.writeInteger(area.width, outputStream);
         UtilIO.writeInteger(area.height, outputStream);
      }

      JHelpImage.saveImage(outputStream, this.base);
      this.spriteOver.setVisible(true);
   }

   /**
    * Defines the grid
    * 
    * @param width
    *           Width
    * @param height
    *           Height
    */
   public void setGrid(final int width, final int height)
   {
      this.gridWidth = Math.max(1, width);
      this.gridHeight = Math.max(1, height);
   }

   /**
    * Reduce over rectangle to one point
    * 
    * @param x
    *           X
    * @param y
    *           Y
    */
   public void setOnePoint(final int x, final int y)
   {
      this.setOverRectangle(x, y, x, y);
   }

   /**
    * Changes the over rectangle
    * 
    * @param x1
    *           First corner X
    * @param y1
    *           First corner Y
    * @param x2
    *           Second corner X
    * @param y2
    *           Second corner Y
    */
   public void setOverRectangle(final int x1, final int y1, final int x2, final int y2)
   {
      this.overRectangle = new Rectangle(x1, y1, x2, y2);
      this.updateOver();
   }

   /**
    * Change the selection
    * 
    * @param x
    *           X
    * @param y
    *           Y
    * @param overPosition
    *           Relative position to change
    */
   public void setPoint(final int x, final int y, final OverPosition overPosition)
   {
      if(this.overRectangle == null)
      {
         this.setOverRectangle(x, y, x, y);
         return;
      }

      this.updateOver();

      final int mx = (this.overRectangle.right + this.overRectangle.left) >> 1;
      final int my = (this.overRectangle.down + this.overRectangle.up) >> 1;
      int vx = x - mx;
      int vy = y - my;

      if((this.overRectangle.left + vx) < 0)
      {
         vx = -this.overRectangle.left;
      }

      if((this.overRectangle.right + vx) > (this.base.getWidth() - 1))
      {
         vx = this.base.getWidth() - 1 - this.overRectangle.right;
      }

      if((this.overRectangle.up + vy) < 0)
      {
         vy = -this.overRectangle.up;
      }

      if((this.overRectangle.down + vy) > (this.base.getHeight() - 1))
      {
         vy = this.base.getHeight() - 1 - this.overRectangle.down;
      }

      switch(overPosition)
      {
         case DOWN_EDGE:
            this.overRectangle.down = y;
         break;
         case DOWN_LEFT_CORNER:
            this.overRectangle.left = x;
            this.overRectangle.down = y;
         break;
         case DOWN_RIGHT_CORNER:
            this.overRectangle.right = x;
            this.overRectangle.down = y;
         break;
         case INSIDE:
            this.overRectangle.left += vx;
            this.overRectangle.right += vx;
            this.overRectangle.up += vy;
            this.overRectangle.down += vy;
         break;
         case LEFT_EDGE:
            this.overRectangle.left = x;
         break;
         case OUTSIDE:
         break;
         case RIGHT_EDGE:
            this.overRectangle.right = x;
         break;
         case UP_EDGE:
            this.overRectangle.up = y;
         break;
         case UP_LEFT_CORNER:
            this.overRectangle.left = x;
            this.overRectangle.up = y;
         break;
         case UP_RIGHT_CORNER:
            this.overRectangle.right = x;
            this.overRectangle.up = y;
         break;
      }

      this.updateOver();
   }

   /**
    * Change selection down right corner
    * 
    * @param x
    *           X
    * @param y
    *           Y
    */
   public void setPointDownRight(final int x, final int y)
   {
      if(this.overRectangle == null)
      {
         this.setOverRectangle(x, y, x, y);
         return;
      }

      this.overRectangle.right = x;
      this.overRectangle.down = y;
      this.updateOver();
   }

   /**
    * Change selection up left corner
    * 
    * @param x
    *           X
    * @param y
    *           Y
    */
   public void setPointUpLeft(final int x, final int y)
   {
      if(this.overRectangle == null)
      {
         this.setOverRectangle(x, y, x, y);
         return;
      }

      this.overRectangle.left = x;
      this.overRectangle.up = y;
      this.updateOver();
   }
}