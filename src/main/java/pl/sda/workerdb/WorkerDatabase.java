package pl.sda.workerdb;

import java.sql.*;
import java.util.Map;

public class WorkerDatabase {
    static final private String DB_URL = "jdbc:mysql://localhost/workerdb";
    static final private String USER = "worker";
    static final private String PASS = "workerpass";
    private Connection connection;
    private Statement statement;

    public void connectDatabase() {
        registerMysqlDriver();
        getSqlConnection();
        getSqlStatement();
    }

    private void registerMysqlDriver(){
        try {
            Driver driver = new com.mysql.jdbc.Driver();
            DriverManager.registerDriver(driver);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getSqlConnection(){
        try {
            connection = DriverManager.getConnection(DB_URL,USER,PASS);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getSqlStatement() {
        try {
            statement = connection.createStatement();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void closeConnection(){
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


    public int insertProduct(Product product) {
        int inserted = 0;
        try {
            inserted = statement.executeUpdate("INSERT INTO PRODUCTS VALUES" +
                    "("+product.getProductId() + ",'"+product.getCatalogNumber() + "','"+product.getName()+"','"+product.getDescription()+"', '" + product.getUpdatedate() + "');");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inserted;
    }

    public int deleteProduct(int productId) {
        int inserted = 0;
        try {
            inserted = statement.executeUpdate("DELETE FROM Products WHERE Product_Id = " + productId);
        } catch (SQLException e) {
            e.printStackTrace();
            inserted = -99;
        }

        return inserted;
    }

    public int updateProduct(Product product) {
        int inserted = 0;
        try {
            inserted = statement.executeUpdate("UPDATE Products " +
                    "SET product_id = " + product.getProductId() + "," +
                    "catalog_number = '" + product.getCatalogNumber() + "'," +
                    "name = '"+product.getName()+"'," +
                    "description = '"+product.getDescription()+"'," +
                    "updatedate = '" + product.getUpdatedate() + "';");
        } catch (SQLException e) {
            e.printStackTrace();
            inserted = -99;
        }
        return inserted;
    }

    public void syncDatabase(Map productMap, Timestamp currentUpdateDate){

        boolean databaseNotSynced = false;
        String sqlCommand;
        if (currentUpdateDate == null){
            sqlCommand = "SELECT product_id,catalog_number, name, description, updatedate FROM Products ";
        } else{
            sqlCommand = "SELECT product_id,catalog_number, name, description, updatedate FROM Products WHERE updatedate > '" + currentUpdateDate + "';";
        }

        try {
            databaseNotSynced = statement.execute(sqlCommand);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(databaseNotSynced){
            ResultSet resultSet = null;
            try {
                resultSet = statement.executeQuery(sqlCommand);


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
    }
}
