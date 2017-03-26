package jhelp.util.preference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import jhelp.util.debug.Debug;
import jhelp.util.io.UtilIO;
import jhelp.util.list.Pair;
import jhelp.util.thread.Mutex;
import jhelp.util.thread.ThreadManager;
import jhelp.util.thread.ThreadedTask;
import jhelp.util.xml.DynamicWriteXML;

/**
 * Serialize preferences in XML
 * 
 * @author JHelp
 */
class PreferencesSerializer
      extends ThreadedTask<Void, Void, Void>
      implements PreferencesFileConstants
{
   /** Lock for synchronization */
   private final Mutex                                         mutex;
   /** Preferences to serialize */
   private final HashMap<String, Pair<PreferenceType, Object>> preferences;
   /** File where write XML */
   private final File                                          preferencesFile;
   /** Indicates if a new serialization is need */
   private boolean                                             serializeAgain;
   /** Indicates if we are in serializing */
   private boolean                                             serializing;

   /**
    * Create a new instance of PreferencesSerializer
    * 
    * @param preferencesFile
    *           Preference files where write preferences
    * @param preferences
    *           Preferences to serialize
    */
   PreferencesSerializer(final File preferencesFile, final HashMap<String, Pair<PreferenceType, Object>> preferences)
   {
      this.preferencesFile = preferencesFile;
      this.preferences = preferences;

      this.serializing = false;
      this.serializeAgain = false;
      this.mutex = new Mutex();
   }

   /**
    * Do the serialization in separate thread <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param parameter
    *           Unused
    * @return {@code null}
    * @see jhelp.util.thread.ThreadedTask#doThreadAction(java.lang.Object)
    */
   @Override
   protected Void doThreadAction(final Void parameter)
   {
      FileOutputStream fileOutputStream = null;
      try
      {
         if(!UtilIO.createFile(this.preferencesFile))
         {
            throw new IOException("Can't create file " + this.preferencesFile.getAbsolutePath());
         }

         fileOutputStream = new FileOutputStream(this.preferencesFile);

         Pair<PreferenceType, Object> preference;

         final DynamicWriteXML dynamicWriteXML = new DynamicWriteXML(fileOutputStream);

         dynamicWriteXML.openMarkup(PreferencesFileConstants.MARKUP_PREFERENCES);

         for(final String name : this.preferences.keySet())
         {
            preference = this.preferences.get(name);

            dynamicWriteXML.openMarkup(PreferencesFileConstants.MARKUP_PREFERENCE);

            dynamicWriteXML.appendParameter(PreferencesFileConstants.PARAMETER_NAME, name);
            dynamicWriteXML.appendParameter(PreferencesFileConstants.PARAMETER_TYPE, preference.element1.name());
            dynamicWriteXML.appendParameter(PreferencesFileConstants.PARAMETER_VALUE, Preferences.serialize(preference.element2, preference.element1));

            dynamicWriteXML.closeMarkup();
         }

         dynamicWriteXML.closeMarkup();
      }
      catch(final Exception exception)
      {
         Debug.printException(exception, "Issue while serializing preferences !");
      }
      finally
      {
         if(fileOutputStream != null)
         {
            try
            {
               fileOutputStream.flush();
            }
            catch(final Exception ignored)
            {
            }

            try
            {
               fileOutputStream.close();
            }
            catch(final Exception ignored)
            {
            }
         }
      }

      return null;
   }

   /**
    * Call when serialization is finish <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param result
    *           Unused
    * @see jhelp.util.thread.ThreadedTask#setResult(java.lang.Object)
    */
   @Override
   protected void setResult(final Void result)
   {
      this.mutex.lock();

      try
      {
         this.serializing = false;

         if(this.serializeAgain)
         {
            this.serializeAgain = false;
            this.serializing = true;

            ThreadManager.THREAD_MANAGER.doThread(this, null);
         }
      }
      finally
      {
         this.mutex.unlock();
      }
   }

   /**
    * Serialize the preferences as soon as possible
    */
   public void serialize()
   {
      this.mutex.lock();
      try
      {
         if(!this.serializing)
         {
            this.serializing = true;

            ThreadManager.THREAD_MANAGER.doThread(this, null);
         }
         else
         {
            this.serializeAgain = true;
         }
      }
      finally
      {
         this.mutex.unlock();
      }
   }
}