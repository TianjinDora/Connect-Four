package eecs285.proj5.shuochen;

//Programmer: Shuo Chen
//Date: December 2015
//Purpose: Demonstrate the use of GUI , network, and thread to 
//         support a game, "Connect 4".
//         This project will involve developing multiple types of
//         layouts and action listener that will serve our clients.
//         The client can run the program separately to play the game,
//         and chat with each other while playing.

//The thread for playing the chess function used by the clients
public class PlayChess implements Runnable
{
  Chess chess;
  int player;

  public PlayChess(Chess inChess, int client)
  {
    chess = inChess;
    player = client;
  }

  public void run()
  {
    // The red client puts a chess firstly
    if (player == 2)
    {
      chess.disable();
    }
    while (true)
    {
      chess.otherChess();// Wait for the other player to put a chess
      chess.able();// Enable the buttons to put another chess on
    }
  }
}
