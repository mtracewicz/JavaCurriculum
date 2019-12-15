package pl.umk.mat.mtracewicz.entity;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "EVENTS",schema = "G8_MTRACEWICZ")
public class Events {
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    @Column(name = "id_event")
    private Integer id_event;

    @Column(name = "name")
    private String name;

    @Column(name = "STARTDATE")
    private Date start;

    @Column(name = "ENDDATE")
    private Date end;

    @Column(name = "localization")
    private String localization;

    @Column(name = "color")
    private String color;

    @OneToOne
    @JoinColumn(name = "notes_id")
    private Notes notes_id;

    @OneToMany
    @JoinColumn(name = "todo_id")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Set<Tasks> tasks_id;

    @ManyToOne
    @JoinColumn(name = "items_id")
    private Items item_id;

    public Events(){}

    public boolean equals(Events e) {
        return  (this.getId_event().equals(e.getId_event()));
    }

    public Events(Integer id_event, String name, Date start, Date end, String localization, String color, Items item_id) {
        this.id_event = id_event;
        this.name = name;
        this.start = start;
        this.end = end;
        this.localization = localization;
        this.color = color;
        this.item_id = item_id;
    }

    public Events(Integer id_event, String name, Date start, Date end, String localization, String color, Notes notes_id) {
        this.id_event = id_event;
        this.name = name;
        this.start = start;
        this.end = end;
        this.localization = localization;
        this.color = color;
        this.notes_id = notes_id;
    }

    public Events(Integer id_event, String name, Date start, Date end, String localization, String color, Set<Tasks> tasks_id) {
        this.id_event = id_event;
        this.name = name;
        this.start = start;
        this.end = end;
        this.localization = localization;
        this.color = color;
        this.tasks_id = tasks_id;
    }

    public Integer getId_event() {
        return id_event;
    }

    public void setId_event(Integer id_event) {
        this.id_event = id_event;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getLocalization() {
        return localization;
    }

    public void setLocalization(String localization) {
        this.localization = localization;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Items getItem_id() {
        return item_id;
    }

    public void setItem_id(Items item_id) {
        this.item_id = item_id;
    }

    public Notes getNotes_id() {
        return notes_id;
    }

    public void setNotes_id(Notes notes_id) {
        this.notes_id = notes_id;
    }

    public Set<Tasks> getTasks_id() {
        return tasks_id;
    }

    public void setTasks_id(Set<Tasks> tasks_id) {
        this.tasks_id = tasks_id;
    }
}
