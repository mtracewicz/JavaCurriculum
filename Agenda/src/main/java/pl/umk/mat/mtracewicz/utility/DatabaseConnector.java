package pl.umk.mat.mtracewicz.utility;

import org.mindrot.jbcrypt.BCrypt;
import pl.umk.mat.mtracewicz.entity.*;

import javax.persistence.*;
import javax.swing.*;
import java.util.Calendar;
import java.util.List;

/**
 * Class which is responsible for connection with database
 */
public class DatabaseConnector {
    /**
     *  Allows to create managers
     *  to use in methods and connect to database
     */
    private EntityManagerFactory managerFactory;

    /**
     *  Constructor which creates EntityManagerFactory
     */
    public DatabaseConnector() {
        try {
            this.managerFactory = Persistence.createEntityManagerFactory("pl.umk.mat.mtracewicz");
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    /**
     * Method which validates if user who wants to create change knows his password
     * @param user user which will be checked
     * @return true  if user provided correct password, false otherwise
     */
    private boolean validateUser(Users user){
        boolean returnValue = false;
        JPasswordField pf = new JPasswordField();
        int okCxl = JOptionPane.showConfirmDialog(null, pf, "Enter Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (okCxl == JOptionPane.OK_OPTION) {
            user.setPassword(String.valueOf(pf.getPassword()));
            if (this.checkUserCredentials(user)) {
                returnValue = true;
            } else {
                JOptionPane.showMessageDialog(null, "Wrong password!", "Inane error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return returnValue;
    }

    /**
     * Method with checks if user and password match
     * @param user user to check
     * @return true if email and password are correct, false otherwise
     */
    public boolean checkUserCredentials(Users user){
        boolean returnValue = false;
        try {
            EntityManager manager = managerFactory.createEntityManager();
            Users u = manager.find(Users.class,user.getEmail());
            if(u != null){
                returnValue = BCrypt.checkpw(user.getPassword(), u.getPassword());
            }
            manager.close();
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
        return returnValue;
    }

    /**
     * Method to add user to database
     * @param user user to add
     * @return "Email already in use" - if email is taken, "success" when user got created
     */
    public String addUser(Users user){
        String returnValue="";
        try {
            EntityManager manager = managerFactory.createEntityManager();
            manager.getTransaction().begin();
            if(manager.find(Users.class,user.getEmail()) == null){
                manager.persist(user);
                manager.getTransaction().commit();
                returnValue ="succes";
            }else {
                returnValue = "Email already in use";
            }
            manager.close();
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
        return returnValue;
    }

    /**
     * Method to delete user from database
     * @param user user to delete
     */
    public void deleteUser(Users user){
        if(this.validateUser(user)) {
            try {
                EntityManager manager = managerFactory.createEntityManager();
                manager.getTransaction().begin();
                if (manager.find(Users.class, user.getEmail()) != null) {
                    for(Object o:this.getItems(user.getEmail())){
                        this.deleteItem((Items)o);
                    }
                    manager.remove(manager.contains(user) ? user : manager.merge(user));
                    manager.getTransaction().commit();
                }
                manager.close();
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
    }

    /**
     * Method to change user password
     * @param user user whose password will be changed
     * @param newPassword new password of user
     */
    public void changeUserPassword(Users user,char[] newPassword){
        if(this.validateUser(user)) {
            try {
                EntityManager manager = managerFactory.createEntityManager();
                manager.getTransaction().begin();
                Users u = manager.find(Users.class, user.getEmail());
                u.setPassword(BCrypt.hashpw(String.valueOf(newPassword), BCrypt.gensalt()));
                manager.getTransaction().commit();
                manager.close();
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
    }


    /**
     * Method which returns number of items of concrete type for certain user
     * @param email email of user to check
     * @param type type of items to look for
     * @return number of items of concrete type for certain user
     */
    public int getNumberOfItems(String email, String type){
        int returnedValue = 0;
        Users fromDatabase;
        try {
            EntityManager manager = managerFactory.createEntityManager();
            fromDatabase = manager.find(Users.class,email);
            returnedValue = manager.createQuery("FROM Items item WHERE item.email = :ownerEmail AND item.type = :searchingForType")
                    .setParameter("ownerEmail",fromDatabase)
                    .setParameter("searchingForType",type)
                    .getResultList().size();

            manager.close();
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
        return returnedValue;
    }

    /**
     * Method to get list of Items objects of certain type for user
     * @param email email of user to check
     * @param type type to look for
     * @return List of Object which stores all Items of matching type for user
     */
    public List getItems(String email, String type){
        List items = null;
        Users fromDatabase;
        try {
            EntityManager manager = managerFactory.createEntityManager();
            fromDatabase = manager.find(Users.class,email);
            items = manager.createQuery("FROM Items t WHERE t.email = :ownerEmail AND t.type = :typeToFind")
                    .setParameter("ownerEmail",fromDatabase)
                    .setParameter("typeToFind",type)
                    .getResultList();
            manager.close();
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
        return items;
    }

    /**
     * Method to get list of all Items objects  for user
     * @param email email of user to check
     * @return list of all Items objects  for user
     */
    private List getItems(String email){
        List items = null;
        Users fromDatabase;
        try {
            EntityManager manager = managerFactory.createEntityManager();
            fromDatabase = manager.find(Users.class,email);
            items = manager.createQuery("FROM Items t WHERE t.email = :ownerEmail")
                    .setParameter("ownerEmail",fromDatabase)
                    .getResultList();
            manager.close();
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
        return items;
    }

    /**
     * Method to get specific Items object from database
     * @param email email of user to search
     * @param type type of item to look for
     * @param name name of item to look for
     * @return Items object if exists or null if not
     */
    public Items getItem(String email,String type,String name){
        List items = null;
        Users fromDatabase;
        try {
            EntityManager manager = managerFactory.createEntityManager();
            fromDatabase = manager.find(Users.class,email);
            items = manager.createQuery("FROM Items t WHERE t.email = :ownerEmail AND t.type = :typeToFind AND t.name = :providedName")
                    .setParameter("ownerEmail",fromDatabase)
                    .setParameter("typeToFind",type)
                    .setParameter("providedName",name)
                    .setMaxResults(1)
                    .getResultList();
            manager.close();
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
        if(items!=null && !items.isEmpty()) {
            return (Items) items.get(0);
        }else {
            return null;
        }
    }

    /**
     * Method which allows to add Items object to database
     * @param item Items object to be added to database
     * @param owner owner of Item
     */
    public void addItem(Items item,Users owner){
        try {
            EntityManager manager = managerFactory.createEntityManager();
            if(manager.createQuery("FROM Items i WHERE i.name = :newName AND i.email = :newEmail")
                    .setParameter("newName",item.getName())
                    .setParameter("newEmail",owner)
                    .getResultList().isEmpty()) {

                manager.getTransaction().begin();
                manager.persist(item);
                manager.getTransaction().commit();
            }
            manager.close();
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
    }

    /**
     * Method which deletes Items object from database
     * @param item Items object to delete
     */
    public void deleteItem(Items item) {
        List items;
        try {
            EntityManager manager = managerFactory.createEntityManager();
            manager.getTransaction().begin();
            switch (item.getType()) {
                case "list":
                    List tasks = this.getTasks(item);
                    for (Object t : tasks) {
                        this.deleteTask((Tasks) t);
                    }
                    break;
                case "calendar":
                    List events = this.getEvents(item);
                    for (Object e : events) {
                        this.deleteEvent((Events) e);
                    }
                    break;
                case "notebook":
                    List notes = this.getNotes(item);
                    for (Object n : notes) {
                        this.deleteNote((Notes) n);
                    }
                    break;
            }
            items = manager.createQuery("FROM Items i WHERE i.id  =  :oldId")
                    .setParameter("oldId",item.getId())
                    .getResultList();
            manager.remove(manager.contains(items.get(0)) ? items.get(0) : manager.merge(items.get(0)));
            manager.getTransaction().commit();
            manager.close();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    /**
     * Method which returns number of Tasks object related to Events object
     * @param events Events object to look for
     * @return number of Tasks object related to Events object
     */
    public int getNumberOfTasks(Events events){
        int returnedValue = 0;
        try {
            EntityManager manager = managerFactory.createEntityManager();
            returnedValue = manager.createQuery("FROM Tasks t WHERE t.events_id = :eventsID")
                    .setParameter("eventsID",events)
                    .getResultList().size();
            manager.close();
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
        return returnedValue;
    }

    /**
     * Method which returns number of Tasks object related to Notes object
     * @param notes Notes object to search for tasks
     * @return number of Tasks object related to Notes object
     */
    public int getNumberOfTasks(Notes notes){
        int returnedValue = 0;
        try {
            EntityManager manager = managerFactory.createEntityManager();
            returnedValue = manager.createQuery("FROM Tasks t WHERE t.notes_id = :notesID")
                    .setParameter("notesID",notes)
                    .getResultList().size();
            manager.close();
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
        return returnedValue;
    }

    /**
     * Method which returns number of Notes object related to Items object
     * @param notebook Notebook to check number of notes for
     * @return Notes object related to Items object
     */
    public int getNumberOfNotes(Items notebook){
        int returnedValue = 0;
        try {
            EntityManager manager = managerFactory.createEntityManager();
            returnedValue = manager.createQuery("FROM Notes n WHERE n.items_id = :itemsID")
                    .setParameter("itemsID",notebook)
                    .getResultList().size();
            manager.close();
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
        return returnedValue;
    }

    /**
     * Method to edit Items object in database
     * @param item Items object to be edited
     */
    public void editItem(Items item){
        List items;
        Items itemsOld;
        try {
            EntityManager manager = managerFactory.createEntityManager();
            manager.getTransaction().begin();
            items = manager.createQuery("FROM Items i WHERE i.id  =  :oldId")
                    .setParameter("oldId",item.getId())
                    .getResultList();
            itemsOld = (Items)items.get(0);
            itemsOld.setName(item.getName());
            itemsOld.setColor(item.getColor());
            manager.getTransaction().commit();
            manager.close();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    /**
     * Method to get List of Tasks connected to Items object
     * @param item Items object to get Tasks for
     * @return List of Tasks connected to Items object
     */
    public List getTasks(Items item){
        List tasks = null;
        try {
            EntityManager manager = managerFactory.createEntityManager();
            manager.getTransaction().begin();
            tasks = manager.createQuery("FROM Tasks t WHERE t.items_id = :itemIDToSearch")
                    .setParameter("itemIDToSearch",item)
                    .getResultList();
            manager.getTransaction().commit();
            manager.close();
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
        return tasks;
    }

    /**
     * Method to get List of COMPLETEDTODO connected to Items object
     * @param item Items object to get Tasks for
     * @return List of COMPLETEDTODO connected to Items object
     */
    public List getCTasks(Items item){
        List tasks = null;
        try {
            EntityManager manager = managerFactory.createEntityManager();
            manager.getTransaction().begin();
            tasks = manager.createQuery("FROM CompletedTodo t WHERE t.items_id = :itemIDToSearch")
                    .setParameter("itemIDToSearch",item)
                    .getResultList();
            manager.getTransaction().commit();
            manager.close();
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
        return tasks;
    }

    /**
     * Method to get List of COMPLETEDTODO connected to Notes object
     * @param notes  Notes object to get Tasks for
     * @return List of COMPLETEDTODO connected to Notes object
     */
    private List getCTasks(Notes notes){
        List tasks = null;
        try {
            EntityManager manager = managerFactory.createEntityManager();
            manager.getTransaction().begin();
            tasks = manager.createQuery("FROM CompletedTodo t WHERE t.notes_id = :notesIDToSearch")
                    .setParameter("notesIDToSearch",notes)
                    .getResultList();
            manager.getTransaction().commit();
            manager.close();
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
        return tasks;
    }

    /**
     * Method to get List of Tasks connected to Events object
     * @param event Events object to get Tasks for
     * @return List of Tasks connected to Events object
     */
    public List getTasks(Events event){
        List tasks = null;
        try {
            EntityManager manager = managerFactory.createEntityManager();
            manager.getTransaction().begin();
            tasks = manager.createQuery("FROM Tasks t WHERE t.events_id = :eventIDToSearch")
                    .setParameter("eventIDToSearch",event)
                    .getResultList();
            manager.getTransaction().commit();
            manager.close();
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
        return tasks;
    }

    /**
     * Method to get List of Tasks connected to Notes object
     * @param notes Notes object to get Tasks for
     * @return List of Tasks connected to Notes object
     */
    public List getTasks(Notes notes){
        List tasks = null;
        try {
            EntityManager manager = managerFactory.createEntityManager();
            manager.getTransaction().begin();
            tasks = manager.createQuery("FROM Tasks t WHERE t.notes_id = :notesIDToSearch")
                    .setParameter("notesIDToSearch",notes)
                    .getResultList();
            manager.getTransaction().commit();
            manager.close();
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
        return tasks;
    }

    /**
     * Method which adds Tasks object to database if it wasn't previously stored in there
     * or edits it otherwise
     * @param task Tasks object to add or edit
     */
    public void addOrEditTask(Tasks task){
        try {
            EntityManager manager = managerFactory.createEntityManager();
            if(manager.createQuery("FROM Tasks t WHERE t.id_todo = :newID").setParameter("newID",task.getId_todo()).getResultList().isEmpty()){
                addTask(task);
            }else{
                editTask(task);
            }
            manager.close();
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
    }

    /**
     * Method which add Tasks object to database
     * @param task Tasks object to add
     */
    public void addTask(Tasks task){
        try {
            EntityManager manager = managerFactory.createEntityManager();
            if(!(task.getNotes_id() == null & task.getItems_id() == null & task.getEvents_id() ==null)){
                manager.getTransaction().begin();
                manager.persist(task);
                manager.getTransaction().commit();
            }else{
                System.out.println("Task needs to be related to item or note or event");
            }
            manager.close();
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
    }

    /**
     * Method to edit Tasks object in database
     * @param task Tasks object to edit
     */
    public void editTask(Tasks task){
        Tasks newTask;
        try {
            EntityManager manager = managerFactory.createEntityManager();
            manager.getTransaction().begin();
            newTask = manager.find(Tasks.class,task.getId_todo());
            newTask.setName(task.getName());
            newTask.setStatus(task.getStatus());
            newTask.setItems_id(task.getItems_id());
            newTask.setPriority(task.getPriority());
            newTask.setDeadline(task.getDeadline());
            newTask.setEvents_id(task.getEvents_id());
            newTask.setNotes_id(task.getNotes_id());
            manager.getTransaction().commit();
            manager.close();
        } catch (Exception e){
            e.printStackTrace(System.out);
        }
    }

    /**
     * Method to delete Tasks object from database
     * @param task Tasks object to delete
     */
    public void deleteTask(Tasks task){
        Tasks tasksOld;
        try {
            EntityManager manager = managerFactory.createEntityManager();
            manager.getTransaction().begin();
            tasksOld = manager.find(Tasks.class,task.getId_todo());
            tasksOld.setStatus(task.getStatus());
            manager.getTransaction().commit();
            manager.getTransaction().begin();
            manager.remove(manager.contains(tasksOld) ? tasksOld : manager.merge(tasksOld));
            manager.getTransaction().commit();
            manager.close();
        } catch (Exception e){
            e.printStackTrace(System.out);
        }
    }

    /**
     * Method to delete COMPLETEDTODO object from database
     * @param task CompletedTodo object to delete
     */
    private void deleteCTask(CompletedTodo task){
        try {
            EntityManager manager = managerFactory.createEntityManager();
            manager.getTransaction().begin();
            manager.remove(manager.contains(task) ? task : manager.merge(task));
            manager.getTransaction().commit();
            manager.close();
        } catch (Exception e){
            e.printStackTrace(System.out);
        }
    }

    /**
     * Method to save Events object to database
     * @param events event to save to database
     */
    public void saveEvent(Events events){
        List eventList;
        Events newEvent;
        try {
            EntityManager manager = managerFactory.createEntityManager();
            manager.getTransaction().begin();
            eventList = manager.createQuery("FROM Events e WHERE e.id_event  =  :oldId")
                    .setParameter("oldId", events.getId_event())
                    .getResultList();
            if(!eventList.isEmpty()) {
                newEvent = (Events) eventList.get(0);
                newEvent.setName(events.getName());
                newEvent.setItem_id(events.getItem_id());
                newEvent.setNotes_id(events.getNotes_id());
                newEvent.setLocalization(events.getLocalization());
                newEvent.setColor(events.getColor());
                newEvent.setEnd(events.getEnd());
                newEvent.setStart(events.getStart());
            } else {
                manager.persist(events);
            }
            manager.getTransaction().commit();
            manager.close();
        } catch (Exception e){
            e.printStackTrace(System.out);
        }
    }

    /**
     * Method to delete Events object from database
     * @param event event to delete
     */
    public void deleteEvent(Events event){
        List events;
        Events eventsOld;
        try {
            EntityManager manager = managerFactory.createEntityManager();
            manager.getTransaction().begin();
            events = manager.createQuery("FROM Events e WHERE e.id_event  =  :oldId")
                    .setParameter("oldId", event.getId_event())
                    .getResultList();
            if(!events.isEmpty()) {
                eventsOld = (Events) events.get(0);
                Notes n = this.getNoteForEvent(eventsOld);
                if( n != null){
                    eventsOld.setNotes_id(null);
                    manager.getTransaction().commit();
                    manager.getTransaction().begin();
                    this.deleteNote(n);
                }
                for(Object o: this.getTasks(eventsOld)){
                    Tasks t = (Tasks)o;
                    t.setEvents_id(null);
                    this.editTask(t);
                    this.deleteTask(t);
                }
                manager.remove(manager.contains(eventsOld) ? eventsOld : manager.merge(eventsOld));
            }
            manager.getTransaction().commit();
            manager.close();
        } catch (Exception e){
            e.printStackTrace(System.out);
        }
    }

    /**
     * Method to get List of Events for provided Items object
     * @param item Items object to search database for
     * @return List of Events for provided Items object
     */
    private List getEvents(Items item){
        List tasks = null;
        try {
            EntityManager manager = managerFactory.createEntityManager();
            manager.getTransaction().begin();
            tasks = manager.createQuery("FROM Events e WHERE e.item_id = :itemIDToSearch")
                    .setParameter("itemIDToSearch",item)
                    .getResultList();
            manager.getTransaction().commit();
            manager.close();
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
        return tasks;
    }

    /**
     * Method to get List of Notes for provided Items object
     * @param item Items object to search database for
     * @return List of Notes for provided Items object
     */
    public List getNotes(Items item){
        List tasks = null;
        try {
            EntityManager manager = managerFactory.createEntityManager();
            tasks = manager.createQuery("FROM Notes n WHERE n.items_id = :itemIDToSearch")
                    .setParameter("itemIDToSearch",item)
                    .getResultList();
            manager.close();
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
        return tasks;
    }

    /**
     * Method to add Notes object to database
     * @param note note to add
     */
    public void addNote(Notes note){
        try {
            EntityManager manager = managerFactory.createEntityManager();
            if(manager.createQuery("FROM Notes  n WHERE n.title = :newName")
                    .setParameter("newName",note.getTitle())
                    .getResultList().isEmpty()) {
                manager.getTransaction().begin();
                manager.persist(note);
                if(note.getEvents_id() != null){
                    note.getEvents_id().setNotes_id(note);
                }
                manager.getTransaction().commit();
            }
            manager.close();
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
    }

    /**
     * Method to edit Notes object in database
     * @param notes note to be edited
     */
    public void editNote(Notes notes){

        Notes newNotes;
        try {
            EntityManager manager = managerFactory.createEntityManager();
            manager.getTransaction().begin();
            newNotes = manager.find(Notes.class,notes.getNotes_id());
            newNotes.setEvents_id(notes.getEvents_id());
            newNotes.setText(notes.getText());
            newNotes.setColor(notes.getColor());
            newNotes.setItems_id(notes.getItems_id());
            newNotes.setTitle(notes.getTitle());
            manager.getTransaction().commit();
            manager.close();
        } catch (Exception e){
            e.printStackTrace(System.out);
        }
    }

    /**
     * Method to delete Notes object from database
     * @param note note to be deleted
     */
    public void deleteNote(Notes note){
        List notes;
        Notes noteOld;
        try {
            EntityManager manager = managerFactory.createEntityManager();
            manager.getTransaction().begin();
            notes = manager.createQuery("FROM Notes n WHERE n.notes_id =  :oldId")
                    .setParameter("oldId", note.getNotes_id())
                    .getResultList();
            if(!notes.isEmpty()) {
                noteOld = (Notes) notes.get(0);
                if(noteOld.getEvents_id() != null){
                    noteOld.setEvents_id(null);
                }
                for(Object o:this.getTasks(noteOld)){
                    Tasks t = (Tasks)o;
                    this.deleteTask(t);
                }
                for(Object o:this.getCTasks(noteOld)){
                    CompletedTodo t = (CompletedTodo)o;
                    this.deleteCTask(t);
                }
                manager.getTransaction().commit();
                manager.getTransaction().begin();
                manager.remove(manager.contains(noteOld) ? noteOld : manager.merge(noteOld));
            }
            manager.getTransaction().commit();
            manager.close();
        } catch (Exception e){
            e.printStackTrace(System.out);
        }
    }

    /**
     * Method to check if there is an event happening on provided date
     * @param day day of date
     * @param month month of date
     * @param year year of date
     * @param user user to check for events for
     * @return first found event if there is one, null otherwise
     */
    public Events isThereAnEvent(int day,int month,int year,Users user){
        List events = null;
        try {
            Calendar calendar = Calendar.getInstance();
            Calendar calendar1 = Calendar.getInstance();
            calendar.set(year,month-1,day,0,0,0);
            calendar1.set(year,month-1,day+1,0,0,0);
            EntityManager manager = managerFactory.createEntityManager();
            events = manager.createQuery("FROM Events e WHERE e.item_id.email = :user AND" +
                    " e.start >= :date AND e.start < :date1")
                    .setParameter("user",user)
                    .setParameter("date",calendar.getTime())
                    .setParameter("date1",calendar1.getTime())
                    .getResultList();
            manager.close();
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
        if(events != null && !events.isEmpty()) {
            return (Events) events.get(0);
        }
        return null;
    }

    /**
     * Method to get a list of all events for provided User
     * @param user user to check for events for
     * @return list of all events for provided User
     */
    public List getEvents(Users user){
        List events = null;
        try {
            EntityManager manager = managerFactory.createEntityManager();
            events = manager.createQuery("FROM Events e WHERE e.item_id.email = :user")
                    .setParameter("user",user)
                    .getResultList();
            manager.close();
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
        if(events != null && !events.isEmpty()) {
            return events;
        }
        return null;
    }

    /**
     * Method which returns note related to event
     * @param events event to search for note
     * @return Notes object if it exists for this event or null
     */
    private Notes getNoteForEvent(Events events){
        List notes = null;
        try {
            EntityManager manager = managerFactory.createEntityManager();
            manager.getTransaction().begin();
            notes = manager.createQuery("FROM Notes n WHERE events_id = :eventsIDToSearch")
                    .setParameter("eventsIDToSearch",events)
                    .getResultList();
            manager.getTransaction().commit();
            manager.close();
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
        if(notes == null){
            return null;
        }else if(notes.isEmpty()){
            return null;
        }
        return (Notes)notes.get(0);
    }
}
