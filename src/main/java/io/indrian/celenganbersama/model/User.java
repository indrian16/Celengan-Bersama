package io.indrian.celenganbersama.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Email
    @Column(unique = true)
    private String email;

    @NotNull
    @NotEmpty
    @Column(name = "first_name")
    @JsonProperty("first_name")
    private String firstName;

    @Column(name = "last_name")
    @JsonProperty("last_name")
    private String lastName;

    @Column(name = "avatar_url")
    @JsonProperty("avatar_url")
    private String avatarUrl;

    @ManyToMany(mappedBy = "users")
    @JsonIgnore
    private Set<Saving> savings = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<Income> incomes = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<Expense> expenses = new HashSet<>();

    @JsonProperty("display_name")
    public String getDisplayName() {

        return firstName + " " + lastName;
    }
}