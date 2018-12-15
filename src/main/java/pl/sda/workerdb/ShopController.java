package pl.sda.workerdb;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ShopController implements ShopMVC.Controller {
    static final private String DB_URL = "jdbc:mysql://localhost/workerdb";
    static final private String USER = "worker";
    static final private String PASS = "workerpass";
    private ShopMVC.View view;
    private Connection connection;
    Map<Integer,Product> productMap = new HashMap<>();
    private Statement statement;

    public ShopController(ShopMVC.View view){
        this.view = view;
        connectDatabase();
        initProducts();
    }

    @Override
    public void getAllProducts() {
        if(productMap.size() == 0){
            view.noProductsAvailable();
        } else {
            view.displayProducts(productMap);
        }
    }

    @Override
    public void getProduct(int productId) {
        Product product = productMap.get(productId);
        view.displayProduct(product);
    }

    @Override
    public boolean productExists(int productId) {
        if(productMap.containsKey(productId)){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void addProduct(Product product) {
        int inserted = 0;
        try {
            inserted = statement.executeUpdate("INSERT INTO PRODUCTS VALUES" +
                    "("+product.getProductId() + ",'"+product.getCatalogNumber() + "','"+product.getName()+"','"+product.getDescription()+"', '" + product.getUpdatedate() + "');");
            productMap.put(product.getProductId(),product);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        view.displayProductAdded();

    }

    @Override
    public void deleteProduct(int productId) {
        if(!productMap.containsKey(productId)){
            view.displayProductIdNotExists();
            return;
        }

        try {
            int inserted = statement.executeUpdate("DELETE FROM Products WHERE Product_Id = " + productId);
            productMap.remove(productId);
            view.displayProductDeleted(productId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateProduct(Product product) {
        int inserted = 0;
        try {
            inserted = statement.executeUpdate("UPDATE Products " +
                    "SET product_id = " + product.getProductId() + "," +
                    "catalog_number = '" + product.getCatalogNumber() + "'," +
                    "name = '"+product.getName()+"'," +
                    "description = '"+product.getDescription()+"'," +
                    "updatedate = '" + product.getUpdatedate() + "';");
            productMap.put(product.getProductId(),product);
            view.diplayProductUpdated();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void connectDatabase() {
        registerMysqlDriver();
        connection = getSqlConnection();
    }

    private void registerMysqlDriver(){
        try {
            Driver driver = new com.mysql.jdbc.Driver();
            DriverManager.registerDriver(driver);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection getSqlConnection(){
        try {
            return DriverManager.getConnection(DB_URL,USER,PASS);

             /*Properties properties = new Properties();
        properties.setProperty()
        Connection connection = DriverManager.getConnection(DB_URL,properties);
        */

        } catch (SQLException e) {
            view.noConnection();
            e.printStackTrace();
        }
        return null;
    }

    public void initProducts() {
        try{
            statement = connection.createStatement();
            boolean productsExist = statement.execute("SELECT product_id FROM Products;");
            if(!productsExist){
                view.noProductsAvailable();
                return;
            }

            ResultSet resultSet = null;
            try {
                resultSet = statement.executeQuery("SELECT product_id,catalog_number, name, description, updatedate FROM Products;");

                while(resultSet.next()){
                    int productId = resultSet.getInt("product_id");
                    String catalogNumber = resultSet.getString("catalog_number");
                    String name = resultSet.getString("name");
                    String desc = resultSet.getString("description");
                    Timestamp updateDate = resultSet.getTimestamp("updatedate");

                    productMap.put(productId,new Product(productId,catalogNumber,name,desc, updateDate));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if(resultSet != null){
                    try {
                        resultSet.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void close(){
        if(statement != null){
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
