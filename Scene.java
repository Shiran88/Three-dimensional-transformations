

//package com.company;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.nio.file.FileSystemNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner; // Import the Scanner class to read text files

/**
 * This class represents a parser of scene files (files with .scn extension)
 */
public class Scene {
    private File scnFile;




    private ArrayList<Vertex> ListOfPoints;
    private ArrayList<int[]> listOfLines;



    public Scene(String scnFileName){
        this.scnFile = creatTheFile(scnFileName);

    }
    public File creatTheFile(String filename ){
        File myObj = null;
        try {
            myObj = new File(filename);
        } catch (FileSystemNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return myObj;
    }
    public ArrayList<Vertex> getListOfPoints(){
        return this.ListOfPoints;
    }
    public ArrayList<int[]> getListOfLines(){
        return this.listOfLines;
    }

    public void parseScnFile() {
        int count=-1;
        int num=0;
        boolean flag=false;
        try {
            Scanner myReader = new Scanner(this.scnFile);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if(count==-1){
                    num =  Integer.parseInt(data);
                    // System.out.println(num);
                    if(flag==true){
                        listOfLines = new ArrayList<>();
                    }
                    else {
                        ListOfPoints =  new ArrayList<>();
                    }
                    count++;
                }

                else{
                    Vertex onePoint;
                    String[] arrOfStr = data.split(" ");
                        if(flag==true){
                            int[]oneEdge={Integer.parseInt(arrOfStr[0]),Integer.parseInt(arrOfStr[1])};
                            listOfLines.add(oneEdge);}
                        else{
                            onePoint = new Vertex(Double.parseDouble(arrOfStr[0]), Double.parseDouble(arrOfStr[1]),
                                    Double.parseDouble(arrOfStr[2]));
                            ListOfPoints.add(onePoint);
                        }
                    count++;
                    if (count== num){
                        flag=true;
                        count=-1;
                    }
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }



}


