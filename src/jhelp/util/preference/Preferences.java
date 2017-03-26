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
package jhelp.util.preference;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;

import jhelp.util.Utilities;
import jhelp.util.io.UtilIO;
import jhelp.util.io.base64.Base64Common;
import jhelp.util.list.Pair;

/**
 * Store preferences in RAM and file.<br>
 * A preference have a name, type and value.<br>
 * Actual preferences type are describe in {@link PreferenceType}<br>
 * The preferences file may not exists on first use, it will be created automatically when save is need
 *
 * @author JHelp
 */
public class Preferences
{
    /**
     * Preferences map
     */
    private final HashMap<String, Pair<PreferenceType, Object>> preferences;
    /**
     * File where store preferences
     */
    private final File                                          preferencesFile;
    /**
     * Preferences serializer to save them
     */
    private final PreferencesSerializer                         preferencesSerializer;
    /**
     * Create a new instance of Preferences
     *
     * @param preferencesFile File where read/write preferences may not actually exists
     */
    public Preferences(final File preferencesFile)
    {
        if (preferencesFile == null)
        {
            throw new NullPointerException("preferencesFile MUST NOT be null");
        }

        this.preferencesFile = preferencesFile;
        this.preferences = new HashMap<String, Pair<PreferenceType, Object>>();

        this.preferencesSerializer = new PreferencesSerializer(preferencesFile, this.preferences);

        this.loadPreferences();
    }

    /**
     * Load preferences
     */
    @SuppressWarnings("unused")
    private void loadPreferences()
    {
        if (this.preferencesFile.exists())
        {
            new PreferencesParser(this.preferencesFile, this.preferences);
        }
    }

    /**
     * Parse a serialized value to the real value depends on {@link PreferenceType}
     *
     * @param serializedValue Serialized value
     * @param preferenceType  Preference type
     * @return Parsed value
     */
    public static Object parse(final String serializedValue, final PreferenceType preferenceType)
    {
        switch (preferenceType)
        {
            case ARRAY:
            {
                return Base64Common.fromBase64(serializedValue);
            }
            case BOOLEAN:
                if ("true".equalsIgnoreCase(serializedValue))
                {
                    return true;
                }

                if ("false".equalsIgnoreCase(serializedValue))
                {
                    return false;
                }

                throw new IllegalArgumentException(serializedValue + " not a " + PreferenceType.BOOLEAN + " value !");
            case FILE:
                return UtilIO.obtainExternalFile(serializedValue);
            case INTEGER:
                return Integer.parseInt(serializedValue);
            case STRING:
                return serializedValue;
            case LOCALE:
                return Utilities.convertStringToLocale(serializedValue);
        }

        return null;
    }

    /**
     * Serialize a value depends on {@link PreferenceType}
     *
     * @param value          Value to serialize
     * @param preferenceType Preference type
     * @return Serialized value
     */
    public static String serialize(final Object value, final PreferenceType preferenceType)
    {
        switch (preferenceType)
        {
            case ARRAY:
            {
                return Base64Common.toBase64((byte[]) value);
            }
            case BOOLEAN:
                return ((Boolean) value).booleanValue()
                       ? "TRUE"
                       : "FALSE";
            case FILE:
                return UtilIO.computeRelativePath(UtilIO.obtainOutsideDirectory(), (File) value);
            case INTEGER:
                return String.valueOf(((Integer) value).intValue());
            case STRING:
                return (String) value;
            case LOCALE:
                return value.toString();
        }

        return null;
    }

    /**
     * Get a byte[] from preferences
     *
     * @param name Preference name
     * @return Array in preference or {@code null} if not exists
     */
    public byte[] getArrayValue(final String name)
    {
        if (name == null)
        {
            throw new NullPointerException("name MUST NOT be null");
        }

        final Pair<PreferenceType, Object> pair = this.preferences.get(name);

        if (pair == null)
        {
            return null;
        }

        if (pair.element1 != PreferenceType.ARRAY)
        {
            throw new IllegalArgumentException(
                    "The value of" + name + " isn't a " + PreferenceType.ARRAY + " but a " + pair.element1);
        }

        return (byte[]) pair.element2;
    }

    /**
     * Get file from preferences
     *
     * @param name preference name
     * @return File in preferences or {@code null} if not exists
     */
    public File getFileValue(final String name)
    {
        if (name == null)
        {
            throw new NullPointerException("name MUST NOT be null");
        }

        final Pair<PreferenceType, Object> pair = this.preferences.get(name);

        if (pair == null)
        {
            return null;
        }

        if (pair.element1 != PreferenceType.FILE)
        {
            throw new IllegalArgumentException(
                    "The value of" + name + " isn't a " + PreferenceType.FILE + " but a " + pair.element1);
        }

        return (File) pair.element2;
    }

    /**
     * Obtain the type of a preference
     *
     * @param name Preference name
     * @return Preference type or {@code null} if preference dosen't exists
     */
    public PreferenceType getPreferenceType(final String name)
    {
        final Pair<PreferenceType, Object> preference = this.preferences.get(name);

        if (preference == null)
        {
            return null;
        }

        return preference.element1;
    }

    /**
     * Get a boolean value from preferences
     *
     * @param name         Preference name
     * @param defaultValue Value to store and return if preference not already exists
     * @return Preference value or default value
     */
    public boolean getValue(final String name, final boolean defaultValue)
    {
        if (name == null)
        {
            throw new NullPointerException("name MUST NOT be null");
        }

        final Pair<PreferenceType, Object> pair = this.preferences.get(name);

        if (pair == null)
        {
            this.setValue(name, defaultValue);

            return defaultValue;
        }

        if (pair.element1 != PreferenceType.BOOLEAN)
        {
            throw new IllegalArgumentException(
                    "The value of" + name + " isn't a " + PreferenceType.BOOLEAN + " but a " + pair.element1);
        }

        return (Boolean) pair.element2;
    }

    /**
     * Define/change a boolean value
     *
     * @param name  Preference name
     * @param value New value
     */
    public void setValue(final String name, final boolean value)
    {
        if (name == null)
        {
            throw new NullPointerException("name MUST NOT be null");
        }

        Pair<PreferenceType, Object> pair = this.preferences.get(name);

        if (pair == null)
        {
            pair = new Pair<PreferenceType, Object>(PreferenceType.BOOLEAN, value);

            this.preferences.put(name, pair);

            this.savePreferences();

            return;
        }

        if (pair.element1 != PreferenceType.BOOLEAN)
        {
            throw new IllegalArgumentException(
                    "The preference " + name + " is not a " + PreferenceType.BOOLEAN + " but a " + pair.element1);
        }

        pair.element2 = value;

        this.savePreferences();
    }

    /**
     * Save preferences
     */
    private void savePreferences()
    {
        this.preferencesSerializer.serialize();
    }

    /**
     * Get a int value from preferences
     *
     * @param name         Preference name
     * @param defaultValue Value to store and return if preference not already exists
     * @return Preference value or default value
     */
    public int getValue(final String name, final int defaultValue)
    {
        if (name == null)
        {
            throw new NullPointerException("name MUST NOT be null");
        }

        final Pair<PreferenceType, Object> pair = this.preferences.get(name);

        if (pair == null)
        {
            this.setValue(name, defaultValue);

            return defaultValue;
        }

        if (pair.element1 != PreferenceType.INTEGER)
        {
            throw new IllegalArgumentException(
                    "The value of" + name + " isn't a " + PreferenceType.INTEGER + " but a " + pair.element1);
        }

        return (Integer) pair.element2;
    }

    /**
     * Define/change a int value
     *
     * @param name  Preference name
     * @param value New value
     */
    public void setValue(final String name, final int value)
    {
        if (name == null)
        {
            throw new NullPointerException("name MUST NOT be null");
        }

        Pair<PreferenceType, Object> pair = this.preferences.get(name);

        if (pair == null)
        {
            pair = new Pair<PreferenceType, Object>(PreferenceType.INTEGER, value);

            this.preferences.put(name, pair);

            this.savePreferences();

            return;
        }

        if (pair.element1 != PreferenceType.INTEGER)
        {
            throw new IllegalArgumentException(
                    "The preference " + name + " is not a " + PreferenceType.INTEGER + " but a " + pair.element1);
        }

        pair.element2 = value;

        this.savePreferences();
    }

    /**
     * Get a locale value from preferences
     *
     * @param name         Preference name
     * @param defaultValue Value to store and return if preference not already exists
     * @return Preference value or default value
     */
    public Locale getValue(final String name, final Locale defaultValue)
    {
        if (name == null)
        {
            throw new NullPointerException("name MUST NOT be null");
        }

        final Pair<PreferenceType, Object> pair = this.preferences.get(name);

        if (pair == null)
        {
            this.setValue(name, defaultValue);

            return defaultValue;
        }

        if (pair.element1 != PreferenceType.LOCALE)
        {
            throw new IllegalArgumentException(
                    "The value of" + name + " isn't a " + PreferenceType.LOCALE + " but a " + pair.element1);
        }

        return (Locale) pair.element2;
    }

    /**
     * Define/change a locale value
     *
     * @param name  Preference name
     * @param value New value
     */
    public void setValue(final String name, final Locale value)
    {
        if (name == null)
        {
            throw new NullPointerException("name MUST NOT be null");
        }

        if (value == null)
        {
            throw new NullPointerException("value MUST NOT be null");
        }

        Pair<PreferenceType, Object> pair = this.preferences.get(name);

        if (pair == null)
        {
            pair = new Pair<PreferenceType, Object>(PreferenceType.LOCALE, value);

            this.preferences.put(name, pair);

            this.savePreferences();

            return;
        }

        if (pair.element1 != PreferenceType.LOCALE)
        {
            throw new IllegalArgumentException(
                    "The preference " + name + " is not a " + PreferenceType.LOCALE + " but a " + pair.element1);
        }

        pair.element2 = value;

        this.savePreferences();
    }

    /**
     * Get a String value from preferences
     *
     * @param name         Preference name
     * @param defaultValue Value to store and return if preference not already exists
     * @return Preference value or default value
     */
    public String getValue(final String name, final String defaultValue)
    {
        if (name == null)
        {
            throw new NullPointerException("name MUST NOT be null");
        }

        final Pair<PreferenceType, Object> pair = this.preferences.get(name);

        if (pair == null)
        {
            this.setValue(name, defaultValue);

            return defaultValue;
        }

        if (pair.element1 != PreferenceType.STRING)
        {
            throw new IllegalArgumentException(
                    "The value of" + name + " isn't a " + PreferenceType.STRING + " but a " + pair.element1);
        }

        return (String) pair.element2;
    }

    /**
     * Define/change a String value
     *
     * @param name  Preference name
     * @param value New value
     */
    public void setValue(final String name, final String value)
    {
        if (name == null)
        {
            throw new NullPointerException("name MUST NOT be null");
        }

        if (value == null)
        {
            throw new NullPointerException("value MUST NOT be null");
        }

        Pair<PreferenceType, Object> pair = this.preferences.get(name);

        if (pair == null)
        {
            pair = new Pair<PreferenceType, Object>(PreferenceType.STRING, value);

            this.preferences.put(name, pair);

            this.savePreferences();

            return;
        }

        if (pair.element1 != PreferenceType.STRING)
        {
            throw new IllegalArgumentException(
                    "The preference " + name + " is not a " + PreferenceType.STRING + " but a " + pair.element1);
        }

        pair.element2 = value;

        this.savePreferences();
    }

    /**
     * Remove a preference
     *
     * @param name Preference name to remove
     */
    public void removePreference(final String name)
    {
        if (this.preferences.remove(name) != null)
        {
            this.savePreferences();
        }
    }

    /**
     * Define/change a byte[] value
     *
     * @param name  Preference name
     * @param value New value
     */
    public void setValue(final String name, final byte[] value)
    {
        if (name == null)
        {
            throw new NullPointerException("name MUST NOT be null");
        }

        if (value == null)
        {
            throw new NullPointerException("value MUST NOT be null");
        }

        Pair<PreferenceType, Object> pair = this.preferences.get(name);

        if (pair == null)
        {
            pair = new Pair<PreferenceType, Object>(PreferenceType.ARRAY, value);

            this.preferences.put(name, pair);

            this.savePreferences();

            return;
        }

        if (pair.element1 != PreferenceType.ARRAY)
        {
            throw new IllegalArgumentException(
                    "The preference " + name + " is not a " + PreferenceType.ARRAY + " but a " + pair.element1);
        }

        pair.element2 = value;

        this.savePreferences();
    }

    /**
     * Define/change a File value
     *
     * @param name  Preference name
     * @param value New value
     */
    public void setValue(final String name, final File value)
    {
        if (name == null)
        {
            throw new NullPointerException("name MUST NOT be null");
        }

        if (value == null)
        {
            throw new NullPointerException("value MUST NOT be null");
        }

        Pair<PreferenceType, Object> pair = this.preferences.get(name);

        if (pair == null)
        {
            pair = new Pair<PreferenceType, Object>(PreferenceType.FILE, value);

            this.preferences.put(name, pair);

            this.savePreferences();

            return;
        }

        if (pair.element1 != PreferenceType.FILE)
        {
            throw new IllegalArgumentException(
                    "The preference " + name + " is not a " + PreferenceType.FILE + " but a " + pair.element1);
        }

        pair.element2 = value;

        this.savePreferences();
    }
}