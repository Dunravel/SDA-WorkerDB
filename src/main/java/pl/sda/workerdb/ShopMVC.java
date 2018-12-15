package pl.sda.workerdb;

import java.util.Map;

public interface ShopMVC {
    interface View{

        void run();

        void noProductsAvailable();

        void displayProducts(Map<Integer, Product> productMap);

        void noConnection();

        void displayProduct(Product product);

        void displayProductAdded();

        void displayProductIdNotExists();

        void displayProductDeleted(int productId);

        void diplayProductUpdated();
    }

    interface Controller{

        void getAllProducts();

        void getProduct(int productId);

        boolean productExists(int productId);

        void addProduct(Product product);

        void deleteProduct(int productId);

        void updateProduct(Product product);
    }
}
