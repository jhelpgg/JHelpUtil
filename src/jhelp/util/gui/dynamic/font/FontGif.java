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
package jhelp.util.gui.dynamic.font;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import jhelp.util.gui.GIF;
import jhelp.util.list.Pair;
import jhelp.util.text.StringCutter;

/**
 * Font base on GIF animations
 * 
 * @author JHelp
 */
public class FontGif
{
   /** File that describe a font */
   private static final String           CHARACRCTERS = "characters.txt";
   /** List of association between a list of character and a GIF image */
   private final List<Pair<String, GIF>> gifs;
   /** Font height */
   private int                           height;
   /** Space size */
   private int                           space;

   /**
    * Create a new instance of FontGif
    * 
    * @param font
    *           Font folder name
    * @throws IOException
    *            On creation issue
    */
   public FontGif(final String font)
         throws IOException
   {
      this.gifs = new ArrayList<Pair<String, GIF>>();
      this.parseFont(font);
   }

   /**
    * Parse font description
    * 
    * @param font
    *           Font folder
    * @throws IOException
    *            On parsing issue
    */
   private void parseFont(final String font) throws IOException
   {
      BufferedReader bufferedReader = null;
      InputStream inputStream = null;
      final String header = font + "/";
      this.space = Integer.MAX_VALUE;
      this.height = 0;
      GIF gif;

      try
      {
         bufferedReader = new BufferedReader(new InputStreamReader(FontGif.class.getResourceAsStream(header + FontGif.CHARACRCTERS)));
         String line = bufferedReader.readLine();
         int index;
         String key, image;

         while(line != null)
         {
            if((line.length() > 0) && (line.charAt(0) != '#'))
            {
               index = line.indexOf('\t');

               if(index > 0)
               {
                  key = line.substring(0, index);

                  index = line.lastIndexOf('\t');

                  if(index > 0)
                  {
                     image = line.substring(index + 1);

                     inputStream = FontGif.class.getResourceAsStream(header + image);
                     gif = new GIF(inputStream);
                     inputStream.close();
                     inputStream = null;

                     this.gifs.add(new Pair<String, GIF>(key, gif));
                     this.space = Math.min(this.space, gif.getWidth());
                     this.height = Math.max(this.height, gif.getHeight());
                  }
               }
            }

            line = bufferedReader.readLine();
         }
      }
      catch(final Exception exception)
      {
         throw new IOException("Failed to parse font : " + font, exception);
      }
      finally
      {
         if(bufferedReader != null)
         {
            try
            {
               bufferedReader.close();
            }
            catch(final Exception exception)
            {
            }
         }

         if(inputStream != null)
         {
            try
            {
               inputStream.close();
            }
            catch(final Exception exception)
            {
            }
         }
      }
   }

   /**
    * Compute text description from a String
    * 
    * @param text
    *           String to get text description
    * @return Text description
    */
   public GifText computeGifText(final String text)
   {
      int width = 0;
      int y = 0;
      int x, index;
      final List<GifPosition> gifPositions = new ArrayList<GifPosition>();
      final StringCutter stringCutter = new StringCutter(text, '\n');
      String line = stringCutter.next();
      char[] characters;
      final int size = this.gifs.size();
      Pair<String, GIF> pair;
      GIF gif;

      while(line != null)
      {
         x = 0;
         characters = line.toCharArray();

         for(final char character : characters)
         {
            gif = null;

            for(index = 0; index < size; index++)
            {
               pair = this.gifs.get(index);

               if(pair.element1.indexOf(character) >= 0)
               {
                  gif = pair.element2;
                  break;
               }
            }

            if(gif != null)
            {
               gifPositions.add(new GifPosition(gif, x, y));
               x += gif.getWidth();
            }
            else
            {
               x += this.space;
            }
         }

         width = Math.max(x, width);
         y += this.height;
         line = stringCutter.next();
      }

      return new GifText(width, y, gifPositions);
   }
}