package pl.umk.mat.mtracewicz.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "NOTES",schema = "G8_MTRACEWICZ")
public class Notes {
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    @Column(name = "notes_id")
    private Integer notes_id;

    @Column(name = "title")
    private String title;

    @Column(name = "text")
    private String text;

    @Column(name = "color")
    private String color;

    @ManyToOne
    @JoinColumn(name = "items_id")
    private Items items_id;

    @OneToOne
    @JoinColumn(name = "events_id")
    private Events events_id;

    @ManyToOne
    @JoinColumn(name = "todo_id")
    private Tasks tasks_id;

    public Notes(){}

    public Notes(Integer notes_id, String title, String text, String color, Items items_id) {
        this.notes_id = notes_id;
        this.title = title;
        this.text = text;
        this.color = color;
        this.items_id = items_id;
    }

    public Notes(Integer notes_id, String title, String text, String color, Events events_id) {
        this.notes_id = notes_id;
        this.title = title;
        this.text = text;
        this.color = color;
        this.events_id = events_id;
    }

    public Notes(Integer notes_id, String title, String text, String color, Tasks tasks_id) {
        this.notes_id = notes_id;
        this.title = title;
        this.text = text;
        this.color = color;
        this.tasks_id = tasks_id;
    }

    public Integer getNotes_id() {
        return notes_id;
    }

    public void setNotes_id(Integer notes_id) {
        this.notes_id = notes_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Items getItems_id() {
        return items_id;
    }

    public void setItems_id(Items items_id) {
        this.items_id = items_id;
    }

    public Events getEvents_id() {
        return events_id;
    }

    public void setEvents_id(Events events_id) {
        this.events_id = events_id;
    }

    public Tasks getTasks_id() {
        return tasks_id;
    }

    public void setTasks_id(Tasks tasks_id) {
        this.tasks_id = tasks_id;
    }

}
