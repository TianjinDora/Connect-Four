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

public class ClientYellowDemo
{
  public static void main(String[] args)
  {
    // Drew the chess board
    Chess yellowChess = new Chess("Connect 4", 2);
    yellowChess.pack(); // resize the window
    yellowChess.setVisible(true);
    yellowChess.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // The runnable
    PlayChess play = new PlayChess(yellowChess, 2);
    Chat chat = new Chat(yellowChess, 2);
    // The threads
    Thread t1 = new Thread(play);
    Thread t2 = new Thread(chat);
    // Start all the threads at the same time
    t1.start();
    t2.start();
  }
}
