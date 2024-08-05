package cinema;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static int totalRows;
    private static int totalSeats;
    private static int seatsInRow;
    private static int purchasedTicketsCount;
    private static double purchasedTicketsPercentage;
    private static int currentIncome;
    private static int totalIncome;
    private static String[] seatsArray;
    private static final List<Integer> purchasedSeatsIndexes = new ArrayList<>();

    public static void main(String[] args) {

        initCinema();
        printMenu();
        int input = SCANNER.nextInt();
        while (input != 0) {
            menuAction(input, seatsArray, totalRows, totalSeats, seatsInRow);
            printMenu();
            input = SCANNER.nextInt();
        }

        SCANNER.close();
    }

    /* Create cinema by entering number of rows and seats in each row.
    *  Prompts user to enter values, and fills array of seats
    *  with 'S', marking and empty seat. */
    private static void initCinema() {
        System.out.print("Enter the number of rows :\n> ");
        totalRows = SCANNER.nextInt();
        System.out.print("Enter the number of seats in each row :\n> ");
        seatsInRow = SCANNER.nextInt();

        totalSeats = totalRows * seatsInRow;
        seatsArray = new String[totalSeats];
        Arrays.fill(seatsArray, "S");
        getTotalIncome(seatsArray, totalSeats);
    }

    /* Prints cinema seats row by row.
    *  Method iterates over array of seats.
    *   - If seat is free - prints 'S'
    *   - If seat is occupied - prints 'B' */
    private static void printCinemaSeats(String[] seatsArray, int rows, int seatsInRow) {
        System.out.println();
        System.out.println("Cinema:");
        int t = 0;
        for (int i = 0; i <= rows; i++) {
            if (i == 0) {
                System.out.print("  ");
            } else {
                System.out.print(i + " ");
            }

            for (int j = 1; j <= seatsInRow; j++) {
                if (i == 0) {
                    System.out.print(j + " ");
                } else {
                    System.out.print(seatsArray[t++] + " ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    /* Prompts user to enter row and seat number of interest.
    *  Input is valid if row value and seat value are present in seats array.
    *  If seat is free, calculates seat price and prints it.
    *  Marks seat in seat array as occupied (char 'B')
    *  If seat is occupied, prompts again. */
    private static void buyTicket(String[] seatsArray, int totalRows, int totalSeats, int seatsInARow) {
        System.out.println();
        System.out.print("Enter a row number:\n> ");
        int chosenRow = SCANNER.nextInt();
        System.out.print("Enter a seat number in that row:\n> ");
        int chosenSeat = SCANNER.nextInt();
        System.out.println();

        if (chosenRow > totalRows || chosenRow < 0 || chosenSeat > seatsInARow || chosenSeat < 1) {
            System.out.println("Wrong input!");
            buyTicket(seatsArray, totalRows, totalSeats, seatsInARow);
            return;
        }

        int seatNo = seatsInARow * (chosenRow - 1) + chosenSeat;
        int seatIndex = seatNo - 1;

        if (isSeatFree(seatIndex)) {
            purchasedTicketsCount++;
            purchasedTicketsPercentage = purchasedTicketsCount / (double) totalSeats * 100;
            purchasedSeatsIndexes.add(seatIndex);
            int seatPrize = checkPrize(seatNo, totalSeats, totalRows, seatsInARow);
            currentIncome += seatPrize;
            System.out.println("Ticket price: $" + seatPrize);
            seatsArray[seatIndex] = "B";
            printCinemaSeats(seatsArray, totalRows, seatsInARow);
        } else {
            System.out.println("That ticket has already been purchased!");
            buyTicket(seatsArray, totalRows, totalSeats, seatsInARow);
        }
    }


    private static int checkPrize(int chosenSeatNo, int seatsCount, int rows, int seatsInRow) {
        int price;
        if (seatsCount <= 60) {
            price = 10;
        } else {
            int halfRows = rows / 2;
            int firstHalf = halfRows * seatsInRow;
            if (chosenSeatNo > firstHalf) {
                price = 8;
            } else {
                price = 10;
            }
        }
        return price;
    }

    private static void getTotalIncome(String[] seatsArray, int totalSeats) {
        for (int i = 0; i < seatsArray.length; i++) {
            totalIncome += checkPrize(i + 1, totalSeats, totalRows, seatsInRow);
        }
    }

    /* If seat index is present in purchasedSeat list, returns false */
    private static boolean isSeatFree(int seatIndex) {
        for (var purchasedSeat : Main.purchasedSeatsIndexes) {
            if (purchasedSeat == seatIndex) {
                return false;
            }
        }
        return true;
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("1. Show the seats");
        System.out.println("2. Buy a ticket");
        System.out.println("3. Statistics");
        System.out.println("0. Exit");
        System.out.print("> ");
    }

    private static void menuAction(int input, String[] seatsArray, int totalRows, int totalSeats, int seatsInARow) {

        switch (input) {
            case 1:
                printCinemaSeats(seatsArray, totalRows, seatsInARow);
                break;
            case 2:
                buyTicket(seatsArray, totalRows, totalSeats, seatsInARow);
                break;
            case 3:
                showStatistics();
            case 0:
                return;
            default:
                System.out.println("Wrong input!");
        }
    }

    private static void showStatistics() {
        System.out.println();
        System.out.println("Number of purchased tickets: " + purchasedTicketsCount);
        System.out.printf("Percentage: %.2f%%%n", purchasedTicketsPercentage);
        System.out.printf("Current income: $%d%n", currentIncome);
        System.out.printf("Total expected income: $%d%n", totalIncome);
    }
}