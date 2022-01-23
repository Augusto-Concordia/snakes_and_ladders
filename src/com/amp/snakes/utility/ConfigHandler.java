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
    public static Square[][] getBoardConfig(String path, int boardSize) {
        if (path == null) path = "board.conf";

        Square[][] board = new Square[boardSize][boardSize];
        String rawConfig = loadConfig(path);

        if (rawConfig.isEmpty()) return null;

        Scanner rawConfigScanner = new Scanner(rawConfig);
        int index = 0;

        SquareType[] squareTypes = SquareType.values();

        while (rawConfigScanner.hasNextLine()) {
            SquareType squareType = squareTypes[rawConfigScanner.nextInt()];
            int linkedSquare = rawConfigScanner.nextInt();

            //Inspired from here: https://stackoverflow.com/questions/19320183/1d-array-to-2d-array-mapping
            board[index / boardSize][index % boardSize] = new Square(linkedSquare, squareType);
            index++;
        }
        return board;
    }

    private static String loadConfig(String path) {
        try {
            File configFile = new File(path);

            Scanner configReader = new Scanner(configFile);

            StringBuilder data = new StringBuilder();

            while (configReader.hasNextLine()) {
                String currLine = configReader.nextLine();
                //System.out.println(currLine);

                if (currLine.isEmpty() || currLine.charAt(0) == '#') continue; //don't read comments or empty lines

                if (configReader.hasNextLine())
                    data.append(currLine).append('\n');
                else
                    data.append(currLine);
            }

            configReader.close();

            return data.toString();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred when loading a config file at " + path + ".");
            e.printStackTrace();
            return "";
        }
    }
}
