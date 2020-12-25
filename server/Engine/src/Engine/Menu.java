package Engine;

import java.lang.reflect.Field;

public enum Menu {
    ShowCustomersDetails, ShowStoresDetails,ShowAllItems,MakeAnOrder,ShowOrdersHistory,UpdateProductInStore,SaveOrdersHistoryToFile,ReadOrdersHistoryFromFile,Exit;

    public String[] optionsToString() {
        String[] menuString = new String[this.getClass().getDeclaredFields().length];
        int i = 0;
        for(Field curField : this.getClass().getDeclaredFields()){
            menuString[i] = this.getClass().getDeclaredFields()[i].getName();
            i++;
        }
        return menuString;
    }
}