package pl.sda.workerdb;


import java.sql.Timestamp;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class ShopApp implements ShopMVC.View {
    ShopController shopController = new ShopController(this);

    @Override
    public void run(){

        System.out.println("Welcome to our shop! \n");
        while(true) {
            System.out.println("Select operation: ");
            System.out.println("1. Display product");
            System.out.println("2. Add new product");
            System.out.println("3. Delete product");
            System.out.println("4. Update product");
            System.out.println("5. List all products");
            System.out.println("0. Exit");

            Scanner in = new Scanner(System.in);
            int selection = in.nextInt();

            if(selection == 0){
                shopController.exit();
                break;
            }

            switch (selection){
                case 5: {
                    shopController.getAllProducts();
                    break;
                }
                case 1: {
                    int productId = entryProductId();
                    if (productId > 0) {
                        shopController.getProduct(productId);
                    }
                    break;
                }
                case 2:{
                    Product product = prepareProductAdd();
                    if(product != null) {
                        shopController.addProduct(product);
                    }
                    break;
                }
                case 3: {
                    int productId = entryProductId();
                    if (productId > 0) {
                        shopController.deleteProduct(productId);
                    }
                    break;
                }
                case 4: {
                    Product product = prepareProductUpdate();
                    if(product != null){
                        shopController.updateProduct(product);
                    }
                }

            }
        }
    }

    private int entryProductId(){
        Scanner in = new Scanner(System.in);
        System.out.println("Enter product id: ");
        int productId = 0;
        try{
            productId = in.nextInt();
        }catch(InputMismatchException e){
            System.out.println("Incorrect entry \n");
            return -99;

        }
        return productId;
    }

    private Product prepareProductAdd(){
        System.out.println("Adding new product.\n Enter required data: ");
        Scanner in = new Scanner(System.in);
        System.out.println("Product ID: ");
        int productId = in.nextInt();
        if(in.hasNextLine()){
            in.nextLine();
        }

        if(shopController.productExists(productId)){
            System.out.println("Product ID already exist. Cannot add another product with the same ID \n");
            return null;
        }
        return prepareProduct(productId);
    }

    private Product prepareProductUpdate(){
        System.out.println("Updating product.\n Enter required data: ");
        Scanner in = new Scanner(System.in);
        System.out.println("Product ID: ");
        int productId = in.nextInt();
        if(in.hasNextLine()){
            in.nextLine();
        }

        if(!shopController.productExists(productId)){
            System.out.println("Product ID does not exist. \n");
            return null;
        }
        return prepareProduct(productId);
    }


    private Product prepareProduct(int productId) {

        Scanner in = new Scanner(System.in);
        System.out.println("Product name:");
        String name = in.nextLine();

        System.out.println("Catalog number:");
        String catalogNumber = in.nextLine();

        System.out.println("Description:");
        String description = in.nextLine();

        Product product = new Product(productId,catalogNumber,name,description,new Timestamp(System.currentTimeMillis()));
        return product;
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
    public void displayProduct(Product product) {
        System.out.println("ID: " + product.getProductId()
                + " Name: " + product.getName()
                + " Catalog: " + product.getCatalogNumber()
                + " Description: " + product.getDescription());
        System.out.println();
    }

    @Override
    public void displayProducts(Map<Integer, Product> productMap) {
        for(Map.Entry<Integer,Product> entry : productMap.entrySet()){
            Product product = entry.getValue();
            System.out.println("ID: " + entry.getKey()
                    + " Name: " + product.getName()
                    + " Catalog: " + product.getCatalogNumber()
                    + " Description: " + product.getDescription());
        }
        System.out.println();
    }

    @Override
    public void displayProductAdded() {
        System.out.println("Product has been added. \n");
    }

    @Override
    public void displayProductIdNotExists() {
        System.out.println("Product ID does not exist. \n");
    }

    @Override
    public void displayProductDeleted(int productId) {
        System.out.println("Product: " + productId + " has been deleted. \n");
    }

    @Override
    public void diplayProductUpdated() {
        System.out.println("Product has been updated. \n");
    }
}
