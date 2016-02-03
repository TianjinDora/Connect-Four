package eecs285.proj5.shuochen;

//Programmer: Shuo Chen
//Date: December 2015
//Purpose: Demonstrate the use of GUI , network, and thread to 
//         support a game, "Connect 4".
//         This project will involve developing multiple types of
//         layouts and action listener that will serve our clients.
//         The client can run the program separately to play the game,
//         and chat with each other while playing.

//The demo for the server
public class ServerDemo
{
  public static void main(String args[])
  {
    // Create 2 sockets for the red client, one for integer, and the other one
    // for string
    ClientServerSocket theRedServer = new ClientServerSocket("127.0.0.1",
        45000);
    ClientServerSocket theRedServerChat = new ClientServerSocket("127.0.0.1",
        45001);
    // Start the servers for the red client
    theRedServer.startServer();
    theRedServerChat.startServer();
    // Create 2 sockets for the yellow client, one for integer, and the other
    // one
    // for string
    ClientServerSocket theYellowServer = new ClientServerSocket("127.0.0.1",
        45002);
    ClientServerSocket theYellowServerChat = new ClientServerSocket("127.0.0.1",
        45003);
    // Start the servers for the yellow client
    theYellowServer.startServer();
    theYellowServerChat.startServer();
    // The runnable
    ServeChess serChess = new ServeChess(theRedServer, theYellowServer);
    ServeRedToYellow redToYellow = new ServeRedToYellow(theRedServerChat,
        theYellowServerChat);
    SendYellowToRed yellowToRed = new SendYellowToRed(theRedServerChat,
        theYellowServerChat);
    // The threads
    Thread t1 = new Thread(serChess);
    Thread t2 = new Thread(redToYellow);
    Thread t3 = new Thread(yellowToRed);
    // Start all the threads at the same time
    t1.start();
    t2.start();
    t3.start();
  }
}
