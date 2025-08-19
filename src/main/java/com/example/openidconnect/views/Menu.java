package com.example.openidconnect.views;

import com.example.openidconnect.controller.MainController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

public class Menu {
    public static void show(){
        MainController mainController = new  MainController();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;



        while (running) {
            System.out.println("=== Menu ===");
            System.out.println("1. Login con Google");
            System.out.println("2. Visualizza informazioni utente");
            System.out.println("3. Logout");
            System.out.println("0. Esci");
            System.out.print("Seleziona un'opzione: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    mainController.login();
                    break;
                case 2:
                    mainController.viewUserInfo();
                    break;
                case 3:
                    mainController.logout();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Opzione non valida!");
            }
        }

    }
}
