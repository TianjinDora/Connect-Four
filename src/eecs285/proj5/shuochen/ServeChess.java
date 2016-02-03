package eecs285.proj5.shuochen;

//Programmer: Shuo Chen
//Date: December 2015
//Purpose: Demonstrate the use of GUI , network, and thread to 
//         support a game, "Connect 4".
//         This project will involve developing multiple types of
//         layouts and action listener that will serve our clients.
//         The client can run the program separately to play the game,
//         and chat with each other while playing.

//The thread for manage the playing of chess used by the server
public class ServeChess implements Runnable
{
  ClientServerSocket theRedServer;
  ClientServerSocket theYellowServer;

  public ServeChess(ClientServerSocket inRed, ClientServerSocket inYellow)
  {
    theRedServer = inRed;
    theYellowServer = inYellow;
  }

  public void run()
  {
    int col;
    while (true)
    {
      col = theRedServer.recvInt();// Receive the column from the red client

      theYellowServer.sendInt(col);// Send the column to the yellow client

      col = theYellowServer.recvInt();// Receive the column from the yellow
                                      // client
      theRedServer.sendInt(col);// Send the column to the red client
    }
  }
}
