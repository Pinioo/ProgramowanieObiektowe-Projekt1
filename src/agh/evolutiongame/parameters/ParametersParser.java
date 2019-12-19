package agh.evolutiongame.parameters;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.naming.directory.NoSuchAttributeException;
import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;

public class ParametersParser {
    private static GameParameters defaultParameters = new GameParameters(
            100,
            30,
            0.4,
            200,
            1,
            50,
            80,
            1000,
            100
    );

    // Integer fields in GameParameters
    private static LinkedList<String> integerFields = new LinkedList<>(Arrays.asList(
            "width", "height", "startEnergy", "moveEnergy", "grassEnergy", "randomAnimals", "days", "delay"
    ));

    // Gets value from params.field
    private static Object getValueFromParams(GameParameters params, String field){
        try {
            return GameParameters.class.getField(field).get(params);
        } catch (IllegalAccessException | NoSuchFieldException ignored) {}
        return null;
    }

    // Sets value params.field = value
    private static void setValueInParams(GameParameters params, String field, Object value){
        try {
            GameParameters.class.getField(field).set(params, value);
        } catch (IllegalAccessException | NoSuchFieldException ignored) {}
    }

    // Create configName .json file with params GameParameters
    public static void createJson(GameParameters params, String configName) {
        JSONObject json = new JSONObject();
        json.put("jungleRatio", params.jungleRatio);

        for(String field : integerFields) {
            json.put(field, getValueFromParams(params, field));
        }

        try (FileWriter file = new FileWriter(configName)) {
            file.write(json.toJSONString());
        } catch (IOException ignored) {}
    }

    // Gets json's field as Number object
    private static Number loadNumberFromJson(JSONObject json, String field) throws NoSuchAttributeException {
        Object extracted = json.get(field);

        if(!(extracted instanceof Number))
            throw new NoSuchAttributeException(field + " is invalid or doesn't exist.");

        return (Number)extracted;
    }

    // Creates default parameters file as configName
    public static void createDefaultJson(String configName) {
        createJson(defaultParameters, configName);
    }

    // Parses json to GameParameters
    // If any of parameters are incorrect -> repairs file and throws IllegalStateException
    // If there is a problem with opening/parsing file -> throws IOException/ParseException
    public static GameParameters jsonToParams(String fileName) throws IOException, ParseException, IllegalStateException {
        // Reading json file to JSONObject
        // May throw IOException/ParseException
        Reader file = new FileReader(fileName);
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(file);
        file.close();

        // Flag which indicates if json file has correct parameters
        boolean allParametersCorrect = true;

        // Loading default parameters
        // Copying defaultParameters
        // If loaded parameter is valid -> save it to config object
        GameParameters config = new GameParameters(defaultParameters);

        long value;

        // Looping through all integer fields in GameParameters to check them
        for(String field : integerFields){
            try {
                value = loadNumberFromJson(json, field).longValue();
                // Every value must be positive
                if (value <= 0) {
                    throw new IllegalArgumentException(field + " must be positive.");
                }
                // If it is -> load it to config object
                else {
                    setValueInParams(config, field, value);
                }
            } catch (IllegalArgumentException | NoSuchAttributeException e){
                // If try block generated exception -> default value is loaded
                allParametersCorrect = false;
                System.out.println(e.getMessage());
                System.out.println(
                        field + " value will be replaced by default value (" + getValueFromParams(defaultParameters, field).toString() + ")"
                );
                System.out.println();
            }
        }
        try {
            double jungleRatio = loadNumberFromJson(json, "jungleRatio").doubleValue();
            if (jungleRatio < 0 || jungleRatio > 1)
                throw new IllegalArgumentException("Jungle ratio must be a value between 0 and 1!");
            config.jungleRatio = jungleRatio;
        } catch (NoSuchAttributeException | IllegalArgumentException e) {
            // If try block generated exception -> default value is loaded
            allParametersCorrect = false;
            System.out.println(e.getMessage());
            System.out.println(
                    "JungleRatio value will be replaced by default value (" + defaultParameters.jungleRatio + ")"
            );
            System.out.println();
        }

        // Maximal number of animals is width*height
        if (config.randomAnimals > config.width * config.height){
            allParametersCorrect = false;
            System.out.println(
                    "Map cannot contain " + config.randomAnimals + " animals."
            );
            config.randomAnimals = (int) (0.1 * config.width * config.height);
            System.out.println(
                    "randomAnimals value will be replaced by 10% of map's capability (" + config.randomAnimals + ")"
            );
            System.out.println();
        }

        // Creating Json with correct parameters (nothing changes if all parameters were valid)
        createJson(config, fileName);

        if(!allParametersCorrect)
            // Exception to be caught by MainMenu if changes in parameters were made
            throw new IllegalStateException();

        return config;
    }
}
