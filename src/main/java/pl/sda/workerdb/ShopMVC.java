package pl.sda.workerdb;

import java.util.Map;

public interface ShopMVC {
    interface View{

        void run();

        void noProductsAvailable();

        void displayProducts(Map<Integer, Product> productMap);

        void noConnection();

        void displayProduct(Product product);
    }

    interface Controller{

        void getAllProducts();

        void getProduct(int productId);
    }
}
