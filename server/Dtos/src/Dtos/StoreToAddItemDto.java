package Dtos;

import Engine.Store;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class StoreToAddItemDto {
    private final int id;
    private final String name;
    private TextField priceTextField;

    public StoreToAddItemDto(Store store){
        this.id = store.getId();
        this.name=store.getName();
        this.priceTextField =new TextField();
        priceTextField.setOnKeyTyped(this::onChangPrice);
    }

    public StoreToAddItemDto(int id, String name) {
        this.id = id;
        this.name = name;
        this.priceTextField =new TextField();
    }

    private void onChangPrice(KeyEvent charEvent) {
        try {
            Integer.parseInt(charEvent.getCharacter());
        }
        catch (Exception exp) {
            if (!charEvent.getCharacter().equals(".") || priceTextField.getText().contains(".")) {
                charEvent.consume();
            }
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public TextField getPriceTextField() {
        return priceTextField;
    }
}
