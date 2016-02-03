package eecs285.proj5.shuochen;

import javax.swing.JFrame;

//Programmer: Shuo Chen
//Date: December 2015
//Purpose: Demonstrate the use of GUI , network, and thread to 
//         support a game, "Connect 4".
//         This project will involve developing multiple types of
//         layouts and action listener that will serve our clients.
//         The client can run the program separately to play the game,
//         and chat with each other while playing.

//The demo for the red client
public class ClientRedDemo
{
  public static void main(String[] args)
  {
    // Drew the chess board
    Chess redChess = new Chess("Connect 4", 1);
    redChess.pack(); // resize the window
    redChess.setVisible(true);
    redChess.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // The runnable
    PlayChess play = new PlayChess(redChess, 1);
    Chat chat = new Chat(redChess, 1);
    // The threads
    Thread t1 = new Thread(play);
    Thread t2 = new Thread(chat);
    // Start all the threads at the same time
    t1.start();
    t2.start();
  }
}
