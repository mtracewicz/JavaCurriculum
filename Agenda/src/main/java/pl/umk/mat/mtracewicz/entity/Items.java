package pl.umk.mat.mtracewicz.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 *Class representing entity for hibernate mapping for ITEMS table
 */
@Entity
@Table(name = "ITEMS",schema = "G8_MTRACEWICZ")
public class Items{
    /**
     * Primary key, id generated by sequence
     */
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    @Column(name = "id")
    private Integer id;

    /**
     * Name of item
     */
    @Column(name = "name")
    private String name;

    /**
     * Color of item
     */
    @Column(name = "color")
    private String color;

    /**
     * Type of item, can be one of following:
     * list, notebook, event
     */
    @Column(name = "type")
    private String type;

    /**
     *  Users object connected by foreign key
     */
    @ManyToOne
    @JoinColumn(name = "email",nullable = false)
    private Users email;

    /**
     *  Default constructor
     */
    public Items(){}

    /**
     * Constructor without setting id
     * @param name name to set
     * @param color color to set
     * @param type type to set
     * @param email email to set(Users object)
     */
    public Items(String name, String color, String type, Users email) {
        this.name = name;
        this.color = color;
        this.type = type;
        this.email = email;
    }

    /**
     * Constructor
     * @param id id to set
     * @param name name to set
     * @param color color to set
     * @param type type to set
     * @param email email to set(Users object)
     */
    public Items(Integer id,String name, String color, String type, Users email) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.type = type;
        this.email = email;
    }

    /**
     * Getter for Id
     * @return Items.id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Getter for name
     * @return Items.name
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
     * Getter for color
     * @return Items.color
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
     * Getter for type
     * @return String representing Items.type
     */
    public String getType() {
        return type;
    }

    /**
     * Setter for type
     * @param type type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Setter for user
     * @param email Users object to set
     */
    public void setEmail(Users email) {
        this.email = email;
    }
}