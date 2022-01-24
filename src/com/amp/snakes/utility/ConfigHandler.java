// -----------------------------------------------------
// Assignment 1 - COMP 249
// Due Date: February 4th
// Question: Part I
// Written by: Augusto Mota Pinheiro (40208080)
//             Michaël Gugliandolo (40213419)
// -----------------------------------------------------

package com.amp.snakes.utility;

import com.amp.snakes.Square;
import com.amp.snakes.enums.SquareType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ConfigHandler {
    public static Square[][] getBoardConfig(String path) {
        if (path == null) path = "board.conf"; //default config path if null is given

        Square[][] board = null;
        int boardSize = -1;

        String rawConfig = loadConfig(path);
        if (rawConfig.isEmpty()) return null;
        Scanner rawConfigScanner = new Scanner(rawConfig);

        SquareType[] squareTypes = SquareType.values();
        int index = 0;
        boolean onFirst = true;

        while (rawConfigScanner.hasNextLine()) {
            //the first line should be the board size
            if (onFirst)
            {
                onFirst = false;
                boardSize = rawConfigScanner.nextInt();
                board = new Square[boardSize][boardSize];
                continue;
            }

            //if reading the boardSize failed, an error occurred
            if (boardSize == -1) return null;

            SquareType squareType = squareTypes[rawConfigScanner.nextInt()];
            int linkedSquare = rawConfigScanner.nextInt();
            //rawConfigScanner.nextLine();

            //Inspired from here: https://stackoverflow.com/questions/19320183/1d-array-to-2d-array-mapping
            int row = index / boardSize;

            //if it's an odd-numbered row, flip it to match a real board configuration
            board[row][row % 2 != 0 ? boardSize - 1 - index % boardSize : index % boardSize] = new Square(linkedSquare, index + 1, squareType);
            index++;
        }
        return board;
    }

    private static String loadConfig(String path) {
        try {
            File configFile = new File(path);

            Scanner configReader = new Scanner(configFile);

            StringBuilder data = new StringBuilder();

            boolean onFirst = true;

            while (configReader.hasNextLine()) {
                String currLine = configReader.nextLine();
                //System.out.println(currLine); //todo remove

                if (currLine.isEmpty() || currLine.charAt(0) == '#') continue; //don't read comments or empty lines

                if (onFirst) {
                    data.append(currLine);
                    onFirst = false;
                } else {
                    data.append('\n').append(currLine);
                }
            }

            configReader.close();

            return data.toString();
        } catch (FileNotFoundException e) {
            System.out.println("A FileNotFound error occurred when loading a config file at " + path + ".");
            e.printStackTrace();
            return "";
        } catch (Exception e) {
            System.out.println("An error occurred when loading a config file at " + path + ".");
            e.printStackTrace();
            return "";
        }
    }
}
