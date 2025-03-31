package net.javaguides.springboot.entity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
public class User implements Serializable{

    @Id
    @Column(name="userId",updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;  // ASK -> The M-T-M Join table shows userId!
    
    @Column(nullable=false , unique=true)
    private String username;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    
    @ManyToMany(cascade = CascadeType.ALL , fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "userId"),
        inverseJoinColumns = @JoinColumn(name = "roleId")
    )
    private List<Roles> roles=new ArrayList<>();

    private String status;

    private String email;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Version
    private Long version;

}