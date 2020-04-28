package io.indrian.celenganbersama.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "savings")
@Getter
@Setter
public class Saving extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "owner_id")
    @JsonProperty("owner_id")
    private Long ownerId;

    private String name;
    private String hope;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "saving_user",
            joinColumns = @JoinColumn(name = "saving_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
    )
    @JsonIgnoreProperties(value = { "savings", "create_date", "modified_date" })
    private Set<User> users;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "join_id", referencedColumnName = "id")
    @JsonProperty("join_saving")
    @JsonIgnoreProperties(value = { "saving", "id", "create_date", "modified_date" })
    private JoinSaving joinSaving;
}
