package pl.sda.workerdb;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ShopController implements ShopMVC.Controller {
    static final private String DB_URL = "jdbc:mysql://localhost/workerdb";
    static final private String USER = "worker";
    static final private String PASS = "workerpass";
    private ShopMVC.View view;
    private WorkerDatabase workerDb;
    Map<Integer,Product> productMap = new HashMap<>();
    private Timestamp updateDate = null;

    public ShopController(ShopMVC.View view){
        this.view = view;
        workerDb = new WorkerDatabase();
        workerDb.connectDatabase();
        //connectDatabase();
    }

    @Override
    public void getAllProducts() {
        syncDatabase();
        if(productMap.size() == 0){
            view.noProductsAvailable();
        } else {
            view.displayProducts(productMap);
        }
    }

    private void syncDatabase() {
        Timestamp currentTimestamp = updateDate;
       workerDb.syncDatabase(productMap,currentTimestamp);
       updateDate = new Timestamp(System.currentTimeMillis());
    }

    @Override
    public void getProduct(int productId) {
        syncDatabase();
        if(productMap.containsKey(productId)) {
            Product product = productMap.get(productId);
            view.displayProduct(product);
        } else {
            view.displayProductIdNotExists();
        }
    }

    @Override
    public boolean productExists(int productId) {
        syncDatabase();
        if(productMap.containsKey(productId)){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void addProduct(Product product) {

        int inserted = workerDb.insertProduct(product);
        if(inserted > 0) {
            productMap.put(product.getProductId(), product);
            view.displayProductAdded();
        }
    }

    @Override
    public void deleteProduct(int productId) {
        if(!productMap.containsKey(productId)){
            view.displayProductIdNotExists();
            return;
        }

        workerDb.deleteProduct(productId);
        productMap.remove(productId);
        view.displayProductDeleted(productId);

    }

    @Override
    public void updateProduct(Product product) {
       workerDb.updateProduct(product);
       productMap.put(product.getProductId(),product);
       view.diplayProductUpdated();
    }




    public void exit(){
        workerDb.closeConnection();
    }
}
