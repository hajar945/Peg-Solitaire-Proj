import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AutoGameTest {

    @Test
    public void testFirstAutomatedMoveIsValid() {
        // create a brand new automated game.
        AutomatedGame game = new AutomatedGame();
        
        // tell the computer to take its first turn and save the move it chose
        Move move = game.playAutomatedMove();
        
        // assertNotNull makes sure that the computer actually found a move and didn't crash or return nothing
        assertNotNull(move, "The automated player should find a valid first move.");
        
        // assertTrue verifies that the move the computer chose mathematically qualifies as a jump (distance of 2)
        assertTrue(move.isJump(), "The automated move must be a legal jump of 2 spaces.");
    }

    @Test
    public void testAutomatedMoveAlwaysLandsInCenterOnTurnOne() {
        AutomatedGame game = new AutomatedGame();
        
        // the center hole (Row 3, Col 3) is the only empty spot at the start
        // so no matter the random move the computer picks on the first turn, it has to land in the center
        game.playAutomatedMove();
        
        // assertEquals verifies that the center hole now has a peg
        assertEquals(Game.PEG, game.pieceAt(3, 3), "After the first automated move, the center hole must contain a peg.");
    }

    @Test
    public void testAutomatedGameEventuallyEnds() {
        AutomatedGame game = new AutomatedGame();
        
        // create a limit to prevent the test from getting stuck in an infinite loop
        int maxTurns = 100;
        int turnsTaken = 0;
        
        // while loop forces the computer to play against itself repeatedly
        // keeps looping as long as playAutomatedMove() finds a valid move and hasn't hit 100 turns
        while (game.playAutomatedMove() != null && turnsTaken < maxTurns) {
            turnsTaken++;
        }
        
        // use assertNull to check that getLegalMoves() also confirms the game is over
        assertNull(game.getLegalMoves(), "When the automated player finishes playing, there should be no legal moves left.");
    }
}