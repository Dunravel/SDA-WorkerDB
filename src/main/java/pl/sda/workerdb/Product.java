package pl.sda.workerdb;

import java.sql.Timestamp;

public class Product {
    private int productId;
    private String catalogNumber;
    private String name;
    private String description;
    private Timestamp updatedate;

    Product(int productId, String catalogNumber, String name, String description, Timestamp updatedate) {
        this.productId = productId;
        this.catalogNumber = catalogNumber;
        this.name = name;
        this.description = description;
        this.updatedate = updatedate;
    }

    public Timestamp getUpdatedate() {
        return updatedate;
    }

    int getProductId() {
        return productId;
    }

    void setProductId(int productId) {
        this.productId = productId;
    }

    String getCatalogNumber() {
        return catalogNumber;
    }

    void setCatalogNumber(String catalogNumber) {
        this.catalogNumber = catalogNumber;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

     String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }
}
