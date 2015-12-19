/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 * 
 * @author JHelp
 */
package jhelp.util.gui.dynamic;

/**
 * Do an interpolation of value.<br>
 * Interpolation aim is to change the feeling of an interpolation.<br>
 * It is a function <b>f(x)</b> that MUST meet :
 * <ul>
 * <li>[0, 1] -f-> [0, 1]</li>
 * <li>f(0) = 0</li>
 * <li>f(1) = 1</li>
 * </ul>
 * <br>
 * We recommend that f is continues, else movement make some disappear/appear effects.<br>
 * We also recommends that function is increase, else transition shows some "go back" effect.
 * 
 * @author JHelp
 */
public interface Interpolation
{
   /**
    * Interpolate a [0, 1] value.<br>
    * The function <b>f(x)</b> MUST meet :
    * <ul>
    * <li>[0, 1] -f-> [0, 1]</li>
    * <li>f(0) = 0</li>
    * <li>f(1) = 1</li>
    * </ul>
    * <br>
    * We recommend that f is continues, else movement make some disappear/appear effects.<br>
    * We also recommends that function is increase, else transition shows some "go back" effect.
    * 
    * @param percent
    *           Value (in (0, 1]) to interpolate
    * @return Interpolation result (in [0, 1])
    */
   public float interpolation(float percent);
}