package pl.sda.workerdb;

import java.util.Map;
import java.util.Scanner;

public class ShopApp implements ShopMVC.View {
    ShopController shopController = new ShopController(this);

    @Override
    public void run(){

        System.out.println("Welcome to our shop! \n");
        while(true) {
            System.out.println("Select operation: ");
            System.out.println("1. List all products");
            System.out.println("2. Add new product");
            System.out.println("0. Exit");

            Scanner in = new Scanner(System.in);
            int selection = in.nextInt();

            if(selection == 0){
                shopController.close();
                break;
            }

            switch (selection){
                case 1:
                    shopController.listProducts();
                    break;
                case 2:
                   // shopController.addProduct();
                    break;
            }
        }
    }

    @Override
    public void noConnection() {
        System.out.println("Unable to connect to database - exiting.");
        System.exit(-1);
    }

    @Override
    public void noProductsAvailable() {
        System.out.println("There are no products");
    }

    @Override
    public void displayProducts(Map<Integer, Product> productMap) {
        for(Map.Entry<Integer,Product> entry : productMap.entrySet()){
            Product product = entry.getValue();
            System.out.println("ID: " + entry.getKey()
                    + " Name: " + product.getName()
                    + " Calatog: " + product.getCatalogNumber()
                    + " Description: " + product.getDescription());
        }
        System.out.println();
    }
}
