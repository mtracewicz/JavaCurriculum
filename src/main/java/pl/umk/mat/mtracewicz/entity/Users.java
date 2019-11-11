package pl.umk.mat.mtracewicz.entity;

import javax.persistence.*;
import java.util.Set;

/**
 *Class representing entity for hibernate mapping for USERS table
 */
@Entity
@Table(name = "USERS",schema = "G8_MTRACEWICZ")
public class Users {

    /**
     * Email of user, this is primary key
     */
    @Id
    @Column(name = "email")
    private String email;

    /**
     * Hash of password
     */
    @Column(name = "password")
    private String password;

    /**
     * Set of Items object connected to Users by foreign key
     */
    @OneToMany(mappedBy = "email")
    private Set<Items> items;

    /**
     *  Default constructor
     */
    public Users(){}

    /**
     * Standard Constructor with email and password hash
     * @param email User email
     * @param password Password Hash
     */
    public Users(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * Constructor with only user email
     * @param email Email of user
     */
    public Users(String email) {
        this.email = email;
    }


    /**
     * Getter for email
     * @return User.email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter for email
     * @param email email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter for password hash
     * @return password hash
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter for password hash
     * @param password password hash to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter for Items connected by foreign key
     * @return Set of Items
     */
    public Set<Items> getItems() {
        return items;
    }

    /**
     * Setter for Items set
     * @param items Item set
     */
    public void setItems(Set<Items> items) {
        this.items = items;
    }
}
