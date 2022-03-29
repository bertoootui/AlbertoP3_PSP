


	import java.io.DataInputStream;
	import java.io.PrintStream;
	import java.io.BufferedReader;
	import java.io.InputStreamReader;
	import java.io.IOException;
	import java.net.Socket;
	import java.net.UnknownHostException;

	public class Client implements Runnable {

	 
	  private static Socket clientSocket = null;
	  
	  private static PrintStream os = null;
	  
	  private static DataInputStream is = null;

	  private static BufferedReader inputLine = null;
	  private static boolean closed = false;
	  public static String username = "asdf";
	  
	
	  
	  public static void main(String[] args) {

	    
	    
	    
	    String host = "localhost";
	    int portNumber = 2222;
	    
	    if (args.length < 2) {
	    	username = "invitado";
	    	setUsername(username);
	      System.out
	          .println("Uso: java Client <host> <Username>\n"
	              + "Usando host=" + host + ", Username=" + username);
	    } else {
	      host = args[0];
	      username = args[1];
	      setUsername(username);
	    }
	   try {
		   username = args[1];
	   }catch(java.lang.ArrayIndexOutOfBoundsException e) {
		   System.out.println("SYSTEM: No ha introducido ningún usuario");
	   }
	    setUsername(username);
	 
	  
	   
	    
	    
	    try {
	      clientSocket = new Socket(host, portNumber);
	      inputLine = new BufferedReader(new InputStreamReader(System.in));
	      os = new PrintStream(clientSocket.getOutputStream());
	      is = new DataInputStream(clientSocket.getInputStream());
	    } catch (UnknownHostException e) {
	      System.err.println("No se reconoce el host:  " + host);
	    } catch (IOException e) {
	      System.err.println("No se puede realizar la conexión con el host "
	          + host);
	    }
	    String nombre = "";
	    if (clientSocket != null && os != null && is != null) {
	      try {

	    	 
	        new Thread(new Client()).start();
	       
	        if(os.toString().equals("#salir")) closed = true;

	        	os.println(username);
	        	
	        while (!closed) {
	        	if(os.toString().equals("#salir")) break;
	        	os.println(inputLine.readLine().trim());
	          
	        }
	      
	        os.close();
	        is.close();
	        clientSocket.close();
	      } catch (IOException e) {
	        System.err.println("SYSTEM: Se ha perdido la conexión con el host1");
	      }
	    }
	  }


	  public void run() {
	   
	    String responseLine;
	    try {
	      while ((responseLine = is.readLine()) != null) {
	        System.out.println(responseLine);
	        if (responseLine.indexOf("*** Adiós") != -1)
	          break;
	      }//end while
	      closed = true;
	    } catch (IOException e) {
	    	
	      System.err.println("SYSTEM: Se ha perdido la conexión con el host2");
	      closed = true;
	    }
	  }//end run
	  
	  
	  
	  public  String getUsername() {
		return username;
	}
	  public static void setUsername(String user)
	  {
		  username = user;
	  }

	
	}//end class


