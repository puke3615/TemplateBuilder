package com.puke.tb.ui;

/**
 * @author zijiao
 * @version 17/2/9
 */
public class InputAccessorTest {

    public static void main(String[] args){
        InputAccessor.InputData data =
                new InputAccessor.InputData("ID", "NAME", "string", "DEFAULT_VALUE", "HELP");
        InputAccessor.getInputInfo(System.out::println, data);
    }

}
