package pl.umk.mat.mtracewicz.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 *Class representing entity for hibernate mapping for TASKS table
 */
@Entity
@Table(name = "TASKS",schema = "G8_MTRACEWICZ")
public class Tasks {
    /**
     *  Primary key, id set by sequence
     */
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    @Column(name = "id_todo")
    private Integer id_todo;

    /**
     * Name of Task
     */
    @Column(name = "name")
    private String name;

    /**
     * Date of deadline for task
     */
    @Column(name = "deadline")
    private Date deadline;

    /**
     * Status of task
     */
    @Column(name = "status")
    private Integer status;

    /**
     *  Priority of task
     */
    @Column(name = "priority")
    private Integer priority;

    /**
     * Items object connected by foreign key
     */
    @ManyToOne()
    @JoinColumn(name = "items_id")
    private Items items_id;

    /**
     * Notes object connected by foreign key
     */
    @ManyToOne
    @JoinColumn(name = "notes_id")
    private Notes notes_id;

    /**
     *  Events object connected by foreign key
     */
    @ManyToOne
    @JoinColumn(name = "events_id")
    private Events events_id;

    /**
     *  Default constructor
     */
    public Tasks(){}

    /**
     * Getter for id
     * @return Task.id
     */
    public Integer getId_todo() {
        return id_todo;
    }

    /**
     * Getter for name
     * @return Task.name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name
     * @param name name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for deadline
     * @return Tasks.deadline
     */
    public Date getDeadline() {
        return deadline;
    }

    /**
     * Setter for deadline
     * @param deadline deadline to set
     */
    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    /**
     * Getter for status
     * @return Tasks.status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * Setter for status
     * @param status status to set
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * Getter for priority
     * @return Tasks.priority
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * Setter for priority
     * @param priority priority to set
     */
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    /**
     * Getter for Items object connected by foreign key
     * @return Items object connected by foreign key
     */
    public Items getItems_id() {
        return items_id;
    }

    /**
     * Setter for Items object connected by foreign key
     * @param items_id Items object to set
     */
    public void setItems_id(Items items_id) {
        this.items_id = items_id;
    }

    /**
     * Getter for Notes object connected by foreign key
     * @return Notes object connected by foreign key
     */
    public Notes getNotes_id() {
        return notes_id;
    }

    /**
     * Setter for Notes object connected by foreign key
     * @param notes_id Notes object connected by foreign key
     */
    public void setNotes_id(Notes notes_id) {
        this.notes_id = notes_id;
    }

    /**
     * Getter for Events object connected by foreign key
     * @return Events object connected by foreign key
     */
    public Events getEvents_id() {
        return events_id;
    }

    /**
     * Setter for Events object connected by foreign key
     * @param events_id Events object to set
     */
    public void setEvents_id(Events events_id) {
        this.events_id = events_id;
    }
}
