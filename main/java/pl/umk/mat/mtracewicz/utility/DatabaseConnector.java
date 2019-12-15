package pl.umk.mat.mtracewicz.utility;

import org.mindrot.jbcrypt.BCrypt;
import pl.umk.mat.mtracewicz.entity.*;
import pl.umk.mat.mtracewicz.gui.occurrencePanels.Event;
import pl.umk.mat.mtracewicz.gui.occurrencePanels.Note;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DatabaseConnector {
    private EntityManagerFactory managerFactory;

    public DatabaseConnector() {
        try {
            this.managerFactory = Persistence.createEntityManagerFactory("pl.umk.mat.mtracewicz");
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }
    private boolean validateUser(Users user){
        JPasswordField pf = new JPasswordField();
        int okCxl = JOptionPane.showConfirmDialog(null, pf, "Enter Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        user.setPassword(String.valueOf(pf.getPassword()));
        if(this.checkUserCredentials(user)){
            return true;
        }else {
            JOptionPane.showMessageDialog(null, "Wrong password!", "Inane error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    public boolean checkUserCredentials(Users user){
        boolean returnValue = false;
        try {
            EntityManager manager = managerFactory.createEntityManager();
            if(manager.find(Users.class,user.getEmail()) != null){
                Users u = manager.find(Users.class,user.getEmail());
                returnValue = BCrypt.checkpw(user.getPassword(), u.getPassword());
            }
            manager.close();
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
        return returnValue;
    }
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
    public void deleteUser(Users user){
        if(this.validateUser(user)) {
            try {
                EntityManager manager = managerFactory.createEntityManager();
                manager.getTransaction().begin();
                if (manager.find(Users.class, user.getEmail()) != null) {
                    manager.remove(manager.contains(user) ? user : manager.merge(user));
                    manager.getTransaction().commit();
                }
                manager.close();
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
    }
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
    public int getNumberOfItems(String email, String type){
        int returnedValue = 0;
        Users fromDatabase;
        try {
            EntityManager manager = managerFactory.createEntityManager();
            manager.getTransaction().begin();
            fromDatabase = manager.find(Users.class,email);
            returnedValue = manager.createQuery("FROM Items t WHERE t.id IN (SELECT id FROM Items item WHERE item.email = :ownerEmail AND item.type = :searchingForType)")
                    .setParameter("ownerEmail",fromDatabase)
                    .setParameter("searchingForType",type)
                    .getResultList().size();
            manager.getTransaction().commit();
            manager.close();
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
        return returnedValue;
    }
    public List getItems(String email, String type){
        List items = null;
        Users fromDatabase;
        try {
            EntityManager manager = managerFactory.createEntityManager();
            manager.getTransaction().begin();
            fromDatabase = manager.find(Users.class,email);
            items = manager.createQuery("FROM Items t WHERE t.id IN (SELECT id FROM Items item WHERE item.email = :ownerEmail AND item.type = :typeToFind)")
                    .setParameter("ownerEmail",fromDatabase)
                    .setParameter("typeToFind",type)
                    .getResultList();
            manager.getTransaction().commit();
            manager.close();
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
        return items;
    }
    public Items getItem(String email,String type,String name){
        List items = null;
        Users fromDatabase;
        try {
            EntityManager manager = managerFactory.createEntityManager();
            manager.getTransaction().begin();
            fromDatabase = manager.find(Users.class,email);
            items = manager.createQuery("FROM Items t WHERE t.email = :ownerEmail AND t.type = :typeToFind AND t.name = :providedName")
                    .setParameter("ownerEmail",fromDatabase)
                    .setParameter("typeToFind",type)
                    .setParameter("providedName",name)
                    .setMaxResults(1)
                    .getResultList();
            manager.getTransaction().commit();
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
    public void deleteItem(Items item) {
        List items;
        try {
            EntityManager manager = managerFactory.createEntityManager();
            manager.getTransaction().begin();
            if(item.getType().equals("list")) {
                List tasks = this.getTasks(item);
                for (Object t : tasks) {
                    this.deleteTask((Tasks) t);
                }
            }else if(item.getType().equals("calendar")){
                List events = this.getEvents(item);
                for (Object e : events) {
                    this.deleteEvent((Events) e);
                }
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
    public int getNumberOfTaks(Events events){
        int returnedValue = 0;
        try {
            EntityManager manager = managerFactory.createEntityManager();
            manager.getTransaction().begin();
            returnedValue = manager.createQuery("FROM Tasks t WHERE t.events_id = :eventsID")
                    .setParameter("eventsID",events.getId_event())
                    .getResultList().size();
            manager.getTransaction().commit();
            manager.close();
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
        return returnedValue;
    }
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
    public List<Tasks> getTasks(String email, Events event){
        List<Tasks> tasks = null;
        try {
            EntityManager manager = managerFactory.createEntityManager();
            manager.getTransaction().begin();
            tasks = manager.createQuery("FROM Tasks t WHERE t.id IN (SELECT id FROM Tasks task WHERE task.items_id= :eventIDToSearch)")
                    .setParameter("eventIDToSearch",event.getId_event())
                    .getResultList();
            manager.getTransaction().commit();
            manager.close();
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
        return tasks;
    }
    public List<Tasks> getTasks(String email, Notes note){
        List<Tasks> tasks = null;
        try {
            EntityManager manager = managerFactory.createEntityManager();
            manager.getTransaction().begin();
            tasks = manager.createQuery("FROM Tasks t WHERE t.id IN (SELECT id FROM Tasks task WHERE task.items_id= :notesIDToSearch)")
                    .setParameter("notesIDToSearch",note.getNotes_id())
                    .getResultList();
            manager.getTransaction().commit();
            manager.close();
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
        return tasks;
    }
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
    public void editTask(Tasks task){
        List tasks;
        Tasks newTask;
        try {
            EntityManager manager = managerFactory.createEntityManager();
            manager.getTransaction().begin();
            tasks = manager.createQuery("FROM Tasks t WHERE t.id_todo  =  :oldId")
                    .setParameter("oldId", task.getId_todo())
                    .getResultList();
            newTask = (Tasks)tasks.get(0);
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
    public void deleteTask(Tasks task){
        List tasks;
        Tasks tasksOld;
        try {
            EntityManager manager = managerFactory.createEntityManager();
            manager.getTransaction().begin();
            tasks = manager.createQuery("FROM Tasks t WHERE t.id_todo  =  :oldId")
                    .setParameter("oldId", task.getId_todo())
                    .getResultList();
            tasksOld = (Tasks)tasks.get(0);
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
    public void saveEvent(Events events){
        List eventList;
        Events newEvent;
        try {
            EntityManager manager = managerFactory.createEntityManager();
            manager.getTransaction().begin();
            if (events.getId_event() != null) {
                eventList = manager.createQuery("FROM Events e WHERE e.id_event  =  :oldId")
                        .setParameter("oldId", events.getId_event())
                        .getResultList();
                newEvent = (Events) eventList.get(0);
                newEvent.setName(events.getName());
                newEvent.setItem_id(events.getItem_id());
                newEvent.setNotes_id(events.getNotes_id());
                newEvent.setLocalization(events.getLocalization());
                newEvent.setColor(events.getColor());
                newEvent.setEnd(events.getEnd());
                newEvent.setStart(events.getStart());
                newEvent.setTasks_id(events.getTasks_id());
            } else {
                manager.persist(events);
            }
            manager.getTransaction().commit();
            manager.close();
        } catch (Exception e){
            e.printStackTrace(System.out);
        }
    }
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
                if(this.getNoteForEvent(eventsOld) != null){
                    System.out.println("IN");
                    this.deleteNote(this.getNoteForEvent(eventsOld));
                }
                manager.remove(manager.contains(eventsOld) ? eventsOld : manager.merge(eventsOld));
            }
            manager.getTransaction().commit();
            manager.close();
        } catch (Exception e){
            e.printStackTrace(System.out);
        }
    }
    public List getEvents(Items item){
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
    public void addNote(Notes note){
        try {
            EntityManager manager = managerFactory.createEntityManager();
            if(manager.createQuery("FROM Notes  n WHERE n.title = :newName")
                    .setParameter("newName",note.getTitle())
                    .getResultList().isEmpty()) {

                manager.getTransaction().begin();
                manager.persist(note);
                manager.getTransaction().commit();
            }
            manager.close();
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
    }
    public void editNote(Notes notes){
        List notesList;
        Notes newNotes;
        try {
            EntityManager manager = managerFactory.createEntityManager();
            manager.getTransaction().begin();
            notesList = manager.createQuery("FROM Notes n WHERE n.notes_id  =  :oldId")
                    .setParameter("oldId", notes.getNotes_id())
                    .getResultList();
            newNotes = (Notes)notesList.get(0);
            newNotes.setEvents_id(notes.getEvents_id());
            newNotes.setText(notes.getText());
            newNotes.setColor(notes.getColor());
            newNotes.setItems_id(notes.getItems_id());
            newNotes.setTasks_id(notes.getTasks_id());
            newNotes.setTitle(notes.getTitle());
            manager.getTransaction().commit();
            manager.close();
        } catch (Exception e){
            e.printStackTrace(System.out);
        }
    }
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
        if(notes.isEmpty()){
            return null;
        }
        return (Notes)notes.get(0);
    }
}
