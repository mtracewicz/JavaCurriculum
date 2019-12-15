package pl.umk.mat.mtracewicz.gui.contentPanels;

import pl.umk.mat.mtracewicz.entity.CompletedTodo;
import pl.umk.mat.mtracewicz.entity.Items;
import pl.umk.mat.mtracewicz.entity.Tasks;
import pl.umk.mat.mtracewicz.entity.Users;
import pl.umk.mat.mtracewicz.gui.Agenda;
import pl.umk.mat.mtracewicz.gui.occurrencePanels.Task;
import pl.umk.mat.mtracewicz.gui.utilityPanels.ColorPicker;
import pl.umk.mat.mtracewicz.gui.utilityPanels.DatePicker;
import pl.umk.mat.mtracewicz.utility.AgendaConstants;
import pl.umk.mat.mtracewicz.utility.ColorPriorityHandler;
import pl.umk.mat.mtracewicz.utility.DateHandler;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Class representing panel which shows to-do lists and tasks
 */
public class ToDoPane extends ContentPanel {

    /**
     *  Combobox to select to-do list
     */
    private JComboBox comboBox;
    /**
     *  Number of list for user
     */
    private int numberOfLists;
    /**
     *  Reference to main frame
     */
    private Agenda agenda;
    /**
     *  Items object representing currently selected list
     */
    private Items currentList;
    /**
     *  Helper variable if the task should be added to database or only added to panel
     */
    private boolean add;
    /**
     *  Map of names of to-do list and boolean value if the list with provided
     *  name had been set up
     */
    private Map<String,Boolean> setup;

    /**
     * Constructor of panel with provided reference to main frame
     * @param agenda reference to main frame
     */
    public ToDoPane(Agenda agenda){
        super();
        this.agenda = agenda;
        add = true;
        setup = new HashMap<>();
        comboBox = new JComboBox();
        this.numberOfLists = agenda.getConnector().getNumberOfItems(agenda.getUsername(),"list");
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

    /**
     *  Method which sets up header for to-do list
     */
    private void setUpHeader(){
        JButton newList = new JButton("Add new list +");
        newList.addActionListener(e -> this.addList());
        clearPanel(header);
        if(numberOfLists > 0) {
            footer.setVisible(true);
            header.setLayout(new GridLayout(1, 4));
            JButton editListButton= new JButton("Edit");
            editListButton.addActionListener(e->editList());
            JButton deleteListButton = new JButton("X");
            deleteListButton.addActionListener(e -> deleteList());
            comboBox.setSelectedIndex(0);
            currentList = agenda.getConnector().getItem(agenda.getUsername(),"list",comboBox.getSelectedItem().toString());
            comboBox.addActionListener(e -> {
                if (comboBox.getSelectedIndex() < numberOfLists) {
                    if(comboBox.getItemCount() > 0) {
                        currentList = agenda.getConnector().getItem(agenda.getUsername(), "list", comboBox.getSelectedItem().toString());
                    }
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
                    if(currentList != null) {
                        picker.setColor(currentList.getColor());
                    }
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
            footer.setVisible(false);
        }
        this.updateComponent(header);
    }

    /**
     *  Method which sets up footer, to make it available to add
     *  tasks to to-do lists
     */
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
            JPanel taskList = (JPanel)((JViewport)((JScrollPane)list.getComponent(1)).getComponent(0)).getComponent(0);
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

    /**
     *  Method to load to-do list from database
     */
    private void loadLists() {
        setUpFooter();
        if (numberOfLists > 0) {
            this.updateComponent(footer);
            for (Object l : agenda.getConnector().getItems(agenda.getUsername(), "list")) {
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
        } else{
            setUpHeader();
        }
    }

    /**
     *  Method to add list to combobox and database
     */
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
        agenda.getConnector().addItem(item,user);
        comboBox.addItem(name);
        currentList = agenda.getConnector().getItem(agenda.getUsername(),"list",name);
        JPanel panel = new JPanel();
        setUpList(panel);
        setup.put(name,true);
        comboBox.setSelectedItem(name);
        panel.setName(name);
        window.add(name, panel);
        this.showPanel(name);
        this.updateComponent(panel);
        comboBox.setSelectedIndex(numberOfLists-1);

        if(numberOfLists==1){
            setUpHeader();
        }
    }

    /**
     *  Method to delete list from combobox and database
     */
    private void deleteList(){
        new Thread(() -> agenda.getConnector().deleteItem(currentList)).start();
        numberOfLists--;
        for(Component c:window.getComponents()){
            if (c.getName().equals(comboBox.getSelectedItem().toString())){
                window.remove(c);
            }
        }
        if(comboBox.getItemCount() == 1){
            comboBox = new JComboBox();
        }else {
            comboBox.removeItemAt(comboBox.getSelectedIndex());
        }
        setUpHeader();
        this.updateComponent(header);
    }

    /**
     *  Method to edit list in combobox and database
     */
    private void editList(){
        String name =this.stringPopUp("New name:");
        Color newDefaultPriority =  ColorPicker.colorPopUp(ColorPicker.getColor(currentList.getColor()));
        if(name != null && !name.isEmpty()) {
            String oldName = getPanelForList().getName();
            JPanel panel = getPanelForList();
            currentList.setName(name);
            currentList.setColor(ColorPicker.getColorName(newDefaultPriority));
            agenda.getConnector().editItem(currentList);
            setup.remove(oldName);
            window.remove(panel);
            setup.put(name,true);
            panel.setName(name);
            comboBox.addItem(name);
            comboBox.setSelectedItem(name);
            window.add(name,panel);
            comboBox.removeItem(oldName);
            this.updateComponent(panel);
            this.showPanel(name);
            this.updateComponent(header);
        }
    }

    /**
     * Method which sets up provided JPanel as to-do list
     * @param panel JPanel to make a list from
     */
    private void setUpList(JPanel panel){
        panel.setLayout(new BorderLayout());
        JPanel holder = new JPanel();
        holder.setLayout(new BoxLayout(holder,BoxLayout.Y_AXIS));
        for(Object t: agenda.getConnector().getTasks(currentList)) {
            Tasks task = (Tasks)t;
            add = false;
            this.addTask(holder,task);
            add = true;
        }
        JScrollPane scrollPane = new JScrollPane(holder,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        JButton showCompleted = new JButton("Show completed");
        showCompleted.addActionListener(e -> {
            JPanel completedPanels = new JPanel();
            this.setUpSize(completedPanels,400,400,AgendaConstants.ALL);
            completedPanels.setLayout(new BoxLayout(completedPanels,BoxLayout.Y_AXIS));
            for(Object o: agenda.getConnector().getCTasks(currentList)){
                CompletedTodo t = (CompletedTodo) o;
                Task task = new Task(t);
                this.setUpSize(task,390,50,AgendaConstants.MAXIMUM);
                completedPanels.add(task);
            }
            JOptionPane.showMessageDialog(null,completedPanels);
        });
        panel.add(showCompleted,BorderLayout.NORTH);
        panel.add(scrollPane,BorderLayout.CENTER);
        this.updateComponent(panel);
    }

    /**
     * Method to add Task to panel and database
     * @param panel Panel to which task will be added
     * @param task  Task to be added
     */
    private void addTask(JPanel panel,Tasks task){
        Task taskPane = new Task(task, this.agenda.getConnector());
        this.setUpSize(taskPane,1920,50,AgendaConstants.ALL);
        panel.add(taskPane);
        if(add) {
            agenda.getConnector().addTask(task);
        }
        this.updateComponent(panel);
    }

    /**
     * Method which returns JPanel for currently used list
     * @return JPanel of current list
     */
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
