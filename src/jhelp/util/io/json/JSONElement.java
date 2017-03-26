/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not
 * responsible about any damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that
 * avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.util.io.json;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a field will be serialize with {@link JSONWriter} and parse with {@link JSONReader}.<br>
 * This annotation will only work for field of type :
 * <ul>
 * <li>primitives : boolean, char, byte, short, int, long, float, double</li>
 * <li>primitives Object representation : {@link Boolean}, {@link Character}, {@link Byte}, {@link Short},
 * {@link Integer}, {@link Long}, {@link Float}, {@link Double})</li>
 * <li>{@link String}</li>
 * <li>enum</li>
 * <li>objects annotated {@link JSONObject} and respects the contract</li>
 * </ul>
 * For convenience, it exits {@link JSONList} and {@link JSONMap} that are valid {@link JSONObject}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface JSONElement
{
}
