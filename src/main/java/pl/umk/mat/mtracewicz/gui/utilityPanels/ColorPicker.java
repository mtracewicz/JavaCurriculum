package pl.umk.mat.mtracewicz.gui.utilityPanels;

import pl.umk.mat.mtracewicz.gui.AgendaPanel;

import javax.swing.*;
import java.awt.*;

public class ColorPicker extends AgendaPanel {
    private Color color;
    private JButton red;
    private JButton orange;
    private JButton yellow;
    private JButton green;
    private JButton blue;

    public static Color colorPopUp(){
        Color colorToReturn = null;
        ColorPicker cp = new ColorPicker(4);
        int okCxl = JOptionPane.showConfirmDialog(null, cp, "Enter Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if(okCxl != JOptionPane.CANCEL_OPTION){
            colorToReturn = cp.getColor();
        }
        return colorToReturn;
    }
    public static Color colorPopUp(Color defaultColor){
        Color colorToReturn = null;
        ColorPicker cp = new ColorPicker(4,defaultColor);
        int okCxl = JOptionPane.showConfirmDialog(null, cp, "Enter Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if(okCxl != JOptionPane.CANCEL_OPTION){
            colorToReturn = cp.getColor();
        }
        return colorToReturn;
    }
    public static ColorPicker createColorPicker(int numberOfColors){
        if(numberOfColors > 5 || numberOfColors <1 ){
            return new ColorPicker(5);
        }else {
            return new ColorPicker(numberOfColors);
        }
    }
    public static ColorPicker createColorPicker(int numberOfColors,Color defaultColor){
        if(numberOfColors > 5 || numberOfColors <1 ){
            return new ColorPicker(5,defaultColor);
        }else {
            return new ColorPicker(numberOfColors,defaultColor);
        }
    }
    private void setSelected(String colorToBorder){
        switch (colorToBorder){
            case "red":
                this.color = Color.RED;
                red.setBorder(BorderFactory.createLineBorder(Color.BLACK,3));
                orange.setBorder(BorderFactory.createLineBorder(Color.BLACK,0));
                yellow.setBorder(BorderFactory.createLineBorder(Color.BLACK,0));
                green.setBorder(BorderFactory.createLineBorder(Color.BLACK,0));
                blue.setBorder(BorderFactory.createLineBorder(Color.BLACK,0));
                break;
            case "orange":
                this.color = Color.ORANGE;
                red.setBorder(BorderFactory.createLineBorder(Color.BLACK,0));
                orange.setBorder(BorderFactory.createLineBorder(Color.BLACK,3));
                yellow.setBorder(BorderFactory.createLineBorder(Color.BLACK,0));
                green.setBorder(BorderFactory.createLineBorder(Color.BLACK,0));
                blue.setBorder(BorderFactory.createLineBorder(Color.BLACK,0));
                break;
            case "yellow":
                this.color = Color.YELLOW;
                red.setBorder(BorderFactory.createLineBorder(Color.BLACK,0));
                orange.setBorder(BorderFactory.createLineBorder(Color.BLACK,0));
                yellow.setBorder(BorderFactory.createLineBorder(Color.BLACK,3));
                green.setBorder(BorderFactory.createLineBorder(Color.BLACK,0));
                blue.setBorder(BorderFactory.createLineBorder(Color.BLACK,0));
                break;
            case "green":
                this.color = Color.GREEN;
                red.setBorder(BorderFactory.createLineBorder(Color.BLACK,0));
                orange.setBorder(BorderFactory.createLineBorder(Color.BLACK,0));
                yellow.setBorder(BorderFactory.createLineBorder(Color.BLACK,0));
                green.setBorder(BorderFactory.createLineBorder(Color.BLACK,3));
                blue.setBorder(BorderFactory.createLineBorder(Color.BLACK,0));
                break;
            case "blue":
                this.color = Color.BLUE;
                red.setBorder(BorderFactory.createLineBorder(Color.BLACK,0));
                orange.setBorder(BorderFactory.createLineBorder(Color.BLACK,0));
                yellow.setBorder(BorderFactory.createLineBorder(Color.BLACK,0));
                green.setBorder(BorderFactory.createLineBorder(Color.BLACK,0));
                blue.setBorder(BorderFactory.createLineBorder(Color.BLACK,3));
                break;
        }
    }
    public Color getColor(){
        return this.color;
    }
    public static Color getColor(String colorName){
        Color color = Color.RED;
        switch (colorName){
            case "red":
                color= Color.RED;
                break;
            case "orange":
                color = Color.ORANGE;
                break;
            case "yellow":
                color = Color.YELLOW;
                break;
            case "green":
                color = Color.GREEN;
                break;
            case "blue":
                color = Color.BLUE;
                break;
        }
        return color;
    }
    public void setColor(String colorName){
        switch (colorName){
            case "red":
                red.doClick();
                break;
            case "orange":
                orange.doClick();
                break;
            case "yellow":
                yellow.doClick();
                break;
            case "green":
                green.doClick();
                break;
            case "blue":
                blue.doClick();
                break;
        }
    }
    public static String getColorName(Color color){
        String colorName = "red";
        if(color == Color.RED){
            colorName = "red";
        }else if(color == Color.ORANGE){
            colorName = "orange";
        }else if(color == Color.YELLOW){
            colorName = "yellow";
        }else if(color == Color.GREEN){
            colorName = "green";
        }else if(color == Color.BLUE){
            colorName = "blue";
        }
        return colorName;
    }
    private ColorPicker(int numberOfColors) {
        this.setLayout(new GridLayout(1,numberOfColors));
        this.color = Color.RED;

        red = new JButton();
        orange = new JButton();
        yellow = new JButton();
        green = new JButton();
        blue = new JButton();

        red.setBackground(Color.red);
        red.addActionListener(e -> {
            this.setSelected("red");
        });
        orange.setBackground(Color.orange);
        orange.addActionListener(e -> {
            this.setSelected("orange");
        });
        yellow.setBackground(Color.yellow);
        yellow.addActionListener(e -> {
            this.setSelected("yellow");
        });
        green.setBackground(Color.green);
        green.addActionListener(e -> {
            this.setSelected("green");
        });
        blue.setBackground(Color.blue);
        blue.addActionListener(e -> {
            this.setSelected("blue");
        });

        this.setSelected("red");

        switch (numberOfColors) {
            case 5:
                this.add(blue);
            case 4:
                this.add(green);
            case 3:
                this.add(yellow);
            case 2:
                this.add(orange);
            case 1:
                this.add(red);
        }
    }
    private ColorPicker(int numberOfColors,Color defaultColor) {
        this(numberOfColors);
        this.color = defaultColor;
        String colorName = ColorPicker.getColorName(defaultColor);
        this.setSelected(colorName);
    }
}
