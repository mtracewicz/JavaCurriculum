package pl.umk.mat.mtracewicz.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "USERS",schema = "G8_MTRACEWICZ")
public class Users {

    @Id
    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "email")
    private Set<Items> items;

    public Users(){}

    public Users(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public Users(String email) {
        this.email = email;
    }

    public Users(String email, String password, Set<Items> items) {
        this.email = email;
        this.password = password;
        this.items = items;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Items> getItems() {
        return items;
    }

    public void setItems(Set<Items> items) {
        this.items = items;
    }
}
