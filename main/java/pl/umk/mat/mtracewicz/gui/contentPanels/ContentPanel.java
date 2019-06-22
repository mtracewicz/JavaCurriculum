package pl.umk.mat.mtracewicz.gui.contentPanels;

import pl.umk.mat.mtracewicz.gui.AgendaPanel;
import javax.swing.*;
import java.awt.*;

public abstract class ContentPanel extends AgendaPanel {
    protected JPanel header;
    protected JPanel window;
    protected JPanel footer;

    protected ContentPanel(){
        this.setLayout(new BorderLayout());
        this.header = new JPanel();
        this.window = new JPanel(new CardLayout());
        this.footer = new JPanel();
        this.add(header,BorderLayout.NORTH);
        this.add(window,BorderLayout.CENTER);
        this.add(footer,BorderLayout.SOUTH);
    }

    public abstract void resetPanel();

    protected void showPanel(String name){
        CardLayout cardLayout = (CardLayout)window.getLayout();
        cardLayout.show(window,name);
    }
    protected void emptyTextFieldPopUp(String textFieldName){
        JOptionPane.showMessageDialog(window, "Please fill in "+textFieldName, "Inane error", JOptionPane.ERROR_MESSAGE);
    }
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
