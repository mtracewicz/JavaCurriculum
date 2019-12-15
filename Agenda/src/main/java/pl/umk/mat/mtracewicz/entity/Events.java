package pl.umk.mat.mtracewicz.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Class representing entity for hibernate mapping for EVENTS table
 */
@Entity
@Table(name = "EVENTS",schema = "G8_MTRACEWICZ")
public class Events {
    /**
     * Numerical id generated from sequence
     */
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    @Column(name = "id_event")
    private Integer id_event;

    /**
     *  Name of event
     */
    @Column(name = "name")
    private String name;

    /**
     *  Starting date of event
     */
    @Column(name = "STARTDATE")
    private Date start;

    /**
     * End date of event
     */
    @Column(name = "ENDDATE")
    private Date end;

    /**
     *  String of localization in which event takes place
     */
    @Column(name = "localization")
    private String localization;

    /**
     * Color of event
     */
    @Column(name = "color")
    private String color;

    /**
     * Reference to Notes object which NOTES foreign key in EVENTS points to
     */
    @OneToOne
    @JoinColumn(name = "notes_id")
    private Notes notes_id;

    /**
     * Reference to Items object which ITEMS foreign key in EVENTS points to
     */
    @ManyToOne
    @JoinColumn(name = "items_id")
    private Items item_id;

    /**
     * Default constructor
     */
    public Events(){}

    /**
     * Custom equals method for object where only compering
     * events id
     * @param e event to compare to
     * @return true if id are equal, false otherwise
     */
    public boolean equals(Events e) {
        return  (this.getId_event().equals(e.getId_event()));
    }

    /**
     * Getter for id
     * @return Event.id
     */
    public Integer getId_event() {
        return id_event;
    }

    /**
     * Getter for name
     * @return Event.name
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
     * Getter for Startdate
     * @return Event.startdate
     */
    public Date getStart() {
        return start;
    }

    /**
     * Setter for startdate
     * @param start date to set
     */
    public void setStart(Date start) {
        this.start = start;
    }

    /**
     * Getter for end date
     * @return Event.enddate
     */
    public Date getEnd() {
        return end;
    }

    /**
     * Setter for end date
     * @param end date to set
     */
    public void setEnd(Date end) {
        this.end = end;
    }

    /**
     * Getter for event localization
     * @return Event.localization
     */
    public String getLocalization() {
        return localization;
    }

    /**
     * Setter for localization
     * @param localization localization to set
     */
    public void setLocalization(String localization) {
        this.localization = localization;
    }

    /**
     * Getter for color
     * @return Event.color
     */
    public String getColor() {
        return color;
    }

    /**
     * Setter for color
     * @param color color to set
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Get Items object related to event by foreign key
     * @return Items object related to event by foreign key
     */
    public Items getItem_id() {
        return item_id;
    }

    /**
     * Set Items object related to event by foreign key
     * @param item_id Items object related to event by foreign key to set
     */
    public void setItem_id(Items item_id) {
        this.item_id = item_id;
    }

    /**
     * Getter for Notes object related to event by foreign key
     * @return Notes object related to event by foreign key
     */
    public Notes getNotes_id() {
        return notes_id;
    }

    /**
     * Setter for Notes object related to event by foreign key
     * @param notes_id Notes object related to event by foreign key
     */
    public void setNotes_id(Notes notes_id) {
        this.notes_id = notes_id;
    }
}
