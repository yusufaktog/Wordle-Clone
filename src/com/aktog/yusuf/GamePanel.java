package com.aktog.yusuf;

import javax.swing.*;
import javax.swing.plaf.IconUIResource;
import javax.swing.plaf.basic.BasicIconFactory;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;


public class GamePanel extends JPanel implements KeyListener {

    public static final int SPACE_BETWEEN = 10; // space between cells
    public static final int BOARD_SIZE = 5; // 5x5 grid

    //Auto calculated frame width and frame length
    public static final int PANEL_WIDTH = Cell.SIZE * (5 + 1) + SPACE_BETWEEN * (BOARD_SIZE - 1);
    public static final int PANEL_HEIGHT = Cell.SIZE * (5 + 1) + SPACE_BETWEEN * (BOARD_SIZE - 1);

    private final Cell[][] gameBoard;
    int i, j = 0;

    String chosenWord;
    String guess ;
    Boolean backSpaceUsed = false;
    int round = 1;

    public GamePanel() {
        gameBoard = new Cell[BOARD_SIZE][BOARD_SIZE];
        initGameBoard();
        loadPreferences();
        this.addKeyListener(this);
        chosenWord = chooseRandomWord();
        System.out.println(chosenWord);

    }

    public void initGameBoard() {
        int x = Cell.SIZE / 2;
        int y = Cell.SIZE / 2;

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Cell cell = new Cell(x, y, Color.gray, "");
                gameBoard[i][j] = cell;
                x += Cell.SIZE + 5;

            }
            y += Cell.SIZE + 5;
            x = Cell.SIZE / 2;
        }
    }

    final void loadPreferences() {
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.setLayout(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Font font = new Font("", Font.BOLD, 50);
        int rowCount = 1;
        for (Cell[] row : gameBoard) {
            for (Cell cell : row) {
                g.setColor(cell.getColor());
                g.setFont(font);

                if (rowCount < round) {
                    g.fillRect(cell.getX(), cell.getY(), Cell.SIZE, Cell.SIZE);
                } else {
                    g.drawRect(cell.getX(), cell.getY(), Cell.SIZE, Cell.SIZE);
                }

                g.setColor(new Color(200, 200, 200));
                g.drawString(cell.getLetter(),
                        cell.getX() + Cell.SIZE / 2 - font.getSize() / 3,
                        cell.getY() + Cell.SIZE / 2 + font.getSize() / 3);
            }
            rowCount++;
        }
        if (j == 0 && i != 0 && !backSpaceUsed && !isInWordList(guess)) {

            g.setColor(Color.red);
            g.setFont(font.deriveFont(Font.BOLD, 25));
            g.drawString("NOT IN THE WORD LIST: " + guess, PANEL_WIDTH / 2 - font.getSize() * 4, PANEL_HEIGHT - 40);

        }
    }

    public String chooseRandomWord() {
        ArrayList<String> words = FileHandler.readFrom("words.txt");
        int randomIndex = ThreadLocalRandom.current().nextInt(words.size());
        return words.get(randomIndex);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == 8) {
            if(j == 0)
                return;
            backSpaceUsed = true;
            deleteLastCell();
            repaint();
            return;
        }

        if (!isValidExtendedKeyKode(e.getExtendedKeyCode()))
            return;

        gameBoard[i][j].setLetter(String.valueOf(e.getKeyChar()).toUpperCase());
        repaint();
        j++;
        // End of the round, automatically check the guess and start to a new round
        if (j == 5) {
            i++;
            j = 0;
            guess = retrieveGuess();
            System.out.printf("guess: %s\n", guess);

            if (!isInWordList(guess)) {
                rollBackRound();
            } else {
                checkGuess();
                round++;
            }
            backSpaceUsed = false;
            if (gameOver()) {
                switch (showOptions()) {
                    case 0:
                        newGame();
                        break;
                    case 1:
                        System.exit(0);
                        break;

                }

            }
        }
    }

    public void newGame() {
        chosenWord = chooseRandomWord();
        System.out.println(chosenWord);
        guess = "";
        i = 0;
        j = 0;
        round = 1;
        backSpaceUsed = false;
        initGameBoard();
        repaint();
    }

    public int showOptions() {
        return JOptionPane.showOptionDialog(
                this,
                "Game Over!",
                "Title",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                BasicIconFactory.createEmptyFrameIcon(),
                new String[]{"New Game", "Exit"},
                0);
    }


    @Override
    public void keyReleased(KeyEvent e) {

    }

    public boolean isValidExtendedKeyKode(int keyCode) {
        // check for special turkish characters
        if (keyCode == 16777503) // ğ
            return true;
        if (keyCode == 16777468) // ü
            return true;
        if (keyCode == 16777567) // ş
            return true;
        if (keyCode == 16777415) // ç
            return true;
        if (keyCode == 16777430) // ö
            return true;
        if (keyCode == 16777521) // ı
            return true;
        //check for non-turkish characters
        if (keyCode == 81) // Q
            return false;
        if (keyCode == 88) // X
            return false;
        // A-Z
        return keyCode >= 65 && keyCode <= 90;

    }

    public void deleteLastCell() {
        gameBoard[i][j - 1].setLetter("");
        j--;
    }

    public boolean isInWordList(String word) {
        return FileHandler.readFrom("words.txt").contains(reFormatString(word));
    }

    public String retrieveGuess() {
        StringBuilder guess = new StringBuilder();
        for (int l = 0; l < BOARD_SIZE; l++) {
            guess.append(gameBoard[round - 1][l].getLetter());
        }

        return guess.toString();
    }

    public void rollBackRound() {
        for (int k = BOARD_SIZE - 1; k >= 0; k--) {
            gameBoard[i - 1][k].setLetter("");
        }
        i--;
    }

    // Make the first Letter upper, the rest lower.
    public String reFormatString(String guess) {
        return guess.charAt(0) + guess.substring(1).toLowerCase();
    }

    public void checkGuess() {
        Status status;
        for (int k = 0; k < BOARD_SIZE; k++) {
            status = checkCharacter(guess.charAt(k), k);
            adjustCellColor(status, round - 1, k);
            repaint();
        }
    }

    public Status checkCharacter(Character ch, Integer index) {

        // convert everything to uppercase to do a correct comparison
        chosenWord = chosenWord.toUpperCase();
        ch = Character.toUpperCase(ch);

        if (chosenWord.charAt(index) == ch) {

            return Status.SUCCESS_GUESS;
        } else if (chosenWord.charAt(index) != ch && chosenWord.contains(ch.toString())) {

            return Status.WRONG_PLACE;
        } else {

            return Status.FAIL;
        }
    }

    public void adjustCellColor(Status status, int i, int j) {
        switch (status) {
            case SUCCESS_GUESS:
                gameBoard[i][j].setColor(Color.green);
                break;
            case WRONG_PLACE:
                gameBoard[i][j].setColor(Color.yellow);
                break;
            case FAIL:
                gameBoard[i][j].setColor(Color.gray);
                break;
        }
    }

    public boolean gameOver() {
        return guess.equals(chosenWord) || round == 6;
    }
}