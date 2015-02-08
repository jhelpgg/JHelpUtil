package jhelp.util.gui.dynamic;

/**
 * Indicates that the object have a position
 * 
 * @author JHelp
 */
public interface Positionable
{
   /**
    * Current position
    * 
    * @return Current position
    */
   public Position getPosition();

   /**
    * Change position
    * 
    * @param position
    *           New position
    */
   public void setPosition(Position position);
}