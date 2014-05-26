package jhelp.util.math.rational;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Matrix based on {@link Rational}
 * 
 * @author JHelp
 */
public class MatrixRational
{
   /** Matrix determinant */
   private Rational         determinant;
   /** Matrix height */
   private final int        height;
   /** Matrix itself */
   private final Rational[] matrix;
   /** Matrix size */
   private final int        size;
   /** Matrix width */
   private final int        width;

   /**
    * Create a new instance of MatrixRational
    * 
    * @param width
    *           Matrix width
    * @param height
    *           Matrix height
    */
   public MatrixRational(final int width, final int height)
   {
      if((width < 1) || (height < 1))
      {
         throw new IllegalArgumentException("Width and height must be > 0 not " + width + "x" + height);
      }

      this.width = width;
      this.height = height;
      this.size = this.width * this.height;
      this.matrix = new Rational[this.size];

      for(int i = 0; i < this.size; i++)
      {
         this.matrix[i] = Rational.ZERO;
      }

      if(width == height)
      {
         this.determinant = Rational.ZERO;
      }
      else
      {
         this.determinant = Rational.INVALID;
      }
   }

   /**
    * Check if a cell is inside the matrix
    * 
    * @param x
    *           Cell X
    * @param y
    *           Cell Y
    * @throws IllegalArgumentException
    *            If cell outside the matrix
    */
   private void check(final int x, final int y)
   {
      if((x < 0) || (x >= this.width) || (y < 0) || (y >= this.height))
      {
         throw new IllegalArgumentException("x must be in [0, " + this.width + "[ and y in [0, " + this.height + "[ not (" + x + ", " + y + ")");
      }
   }

   /**
    * Compute internally the determinant
    * 
    * @return Matrix determinant
    */
   private Rational determinantInternal()
   {
      if(this.width == 1)
      {
         this.determinant = this.matrix[0];
         return this.determinant;
      }

      if(this.width == 2)
      {
         this.determinant = Rational.subtract(this.matrix[0].multiply(this.matrix[3]), this.matrix[2].multiply(this.matrix[1]));
         return this.determinant;
      }

      return this.determinantInternalMore2();
   }

   /**
    * Compute matrix determinant if matrix width is more than 2
    * 
    * @return Matrix determinant
    */
   private Rational determinantInternalMore2()
   {
      final MatrixRational work = this.copy();
      Rational determinant = Rational.ONE;
      int y2;
      int diago = 0;
      final int more = this.width + 1;
      Rational diagoVal;

      for(int y = 0; y < this.height; y++)
      {
         y2 = y;
         diagoVal = work.matrix[diago];

         while(diagoVal == Rational.ZERO)
         {
            y2++;

            if(y2 >= this.height)
            {
               this.determinant = Rational.ZERO;
               return Rational.ZERO;
            }

            determinant = determinant.multiply(Rational.MINUS_ONE);
            work.interExchangeRowInternal(y, y2);
            diagoVal = work.matrix[diago];
         }

         determinant = determinant.multiply(diagoVal);
         work.pivoteInternal(y);
         diago += more;
      }

      this.determinant = determinant;
      return determinant;
   }

   /**
    * Exchange two matrix rows
    * 
    * @param y1
    *           First row
    * @param y2
    *           Second row
    */
   private void interExchangeRowInternal(final int y1, final int y2)
   {
      final int line1 = y1 * this.width;
      final int line2 = y2 * this.width;
      final Rational[] temp = new Rational[this.width];
      System.arraycopy(this.matrix, line1, temp, 0, this.width);
      System.arraycopy(this.matrix, line2, this.matrix, line1, this.width);
      System.arraycopy(temp, 0, this.matrix, line2, this.width);
   }

   /**
    * Pivote matrix form diagonal point
    * 
    * @param xy
    *           x and y coordinate of diagonal
    */
   private void pivoteInternal(final int xy)
   {
      final int startY = xy * this.width;
      int y = startY + this.width;
      int destination;
      int source;
      Rational coeficient;
      final Rational div = this.matrix[startY + xy];

      for(int yy = xy + 1; yy < this.height; yy++)
      {
         coeficient = this.matrix[y + xy];

         if(coeficient != Rational.ZERO)
         {
            destination = y;
            source = startY;
            coeficient = coeficient.divide(div);

            for(int xx = 0; xx < this.width; xx++)
            {
               this.matrix[destination] = this.matrix[destination].subtract(coeficient.multiply(this.matrix[source++]));
               destination++;
            }
         }

         y += this.width;
      }
   }

   /**
    * Compute a submatrix on removing one column and one row
    * 
    * @param removedColumn
    *           Column to remove
    * @param removedRow
    *           Row to remove
    * @return Extracted matrix
    */
   private MatrixRational subMatrixRational(final int removedColumn, final int removedRow)
   {
      final int w = this.width - 1;
      final MatrixRational result = new MatrixRational(w, this.height - 1);
      int lineSource = 0;
      int lineDestination = 0;
      final int x = removedColumn + 1;
      final int left = w - removedColumn;

      for(int y = 0; y < removedRow; y++)
      {
         System.arraycopy(this.matrix, lineSource, result.matrix, lineDestination, removedColumn);
         System.arraycopy(this.matrix, lineSource + x, result.matrix, lineDestination + removedColumn, left);
         lineSource += this.width;
         lineDestination += w;
      }

      lineSource += this.width;

      for(int y = removedRow + 1; y < this.height; y++)
      {
         System.arraycopy(this.matrix, lineSource, result.matrix, lineDestination, removedColumn);
         System.arraycopy(this.matrix, lineSource + x, result.matrix, lineDestination + removedColumn, left);
         lineSource += this.width;
         lineDestination += w;
      }

      result.determinant = Rational.INVALID;
      return result;
   }

   /**
    * Compute the determinant of a sub matrix (matrix with one column and one row removed)
    * 
    * @param x
    *           Column to remove
    * @param y
    *           Row to remove
    * @return Compute determinant
    */
   Rational determinantSubMatrix(final int x, final int y)
   {
      if(this.width == 2)
      {
         return this.matrix[(1 - x) + ((1 - y) * this.width)];
      }

      if(this.width == 3)
      {
         switch(x)
         {
            case 0:
               switch(y)
               {
                  case 0:
                     return Rational.subtract(this.matrix[4].multiply(this.matrix[8]), this.matrix[5].multiply(this.matrix[7]));
                  case 1:
                     return Rational.subtract(this.matrix[1].multiply(this.matrix[8]), this.matrix[2].multiply(this.matrix[7]));
                  case 2:
                     return Rational.subtract(this.matrix[1].multiply(this.matrix[5]), this.matrix[2].multiply(this.matrix[4]));
               }
            case 1:
               switch(y)
               {
                  case 0:
                     return Rational.subtract(this.matrix[3].multiply(this.matrix[8]), this.matrix[5].multiply(this.matrix[6]));
                  case 1:
                     return Rational.subtract(this.matrix[0].multiply(this.matrix[8]), this.matrix[2].multiply(this.matrix[6]));
                  case 2:
                     return Rational.subtract(this.matrix[0].multiply(this.matrix[5]), this.matrix[2].multiply(this.matrix[3]));
               }
            case 2:
               switch(y)
               {
                  case 0:
                     return Rational.subtract(this.matrix[3].multiply(this.matrix[7]), this.matrix[4].multiply(this.matrix[6]));
                  case 1:
                     return Rational.subtract(this.matrix[0].multiply(this.matrix[7]), this.matrix[1].multiply(this.matrix[6]));
                  case 2:
                     return Rational.subtract(this.matrix[0].multiply(this.matrix[4]), this.matrix[1].multiply(this.matrix[3]));
               }
         }
      }

      return this.subMatrixRational(x, y).determinantInternalMore2();
   }

   /**
    * Add an other matrix.<br>
    * The added matrix MUST have same dimensions
    * 
    * @param matrixRational
    *           Matrix to add
    */
   public void addition(final MatrixRational matrixRational)
   {
      if((this.width != matrixRational.width) || (this.height != matrixRational.height))
      {
         throw new IllegalArgumentException("Matrix must have same size !");
      }

      for(int i = 0; i < this.size; i++)
      {
         this.matrix[i] = this.matrix[i].addition(matrixRational.matrix[i]);
      }

      this.determinant = Rational.INVALID;
   }

   /**
    * Compute the matrix adjacent.<br>
    * The matrix MUST be square
    * 
    * @return Adjacent matrix
    */
   public MatrixRational adjacent()
   {
      if(this.width != this.height)
      {
         throw new IllegalStateException("Adjacent only for square matrix");
      }

      if(this.width == 1)
      {
         return this.copy();
      }

      if(this.width == 2)
      {
         final MatrixRational adjacent = new MatrixRational(2, 2);
         adjacent.matrix[0] = this.matrix[3];
         adjacent.matrix[1] = this.matrix[1].opposite();
         adjacent.matrix[2] = this.matrix[2].opposite();
         adjacent.matrix[3] = this.matrix[0];
         adjacent.determinant = this.determinant;
         return adjacent;
      }

      final MatrixRational adjacent = new MatrixRational(this.width, this.height);
      int index = 0;
      Rational signMain = Rational.ONE;
      final AtomicInteger count = new AtomicInteger(this.width);

      for(int x = 0; x < this.width; x++)
      {
         (new TaskAdjacent(count, adjacent.matrix, signMain, index, x, this.height, this)).start();
         signMain = signMain.opposite();
         index += this.width;
      }

      synchronized(count)
      {
         while(count.get() > 0)
         {
            try
            {
               count.wait();
            }
            catch(final Exception exception)
            {
            }
         }
      }

      adjacent.determinant = Rational.INVALID;
      return adjacent;
   }

   /**
    * Indicates if the matrix can be invert
    * 
    * @return {@code true} if matrix can be invert
    */
   public boolean canBeInvert()
   {
      return (this.isSquare() == true) && (this.determinant() != Rational.ZERO);
   }

   /**
    * Create a matrix copy
    * 
    * @return Matrix copy
    */
   public MatrixRational copy()
   {
      final MatrixRational matrixRational = new MatrixRational(this.width, this.height);
      System.arraycopy(this.matrix, 0, matrixRational.matrix, 0, this.size);
      matrixRational.determinant = this.determinant;
      return matrixRational;
   }

   /**
    * compute matrix determinant.<br>
    * The matrix MUST be square
    * 
    * @return Matrix determinant
    */
   public Rational determinant()
   {
      if(this.width != this.height)
      {
         throw new IllegalStateException("Matrix must be square");
      }

      if(this.determinant != Rational.INVALID)
      {
         return this.determinant;
      }

      return this.determinantInternal();
   }

   /**
    * Indicates if an object is equals to this matrix <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param object
    *           Compared object
    * @return {@code true} if the object is equals to this matrix
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals(final Object object)
   {
      if(this == object)
      {
         return true;
      }

      if(object == null)
      {
         return false;
      }

      if(this.getClass() != object.getClass())
      {
         return false;
      }

      final MatrixRational matrixRational = (MatrixRational) object;

      if(this.height != matrixRational.height)
      {
         return false;
      }

      if(this.width != matrixRational.width)
      {
         return false;
      }

      for(int i = 0; i < this.size; i++)
      {
         if(this.matrix[i].equals(matrixRational.matrix[i]) == false)
         {
            return false;
         }
      }

      return true;
   }

   /**
    * Obtain a matrix cell value
    * 
    * @param x
    *           Cell X
    * @param y
    *           Cell Y
    * @return Cell value
    */
   public Rational get(final int x, final int y)
   {
      this.check(x, y);

      return this.matrix[x + (y * this.width)];
   }

   /**
    * Matrix height
    * 
    * @return Matrix height
    */
   public int getHeight()
   {
      return this.height;
   }

   /**
    * Matrix width
    * 
    * @return Matrix width
    */
   public int getWidth()
   {
      return this.width;
   }

   /**
    * Matrix hash code <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return Matrix hash code
    * @see java.lang.Object#hashCode()
    */
   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = (prime * result) + this.width;
      result = (prime * result) + this.height;
      result = (prime * result) + Arrays.hashCode(this.matrix);
      return result;
   }

   /**
    * Exchange two rows
    * 
    * @param y1
    *           First row
    * @param y2
    *           Second row
    */
   public void interExchangeRow(final int y1, final int y2)
   {
      if((y1 < 0) || (y1 >= this.height) || (y2 < 0) || (y2 >= this.height))
      {
         throw new IllegalArgumentException("It is a " + this.width + "x" + this.height + " matrix, but y1=" + y1 + " and y2=" + y2);
      }

      if(y1 == y2)
      {
         return;
      }

      this.interExchangeRowInternal(y1, y2);
   }

   /**
    * Invert (If possible) the matrix.<br>
    * Use {@code #canBeInvert()} to know if the matrix can be invert.<br>
    * Remenber a matrix {@link #canBeInvert() can be invert} if the matrix {@link #isSquare() is square} and its
    * {@code #determinant determinant} is not zero
    * 
    * @return Inverted matrix
    */
   public MatrixRational invert()
   {
      if(this.width != this.height)
      {
         throw new IllegalStateException("Only square matrix can be invert");
      }

      final Rational determinant = this.determinant();

      if(determinant == Rational.ZERO)
      {
         throw new IllegalArgumentException("Matrix determinant is zero, so have no invert");
      }

      final MatrixRational invert = this.adjacent();

      for(int i = 0; i < this.size; i++)
      {
         invert.matrix[i] = invert.matrix[i].divide(determinant);
      }

      invert.determinant = determinant.inverse();
      return invert;
   }

   /**
    * Indicates if the matrix is an identity matrix
    * 
    * @return {@code true} if the matrix is an identity matrix
    */
   public boolean isIdentity()
   {
      final int diagonal = this.width + 1;
      final int max = this.width * this.width;

      for(int i = 0; i < this.size; i++)
      {
         if((i < max) && ((i % diagonal) == 0))
         {
            if(this.matrix[i] != Rational.ONE)
            {
               return false;
            }
         }
         else if(this.matrix[i] != Rational.ZERO)
         {
            return false;
         }
      }

      if(this.width == this.height)
      {
         this.determinant = Rational.ONE;
      }
      else
      {
         this.determinant = Rational.INVALID;
      }

      return true;
   }

   /**
    * Indicates if the matrix is square
    * 
    * @return {@code true} if the matrix is square
    */
   public boolean isSquare()
   {
      return this.width == this.height;
   }

   /**
    * Indicates if the matrix is a zero matrix
    * 
    * @return {@code true} if the matrix is a zero matrix
    */
   public boolean isZero()
   {
      for(int i = 0; i < this.size; i++)
      {
         if(this.matrix[i] != Rational.ZERO)
         {
            return false;
         }
      }

      if(this.width == this.height)
      {
         this.determinant = Rational.ZERO;
      }
      else
      {
         this.determinant = Rational.INVALID;
      }

      return true;
   }

   /**
    * Multiply by an other matrix.<br>
    * The multiplied matrix MUST have for width the height of this matrix AND for height the width of this matrix
    * 
    * @param matrixRational
    *           Multiplied matrix
    * @return Result
    */
   public MatrixRational multiplication(final MatrixRational matrixRational)
   {
      if((this.width != matrixRational.height) || (this.height != matrixRational.width))
      {
         throw new IllegalArgumentException("The multiplied matrix must have size : " + this.height + "x" + this.width);
      }

      final MatrixRational result = new MatrixRational(matrixRational.width, this.height);
      final AtomicInteger count = new AtomicInteger(result.size);

      for(int y = 0; y < this.height; y++)
      {
         for(int x = 0; x < matrixRational.width; x++)
         {
            (new TaskMultiplicationCell(count, x, y, this.width, matrixRational.width, matrixRational.width, result.matrix, this.matrix, matrixRational.matrix)).start();
         }
      }

      synchronized(count)
      {
         while(count.get() > 0)
         {
            try
            {
               count.wait();
            }
            catch(final Exception exception)
            {
            }
         }
      }

      result.determinant = this.determinant.multiply(matrixRational.determinant);
      return result;
   }

   /**
    * Multiply the matrix by a factor(All cells will be multiplied by the factor)
    * 
    * @param factor
    *           Factor to multiply with
    */
   public void multiplication(final Rational factor)
   {
      for(int i = 0; i < this.size; i++)
      {
         this.matrix[i] = this.matrix[i].multiply(factor);
      }

      this.determinant = this.determinant.multiply(factor.power(this.width));
   }

   /**
    * Pivote matrix around a diagonal
    * 
    * @param xy
    *           Diagonal X and Y
    */
   public void pivote(final int xy)
   {
      if((xy < 0) || (xy >= this.width) || (xy >= this.height))
      {
         throw new IllegalArgumentException("It is a " + this.width + "x" + this.height + " matrix, but xy=" + xy);
      }

      if(this.matrix[xy + (xy * this.width)] == Rational.ZERO)
      {
         return;
      }

      this.pivoteInternal(xy);
   }

   /**
    * Change a cell value
    * 
    * @param x
    *           Cell X
    * @param y
    *           Cell Y
    * @param value
    *           New value
    */
   public void set(final int x, final int y, final Rational value)
   {
      if(value == null)
      {
         throw new NullPointerException("value musn't be null");
      }

      this.check(x, y);
      this.matrix[x + (y * this.width)] = value;
      this.determinant = Rational.INVALID;
   }

   /**
    * Set several value to the matrix, they fill the matrix line by line.<br>
    * If they are to much value, they will be ignore.<br>
    * If they are not enough value, the rest of the matrix will be fill of zero
    * 
    * @param values
    *           Values to set
    */
   public void setValues(final Rational... values)
   {
      final int nb = Math.min(this.size, values.length);
      System.arraycopy(values, 0, this.matrix, 0, nb);

      for(int i = nb; i < this.size; i++)
      {
         this.matrix[i] = Rational.ZERO;
      }

      this.determinant = Rational.INVALID;
   }

   /**
    * Subtract an other matrix.<br>
    * The subtracted matrix MUST have the same size
    * 
    * @param matrixRational
    *           Matrix to subtract
    */
   public void subtraction(final MatrixRational matrixRational)
   {
      if((this.width != matrixRational.width) || (this.height != matrixRational.height))
      {
         throw new IllegalArgumentException("Matrix must have same size !");
      }

      for(int i = 0; i < this.size; i++)
      {
         this.matrix[i] = this.matrix[i].subtract(matrixRational.matrix[i]);
      }

      this.determinant = Rational.INVALID;
   }

   /**
    * Put the matrix to identity
    */
   public void toIdentity()
   {
      this.toZero();

      final int number = this.width * Math.min(this.width, this.height);

      for(int p = 0; p < number; p += this.width + 1)
      {
         this.matrix[p] = Rational.ONE;
      }

      if(this.width == this.height)
      {
         this.determinant = Rational.ONE;
      }
      else
      {
         this.determinant = Rational.INVALID;
      }
   }

   /**
    * String representation <br>
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
      final StringBuilder stringBuilder = new StringBuilder("Matrix ");
      stringBuilder.append(this.width);
      stringBuilder.append('x');
      stringBuilder.append(this.height);

      if(this.determinant != Rational.INVALID)
      {
         stringBuilder.append(" determinant=");
         stringBuilder.append(this.determinant);
      }

      stringBuilder.append(" : ");
      int w = 0;
      final String[] texts = new String[this.size];
      final NumberFormat numberFormat = NumberFormat.getInstance();
      numberFormat.setMinimumFractionDigits(5);
      numberFormat.setMaximumFractionDigits(5);
      int length;

      for(int i = 0; i < this.size; i++)
      {
         texts[i] = this.matrix[i].toString();
         length = texts[i].length();
         w = Math.max(length, w);
      }

      for(int i = 0; i < this.size; i++)
      {
         if((i % this.width) == 0)
         {
            stringBuilder.append("\n     ");
         }

         length = texts[i].length();

         for(int s = length; s < w; s++)
         {
            stringBuilder.append(' ');
         }

         stringBuilder.append(texts[i]);
         stringBuilder.append(' ');
      }

      return stringBuilder.toString();
   }

   /**
    * Put the matrix to zero
    */
   public void toZero()
   {
      for(int i = 0; i < this.size; i++)
      {
         this.matrix[i] = Rational.ZERO;
      }

      if(this.width == this.height)
      {
         this.determinant = Rational.ZERO;
      }
      else
      {
         this.determinant = Rational.INVALID;
      }
   }

   /**
    * Compute the transpose matrix
    * 
    * @return Transpose matrix
    */
   public MatrixRational transpose()
   {
      final MatrixRational transpose = new MatrixRational(this.height, this.width);
      int index = 0;
      int pos;

      for(int y = 0; y < this.height; y++)
      {
         pos = y;
         for(int x = 0; x < this.width; x++)
         {
            transpose.matrix[pos] = this.matrix[index++];
            pos += this.height;
         }
      }

      transpose.determinant = this.determinant;

      return transpose;
   }
}