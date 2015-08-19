package jhelp.util.math;

import jhelp.util.Utilities;

/**
 * Vector/point
 * 
 * @author JHelp
 */
public class VectorPoint
{
   /** Vector/point coordinates */
   private final double[] coordinate;

   /**
    * Create a new instance of VectorPoint
    * 
    * @param coordinate
    *           Vector/point coordinates
    */
   public VectorPoint(final double... coordinate)
   {
      if(coordinate == null)
      {
         throw new NullPointerException("coordinate musn't be null");
      }

      this.coordinate = coordinate;
   }

   /**
    * Create a new instance of VectorPoint
    * 
    * @param size
    *           Number of coordinate
    */
   public VectorPoint(final int size)
   {
      this.coordinate = new double[size];
   }

   /**
    * Get a coordinate
    * 
    * @param index
    *           Coordinate index
    * @return Coordinate at given index
    */
   public double getCoordinate(final int index)
   {
      return this.coordinate[index];
   }

   /**
    * Vector/point coordinates
    * 
    * @return Vector/point coordinates
    */
   public double[] getCoordinates()
   {
      return Utilities.createCopy(this.coordinate);
   }

   /**
    * Number of coordinate
    * 
    * @return Number of coordinate
    */
   public int numberOfCoordinate()
   {
      return this.coordinate.length;
   }

   /**
    * Change coordinate<br>
    * If given coordinates to short only first coordinates are changed<br>
    * If coordinates to long only the number of coordinate of this vector/point will be take in count
    * 
    * @param coordinate
    *           New coordinates
    */
   public void setCoordinate(final double... coordinate)
   {
      final int size = Math.min(this.coordinate.length, coordinate.length);

      if(size > 0)
      {
         System.arraycopy(coordinate, 0, this.coordinate, 0, size);
      }
   }

   /**
    * Change one coordinates
    * 
    * @param index
    *           Coordinate index
    * @param value
    *           New coordinate value
    */
   public void setCoordinate(final int index, final double value)
   {
      this.coordinate[index] = value;
   }
}