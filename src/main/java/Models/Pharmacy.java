package Models;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Pharmacy {
    private final SimpleStringProperty numberOfRow;
    private final SimpleStringProperty nameOfProduct;
    private final SimpleIntegerProperty totalNumberOfProduct;
    private final SimpleIntegerProperty currentNumberOfProduct;
    private final SimpleFloatProperty priceOfProduct;
    private final SimpleFloatProperty sumOfProduct;
    private final SimpleStringProperty shelfLife;
    private final SimpleStringProperty manufacturerOfProduct;
    private final SimpleStringProperty sourceOfFound;

    public Pharmacy(String numberOfRow, String nameOfProduct, int totalNumberOfProduct, int currentNumberOfProduct,
                    float priceOfProduct, float sumOfProduct, String shelfLife, String manufacturerOfProduct,
                    String sourceOfFound) {
        this.numberOfRow = new SimpleStringProperty(numberOfRow);
        this.nameOfProduct = new SimpleStringProperty(nameOfProduct);
        this.totalNumberOfProduct = new SimpleIntegerProperty(totalNumberOfProduct);
        this.currentNumberOfProduct = new SimpleIntegerProperty(currentNumberOfProduct);
        this.priceOfProduct = new SimpleFloatProperty(priceOfProduct);
        this.sumOfProduct = new SimpleFloatProperty(sumOfProduct);
        this.shelfLife = new SimpleStringProperty(shelfLife);
        this.manufacturerOfProduct = new SimpleStringProperty(manufacturerOfProduct);
        this.sourceOfFound = new SimpleStringProperty(sourceOfFound);
    }

    //getters and setters for private properties
    public String getNumberOfRow() { return this.numberOfRow.get();}
    public void setNumberOfRow(String numberOfRow) { this.numberOfRow.set(numberOfRow);}

    public String getNameOfProduct() { return this.nameOfProduct.get();}
    public void setNameOfProduct(String nameOfProduct) { this.nameOfProduct.set(nameOfProduct);}

    public int getTotalNumberOfProduct() { return this.totalNumberOfProduct.get();}
    public void setTotalNumberOfProduct(int totalNumberOfProduct) {this.totalNumberOfProduct.set(totalNumberOfProduct);}

    public int getCurrentNumberOfProduct() { return this.currentNumberOfProduct.get();}
    public void setCurrentNumberOfProduct(int currentNumberOfProduct) { this.currentNumberOfProduct.set(currentNumberOfProduct);}

    public float getPriceOfProduct() { return this.priceOfProduct.get();}
    public void setPriceOfProduct(int priceOfProduct) {this.priceOfProduct.set(priceOfProduct);}

    public float getSumOfProduct() { return this.sumOfProduct.get();}
    public void setSumOfProduct(float sumOfProduct) { this.sumOfProduct.set(sumOfProduct);}

    public String getShelfLife() { return this.shelfLife.get();}
    public void setShelfLife(String shelfLife) { this.shelfLife.set(shelfLife);}

    public String getManufacturerOfProduct() { return this.manufacturerOfProduct.get();}
    public void setManufacturerOfProduct(String manufacturerOfProduct) { this.manufacturerOfProduct.set(manufacturerOfProduct);}

    public String getSourceOfFound() { return this.sourceOfFound.get();}
    public void setSourceOfFound(String sourceOfFound) { this.sourceOfFound.set(sourceOfFound);}
}
