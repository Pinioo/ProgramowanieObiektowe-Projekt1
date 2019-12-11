package agh.evolutiongame;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class ParametersParser {
    private static GameParameters defaultParameters = new GameParameters(
            100,
            30,
            0.2,
            200,
            1,
            50,
            80,
            1000
    );

    public static void createJson(GameParameters params){
        JSONObject json = new JSONObject();
        json.put("width", params.width);
        json.put("height", params.height);
        json.put("jungleRatio", params.jungleRatio);
        json.put("startEnergy", params.startEnergy);
        json.put("moveEnergy", params.moveEnergy);
        json.put("grassEnergy", params.grassEnergy);
        json.put("randomAnimals", params.randomAnimals);
        json.put("days", params.days);


        try (FileWriter file = new FileWriter("parameters.json")) {
            file.write(json.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Number loadNumberFromJson(JSONObject json, String field){
        return (Number)json.get(field);
    }

    public static void createDefalutJson(){
        createJson(defaultParameters);
    }

    public static GameParameters jsonToParams(String fileName) throws IOException, ParseException {
        Reader file = new FileReader(fileName);
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(file);

        return new GameParameters(
                ((Number)json.get("width")).intValue(),
                ((Number)json.get("height")).intValue(),
                ((Number)json.get("jungleRatio")).doubleValue(),
                ((Number)json.get("startEnergy")).intValue(),
                ((Number)json.get("moveEnergy")).intValue(),
                ((Number)json.get("grassEnergy")).intValue(),
                ((Number)json.get("randomAnimals")).intValue(),
                ((Number)json.get("days")).intValue()
        );
    }
}
