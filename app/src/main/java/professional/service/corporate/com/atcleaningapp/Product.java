package professional.service.corporate.com.atcleaningapp;

import java.math.BigDecimal;



public class Product {
     String id;
     String name;
     String description;
     String image;
     String sku;

    public Product(String s, String project_name, String description, String image, BigDecimal price, String sku, String payee) {
        this.id = s;
        this.name = project_name;
        this.description = description;
        this.image = image;
        this.price = price;
        this.sku = sku;
        this.payee = payee;

    }

    public String getAmount_to_be_released() {
        return amount_to_be_released;
    }

    public void setAmount_to_be_released(String amount_to_be_released) {
        this.amount_to_be_released = amount_to_be_released;
    }

    public String amount_to_be_released;

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    private String payee;
    private BigDecimal price;

    public String getSafe_amt() {
        return safe_amt;
    }

    public void setSafe_amt(String safe_amt) {
        this.safe_amt = safe_amt;
    }

    String safe_amt;

    public Product(String id, String project_name, String description, String safe_deposit_amt, String image, BigDecimal price, String sku, String payee) {
        this.id = id;
        this.name = project_name;
        this.description = description;
        this.image = image;
        this.price = price;
        this.sku = sku;
        this.payee = payee;
        this.safe_amt = safe_deposit_amt;
    }

    public Product(String id, String name, String description, String image,
                   BigDecimal price, String sku, String payee, String safe_amt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
        this.sku = sku;
        this.payee = payee;
        this.safe_amt = safe_amt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

}