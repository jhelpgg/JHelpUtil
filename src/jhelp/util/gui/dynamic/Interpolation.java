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