package pl.umk.mat.mtracewicz.entity;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

/**
 * Class representing entity for hibernate mapping for COMPLETEDTODO table
 */
@Entity
@Table(name = "COMPLETED_TODO",schema = "G8_MTRACEWICZ")
public class CompletedTodo{
    /**
     * Name of completed task
     */
    @Id
    @Column(name = "name")
    private String name;

    /**
     * Deadline for task
     */
    @Column(name = "deadline")
    private Date deadline;

    /**
     * Status of task
     */
    @Column(name = "status")
    private Integer status;

    /**
     * Priority of task
     */
    @Column(name = "priority")
    private Integer priority;

    /**
     * Items object referenced to COMPLETEDTODO by foreign key
     */
    @ManyToOne
    @JoinColumn(name = "items_id")
    private Items items_id;

    /**
     *  Notes object referenced to COMPLETEDTODO by foreign key
     */
    @ManyToOne
    @JoinColumn(name = "notes_id")
    private Notes notes_id;

    /**
     *  Events object referenced to COMPLETEDTODO by foreign key
     */
    @ManyToOne
    @JoinColumn(name = "events_id")
    private Events events_id;

    /**
     *  Date of completion for task
     */
    @Column(name = "completion_date")
    private Date completion_date;

    /**
     * Default constructor
     */
    public CompletedTodo(){}

    /**
     * Getter for name
     * @return String name
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
     * @return Date deadline
     */
    public Date getDeadline() {
        return deadline;
    }

    /**
     * Getter priority
     * @return Integer priority
     */
    public Integer getPriority() {
        return priority;
    }
}
