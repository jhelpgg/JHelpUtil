package jhelp.util.thread;

import jhelp.util.debug.Debug;
import jhelp.util.debug.DebugLevel;
import jhelp.util.text.UtilText;

/**
 * Mutex (Mutual Exclusion) for synchronization
 * 
 * @author JHelp
 */
public class Mutex
{
   /** For enable/disable mutex debugging */
   private static final boolean DEBUG = false;
   /** Indicates if mutex already locked */
   private boolean              isLocked;
   /** Last mutex information (For printing) */
   private String               lastMutexInformation;
   /** Lock link to the mutex */
   private final Object         lock  = new Object();
   /** Thread ID that have locked the mutex */
   private String               lockerThreadID;

   /**
    * Create a new instance of Mutex
    */
   public Mutex()
   {
      this.isLocked = false;
   }

   /**
    * Compute the mutex information with the caller stack trace
    * 
    * @param traceCaller
    *           Caller stack trace
    * @return Information created
    */
   private String createMutexInformation(final StackTraceElement traceCaller)
   {
      return UtilText.concatenate(traceCaller.getClassName(), '.', traceCaller.getMethodName(), " at ", traceCaller.getLineNumber(), " : ", this.createThreadID());
   }

   /**
    * Create current thread ID
    * 
    * @return Current thread ID
    */
   private String createThreadID()
   {
      final Thread thread = Thread.currentThread();

      return UtilText.concatenate(thread.getName(), ':', thread.getId());
   }

   /**
    * Lock the mutex
    */
   @SuppressWarnings("unused")
   public void lock()
   {
      String info;

      if(Mutex.DEBUG == true)
      {
         info = this.createMutexInformation((new Throwable()).getStackTrace()[1]);
      }

      synchronized(this.lock)
      {
         if(this.isLocked == true)
         {
            if(Mutex.DEBUG == true)
            {
               Debug.println(DebugLevel.DEBUG, info, "have to wait, ", this.lastMutexInformation, " have the lock");
            }

            try
            {
               this.lock.wait();
            }
            catch(final Exception exception)
            {
            }
         }

         this.isLocked = true;

         if(Mutex.DEBUG == true)
         {
            this.lockerThreadID = this.createThreadID();
            this.lastMutexInformation = info;
            Debug.println(DebugLevel.DEBUG, info, " take the lock");
         }
      }
   }

   /**
    * Unlock the mutex
    */
   @SuppressWarnings("unused")
   public void unlock()
   {
      String info;

      if(Mutex.DEBUG == true)
      {
         info = this.createMutexInformation((new Throwable()).getStackTrace()[1]);
      }

      synchronized(this.lock)
      {
         if(this.isLocked == true)
         {
            if(Mutex.DEBUG == true)
            {
               Debug.println(DebugLevel.DEBUG, info, " release lock and the owner was ", this.lastMutexInformation);

               if(this.createThreadID().equals(this.lockerThreadID) == false)
               {
                  Debug.println(DebugLevel.WARNING, "Not the same thread that take the lock and release it !");
               }
            }

            this.lock.notify();
         }

         this.isLocked = false;
      }

      Thread.yield();
   }
}