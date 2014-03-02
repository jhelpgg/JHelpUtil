package jhelp.util.preference;

import java.io.File;
import java.util.Locale;

/**
 * Preference type
 * 
 * @author JHelp
 */
public enum PreferenceType
{
   /** Type byte[] */
   ARRAY,
   /** Type boolean */
   BOOLEAN,
   /** Type {@link File} */
   FILE,
   /** Type int */
   INTEGER,
   /** Type {@link Locale} */
   LOCALE,
   /** Type {@link String} */
   STRING
}