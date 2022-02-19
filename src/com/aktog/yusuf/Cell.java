package com.aktog.yusuf;

import java.awt.*;
import java.util.Locale;

public class Cell {
    private int x;
    private int y;
    private Color color;
    private String letter;
    public static final int SIZE = 125;

    public Cell(int x, int y, Color color, String letter) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.letter = letter;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }
}