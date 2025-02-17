import controller.Hotel;
import exception.BookingNotFoundException;
import exception.DateValidationException;
import exception.UnavailableRoomException;
import exception.UserAlreadyRegisteredException;
import menues.MenuManager;
import menues.MenuReceptionist;
import model.*;
import util.Log;
import menues.MenuPassenger;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        Hotel OllivandersHotel = null;
        try {

            OllivandersHotel = new Hotel("Ollivanders", "Belgrano 3998", "4758996");
            OllivandersHotel.loadData();
        } catch (IOException e) {

            System.out.println("\n" + e.getMessage() + "\n");
        } catch (BookingNotFoundException e) {
            System.out.println("\n" + e.getMessage() + "\n");
        } catch (DateValidationException e) {
            System.out.println("\n" + e.getMessage() + "\n");
        } catch (UnavailableRoomException e) {

            System.out.println("\n" + e.getMessage() + "\n");
        }

        int z = 0, option;

        String dni, firstName, lastName, address, telephone, email, password, password2, origin;
        int age, genderOption = 0;
        Gender gender = null;

        while (z == 0) {

            System.out.println("\n===========================");
            System.out.println("Welcome to " + OllivandersHotel.getName() + " Hotel\n\nLocated in " + OllivandersHotel.getAddress() + "\nContact: " + OllivandersHotel.getTelephone());
            System.out.println("===========================\n");
            System.out.println("[1]. Register\n[2]. Log In\n");
            System.out.println("0. Exit");
            System.out.print("Option: ");
            System.out.flush();
            try {

                option = scan.nextInt();
                switch (option) {
                    case 1: {
                        System.out.println("\nPlease, complete the next form\n");
                        System.out.print("DNI: ");
                        dni = scan.next();
                        System.out.print("First Name: ");
                        firstName = scan.next();
                        System.out.print("Last Name: ");
                        lastName = scan.next();
                        System.out.print("Age: ");
                        age = scan.nextInt();

                        if (age >= 18 && age < 100) {

                            while (genderOption <= 0 || genderOption > 4) {
                                System.out.print("Gender\n¯¯¯¯¯¯¯\n[1]. Male\n[2]. Female\n[3]. Other\n[4]. N/A\n\nOption: ");
                                genderOption = scan.nextInt();
                                switch (genderOption) {
                                    case 1: {
                                        gender = Gender.MALE;
                                        break;
                                    }
                                    case 2: {
                                        gender = Gender.FEMALE;
                                        break;
                                    }
                                    case 3: {
                                        gender = Gender.OTHER;
                                        break;
                                    }
                                    case 4: {

                                        gender = Gender.NA;
                                        break;
                                    }
                                    default: {

                                        System.out.println("\nPlease select a valid option number\n");
                                        break;
                                    }
                                }
                            }
                            System.out.print("Address: ");
                            scan.nextLine();
                            address = scan.nextLine();
                            System.out.print("Telephone: ");
                            telephone = scan.next();
                            if (!OllivandersHotel.ifStringContainsLetters(telephone)) {

                                System.out.print("Email: ");
                                email = scan.next();
                                if (email.contains("@") && email.contains(".co")) {

                                    System.out.print("Password: ");
                                    password = scan.next();
                                    System.out.print("Enter your password again: ");
                                    password2 = scan.next();
                                    if (password.equals(password2)) {

                                        System.out.print("Origin (City): ");
                                        scan.nextLine();
                                        origin = scan.nextLine();


                                        User user = new Passenger(dni, firstName, lastName, age, gender, address, telephone, email, password, origin);

                                        try {

                                            OllivandersHotel.register(user);
                                            System.out.println("\nUser successfully registered. To operate, please, log in.\n");
                                        } catch (UserAlreadyRegisteredException e) {
                                            System.out.println("\n" + e.getMessage() + "\n");
                                        }
                                    } else {

                                        System.out.println("\nThe passwords do not match\n");
                                    }
                                } else {

                                    System.out.println("\nNot a valid email\n");
                                }
                            } else {

                                System.out.println("\nNot a valid phone number\n");
                            }
                        } else {

                            System.out.println("\nYou need to be between 18 and 99 years old\n");
                        }
                        break;
                    }
                    case 2: {
                        System.out.println("\nPlease, enter your DNI and Password\n");
                        System.out.print("DNI: ");
                        dni = scan.next();
                        System.out.print("Password: ");
                        password = scan.next();

                        User loggedUser = null;
                        try {
                            loggedUser = Log.logIn(dni, password, OllivandersHotel);
                        } catch (IOException e) {
                            System.out.println("\n" + e.getMessage() + "\n");
                        }
                        if (loggedUser != null) {

                            if (loggedUser instanceof Passenger) {

                                System.out.println("\nPassenger found. Welcome " + loggedUser.getFirstName() + " " + loggedUser.getLastName() + " to Hotel Ollivanders!\n");
                                MenuPassenger.menuPassenger(scan, OllivandersHotel, loggedUser);
                            } else if (loggedUser instanceof Receptionist) {

                                System.out.println("User found. Welcome " + loggedUser.getFirstName() + " " + loggedUser.getLastName() + "\n");
                                OllivandersHotel = MenuReceptionist.menuReceptionist(scan, OllivandersHotel, loggedUser);
                            } else {

                                System.out.println("User found. Welcome " + loggedUser.getFirstName() + " " + loggedUser.getLastName() + "\n");
                                OllivandersHotel = MenuManager.menuManager(scan, OllivandersHotel, loggedUser);
                            }
                        } else {

                            System.out.println("\nUser not found or user deactivated. Please register or try to log in again.");
                            System.out.println("If your account was deactivated, please reach one of our managers\n");
                        }
                        break;
                    }
                    case 0: {
                        System.out.println("\nSee you soon :)\n");
                        try {
                            OllivandersHotel.saveData();
                        } catch (IOException e) {
                            System.out.println("\n" + e.getMessage() + "\n");
                        }
                        z++;
                        break;
                    }
                    default: {
                        System.out.println("\nPlease, choose a valid option\n");
                        break;
                    }
                }
            } catch (InputMismatchException ime) {

                System.out.println("\n" + ime.getMessage() + "\n");
            }
        }
        scan.close();
    }
}
