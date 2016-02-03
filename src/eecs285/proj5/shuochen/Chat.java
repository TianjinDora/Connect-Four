package eecs285.proj5.shuochen;

//Programmer: Shuo Chen
//Date: December 2015
//Purpose: Demonstrate the use of GUI , network, and thread to 
//         support a game, "Connect 4".
//         This project will involve developing multiple types of
//         layouts and action listener that will serve our clients.
//         The client can run the program separately to play the game,
//         and chat with each other while playing.

//The thread for the chat function used by the clients
public class Chat implements Runnable
{
  Chess chess;
  int player;

  public Chat(Chess inChess, int client)
  {
    chess = inChess;
    player = client;
  }

  public void run()
  {
    while (true)
    {
      chess.otherSentence();// Wait for the other player to input a sentence
    }
  }
}
