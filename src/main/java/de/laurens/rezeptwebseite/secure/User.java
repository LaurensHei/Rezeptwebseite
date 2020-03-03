package de.laurens.rezeptwebseite.secure;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

    public int id;
    public String username;
    public String email;
    public String password;
    public String permission;

    public User(int id, String username, String email, String password, String permission) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.permission = permission;
    }

    public User(int id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }
}
