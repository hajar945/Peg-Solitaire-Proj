import java.util.ArrayList;

public abstract class Game {

    // final means these variables cannot be changed after the program starts
    // static means these variables are shared across the entire program
    public static final int EMPTY = 0, PEG = 1, INVALID = 2;
    public static final int ENGLISH = 0, HEXAGON = 1, DIAMOND = 2;

    // protected means these variables can be accessed by files that copy this class
    // int[][] makes a 2d array of rows and columns
    protected int[][] board;
    protected int boardSize = 7;
    protected int boardType = ENGLISH;

    // constructor.that runs automatically when the class is initialized
    public Game() {
        setUpGame();
    }

    // updates the boardSize variable with a new integer
    public void setBoardSize(int newSize) {
        this.boardSize = newSize;
    }

    // returns the current integer stored in boardSize
    public int getBoardSize() {
        return this.boardSize;
    }

    // updates the boardType variable
    public void setBoardType(int newType) {
        this.boardType = newType;
    }

    // returns the current integer stored in boardType
    public int getBoardType() {
        return this.boardType;
    }

    // builds the initial starting state of the board
    public void setUpGame() {
        // makes a new 2d array based on the boardSize
        board = new int[boardSize][boardSize];
        
        int cornerSize = boardSize / 3; 
        int center = boardSize / 2; 

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                
                boolean isCorner = false;

                if (boardType == ENGLISH) {
                    isCorner = (row < cornerSize || row >= boardSize - cornerSize) &&
                               (col < cornerSize || col >= boardSize - cornerSize);
                               
                } else if (boardType == DIAMOND) {
                    int distance = Math.abs(row - center) + Math.abs(col - center);
                    isCorner = (distance > center);
                    
                } else if (boardType == HEXAGON) {
                    int distance = Math.abs(row - center) + Math.abs(col - center);
                    isCorner = (distance > center + (boardSize / 4));
                }

                boolean isCenter = (row == center && col == center);

                // assigns the starting numbers to specific coordinates in the array
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

    // returns the integer found at specific row and column coordinates in the array
    public int pieceAt(int row, int col) {
        return board[row][col];
    }

    // takes a Move object, gets the four integers inside it, and passes them to the next method
    public void makeMove(Move move) {
        makeMove(move.fromRow, move.fromCol, move.toRow, move.toCol);
    }

    // overwrites the data in the array to do a jump
    public void makeMove(int fromRow, int fromCol, int toRow, int toCol) {
        board[toRow][toCol] = board[fromRow][fromCol];
        board[fromRow][fromCol] = EMPTY;

        // calculates the exact midpoint between the start and end coordinates
        int jumpRow = (fromRow + toRow) / 2;
        int jumpCol = (fromCol + toCol) / 2;
        board[jumpRow][jumpCol] = EMPTY;
    }

    // looks through the entire array to find coordinates that meet the requirements for a legal jump
    public Move[] getLegalMoves() {

        ArrayList<Move> moves = new ArrayList<>();

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (board[row][col] == PEG) {
                    if (canJump(row, col, row - 1, col, row - 2, col)) moves.add(new Move(row, col, row - 2, col));
                    if (canJump(row, col, row + 1, col, row + 2, col)) moves.add(new Move(row, col, row + 2, col));
                    if (canJump(row, col, row, col - 1, row, col - 2)) moves.add(new Move(row, col, row, col - 2));
                    if (canJump(row, col, row, col + 1, row, col + 2)) moves.add(new Move(row, col, row, col + 2));
                }
            }
        }

        // checks if the ArrayList has zero items inside it.
        if (moves.isEmpty()) return null;

        // converts the resizable ArrayList into a fixed size Array before returning the data
        Move[] moveArray = new Move[moves.size()];
        return moves.toArray(moveArray);
    }

    // checks if a specific set of coordinates violates the boundaries or state game rules
    private boolean canJump(int r1, int c1, int r2, int c2, int r3, int c3) {
        if (r3 < 0 || r3 >= boardSize || c3 < 0 || c3 >= boardSize) return false;
        if (board[r3][c3] != EMPTY) return false;
        if (board[r2][c2] != PEG) return false;
        return true;
    }

    // iterate through the array and assign new values using a random number generator
    public void randomizeBoard() {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
  
                if (board[row][col] != INVALID) {
                    // Math.random() outputs a decimal between 0.0 and 1.0
                    // makes a 50 % chance condition
                    if (Math.random() > 0.5) {
                        board[row][col] = PEG;
                    } else {
                        board[row][col] = EMPTY;
                    }
                }
            }
        }
    }
}