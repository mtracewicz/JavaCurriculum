package pl.umk.mat.mtracewicz.gui;

import pl.umk.mat.mtracewicz.utility.AgendaConstants;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract class representing all panels which will be used in Agenda application
 */
public abstract class AgendaPanel extends JPanel {
    /**
     * Method to set size of provided component
     * @param component component which size will be set
     * @param width width of component
     * @param height height of component
     * @param type can be one of AgendaConstants, preferred size is always set,
     *             MINIMUM sets minimum size, MAXIMUM sets maximum size, ALL sets both
     */
    protected void setUpSize(Component component, int width, int height, int type){
        if(type == AgendaConstants.MINIMUM || type == AgendaConstants.ALL) {
            component.setMinimumSize(new Dimension(width, height));
        }
        if(type == AgendaConstants.MAXIMUM || type == AgendaConstants.ALL) {
            component.setMaximumSize(new Dimension(width, height));
        }
        component.setPreferredSize(new Dimension(width,height));
    }

    /**
     * This method revalidate and repaints Swing component
     * to update it looks dynamically
     * @param component component to update
     */
    protected void updateComponent(Component component){
        component.revalidate();
        component.repaint();
    }

    /**
     * This method checks if provided JPanel has any children
     * if so it removes all of them
     * @param panel Panel to be cleared
     */
    protected void clearPanel(JPanel panel){
        if(panel.getComponentCount() > 0){
            for(Component c: panel.getComponents()){
                panel.remove(c);
            }
        }
    }

    /**
     * Method which creates JOptionPane input popup with msg
     * as message and returns inserted string
     * @param msg Message to be displayed
     * @return String provided by user or empty string if window got closed not by clicking ok
     */
    protected String stringPopUp(String msg){
        String answer = JOptionPane.showInputDialog(msg);
        if(answer != null) {
            return answer;
        }
        return "";
    }
}
