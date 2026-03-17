import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

public class Checkers extends JPanel {

    public static void main(String[] args) {
        JFrame window = new JFrame("Peg Solitaire");
        Checkers content = new Checkers();
        window.setContentPane(content);
        window.pack();
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        window.setLocation( (screensize.width - window.getWidth())/2,
                (screensize.height - window.getHeight())/2 );
        window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        window.setResizable(false);  
        window.setVisible(true);
    }
    
    private JButton newGameButton;
    private JButton resignButton;
    private JButton sizeButton; // New button to change board size
    private JLabel message;  

   public Checkers() {
        setLayout(null);  
        
        // 1. INCREASED WINDOW SIZE to fit the bigger board
        
        setPreferredSize( new Dimension(700, 600) );
        setBackground(new Color(0,150,0));  

        Board board = new Board();  
        add(board);
        add(newGameButton);
        add(resignButton);
        add(sizeButton);
        add(message);

        // 2. INCREASED BOARD BOUNDS (allows up to a 13x13 grid at 40px each)
        board.setBounds(20, 20, 540, 540); 
        
        // Pushed the buttons further to the right so they don't overlap the board
        newGameButton.setBounds(560, 60, 120, 30);
        resignButton.setBounds(560, 100, 120, 30);
        sizeButton.setBounds(560, 140, 120, 30); 
        message.setBounds(20, 560, 450, 30);
    }
    private static class CheckersMove {
        int fromRow, fromCol;  
        int toRow, toCol;      
        CheckersMove(int r1, int c1, int r2, int c2) {
            fromRow = r1;
            fromCol = c1;
            toRow = r2;
            toCol = c2;
        }
        boolean isJump() {
            return (Math.abs(fromRow - toRow) == 2 || Math.abs(fromCol - toCol) == 2);
        }
    }  

    private class Board extends JPanel implements ActionListener, MouseListener {

        CheckersData board; 
        boolean gameInProgress; 
        int currentPlayer;      
        int selectedRow, selectedCol;   
        CheckersMove[] legalMoves;  

        Board() {
setBackground(new Color(0,150,0)); // Change background to match main panel color            
            addMouseListener(this);
            resignButton = new JButton("Resign");
            resignButton.addActionListener(this);
            newGameButton = new JButton("New Game");
            newGameButton.addActionListener(this);
            
            sizeButton = new JButton("Change Size");
            sizeButton.addActionListener(this);
            
            message = new JLabel("",JLabel.CENTER);
            message.setFont(new  Font("Serif", Font.BOLD, 14));
            message.setForeground(Color.green);
            board = new CheckersData();
            doNewGame();
        }

        public void actionPerformed(ActionEvent evt) {
            Object src = evt.getSource();
            if (src == newGameButton)
                doNewGame();
            else if (src == resignButton)
                doResign();
            else if (src == sizeButton)
                doChangeSize();
        }

        // --- NEW METHOD FOR DYNAMIC SIZING ---
        void doChangeSize() {
            String input = JOptionPane.showInputDialog(this, "Enter board size (odd numbers only, e.g., 7, 9):");
            if (input != null) {
                try {
                    int newSize = Integer.parseInt(input);
                    // Validate: Must be an odd number and at least 3
                    if (newSize < 3 || newSize % 2 == 0) {
                        JOptionPane.showMessageDialog(this, "Invalid! Size must be an odd number greater than 2.");
                    } else {
                        board.setBoardSize(newSize);
                        doNewGame();
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid number.");
                }
            }
        }

        void doNewGame() {
            if (gameInProgress == true) {
                message.setText("Finish the current game first!");
                return;
            }
            board.setUpGame();   
            currentPlayer = CheckersData.PEG;   
            legalMoves = board.getLegalMoves(currentPlayer);  
            selectedRow = -1;   
            message.setText("Make your move.");
            gameInProgress = true;
            newGameButton.setEnabled(false);
            sizeButton.setEnabled(false); // Lock size changes during a game
            resignButton.setEnabled(true);
            repaint();
        }

        void doResign() {
            if (gameInProgress == false) {  
                message.setText("There is no game in progress!");
                return;
            }
            gameOver("You resigned.");
        }

        void gameOver(String str) {
            message.setText(str);
            newGameButton.setEnabled(true);
            sizeButton.setEnabled(true); // Unlock size changes
            resignButton.setEnabled(false);
            gameInProgress = false;
        }

        void doClickSquare(int row, int col) {
            for (int i = 0; i < legalMoves.length; i++)
                if (legalMoves[i].fromRow == row && legalMoves[i].fromCol == col) {
                    selectedRow = row;
                    selectedCol = col;
                    message.setText("Make your move.");
                    repaint();
                    return;
                }

            if (selectedRow < 0) {
                message.setText("Click the piece you want to move.");
                return;
            }

            for (int i = 0; i < legalMoves.length; i++)
                if (legalMoves[i].fromRow == selectedRow && legalMoves[i].fromCol == selectedCol
                && legalMoves[i].toRow == row && legalMoves[i].toCol == col) {
                    doMakeMove(legalMoves[i]);
                    return;
                }

            message.setText("Click the square you want to move to.");
        }  

        void doMakeMove(CheckersMove move) {
            board.makeMove(move);
            legalMoves = board.getLegalMoves(currentPlayer);
            
            if (legalMoves == null) {
                int pegsRemaining = 0;
                for(int r=0; r<board.boardSize; r++) {
                    for(int c=0; c<board.boardSize; c++) {
                        if(board.pieceAt(r,c) == CheckersData.PEG) pegsRemaining++;
                    }
                }
                
                if (pegsRemaining == 1) {
                    gameOver("You Win! Only 1 peg left!");
                } else {
                    gameOver("Game Over! Pegs left: " + pegsRemaining);
                }
            } else {
                message.setText("Make your move.");
            }

            selectedRow = -1;
            repaint();
        }  

        public void paintComponent(Graphics g) {
            super.paintComponent(g); 
            
            Graphics2D g2 = (Graphics2D)g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            for (int row = 0; row < board.boardSize; row++) {
                for (int col = 0; col < board.boardSize; col++) {
                    if (board.pieceAt(row, col) == CheckersData.INVALID)
                        g.setColor(Color.DARK_GRAY);
                    else
                        g.setColor(Color.LIGHT_GRAY);
                    
                    // SCALED UP: 40x40 squares
                    g.fillRect(2 + col*40, 2 + row*40, 40, 40);
                    
                    switch (board.pieceAt(row,col)) {
                    case CheckersData.PEG:
                        g.setColor(Color.RED);
                        // SCALED UP: 32x32 pegs
                        g.fillOval(6 + col*40, 6 + row*40, 32, 32);
                        break;
                    case CheckersData.EMPTY:
                        g.setColor(Color.BLACK);
                        // SCALED UP: 12x12 empty hole markers
                        g.fillOval(16 + col*40, 16 + row*40, 12, 12); 
                        break;
                    }
                }
            }

            if (gameInProgress && legalMoves != null) {
                g.setColor(Color.cyan);
                for (int i = 0; i < legalMoves.length; i++) {
                    // SCALED UP HIGHLIGHTS
                    g.drawRect(2 + legalMoves[i].fromCol*40, 2 + legalMoves[i].fromRow*40, 39, 39);
                    g.drawRect(3 + legalMoves[i].fromCol*40, 3 + legalMoves[i].fromRow*40, 37, 37);
                }
                if (selectedRow >= 0) {
                    g.setColor(Color.white);
                    g.drawRect(2 + selectedCol*40, 2 + selectedRow*40, 39, 39);
                    g.drawRect(3 + selectedCol*40, 3 + selectedRow*40, 37, 37);
                    g.setColor(Color.green);
                    for (int i = 0; i < legalMoves.length; i++) {
                        if (legalMoves[i].fromCol == selectedCol && legalMoves[i].fromRow == selectedRow) {
                            g.drawRect(2 + legalMoves[i].toCol*40, 2 + legalMoves[i].toRow*40, 39, 39);
                            g.drawRect(3 + legalMoves[i].toCol*40, 3 + legalMoves[i].toRow*40, 37, 37);
                        }
                    }
                }
            }
        }
        public void mousePressed(MouseEvent evt) {
            if (gameInProgress == false)
                message.setText("Click \"New Game\" to start a new game.");
            else {
                // Divide the mouse click coordinates by 40
                int col = (evt.getX() - 2) / 40;
                int row = (evt.getY() - 2) / 40;
                if (col >= 0 && col < board.boardSize && row >= 0 && row < board.boardSize)
                    doClickSquare(row,col);
            }
        }
        public void mouseReleased(MouseEvent evt) { }
        public void mouseClicked(MouseEvent evt) { }
        public void mouseEntered(MouseEvent evt) { }
        public void mouseExited(MouseEvent evt) { }
    }  

    private static class CheckersData {

        static final int EMPTY = 0, PEG = 1, INVALID = 2;

        int[][] board;  
        int boardSize = 7; // Default starting size

        CheckersData() {
            setUpGame();
        }

        // Sets up a new array using the current boardSize
        public void setBoardSize(int newSize) {
            this.boardSize = newSize;
        }

        void setUpGame() {
            board = new int[boardSize][boardSize];
            
            // The size of the missing corners scales with the board size
            int cornerSize = boardSize / 3;
            int center = boardSize / 2;

            for (int row = 0; row < boardSize; row++) {
                for (int col = 0; col < boardSize; col++) {
                    
                    boolean isCorner = (row < cornerSize || row >= boardSize - cornerSize) && 
                                       (col < cornerSize || col >= boardSize - cornerSize);
                    
                    boolean isCenter = (row == center && col == center);
                    
                    if (isCorner) {
                        board[row][col] = INVALID;
                    } else if (isCenter) {
                        board[row][col] = EMPTY;
                    } else {
                        board[row][col] = PEG;
                    }
                }
            }
        }  

        int pieceAt(int row, int col) {
            return board[row][col];
        }

        void makeMove(CheckersMove move) {
            makeMove(move.fromRow, move.fromCol, move.toRow, move.toCol);
        }

        void makeMove(int fromRow, int fromCol, int toRow, int toCol) {
            board[toRow][toCol] = board[fromRow][fromCol];
            board[fromRow][fromCol] = EMPTY;
            
            int jumpRow = (fromRow + toRow) / 2;  
            int jumpCol = (fromCol + toCol) / 2;  
            board[jumpRow][jumpCol] = EMPTY;
        }

        CheckersMove[] getLegalMoves(int player) {
            if (player != PEG) return null;

            ArrayList<CheckersMove> moves = new ArrayList<CheckersMove>();

            for (int row = 0; row < boardSize; row++) {
                for (int col = 0; col < boardSize; col++) {
                    if (board[row][col] == player) {
                        if (canJump(row, col, row-1, col, row-2, col)) moves.add(new CheckersMove(row, col, row-2, col));
                        if (canJump(row, col, row+1, col, row+2, col)) moves.add(new CheckersMove(row, col, row+2, col));
                        if (canJump(row, col, row, col-1, row, col-2)) moves.add(new CheckersMove(row, col, row, col-2));
                        if (canJump(row, col, row, col+1, row, col+2)) moves.add(new CheckersMove(row, col, row, col+2));
                    }
                }
            }

            if (moves.size() == 0) return null;
            
            CheckersMove[] moveArray = new CheckersMove[moves.size()];
            for (int i = 0; i < moves.size(); i++) moveArray[i] = moves.get(i);
            return moveArray;
        }  

        CheckersMove[] getLegalJumpsFrom(int player, int row, int col) {
            if (player != PEG) return null;

            ArrayList<CheckersMove> moves = new ArrayList<CheckersMove>();
            
            if (board[row][col] == player) {
                if (canJump(row, col, row-1, col, row-2, col)) moves.add(new CheckersMove(row, col, row-2, col));
                if (canJump(row, col, row+1, col, row+2, col)) moves.add(new CheckersMove(row, col, row+2, col));
                if (canJump(row, col, row, col-1, row, col-2)) moves.add(new CheckersMove(row, col, row, col-2));
                if (canJump(row, col, row, col+1, row, col+2)) moves.add(new CheckersMove(row, col, row, col+2));
            }
            
            if (moves.size() == 0) return null;
            
            CheckersMove[] moveArray = new CheckersMove[moves.size()];
            for (int i = 0; i < moves.size(); i++) moveArray[i] = moves.get(i);
            return moveArray;
        }  

        private boolean canJump(int r1, int c1, int r2, int c2, int r3, int c3) {
            // Updated to check bounds dynamically against boardSize!
            if (r3 < 0 || r3 >= boardSize || c3 < 0 || c3 >= boardSize)
                return false; 

            if (board[r3][c3] != EMPTY)
                return false; 

            if (board[r2][c2] != PEG)
                return false; 

            return true; 
        }
    } 
}