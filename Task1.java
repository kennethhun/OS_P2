package Project2;

import java.util.Scanner;


public class Task1 {
    public static void main(String[] args) {
        int pSDefault = 1024; // 1KB
        Scanner scan = new Scanner(System.in);

        System.out.println("Enter page size: ");
        int pageSize = scan.nextInt();

        System.out.println("Enter a virtual address: ");
        int virtualAddress = scan.nextInt();

        pageSize = pSDefault * pageSize;

        int pageNumber = virtualAddress / pageSize;
        int offset = virtualAddress % pageSize;

        System.out.println("The address " + virtualAddress + " contains: page number= " + pageNumber + " offset= " + offset);

        scan.close();

    }
/*
Enter page size: 
4
Enter a virtual address: 
19986
The address 19986 contains: page number= 4 offset= 3602
 */    
}
