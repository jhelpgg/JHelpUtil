/*
 * License :
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any damage it may cause.
 * You can use, modify, the code as your need for any usage.
 * But you can't do any action that avoid me or other person use, modify this code.
 * The code is free for usage and modification, you can't change that fact.
 * JHelp
 */

/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any
 * damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.util.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import jhelp.util.Utilities;
import jhelp.util.gui.resolution.Resolution;
import jhelp.util.gui.resolution.ResolutionUnit;
import jhelp.util.math.UtilMath;

/**
 * Utilities for GUI
 *
 * @author JHelp
 */
public final class UtilGUI
{
    /**
     * Special character for delete
     */
    public static final char CHARACTER_DELETE = '\b';
    /**
     * Special character for escape
     */
    public static final char CHARACTER_ESCAPE = (char) 0x1B;
    /**
     * Current graphics environment
     */
    public static final GraphicsEnvironment GRAPHICS_ENVIRONMENT;
    /**
     * Current screen resolution
     */
    public static final Resolution          SCREEN_RESOLUTION;
    /**
     * Font use for sub title
     */
    public static final Font SUB_TITLE_FONT = new Font("Arial", Font.PLAIN, 22);
    /**
     * Font use for title
     */
    public static final Font TITLE_FONT     = new Font("Arial", Font.BOLD, 24);
    /**
     * Current toolkit
     */
    public static final  Toolkit          TOOLKIT;
    /**
     * List of graphics devices
     */
    private static final GraphicsDevice[] GRAPHICS_DEVICES;
    /**
     * Robot for simulate keyboard and mouse events
     */
    private final static Robot            ROBOT;
    /**
     * Empty image
     */
    private static       BufferedImage    EMPTY_IMAGE;
    /**
     * Invisible cursor
     */
    private static       Cursor           INVISIBLE_CURSOR;

    static
    {
        Robot robot = null;

        try
        {
            robot = new Robot();
        }
        catch (final Exception exception)
        {
            robot = null;
        }

        ROBOT = robot;

        //

        final Toolkit             toolkit             = Toolkit.getDefaultToolkit();
        final GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        final GraphicsDevice[]    graphicsDevices     = graphicsEnvironment.getScreenDevices();

        TOOLKIT = toolkit;
        GRAPHICS_ENVIRONMENT = graphicsEnvironment;
        GRAPHICS_DEVICES = graphicsDevices;
        SCREEN_RESOLUTION = new Resolution(UtilGUI.TOOLKIT.getScreenResolution(), ResolutionUnit.PIXEL_PER_INCH);
    }

    /**
     * For avoid instance
     */
    private UtilGUI()
    {
    }

    /**
     * Add sub-title to component to have a title but visually less important than title add with
     * {@link #addTitle(JComponent, String)}
     *
     * @param component Component to add sub-title
     * @param title     Sub-title text
     */
    public static void addSubTitle(final JComponent component, final String title)
    {
        if ((component == null) || (title == null))
        {
            return;
        }

        component.setBorder(//
                            BorderFactory.createTitledBorder(//
                                                             BorderFactory.createEtchedBorder(), //
                                                             title, TitledBorder.CENTER, TitledBorder.TOP,
                                                             UtilGUI.SUB_TITLE_FONT));
    }

    /**
     * Add title to component
     *
     * @param component Component to add title
     * @param title     Title
     */
    public static void addTitle(final JComponent component, final String title)
    {
        if ((component == null) || (title == null))
        {
            return;
        }

        component.setBorder(//
                            BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), //
                                                             title, TitledBorder.CENTER, TitledBorder.TOP,
                                                             UtilGUI.TITLE_FONT));
    }

    /**
     * Center a window on its screen
     *
     * @param window Widow to center
     */
    public static void centerOnScreen(final Window window)
    {
        final Dimension dimension = window.getSize();
        final Rectangle screen    = UtilGUI.computeScreenRectangle(window);
        window.setLocation(screen.x + ((screen.width - dimension.width) / 2), //
                           screen.y + ((screen.height - dimension.height) / 2));
    }

    /**
     * Compute the rectangle of the screen where is a window
     *
     * @param window Window we looking for its screen
     * @return Screen's rectangle
     */
    public static Rectangle computeScreenRectangle(final Window window)
    {
        final Rectangle windowBounds = window.getBounds();

        GraphicsConfiguration graphicsConfiguration = UtilGUI.GRAPHICS_DEVICES[0].getDefaultConfiguration();
        Rectangle             screenBounds          = graphicsConfiguration.getBounds();
        int                   areaMax               = UtilMath.computeIntresectedArea(windowBounds, screenBounds);

        int totalWidth  = screenBounds.x + screenBounds.width;
        int totalHeight = screenBounds.y + screenBounds.height;

        Rectangle             bounds;
        int                   area;
        GraphicsConfiguration cg;
        for (int i = 1; i < UtilGUI.GRAPHICS_DEVICES.length; i++)
        {
            cg = UtilGUI.GRAPHICS_DEVICES[i].getDefaultConfiguration();
            bounds = cg.getBounds();
            area = UtilMath.computeIntresectedArea(windowBounds, bounds);

            totalWidth = Math.max(totalWidth, bounds.x + bounds.width);
            totalHeight = Math.max(totalHeight, bounds.y + bounds.height);

            if (area > areaMax)
            {
                graphicsConfiguration = cg;
                screenBounds = bounds;
                areaMax = area;
            }
        }

        final Insets margin = UtilGUI.TOOLKIT.getScreenInsets(graphicsConfiguration);

        final Rectangle screenRectangle = new Rectangle(screenBounds);
        screenRectangle.x = margin.left;
        screenRectangle.y = margin.top;
        screenRectangle.width = totalWidth - margin.left - margin.right;
        screenRectangle.height = totalHeight - margin.top - margin.bottom;

        return screenRectangle;
    }

    /**
     * Change a window of screen
     *
     * @param window      Widow to translate
     * @param screenIndex Destination screen
     */
    public static void changeScreen(final Window window, final int screenIndex)
    {
        UtilGUI.checkScreenIndex(screenIndex);

        final Rectangle sourceScreen = UtilGUI.computeScreenRectangle(window);
        final int       x            = window.getX() - sourceScreen.x;
        final int       y            = window.getY() - sourceScreen.y;

        final GraphicsConfiguration graphicsConfiguration = UtilGUI.GRAPHICS_DEVICES[screenIndex]
                .getDefaultConfiguration();
        final Rectangle destinationScreen = graphicsConfiguration.getBounds();
        final Insets    insets            = UtilGUI.TOOLKIT.getScreenInsets(graphicsConfiguration);

        window.setLocation(x + destinationScreen.x + insets.left, //
                           y + destinationScreen.y + insets.top);
    }

    /**
     * Check if a screen index is valid
     *
     * @param screenIndex Screen index to check
     */
    private static void checkScreenIndex(final int screenIndex)
    {
        if ((screenIndex < 0) || (screenIndex >= UtilGUI.GRAPHICS_DEVICES.length))
        {
            throw new IllegalArgumentException(
                    "You have " + UtilGUI.GRAPHICS_DEVICES.length + " screens so the screen index must be in [0, "
                            + UtilGUI.GRAPHICS_DEVICES.length + "[ not " + screenIndex);
        }
    }

    /**
     * Compute the maximum dimension of a component
     *
     * @param component Component to compute it's maximum size
     * @return Maximum size
     */
    public static Dimension computeMaximumDimension(final Component component)
    {
        if (component instanceof WithFixedSize)
        {
            return ((WithFixedSize) component).getFixedSize();
        }

        if (component instanceof Container)
        {
            final Container     container     = (Container) component;
            final LayoutManager layoutManager = container.getLayout();
            Dimension           dimension     = null;

            if (layoutManager != null)
            {
                dimension = layoutManager.preferredLayoutSize(container);
            }


            if (container.getComponentCount() < 1 || dimension == null)
            {
                dimension = component.getMaximumSize();

                if (dimension == null)
                {
                    return new Dimension(128, 128);
                }

                return new Dimension(Math.max(128, dimension.width), Math.max(128, dimension.height));
            }

            return dimension;

        }

        return component.getMaximumSize();
    }

    /**
     * Compute the minimum dimension of a component
     *
     * @param component Component to compute it's minimum size
     * @return Minimum size
     */
    public static Dimension computeMinimumDimension(final Component component)
    {
        if (component instanceof WithFixedSize)
        {
            return ((WithFixedSize) component).getFixedSize();
        }

        if (component instanceof Container)
        {
            final Container     container     = (Container) component;
            final LayoutManager layoutManager = container.getLayout();
            Dimension           dimension     = null;

            if (layoutManager != null)
            {
                dimension = layoutManager.preferredLayoutSize(container);
            }

            if (container.getComponentCount() < 1 || dimension == null)
            {
                dimension = component.getMinimumSize();

                if (dimension == null)
                {
                    return new Dimension(1, 1);
                }

                return new Dimension(Math.max(1, dimension.width), Math.max(1, dimension.height));
            }

            return dimension;
        }

        return component.getMinimumSize();
    }

    /**
     * Compute the preferred dimension of a component
     *
     * @param component Component to compute it's preferred size
     * @return Preferred size
     */
    public static Dimension computePreferredDimension(final Component component)
    {
        if (component instanceof WithFixedSize)
        {
            return ((WithFixedSize) component).getFixedSize();
        }

        if (component instanceof Container)
        {
            final Container     container     = (Container) component;
            final LayoutManager layoutManager = container.getLayout();
            Dimension           dimension     = null;

            if (layoutManager != null)
            {
                dimension = layoutManager.preferredLayoutSize(container);
            }

            if (container.getComponentCount() < 1 || dimension == null)
            {
                dimension = component.getPreferredSize();

                if (dimension == null)
                {
                    return new Dimension(16, 16);
                }

                return new Dimension(Math.max(16, dimension.width), Math.max(16, dimension.height));
            }

            return dimension;
        }

        return component.getPreferredSize();
    }

    /**
     * Create key stroke short cut for a character type without any special key like shift, control, ...
     *
     * @param character Character short cut
     * @return Created shortcut
     */
    public static KeyStroke createKeyStroke(final char character)
    {
        return UtilGUI.createKeyStroke(character, false, false, false, false, false);
    }

    /**
     * Create key stroke short cut for given key combination
     *
     * @param character Character
     * @param control   Indicates if control down
     * @param alt       Indicates if alt is down
     * @param shift     Indicates if shift is down
     * @param altGraph  Indicates if alt graph is down
     * @param meta      Indicates if meta is down
     * @return Creates key stroke short cut for given key combination
     */
    public static KeyStroke createKeyStroke(final char character, final boolean control, final boolean alt,
                                            final boolean shift, final boolean altGraph,
                                            final boolean meta)
    {
        int modifiers = 0;

        if (control)
        {
            modifiers |= InputEvent.CTRL_DOWN_MASK;
        }

        if (alt)
        {
            modifiers |= InputEvent.ALT_DOWN_MASK;
        }

        if (shift)
        {
            modifiers |= InputEvent.SHIFT_DOWN_MASK;
        }

        if (altGraph)
        {
            modifiers |= InputEvent.ALT_GRAPH_DOWN_MASK;
        }

        if (meta)
        {
            modifiers |= InputEvent.META_DOWN_MASK;
        }

        return KeyStroke.getKeyStroke(UtilGUI.charToKeyCodeForShortCut(character), modifiers);
    }

    /**
     * Compute the key code to use for short cut that use a given character.<br>
     * It is possible to use {@link #CHARACTER_DELETE} or {@link #CHARACTER_ESCAPE} character if you want build short cut
     * for respectively delete key, escape key
     *
     * @param character Character to compute the key code to use
     * @return Computed key code
     */
    public static int charToKeyCodeForShortCut(final char character)
    {
        switch (character)
        {
            case '0':
                return KeyEvent.VK_NUMPAD0;
            case '1':
                return KeyEvent.VK_NUMPAD1;
            case '2':
                return KeyEvent.VK_NUMPAD2;
            case '3':
                return KeyEvent.VK_NUMPAD3;
            case '4':
                return KeyEvent.VK_NUMPAD4;
            case '5':
                return KeyEvent.VK_NUMPAD5;
            case '6':
                return KeyEvent.VK_NUMPAD6;
            case '7':
                return KeyEvent.VK_NUMPAD7;
            case '8':
                return KeyEvent.VK_NUMPAD8;
            case '9':
                return KeyEvent.VK_NUMPAD9;
            case '+':
                return KeyEvent.VK_ADD;
            case '-':
                return KeyEvent.VK_SUBTRACT;
            case '*':
                return KeyEvent.VK_MULTIPLY;
            case '/':
                return KeyEvent.VK_DIVIDE;
            case '.':
                return KeyEvent.VK_PERIOD;
            case CHARACTER_ESCAPE:
                return KeyEvent.VK_ESCAPE;
            case CHARACTER_DELETE:
                return KeyEvent.VK_BACK_SPACE;
            case '\n':
                return KeyEvent.VK_ENTER;
            default:
                return KeyEvent.getExtendedKeyCodeForChar(character);
        }
    }

    /**
     * Create key stroke short cut for given key combination
     *
     * @param character Character
     * @param control   Indicates if control down
     * @return Creates key stroke short cut for given key combination
     */
    public static KeyStroke createKeyStroke(final char character, final boolean control)
    {
        return UtilGUI.createKeyStroke(character, control, false, false, false, false);
    }

    /**
     * Create key stroke short cut for given key combination
     *
     * @param character Character
     * @param control   Indicates if control down
     * @param alt       Indicates if alt is down
     * @return Creates key stroke short cut for given key combination
     */
    public static KeyStroke createKeyStroke(final char character, final boolean control, final boolean alt)
    {
        return UtilGUI.createKeyStroke(character, control, alt, false, false, false);
    }

    /**
     * Create key stroke short cut for given key combination
     *
     * @param character Character
     * @param control   Indicates if control down
     * @param alt       Indicates if alt is down
     * @param shift     Indicates if shift is down
     * @return Creates key stroke short cut for given key combination
     */
    public static KeyStroke createKeyStroke(final char character, final boolean control, final boolean alt,
                                            final boolean shift)
    {
        return UtilGUI.createKeyStroke(character, control, alt, shift, false, false);
    }

    /**
     * Create key stroke short cut for given key combination
     *
     * @param character Character
     * @param control   Indicates if control down
     * @param alt       Indicates if alt is down
     * @param shift     Indicates if shift is down
     * @param altGraph  Indicates if alt graph is down
     * @return Creates key stroke short cut for given key combination
     */
    public static KeyStroke createKeyStroke(final char character, final boolean control, final boolean alt,
                                            final boolean shift, final boolean altGraph)
    {
        return UtilGUI.createKeyStroke(character, control, alt, shift, altGraph, false);
    }

    /**
     * Obtain frame parent of a container
     *
     * @param container Container to get its parent
     * @return Parent frame
     */
    public static JFrame getFrameParent(Container container)
    {
        while (container != null)
        {
            if ((container instanceof JFrame))
            {
                return (JFrame) container;
            }

            container = container.getParent();
        }

        return null;
    }

    /**
     * Give the relative position of a component for an other one
     *
     * @param component Component to search its position
     * @param parent    A component ancestor
     * @return Relative position of {@code null} if parent is not an ancestor of component
     */
    public static Point getLocationOn(Component component, final Component parent)
    {
        final Point point = new Point();

        while ((component != null) && (!component.equals(parent)))
        {
            point.translate(component.getX(), component.getY());

            component = component.getParent();
        }

        if (component == null)
        {
            return null;
        }

        return point;
    }

    /**
     * Give bounds of a screen
     *
     * @param screen Screen index
     * @return Screen bounds
     */
    public static Rectangle getScreenBounds(final int screen)
    {
        final Rectangle bounds = UtilGUI.GRAPHICS_DEVICES[screen].getDefaultConfiguration()
                                                                 .getBounds();

        final Insets insets = UtilGUI.TOOLKIT.getScreenInsets(UtilGUI.GRAPHICS_DEVICES[screen].getDefaultConfiguration());

        if (bounds.x < insets.left)
        {
            insets.left -= bounds.x;
        }

        bounds.x += insets.left;
        bounds.y += insets.top;
        bounds.width -= insets.left + insets.right;
        bounds.height -= insets.top + insets.bottom;

        return bounds;
    }

    /**
     * Screen identifier
     *
     * @param screenIndex Screen index
     * @return Screen identifier
     */
    public static String getScreenIdentifier(final int screenIndex)
    {
        UtilGUI.checkScreenIndex(screenIndex);

        final StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append(System.getProperty("java.vendor"));
        stringBuffer.append(" | ");
        stringBuffer.append(UtilGUI.GRAPHICS_DEVICES[screenIndex].getIDstring());
        stringBuffer.append(" | ");
        stringBuffer.append(screenIndex);

        return stringBuffer.toString();
    }

    /**
     * Initialize GUI with operating system skin, call it before create any frame
     */
    public static void initializeGUI()
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (final Exception ignored)
        {
        }
    }

    /**
     * Place the mouse over the middle of a component
     *
     * @param component Component mouse go over
     */
    public static void locateMouseOver(final Component component)
    {
        if (component == null)
        {
            throw new NullPointerException("component MUST NOT be null");
        }

        if ((!component.isValid()) || (!component.isVisible()))
        {
            return;
        }

        Dimension dimension = component.getSize();
        UtilGUI.locateMouseOver(component, dimension.width / 2, dimension.height / 2);
        dimension = null;
    }

    /**
     * Place the mouse over a component
     *
     * @param component Component mouse go over
     * @param x         X relative to component up-left corner
     * @param y         Y relative to component up-left corner
     */
    public static void locateMouseOver(final Component component, final int x, final int y)
    {
        if (component == null)
        {
            throw new NullPointerException("component MUST NOT be null");
        }

        if ((!component.isValid()) || (!component.isVisible()))
        {
            return;
        }

        Point position = component.getLocationOnScreen();
        UtilGUI.locateMouseAt(position.x + x, position.y + y);
        position = null;
    }

    /**
     * Place the mouse on a location on the screen
     *
     * @param x X screen position
     * @param y Y screen position
     */
    public static void locateMouseAt(final int x, final int y)
    {
        if (UtilGUI.ROBOT == null)
        {
            return;
        }

        UtilGUI.ROBOT.mouseMove(x, y);
    }

    /**
     * Number of screen
     *
     * @return Number of screen
     */
    public static int numberOfScreen()
    {
        return UtilGUI.GRAPHICS_DEVICES.length;
    }

    /**
     * Invisible cursor
     *
     * @return Invisible cursor
     */
    public static Cursor obtainInvisbleCursor()
    {
        if (UtilGUI.INVISIBLE_CURSOR != null)
        {
            return UtilGUI.INVISIBLE_CURSOR;
        }

        Dimension dimension = Toolkit.getDefaultToolkit()
                                     .getBestCursorSize(32, 32);

        if ((dimension == null) || (dimension.width < 1) || (dimension.height < 1))
        {
            UtilGUI.INVISIBLE_CURSOR = Cursor.getDefaultCursor();
        }
        else
        {
            BufferedImage bufferedImage = new BufferedImage(dimension.width, dimension.height,
                                                            BufferedImage.TYPE_INT_ARGB);
            bufferedImage.flush();
            UtilGUI.INVISIBLE_CURSOR = Toolkit.getDefaultToolkit()
                                              .createCustomCursor(bufferedImage,
                                                                  new Point(dimension.width >> 1, dimension.height >> 1),
                                                                  "Invisible");
            bufferedImage = null;
        }

        dimension = null;

        return UtilGUI.INVISIBLE_CURSOR;
    }

    /**
     * Number of screen
     *
     * @return Number of screen
     */
    public static int obtainNumberOffScreen()
    {
        return UtilGUI.GRAPHICS_DEVICES.length;
    }

    /**
     * Obtain index of the screen where is the window
     *
     * @param window Considered window
     * @return Screen index
     */
    public static int obtainScreenIndex(final Window window)
    {
        final Rectangle windowBounds = window.getBounds();

        GraphicsConfiguration graphicsConfiguration = UtilGUI.GRAPHICS_DEVICES[0].getDefaultConfiguration();
        Rectangle             bounds                = graphicsConfiguration.getBounds();
        int                   areaMax               = UtilMath.computeIntresectedArea(windowBounds, bounds);
        int                   screenIndex           = 0;
        int                   area;
        for (int i = 1; i < UtilGUI.GRAPHICS_DEVICES.length; i++)
        {
            graphicsConfiguration = UtilGUI.GRAPHICS_DEVICES[i].getDefaultConfiguration();
            bounds = graphicsConfiguration.getBounds();
            area = UtilMath.computeIntresectedArea(windowBounds, bounds);

            if (area > areaMax)
            {
                screenIndex = i;
                areaMax = area;
            }
        }

        return screenIndex;
    }

    /**
     * Put a window in it's pack size<br>
     * Size is automatic limited to the window's screen
     *
     * @param window Window to pack
     */
    public static void packedSize(final Window window)
    {
        window.pack();

        final Dimension dimension = window.getSize();
        final Rectangle screen    = UtilGUI.computeScreenRectangle(window);

        if (dimension.width > screen.width)
        {
            dimension.width = screen.width;
        }

        if (dimension.height > screen.height)
        {
            dimension.height = screen.height;
        }

        window.setSize(dimension);
    }

    /**
     * Create a screen shot on JHelpImage
     *
     * @return JHelpImage with screen shot
     */
    public static JHelpImage screenShotJHelpImage()
    {
        return JHelpImage.createImage(UtilGUI.screenShot());
    }

    /**
     * Make a screen shot
     *
     * @return Screen shot
     */
    public static BufferedImage screenShot()
    {
        int       xMin = 0;
        int       xMax = 0;
        int       yMin = 0;
        int       yMax = 0;
        Rectangle bounds;

        for (final GraphicsDevice graphicsDevice : UtilGUI.GRAPHICS_DEVICES)
        {
            bounds = graphicsDevice.getDefaultConfiguration()
                                   .getBounds();

            xMin = Math.min(xMin, -bounds.x);
            xMax = Math.max(xMax, -bounds.x + bounds.width);
            yMin = Math.min(yMin, -bounds.y);
            yMax = Math.max(yMax, -bounds.y + bounds.height);
        }

        final Dimension size = Toolkit.getDefaultToolkit()
                                      .getScreenSize();

        final int width  = Math.max(xMax - xMin, size.width);
        final int height = Math.max(yMax - yMin, size.height);

        if (UtilGUI.ROBOT == null)
        {
            if (UtilGUI.EMPTY_IMAGE == null)
            {
                UtilGUI.EMPTY_IMAGE = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            }

            int[] pixels = new int[width * height];
            UtilGUI.EMPTY_IMAGE.setRGB(0, 0, width, height, pixels, 0, width);
            pixels = null;

            return UtilGUI.EMPTY_IMAGE;
        }

        return UtilGUI.ROBOT.createScreenCapture(new Rectangle(xMin, yMin, width, height));
    }

    /**
     * Search JFrame parent of a component
     *
     * @param component Component search it's JFram parent
     * @return JFrame parent or {@code null} if component haven't a JFrame parent
     */
    public static JFrame searchFrameParent(Component component)
    {
        while (component != null)
        {
            if ((component instanceof JFrame))
            {
                return (JFrame) component;
            }

            if ((component instanceof Window))
            {
                component = ((Window) component).getOwner();
            }
            else
            {
                component = component.getParent();
            }
        }

        return null;
    }

    /**
     * Simulate a key press
     *
     * @param keyCode Key code
     */
    public static void simulateKeyPress(final int keyCode)
    {
        if (UtilGUI.ROBOT == null)
        {
            return;
        }

        UtilGUI.ROBOT.keyPress(keyCode);
    }

    /**
     * Simulate a key release
     *
     * @param keyCode Key code
     */
    public static void simulateKeyRelease(final int keyCode)
    {
        if (UtilGUI.ROBOT == null)
        {
            return;
        }

        UtilGUI.ROBOT.keyRelease(keyCode);
    }

    /**
     * Simulate (If system allow it) a mouse click
     *
     * @param time Duration of down state
     */
    public static void simulateMouseClick(final int time)
    {
        if (UtilGUI.ROBOT == null)
        {
            return;
        }

        UtilGUI.ROBOT.mousePress(InputEvent.BUTTON1_MASK);
        Utilities.sleep(time);
        UtilGUI.ROBOT.mouseRelease(InputEvent.BUTTON1_MASK | InputEvent.BUTTON2_MASK | InputEvent.BUTTON3_MASK);
    }

    /**
     * Simulate a mouse press
     *
     * @param button Mouse buttons
     */
    public static void simulateMousePress(final int button)
    {
        if (UtilGUI.ROBOT == null)
        {
            return;
        }

        UtilGUI.ROBOT.mousePress(button);
    }

    /**
     * Simulate a mouse release
     *
     * @param button Mouse buttons
     */
    public static void simulateMouseRelease(final int button)
    {
        if (UtilGUI.ROBOT == null)
        {
            return;
        }

        UtilGUI.ROBOT.mouseRelease(button);
    }

    /**
     * Simulate mouse wheel move
     *
     * @param tick Number of "notches" to move the mouse wheel Negative values indicate movement up/away from the user,
     *             positive values indicate movement down/towards the user.
     */
    public static void simulateMouseWhell(final int tick)
    {
        if (UtilGUI.ROBOT == null)
        {
            return;
        }

        UtilGUI.ROBOT.mouseWheel(tick);
    }

    /**
     * Simulate a release then press mouse button
     *
     * @param time Time between release and press
     */
    public static void simulateReleasedPressed(final int time)
    {
        if (UtilGUI.ROBOT == null)
        {
            return;
        }

        UtilGUI.ROBOT.mouseRelease(InputEvent.BUTTON1_MASK | InputEvent.BUTTON2_MASK | InputEvent.BUTTON3_MASK);
        Utilities.sleep(time);
        UtilGUI.ROBOT.mousePress(InputEvent.BUTTON1_MASK);
    }

    /**
     * Make widow take all it's screen
     *
     * @param window Window to maximize
     */
    public static void takeAllScreen(final Window window)
    {
        final Rectangle screen = UtilGUI.computeScreenRectangle(window);
        window.setBounds(screen);
    }
}