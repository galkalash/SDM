package Dtos;

import Engine.Menu;

import java.lang.reflect.Field;

public class OptionsDto {
    private String[] options;

    public OptionsDto(Field[] optionsEnum){
        options = new String[optionsEnum.length - 1];
        for(int i = 0;i<options.length;i++){
            options[i] = optionsEnum[i].getName();
        }
    }

    public String[] getOptions() {
        return options;
    }
}
