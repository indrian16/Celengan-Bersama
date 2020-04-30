package io.indrian.celenganbersama.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "savings")
@Setter
@Getter
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
    @JsonIgnore
    private Set<User> users;

    @OneToOne
    @JoinColumn(name = "join_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = { "saving", "id", "create_date", "modified_date" })
    @JsonProperty("join_saving")
    private JoinSaving joinSaving;

    @OneToMany(mappedBy = "saving")
    @JsonIgnore
    private List<Income> incomes = new ArrayList<>();

    @JsonProperty("balance")
    public Double getBalance() {

        double totalSaving = 0.0;
        if (!incomes.isEmpty()) {

            totalSaving = incomes.stream()
                    .mapToDouble(Income::getAmount)
                    .sum();
        }
        return totalSaving;
    }
}
