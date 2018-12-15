package pl.sda.workerdb;

import java.util.Map;

public interface ShopMVC {
    interface View{

        void run();

        void noProductsAvailable();

        void displayProducts(Map<Integer, Product> productMap);

        void noConnection();
    }

    interface Controller{

        void listProducts();
        
    }
}
