package jhelp.util.math.matrix;

import java.util.concurrent.atomic.AtomicInteger;

class TaskAdjacent
      extends Thread
{
   private final double[]      adjacent;
   private final AtomicInteger count;
   private final int           height;
   private int                 index;
   private final Matrix        matrix;
   private double              sign;
   private final int           x;

   public TaskAdjacent(final AtomicInteger count, final double[] adjacent, final double sign, final int index, final int x, final int height, final Matrix matrix)
   {
      this.count = count;
      this.adjacent = adjacent;
      this.sign = sign;
      this.index = index;
      this.x = x;
      this.height = height;
      this.matrix = matrix;
   }

   @Override
   public void run()
   {
      for(int y = 0; y < this.height; y++)
      {
         this.adjacent[this.index++] = this.sign * this.matrix.determinantSubMatrix(this.x, y);
         this.sign *= -1;
      }

      synchronized(this.count)
      {
         if(this.count.decrementAndGet() <= 0)
         {
            this.count.notify();
         }
      }
   }
}