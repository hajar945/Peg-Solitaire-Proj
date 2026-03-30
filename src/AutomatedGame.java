public class AutomatedGame extends Game {

    public AutomatedGame() {
        super();
    }

    // This method selects one item from the list of legal moves and executes it.
    public Move playAutomatedMove() {
        Move[] moves = getLegalMoves();
        
        // If there are zero moves available, it outputs null.
        if (moves == null) return null;
        
        // Math.random() generates a decimal.
        // Multiplying it by moves.length limits the decimal to the size of the array.
        // (int) is a process called casting. It deletes the decimal point to leave a whole integer.
        int randomIndex = (int)(Math.random() * moves.length);
        
        // It passes the selected move to the method that updates the array.
        makeMove(moves[randomIndex]);
        
        // It outputs the selected move so the UI file knows what to draw on the screen.
        return moves[randomIndex];
    }
}