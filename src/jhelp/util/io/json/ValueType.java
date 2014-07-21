package jhelp.util.io.json;

/**
 * Type of JSON value
 * 
 * @author JHelp
 */
public enum ValueType
{
   /** JSON array */
   ARRAY,
   /** Boolean : <b>true</b> or <b>false</b> */
   BOOLEAN,
   /** Null value : <b>null</b> */
   NULL,
   /** Number value */
   NUMBER,
   /** JSON object */
   OBJECT,
   /** String */
   STRING
}