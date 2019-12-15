package pl.umk.mat.mtracewicz.gui.contentPanels;

import pl.umk.mat.mtracewicz.gui.AgendaPanel;
import javax.swing.*;
import java.awt.*;

/**
 * Abstract class representing panels in Agenda app which
 * represent list of items
 */
public abstract class ContentPanel extends AgendaPanel {
    /**
     * Panel placed in header
     */
    protected JPanel header;
    /**
     *  Panel placed in the middle of panel
     */
    protected JPanel window;
    /**
     *  Panel placed in footer
     */
    protected JPanel footer;

    /**
     * Standard constructor for all panels inheriting from this class
     */
    protected ContentPanel(){
        this.setLayout(new BorderLayout());
        this.header = new JPanel();
        this.window = new JPanel(new CardLayout());
        this.footer = new JPanel();
        this.add(header,BorderLayout.NORTH);
        this.add(window,BorderLayout.CENTER);
        this.add(footer,BorderLayout.SOUTH);
    }

    /**
     * Method which should reset panel to standard state
     */
    public abstract void resetPanel();

    /**
     * Method to show panel with "name" in window card layout
     * @param name name of panel to show
     */
    protected void showPanel(String name){
        CardLayout cardLayout = (CardLayout)window.getLayout();
        cardLayout.show(window,name);
    }

    /**
     * Popup with message about text field being empty
     * @param textFieldName text field which is empty
     */
    protected void emptyTextFieldPopUp(String textFieldName){
        JOptionPane.showMessageDialog(window, "Please fill in "+textFieldName, "Inane error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Insert popup for string with message
     * @param whatToAsk message that will be displayed in popup
     * @return inserted string
     */
    protected String stringPopUp(String whatToAsk){
        String answer = JOptionPane.showInputDialog(window,whatToAsk, null);
        if(answer == null){
            return null;
        } else if(answer.isEmpty()){
            return this.stringPopUp("Can't be empty");
        }else {
            return answer;
        }
    }
}
