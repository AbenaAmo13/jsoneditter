package com.company;

import com.google.common.collect.Multimap;
import org.json.simple.JSONArray;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonObjects {

    private static ArrayList<String> getRules() {
        ArrayList<String> rulesList = new ArrayList<>();
        //Getting the rules for the files.
        JSONParser parser = new JSONParser();
        try {
            //Get the user to input the file path of the json file that has the rules for the k or v values.
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter the name of the file containing the json array of strings in the format of k:<regex>\" OR \"v:<regex> ");
            String pathOfJsonArrayFileInstruction = reader.readLine();
            //Read  the json file given from the path and parse as an object named object
            Object obj = parser.parse(new FileReader(pathOfJsonArrayFileInstruction));
            JSONArray instructionJsonArray = (JSONArray) obj;
            instructionJsonArray.forEach(object -> {
                //Place the instruction into the arraylist
                rulesList.add((String) object);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rulesList;

    }

    //This method is to get the JSON Objects from the people.json file or similar file
    // and create an arraylist of HashMaps containing the key value pair information
    public static void createKeyValuePair(ArrayList<String> instructions) {
        HashMap<String, String> jsonKeyValuePair = new HashMap<>();
        //HashMap<String, String> finalJsonKeyValuePair = new HashMap<>();
        ArrayList<HashMap> keyValuePairObjects = new ArrayList<>();
        ArrayList<HashMap> finalKeyValuePairObjects = new ArrayList<>();
        String pathOfJsonPeopleArrayFile="";
        //Get the JSON objects.
        try {
            //Get the file paths:
            boolean added = false;
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter the name of the file containing the json files with the information of people:  ");
            pathOfJsonPeopleArrayFile = reader.readLine();
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(pathOfJsonPeopleArrayFile));
            //System.out.println(obj);
            JSONArray peopleJsonArray = (JSONArray) obj;

            JSONObject currentObject = new JSONObject();
            String keyName="";
            String valueName="";

            for (Object person : peopleJsonArray) {
                currentObject = getJsonObjects((JSONObject) person);
                Set<String> keys = currentObject.keySet();
                for (String key : keys) {
                    keyName = key;
                    valueName = (String) currentObject.get(keyName);
                    System.out.println("Key is: " + keyName + " value is : " + valueName);
                    if (rulesOnObjects(instructions, keyName, valueName)) {
                        //Star the keyName and valueName and replace the current pair with the starred version
                        String valueNameStarred = valueName.replaceAll(".", "*");
                        jsonKeyValuePair.put(keyName, valueNameStarred);
                        //keyValuePairObjects.add(jsonKeyValuePair);
                        added = true;
                        System.out.println(valueNameStarred);
                    }else{
                        jsonKeyValuePair.put(keyName, valueName);
                    }
                    /*
                    currentObject.remove(keyName, valueName);
                    try{
                        FileWriter fileWriter = new FileWriter(pathOfJsonPeopleArrayFile);
                        fileWriter.write(currentObject.toJSONString());
                        fileWriter.flush();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                     */
                }
                if (added) {
                    finalKeyValuePairObjects.add(jsonKeyValuePair);
                    //System.out.println(finalKeyValuePairObjects);
                    added= false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(finalKeyValuePairObjects);

    }


    //A helper method to get the Json Objects from the files that contain the people infomration.
    private static JSONObject getJsonObjects(JSONObject person) {


        return person;


    }
    //This is the method that will remove all the JSON Objects in the file and replace it all with the JSON objects
    //which are all starred by the rules given in the array.
    public static void writeJSONObjects(ArrayList<HashMap> starredJsonObjects, String filename){
        //This first section is to remove all the JSON objects currently in the file.
        JSONObject parsedFileJsonObjects = new JSONObject();
        try {
            JSONArray fileJsonArray = (JSONArray) new JSONParser().parse(new FileReader(filename));

            for (Object person : fileJsonArray) {
                parsedFileJsonObjects = getJsonObjects((JSONObject) person);
                Set objectKeys = parsedFileJsonObjects.keySet();
                Iterator iterator = objectKeys.iterator();
                while(iterator.hasNext()){
                    String currentKey = (String) iterator.next();
                    iterator.remove();
                    parsedFileJsonObjects.remove(currentKey);
                }

            }



            try{
                FileWriter fileWriter = new FileWriter(filename);
                fileWriter.write(parsedFileJsonObjects.toJSONString());
                fileWriter.flush();
            }catch(Exception e){
                e.printStackTrace();


            }


            }catch(Exception e ){
            e.printStackTrace();


        }




    }

    //This is a method to confirm that the objects go along with the rules in the json array file.
    private static boolean rulesOnObjects(ArrayList<String> rulesInstruction, String pairKey, String pairValue) {
        boolean followsRules = false;
        String k_Value = "";
        String v_Value = "";
        boolean matchFound = false;
        //Get the instruction in the rules json files.(the "k" and "v" instructions)
        for (String instructions : rulesInstruction) {
            int BeforeColonIndex = instructions.indexOf(":") - 1;
            String instructionsLetter = Character.toString(instructions.charAt(BeforeColonIndex));
            System.out.println(instructionsLetter);

            switch (instructionsLetter) {
                case "k":
                    k_Value = instructions.substring(instructions.lastIndexOf(":") + 1);

                    break;
                case "v":
                    v_Value = instructions.substring(instructions.lastIndexOf(":") + 1);
                    //Check if the k_Value or the v_value  is equals to the keys of the Hashmap
                    Pattern pattern = Pattern.compile(v_Value, Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(pairValue);
                    matchFound = matcher.find();
                    if (matchFound) {
                        System.out.println(matcher.group());

                    }


            }

        }
        System.out.println(k_Value);
        System.out.println(v_Value);
        if (k_Value.equalsIgnoreCase(pairKey) || matchFound) {
            followsRules = true;
        }
        System.out.println(followsRules);

        return followsRules;
    }

    public static void main(String[] args) {
        // write your code here
        try {
            /*
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter the name of the file containing the json array of strings in the format of k:<regex>\" OR \"v:<regex> ");
            String pathOfJsonArrayFileInstruction = reader.readLine();
            System.out.println(getRules(pathOfJsonArrayFileInstruction));
             */
            createKeyValuePair(getRules());

        } catch (Exception e) {
            e.printStackTrace();

        }


    }
}
