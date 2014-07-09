package jhelp.util.math.matrix;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Threaded task to compute the value of one cell during the matrix multiplication
 * 
 * @author JHelp
 */
class TaskMultiplicationCell
      extends Thread
{
   /** Number total task left */
   private final AtomicInteger count;
   /** First matrix in multiplication */
   private final double[]      first;
   /** First matrix width */
   private final int           firstWidth;
   /** Matrix where write the result */
   private final double[]      result;
   /** Matrix result width */
   private final int           resultWidth;
   /** Second matrix in multiplication */
   private final double[]      second;
   /** Second matrix width */
   private final int           secondWidth;
   /** Cell to compute X */
   private final int           x;
   /** Cell to compute Y */
   private final int           y;

   /**
    * Create a new instance of TaskMultiplicationCell
    * 
    * @param count
    *           Number total task left
    * @param x
    *           Cell to compute X
    * @param y
    *           Cell to compute Y
    * @param firstWidth
    *           First matrix width
    * @param secondWidth
    *           Second matrix width
    * @param resultWidth
    *           Matrix result width
    * @param result
    *           Matrix where write the result
    * @param first
    *           First matrix in multiplication
    * @param second
    *           Second matrix in multiplication
    */
   public TaskMultiplicationCell(final AtomicInteger count, final int x, final int y, final int firstWidth, final int secondWidth, final int resultWidth, final double[] result, final double[] first, final double[] second)
   {
      this.count = count;
      this.x = x;
      this.y = y;
      this.firstWidth = firstWidth;
      this.secondWidth = secondWidth;
      this.resultWidth = resultWidth;
      this.result = result;
      this.first = first;
      this.second = second;
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
      // Get coordinate
      int indexFirst = this.y * this.firstWidth;
      int indexSecond = this.x;
      double res = 0;

      // Compute cell value
      for(int i = 0; i < this.firstWidth; i++)
      {
         res += this.first[indexFirst] * this.second[indexSecond];
         indexFirst++;
         indexSecond += this.secondWidth;
      }

      this.result[this.x + (this.y * this.resultWidth)] = res;

      // The task is done, if its the last one, say its finish
      synchronized(this.count)
      {
         if(this.count.decrementAndGet() <= 0)
         {
            this.count.notify();
         }
      }
   }
}