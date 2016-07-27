package jhelp.util.gui;

import jhelp.util.list.Pair;
import jhelp.util.math.UtilMath;
import jhelp.util.resources.ResourceText;
import jhelp.util.resources.Resources;

/**
 * Color with lot of information : ARGB, HSV, HSL and its name
 *
 * @author JHelp <br>
 */
@SuppressWarnings(
{
      "unchecked", "rawtypes"
})
public class FullColor
{
   /** Pairs of name/darkness for given HSV */
   private static final Pair<String, String> COLORS_HSV[][][];
   /** Resources where find color names */
   private static final Resources            RESOURCES;
   /** Key text for black */
   public static final String                BLACK;
   /** Key text for blue */
   public static final String                BLUE;
   /** Key text for brown */
   public static final String                BROWN;
   /** Key text for cyan */
   public static final String                CYAN;
   /** Key text for dark */
   public static final String                DARK;
   /** Key text for green */
   public static final String                GREEN;
   /** Key text for grey */
   public static final String                GREY;
   /** Key text for light */
   public static final String                LIGHT;
   /** Key text for magenta */
   public static final String                MAGENTA;
   /** Key text for orange */
   public static final String                ORANGE;
   /** Key text for pink */
   public static final String                PINK;
   /** Key text for red */
   public static final String                RED;
   /** Resources text where found value of key text depends on current language */
   public static final ResourceText          RESOURCE_TEXT;
   /** Key text for white */
   public static final String                WHITE;
   /** Key text for yellow */
   public static final String                YELLOW;

   static
   {
      BLACK = "black";
      BLUE = "blue";
      BROWN = "brown";
      CYAN = "cyan";
      DARK = "dark";
      GREEN = "green";
      GREY = "grey";
      LIGHT = "light";
      MAGENTA = "magenta";
      ORANGE = "orange";
      PINK = "pink";
      RED = "red";
      WHITE = "white";
      YELLOW = "yellow";
      RESOURCES = new Resources(FullColor.class);
      RESOURCE_TEXT = FullColor.RESOURCES.obtainResourceText("colors/colors");
      COLORS_HSV = new Pair[12][5][9];

      // saturation 0 case

      for(int h = 0; h < 12; h++)
      {
         FullColor.COLORS_HSV[h][0][8] = new Pair(FullColor.WHITE, null);
         FullColor.COLORS_HSV[h][0][7] = new Pair(FullColor.GREY, FullColor.LIGHT);
         FullColor.COLORS_HSV[h][0][6] = new Pair(FullColor.GREY, FullColor.LIGHT);
         FullColor.COLORS_HSV[h][0][5] = new Pair(FullColor.GREY, null);
         FullColor.COLORS_HSV[h][0][4] = new Pair(FullColor.GREY, null);
         FullColor.COLORS_HSV[h][0][3] = new Pair(FullColor.GREY, null);
         FullColor.COLORS_HSV[h][0][2] = new Pair(FullColor.GREY, FullColor.DARK);
         FullColor.COLORS_HSV[h][0][1] = new Pair(FullColor.BLACK, null);
         FullColor.COLORS_HSV[h][0][0] = new Pair(FullColor.BLACK, null);
      }

      // hue 0 ~ Red
      FullColor.COLORS_HSV[0][1][8] = new Pair(FullColor.PINK, null);
      FullColor.COLORS_HSV[0][1][7] = new Pair(FullColor.PINK, null);
      FullColor.COLORS_HSV[0][1][6] = new Pair(FullColor.PINK, FullColor.DARK);
      FullColor.COLORS_HSV[0][1][5] = new Pair(FullColor.GREY, null);
      FullColor.COLORS_HSV[0][1][4] = new Pair(FullColor.GREY, null);
      FullColor.COLORS_HSV[0][1][3] = new Pair(FullColor.GREY, FullColor.DARK);
      FullColor.COLORS_HSV[0][1][2] = new Pair(FullColor.GREY, FullColor.DARK);
      FullColor.COLORS_HSV[0][1][1] = new Pair(FullColor.BLACK, null);
      FullColor.COLORS_HSV[0][1][0] = new Pair(FullColor.BLACK, null);

      FullColor.COLORS_HSV[0][2][8] = new Pair(FullColor.RED, FullColor.LIGHT);
      FullColor.COLORS_HSV[0][2][7] = new Pair(FullColor.RED, FullColor.LIGHT);
      FullColor.COLORS_HSV[0][2][6] = new Pair(FullColor.RED, null);
      FullColor.COLORS_HSV[0][2][5] = new Pair(FullColor.RED, null);
      FullColor.COLORS_HSV[0][2][4] = new Pair(FullColor.RED, null);
      FullColor.COLORS_HSV[0][2][3] = new Pair(FullColor.RED, FullColor.DARK);
      FullColor.COLORS_HSV[0][2][2] = new Pair(FullColor.RED, FullColor.DARK);
      FullColor.COLORS_HSV[0][2][1] = new Pair(FullColor.BLACK, null);
      FullColor.COLORS_HSV[0][2][0] = new Pair(FullColor.BLACK, null);

      FullColor.COLORS_HSV[0][3][8] = new Pair(FullColor.RED, null);
      FullColor.COLORS_HSV[0][3][7] = new Pair(FullColor.RED, null);
      FullColor.COLORS_HSV[0][3][6] = new Pair(FullColor.RED, null);
      FullColor.COLORS_HSV[0][3][5] = new Pair(FullColor.RED, null);
      FullColor.COLORS_HSV[0][3][4] = new Pair(FullColor.RED, FullColor.DARK);
      FullColor.COLORS_HSV[0][3][3] = new Pair(FullColor.RED, FullColor.DARK);
      FullColor.COLORS_HSV[0][3][2] = new Pair(FullColor.RED, FullColor.DARK);
      FullColor.COLORS_HSV[0][3][1] = new Pair(FullColor.BLACK, null);
      FullColor.COLORS_HSV[0][3][0] = new Pair(FullColor.BLACK, null);

      FullColor.COLORS_HSV[0][4][8] = new Pair(FullColor.RED, null);
      FullColor.COLORS_HSV[0][4][7] = new Pair(FullColor.RED, null);
      FullColor.COLORS_HSV[0][4][6] = new Pair(FullColor.RED, null);
      FullColor.COLORS_HSV[0][4][5] = new Pair(FullColor.RED, null);
      FullColor.COLORS_HSV[0][4][4] = new Pair(FullColor.RED, FullColor.DARK);
      FullColor.COLORS_HSV[0][4][3] = new Pair(FullColor.RED, FullColor.DARK);
      FullColor.COLORS_HSV[0][4][2] = new Pair(FullColor.RED, FullColor.DARK);
      FullColor.COLORS_HSV[0][4][1] = new Pair(FullColor.BLACK, null);
      FullColor.COLORS_HSV[0][4][0] = new Pair(FullColor.BLACK, null);

      // hue 30 ~ yellow-red
      FullColor.COLORS_HSV[1][1][8] = new Pair(FullColor.ORANGE, FullColor.LIGHT);
      FullColor.COLORS_HSV[1][1][7] = new Pair(FullColor.ORANGE, FullColor.LIGHT);
      FullColor.COLORS_HSV[1][1][6] = new Pair(FullColor.GREY, null);
      FullColor.COLORS_HSV[1][1][5] = new Pair(FullColor.GREY, null);
      FullColor.COLORS_HSV[1][1][4] = new Pair(FullColor.GREY, null);
      FullColor.COLORS_HSV[1][1][3] = new Pair(FullColor.GREY, FullColor.DARK);
      FullColor.COLORS_HSV[1][1][2] = new Pair(FullColor.GREY, FullColor.DARK);
      FullColor.COLORS_HSV[1][1][1] = new Pair(FullColor.BLACK, null);
      FullColor.COLORS_HSV[1][1][0] = new Pair(FullColor.BLACK, null);

      FullColor.COLORS_HSV[1][2][8] = new Pair(FullColor.ORANGE, FullColor.LIGHT);
      FullColor.COLORS_HSV[1][2][7] = new Pair(FullColor.ORANGE, null);
      FullColor.COLORS_HSV[1][2][6] = new Pair(FullColor.ORANGE, null);
      FullColor.COLORS_HSV[1][2][5] = new Pair(FullColor.BROWN, null);
      FullColor.COLORS_HSV[1][2][4] = new Pair(FullColor.GREY, null);
      FullColor.COLORS_HSV[1][2][3] = new Pair(FullColor.GREY, FullColor.DARK);
      FullColor.COLORS_HSV[1][2][2] = new Pair(FullColor.GREY, FullColor.DARK);
      FullColor.COLORS_HSV[1][2][1] = new Pair(FullColor.BLACK, null);
      FullColor.COLORS_HSV[1][2][0] = new Pair(FullColor.BLACK, null);

      FullColor.COLORS_HSV[1][3][8] = new Pair(FullColor.ORANGE, null);
      FullColor.COLORS_HSV[1][3][7] = new Pair(FullColor.ORANGE, null);
      FullColor.COLORS_HSV[1][3][6] = new Pair(FullColor.ORANGE, null);
      FullColor.COLORS_HSV[1][3][5] = new Pair(FullColor.BROWN, null);
      FullColor.COLORS_HSV[1][3][4] = new Pair(FullColor.BROWN, null);
      FullColor.COLORS_HSV[1][3][3] = new Pair(FullColor.BROWN, null);
      FullColor.COLORS_HSV[1][3][2] = new Pair(FullColor.BROWN, null);
      FullColor.COLORS_HSV[1][3][1] = new Pair(FullColor.BLACK, null);
      FullColor.COLORS_HSV[1][3][0] = new Pair(FullColor.BLACK, null);

      FullColor.COLORS_HSV[1][4][8] = new Pair(FullColor.ORANGE, null);
      FullColor.COLORS_HSV[1][4][7] = new Pair(FullColor.ORANGE, null);
      FullColor.COLORS_HSV[1][4][6] = new Pair(FullColor.ORANGE, null);
      FullColor.COLORS_HSV[1][4][5] = new Pair(FullColor.BROWN, null);
      FullColor.COLORS_HSV[1][4][4] = new Pair(FullColor.BROWN, null);
      FullColor.COLORS_HSV[1][4][3] = new Pair(FullColor.BROWN, null);
      FullColor.COLORS_HSV[1][4][2] = new Pair(FullColor.BROWN, null);
      FullColor.COLORS_HSV[1][4][1] = new Pair(FullColor.BLACK, null);
      FullColor.COLORS_HSV[1][4][0] = new Pair(FullColor.BLACK, null);

      // hue 60 ~ yellow
      FullColor.COLORS_HSV[2][1][8] = new Pair(FullColor.YELLOW, FullColor.LIGHT);
      FullColor.COLORS_HSV[2][1][7] = new Pair(FullColor.YELLOW, FullColor.LIGHT);
      FullColor.COLORS_HSV[2][1][6] = new Pair(FullColor.GREY, null);
      FullColor.COLORS_HSV[2][1][5] = new Pair(FullColor.GREY, null);
      FullColor.COLORS_HSV[2][1][4] = new Pair(FullColor.GREY, null);
      FullColor.COLORS_HSV[2][1][3] = new Pair(FullColor.GREY, FullColor.DARK);
      FullColor.COLORS_HSV[2][1][2] = new Pair(FullColor.GREY, FullColor.DARK);
      FullColor.COLORS_HSV[2][1][1] = new Pair(FullColor.BLACK, null);
      FullColor.COLORS_HSV[2][1][0] = new Pair(FullColor.BLACK, null);

      FullColor.COLORS_HSV[2][2][8] = new Pair(FullColor.YELLOW, FullColor.LIGHT);
      FullColor.COLORS_HSV[2][2][7] = new Pair(FullColor.YELLOW, null);
      FullColor.COLORS_HSV[2][2][6] = new Pair(FullColor.YELLOW, null);
      FullColor.COLORS_HSV[2][2][5] = new Pair(FullColor.YELLOW, FullColor.DARK);
      FullColor.COLORS_HSV[2][2][4] = new Pair(FullColor.YELLOW, FullColor.DARK);
      FullColor.COLORS_HSV[2][2][3] = new Pair(FullColor.YELLOW, FullColor.DARK);
      FullColor.COLORS_HSV[2][2][2] = new Pair(FullColor.YELLOW, FullColor.DARK);
      FullColor.COLORS_HSV[2][2][1] = new Pair(FullColor.BLACK, null);
      FullColor.COLORS_HSV[2][2][0] = new Pair(FullColor.BLACK, null);

      FullColor.COLORS_HSV[2][3][8] = new Pair(FullColor.YELLOW, FullColor.LIGHT);
      FullColor.COLORS_HSV[2][3][7] = new Pair(FullColor.YELLOW, null);
      FullColor.COLORS_HSV[2][3][6] = new Pair(FullColor.YELLOW, null);
      FullColor.COLORS_HSV[2][3][5] = new Pair(FullColor.YELLOW, FullColor.DARK);
      FullColor.COLORS_HSV[2][3][4] = new Pair(FullColor.YELLOW, FullColor.DARK);
      FullColor.COLORS_HSV[2][3][3] = new Pair(FullColor.YELLOW, FullColor.DARK);
      FullColor.COLORS_HSV[2][3][2] = new Pair(FullColor.YELLOW, FullColor.DARK);
      FullColor.COLORS_HSV[2][3][1] = new Pair(FullColor.BLACK, null);
      FullColor.COLORS_HSV[2][3][0] = new Pair(FullColor.BLACK, null);

      FullColor.COLORS_HSV[2][4][8] = new Pair(FullColor.YELLOW, FullColor.LIGHT);
      FullColor.COLORS_HSV[2][4][7] = new Pair(FullColor.YELLOW, null);
      FullColor.COLORS_HSV[2][4][6] = new Pair(FullColor.YELLOW, null);
      FullColor.COLORS_HSV[2][4][5] = new Pair(FullColor.YELLOW, FullColor.DARK);
      FullColor.COLORS_HSV[2][4][4] = new Pair(FullColor.YELLOW, FullColor.DARK);
      FullColor.COLORS_HSV[2][4][3] = new Pair(FullColor.YELLOW, FullColor.DARK);
      FullColor.COLORS_HSV[2][4][2] = new Pair(FullColor.YELLOW, FullColor.DARK);
      FullColor.COLORS_HSV[2][4][1] = new Pair(FullColor.BLACK, null);
      FullColor.COLORS_HSV[2][4][0] = new Pair(FullColor.BLACK, null);

      // hue 90 ~ green-yellow
      FullColor.COLORS_HSV[3][1][8] = new Pair(FullColor.GREEN, FullColor.LIGHT);
      FullColor.COLORS_HSV[3][1][7] = new Pair(FullColor.GREEN, FullColor.LIGHT);
      FullColor.COLORS_HSV[3][1][6] = new Pair(FullColor.GREEN, null);
      FullColor.COLORS_HSV[3][1][5] = new Pair(FullColor.GREEN, null);
      FullColor.COLORS_HSV[3][1][4] = new Pair(FullColor.GREEN, FullColor.DARK);
      FullColor.COLORS_HSV[3][1][3] = new Pair(FullColor.GREEN, FullColor.DARK);
      FullColor.COLORS_HSV[3][1][2] = new Pair(FullColor.GREEN, FullColor.DARK);
      FullColor.COLORS_HSV[3][1][1] = new Pair(FullColor.BLACK, null);
      FullColor.COLORS_HSV[3][1][0] = new Pair(FullColor.BLACK, null);

      FullColor.COLORS_HSV[3][2][8] = new Pair(FullColor.GREEN, FullColor.LIGHT);
      FullColor.COLORS_HSV[3][2][7] = new Pair(FullColor.GREEN, FullColor.LIGHT);
      FullColor.COLORS_HSV[3][2][6] = new Pair(FullColor.GREEN, null);
      FullColor.COLORS_HSV[3][2][5] = new Pair(FullColor.GREEN, null);
      FullColor.COLORS_HSV[3][2][4] = new Pair(FullColor.GREEN, null);
      FullColor.COLORS_HSV[3][2][3] = new Pair(FullColor.GREEN, FullColor.DARK);
      FullColor.COLORS_HSV[3][2][2] = new Pair(FullColor.GREEN, FullColor.DARK);
      FullColor.COLORS_HSV[3][2][1] = new Pair(FullColor.BLACK, null);
      FullColor.COLORS_HSV[3][2][0] = new Pair(FullColor.BLACK, null);

      FullColor.COLORS_HSV[3][3][8] = new Pair(FullColor.GREEN, FullColor.LIGHT);
      FullColor.COLORS_HSV[3][3][7] = new Pair(FullColor.GREEN, FullColor.LIGHT);
      FullColor.COLORS_HSV[3][3][6] = new Pair(FullColor.GREEN, null);
      FullColor.COLORS_HSV[3][3][5] = new Pair(FullColor.GREEN, null);
      FullColor.COLORS_HSV[3][3][4] = new Pair(FullColor.GREEN, null);
      FullColor.COLORS_HSV[3][3][3] = new Pair(FullColor.GREEN, FullColor.DARK);
      FullColor.COLORS_HSV[3][3][2] = new Pair(FullColor.GREEN, FullColor.DARK);
      FullColor.COLORS_HSV[3][3][1] = new Pair(FullColor.BLACK, null);
      FullColor.COLORS_HSV[3][3][0] = new Pair(FullColor.BLACK, null);

      FullColor.COLORS_HSV[3][4][8] = new Pair(FullColor.GREEN, FullColor.LIGHT);
      FullColor.COLORS_HSV[3][4][7] = new Pair(FullColor.GREEN, FullColor.LIGHT);
      FullColor.COLORS_HSV[3][4][6] = new Pair(FullColor.GREEN, null);
      FullColor.COLORS_HSV[3][4][5] = new Pair(FullColor.GREEN, null);
      FullColor.COLORS_HSV[3][4][4] = new Pair(FullColor.GREEN, null);
      FullColor.COLORS_HSV[3][4][3] = new Pair(FullColor.GREEN, FullColor.DARK);
      FullColor.COLORS_HSV[3][4][2] = new Pair(FullColor.GREEN, FullColor.DARK);
      FullColor.COLORS_HSV[3][4][1] = new Pair(FullColor.BLACK, null);
      FullColor.COLORS_HSV[3][4][0] = new Pair(FullColor.BLACK, null);

      // hue 120 ~ green
      FullColor.COLORS_HSV[4][1][8] = new Pair(FullColor.GREEN, FullColor.LIGHT);
      FullColor.COLORS_HSV[4][1][7] = new Pair(FullColor.GREEN, FullColor.LIGHT);
      FullColor.COLORS_HSV[4][1][6] = new Pair(FullColor.GREEN, null);
      FullColor.COLORS_HSV[4][1][5] = new Pair(FullColor.GREEN, null);
      FullColor.COLORS_HSV[4][1][4] = new Pair(FullColor.GREEN, FullColor.DARK);
      FullColor.COLORS_HSV[4][1][3] = new Pair(FullColor.GREEN, FullColor.DARK);
      FullColor.COLORS_HSV[4][1][2] = new Pair(FullColor.GREEN, FullColor.DARK);
      FullColor.COLORS_HSV[4][1][1] = new Pair(FullColor.BLACK, null);
      FullColor.COLORS_HSV[4][1][0] = new Pair(FullColor.BLACK, null);

      FullColor.COLORS_HSV[4][2][8] = new Pair(FullColor.GREEN, FullColor.LIGHT);
      FullColor.COLORS_HSV[4][2][7] = new Pair(FullColor.GREEN, FullColor.LIGHT);
      FullColor.COLORS_HSV[4][2][6] = new Pair(FullColor.GREEN, null);
      FullColor.COLORS_HSV[4][2][5] = new Pair(FullColor.GREEN, null);
      FullColor.COLORS_HSV[4][2][4] = new Pair(FullColor.GREEN, null);
      FullColor.COLORS_HSV[4][2][3] = new Pair(FullColor.GREEN, FullColor.DARK);
      FullColor.COLORS_HSV[4][2][2] = new Pair(FullColor.GREEN, FullColor.DARK);
      FullColor.COLORS_HSV[4][2][1] = new Pair(FullColor.BLACK, null);
      FullColor.COLORS_HSV[4][2][0] = new Pair(FullColor.BLACK, null);

      FullColor.COLORS_HSV[4][3][8] = new Pair(FullColor.GREEN, FullColor.LIGHT);
      FullColor.COLORS_HSV[4][3][7] = new Pair(FullColor.GREEN, FullColor.LIGHT);
      FullColor.COLORS_HSV[4][3][6] = new Pair(FullColor.GREEN, null);
      FullColor.COLORS_HSV[4][3][5] = new Pair(FullColor.GREEN, null);
      FullColor.COLORS_HSV[4][3][4] = new Pair(FullColor.GREEN, null);
      FullColor.COLORS_HSV[4][3][3] = new Pair(FullColor.GREEN, FullColor.DARK);
      FullColor.COLORS_HSV[4][3][2] = new Pair(FullColor.GREEN, FullColor.DARK);
      FullColor.COLORS_HSV[4][3][1] = new Pair(FullColor.BLACK, null);
      FullColor.COLORS_HSV[4][3][0] = new Pair(FullColor.BLACK, null);

      FullColor.COLORS_HSV[4][4][8] = new Pair(FullColor.GREEN, FullColor.LIGHT);
      FullColor.COLORS_HSV[4][4][7] = new Pair(FullColor.GREEN, FullColor.LIGHT);
      FullColor.COLORS_HSV[4][4][6] = new Pair(FullColor.GREEN, null);
      FullColor.COLORS_HSV[4][4][5] = new Pair(FullColor.GREEN, null);
      FullColor.COLORS_HSV[4][4][4] = new Pair(FullColor.GREEN, null);
      FullColor.COLORS_HSV[4][4][3] = new Pair(FullColor.GREEN, FullColor.DARK);
      FullColor.COLORS_HSV[4][4][2] = new Pair(FullColor.GREEN, FullColor.DARK);
      FullColor.COLORS_HSV[4][4][1] = new Pair(FullColor.BLACK, null);
      FullColor.COLORS_HSV[4][4][0] = new Pair(FullColor.BLACK, null);

      // hue 150 ~ cyan-green
      FullColor.COLORS_HSV[5][1][8] = new Pair(FullColor.GREEN, FullColor.LIGHT);
      FullColor.COLORS_HSV[5][1][7] = new Pair(FullColor.GREEN, FullColor.LIGHT);
      FullColor.COLORS_HSV[5][1][6] = new Pair(FullColor.GREEN, null);
      FullColor.COLORS_HSV[5][1][5] = new Pair(FullColor.GREEN, null);
      FullColor.COLORS_HSV[5][1][4] = new Pair(FullColor.GREEN, FullColor.DARK);
      FullColor.COLORS_HSV[5][1][3] = new Pair(FullColor.GREEN, FullColor.DARK);
      FullColor.COLORS_HSV[5][1][2] = new Pair(FullColor.GREEN, FullColor.DARK);
      FullColor.COLORS_HSV[5][1][1] = new Pair(FullColor.BLACK, null);
      FullColor.COLORS_HSV[5][1][0] = new Pair(FullColor.BLACK, null);

      FullColor.COLORS_HSV[5][2][8] = new Pair(FullColor.GREEN, FullColor.LIGHT);
      FullColor.COLORS_HSV[5][2][7] = new Pair(FullColor.GREEN, FullColor.LIGHT);
      FullColor.COLORS_HSV[5][2][6] = new Pair(FullColor.GREEN, null);
      FullColor.COLORS_HSV[5][2][5] = new Pair(FullColor.GREEN, null);
      FullColor.COLORS_HSV[5][2][4] = new Pair(FullColor.GREEN, null);
      FullColor.COLORS_HSV[5][2][3] = new Pair(FullColor.GREEN, FullColor.DARK);
      FullColor.COLORS_HSV[5][2][2] = new Pair(FullColor.GREEN, FullColor.DARK);
      FullColor.COLORS_HSV[5][2][1] = new Pair(FullColor.BLACK, null);
      FullColor.COLORS_HSV[5][2][0] = new Pair(FullColor.BLACK, null);

      FullColor.COLORS_HSV[5][3][8] = new Pair(FullColor.GREEN, FullColor.LIGHT);
      FullColor.COLORS_HSV[5][3][7] = new Pair(FullColor.GREEN, FullColor.LIGHT);
      FullColor.COLORS_HSV[5][3][6] = new Pair(FullColor.GREEN, null);
      FullColor.COLORS_HSV[5][3][5] = new Pair(FullColor.GREEN, null);
      FullColor.COLORS_HSV[5][3][4] = new Pair(FullColor.GREEN, null);
      FullColor.COLORS_HSV[5][3][3] = new Pair(FullColor.GREEN, FullColor.DARK);
      FullColor.COLORS_HSV[5][3][2] = new Pair(FullColor.GREEN, FullColor.DARK);
      FullColor.COLORS_HSV[5][3][1] = new Pair(FullColor.BLACK, null);
      FullColor.COLORS_HSV[5][3][0] = new Pair(FullColor.BLACK, null);

      FullColor.COLORS_HSV[5][4][8] = new Pair(FullColor.GREEN, FullColor.LIGHT);
      FullColor.COLORS_HSV[5][4][7] = new Pair(FullColor.GREEN, FullColor.LIGHT);
      FullColor.COLORS_HSV[5][4][6] = new Pair(FullColor.GREEN, null);
      FullColor.COLORS_HSV[5][4][5] = new Pair(FullColor.GREEN, null);
      FullColor.COLORS_HSV[5][4][4] = new Pair(FullColor.GREEN, null);
      FullColor.COLORS_HSV[5][4][3] = new Pair(FullColor.GREEN, FullColor.DARK);
      FullColor.COLORS_HSV[5][4][2] = new Pair(FullColor.GREEN, FullColor.DARK);
      FullColor.COLORS_HSV[5][4][1] = new Pair(FullColor.BLACK, null);
      FullColor.COLORS_HSV[5][4][0] = new Pair(FullColor.BLACK, null);

      // hue 180 ~ cyan
      FullColor.COLORS_HSV[6][1][8] = new Pair(FullColor.CYAN, FullColor.LIGHT);
      FullColor.COLORS_HSV[6][1][7] = new Pair(FullColor.CYAN, null);
      FullColor.COLORS_HSV[6][1][6] = new Pair(FullColor.CYAN, null);
      FullColor.COLORS_HSV[6][1][5] = new Pair(FullColor.CYAN, null);
      FullColor.COLORS_HSV[6][1][4] = new Pair(FullColor.CYAN, FullColor.DARK);
      FullColor.COLORS_HSV[6][1][3] = new Pair(FullColor.CYAN, FullColor.DARK);
      FullColor.COLORS_HSV[6][1][2] = new Pair(FullColor.CYAN, FullColor.DARK);
      FullColor.COLORS_HSV[6][1][1] = new Pair(FullColor.BLACK, null);
      FullColor.COLORS_HSV[6][1][0] = new Pair(FullColor.BLACK, null);

      FullColor.COLORS_HSV[6][2][8] = new Pair(FullColor.CYAN, null);
      FullColor.COLORS_HSV[6][2][7] = new Pair(FullColor.CYAN, null);
      FullColor.COLORS_HSV[6][2][6] = new Pair(FullColor.CYAN, null);
      FullColor.COLORS_HSV[6][2][5] = new Pair(FullColor.CYAN, null);
      FullColor.COLORS_HSV[6][2][4] = new Pair(FullColor.CYAN, FullColor.DARK);
      FullColor.COLORS_HSV[6][2][3] = new Pair(FullColor.CYAN, FullColor.DARK);
      FullColor.COLORS_HSV[6][2][2] = new Pair(FullColor.CYAN, FullColor.DARK);
      FullColor.COLORS_HSV[6][2][1] = new Pair(FullColor.BLACK, null);
      FullColor.COLORS_HSV[6][2][0] = new Pair(FullColor.BLACK, null);

      FullColor.COLORS_HSV[6][3][8] = new Pair(FullColor.CYAN, null);
      FullColor.COLORS_HSV[6][3][7] = new Pair(FullColor.CYAN, null);
      FullColor.COLORS_HSV[6][3][6] = new Pair(FullColor.CYAN, null);
      FullColor.COLORS_HSV[6][3][5] = new Pair(FullColor.CYAN, null);
      FullColor.COLORS_HSV[6][3][4] = new Pair(FullColor.CYAN, FullColor.DARK);
      FullColor.COLORS_HSV[6][3][3] = new Pair(FullColor.CYAN, FullColor.DARK);
      FullColor.COLORS_HSV[6][3][2] = new Pair(FullColor.CYAN, FullColor.DARK);
      FullColor.COLORS_HSV[6][3][1] = new Pair(FullColor.BLACK, null);
      FullColor.COLORS_HSV[6][3][0] = new Pair(FullColor.BLACK, null);

      FullColor.COLORS_HSV[6][4][8] = new Pair(FullColor.CYAN, null);
      FullColor.COLORS_HSV[6][4][7] = new Pair(FullColor.CYAN, null);
      FullColor.COLORS_HSV[6][4][6] = new Pair(FullColor.CYAN, null);
      FullColor.COLORS_HSV[6][4][5] = new Pair(FullColor.CYAN, null);
      FullColor.COLORS_HSV[6][4][4] = new Pair(FullColor.CYAN, FullColor.DARK);
      FullColor.COLORS_HSV[6][4][3] = new Pair(FullColor.CYAN, FullColor.DARK);
      FullColor.COLORS_HSV[6][4][2] = new Pair(FullColor.CYAN, FullColor.DARK);
      FullColor.COLORS_HSV[6][4][1] = new Pair(FullColor.CYAN, FullColor.DARK);
      FullColor.COLORS_HSV[6][4][0] = new Pair(FullColor.BLACK, null);

      // hue 210 ~ blue-cyan
      FullColor.COLORS_HSV[7][1][8] = new Pair(FullColor.BLUE, FullColor.LIGHT);
      FullColor.COLORS_HSV[7][1][7] = new Pair(FullColor.BLUE, FullColor.LIGHT);
      FullColor.COLORS_HSV[7][1][6] = new Pair(FullColor.BLUE, null);
      FullColor.COLORS_HSV[7][1][5] = new Pair(FullColor.BLUE, null);
      FullColor.COLORS_HSV[7][1][4] = new Pair(FullColor.BLUE, FullColor.DARK);
      FullColor.COLORS_HSV[7][1][3] = new Pair(FullColor.BLUE, FullColor.DARK);
      FullColor.COLORS_HSV[7][1][2] = new Pair(FullColor.BLUE, FullColor.DARK);
      FullColor.COLORS_HSV[7][1][1] = new Pair(FullColor.BLACK, null);
      FullColor.COLORS_HSV[7][1][0] = new Pair(FullColor.BLACK, null);

      FullColor.COLORS_HSV[7][2][8] = new Pair(FullColor.BLUE, FullColor.LIGHT);
      FullColor.COLORS_HSV[7][2][7] = new Pair(FullColor.BLUE, null);
      FullColor.COLORS_HSV[7][2][6] = new Pair(FullColor.BLUE, null);
      FullColor.COLORS_HSV[7][2][5] = new Pair(FullColor.BLUE, null);
      FullColor.COLORS_HSV[7][2][4] = new Pair(FullColor.BLUE, FullColor.DARK);
      FullColor.COLORS_HSV[7][2][3] = new Pair(FullColor.BLUE, FullColor.DARK);
      FullColor.COLORS_HSV[7][2][2] = new Pair(FullColor.BLUE, FullColor.DARK);
      FullColor.COLORS_HSV[7][2][1] = new Pair(FullColor.BLACK, null);
      FullColor.COLORS_HSV[7][2][0] = new Pair(FullColor.BLACK, null);

      FullColor.COLORS_HSV[7][3][8] = new Pair(FullColor.BLUE, null);
      FullColor.COLORS_HSV[7][3][7] = new Pair(FullColor.BLUE, null);
      FullColor.COLORS_HSV[7][3][6] = new Pair(FullColor.BLUE, null);
      FullColor.COLORS_HSV[7][3][5] = new Pair(FullColor.BLUE, null);
      FullColor.COLORS_HSV[7][3][4] = new Pair(FullColor.BLUE, null);
      FullColor.COLORS_HSV[7][3][3] = new Pair(FullColor.BLUE, FullColor.DARK);
      FullColor.COLORS_HSV[7][3][2] = new Pair(FullColor.BLUE, FullColor.DARK);
      FullColor.COLORS_HSV[7][3][1] = new Pair(FullColor.BLACK, null);
      FullColor.COLORS_HSV[7][3][0] = new Pair(FullColor.BLACK, null);

      FullColor.COLORS_HSV[7][4][8] = new Pair(FullColor.BLUE, null);
      FullColor.COLORS_HSV[7][4][7] = new Pair(FullColor.BLUE, null);
      FullColor.COLORS_HSV[7][4][6] = new Pair(FullColor.BLUE, null);
      FullColor.COLORS_HSV[7][4][5] = new Pair(FullColor.BLUE, null);
      FullColor.COLORS_HSV[7][4][4] = new Pair(FullColor.BLUE, FullColor.DARK);
      FullColor.COLORS_HSV[7][4][3] = new Pair(FullColor.BLUE, FullColor.DARK);
      FullColor.COLORS_HSV[7][4][2] = new Pair(FullColor.BLUE, FullColor.DARK);
      FullColor.COLORS_HSV[7][4][1] = new Pair(FullColor.BLUE, FullColor.DARK);
      FullColor.COLORS_HSV[7][4][0] = new Pair(FullColor.BLACK, null);

      // hue 240 ~ blue
      FullColor.COLORS_HSV[8][1][8] = new Pair(FullColor.BLUE, FullColor.LIGHT);
      FullColor.COLORS_HSV[8][1][7] = new Pair(FullColor.BLUE, FullColor.LIGHT);
      FullColor.COLORS_HSV[8][1][6] = new Pair(FullColor.BLUE, null);
      FullColor.COLORS_HSV[8][1][5] = new Pair(FullColor.BLUE, null);
      FullColor.COLORS_HSV[8][1][4] = new Pair(FullColor.BLUE, FullColor.DARK);
      FullColor.COLORS_HSV[8][1][3] = new Pair(FullColor.BLUE, FullColor.DARK);
      FullColor.COLORS_HSV[8][1][2] = new Pair(FullColor.BLUE, FullColor.DARK);
      FullColor.COLORS_HSV[8][1][1] = new Pair(FullColor.BLACK, null);
      FullColor.COLORS_HSV[8][1][0] = new Pair(FullColor.BLACK, null);

      FullColor.COLORS_HSV[8][2][8] = new Pair(FullColor.BLUE, null);
      FullColor.COLORS_HSV[8][2][7] = new Pair(FullColor.BLUE, null);
      FullColor.COLORS_HSV[8][2][6] = new Pair(FullColor.BLUE, null);
      FullColor.COLORS_HSV[8][2][5] = new Pair(FullColor.BLUE, null);
      FullColor.COLORS_HSV[8][2][4] = new Pair(FullColor.BLUE, FullColor.DARK);
      FullColor.COLORS_HSV[8][2][3] = new Pair(FullColor.BLUE, FullColor.DARK);
      FullColor.COLORS_HSV[8][2][2] = new Pair(FullColor.BLUE, FullColor.DARK);
      FullColor.COLORS_HSV[8][2][1] = new Pair(FullColor.BLACK, null);
      FullColor.COLORS_HSV[8][2][0] = new Pair(FullColor.BLACK, null);

      FullColor.COLORS_HSV[8][3][8] = new Pair(FullColor.BLUE, null);
      FullColor.COLORS_HSV[8][3][7] = new Pair(FullColor.BLUE, null);
      FullColor.COLORS_HSV[8][3][6] = new Pair(FullColor.BLUE, null);
      FullColor.COLORS_HSV[8][3][5] = new Pair(FullColor.BLUE, null);
      FullColor.COLORS_HSV[8][3][4] = new Pair(FullColor.BLUE, null);
      FullColor.COLORS_HSV[8][3][3] = new Pair(FullColor.BLUE, FullColor.DARK);
      FullColor.COLORS_HSV[8][3][2] = new Pair(FullColor.BLUE, FullColor.DARK);
      FullColor.COLORS_HSV[8][3][1] = new Pair(FullColor.BLACK, null);
      FullColor.COLORS_HSV[8][3][0] = new Pair(FullColor.BLACK, null);

      FullColor.COLORS_HSV[8][4][8] = new Pair(FullColor.BLUE, null);
      FullColor.COLORS_HSV[8][4][7] = new Pair(FullColor.BLUE, null);
      FullColor.COLORS_HSV[8][4][6] = new Pair(FullColor.BLUE, null);
      FullColor.COLORS_HSV[8][4][5] = new Pair(FullColor.BLUE, null);
      FullColor.COLORS_HSV[8][4][4] = new Pair(FullColor.BLUE, FullColor.DARK);
      FullColor.COLORS_HSV[8][4][3] = new Pair(FullColor.BLUE, FullColor.DARK);
      FullColor.COLORS_HSV[8][4][2] = new Pair(FullColor.BLUE, FullColor.DARK);
      FullColor.COLORS_HSV[8][4][1] = new Pair(FullColor.BLUE, FullColor.DARK);
      FullColor.COLORS_HSV[8][4][0] = new Pair(FullColor.BLACK, null);

      // hue 270 ~ magenta-blue
      FullColor.COLORS_HSV[9][1][8] = new Pair(FullColor.MAGENTA, FullColor.LIGHT);
      FullColor.COLORS_HSV[9][1][7] = new Pair(FullColor.MAGENTA, FullColor.LIGHT);
      FullColor.COLORS_HSV[9][1][6] = new Pair(FullColor.MAGENTA, null);
      FullColor.COLORS_HSV[9][1][5] = new Pair(FullColor.MAGENTA, null);
      FullColor.COLORS_HSV[9][1][4] = new Pair(FullColor.MAGENTA, FullColor.DARK);
      FullColor.COLORS_HSV[9][1][3] = new Pair(FullColor.MAGENTA, FullColor.DARK);
      FullColor.COLORS_HSV[9][1][2] = new Pair(FullColor.MAGENTA, FullColor.DARK);
      FullColor.COLORS_HSV[9][1][1] = new Pair(FullColor.BLACK, null);
      FullColor.COLORS_HSV[9][1][0] = new Pair(FullColor.BLACK, null);

      FullColor.COLORS_HSV[9][2][8] = new Pair(FullColor.MAGENTA, null);
      FullColor.COLORS_HSV[9][2][7] = new Pair(FullColor.MAGENTA, null);
      FullColor.COLORS_HSV[9][2][6] = new Pair(FullColor.MAGENTA, null);
      FullColor.COLORS_HSV[9][2][5] = new Pair(FullColor.MAGENTA, null);
      FullColor.COLORS_HSV[9][2][4] = new Pair(FullColor.MAGENTA, FullColor.DARK);
      FullColor.COLORS_HSV[9][2][3] = new Pair(FullColor.MAGENTA, FullColor.DARK);
      FullColor.COLORS_HSV[9][2][2] = new Pair(FullColor.MAGENTA, FullColor.DARK);
      FullColor.COLORS_HSV[9][2][1] = new Pair(FullColor.BLACK, null);
      FullColor.COLORS_HSV[9][2][0] = new Pair(FullColor.BLACK, null);

      FullColor.COLORS_HSV[9][3][8] = new Pair(FullColor.MAGENTA, null);
      FullColor.COLORS_HSV[9][3][7] = new Pair(FullColor.MAGENTA, null);
      FullColor.COLORS_HSV[9][3][6] = new Pair(FullColor.MAGENTA, null);
      FullColor.COLORS_HSV[9][3][5] = new Pair(FullColor.MAGENTA, null);
      FullColor.COLORS_HSV[9][3][4] = new Pair(FullColor.MAGENTA, null);
      FullColor.COLORS_HSV[9][3][3] = new Pair(FullColor.MAGENTA, FullColor.DARK);
      FullColor.COLORS_HSV[9][3][2] = new Pair(FullColor.MAGENTA, FullColor.DARK);
      FullColor.COLORS_HSV[9][3][1] = new Pair(FullColor.BLACK, null);
      FullColor.COLORS_HSV[9][3][0] = new Pair(FullColor.BLACK, null);

      FullColor.COLORS_HSV[9][4][8] = new Pair(FullColor.MAGENTA, null);
      FullColor.COLORS_HSV[9][4][7] = new Pair(FullColor.MAGENTA, null);
      FullColor.COLORS_HSV[9][4][6] = new Pair(FullColor.MAGENTA, null);
      FullColor.COLORS_HSV[9][4][5] = new Pair(FullColor.MAGENTA, null);
      FullColor.COLORS_HSV[9][4][4] = new Pair(FullColor.MAGENTA, FullColor.DARK);
      FullColor.COLORS_HSV[9][4][3] = new Pair(FullColor.MAGENTA, FullColor.DARK);
      FullColor.COLORS_HSV[9][4][2] = new Pair(FullColor.MAGENTA, FullColor.DARK);
      FullColor.COLORS_HSV[9][4][1] = new Pair(FullColor.MAGENTA, FullColor.DARK);
      FullColor.COLORS_HSV[9][4][0] = new Pair(FullColor.BLACK, null);

      // hue 300 ~ magenta
      FullColor.COLORS_HSV[10][1][8] = new Pair(FullColor.PINK, FullColor.LIGHT);
      FullColor.COLORS_HSV[10][1][7] = new Pair(FullColor.PINK, FullColor.LIGHT);
      FullColor.COLORS_HSV[10][1][6] = new Pair(FullColor.PINK, null);
      FullColor.COLORS_HSV[10][1][5] = new Pair(FullColor.PINK, null);
      FullColor.COLORS_HSV[10][1][4] = new Pair(FullColor.PINK, FullColor.DARK);
      FullColor.COLORS_HSV[10][1][3] = new Pair(FullColor.PINK, FullColor.DARK);
      FullColor.COLORS_HSV[10][1][2] = new Pair(FullColor.PINK, FullColor.DARK);
      FullColor.COLORS_HSV[10][1][1] = new Pair(FullColor.BLACK, null);
      FullColor.COLORS_HSV[10][1][0] = new Pair(FullColor.BLACK, null);

      FullColor.COLORS_HSV[10][2][8] = new Pair(FullColor.PINK, null);
      FullColor.COLORS_HSV[10][2][7] = new Pair(FullColor.PINK, null);
      FullColor.COLORS_HSV[10][2][6] = new Pair(FullColor.PINK, null);
      FullColor.COLORS_HSV[10][2][5] = new Pair(FullColor.PINK, null);
      FullColor.COLORS_HSV[10][2][4] = new Pair(FullColor.MAGENTA, null);
      FullColor.COLORS_HSV[10][2][3] = new Pair(FullColor.MAGENTA, null);
      FullColor.COLORS_HSV[10][2][2] = new Pair(FullColor.MAGENTA, null);
      FullColor.COLORS_HSV[10][2][1] = new Pair(FullColor.BLACK, null);
      FullColor.COLORS_HSV[10][2][0] = new Pair(FullColor.BLACK, null);

      FullColor.COLORS_HSV[10][3][8] = new Pair(FullColor.PINK, null);
      FullColor.COLORS_HSV[10][3][7] = new Pair(FullColor.PINK, null);
      FullColor.COLORS_HSV[10][3][6] = new Pair(FullColor.PINK, null);
      FullColor.COLORS_HSV[10][3][5] = new Pair(FullColor.PINK, null);
      FullColor.COLORS_HSV[10][3][4] = new Pair(FullColor.PINK, null);
      FullColor.COLORS_HSV[10][3][3] = new Pair(FullColor.MAGENTA, null);
      FullColor.COLORS_HSV[10][3][2] = new Pair(FullColor.MAGENTA, null);
      FullColor.COLORS_HSV[10][3][1] = new Pair(FullColor.BLACK, null);
      FullColor.COLORS_HSV[10][3][0] = new Pair(FullColor.BLACK, null);

      FullColor.COLORS_HSV[10][4][8] = new Pair(FullColor.PINK, null);
      FullColor.COLORS_HSV[10][4][7] = new Pair(FullColor.PINK, null);
      FullColor.COLORS_HSV[10][4][6] = new Pair(FullColor.PINK, null);
      FullColor.COLORS_HSV[10][4][5] = new Pair(FullColor.PINK, null);
      FullColor.COLORS_HSV[10][4][4] = new Pair(FullColor.MAGENTA, null);
      FullColor.COLORS_HSV[10][4][3] = new Pair(FullColor.MAGENTA, null);
      FullColor.COLORS_HSV[10][4][2] = new Pair(FullColor.MAGENTA, null);
      FullColor.COLORS_HSV[10][4][1] = new Pair(FullColor.MAGENTA, null);
      FullColor.COLORS_HSV[10][4][0] = new Pair(FullColor.BLACK, null);

      // hue 330 ~ red-magenta
      FullColor.COLORS_HSV[11][1][8] = new Pair(FullColor.PINK, FullColor.LIGHT);
      FullColor.COLORS_HSV[11][1][7] = new Pair(FullColor.PINK, FullColor.LIGHT);
      FullColor.COLORS_HSV[11][1][6] = new Pair(FullColor.PINK, null);
      FullColor.COLORS_HSV[11][1][5] = new Pair(FullColor.PINK, null);
      FullColor.COLORS_HSV[11][1][4] = new Pair(FullColor.PINK, FullColor.DARK);
      FullColor.COLORS_HSV[11][1][3] = new Pair(FullColor.PINK, FullColor.DARK);
      FullColor.COLORS_HSV[11][1][2] = new Pair(FullColor.PINK, FullColor.DARK);
      FullColor.COLORS_HSV[11][1][1] = new Pair(FullColor.BLACK, null);
      FullColor.COLORS_HSV[11][1][0] = new Pair(FullColor.BLACK, null);

      FullColor.COLORS_HSV[11][2][8] = new Pair(FullColor.PINK, null);
      FullColor.COLORS_HSV[11][2][7] = new Pair(FullColor.PINK, null);
      FullColor.COLORS_HSV[11][2][6] = new Pair(FullColor.PINK, null);
      FullColor.COLORS_HSV[11][2][5] = new Pair(FullColor.PINK, null);
      FullColor.COLORS_HSV[11][2][4] = new Pair(FullColor.MAGENTA, null);
      FullColor.COLORS_HSV[11][2][3] = new Pair(FullColor.MAGENTA, null);
      FullColor.COLORS_HSV[11][2][2] = new Pair(FullColor.MAGENTA, null);
      FullColor.COLORS_HSV[11][2][1] = new Pair(FullColor.BLACK, null);
      FullColor.COLORS_HSV[11][2][0] = new Pair(FullColor.BLACK, null);

      FullColor.COLORS_HSV[11][3][8] = new Pair(FullColor.PINK, null);
      FullColor.COLORS_HSV[11][3][7] = new Pair(FullColor.PINK, null);
      FullColor.COLORS_HSV[11][3][6] = new Pair(FullColor.PINK, null);
      FullColor.COLORS_HSV[11][3][5] = new Pair(FullColor.PINK, null);
      FullColor.COLORS_HSV[11][3][4] = new Pair(FullColor.PINK, null);
      FullColor.COLORS_HSV[11][3][3] = new Pair(FullColor.MAGENTA, null);
      FullColor.COLORS_HSV[11][3][2] = new Pair(FullColor.MAGENTA, null);
      FullColor.COLORS_HSV[11][3][1] = new Pair(FullColor.BLACK, null);
      FullColor.COLORS_HSV[11][3][0] = new Pair(FullColor.BLACK, null);

      FullColor.COLORS_HSV[11][4][8] = new Pair(FullColor.PINK, null);
      FullColor.COLORS_HSV[11][4][7] = new Pair(FullColor.PINK, null);
      FullColor.COLORS_HSV[11][4][6] = new Pair(FullColor.PINK, null);
      FullColor.COLORS_HSV[11][4][5] = new Pair(FullColor.PINK, null);
      FullColor.COLORS_HSV[11][4][4] = new Pair(FullColor.MAGENTA, null);
      FullColor.COLORS_HSV[11][4][3] = new Pair(FullColor.MAGENTA, null);
      FullColor.COLORS_HSV[11][4][2] = new Pair(FullColor.MAGENTA, null);
      FullColor.COLORS_HSV[11][4][1] = new Pair(FullColor.MAGENTA, null);
      FullColor.COLORS_HSV[11][4][0] = new Pair(FullColor.BLACK, null);
   }

   /**
    * Compute hue (H in HSV/HSL) for given ARGB
    *
    * @param argb
    *           ARGB
    * @return Hue
    */
   public static double computeHue(final int argb)
   {
      final double r = ((argb >> 16) & 0xFF) / 255.0;
      final double g = ((argb >> 8) & 0xFF) / 255.0;
      final double b = (argb & 0xFF) / 255.0;
      final double min = UtilMath.min(r, g, b);
      final double max = UtilMath.max(r, g, b);
      final double chroma = max - min;

      if(UtilMath.isNul(chroma) == true)
      {
         return 0;
      }

      if(UtilMath.equals(max, r) == true)
      {
         return 60.0 * UtilMath.modulo((g - b) / chroma, 6.0);
      }

      if(UtilMath.equals(max, g) == true)
      {
         return 60.0 * (((b - r) / chroma) + 2.0);
      }

      return 60.0 * (((r - g) / chroma) + 4.0);
   }

   /** Alpha part */
   private final int    alpha;
   /** Blue part */
   private final int    blue;
   /** Chroma value */
   private final double chroma;
   /** Color in ARGB */
   private final int    color;
   /** Green part */
   private final int    green;
   /** Hue : H in HSL/HSV */
   private final double hue;
   /** Key text for darkness (Null if between dark and light, i.e "normal") */
   private final String keyDarkLight;
   /** Key text for name */
   private final String keyName;
   /** Lightness : L in HSL */
   private final double lightness;
   /** Luma */
   private final double luma;
   /** Red part */
   private final int    red;
   /** Saturation for HSL */
   private final double saturationHSL;
   /** Saturation for HSV */
   private final double saturationHSV;
   /** Value : V in HSV */
   private final double value;

   /**
    * Create full color with percent of RGB
    *
    * @param red
    *           Percent of red (in [0-1])
    * @param green
    *           Percent of green (in [0-1])
    * @param blue
    *           Percent of blue (in [0-1])
    */
   public FullColor(final double red, final double green, final double blue)
   {
      this((int) Math.round(red * 255), (int) Math.round(green * 255), (int) Math.round(blue * 255));
   }

   /**
    * Create full color with ARGB
    *
    * @param argb
    *           ARGB
    */
   public FullColor(final int argb)
   {
      this.color = argb;
      this.alpha = this.color >>> 24;
      this.red = (this.color >> 16) & 0xFF;
      this.green = (this.color >> 8) & 0xFF;
      this.blue = this.color & 0xFF;
      final double r = this.red / 255.0;
      final double g = this.green / 255.0;
      final double b = this.blue / 255.0;
      final double min = UtilMath.min(r, g, b);
      final double max = UtilMath.max(r, g, b);
      this.chroma = max - min;

      if(UtilMath.isNul(this.chroma) == true)
      {
         this.hue = 0;
      }
      else if(UtilMath.equals(max, r) == true)
      {
         this.hue = 60.0 * UtilMath.modulo((g - b) / this.chroma, 6.0);
      }
      else if(UtilMath.equals(max, g) == true)
      {
         this.hue = 60.0 * (((b - r) / this.chroma) + 2.0);
      }
      else
      {
         this.hue = 60.0 * (((r - g) / this.chroma) + 4.0);
      }

      this.value = max;
      this.lightness = (max + min) / 2.0;
      this.luma = (0.3 * r) + (0.59 * g) + (0.11 * b);

      if(UtilMath.isNul(this.chroma) == true)
      {
         this.saturationHSV = 0;
         this.saturationHSL = 0;
      }
      else
      {
         this.saturationHSV = this.chroma / this.value;
         this.saturationHSL = this.chroma / (1.0 - Math.abs((2.0 * this.lightness) - 1.0));
      }

      final int h = (((int) Math.round(this.hue) + 14) / 30) % 12;
      final int s = (int) Math.round(this.saturationHSV * 4);
      final int v = (int) Math.round(this.value * 8);
      final Pair<String, String> pair = FullColor.COLORS_HSV[h][s][v];
      this.keyName = pair.element1;
      this.keyDarkLight = pair.element2;
   }

   /**
    * Create a new instance of FullColor
    *
    * @param red
    *           Red part
    * @param green
    *           Green part
    * @param blue
    *           Blue part
    */
   public FullColor(final int red, final int green, final int blue)
   {
      this(255, red, green, blue);
   }

   /**
    * Create a new instance of FullColor
    *
    * @param alpha
    *           Alpha part
    * @param red
    *           Red part
    * @param green
    *           Green part
    * @param blue
    *           Blue part
    */
   public FullColor(final int alpha, final int red, final int green, final int blue)
   {
      this(((alpha & 0xFF) << 24) | ((red & 0xFF) << 16) | ((green & 0xFF) << 8) | (blue & 0xFF));
   }

   /**
    * Alpha part
    *
    * @return Alpha part
    */
   public int getAlpha()
   {
      return this.alpha;
   }

   /**
    * Base name
    *
    * @return Base name
    */
   public String getBaseName()
   {
      return FullColor.RESOURCE_TEXT.getText(this.keyName);
   }

   /**
    * Blue part
    *
    * @return Blue part
    */
   public int getBlue()
   {
      return this.blue;
   }

   /**
    * Chroma
    *
    * @return Chroma
    */
   public double getChroma()
   {
      return this.chroma;
   }

   /**
    * ARGB
    *
    * @return ARGB
    */
   public int getColor()
   {
      return this.color;
   }

   /**
    * Complete name
    *
    * @return Complete name
    */
   public String getColorName()
   {
      final StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(FullColor.RESOURCE_TEXT.getText(this.keyName));

      if(this.keyDarkLight != null)
      {
         stringBuilder.append(' ');
         stringBuilder.append(FullColor.RESOURCE_TEXT.getText(this.keyDarkLight));
      }

      return stringBuilder.toString();
   }

   /**
    * Green part
    *
    * @return Green part
    */
   public int getGreen()
   {
      return this.green;
   }

   /**
    * Hue
    *
    * @return Hue
    */
   public double getHue()
   {
      return this.hue;
   }

   /**
    * Key text use for dark/light name part. null if neutral
    *
    * @return Key text use for dark/light name part. null if neutral
    */
   public String getKeyDarkLight()
   {
      return this.keyDarkLight;
   }

   /**
    * Key text for base name
    *
    * @return Key text for base name
    */
   public String getKeyName()
   {
      return this.keyName;
   }

   /**
    * Lightness
    *
    * @return Lightness
    */
   public double getLightness()
   {
      return this.lightness;
   }

   /**
    * Luma
    *
    * @return Luma
    */
   public double getLuma()
   {
      return this.luma;
   }

   /**
    * Red part
    *
    * @return Red part
    */
   public int getRed()
   {
      return this.red;
   }

   /**
    * Saturation for HSL
    *
    * @return Saturation for HSL
    */
   public double getSaturationHSL()
   {
      return this.saturationHSL;
   }

   /**
    * Saturation for HSV
    *
    * @return Saturation for HSV
    */
   public double getSaturationHSV()
   {
      return this.saturationHSV;
   }

   /**
    * Value
    *
    * @return Value
    */
   public double getValue()
   {
      return this.value;
   }
}