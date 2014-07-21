package jhelp.util.io.net;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import jhelp.util.debug.Debug;
import jhelp.util.io.UtilIO;
import jhelp.util.text.UtilText;

/**
 * Establish a HTTP communication to get information from server..<br>
 * Its possible to upload a file, but in that case the sever <b>MUST</b> support multi-part requests
 * 
 * @author JHelp
 */
public class HttpCommunication
{
   /**
    * Thread for report connection errors
    * 
    * @author JHelp
    */
   class ThreadErrors
         extends Thread
   {
      /**
       * Create a new instance of ThreadErrors
       */
      ThreadErrors()
      {
      }

      /**
       * Report errors <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       * 
       * @see java.lang.Thread#run()
       */
      @Override
      public void run()
      {
         HttpCommunication.this.doErrors();
      }
   }

   /**
    * Thread for receive server response
    * 
    * @author JHelp
    */
   class ThreadReceiver
         extends Thread
   {
      /**
       * Create a new instance of ThreadReceiver
       */
      ThreadReceiver()
      {
      }

      /**
       * Report server response <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       * 
       * @see java.lang.Thread#run()
       */
      @Override
      public void run()
      {
         HttpCommunication.this.doAnswers();
      }
   }

   /** Connection with the server */
   private HttpURLConnection             connection;
   /** Stream to have connection errors */
   private InputStream                   errorStream;
   /** Request parameters */
   private final HashMap<String, String> fields;
   /** File to upload */
   private File                          file;
   /** File key */
   private String                        fileKey;
   /** Request headers */
   private final HashMap<String, String> headers;
   /** Stream to read server response */
   private InputStream                   inputStream;
   /** Connection method */
   private final Method                  method;
   /** File mime type */
   private String                        mimeType;
   /** Listener of server response */
   private final ResponseReceiver        responseReceiver;
   /** Thread errors reporter */
   private ThreadErrors                  threadErrors;
   /** Thread response receiver */
   private ThreadReceiver                threadReceiver;
   /** Connection url */
   private final String                  urlPath;

   /**
    * Create a new instance of HttpCommunication
    * 
    * @param urlPath
    *           Connection URL
    * @param method
    *           Request method
    * @param responseReceiver
    *           Listener of server response
    */
   public HttpCommunication(final String urlPath, final Method method, final ResponseReceiver responseReceiver)
   {
      if(urlPath == null)
      {
         throw new NullPointerException("urlPath musn't be null");
      }

      if(method == null)
      {
         throw new NullPointerException("method musn't be null");
      }

      if(responseReceiver == null)
      {
         throw new NullPointerException("responseReceiver musn't be null");
      }

      this.urlPath = urlPath;
      this.method = method;
      this.responseReceiver = responseReceiver;
      this.headers = new HashMap<String, String>();
      this.fields = new HashMap<String, String>();
   }

   /**
    * Manage server response
    */
   void doAnswers()
   {
      try
      {
         this.responseReceiver.startOfResponse();

         final byte[] buffer = new byte[4096];
         int read = this.inputStream.read(buffer);

         while((read >= 0) && (this.threadReceiver != null))
         {
            this.responseReceiver.serverMessage(buffer, 0, read);
            read = this.inputStream.read(buffer);
         }

         this.responseReceiver.endOfResponse();
      }
      catch(final Exception exception)
      {
         Debug.printException(exception, "Failed to get message from ", this.urlPath);
      }
      finally
      {
         try
         {
            this.inputStream.close();
         }
         catch(final Exception exception)
         {
         }
      }
   }

   /**
    * Manage connection errors
    */
   void doErrors()
   {
      if(this.errorStream == null)
      {
         return;
      }

      BufferedReader bufferedReader = null;

      try
      {
         bufferedReader = new BufferedReader(new InputStreamReader(this.errorStream, "UTF-8"));
         String line = bufferedReader.readLine();

         while((line != null) && (this.threadErrors != null))
         {
            this.responseReceiver.serverError(line);
            line = bufferedReader.readLine();
         }
      }
      catch(final Exception exception)
      {
         Debug.printException(exception, "Failed to report error for ", this.urlPath);
      }
      finally
      {
         try
         {
            this.errorStream.close();
         }
         catch(final Exception exception)
         {
         }
      }
   }

   /**
    * Close the connection.<br>
    * Do nothing if not connected
    */
   public void close()
   {
      if(this.connection == null)
      {
         return;
      }

      this.threadErrors = null;
      this.threadReceiver = null;

      try
      {
         this.inputStream.close();
      }
      catch(final Exception exception)
      {
      }

      try
      {
         this.errorStream.close();
      }
      catch(final Exception exception)
      {
      }

      this.connection.disconnect();
      this.connection = null;
   }

   /**
    * Connect the server.<br>
    * Do nothing if already connected
    */
   public void connect()
   {
      if(this.connection != null)
      {
         return;
      }

      URL url = null;

      try
      {
         final StringBuilder path = new StringBuilder(this.urlPath);

         if((this.fields.isEmpty() == false) && (this.file == null))
         {
            path.append('?');

            for(final String key : this.fields.keySet())
            {
               path.append(key);
               path.append('=');
               path.append(URLEncoder.encode(this.fields.get(key), "UTF-8"));
               path.append('&');
            }

            path.deleteCharAt(path.length() - 1);
         }

         final String boundaryMark = "--";
         final String separator = "\r\n";
         final StringBuilder requestBody = new StringBuilder();

         if(this.file != null)
         {
            for(final String key : this.fields.keySet())
            {
               requestBody.append(separator);
               requestBody.append(boundaryMark);
               requestBody.append("CAFEFACE");
               requestBody.append(separator);
               requestBody.append("Content-Disposition: form-data; name=\"");
               requestBody.append(key);
               requestBody.append("\"");
               requestBody.append(separator);
               requestBody.append(this.fields.get(key));
            }

            requestBody.append(separator);
            requestBody.append(boundaryMark);
            requestBody.append("CAFEFACE");
            requestBody.append(separator);
            requestBody.append("Content-Disposition: form-data; name=\"");
            requestBody.append(this.fileKey);
            requestBody.append("\"; filename=\"");
            requestBody.append(this.file.getName());
            requestBody.append("\"");
            requestBody.append(separator);
            requestBody.append("Content-Type: ");
            requestBody.append(this.mimeType);
            requestBody.append(separator);
         }

         final byte[] bodyHead = requestBody.toString().getBytes();
         final byte[] bodyEnd = UtilText.concatenate(separator, boundaryMark, "CAFEFACE", boundaryMark, separator).getBytes();

         int length = bodyHead.length + bodyEnd.length;

         if(this.file != null)
         {
            length += this.file.length();
         }

         url = new URL(path.toString());
         this.connection = (HttpURLConnection) url.openConnection();
         this.connection.setRequestMethod(this.method.getMethod());

         for(final String key : this.headers.keySet())
         {
            this.connection.setRequestProperty(key, this.headers.get(key));
         }

         if(this.file != null)
         {
            this.connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=CAFEFACE");
            this.connection.setRequestProperty("Content-Length", String.valueOf(length));
         }

         this.connection.setDoInput(true);

         if(this.file != null)
         {
            this.connection.setDoOutput(true);
         }

         this.connection.connect();

         if(this.file != null)
         {
            final OutputStream outputStream = this.connection.getOutputStream();
            UtilIO.writeByteArray(bodyHead, outputStream);

            if(this.file != null)
            {
               UtilIO.write(this.file, outputStream);
            }

            UtilIO.writeByteArray(bodyEnd, outputStream);
         }

         this.errorStream = this.connection.getErrorStream();
         this.threadErrors = new ThreadErrors();
         this.threadErrors.start();

         this.inputStream = this.connection.getInputStream();
         this.threadReceiver = new ThreadReceiver();
         this.threadReceiver.start();
      }
      catch(final Exception exception)
      {
         this.responseReceiver.connectionFailed(exception, url);
      }
   }

   /**
    * Indicates if connected to the server
    * 
    * @return {@code true} if connected to the server
    */
   public boolean isConnected()
   {
      return this.connection != null;
   }

   /**
    * Defines one file to upload.<br>
    * Using it implies that distant server managed multi-part requests.<br>
    * Have effect only to next connection
    * 
    * @param key
    *           File key
    * @param file
    *           File to upload
    * @param mimeType
    *           File mime-type
    */
   public void putField(final String key, final File file, final String mimeType)
   {
      if(key == null)
      {
         throw new NullPointerException("key musn't be null");
      }

      if(file == null)
      {
         throw new NullPointerException("file musn't be null");
      }

      if(mimeType == null)
      {
         throw new NullPointerException("mimeType musn't be null");
      }

      if(file.exists() == false)
      {
         throw new IllegalArgumentException(file.getAbsolutePath() + " dosen't exits");
      }

      this.file = file;
      this.fileKey = key;
      this.mimeType = mimeType;
   }

   /**
    * Set a request field.<br>
    * Have effect only to next connection
    * 
    * @param key
    *           Request key
    * @param value
    *           Request value
    */
   public void putField(final String key, final String value)
   {
      if(key == null)
      {
         throw new NullPointerException("key musn't be null");
      }

      if(value == null)
      {
         throw new NullPointerException("value musn't be null");
      }

      this.fields.put(key, value);
   }

   /**
    * Add request header.<br>
    * Have effect only to next connection
    * 
    * @param key
    *           Header key
    * @param value
    *           Header value
    */
   public void putHeader(final String key, final String value)
   {
      if(key == null)
      {
         throw new NullPointerException("key musn't be null");
      }

      if(value == null)
      {
         throw new NullPointerException("value musn't be null");
      }

      this.headers.put(key, value);
   }
}