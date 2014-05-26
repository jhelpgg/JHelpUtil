package jhelp.util.math.matrix;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import jhelp.util.io.ByteArray;

public class Matrix
{

   public static final double PRECISION = 1e-5;

   public static boolean equals(final double real1, final double real2)
   {
      return Math.abs(real1 - real2) <= Matrix.PRECISION;
   }

   public static boolean isNul(final double real)
   {
      return Math.abs(real) <= Matrix.PRECISION;
   }

   public static Matrix parseBinary(final ByteArray byteArray)
   {
      final int width = byteArray.readInteger();
      final int height = byteArray.readInteger();
      final Matrix matrix = new Matrix(width, height);
      matrix.setValues(byteArray.readDoubleArray());
      matrix.determinantKnown = byteArray.readBoolean();
      matrix.determinant = byteArray.readDouble();
      return matrix;
   }

   private double         determinant;
   private boolean        determinantKnown;
   private final int      height;
   private final double[] matrix;

   private final int      size;

   private final int      width;

   public Matrix(final int width, final int height)
   {
      if((width < 1) || (height < 1))
      {
         throw new IllegalArgumentException("Width and height must be > 0 not " + width + "x" + height);
      }

      this.width = width;
      this.height = height;
      this.size = this.width * this.height;
      this.matrix = new double[this.size];
      this.determinantKnown = width == height;
      this.determinant = 0;
   }

   private void check(final int x, final int y)
   {
      if((x < 0) || (x >= this.width) || (y < 0) || (y >= this.height))
      {
         throw new IllegalArgumentException("x must be in [0, " + this.width + "[ and y in [0, " + this.height + "[ not (" + x + ", " + y + ")");
      }
   }

   private double determinantInternal()
   {
      if(this.width == 1)
      {
         this.determinantKnown = true;
         this.determinant = this.matrix[0];
         return this.determinant;
      }

      if(this.width == 2)
      {
         this.determinantKnown = true;
         this.determinant = (this.matrix[0] * this.matrix[3]) - (this.matrix[2] * this.matrix[1]);
         return this.determinant;
      }

      return this.determinantInternalMore2();
   }

   private double determinantInternalMore2()
   {
      final Matrix work = this.copy();
      double determinant = 1;
      int y2;
      int diago = 0;
      final int more = this.width + 1;
      double diagoVal;

      for(int y = 0; y < this.height; y++)
      {
         y2 = y;
         diagoVal = work.matrix[diago];

         while(Matrix.isNul(diagoVal) == true)
         {
            y2++;

            if(y2 >= this.height)
            {
               this.determinantKnown = true;
               this.determinant = 0;
               return 0;
            }

            determinant *= -1;
            work.interExchangeRowInternal(y, y2);
            diagoVal = work.matrix[diago];
         }

         determinant *= diagoVal;
         work.pivoteInternal(y);
         diago += more;
      }

      this.determinantKnown = true;
      this.determinant = determinant;
      return determinant;
   }

   private void interExchangeRowInternal(final int y1, final int y2)
   {
      final int line1 = y1 * this.width;
      final int line2 = y2 * this.width;
      final double[] temp = new double[this.width];
      System.arraycopy(this.matrix, line1, temp, 0, this.width);
      System.arraycopy(this.matrix, line2, this.matrix, line1, this.width);
      System.arraycopy(temp, 0, this.matrix, line2, this.width);
   }

   private void pivoteInternal(final int xy)
   {
      final int startY = xy * this.width;
      int y = startY + this.width;
      int destination;
      int source;
      double coeficient;
      final double div = this.matrix[startY + xy];

      for(int yy = xy + 1; yy < this.height; yy++)
      {
         coeficient = this.matrix[y + xy];

         if(Matrix.isNul(coeficient) == false)
         {
            destination = y;
            source = startY;
            coeficient /= div;

            for(int xx = 0; xx < this.width; xx++)
            {
               this.matrix[destination++] -= coeficient * this.matrix[source++];
            }
         }

         y += this.width;
      }
   }

   private Matrix subMatrix(final int removedColumn, final int removedRow)
   {
      final int w = this.width - 1;
      final Matrix result = new Matrix(w, this.height - 1);
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

      result.determinantKnown = false;
      return result;
   }

   double determinantSubMatrix(final int x, final int y)
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
                     return (this.matrix[4] * this.matrix[8]) - (this.matrix[5] * this.matrix[7]);
                  case 1:
                     return (this.matrix[1] * this.matrix[8]) - (this.matrix[2] * this.matrix[7]);
                  case 2:
                     return (this.matrix[1] * this.matrix[5]) - (this.matrix[2] * this.matrix[4]);
               }
            case 1:
               switch(y)
               {
                  case 0:
                     return (this.matrix[3] * this.matrix[8]) - (this.matrix[5] * this.matrix[6]);
                  case 1:
                     return (this.matrix[0] * this.matrix[8]) - (this.matrix[2] * this.matrix[6]);
                  case 2:
                     return (this.matrix[0] * this.matrix[5]) - (this.matrix[2] * this.matrix[3]);
               }
            case 2:
               switch(y)
               {
                  case 0:
                     return (this.matrix[3] * this.matrix[7]) - (this.matrix[4] * this.matrix[6]);
                  case 1:
                     return (this.matrix[0] * this.matrix[7]) - (this.matrix[1] * this.matrix[6]);
                  case 2:
                     return (this.matrix[0] * this.matrix[4]) - (this.matrix[1] * this.matrix[3]);
               }
         }
      }

      return this.subMatrix(x, y).determinantInternalMore2();
   }

   public void addition(final Matrix matrix)
   {
      if((this.width != matrix.width) || (this.height != matrix.height))
      {
         throw new IllegalArgumentException("Matrix must have same size !");
      }

      for(int i = 0; i < this.size; i++)
      {
         this.matrix[i] += matrix.matrix[i];
      }

      this.determinantKnown = false;
   }

   public Matrix adjacent()
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
         final Matrix adjacent = new Matrix(2, 2);
         adjacent.matrix[0] = this.matrix[3];
         adjacent.matrix[1] = -this.matrix[1];
         adjacent.matrix[2] = -this.matrix[2];
         adjacent.matrix[3] = this.matrix[0];
         adjacent.determinantKnown = this.determinantKnown;
         adjacent.determinant = this.determinant;
         return adjacent;
      }

      final Matrix adjacent = new Matrix(this.width, this.height);
      int index = 0;
      double signMain = 1;
      final AtomicInteger count = new AtomicInteger(this.width);

      for(int x = 0; x < this.width; x++)
      {
         (new TaskAdjacent(count, adjacent.matrix, signMain, index, x, this.height, this)).start();
         signMain *= -1;
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

      adjacent.determinantKnown = this.determinantKnown;
      adjacent.determinant = Math.pow(this.determinant, this.width - 1);
      return adjacent;
   }

   public boolean canBeInvert()
   {
      return (this.isSquare() == true) && (Matrix.isNul(this.determinant()) == false);
   }

   public Matrix copy()
   {
      final Matrix matrix = new Matrix(this.width, this.height);
      System.arraycopy(this.matrix, 0, matrix.matrix, 0, this.size);
      matrix.determinantKnown = this.determinantKnown;
      matrix.determinant = this.determinant;
      return matrix;
   }

   public double determinant()
   {
      if(this.width != this.height)
      {
         throw new IllegalStateException("Matrix must be square");
      }

      if(this.determinantKnown == true)
      {
         return this.determinant;
      }

      return this.determinantInternal();
   }

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

      final Matrix matrix = (Matrix) object;

      if(this.height != matrix.height)
      {
         return false;
      }

      if(this.width != matrix.width)
      {
         return false;
      }

      for(int i = 0; i < this.size; i++)
      {
         if(Matrix.equals(this.matrix[i], matrix.matrix[i]) == false)
         {
            return false;
         }
      }

      return true;
   }

   public double get(final int x, final int y)
   {
      this.check(x, y);

      return this.matrix[x + (y * this.width)];
   }

   public int getHeight()
   {
      return this.height;
   }

   public int getWidth()
   {
      return this.width;
   }

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

   public Matrix invert()
   {
      if(this.width != this.height)
      {
         throw new IllegalStateException("Only square matrix can be invert");
      }

      final double determinant = this.determinant();

      if(Matrix.isNul(determinant) == true)
      {
         throw new IllegalArgumentException("Matrix determinant is zero, so have no invert");
      }

      final Matrix invert = this.adjacent();

      for(int i = 0; i < this.size; i++)
      {
         invert.matrix[i] /= determinant;
      }

      invert.determinantKnown = true;
      invert.determinant = 1 / determinant;

      return invert;
   }

   public boolean isIdentity()
   {
      final int diagonal = this.width + 1;
      final int max = this.width * this.width;

      for(int i = 0; i < this.size; i++)
      {
         if((i < max) && ((i % diagonal) == 0))
         {
            if(Matrix.equals(1, this.matrix[i]) == false)
            {
               return false;
            }
         }
         else if(Matrix.isNul(this.matrix[i]) == false)
         {
            return false;
         }
      }

      this.determinantKnown = this.width == this.height;
      this.determinant = 1;
      return true;
   }

   public boolean isSquare()
   {
      return this.width == this.height;
   }

   public boolean isZero()
   {
      for(int i = 0; i < this.size; i++)
      {
         if(Matrix.isNul(this.matrix[i]) == false)
         {
            return false;
         }
      }

      this.determinantKnown = this.width == this.height;
      this.determinant = 0;
      return true;
   }

   public void multiplication(final double factor)
   {
      for(int i = 0; i < this.size; i++)
      {
         this.matrix[i] *= factor;
      }

      this.determinant *= Math.pow(factor, this.width);
   }

   public Matrix multiplication(final Matrix matrix)
   {
      if((this.width != matrix.height) || (this.height != matrix.width))
      {
         throw new IllegalArgumentException("The multiplied matrix must have size : " + this.height + "x" + this.width);
      }

      final Matrix result = new Matrix(matrix.width, this.height);
      final AtomicInteger count = new AtomicInteger(result.size);

      for(int y = 0; y < this.height; y++)
      {
         for(int x = 0; x < matrix.width; x++)
         {
            (new TaskMultiplicationCell(count, x, y, this.width, matrix.width, matrix.width, result.matrix, this.matrix, matrix.matrix)).start();
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

      if((this.determinantKnown == true) && (matrix.determinantKnown == true))
      {
         result.determinantKnown = true;
         result.determinant = this.determinant * matrix.determinant;
      }
      else
      {
         result.determinantKnown = false;
      }

      return result;
   }

   public void pivote(final int xy)
   {
      if((xy < 0) || (xy >= this.width) || (xy >= this.height))
      {
         throw new IllegalArgumentException("It is a " + this.width + "x" + this.height + " matrix, but xy=" + xy);
      }

      if(Matrix.isNul(this.matrix[xy + (xy * this.width)]) == true)
      {
         return;
      }

      this.pivoteInternal(xy);
   }

   public void serializeBinary(final ByteArray byteArray)
   {
      byteArray.writeInteger(this.width);
      byteArray.writeInteger(this.height);
      byteArray.writeDoubleArray(this.matrix);
      byteArray.writeBoolean(this.determinantKnown);
      byteArray.writeDouble(this.determinant);
   }

   public void set(final int x, final int y, final double value)
   {
      this.check(x, y);
      this.matrix[x + (y * this.width)] = value;
      this.determinantKnown = false;
   }

   public void setValues(final double... values)
   {
      final int nb = Math.min(this.size, values.length);
      System.arraycopy(values, 0, this.matrix, 0, nb);

      for(int i = nb; i < this.size; i++)
      {
         this.matrix[i] = 0;
      }

      this.determinantKnown = false;
   }

   public void subtraction(final Matrix matrix)
   {
      if((this.width != matrix.width) || (this.height != matrix.height))
      {
         throw new IllegalArgumentException("Matrix must have same size !");
      }

      for(int i = 0; i < this.size; i++)
      {
         this.matrix[i] -= matrix.matrix[i];
      }

      this.determinantKnown = false;
   }

   public void toIdentity()
   {
      this.toZero();

      final int number = this.width * Math.min(this.width, this.height);

      for(int p = 0; p < number; p += this.width + 1)
      {
         this.matrix[p] = 1;
      }

      this.determinantKnown = this.width == this.height;
      this.determinant = 1;
   }

   @Override
   public String toString()
   {
      final StringBuilder stringBuilder = new StringBuilder("Matrix ");
      stringBuilder.append(this.width);
      stringBuilder.append('x');
      stringBuilder.append(this.height);

      if(this.determinantKnown == true)
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
         texts[i] = numberFormat.format(this.matrix[i]);
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

   public void toZero()
   {
      for(int i = 0; i < this.size; i++)
      {
         this.matrix[i] = 0;
      }

      this.determinantKnown = this.width == this.height;
      this.determinant = 0;
   }

   public Matrix transpose()
   {
      final Matrix transpose = new Matrix(this.height, this.width);
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

      transpose.determinantKnown = this.determinantKnown;
      transpose.determinant = this.determinant;

      return transpose;
   }
}