package pl.umk.mat.mtracewicz.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 *Class representing entity for hibernate mapping for NOTES table
 */
@Entity
@Table(name = "NOTES",schema = "G8_MTRACEWICZ")
public class Notes {
    /**
     * Primary key, numeric id value set by sequence
     */
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    @Column(name = "notes_id")
    private Integer notes_id;

    /**
     *  Notes title
     */
    @Column(name = "title")
    private String title;

    /**
     *  Note text
     */
    @Column(name = "text")
    private String text;

    /**
     *  Note color
     */
    @Column(name = "color")
    private String color;

    /**
     * Items object referenced by foreign key
     */
    @ManyToOne
    @JoinColumn(name = "items_id")
    private Items items_id;

    /**
     *  Events object referenced by foreign key
     */
    @OneToOne
    @JoinColumn(name = "events_id")
    private Events events_id;

    /**
     * Default constructor
     */
    public Notes(){}

    /**
     * Getter for id
     * @return Notes.id
     */
    public Integer getNotes_id() {
        return notes_id;
    }

    /**
     * Getter for title
     * @return Notes.title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for title
     * @param title title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for note text
     * @return Notes.text
     */
    public String getText() {
        return text;
    }

    /**
     * Setter for notes text
     * @param text text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Getter for color
     * @return Tasks.color
     */
    public String getColor() {
        return color;
    }

    /**
     * Setter for notes color
     * @param color color to set
     */
    public void setColor(String color) {
        this.color = color;
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
