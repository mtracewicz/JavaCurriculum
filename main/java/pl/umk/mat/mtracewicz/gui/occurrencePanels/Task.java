package pl.umk.mat.mtracewicz.gui.occurrencePanels;

import pl.umk.mat.mtracewicz.entity.Tasks;
import pl.umk.mat.mtracewicz.gui.Agenda;
import pl.umk.mat.mtracewicz.gui.utilityPanels.ColorPicker;
import pl.umk.mat.mtracewicz.gui.utilityPanels.DatePicker;
import pl.umk.mat.mtracewicz.utility.ColorPriorityHandler;
import pl.umk.mat.mtracewicz.utility.DatabaseConnector;
import pl.umk.mat.mtracewicz.utility.DateHandler;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

public class Task extends OccurrencePanel {
    private Tasks tasks;
    private JCheckBox checkBox;
    private JLabel name;
    private JLabel deadline;
    private JButton editButton;
    private JButton deleteButton;
    private DatabaseConnector databaseConnector;

    public void editTask(){
        String newName;
        String newDeadline;
        Color newPriority;

        newName = super.stringPopUp("Enter new task name");
        if(!newName.equals("")){
            tasks.setName(newName);
            this.name.setText(newName);
        }
        newDeadline = DatePicker.datePopUp();
        if(newDeadline != null){
            tasks.setDeadline(DateHandler.getDateFromString(newDeadline));
            this.deadline.setText(newDeadline);
        }
        newPriority = ColorPicker.colorPopUp(ColorPriorityHandler.getPriorityColor(tasks.getPriority()));
        if(newPriority != null){
            tasks.setPriority(ColorPriorityHandler.getPriority(newPriority));
            this.setBackground(newPriority);
        }
        if(!newName.equals("") || newDeadline != null || newPriority != null){
            this.databaseConnector.editTask(this.tasks);
            this.updateComponent(this.getParent());
        }
    }
    public void deleteTask(){
        Container parent = this.getParent();
        this.getParent().remove(this);
        new Thread(() -> this.databaseConnector.deleteTask(tasks)).start();
        this.updateComponent(parent);
    }
    public Task(Tasks taskInfo,DatabaseConnector databaseConnector){
        super();
        this.tasks = taskInfo;
        this.databaseConnector = databaseConnector;
        this.checkBox = new JCheckBox();
        this.checkBox.setHorizontalAlignment(SwingConstants.CENTER);
        this.checkBox.addActionListener(e -> {
            tasks.setStatus(1);
            this.deleteTask();
        });
        this.add(checkBox);
        this.name = new JLabel(taskInfo.getName());
        this.add(name);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
        this.deadline = new JLabel(sdf.format(taskInfo.getDeadline()));
        this.add(deadline);
        this.editButton = new JButton("Edit");
        this.editButton.addActionListener(e -> this.editTask());
        this.add(editButton);
        this.deleteButton = new JButton("X");
        this.deleteButton.addActionListener(e -> this.deleteTask());
        this.setBackground(ColorPriorityHandler.getPriorityColor(taskInfo.getPriority()));
        this.add(deleteButton);
    }
}
