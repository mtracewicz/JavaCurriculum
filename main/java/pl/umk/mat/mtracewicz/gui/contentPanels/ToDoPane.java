package pl.umk.mat.mtracewicz.gui.contentPanels;

import pl.umk.mat.mtracewicz.entity.Items;
import pl.umk.mat.mtracewicz.entity.Tasks;
import pl.umk.mat.mtracewicz.entity.Users;
import pl.umk.mat.mtracewicz.gui.Agenda;
import pl.umk.mat.mtracewicz.gui.AgendaPanel;
import pl.umk.mat.mtracewicz.gui.occurrencePanels.Task;
import pl.umk.mat.mtracewicz.gui.utilityPanels.ColorPicker;
import pl.umk.mat.mtracewicz.gui.utilityPanels.DatePicker;
import pl.umk.mat.mtracewicz.utility.AgendaConstants;
import pl.umk.mat.mtracewicz.utility.ColorPriorityHandler;
import pl.umk.mat.mtracewicz.utility.DateHandler;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class ToDoPane extends ContentPanel {

    private JComboBox comboBox;
    private int numberOfLists;
    private Agenda agenda;
    private Items currentList;
    private boolean add;
    private Map<String,Boolean> setup;

    public ToDoPane(Agenda agenda){
        super();
        this.agenda = agenda;
        add = true;
        setup = new HashMap<>();
        comboBox = new JComboBox();
        this.numberOfLists = agenda.connector.getNumberOfItems(agenda.getUsername(),"list");
        this.loadLists();
    }
    @Override
    public void resetPanel(){
        if(numberOfLists > 0){
            comboBox.setSelectedIndex(0);
            this.showPanel(comboBox.getSelectedItem().toString());
        }else{
            this.clearPanel(window);
        }
    }
    private void setUpHeader(){
        JButton newList = new JButton("Add new list +");
        newList.addActionListener(e -> this.addList());
        clearPanel(header);
        if(numberOfLists > 0) {
            header.setLayout(new GridLayout(1, 4));
            JButton editListButton= new JButton("Edit");
            editListButton.addActionListener(e->editList());
            JButton deleteListButton = new JButton("X");
            deleteListButton.addActionListener(e -> deleteList());
            comboBox.setSelectedIndex(0);
            currentList = agenda.connector.getItem(agenda.getUsername(),"list",comboBox.getSelectedItem().toString());
            comboBox.addActionListener(e -> {
                if (comboBox.getSelectedIndex() <= numberOfLists) {
                    currentList = agenda.connector.getItem(agenda.getUsername(),"list",comboBox.getSelectedItem().toString());
                    if(!setup.get(comboBox.getSelectedItem().toString())){
                        for(Component c:window.getComponents()){
                            if(c.getName().equals(comboBox.getSelectedItem().toString())){
                                setUpList((JPanel)c);
                                setup.replace(c.getName(),true);
                                break;
                            }
                        }
                    }
                    ColorPicker picker = ((ColorPicker)this.footer.getComponent(7));
                    picker.setColor(currentList.getColor());
                    this.showPanel(comboBox.getSelectedItem().toString());
                }
            });
            header.add(newList);
            header.add(comboBox);
            header.add(editListButton);
            header.add(deleteListButton);
        }else {
            header.setLayout(new GridLayout(1, 1));
            header.add(newList);
        }
        this.updateComponent(header);
    }
    private void setUpFooter(){
        JButton newItem = new JButton();
        JTextField name = new JTextField();
        DatePicker date = new DatePicker();
        ColorPicker priority = ColorPicker.createColorPicker(4,Color.RED);
        newItem.setText("+");
        newItem.addActionListener(e -> {
            Tasks tasks = new Tasks();
            JPanel list = this.getPanelForList();
            boolean allFiled = true;
            if(!name.getText().isEmpty()) {
                tasks.setName(name.getText());
                name.setText(null);
            }else {
                this.emptyTextFieldPopUp("name");
                allFiled = false;
            }
            if(!date.getDate().isEmpty()) {
                tasks.setDeadline(DateHandler.getDateFromString(date.getDate()));
                date.resetDate();
            }else {
                this.emptyTextFieldPopUp("deadline");
                allFiled = false;
            }
            tasks.setPriority(ColorPriorityHandler.getPriority(priority.getColor()));
            tasks.setItems_id(currentList);
            tasks.setStatus(0);
            JPanel taskList = (JPanel)((JViewport)((JScrollPane)list.getComponent(0)).getComponent(0)).getComponent(0);
            if(allFiled) {
                this.addTask(taskList,tasks);
                priority.setColor(currentList.getColor());
            }
        });
        footer.setLayout(new GridLayout(2,4));
        footer.add(new JLabel("Add new"));
        footer.add(new JLabel("Name:"));
        footer.add(new JLabel("Date:"));
        footer.add(new JLabel("Priority:"));
        footer.add(newItem);
        footer.add(name);
        footer.add(date);
        footer.add(priority);
    }
    private void loadLists() {
        if (numberOfLists > 0) {
            setUpFooter();
            this.updateComponent(footer);
            for (Object l : agenda.connector.getItems(agenda.getUsername(), "list")) {
                Items list = (Items) l;
                comboBox.addItem(list.getName());
                JPanel panel = new JPanel();
                panel.setName(list.getName());
                setup.put(list.getName(), false);
                window.add(list.getName(), panel);
                currentList = list;
            }
            setUpHeader();
            for (Component c : window.getComponents()) {
                if (!setup.get(c.getName())){
                    setUpList((JPanel) c);
                    setup.replace(c.getName(),true);
                    break;
                }
            }
            comboBox.setSelectedIndex(numberOfLists-1);
        }
    }
    private void addList(){
        boolean unique = true;
        String name =this.stringPopUp("List name:");
        do {
            if(name == null){
                return;
            }
            if(!unique){
                unique = true;
                name = this.stringPopUp("List with this name already exists");
            }
            for (Component p : window.getComponents()) {
                if (p.getName().equals(name)) {
                    unique = false;
                }
            }
        }while (!unique);
        numberOfLists++;

        Users user = new Users();
        user.setEmail(agenda.getUsername());
        ColorPicker colorPicker = ColorPicker.createColorPicker(4);
        JOptionPane.showConfirmDialog(null, colorPicker, "Chose default priority", JOptionPane.OK_CANCEL_OPTION);
        Items item = new Items(name,ColorPicker.getColorName(colorPicker.getColor()),"list",user);
        agenda.connector.addItem(item,user);
        comboBox.addItem(name);
        currentList = agenda.connector.getItem(agenda.getUsername(),"list",name);
        JPanel panel = new JPanel();
        setUpList(panel);
        setup.put(name,true);
        comboBox.setSelectedItem(name);
        panel.setName(name);
        window.add(name, panel);
        this.showPanel(name);
        this.updateComponent(panel);
        comboBox.setSelectedIndex(numberOfLists-1);
    }
    private void deleteList(){
        numberOfLists--;
        new Thread(() -> agenda.connector.deleteItem(currentList)).start();
        for(Component c:window.getComponents()){
            if (c.getName().equals(comboBox.getSelectedItem().toString())){
                window.remove(c);
            }
        }
        comboBox.removeItemAt(comboBox.getSelectedIndex());
        setUpHeader();
        this.updateComponent(header);
    }
    private void editList(){
        String name =this.stringPopUp("New name:");
        Color newDefaultPriority =  ColorPicker.colorPopUp(ColorPicker.getColor(currentList.getColor()));
        if(name != null) {
            String oldName = getPanelForList().getName();
            JPanel panel = getPanelForList();
            currentList.setName(name);
            currentList.setColor(ColorPicker.getColorName(newDefaultPriority));
            agenda.connector.editItem(currentList);
            setup.remove(oldName);
            window.remove(panel);
            comboBox.removeItemAt(comboBox.getSelectedIndex());
            setup.put(name,true);
            panel.setName(name);
            comboBox.addItem(name);
            comboBox.setSelectedItem(name);
            window.add(name,panel);
            this.updateComponent(panel);
            this.showPanel(name);
            this.updateComponent(header);
        }
    }
    private void setUpList(JPanel panel){
        panel.setLayout(new BorderLayout());
        JPanel holder = new JPanel();

        holder.setLayout(new GridLayout(0,1));
        for(Object t: agenda.connector.getTasks(currentList)) {
            Tasks task = (Tasks)t;
            add = false;
            this.addTask(holder,task);
            add = true;
        }
        JScrollPane scrollPane = new JScrollPane(holder,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(scrollPane,BorderLayout.CENTER);
        this.updateComponent(panel);
    }
    private void addTask(JPanel panel,Tasks task){
        Task taskPane = new Task(task,this.agenda.connector);
        this.setUpSize(taskPane,panel.getWidth(),50,AgendaConstants.ALL);
        panel.add(taskPane);
        if(add) {
            agenda.connector.addTask(task);
        }
        this.updateComponent(panel);
    }
    private JPanel getPanelForList(){
        String listName = ((JComboBox)header.getComponent(1)).getSelectedItem().toString();
        for(Component c:window.getComponents()){
            if(c.getName().equals(listName)){
                return (JPanel)c;
            }
        }
        return null;
    }
}
