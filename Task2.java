package Project2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Random;

public class Task2 {
    public static void main(String[] args) {

        ArrayList<Integer> userArray = new ArrayList<Integer>(); // an arraylist which can be used to dyanmically store txt file input
        Scanner scan = new Scanner(System.in);
        Random random = new Random();
        int diskHead = 0;
        int choice = 0;
        int direction = 0; // 0 = decreasing, 1 = increasing
        boolean valid = false;

        System.out.println("==========================================================");
        System.out.println("Task #2: DISK Scheduling Simulator");
        System.out.println("==========================================================");
        System.out.println("1) Program will generate a series of 1,000 cylinder requests");
        System.out.println("2) The program will read a series of cylinder requests from an input.txt file");

        while (valid == false) {
            try {
                choice = Integer.parseInt(scan.nextLine());
            } 
            catch (NumberFormatException e) {
                System.out.println("Input not an integer, terminating");
                System.exit(1);
            }
            if (choice == 1) {
                int[] genMatrix = new int[1000];
                for(int i = 0; i < genMatrix.length; i++) {
                    genMatrix[i] = (random.nextInt(4999)); // generates a number between 0 and 4999
                    //System.out.println(genMatrix[i]);
                }
                System.out.println("Enter the disk head location, an integer between 0-4999: ");
                diskHead = scan.nextInt();
                System.out.println("Enter the direction of the disk head movement, this is important for certain algorithms. 0 = decreasing, 1 = increasing");
                direction = scan.nextInt();
                System.out.println("FIFO:");
                FIFO(genMatrix, diskHead, direction);
                System.out.println("SSTF:");
                SSTF(genMatrix, diskHead, direction);
                System.out.println("SCAN:");
                SCAN(genMatrix, diskHead, direction);
                System.out.println("CSCAN:");
                CSCAN(genMatrix, diskHead, direction);
                valid = true;
            }
            else if (choice == 2) {
                try {
                    System.out.println("File contents must be organized as a single line of numbers from 0-4999 with spaces separating each number.");
                    System.out.println("Enter a file name");
                    String fName = scan.nextLine();
                    File file = new File(fName);
                    Scanner fileReading = new Scanner(file); // reads a file inputed by user
        
                    while(fileReading.hasNextLine()) {
                        while(fileReading.hasNextInt()) {
                            userArray.add(fileReading.nextInt());
                        }
                    }
                    fileReading.close();
                    int[] userMatrix = userArray.stream().mapToInt(i -> i).toArray();

                    System.out.println("Enter the disk head location, an integer between 0-4999: ");
                    diskHead = scan.nextInt();
                    System.out.println("Enter the direction of the disk head movement, this is important for certain algorithms. 0 = decreasing, 1 = increasing");
                    direction = scan.nextInt();
                    System.out.println("FIFO:");
                    FIFO(userMatrix, diskHead, direction);
                    System.out.println("SSTF:");
                    SSTF(userMatrix, diskHead, direction);
                    System.out.println("SCAN:");
                    SCAN(userMatrix, diskHead, direction);
                    System.out.println("CSCAN:");
                    CSCAN(userMatrix, diskHead, direction);
                    valid = true;
                    
                } catch (IOException e) {
                    //error message when file not found, exiting program
                    System.out.println("Error in file reading, terminating"); 
                    System.exit(1);
                }
            
            }
            else {
                System.out.println("Not a valid input");
            }
        }

        scan.close();
    }

    public static void FIFO(int[] Array, int head, int direction) {
        int directionChange = 0;
        int headMovement = 0;
        int currentDirection = direction; // if 0 decreasing, if 1 increasing
        int pastPosition = head;
        int distanceHolder = 0; // temp value to check for changes in direction

        for(int i = 0; i < Array.length; i++) {
            distanceHolder =  Array[i] - pastPosition;
            if (distanceHolder < 0 && currentDirection == 1) { //previously increasing but is now decreasing
                directionChange++;
                currentDirection = 0;
            }
            else if (distanceHolder > 0 && currentDirection == 0) { //previously decreasing but is now increasing
                directionChange++;
                currentDirection = 1;
            }
            headMovement += Math.abs(distanceHolder);
            pastPosition = Array[i];
        }
        System.out.println("Total head movement: " + headMovement);
        System.out.println("Total direction changes: " + directionChange);
    
    }

    public static void SSTF(int[] Array, int head, int direction) {
        ArrayList<Integer> usedArray = new ArrayList<Integer>(); //holds positions that have already been used
        int directionChange = 0; 
        int headMovement = 0;
        int currentDirection = direction; // if 0 decreasing, if 1 increasing
        int pastPosition = head;
        int placeholder = -1; // saves position within matrix for next value
        int distanceHolder = 100000000; // placeholder to find changes in direction

        for(int i = 0; i < Array.length; i++) {
            for(int j = 0; j < Array.length; j++) {
                if(usedArray.contains(j)) {
                    continue;
                }
                else if(distanceHolder > Math.abs(Array[j] - pastPosition)) {
                    distanceHolder = Array[j] - pastPosition;
                    placeholder = j;
                }
            }
            usedArray.add(placeholder);
            
            if (distanceHolder < 0 && currentDirection == 1) { //previously increasing but is now decreasing
                directionChange++;
                currentDirection = 0;
            }
            else if (distanceHolder > 0 && currentDirection == 0) { //previously decreasing but is now increasing
                directionChange++;
                currentDirection = 1;
            }
            headMovement += Math.abs(distanceHolder);
            pastPosition = Array[placeholder];
            distanceHolder = 100000000;
        }
        System.out.println("Total head movement: " + headMovement);
        System.out.println("Total direction changes: " + directionChange);
    
    }

    public static void SCAN(int[] Array, int head, int direction) {
        int directionChange = 0; 
        int headMovement = 0;
        int currentDirection = direction; //if 0 decreasing, if 1 increasing
        int pastPosition = head;
        int divider = Array.length - 1; // position that will be above the head
        int distanceHolder = 0; // placeholder for changes in direction

        Arrays.sort(Array, 0, Array.length); // sort array find where the head would be in the array
        for(int i = 0; i < Array.length; i++) {
            if(head < Array[i]) {
                divider = i;
                break;
            }
        }

        if(direction == 0) { // decreasing case
            if(divider != 0) {
                for(int i = (divider - 1); i >= 0; i--) {
                    distanceHolder =  Array[i] - pastPosition;
                    if (distanceHolder < 0 && currentDirection == 1) { //previously increasing but is now decreasing
                        directionChange++;
                        currentDirection = 0;
                    }
                    else if (distanceHolder > 0 && currentDirection == 0) { //previously decreasing but is now increasing
                        directionChange++;
                        currentDirection = 1;
                    }
                    headMovement += Math.abs(distanceHolder);
                    pastPosition = Array[i];
                }
            }

            // must touch end
            headMovement += Math.abs(0 - pastPosition);
            pastPosition = 0;

            for(int i = divider; i < Array.length; i++) {
                distanceHolder =  Array[i] - pastPosition;
                if (distanceHolder < 0 && currentDirection == 1) { //previously increasing but is now decreasing
                    directionChange++;
                    currentDirection = 0;
                }
                else if (distanceHolder > 0 && currentDirection == 0) { //previously decreasing but is now increasing
                    directionChange++;
                    currentDirection = 1;
                }
                headMovement += Math.abs(distanceHolder);
                pastPosition = Array[i];
            }
            
        }
        else { // increasing case
            if(divider != (Array.length - 1)) {
                for(int i = divider; i < Array.length; i++) {
                    distanceHolder =  Array[i] - pastPosition;
                    if (distanceHolder < 0 && currentDirection == 1) { //previously increasing but is now decreasing
                        directionChange++;
                        currentDirection = 0;
                    }
                    else if (distanceHolder > 0 && currentDirection == 0) { //previously decreasing but is now increasing
                        directionChange++;
                        currentDirection = 1;
                    }
                    headMovement += Math.abs(distanceHolder);
                    pastPosition = Array[i];
                }
            }

            // must touch end
            headMovement += Math.abs(4999 - pastPosition);
            pastPosition = 4999;
            
            for(int i = (divider - 1); i >= 0; i--) {
                distanceHolder =  Array[i] - pastPosition;
                if (distanceHolder < 0 && currentDirection == 1) { //previously increasing but is now decreasing
                    directionChange++;
                    currentDirection = 0;
                }
                else if (distanceHolder > 0 && currentDirection == 0) { //previously decreasing but is now increasing
                    directionChange++;
                    currentDirection = 1;
                }
                headMovement += Math.abs(distanceHolder);
                pastPosition = Array[i];
            }

        }
        System.out.println("Total head movement: " + headMovement);
        System.out.println("Total direction changes: " + directionChange);

    }

    public static void CSCAN(int[] Array, int head, int direction) {
        int directionChange = 0; 
        int headMovement = 0;
        int currentDirection = direction; // if 0 decreasing, if 1 increasing
        int pastPosition = head;
        int divider = Array.length - 1;
        int distanceHolder = 0;

        Arrays.sort(Array, 0, Array.length); // sort array find where the head would be in the array
        for(int i = 0; i < Array.length; i++) {
            if(head < Array[i]) {
                divider = i;
                break;
            }
        }

        if(direction == 0) { // decreasing case
            if(divider != 0) {
                for(int i = (divider - 1); i >= 0; i--) {
                    distanceHolder =  Array[i] - pastPosition;
                    if (distanceHolder < 0 && currentDirection == 1) { //previously increasing but is now decreasing
                        directionChange++;
                        currentDirection = 0;
                    }
                    else if (distanceHolder > 0 && currentDirection == 0) { //previously decreasing but is now increasing
                        directionChange++;
                        currentDirection = 1;
                    }
                    headMovement += Math.abs(distanceHolder);
                    pastPosition = Array[i];
                }
            }

            headMovement += Math.abs(0 - pastPosition);
            pastPosition = 0;
            headMovement += Math.abs(4999 - pastPosition);
            pastPosition = 4999;
            directionChange++;
            currentDirection = 1;

            for(int i = (Array.length - 1); i >= divider; i--) {
                distanceHolder =  Array[i] - pastPosition;
                if (distanceHolder < 0 && currentDirection == 1) { //previously increasing but is now decreasing
                    directionChange++;
                    currentDirection = 0;
                }
                else if (distanceHolder > 0 && currentDirection == 0) { //previously decreasing but is now increasing
                    directionChange++;
                    currentDirection = 1;
                }
                headMovement += Math.abs(distanceHolder);
                pastPosition = Array[i];
            }
            
        }
        else { // increasing case
            if(divider != (Array.length - 1)) {
                for(int i = divider; i < Array.length; i++) {
                    distanceHolder =  Array[i] - pastPosition;
                    if (distanceHolder < 0 && currentDirection == 1) { //previously increasing but is now decreasing
                        directionChange++;
                        currentDirection = 0;
                    }
                    else if (distanceHolder > 0 && currentDirection == 0) { //previously decreasing but is now increasing
                        directionChange++;
                        currentDirection = 1;
                    }
                    headMovement += Math.abs(distanceHolder);
                    pastPosition = Array[i];
                }
            }

            headMovement += Math.abs(4999 - pastPosition);
            pastPosition = 4999;
            headMovement += Math.abs(0 - pastPosition);
            pastPosition = 0;
            directionChange++;
            currentDirection = 0;
            
            for(int i = 0; i < divider; i++) {
                distanceHolder =  Array[i] - pastPosition;
                if (distanceHolder < 0 && currentDirection == 1) { //previously increasing but is now decreasing
                    directionChange++;
                    currentDirection = 0;
                }
                else if (distanceHolder > 0 && currentDirection == 0) { //previously decreasing but is now increasing
                    directionChange++;
                    currentDirection = 1;
                }
                headMovement += Math.abs(distanceHolder);
                pastPosition = Array[i];
            }

        }
        System.out.println("Total head movement: " + headMovement);
        System.out.println("Total direction changes: " + directionChange);

    }

}
