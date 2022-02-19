package com.aktog.yusuf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;


public class GamePanel extends JPanel implements KeyListener {
    public static final int PANEL_WIDTH = Cell.SIZE * (5 + 1);
    public static final int PANEL_HEIGHT = Cell.SIZE * (5 + 1);
    public static final int BOARD_SIZE = 5;
    int i, j = 0;
    private final Cell[][] gameBoard;

    String chosenWord = chooseRandomWord();
    String guess;
    int round = 1;

    public GamePanel() {
        gameBoard = new Cell[BOARD_SIZE][BOARD_SIZE];
        initGameBoard();
        loadPreferences();
        this.addKeyListener(this);
    }

    public void initGameBoard() {
        int x = Cell.SIZE / 2;
        int y = Cell.SIZE / 2;
        int letterIndex = 65;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Cell cell = new Cell(x, y, Color.cyan, "");
                gameBoard[i][j] = cell;
                x += Cell.SIZE;
                letterIndex++;
            }
            y += Cell.SIZE;
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
        g.setColor(Color.red);

        Font font = new Font("", Font.BOLD, 50);

        for (Cell[] row : gameBoard) {
            for (Cell cell : row) {
                g.setColor(cell.getColor());
                g.setFont(font);

                g.drawRect(cell.getX(), cell.getY(), Cell.SIZE, Cell.SIZE);

                g.drawString(cell.getLetter(), cell.getX() + Cell.SIZE / 2 - font.getSize() / 3, cell.getY() + Cell.SIZE / 2 + font.getSize() / 3);
            }
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

        if (!isValidExtendedKeyKode(e.getExtendedKeyCode()))
            return;
        gameBoard[i][j].setLetter(String.valueOf(e.getKeyChar()).toUpperCase());
        repaint();
        j++;
        if (j == 5) {
            i++;
            j = 0;
            String guess = retrieveGuess(round);
            System.out.printf("guess: %s", guess);
            if (!isInWordList(guess)) {
                resetValues();
            } else {
                round++;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public boolean isValidExtendedKeyKode(int keyCode) {
        System.out.println(keyCode);
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
        if (keyCode == 81) // Q
            return false;
        if (keyCode == 88) // X
            return false;
        if (keyCode >= 65 && keyCode <= 90)
            return true;
        return false;

    }

    public boolean isInWordList(String word) {
        return FileHandler.readFrom("words.txt").contains(reFormatString(word));
    }

    public String retrieveGuess(int round) {
        StringBuilder guess = new StringBuilder();
        for (int k = 0; k < round; k++) {
            for (int l = 0; l < BOARD_SIZE; l++) {
                guess.append(gameBoard[k][l].getLetter());
            }
        }
        return guess.toString();
    }

    public void resetValues() {
        i = 0;
        j = 0;
    }

    public String reFormatString(String guess) { // Make the first Letter upper, the rest lower
        return guess.charAt(0) + guess.substring(1, guess.length()).toLowerCase();
    }
}