package agh.evolutiongame;

import agh.evolutiongame.interfaces.Game;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class MainMenu {
    private Game currentGame;
    private final String configFile = "parameters.json";
    private static LinkedList<String> menuOptions = new LinkedList<>(Arrays.asList("1", "2", "3", "4", "5"));
    private boolean exitRequested = false;

    private MainMenu(){
        Scanner in = new Scanner(System.in);

        // Checks for configFile
        // If none is present creates default one
        if(!(new File(configFile).exists())){
            ParametersParser.createDefaultJson(configFile);
            System.out.println("No parameters.json file found, default one was created!");
            System.out.println();
        }

        // Takes input until user provides valid option
        String choice;
        do{
            printMainMenu();
            choice = in.next();
            ScreenCleaner.clear();
        }while(!menuOptions.contains(choice));

        //
        try {
            switch (choice) {
                case "1":
                    this.startGame();
                    break;
                case "2":
                    this.currentGame = new EvolutionGame(ParametersParser.jsonToParams(this.configFile));
                    new SwingAnimatedGame((EvolutionGame)this.currentGame);
                    break;
                case "3":
                    ScreenCleaner.clear();
                    this.showConfiguration();
                    break;
                case "4":
                    ParametersParser.createDefaultJson(this.configFile);
                    System.out.println("Default settings restored!");
                    System.out.println();
                    break;
                case "5":
                    exitRequested = true;
                    break;
            }
        } catch (IllegalStateException ignored) {
            // Exception caused by invalid parameters in configFile
            // To exit switch in main menu
        } catch (ParseException | IOException e) {
            // Exception cause by missing or corrupted configFile
            System.out.println(e.getMessage());
            ParametersParser.createDefaultJson(this.configFile);
            System.out.println("No " + this.configFile + " file found, default one was created!");
            System.out.println();
        }
    }

    private static void printMainMenu(){
        System.out.println("Choose your action by typing corresponding digit and pressing enter:");
        System.out.println("1. Start simulation");
        System.out.println("2. Start swing simulation");
        System.out.println("3. Show settings");
        System.out.println("4. Restore default settings");
        System.out.println("5. Exit");
    }

    private void startGame() throws IllegalStateException, IOException, ParseException {
        this.currentGame = new EvolutionGame(ParametersParser.jsonToParams(this.configFile));
        this.currentGame.start();
        this.currentGame.end();
    }

    private void showConfiguration() throws IllegalStateException, IOException, ParseException {
        GameParameters currentConfig = null;
        currentConfig = ParametersParser.jsonToParams(configFile);
        ScreenCleaner.clear();
        System.out.println("Current settings:");
        for(Field f : GameParameters.class.getFields()){
            try {
                System.out.println(f.getName() + " : " + f.get(currentConfig));
            } catch (IllegalAccessException ignored) {}
        }
        System.out.println();
    }

    public static void main(String[] args) {
        MainMenu menu;
        do {
            menu = new MainMenu();
        } while(!menu.exitRequested);
    }
}
