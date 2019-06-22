package pl.umk.mat.mtracewicz.gui.occurrencePanels;

import pl.umk.mat.mtracewicz.gui.AgendaPanel;

import java.awt.*;

public abstract class OccurrencePanel extends AgendaPanel {

    protected OccurrencePanel(){
        this.setLayout(new GridLayout(1,5));
    }
}
