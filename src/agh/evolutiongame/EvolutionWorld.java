package agh.evolutiongame;

import agh.evolutiongame.interfaces.Game;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.HashMap;

public class EvolutionWorld {
    public static void main(String[] args) {
        GameParameters params = null;
        try {
            params = ParametersParser.jsonToParams("parameters.json");
        } catch (IOException | ParseException ex) {
            ex.printStackTrace();
        }
        //ParametersParser.createJSON(params);
        if(params != null) {
            Game game = new EvolutionGame(params);
            game.start();
            game.end();
        }
    }
}
