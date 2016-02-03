package eecs285.proj5.shuochen;

//Programmer: Shuo Chen
//Date: December 2015
//Purpose: Demonstrate the use of GUI , network, and thread to 
//         support a game, "Connect 4".
//         This project will involve developing multiple types of
//         layouts and action listener that will serve our clients.
//         The client can run the program separately to play the game,
//         and chat with each other while playing.

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.lang.System.out;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.ImageIcon;

import java.net.URL;

//The frame of the chess board
public class Chess extends JFrame
{
  public static final int ROWS = 6;// The number of rows
  public static final int COLUMNS = 7;// the number of columns
  public static final int SIZE = 20;// The size of the text field
  public static final int LENGTH = 25;// The length of the area field
  public static final int WIDTH = 15;// The width of the area field
  // The chess board to record the status
  private int[][] chessBoard = new int[ROWS][COLUMNS];
  // Two players
  private int player, other;
  // The socket for the player
  private ClientServerSocket theClient, theClientChat;
  // The labels
  private JLabel[][] grid = new JLabel[ROWS][COLUMNS];
  private JLabel[] arrow = new JLabel[COLUMNS];
  // The text area
  private JTextArea record = new JTextArea(LENGTH, WIDTH);
  // The text field
  private JTextField dialog = new JTextField(SIZE);
  // The buttons
  private JButton[] drop = new JButton[COLUMNS];
  private JButton sendButton = new JButton("send");
  // The URLs
  private URL imgURL = getClass().getResource("/images/white.jpg");
  private URL arrURL = getClass().getResource("/images/arrow.png");
  private URL playerURL, otherURL;
  // The action listener
  private ChessListener putChess = new ChessListener();
  private ChatListener playerChat = new ChatListener();

  // The constructor for the chess board
  public Chess(String inTitle, int client)
  {
    super(inTitle);
    setLayout(new FlowLayout());

    // The panel for the chess board
    JPanel board = new JPanel(new GridLayout(ROWS + 2, COLUMNS));
    int row, col;
    player = client;
    // If it is the red client
    if (client == 1)
    {
      other = 2;
      playerURL = getClass().getResource("/images/red.jpg");
      otherURL = getClass().getResource("/images/yellow.jpg");
      // Construct 2 sockets for the red client
      theClient = new ClientServerSocket("127.0.0.1", 45000);
      theClientChat = new ClientServerSocket("127.0.0.1", 45001);
      out.println("Red Clinent connected.");
    }
    // If it is the yellow client
    else
    {
      other = 1;
      playerURL = getClass().getResource("/images/yellow.jpg");
      otherURL = getClass().getResource("/images/red.jpg");
      // Construct 2 sockets for the yellow client
      theClient = new ClientServerSocket("127.0.0.1", 45002);
      theClientChat = new ClientServerSocket("127.0.0.1", 45003);
      out.println("Yellow Clinent connected.");
    }
    // Start the client
    theClient.startClient();// Start the client
    theClientChat.startClient();

    // Initialize the chess board to empty, 0
    for (row = 0; row < ROWS; row++ )
    {
      for (col = 0; col < COLUMNS; col++ )
      {
        chessBoard[row][col] = 0;
      }
    }

    // Add "Drop" Buttons and add listeners on them
    for (col = 0; col < COLUMNS; col++ )
    {
      drop[col] = new JButton("Drop");
      drop[col].addActionListener(putChess);
      board.add(drop[col]);
    }

    // Add labels for arrows
    for (col = 0; col < COLUMNS; col++ )
    {
      arrow[col] = new JLabel(new ImageIcon(arrURL));
      board.add(arrow[col]);
    }

    // Add pictures on grids, and initialize the chess board to empty
    for (row = 0; row < ROWS; row++ )
    {
      for (col = 0; col < COLUMNS; col++ )
      {
        grid[row][col] = new JLabel(new ImageIcon(imgURL));
        board.add(grid[row][col]);
      }
    }
    // Add the panel
    add(board);

    // The panel for the player chat
    JPanel chat = new JPanel(new BorderLayout());
    // Add the text area
    chat.add(record, BorderLayout.NORTH);
    // The panel for the text field and the button
    JPanel send = new JPanel(new FlowLayout());
    // Add the text field
    send.add(dialog);
    // Add action listener to the send button
    sendButton.addActionListener(playerChat);
    // add the button
    send.add(sendButton);
    // Add the panel
    chat.add(send, BorderLayout.SOUTH);
    // Add the panel
    add(chat);
  }

  // This method is the action listener of the chess board
  private class ChessListener implements ActionListener
  {
    // When action is performed
    public void actionPerformed(ActionEvent event)
    {
      int row, col;
      // Check which button is clicked column by column
      for (col = 0; col < COLUMNS; col++ )
      {
        if (event.getSource() == drop[col])
        {
          // Disable all buttons to prevent the player put another chess
          disable();
          // Send the index of the clicked column to the other client
          theClient.sendInt(col);
          // Find the unoccupied row from bottom to up to put the chess on
          for (row = ROWS - 1; row >= 0; row-- )
          {
            if (chessBoard[row][col] == 0)
            {
              break;
            }
          }
          // Put the chess from the player on the board
          chessBoard[row][col] = player;
          grid[row][col].setIcon(new ImageIcon(playerURL));
          // Check whether the player wins
          // If the other player wins, show the message dialog and tell the
          // player "You win".
          // Then exit the system, terminate the game.
          if (win(row, col, player))
          {
            out.println("Win.");
            JOptionPane.showMessageDialog(null, "You win.");
            System.exit(0);
          }
          // Else if the chess board is full, tie
          // Show the message dialog and tell the player "Tie".
          // Then exit the system, terminate the game.
          else if (full())
          {
            out.println("Tie.");
            JOptionPane.showMessageDialog(null, "Tie.");
            System.exit(0);
          }
          // Else continue the game
          else
          {
            out.println("Continue.");
          }
          break;
        }
      }
    }
  }

  private class ChatListener implements ActionListener
  {
    // When action is performed
    public void actionPerformed(ActionEvent event)
    {
      if (player == 1)
      {
        record.append("Red: " + dialog.getText() + "\n");// Update the input to
                                                         // the text area
      }
      else
      {
        record.append("Yellow: " + dialog.getText() + "\n");// Update the input
                                                            // to the text area
      }
      theClientChat.sendString(dialog.getText());// Send the message
      dialog.setText("");// Clear the text field
    }
  }

  // This method can put the chess from the other player on the board and check
  // whether the player loses
  public void otherChess()
  {
    // Receive the column from the other player
    int col = theClient.recvInt();
    int row;
    // Find the unoccupied row from bottom to up to put the chess on
    for (row = ROWS - 1; row >= 0; row-- )
    {
      if (chessBoard[row][col] == 0)
      {
        break;
      }
    }
    // Put the chess from the other player on the board
    chessBoard[row][col] = other;
    grid[row][col].setIcon(new ImageIcon(otherURL));
    // If the column is full, disable the corresponding button
    if (row == 0)
    {
      drop[col].setEnabled(false);
    }
    // Check whether the other player wins.
    // If the other player wins, show the message dialog and tell the player
    // "You lose".
    // Then exit the system, terminate the game.
    if (win(row, col, other))
    {
      out.println("Lose.");
      JOptionPane.showMessageDialog(null, "You lose.");
      System.exit(0);
    }
    // Else if the chess board is full, tie
    // Show the message dialog and tell the player "Tie".
    // Then exit the system, terminate the game.
    else if (full())
    {
      out.println("Tie.");
      JOptionPane.showMessageDialog(null, "Tie.");
      System.exit(0);
    }
  }

  // This method can disable the "Drop" buttons
  public void disable()
  {
    // Disable all the buttons one by one
    for (int col = 0; col < COLUMNS; col++ )
    {
      drop[col].setEnabled(false);
    }
  }

  // This method can enable the "Drop" buttons
  public void able()
  {
    // Enable all the buttons one by one
    for (int col = 0; col < COLUMNS; col++ )
    {
      // If the column is full, disable the corresponding button
      if (chessBoard[0][col] != 0)
      {
        drop[col].setEnabled(false);
      }
      // Else enable the button
      else
      {
        drop[col].setEnabled(true);
      }
    }
  }

  // This method can check whether a player wins the game
  // If a player wins, return true; else, return false
  private boolean win(int row, int col, int player)
  {
    int check;
    // horizontal line
    // Start checking at a possible and valid leftmost point
    for (int start = Math.max(col - 3, 0); start <= Math.min(col,
        COLUMNS - 4); start++ )
    {
      // Check 4 chess horizontally from left to right
      for (check = 0; check < 4; check++ )
      {
        // The coordinate of column increases 1 every time
        if (chessBoard[row][start + check] != player)
        {
          break;
        }
      }
      // If the checking passed, win
      if (check == 4)
      {
        return true;
      }
    }

    // vertical line
    // Start checking at the point inputed if there exists at least 3 rows under
    // it.
    if (row + 3 < ROWS)
    {
      // Check another 3 chess vertically from top to bottom
      for (check = 1; check < 4; check++ )
      {
        // The coordinate of row increases 1 every time
        // Check if the chess all belong to the player
        if (chessBoard[row + check][col] != player)
        {
          break;
        }
      }
      // If the checking passed, win
      if (check == 4)
      {
        return true;
      }
    }

    // diagonal line "\"
    // Start checking at a possible and valid leftmost and upmost point
    for (int start = Math.max(Math.max(col - 3, 0), col - row); start <= Math
        .min(Math.min(col, COLUMNS - 4), col + ROWS - row - 4); start++ )
    {
      // Check 4 chess diagonally from left and top to right and bottom
      for (check = 0; check < 4; check++ )
      {
        // The coordinate of row increases 1 every time
        // The coordinate of column increases 1 every time
        // Check if the chess all belong to the player
        if (chessBoard[row - col + start + check][start + check] != player)
        {
          break;
        }
      }
      // If the checking passed, win
      if (check == 4)
      {
        return true;
      }
    }

    // diagonal line "/"
    // Start checking at a possible and valid leftmost and downmost point
    for (int start = Math.max(Math.max(col - 3, 0),
        col - ROWS + row + 1); start <= Math.min(Math.min(col, COLUMNS - 4),
            col + row - 3); start++ )
    {
      // Check 4 chess diagonally from left and bottom to right and up
      for (check = 0; check < 4; check++ )
      {
        // The coordinate of row decreases 1 every time
        // The coordinate of column increases 1 every time
        // Check if the chess all belong to the player
        if (chessBoard[row + col - start - check][start + check] != player)
        {
          break;
        }
      }
      // If the checking passed, win
      if (check == 4)
      {
        return true;
      }
    }
    // no checking passed, not win
    return false;
  }

  // This method can check whether the chess board is full
  // If the chess board is full, return true; else, return false
  private boolean full()
  {
    // Check whether every column is full one by one
    for (int col = 0; col < COLUMNS; col++ )
    {
      // If a column is not full, the check failed
      if (chessBoard[0][col] == 0)
      {
        return false;
      }
    }
    // If all columns are full, the check passes
    return true;
  }

  // This method can receive the sentence sent from the other player
  public void otherSentence()
  {
    // Receive the string from the other player
    if (other == 1)
    {
      record.append("Red: " + theClientChat.recvString() + "\n");
    }
    else
    {
      record.append("Yellow: " + theClientChat.recvString() + "\n");
    }
  }
}
