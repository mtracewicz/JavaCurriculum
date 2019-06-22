package pl.umk.mat.mtracewicz.utility;

import java.awt.*;

public class ColorPriorityHandler {
    public static int getPriority(Color color){
        int returnedValue = -1;
        if(color == Color.RED){
            returnedValue = 4;
        }else if(color == Color.ORANGE){
            returnedValue = 3;
        }else if(color == Color.YELLOW){
            returnedValue = 2;
        }else if(color == Color.GREEN) {
            returnedValue = 1;
        }
        return returnedValue;
    }
    public static Color getPriorityColor(int i){
        Color color = Color.RED;
        if( i == 1){
            color = Color.GREEN;
        }else if(i == 2){
            color = Color.YELLOW;
        }else if(i == 3){
            color = Color.ORANGE;
        }
        return color;
    }
}
