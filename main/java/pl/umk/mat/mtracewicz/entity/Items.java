package pl.umk.mat.mtracewicz.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "ITEMS",schema = "G8_MTRACEWICZ")
public class Items{
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "color")
    private String color;

    @Column(name = "type")
    private String type;

    @ManyToOne
    @JoinColumn(name = "email",nullable = false)
    private Users email;

    public Items(){}

    public Items(String name, String color, String type, Users email) {
        this.name = name;
        this.color = color;
        this.type = type;
        this.email = email;
    }

    public Items(Integer id,String name, String color, String type, Users email) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.type = type;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Users getEmail() {
        return email;
    }

    public void setEmail(Users email) {
        this.email = email;
    }
}
