import java.io.DataInputStream;

import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.net.ServerSocket;

public class Server {
	
	  private static ServerSocket serverSocket = null;
	
	  private static Socket clientSocket = null;
	  
	  
	  /* en #charlar cambiaste idUc por y en thread[] para mostrar la lista*/
	  
	  
	  static ArrayList<String> Usernames = new ArrayList<>();
	  private static final int maxClientsCount = 10;
	  private static final clientThread[] threads = new clientThread[maxClientsCount];

	  public static void main(String args[]) {

	    
	    int portNumber = 2222;
	    if (args.length < 1) {
	      System.out
	          .println("Uso: java Server <portNumber>\n"
	              + "Ahora usado el puerto=" + portNumber);
	    } else {
	      portNumber = Integer.valueOf(args[0]).intValue();
	    }

	
	    try {
	      serverSocket = new ServerSocket(portNumber);
	    } catch (IOException e) {
	      System.out.println(e);
	    }

	 
	    while (true) {
	      try {
	        clientSocket = serverSocket.accept();
	        int i = 0;
	        for (i = 0; i < maxClientsCount; i++) {
	          if (threads[i] == null) {
	            (threads[i] = new clientThread(clientSocket, threads)).start();
	            break;
	          }
	        }
	        if (i == maxClientsCount) {
	          PrintStream os = new PrintStream(clientSocket.getOutputStream());
	          os.println("El servidor está muy lleno, inténtalo de nuevo más tarde.");
	          os.close();
	          clientSocket.close();
	          
	        }
	      } catch (IOException e) {
	        System.out.println(e);
	      }
	    }
	  }
	}


	class clientThread extends Thread {

	  private DataInputStream is = null;
	  private PrintStream os = null;	
	  private Socket clientSocket = null;
	  private final clientThread[] threads;
	  private int maxClientsCount;
	  private String lineC = null;
	  static int idUc = 0;
	  
	  
	  private String name;
	  public clientThread(Socket clientSocket, clientThread[] threads) {
	    this.clientSocket = clientSocket;
	    this.threads = threads;
	    maxClientsCount = threads.length;
	   
	  }

	  public void run() {
	    int maxClientsCount = this.maxClientsCount;
	    clientThread[] threads = this.threads;
	   
	   
	    
	    try {
	   
	      is = new DataInputStream(clientSocket.getInputStream());
	      os = new PrintStream(clientSocket.getOutputStream());
	   
	     
	     
	      name = is.readLine();
	      boolean end1 = false;
	      int i1 = 0;
	      
	      while(!end1 && i1<Server.Usernames.size()) {
	    	  if(name.equals(Server.Usernames.get(i1))||name.equals(name+(i1+1))) {
	    		  name = name+(i1+1);
	    		 while(!end1)
	    		 {
	    			 if(name.equals(name.replace(Integer.toString(i1),Integer.toString(i1+1) )))
	    			 {
	    				 String e = Integer.toString(i1+1);
	    				 String e1 = Integer.toString(i1++);
	    				 
	    				 name = name.replaceAll(e, e1);
	    				 i1++;
	    				 break;
	    			 }//end if
	    			 else break;
	    			 
	    		 }//end while
	    	  }//end if
	    	  else break;
	    	  
	      }//end while
	      
	      Server.Usernames.add(name);
	     
	      
	    
			/*for(int i = 0;i<Server.Usernames.size();i++)
			{
				if(name.equals(Server.Usernames.get(i).toString())) {idUc = Server.Usernames.indexOf(Server.Usernames.get(i)); break;}
			}//end for*/
			
			
	      
	      
	      os.println("Hola " + name
	          + " estos son los comandos de esta aplicación"
	    	  +"\n--> #charlar NombreUsuario: para abrir un chat privado" + "\n--> #listar: para lisar la lista de usuarios conectados" +"\n--> #ayuda: para mostrar este diálogo y dentro de #charlar" + "\n--> #salir: para salir de la aplicación");
	      for (int i = 0; i < maxClientsCount; i++) {
	        if (threads[i] != null && threads[i] != this) {
	          threads[i].os.println("*** Un nuevo usuario " + name
	              + " ha entrado en la sala de chat !!! ***");
	        }
	      }
	      for (int i = 0; i<threads.length;i++)
				if(threads[i] == this)
				{
					idUc = i;
					break;
					
		}
	     
	      boolean end = true;
	      boolean bcommand = true;
	     
			 while (end) {
				
			while(bcommand) {	
			boolean exists = true;
	        String line = "";line = is.readLine();
	      /*  if(is.readLine() != null)
	        {
	        	
	        }
	        else {
	        	
	        	threads[idUc].os.println("SYSTEM: Por favor introduzca un comando para comenzar");
	        	break;
	        	}*/
	        if(line.equals(null) || line.equals(""))
	        {
	        	threads[idUc].os.println("ERR---NO SE PERMITEN LÍNEAS EN BLANCO");
	        }
	        else {
	        StringTokenizer  tk = new StringTokenizer(line);
	        String command = tk.nextToken();
	      
	    	 
	      
	       
	        
	      	boolean noUser = false;
	      	 
	        switch (command) {
			case "#salir":
				Server.Usernames.remove(idUc);
				for (int i = 0; i<threads.length;i++)
					if(threads[i] == this)
					{
						idUc = i;
						break;
						
			}
				
				/*for(int i = 0;i<Server.Usernames.size();i++)
				{
					if(name.equals(Server.Usernames.get(i).toString())) {idUc = Server.Usernames.indexOf(Server.Usernames.get(i)); break;}
				}//end for*/
				
				bcommand = false;
					end = false;
				break;
			case "#charlar":
				
				String nombre = null;
				if(tk.hasMoreTokens())
				{
					
					nombre = tk.nextToken();
				}
				else
				{
					
					for (int i = 0; i<threads.length;i++)
						if(threads[i] == this)
						{
							threads[i].os.println("SYSTEM:No hay ningún usuario para iniciar una conversación. Por favor introduzca #charlar nombreUsuario");
							noUser = true;
							
							exists = false;
							
						}
					break;
					
				}//end else
				
				
				int userC = 0;
				while(exists) {
				int idCc = 0;
				for( idCc = 0;idCc<Server.Usernames.size();idCc++)
				{
					if(nombre.equals(Server.Usernames.get(Server.Usernames.indexOf(Server.Usernames.get(idCc))).toString())) 
						{
						System.out.println(Server.Usernames.get(idCc).toString());
							exists = true;
							userC = idCc;
							break;
						}//end if
					else 
						{
							
							exists = false;
						}
				
					
				}//end for
				
				if(!exists)	threads[idUc].os.println("SYSTEM:El usuario: " + nombre +" no está conectado");	
				else
				{
					State  alive = null;
					State o = State.TERMINATED;
						if(threads[idCc].equals(null))
						{
							alive = State.TERMINATED;
						}
						else alive = threads[idCc].getState();
					line = is.readLine();
					if(line.length()>0) {
					
					if(line.subSequence(0, 1).equals("#"))
					{
						switch (line) {
						case "#salir":
							Server.Usernames.remove(idUc);
							for (int i = 0; i<threads.length;i++)
								if(threads[i] == this)
								{
									idUc = i;
									break;
									
						}
							/*for(int i = 0;i<Server.Usernames.size();i++)
							{
								if(name.equals(Server.Usernames.get(i).toString())) {idUc = Server.Usernames.indexOf(Server.Usernames.get(i)); break;}
							}//end for*/
							
							
							end = false;
							bcommand = false;
							exists = false;
							//threads[idUc] = null;
							//Server.Usernames.remove(idUc);
							break;
						case "#charlar"	:
							threads[idUc].os.println("SYSTEM:Para abrir otro chat, tiene que cerrar este usando el comando #atras");
							exists = false;
							bcommand = false;
							break;
						case "#listar":
							for(int a = 0;a<Server.Usernames.size();a++) 
							{
								threads[idUc].os.println("--> " + Server.Usernames.get(a).toString());
							}
							break;
						case "#atras":
							threads[idUc].os.println("** Has abandonado el chat **");
							exists = false;
							break;
						case "#ayuda":
							threads[idUc].os.println("Comandos dentro del chat:" + "\n--> #atras: para cerrar la conversación" + "\n--> #listar: para lisar la lista de usuarios conectados" +"\n--> #ayuda: para mostrar este diálogo" + "\n--> #salir: para salir de la aplicación");
							
							
				
							break;
						default:
								//threads[idUc].os.println("ERR-- NO SE RECONOCE EL COMANDO");
							break;
						}
					}//end if
					else {
					if(alive.equals(o)||threads[idCc] == null){
						threads[idUc].os.println("SYSTEM:El usuario " + nombre + " no está conectado");
						
						exists = false;
						
					}
					else threads[idCc].os.println("<"+name+"> "+line);
					System.out.println(alive);
				}//end else
				}//end if
					else threads[idUc].os.println("SYSTEM:No se pueden enviar mensajes en blanco");
				}//end else
				
				}//end while exists
				 
				
				break;
			case "#listar":
				for(int y = 0;y<threads.length;y++)
				{
					
					
						if(threads[y] == this) {
							for(int a = 0;a<Server.Usernames.size();a++) {
								threads[y].os.println("--> " + Server.Usernames.get(a).toString());
							}
							System.out.println("SYSTEM: YES");
							
						}
							
					
				}
				break;
			case "#ayuda":
				
				threads[idUc].os.println("Comandos de la app:" + "\n--> #charlar NombreUsuario: para abrir un chat privado" + "\n--> #listar: para lisar la lista de usuarios conectados" +"\n--> #ayuda: para mostrar este diálogo" + "\n--> #salir: para salir de la aplicación");
				
				break;
			default:
				threads[idUc].os.println("SYSTEM: --"+line +"-- No se reconoce como un comando");
				break;
				 }//end switch
	       
	        
	        
	        }//end else
			}//end while bcommand
	      }//end while end
	      
	      
	      for (int i = 0; i < maxClientsCount; i++) {
	        if (threads[i] != null && threads[i] != this) {
	          threads[i].os.println("*** El usuario " + name
	              + " ha salido del chat!!! ***");
	          
	        }
	      }
	      os.println("*** Adiós " + name + " ***");

	      
	      for (int i = 0; i < maxClientsCount; i++) {
	        if (threads[i] == this) {
	          try {
				threads[i].finalize();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}; 
	        }
	      }

	     // Server.Usernames.remove(idUc);
	      is.close();
	      os.close();
	      clientSocket.close();
	    } catch (IOException e) {
	    }
	  }
}

