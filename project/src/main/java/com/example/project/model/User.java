package com.example.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Generated;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="username", unique = true, nullable = false, length = 16)
    @Size(min=4, message = "Минимум 4 символа.")
    @Size(max = 16, message = "Максимум 16 символов.")
    private String username;

    @Column(name="name", length = 50)
    @NotBlank(message = "Это поле не может быть пустым.")
    @Size(max = 50, message = "Максимум 50 символов.")
    private String name;

    @Column(name = "surname", length = 50)
    @NotBlank(message = "Это поле не может быть пустым.")
    @Size(max = 50, message = "Максимум 50 символов.")
    private String surname;

    @Column(name="password", nullable = false, length = 255)
    @Size(max = 255, message = "Максимум 255 символов.")
    private String password;

    @Transient
    private String confirmPassword;

    @Column(name = "role", nullable = false, length = 10)
    private String role;

    @OneToMany(mappedBy = "user",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
   private List<Tweet> tweets;


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
