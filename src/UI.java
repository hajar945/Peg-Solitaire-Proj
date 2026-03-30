import java.awt.*;
import java.awt.event.*;

// timer is a built-in class that repeatedly runs code on a time delay
import javax.swing.*;

public class UI extends JPanel implements ActionListener, MouseListener {

    private Game game;
    private BoardPanel boardPanel;
    private JButton newGameButton, sizeButton, autoplayBtn, randomizeBtn;
    private JRadioButton englishBtn, hexagonBtn, diamondBtn;
    private JRadioButton manualModeBtn, autoModeBtn;
    private JLabel message;

    private boolean gameInProgress;
    private int selectedRow, selectedCol;
    private Move[] legalMoves;
    private Timer autoTimer;

    public UI() {
        // program defaults to the manual mode subclass on launch
        game = new ManualGame();
        
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(0, 150, 0));

        boardPanel = new BoardPanel();
        boardPanel.addMouseListener(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(new Color(0, 150, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 0));

        newGameButton = new JButton("New Game");
        newGameButton.addActionListener(this);
        
        sizeButton = new JButton("Change Size");
        sizeButton.addActionListener(this);

        // ;abel identifying the board shape buttons
        JLabel typeLabel = new JLabel("Board Type:");
        typeLabel.setForeground(Color.WHITE);
        englishBtn = new JRadioButton("English", true); 
        hexagonBtn = new JRadioButton("Hexagon");
        diamondBtn = new JRadioButton("Diamond");
        englishBtn.setBackground(new Color(0, 150, 0)); englishBtn.setForeground(Color.WHITE);
        hexagonBtn.setBackground(new Color(0, 150, 0)); hexagonBtn.setForeground(Color.WHITE);
        diamondBtn.setBackground(new Color(0, 150, 0)); diamondBtn.setForeground(Color.WHITE);

        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(englishBtn); typeGroup.add(hexagonBtn); typeGroup.add(diamondBtn);
        englishBtn.addActionListener(this); hexagonBtn.addActionListener(this); diamondBtn.addActionListener(this);

        // label identifying the mode selection buttons
        JLabel modeLabel = new JLabel("Game Mode:");
        modeLabel.setForeground(Color.WHITE);
        manualModeBtn = new JRadioButton("Manual", true);
        autoModeBtn = new JRadioButton("Automated");
        manualModeBtn.setBackground(new Color(0, 150, 0)); manualModeBtn.setForeground(Color.WHITE);
        autoModeBtn.setBackground(new Color(0, 150, 0)); autoModeBtn.setForeground(Color.WHITE);
        
        ButtonGroup modeGroup = new ButtonGroup();
        modeGroup.add(manualModeBtn); modeGroup.add(autoModeBtn);
        manualModeBtn.addActionListener(this); autoModeBtn.addActionListener(this);

        // autoplay buttons
        autoplayBtn = new JButton("Autoplay");
        autoplayBtn.addActionListener(this);
        autoplayBtn.setEnabled(false); // defaults to disabled until autoplay mode is selected

        randomizeBtn = new JButton("Randomize");
        randomizeBtn.addActionListener(this);

        // adds all the graphic elements to a vertical list on the left side of the window
        buttonPanel.add(newGameButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(sizeButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(typeLabel);
        buttonPanel.add(englishBtn);
        buttonPanel.add(hexagonBtn);
        buttonPanel.add(diamondBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(modeLabel);
        buttonPanel.add(manualModeBtn);
        buttonPanel.add(autoModeBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(autoplayBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(randomizeBtn);

        message = new JLabel("", JLabel.CENTER);
        message.setFont(new Font("Serif", Font.BOLD, 14));
        message.setForeground(Color.GREEN);
        message.setPreferredSize(new Dimension(0, 40));

        add(buttonPanel, BorderLayout.WEST);
        add(boardPanel, BorderLayout.CENTER);
        add(message, BorderLayout.SOUTH);

        // built-in Timer to do an action every 500 milliseconds
        autoTimer = new Timer(500, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                executeAutoTurn();
            }
        });

        doNewGame();
    }

    // runs whenever the user clicks any button in the interface
    public void actionPerformed(ActionEvent evt) {
        Object src = evt.getSource();
        
        if (src == newGameButton) doNewGame();
        else if (src == sizeButton) doChangeSize();
        else if (src == randomizeBtn) doRandomize();
        else if (src == autoplayBtn) {
            // start the looping timer
            autoTimer.start();
            message.setText("Autoplay is running...");
        }
        else {
            // updates the shape and mode variables based on the radio button clicked
            int currentSize = game.getBoardSize();
            int currentType = game.getBoardType();

            if (src == englishBtn) currentType = Game.ENGLISH;
            else if (src == hexagonBtn) currentType = Game.HEXAGON;
            else if (src == diamondBtn) currentType = Game.DIAMOND;

            if (manualModeBtn.isSelected()) {
                game = new ManualGame();
                autoplayBtn.setEnabled(false);
                autoTimer.stop();
            } else if (autoModeBtn.isSelected()) {
                game = new AutomatedGame();
                autoplayBtn.setEnabled(true);
            }

            game.setBoardSize(currentSize);
            game.setBoardType(currentType);
            doNewGame();
        }
    }

    private void doChangeSize() {
        String input = JOptionPane.showInputDialog(this, "Enter board size (odd numbers only):");
        if (input != null) {
            try {
                
                int newSize = Integer.parseInt(input); // Integer.parseInt() converts text characters into a mathematical integer
                if (newSize < 3 || newSize % 2 == 0) {
                    JOptionPane.showMessageDialog(this, "Invalid! Size must be an odd number greater than 2.");
                } else {
                    game.setBoardSize(newSize);
                    doNewGame();
                    Window window = SwingUtilities.getWindowAncestor(this);
                    if (window != null) {
                        window.pack();
                        window.setLocationRelativeTo(null);
                    }
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number.");
            }
        }
    }

    private void doNewGame() {
        autoTimer.stop();
        game.setUpGame();
        legalMoves = game.getLegalMoves();
        selectedRow = -1;
        message.setText("Make your move.");
        gameInProgress = true;
        // repaint() is a built-in function that forces the visual screen to redraw using the updated array data
        repaint(); 
    }

    // triggers the randomize method in the Game class and resets local state vars
    private void doRandomize() {
        if (!gameInProgress) return;
        autoTimer.stop();
        game.randomizeBoard();
        legalMoves = game.getLegalMoves();
        selectedRow = -1;
        
        if (legalMoves == null) {
            gameOver("Randomized into a Game Over state!");
        } else {
            message.setText("Board randomized. Make your move.");
        }
        repaint();
    }

    private void gameOver(String str) {
        message.setText(str);
        gameInProgress = false;
        autoTimer.stop();
    }

    // this is called by the looping Timer when Autoplay is running
    private void executeAutoTurn() {
        // instanceof keyword verifies that the game variable currently holds the AutomatedGame subclass
        if (game instanceof AutomatedGame && gameInProgress) {
            Move m = ((AutomatedGame) game).playAutomatedMove();
            if (m != null) {
                legalMoves = game.getLegalMoves();
                checkGameOverConditions();
                repaint();
            }
        }
    }

    private void doClickSquare(int row, int col) {
        
        if (game instanceof AutomatedGame) return;// blocks manual mouse clicks if the user has selected Automated mode

        for (int i = 0; i < legalMoves.length; i++) {
            if (legalMoves[i].fromRow == row && legalMoves[i].fromCol == col) {
                selectedRow = row;
                selectedCol = col;
                message.setText("Make your move.");
                repaint();
                return;
            }
        }

        if (selectedRow < 0) {
            message.setText("Click the piece you want to move.");
            return;
        }

        for (int i = 0; i < legalMoves.length; i++) {
            if (legalMoves[i].fromRow == selectedRow && legalMoves[i].fromCol == selectedCol &&
                legalMoves[i].toRow == row && legalMoves[i].toCol == col) {
                
                game.makeMove(legalMoves[i]);
                legalMoves = game.getLegalMoves();
                checkGameOverConditions();
                selectedRow = -1;
                repaint();
                return;
            }
        }

        message.setText("Click the square you want to move to.");
    }

    // checks the array size to count pegs and decide if the user won or lost
    private void checkGameOverConditions() {
        if (legalMoves == null) {
            int pegsRemaining = 0;
            int size = game.getBoardSize();
            for (int r = 0; r < size; r++) {
                for (int c = 0; c < size; c++) {
                    if (game.pieceAt(r, c) == Game.PEG) pegsRemaining++;
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
    }

    private class BoardPanel extends JPanel {
        public BoardPanel() {
            setBackground(new Color(0, 150, 0));
        }

        @Override
        public Dimension getPreferredSize() {
            if (game == null) return new Dimension(300, 300);
            int pixelSize = (game.getBoardSize() * 40) + 20;
            return new Dimension(pixelSize, pixelSize);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int size = game.getBoardSize();
            for (int row = 0; row < size; row++) {
                for (int col = 0; col < size; col++) {
                    int piece = game.pieceAt(row, col);
                    
                    if (piece == Game.INVALID) g.setColor(Color.DARK_GRAY);
                    else g.setColor(Color.LIGHT_GRAY);

                    g.fillRect(2 + col * 40, 2 + row * 40, 40, 40);

                    if (piece == Game.PEG) {
                        g.setColor(Color.RED);
                        g.fillOval(6 + col * 40, 6 + row * 40, 32, 32);
                    } else if (piece == Game.EMPTY) {
                        g.setColor(Color.BLACK);
                        g.fillOval(16 + col * 40, 16 + row * 40, 12, 12);
                    }
                }
            }

            if (gameInProgress && legalMoves != null) {
                g.setColor(Color.cyan);
                for (int i = 0; i < legalMoves.length; i++) {
                    g.drawRect(2 + legalMoves[i].fromCol * 40, 2 + legalMoves[i].fromRow * 40, 39, 39);
                    g.drawRect(3 + legalMoves[i].fromCol * 40, 3 + legalMoves[i].fromRow * 40, 37, 37);
                }
                if (selectedRow >= 0) {
                    g.setColor(Color.white);
                    g.drawRect(2 + selectedCol * 40, 2 + selectedRow * 40, 39, 39);
                    g.drawRect(3 + selectedCol * 40, 3 + selectedRow * 40, 37, 37);
                    g.setColor(Color.green);
                    for (int i = 0; i < legalMoves.length; i++) {
                        if (legalMoves[i].fromCol == selectedCol && legalMoves[i].fromRow == selectedRow) {
                            g.drawRect(2 + legalMoves[i].toCol * 40, 2 + legalMoves[i].toRow * 40, 39, 39);
                            g.drawRect(3 + legalMoves[i].toCol * 40, 3 + legalMoves[i].toRow * 40, 37, 37);
                        }
                    }
                }
            }
        }
    }

    public void mousePressed(MouseEvent evt) {
        if (!gameInProgress) {
            message.setText("Click \"New Game\" to start a new game.");
        } else {
            int col = (evt.getX() - 2) / 40;
            int row = (evt.getY() - 2) / 40;
            if (col >= 0 && col < game.getBoardSize() && row >= 0 && row < game.getBoardSize()) {
                doClickSquare(row, col);
            }
        }
    }
    public void mouseReleased(MouseEvent evt) { }
    public void mouseClicked(MouseEvent evt) { }
    public void mouseEntered(MouseEvent evt) { }
    public void mouseExited(MouseEvent evt) { }
}