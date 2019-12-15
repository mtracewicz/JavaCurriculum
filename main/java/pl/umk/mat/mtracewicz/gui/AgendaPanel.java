package pl.umk.mat.mtracewicz.gui;

import pl.umk.mat.mtracewicz.utility.AgendaConstants;

import javax.swing.*;
import java.awt.*;

public abstract class AgendaPanel extends JPanel {
    protected void setUpSize(Component component, int width, int height, int type){
        if(type == AgendaConstants.MINIMUM || type == AgendaConstants.ALL) {
            component.setMinimumSize(new Dimension(width, height));
        }
        if(type == AgendaConstants.MAXIMUM || type == AgendaConstants.ALL) {
            component.setMaximumSize(new Dimension(width, height));
        }
        component.setPreferredSize(new Dimension(width,height));
    }
    protected void updateComponent(Component component){
        component.revalidate();
        component.repaint();
    }
    protected void clearPanel(JPanel panel){
        if(panel.getComponentCount() > 0){
            for(Component c: panel.getComponents()){
                panel.remove(c);
            }
        }
    }
    protected String stringPopUp(String msg){
        String answer = JOptionPane.showInputDialog(msg);
        if(answer != null) {
            return answer;
        }
        return "";
    }
}
