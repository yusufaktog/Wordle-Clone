package com.aktog.yusuf;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileHandler {

    public static ArrayList<String> readFrom(String fileName){
        ArrayList<String> lines = new ArrayList<>();
        try(Scanner reader = new Scanner(new BufferedReader(new FileReader(fileName)))){
            while((reader.hasNextLine())){
                lines.add((reader.nextLine()));
            }

        } catch (IOException ex) {
            Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lines;
    }

    public static void write(String fileName,List<String> lines){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))){
            for (String line : lines) {
                writer.write(line + "\n");
            }

        } catch (IOException ex) {
            Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void writeMessage(String fileName, String message){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(fileName,true))){
            writer.write(message);

        } catch (IOException ex) {
            Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}