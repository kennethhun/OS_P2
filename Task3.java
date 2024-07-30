package Project2;

import java.util.Scanner;

public class Task3 {
    public static void main(String[] args) {
        int blockCylinders = 0;
        int blockSurfaceNum = 0;
        int blockSector = 0;
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter a logical block number: ");
        int logicalBlock = scan.nextInt();
        System.out.println("Enter HD number of cylinders: ");
        int cylinders = scan.nextInt();
        System.out.println("Enter HD number of tracks: ");
        int tracks = scan.nextInt();
        System.out.println("Enter HD number of sectors: ");
        int sectors = scan.nextInt();

        blockCylinders = logicalBlock / (tracks * sectors);
        blockSurfaceNum = (logicalBlock / sectors) % tracks;
        blockSector = logicalBlock % sectors;

        if(blockCylinders > cylinders) {
            System.out.println();
        }

        System.out.println("<" + blockCylinders + ", " + blockSurfaceNum + ", " + blockSector + ">");

        scan.close();
    }
}
