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
    //private Statement statement;

    public ShopController(ShopMVC.View view){
        this.view = view;
        connectDatabase();
        initProducts();
    }

    @Override
    public void listProducts() {
        if(productMap.size() == 0){
            view.noProductsAvailable();
        } else {
            view.displayProducts(productMap);
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
        Statement statement = null;
        try{
            statement = connection.createStatement();
            boolean productsExist = statement.execute("SELECT product_id FROM Products;");
            if(!productsExist){
                view.noProductsAvailable();
                return;
            }

            ResultSet resultSet = null;
            try {
                resultSet = statement.executeQuery("SELECT product_id,catalog_number, name, description FROM Products;");

                while(resultSet.next()){
                    int productId = resultSet.getInt("product_id");
                    String catalogNumber = resultSet.getString("catalog_number");
                    String name = resultSet.getString("name");
                    String desc = resultSet.getString("description");

                    productMap.put(productId,new Product(productId,catalogNumber,name,desc));
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
        }finally {
            if(statement != null){
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public void close(){
        if(connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
