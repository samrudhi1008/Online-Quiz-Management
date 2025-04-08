package model;


import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;
    
    @Column(nullable = false, unique = true, length = 50)
    private String username;
    
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    
    @Column(name = "password", nullable = false, length = 255)
    private String password;
    
   


    public User() {
    }

    public User(String username, String email, String passwordHash) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(Integer userId, String username, String email, String passwordHash) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
    }
    // Getters and setters
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

}
