package io.indrian.celenganbersama.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.indrian.celenganbersama.utils.RupiahCurrencyUtils;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "expenses")
@Getter
@Setter
public class Expense extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Double amount;

    @Lob
    private String note;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "saving_id", referencedColumnName = "id")
    @JsonIgnore
    private Saving saving;

    @JsonProperty("user")
    public String getUser() {

        return user.getDisplayName();
    }

    @JsonProperty("saving")
    public String getSaving() {

        return saving.getName();
    }

    @JsonProperty("display_amount")
    public String showAmount() {

        return RupiahCurrencyUtils.toRupiah(amount);
    }
}
