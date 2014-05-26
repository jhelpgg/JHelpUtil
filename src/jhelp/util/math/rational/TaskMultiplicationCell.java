package jhelp.util.math.rational;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Task dedicated to matrix multiplication, to do jobs in parallel (To speed up computing)
 * 
 * @author JHelp
 */
class TaskMultiplicationCell
      extends Thread
{
   /** Number task left */
   private final AtomicInteger count;
   /** First matrix in multiplication parameter */
   private final Rational[]    first;
   /** First matrix width */
   private final int           firstWidth;
   /** Result matrix */
   private final Rational[]    result;
   /** Result matrix width */
   private final int           resultWidth;
   /** Second matrix in multiplication parameter */
   private final Rational[]    second;
   /** Second matrix width */
   private final int           secondWidth;
   /** X of cell dedicated to the task */
   private final int           x;
   /** Y of cell dedicated to the task */
   private final int           y;

   /**
    * Create a new instance of TaskMultiplicationCell
    * 
    * @param count
    *           Number task left (Use for know when all tasks are done)
    * @param x
    *           X of the task cell
    * @param y
    *           Y of the task cell
    * @param firstWidth
    *           First matrix width
    * @param secondWidth
    *           Second matrix width
    * @param resultWidth
    *           Result matrix width
    * @param result
    *           Result matrix
    * @param first
    *           First matrix
    * @param second
    *           Second matrix
    */
   public TaskMultiplicationCell(final AtomicInteger count, final int x, final int y, final int firstWidth, final int secondWidth, final int resultWidth, final Rational[] result, final Rational[] first, final Rational[] second)
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
      int indexFirst = this.y * this.firstWidth;
      int indexSecond = this.x;
      Rational res = Rational.ZERO;

      for(int i = 0; i < this.firstWidth; i++)
      {
         res = res.addition(this.first[indexFirst].multiply(this.second[indexSecond]));
         indexFirst++;
         indexSecond += this.secondWidth;
      }

      this.result[this.x + (this.y * this.resultWidth)] = res;

      // We have done one task, if its the last one wake up the sleeper
      synchronized(this.count)
      {
         if(this.count.decrementAndGet() <= 0)
         {
            this.count.notify();
         }
      }
   }
}