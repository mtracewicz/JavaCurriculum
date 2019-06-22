package pl.umk.mat.mtracewicz.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "TASKS",schema = "G8_MTRACEWICZ")
public class Tasks {
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    @Column(name = "id_todo")
    private Integer id_todo;

    @Column(name = "name")
    private String name;

    @Column(name = "deadline")
    private Date deadline;

    @Column(name = "status")
    private Integer status;

    @Column(name = "priority")
    private Integer priority;

    @ManyToOne()
    @JoinColumn(name = "items_id")
    private Items items_id;

    @OneToMany
    @JoinColumn(name = "notes_id")
    private Set<Notes> notes_id;

    @ManyToOne
    @JoinColumn(name = "events_id")
    private Events events_id;

    public Tasks(){}

    public Tasks(String name, Date deadline, Integer status, Integer priority, Items items_id) {
        this.name = name;
        this.deadline = deadline;
        this.status = status;
        this.priority = priority;
        this.items_id = items_id;
    }

    public Tasks(String name, Date deadline, Integer status, Integer priority, Set<Notes> notes_id) {
        this.name = name;
        this.deadline = deadline;
        this.status = status;
        this.priority = priority;
        this.notes_id = notes_id;
    }

    public Tasks(String name, Date deadline, Integer status, Integer priority, Events events_id) {
        this.name = name;
        this.deadline = deadline;
        this.status = status;
        this.priority = priority;
        this.events_id = events_id;
    }

    public Integer getId_todo() {
        return id_todo;
    }

    public void setId_todo(Integer id_todo) {
        this.id_todo = id_todo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Items getItems_id() {
        return items_id;
    }

    public void setItems_id(Items items_id) {
        this.items_id = items_id;
    }

    public Set<Notes> getNotes_id() {
        return notes_id;
    }

    public void setNotes_id(Set<Notes> notes_id) {
        this.notes_id = notes_id;
    }

    public Events getEvents_id() {
        return events_id;
    }

    public void setEvents_id(Events events_id) {
        this.events_id = events_id;
    }
}
