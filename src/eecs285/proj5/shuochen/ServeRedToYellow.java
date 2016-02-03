package eecs285.proj5.shuochen;

//Programmer: Shuo Chen
//Date: December 2015
//Purpose: Demonstrate the use of GUI , network, and thread to 
//         support a game, "Connect 4".
//         This project will involve developing multiple types of
//         layouts and action listener that will serve our clients.
//         The client can run the program separately to play the game,
//         and chat with each other while playing.

//The thread for sending the red client's messages to the yellow client used
//by the server
public class ServeRedToYellow implements Runnable
{
  ClientServerSocket theRedServer;
  ClientServerSocket theYellowServer;

  public ServeRedToYellow(ClientServerSocket inRed, ClientServerSocket inYellow)
  {
    theRedServer = inRed;
    theYellowServer = inYellow;
  }

  public void run()
  {
    String sentence;
    while (true)
    {
      sentence = theRedServer.recvString();// Receive the sentence from the red
                                           // client

      theYellowServer.sendString(sentence);// Send the sentence to the yellow
                                           // client
    }
  }
}
