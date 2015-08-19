package jhelp.util.math;

import java.awt.Rectangle;
import java.math.BigDecimal;
import java.math.BigInteger;

import jhelp.util.list.ArrayInt;

/**
 * Utilities for math.<br>
 * It complete the {@link Math} class
 * 
 * @author JHelp
 */
public final class UtilMath
{
   /** -1 in high definition */
   public static final BigInteger BIG_MINUS_ONE       = UtilMath.createBigInteger(-1);
   /** 2 in high definition */
   public static final BigInteger BIG_TWO             = UtilMath.createBigInteger(2);
   /** One centimeter in pica */
   public static double           CENTIMETER_IN_PICA  = 6.0 / 2.54;
   /** One centimeter in ppoint */
   public static double           CENTIMETER_IN_POINT = 72.0 / 2.54;
   /** Epsilon */
   public static final double     EPSILON             = UtilMath.max(Double.MIN_NORMAL, Math.abs(Math.E - Math.exp(1)), Math.abs(Math.PI - Math.acos(-1)));
   /** One grade in degree */
   public static double           GRADE_IN_DEGREE     = 0.9;
   /** One grade in radian */
   public static double           GRADE_IN_RADIAN     = Math.PI / 200d;
   /** One inch in centimeter */
   public static double           INCH_IN_CENTIMETER  = 2.54;
   /** One inch in millimeter */
   public static double           INCH_IN_MILLIMETER  = 25.4;
   /** One inch in pica */
   public static double           INCH_IN_PICA        = 6.0;
   /** One inch in point */
   public static double           INCH_IN_POINT       = 72.0;
   /** ln(2) */
   public static final double     LOG_2               = Math.log(2);
   /** One millimeter in point */
   public static double           MILLIMETER_IN_POINT = 72.0 / 25.4;
   /** PI / 2 */
   public static double           PI_2                = Math.PI / 2;
   /** One pica in millimeter */
   public static double           PICA_IN_MILLIMETER  = 25.4 / 6.0;
   /** One pica in point */
   public static double           PICA_IN_POINT       = 12.0;
   /** 2 * PI */
   public static double           TWO_PI              = Math.PI * 2;

   /**
    * Compute the Bernouilli value
    * 
    * @param n
    *           Number of elements
    * @param m
    *           Total of elements
    * @param t
    *           Factor in [0, 1]
    * @return Bernouilli value
    */
   public static double Bernouilli(final int n, final int m, final double t)
   {
      return UtilMath.C(n, m) * Math.pow(t, n) * Math.pow(1d - t, m - n);
   }

   /**
    * Convert big integer to big decimal
    * 
    * @param integer
    *           Big integer to convert
    * @return Converted big decimal
    */
   public static BigDecimal bigIntegerToBigDecimal(final BigInteger integer)
   {
      return new BigDecimal(integer.toString());
   }

   /**
    * Compute the combination of N elements in M
    * 
    * <pre>
    *               m!
    * C(n, m) = -------------
    *            n! * (m-n)!
    * </pre>
    * 
    * The issue of ! is that becomes big fast, and if we apply the formula like that, we will quickly goes over int range and
    * result will become very random<br>
    * To solve this issue and have more big value we rewrite the formula (Here we consider <b>m&gt;n</b>, <b>n&gt;1</b> and
    * <b>m-n&gt;1</b>, other cases are easy to treat first)
    * 
    * <pre>
    *            m(m-1)..(n+1)n(n-1)...2            m(m-1)...(n+1)
    * C(n, m) = ------------------------------- = ------------------
    *            n(n-1)...1 * (m-n)(m-n-1)...2     (m-n)(m-n-1)...2
    * </pre>
    * 
    * Write like that less chance to over load int, but we can do better. <br>
    * For example in <b>(m-n)(m-n-1)...2</b> their 2, we are sure their a value in <b>m(m-1)...(n+1)</b> can be divide by
    * <b>2</b>, so if we divide by <b>2</b> this value before do multiplication we less the chance to overload int.<br>
    * This implementation is based on this idea, we first try simplify at maximum <b>m(m-1)...(n+1)</b> and
    * <b>(m-n)(m-n-1)...2</b> before doing multiplications to reduce the chance of out range int<br>
    * See inside code and inside comments to have more detail of algorithm
    * 
    * @param n
    *           Number of elements
    * @param m
    *           Total of elements
    * @return The combination of N elements in M
    */
   public static long C(final int n, final int m)
   {
      // Dummy cases

      if((n <= 0) || (m <= 0) || (n >= m))
      {
         return 1;
      }

      if((n == 1) || (m == (n + 1)))
      {
         return m;
      }

      // Real work : n>1 and m>n and m-n>1

      /**
       * Remember we want reduce
       * 
       * <pre>
       *             m(m-1)...(n+1)
       * C(n, m) =  ------------------
       *            (m-n)(m-n-1)...2
       * </pre>
       * 
       * We note:
       * 
       * <pre>
       *   min = Math.min(n, m-n)
       *   max = Math.max(n, m-n)
       * </pre>
       * 
       * Consider the 2 possibles situation :<br>
       * <br>
       * FIRST CASE : n==min AND m-n==max, so :
       * 
       * <pre>
       *             m(m-1)...(min+1)
       * C(n, m) =  ------------------
       *             max(max-1)...2
       * </pre>
       * 
       * But by nature min <= max so :
       * 
       * <pre>
       *             m(m-1)...(max+1)max(max-1)...(min+1)    m(m-1)...(max+1)
       * C(n, m) =  -------------------------------------- =------------------
       *             max(max-1)...(min+1)min(min-1)...2       min(min-1)...2
       * </pre>
       * 
       * <br>
       * SECOND CASE : n==max AND m-n==min, so
       * 
       * <pre>
       *             m(m-1)...(max+1)
       * C(n, m) =  ------------------
       *             min(min-1)...2
       * </pre>
       * 
       * <br>
       * CONCLUSION : <br>
       * We can already reduce the formula to :
       * 
       * <pre>
       *             m(m-1)...(max+1)
       * C(n, m) =  ------------------
       *             min(min-1)...2
       * </pre>
       */

      final int diff = m - n;
      final int min = Math.min(n, diff);
      final int max = Math.max(n, diff);

      // Collect numerator numbers
      final ArrayInt arrayInt = new ArrayInt();
      for(int i = m; i > max; i--)
      {
         arrayInt.add(i);
      }

      int size = arrayInt.getSize();
      int test;
      int num, gcd;
      long result = 1;

      // For each denominator number
      for(int i = min; i >= 2; i--)
      {
         // Current denominator number
         num = i;

         // For each left numerator numbers
         for(int j = 0; (j < size) && (num > 1); j++)
         {
            // Current numerator number
            test = arrayInt.getInteger(j);
            gcd = UtilMath.greaterCommonDivisor(num, test);

            // If we can simplify current denominator number with current numerator number
            if(gcd > 1)
            {
               // Simplify the denominator
               test /= gcd;

               if(test == 1)
               {
                  // If left nothing (just 1), remove the numerator from list
                  arrayInt.remove(j);
                  size--;
                  j--;
               }
               else
               {
                  // Update the numerator
                  arrayInt.setInteger(j, test);
               }

               // Simplify the denominator
               num /= gcd;
            }
         }
      }

      // We have consume all denominator, so it left only simplified numerator to multiply (No need to divide)
      // We are sure it left only numerator after simplification (1 for denominator)
      // This fact is due m>n and n>1 and m-n>1
      // IF min==n AND max==m-n THEN m=max+n , n=min => m=max+min
      // IF min==m-n AND max==n THEN m=min+n , n=max => m=max+min
      // => c(n,m) = (max+min)...(max+1) / min...2
      // For every p inside {2, 3, ..., min} we are sure to find at least one element in {(max+1), ..., (max+min)} that it can
      // divide
      // q divide p if q=rp (r in N)
      // max = ap+b (a,b in N, a>=0, b<p) => max + (p-b) in {(max+1), ..., (max+min)} AND max + (p-b) = ap+b+p-b = (a+1)p so
      // divide p

      for(int i = 0; i < size; i++)
      {
         result *= arrayInt.getInteger(i);
      }

      return result;
   }

   /**
    * Convert centimeter to inch
    * 
    * @param centimeter
    *           Centimeter to convert
    * @return Converted inch
    */
   public static double centimeterToInch(final double centimeter)
   {
      return centimeter / UtilMath.INCH_IN_CENTIMETER;
   }

   /**
    * Convert centimeter to millimeter
    * 
    * @param centimeter
    *           Centimeter to convert
    * @return Converted millimeter
    */
   public static double centimeterToMillimeter(final double centimeter)
   {
      return centimeter * 10.0;
   }

   /**
    * Convert centimeter to pica
    * 
    * @param centimeter
    *           Centimeter to convert
    * @return Converted pica
    */
   public static double centimeterToPica(final double centimeter)
   {
      return centimeter * UtilMath.CENTIMETER_IN_PICA;
   }

   /**
    * Convert centimeter to point
    * 
    * @param centimeter
    *           Centimeter to convert
    * @return Converted point
    */
   public static double centimeterToPoint(final double centimeter)
   {
      return centimeter * UtilMath.CENTIMETER_IN_POINT;
   }

   /**
    * Compute intersection area between two rectangles
    * 
    * @param rectangle1
    *           First rectangle
    * @param rectangle2
    *           Second rectangle
    * @return Computed area
    */
   public static int computeIntresectedArea(final Rectangle rectangle1, final Rectangle rectangle2)
   {
      final int xmin1 = rectangle1.x;
      final int xmax1 = rectangle1.x + rectangle1.width;
      final int ymin1 = rectangle1.y;
      final int ymax1 = rectangle1.y + rectangle1.height;
      final int xmin2 = rectangle2.x;
      final int xmax2 = rectangle2.x + rectangle2.width;
      final int ymin2 = rectangle2.y;
      final int ymax2 = rectangle2.y + rectangle2.height;

      if((xmin1 > xmax2) || (ymin1 > ymax2) || (xmin2 > xmax1) || (ymin2 > ymax1))
      {
         return 0;
      }

      final int xmin = Math.max(xmin1, xmin2);
      final int xmax = Math.min(xmax1, xmax2);
      if(xmin >= xmax)
      {
         return 0;
      }

      final int ymin = Math.max(ymin1, ymin2);
      final int ymax = Math.min(ymax1, ymax2);
      if(ymin >= ymax)
      {
         return 0;
      }

      return (xmax - xmin) * (ymax - ymin);
   }

   /**
    * Create big integer from an integer
    * 
    * @param value
    *           Integer base
    * @return Big integer created
    */
   public static BigInteger createBigInteger(final int value)
   {
      return new BigInteger(String.valueOf(value));
   }

   /**
    * Create big integer from an integer
    * 
    * @param value
    *           Integer base
    * @return Big integer created
    */
   public static BigInteger createBigInteger(final long value)
   {
      return new BigInteger(String.valueOf(value));
   }

   /**
    * Convert degree to grade
    * 
    * @param degree
    *           Degree to convert
    * @return Converted grade
    */
   public static double degreeToGrade(final double degree)
   {
      return degree * UtilMath.GRADE_IN_DEGREE;
   }

   /**
    * Convert degree to radian
    * 
    * @param degree
    *           Degree to convert
    * @return Converted radian
    */
   public static double degreeToRadian(final double degree)
   {
      return (degree * Math.PI) / 180.0;
   }

   /**
    * Indicates if 2 double are equal
    * 
    * @param real1
    *           First double
    * @param real2
    *           Second double
    * @return {@code true} if equals
    */
   public static boolean equals(final double real1, final double real2)
   {
      return Math.abs(real1 - real2) <= UtilMath.EPSILON;
   }

   /**
    * Compute the factorial of an integer
    * 
    * @param integer
    *           Integer to have is factorial
    * @return integer!
    */
   public static long factorial(int integer)
   {
      if(integer < 1)
      {
         return 0;
      }

      if(integer < 3)
      {
         return integer;
      }

      long factorial = integer;

      integer--;

      while(integer > 1)
      {
         factorial *= integer;

         integer--;
      }

      return factorial;
   }

   /**
    * Convert grade to degree
    * 
    * @param grade
    *           Grade to convert
    * @return Converted degree
    */
   public static double gradeToDegree(final double grade)
   {
      return grade / UtilMath.GRADE_IN_DEGREE;
   }

   /**
    * Convert grade to radian
    * 
    * @param grade
    *           Grade to convert
    * @return Converted radian
    */
   public static double gradeToRadian(final double grade)
   {
      return (grade * Math.PI) / 200.0;
   }

   /**
    * Compute the Greater Common Divisor of two integers.<br>
    * If both integers are 0, 0 is return
    * 
    * @param integer1
    *           First integer
    * @param integer2
    *           Second integer
    * @return GCD
    */
   public static int greaterCommonDivisor(int integer1, int integer2)
   {
      integer1 = Math.abs(integer1);
      integer2 = Math.abs(integer2);

      int min = Math.min(integer1, integer2);
      int max = Math.max(integer1, integer2);

      int temp;

      while(min > 0)
      {
         temp = min;

         min = max % min;
         max = temp;
      }

      return max;
   }

   /**
    * Compute the Greater Common Divisor of two integers.<br>
    * If both integers are 0, 0 is return
    * 
    * @param integer1
    *           First integer
    * @param integer2
    *           Second integer
    * @return GCD
    */
   public static long greaterCommonDivisor(long integer1, long integer2)
   {
      integer1 = Math.abs(integer1);
      integer2 = Math.abs(integer2);

      long min = Math.min(integer1, integer2);
      long max = Math.max(integer1, integer2);

      long temp;

      while(min > 0)
      {
         temp = min;

         min = max % min;
         max = temp;
      }

      return max;
   }

   /**
    * Convert inch to centimeter
    * 
    * @param inch
    *           Inch to convert
    * @return Converted centimeter
    */
   public static double inchToCentimeter(final double inch)
   {
      return inch * UtilMath.INCH_IN_CENTIMETER;
   }

   /**
    * Convert inch to millimeter
    * 
    * @param inch
    *           Inch to convert
    * @return Converted millimeter
    */
   public static double inchToMillimeter(final double inch)
   {
      return inch * UtilMath.INCH_IN_MILLIMETER;
   }

   /**
    * Convert inch to pica
    * 
    * @param inch
    *           Inch to convert
    * @return Converted pica
    */
   public static double inchToPica(final double inch)
   {
      return inch * UtilMath.INCH_IN_PICA;
   }

   /**
    * Convert inch to point
    * 
    * @param inch
    *           Inch to convert
    * @return Converted point
    */
   public static double inchToPoint(final double inch)
   {
      return inch * UtilMath.INCH_IN_POINT;
   }

   /**
    * Compute exponential interpolation.<br>
    * f : [0, 1] -> [0, 1]<br>
    * f(0)=0<br>
    * f(1)=1<br>
    * f is strictly increase
    * 
    * @param t
    *           Value to interpolate in [0, 1]
    * @return Interpolated result in [0, 1]
    */
   public static double interpolationExponential(final double t)
   {
      return Math.expm1(t) / (Math.E - 1d);
   }

   /**
    * Compute logarithm interpolation.<br>
    * f : [0, 1] -> [0, 1]<br>
    * f(0)=0<br>
    * f(1)=1<br>
    * f is strictly increase
    * 
    * @param t
    *           Value to interpolate in [0, 1]
    * @return Interpolated result in [0, 1]
    */
   public static double interpolationLogarithm(final double t)
   {
      return Math.log1p(t) / Math.log(2d);
   }

   /**
    * Compute sinus interpolation.<br>
    * f : [0, 1] -> [0, 1]<br>
    * f(0)=0<br>
    * f(1)=1<br>
    * f is strictly increase
    * 
    * @param t
    *           Value to interpolate in [0, 1]
    * @return Interpolated result in [0, 1]
    */
   public static double interpolationSinus(final double t)
   {
      return 0.5d + (Math.sin((t * Math.PI) - UtilMath.PI_2) / 2d);
   }

   /**
    * Indicates if a double is zero
    * 
    * @param real
    *           Double to test
    * @return {@code true} if zero
    */
   public static boolean isNul(final double real)
   {
      return Math.abs(real) <= UtilMath.EPSILON;
   }

   /**
    * Limit an integer between 2 values.<br>
    * If the integer is between given bounds, the integer is returned.<br>
    * If the integer is lower the minimum of the given bounds, the minimum is returned.<br>
    * If the integer is upper the maximum of the given bounds, the maximum is returned.
    * 
    * @param integer
    *           Integer to limit
    * @param bound1
    *           First bound
    * @param bound2
    *           Second bound
    * @return Limited integer
    */
   public static int limit(final int integer, final int bound1, final int bound2)
   {
      final int min = Math.min(bound1, bound2);
      final int max = Math.max(bound1, bound2);
      return Math.max(min, Math.min(max, integer));
   }

   /**
    * Limit an integer between 2 values.<br>
    * If the integer is between given bounds, the integer is returned.<br>
    * If the integer is lower the minimum of the given bounds, the minimum is returned.<br>
    * If the integer is upper the maximum of the given bounds, the maximum is returned.
    * 
    * @param integer
    *           Integer to limit
    * @param bound1
    *           First bound
    * @param bound2
    *           Second bound
    * @return Limited integer
    */
   public static long limit(final long integer, final long bound1, final long bound2)
   {
      final long min = Math.min(bound1, bound2);
      final long max = Math.max(bound1, bound2);
      return Math.max(min, Math.min(max, integer));
   }

   /**
    * Return the given integer, if the integer is in [0, 255]. If integer<0, we return 0, if integer>255, we return 255
    * 
    * @param integer
    *           Integer to limit in [0, 255]
    * @return Limited integer
    */
   public static int limit0_255(final int integer)
   {
      return UtilMath.limit(integer, 0, 255);
   }

   /**
    * Compute logarithm base 2 of a number
    * 
    * @param real
    *           Number to have is logarithm base 2
    * @return Logarithm base 2 of the number
    */
   public static double log2(final double real)
   {
      return Math.log(real) / UtilMath.LOG_2;
   }

   /**
    * Compute logarithm base 2 of a number
    * 
    * @param integer
    *           Number to have is logarithm base 2
    * @return Logarithm base 2 of the number
    */
   public static int log2(final int integer)
   {
      return (int) (Math.log(integer) / UtilMath.LOG_2);
   }

   /**
    * Compute logarithm base 2 of a number
    * 
    * @param integer
    *           Number to have is logarithm base 2
    * @return Logarithm base 2 of the number
    */
   public static int log2(final long integer)
   {
      return (int) (Math.log(integer) / UtilMath.LOG_2);
   }

   /**
    * Compute the Lower Common Multiple of two integers.<br>
    * If both integers are 0, 0 is return
    * 
    * @param integer1
    *           First integer
    * @param integer2
    *           Second integer
    * @return LCM
    */
   public static int lowerCommonMultiple(final int integer1, final int integer2)
   {
      final int gcd = UtilMath.greaterCommonDivisor(integer1, integer2);

      if(gcd == 0)
      {
         return 0;
      }

      return integer1 * (integer2 / gcd);
   }

   /**
    * Compute the Lower Common Multiple of two integers.<br>
    * If both integers are 0, 0 is return
    * 
    * @param integer1
    *           First integer
    * @param integer2
    *           Second integer
    * @return LCM
    */
   public static long lowerCommonMultiple(final long integer1, final long integer2)
   {
      final long gcd = UtilMath.greaterCommonDivisor(integer1, integer2);

      if(gcd == 0)
      {
         return 0;
      }

      return integer1 * (integer2 / gcd);
   }

   /**
    * Maximum of several double
    * 
    * @param doubles
    *           Doubles to have the maximum
    * @return Maximum of doubles
    */
   public static final double max(final double... doubles)
   {
      double max = doubles[0];

      for(final double real : doubles)
      {
         max = real > max
               ? real
               : max;
      }

      return max;
   }

   /**
    * Maximum of several integers
    * 
    * @param integers
    *           Integer to have the maximum
    * @return Maximum of integers
    */
   public static final int maxIntegers(final int... integers)
   {
      int max = integers[0];

      for(final int integer : integers)
      {
         max = integer > max
               ? integer
               : max;
      }

      return max;
   }

   /**
    * Convert millimeter to centimeter
    * 
    * @param millimeter
    *           Millimeter to convert
    * @return Converted centimeter
    */
   public static double millimeterToCentimeter(final double millimeter)
   {
      return millimeter * 0.1;
   }

   /**
    * Convert millimeter to inch
    * 
    * @param millimeter
    *           Millimeter to convert
    * @return Converted inch
    */
   public static double millimeterToInch(final double millimeter)
   {
      return millimeter / UtilMath.INCH_IN_MILLIMETER;
   }

   /**
    * Convert millimeter to pica
    * 
    * @param millimeter
    *           Millimeter to convert
    * @return Converted pica
    */
   public static double millimeterToPica(final double millimeter)
   {
      return millimeter / UtilMath.PICA_IN_MILLIMETER;
   }

   /**
    * Convert millimeter to point
    * 
    * @param millimeter
    *           Millimeter to convert
    * @return Converted point
    */
   public static double millimeterToPoint(final double millimeter)
   {
      return millimeter * UtilMath.MILLIMETER_IN_POINT;
   }

   /**
    * Minimum of several double
    * 
    * @param doubles
    *           Doubles to have the minimum
    * @return Minimum of doubles
    */
   public static final double min(final double... doubles)
   {
      double min = doubles[0];

      for(final double real : doubles)
      {
         min = real < min
               ? real
               : min;
      }

      return min;
   }

   /**
    * Minimum of several integers
    * 
    * @param integers
    *           Integer to have the minimum
    * @return Minimum of integers
    */
   public static final int minIntegers(final int... integers)
   {
      int min = integers[0];

      for(final int integer : integers)
      {
         min = integer < min
               ? integer
               : min;
      }

      return min;
   }

   /**
    * Compute the modulo of a real
    * 
    * @param real
    *           Real to modulate
    * @param modulo
    *           Modulo to use
    * @return Result
    */
   public static double modulo(final double real, final double modulo)
   {
      return UtilMath.moduloInterval(real, 0, modulo);
   }

   /**
    * Mathematical modulo.<br>
    * For computer -1 modulo 2 is -1, but in Mathematic -1[2]=1 (-1[2] : -1 modulo 2)
    * 
    * @param integer
    *           Integer to modulate
    * @param modulo
    *           Modulo to apply
    * @return Mathematical modulo : <code>integer[modulo]</code>
    */
   public static int modulo(int integer, final int modulo)
   {
      integer %= modulo;

      if(((integer < 0) && (modulo > 0)) || ((integer > 0) && (modulo < 0)))
      {
         integer += modulo;
      }

      return integer;
   }

   /**
    * Mathematical modulo.<br>
    * For computer -1 modulo 2 is -1, but in Mathematic -1[2]=1 (-1[2] : -1 modulo 2)
    * 
    * @param integer
    *           Integer to modulate
    * @param modulo
    *           Modulo to apply
    * @return Mathematical modulo : <code>integer[modulo]</code>
    */
   public static long modulo(long integer, final long modulo)
   {
      integer %= modulo;

      if(((integer < 0) && (modulo > 0)) || ((integer > 0) && (modulo < 0)))
      {
         integer += modulo;
      }

      return integer;
   }

   /**
    * Modulate a real inside an interval
    * 
    * @param real
    *           Real to modulate
    * @param min
    *           Minimum of interval
    * @param max
    *           Maximum of interval
    * @return Modulated value
    */
   public static double moduloInterval(double real, double min, double max)
   {
      if(min > max)
      {
         final double temp = min;
         min = max;
         max = temp;
      }

      if((real >= min) && (real <= max))
      {
         return real;
      }

      final double space = max - min;

      if(UtilMath.isNul(space) == true)
      {
         throw new IllegalArgumentException("Can't take modulo in empty interval");
      }

      real = (real - min) / space;

      return (space * (real - Math.floor(real))) + min;
   }

   /**
    * Compute the cubic interpolation
    * 
    * @param cp
    *           Start value
    * @param p1
    *           First control point
    * @param p2
    *           Second control point
    * @param p3
    *           Third control point
    * @param t
    *           Factor in [0, 1]
    * @return Interpolation
    */
   public static double PCubique(final double cp, final double p1, final double p2, final double p3, final double t)
   {
      final double u = 1d - t;
      return (u * u * u * cp) + (3d * t * u * u * p1) + (3d * t * t * u * p2) + (t * t * t * p3);
   }

   /**
    * Compute several cubic interpolation
    * 
    * @param cp
    *           Start value
    * @param p1
    *           First control point
    * @param p2
    *           Second control point
    * @param p3
    *           Third control point
    * @param precision
    *           Number of interpolation
    * @param cub
    *           Where write interpolations
    * @return Interpolations
    */
   public static double[] PCubiques(final double cp, final double p1, final double p2, final double p3, final int precision, double[] cub)
   {
      double step;
      double actual;
      int i;

      if((cub == null) || (cub.length < precision))
      {
         cub = new double[precision];
      }

      step = 1.0 / (precision - 1.0);
      actual = 0;
      for(i = 0; i < precision; i++)
      {
         if(i == (precision - 1))
         {
            actual = 1.0;
         }
         cub[i] = UtilMath.PCubique(cp, p1, p2, p3, actual);
         actual += step;
      }
      return cub;
   }

   /**
    * Convert pica to centimeter
    * 
    * @param pica
    *           Pica to convert
    * @return Converted centimeter
    */
   public static double picaToCentimeter(final double pica)
   {
      return pica / UtilMath.CENTIMETER_IN_PICA;
   }

   /**
    * Convert pica to inch
    * 
    * @param pica
    *           Pica to convert
    * @return Converted inch
    */
   public static double picaToInch(final double pica)
   {
      return pica / UtilMath.INCH_IN_PICA;
   }

   /**
    * Convert pica to millimeter
    * 
    * @param pica
    *           Pica to convert
    * @return Converted millimeter
    */
   public static double picaToMillimeter(final double pica)
   {
      return pica * UtilMath.PICA_IN_MILLIMETER;
   }

   /**
    * Convert pica to point
    * 
    * @param pica
    *           Pica to convert
    * @return Converted point
    */
   public static double picaToPoint(final double pica)
   {
      return pica * UtilMath.PICA_IN_POINT;
   }

   /**
    * Convert point to centimeter
    * 
    * @param point
    *           Point to convert
    * @return Converted centimeter
    */
   public static double pointToCentimeter(final double point)
   {
      return point / UtilMath.CENTIMETER_IN_POINT;
   }

   /**
    * Convert point to inch
    * 
    * @param point
    *           Point to convert
    * @return Converted inch
    */
   public static double pointToInch(final double point)
   {
      return point / UtilMath.INCH_IN_POINT;
   }

   /**
    * Convert point to millimeter
    * 
    * @param point
    *           Point to convert
    * @return Converted millimeter
    */
   public static double pointToMillimeter(final double point)
   {
      return point / UtilMath.MILLIMETER_IN_POINT;
   }

   /**
    * Convert point to point
    * 
    * @param point
    *           Point to convert
    * @return Converted point
    */
   public static double pointToPica(final double point)
   {
      return point / UtilMath.PICA_IN_POINT;
   }

   /**
    * Power of integer, more fast than {@link Math#pow(double, double)} for some case.<br>
    * integer<sup>pow</sup>
    * 
    * @param integer
    *           Integer to power of
    * @param pow
    *           Power to put
    * @return The result
    */
   public static long pow(final long integer, final long pow)
   {
      if(pow < 0)
      {
         throw new IllegalArgumentException("pow must be >=0, not " + pow);
      }

      if(pow == 0)
      {
         return 1;
      }

      if(pow == 1)
      {
         return integer;
      }

      if(pow == 2)
      {
         return integer * integer;
      }

      if(pow == 3)
      {
         return integer * integer * integer;
      }

      final long result = UtilMath.pow(integer, pow >> 1);

      if((pow & 1) == 0)
      {
         return result * result;
      }

      return integer * result * result;
   }

   /**
    * Compute the quadric interpolation
    * 
    * @param cp
    *           Start value
    * @param p1
    *           First control point
    * @param p2
    *           Second control point
    * @param t
    *           Factor in [0, 1]
    * @return Interpolation
    */
   public static double PQuadrique(final double cp, final double p1, final double p2, final double t)
   {
      final double u = 1d - t;
      return (u * u * cp) + (2d * t * u * p1) + (t * t * p2);
   }

   /**
    * Compute several quadric interpolation
    * 
    * @param cp
    *           Start value
    * @param p1
    *           First control point
    * @param p2
    *           Second control point
    * @param precision
    *           Number of interpolation
    * @param quad
    *           Where write interpolations
    * @return Interpolations
    */
   public static double[] PQuadriques(final double cp, final double p1, final double p2, final int precision, double[] quad)
   {
      double step;
      double actual;
      int i;

      if((quad == null) || (quad.length < precision))
      {
         quad = new double[precision];
      }

      step = 1.0 / (precision - 1.0);
      actual = 0;
      for(i = 0; i < precision; i++)
      {
         if(i == (precision - 1))
         {
            actual = 1.0;
         }
         quad[i] = UtilMath.PQuadrique(cp, p1, p2, actual);
         actual += step;
      }
      return quad;
   }

   /**
    * Convert radian to degree
    * 
    * @param radian
    *           Radian to convert
    * @return Converted degree
    */
   public static double radianToDegree(final double radian)
   {
      return (radian * 180.0) / Math.PI;
   }

   /**
    * Convert radian to grade
    * 
    * @param radian
    *           Radian to convert
    * @return Converted grade
    */
   public static double radianToGrade(final double radian)
   {
      return (radian * 200.0) / Math.PI;
   }

   /**
    * Sign of a double.<br>
    * The answer is like follow table:
    * <table border=1>
    * <tr>
    * <td><b><center>Double is</center></b></td>
    * <td><b><center>Then return</center></b></td>
    * </tr>
    * <tr>
    * <td><b><center>&lt; 0</center></b></td>
    * <td><b><center>-1</center></b></td>
    * </tr>
    * <tr>
    * <td><b><center>= 0</center></b></td>
    * <td><b><center>0</center></b></td>
    * </tr>
    * <tr>
    * <td><b><center>&gt; 0</center></b></td>
    * <td><b><center>1</center></b></td>
    * </tr>
    * </table>
    * 
    * @param real
    *           Double to have its sign
    * @return Double sign (-1, 0 or 1)
    */
   public static int sign(final double real)
   {
      if(UtilMath.isNul(real) == true)
      {
         return 0;
      }

      if(real < 0)
      {
         return -1;
      }

      return 1;
   }

   /**
    * Sign of an integer.<br>
    * It returns
    * <table>
    * <tr>
    * <th>-1</th>
    * <td>If integer is <0</td>
    * </tr>
    * <tr>
    * <th>0</th>
    * <td>If integer is 0</td>
    * </tr>
    * <tr>
    * <th>1</th>
    * <td>If integer is >0</td>
    * </tr>
    * </table>
    * 
    * @param integer
    *           Integer to have sign
    * @return Integer sign
    */
   public static final int sign(final int integer)
   {
      if(integer > 0)
      {
         return 1;
      }

      if(integer < 0)
      {
         return -1;
      }

      return 0;
   }

   /**
    * Sign of an integer.<br>
    * The answer is like follow table:
    * <table border=1>
    * <tr>
    * <td><b><center>Integer is</center></b></td>
    * <td><b><center>Then return</center></b></td>
    * </tr>
    * <tr>
    * <td><b><center>&lt; 0</center></b></td>
    * <td><b><center>-1</center></b></td>
    * </tr>
    * <tr>
    * <td><b><center>= 0</center></b></td>
    * <td><b><center>0</center></b></td>
    * </tr>
    * <tr>
    * <td><b><center>&gt; 0</center></b></td>
    * <td><b><center>1</center></b></td>
    * </tr>
    * </table>
    * 
    * @param integer
    *           Integer to have its sign
    * @return Integer sign (-1, 0 or 1)
    */
   public static int sign(final long integer)
   {
      if(integer == 0)
      {
         return 0;
      }
      if(integer > 0)
      {
         return 1;
      }
      return -1;
   }

   /**
    * Square of a number
    * 
    * @param real
    *           Number to square
    * @return Square result
    */
   public static double square(final double real)
   {
      return real * real;
   }

   /**
    * Square of a number
    * 
    * @param integer
    *           Number to square
    * @return Square result
    */
   public static int square(final int integer)
   {
      return integer * integer;
   }

   /**
    * To avoid instance
    */
   private UtilMath()
   {
   }
}