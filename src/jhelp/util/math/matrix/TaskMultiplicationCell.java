package jhelp.util.math.matrix;

import java.util.concurrent.atomic.AtomicInteger;

class TaskMultiplicationCell
      extends Thread
{
   private final AtomicInteger count;
   private final double[]      first;
   private final int           firstWidth;
   private final double[]      result;
   private final int           resultWidth;
   private final double[]      second;
   private final int           secondWidth;
   private final int           x;
   private final int           y;

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

   @Override
   public void run()
   {
      int indexFirst = this.y * this.firstWidth;
      int indexSecond = this.x;
      double res = 0;

      for(int i = 0; i < this.firstWidth; i++)
      {
         res += this.first[indexFirst] * this.second[indexSecond];
         indexFirst++;
         indexSecond += this.secondWidth;
      }

      this.result[this.x + (this.y * this.resultWidth)] = res;

      synchronized(this.count)
      {
         if(this.count.decrementAndGet() <= 0)
         {
            this.count.notify();
         }
      }
   }
}