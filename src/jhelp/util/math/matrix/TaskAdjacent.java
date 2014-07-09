package jhelp.util.math.matrix;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Threaded task when compute adjacent matrix.<br>
 * It compute one of adjacent line
 * 
 * @author JHelp
 */
class TaskAdjacent
      extends Thread
{
   /** Adjacent matrix result */
   private final double[]      adjacent;
   /** Number of task to do */
   private final AtomicInteger count;
   /** Matrix height */
   private final int           height;
   /** Index where start the line to write */
   private int                 index;
   /** Matrix to get its adjacent */
   private final Matrix        matrix;
   /** Current line sign */
   private double              sign;
   /** X in matrix */
   private final int           x;

   /**
    * Create a new instance of TaskAdjacent
    * 
    * @param count
    *           Number of task to do
    * @param adjacent
    *           Adjacent matrix result
    * @param sign
    *           Current line sign
    * @param index
    *           Index where start the line to write
    * @param x
    *           X in matrix
    * @param height
    *           Matrix height
    * @param matrix
    *           Matrix to get its adjacent
    */
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

   /**
    * Do the task <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @see java.lang.Thread#run()
    */
   @Override
   public void run()
   {
      // Compute adjacent line
      for(int y = 0; y < this.height; y++)
      {
         this.adjacent[this.index++] = this.sign * this.matrix.determinantSubMatrix(this.x, y);
         this.sign *= -1;
      }

      // Task is done, if its last one say its finish
      synchronized(this.count)
      {
         if(this.count.decrementAndGet() <= 0)
         {
            this.count.notify();
         }
      }
   }
}