package com.aktog.yusuf;

import javax.swing.*;
import java.awt.*;


public class GamePanel extends JPanel {
    public static final int PANEL_WIDTH = Cell.SIZE * (5 + 1);
    public static final int PANEL_HEIGHT = Cell.SIZE * (5 + 1);

    public static final int BOARD_SIZE = 5;

    private final Cell[][] gameBoard;

    public GamePanel() {
        gameBoard = new Cell[BOARD_SIZE][BOARD_SIZE];
        initGameBoard();
        loadPreferences();
    }

    public void initGameBoard() {
        int x = Cell.SIZE / 2;
        int y = Cell.SIZE / 2;
        int letterIndex = 65;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Cell cell = new Cell(x, y, Color.cyan, (char) letterIndex);
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

        Font font = new Font("Ink Free", Font.BOLD, 50);

        for (Cell[] row : gameBoard) {
            for (Cell cell : row) {
                g.setColor(cell.getColor());
                g.setFont(font);

                g.drawRect(cell.getX(), cell.getY(), Cell.SIZE, Cell.SIZE);

                g.drawString(cell.getLetter().toString(), cell.getX() + Cell.SIZE / 2 - font.getSize()/3, cell.getY() + Cell.SIZE / 2 + font.getSize()/3);
            }
        }
    }
}