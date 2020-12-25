package Engine;

import java.lang.reflect.Field;

public enum StoreUpdateOptions {
    UpdateItemPrice, RemoveItemFromStore, AddItemToStore;

//    public String[] optionsToString() {
//        String[] options = new String[this.getClass().getDeclaredFields().length];
//        int i = 0;
//        for(Field curField : this.getClass().getDeclaredFields()){
//            options[i] = this.getClass().getDeclaredFields()[i].getName();
//            i++;
//        }
//        return options;
//    }
}
