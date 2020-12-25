package Dtos;

import Engine.PurchaseType;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class ItemInMenuDto {

    private String name;
    private int ID;
    private PurchaseType purchaseType;
    private StringProperty price;
    private TextField quantityTextField;
    private SimpleFloatProperty quantityValue;


    public ItemInMenuDto(ItemDto item) {
        this.name = item.getName();
        this.ID = item.getItemId();
        this.purchaseType = item.getPurchaseType();
        quantityTextField = new TextField();
        quantityTextField.setOnKeyTyped(this::onChangeInNewStore);
    }

    private void onChangeInNewStore(KeyEvent charEvent) {
        try {
            Integer.parseInt(charEvent.getCharacter());
        }
        catch (Exception exp) {
            if (!charEvent.getCharacter().equals(".") || quantityTextField.getText().contains(".")) {
                charEvent.consume();
            }
        }
    }

    public ItemInMenuDto(ItemDto item, String price) {
        this.name = item.getName();
        this.ID = item.getItemId();
        this.purchaseType = item.getPurchaseType();
        this.price = new SimpleStringProperty();
        this.price.set(price);
        quantityTextField = new TextField();
        quantityTextField.setOnKeyTyped(this::onChange);
        quantityValue = new SimpleFloatProperty();
        quantityTextField.disableProperty().bind(Bindings.equal("not being sold.",this.price));
    }

    private void onChange(KeyEvent charEvent) {
       try {
           Integer.parseInt(charEvent.getCharacter());
       }
       catch (Exception exp) {
           if (((!charEvent.getCharacter().equals(".") || quantityTextField.getText().contains(".")) && purchaseType == PurchaseType.Weight ) || purchaseType == PurchaseType.Quantity) {
               charEvent.consume();
           }
       }
    }


    public String getName() {
        return name;
    }

    public int getID() {
        return ID;
    }

    public PurchaseType getPurchaseType() {
        return purchaseType;
    }

    public String getPrice() {
        return price.get();
    }

    public TextField getQuantityTextField() {
        return quantityTextField;
    }


    public void setPrice(String price) {
        this.price.set(price);
    }
}
