package io.indrian.celenganbersama.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "join_savings")
@Getter
@Setter
public class JoinSaving extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String code;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "limit_date")
    @JsonProperty("limit_date")
    private Date limitDate;

    @OneToOne(mappedBy = "joinSaving")
    @JsonIgnoreProperties(
            value = { "join_saving", "owner_id", "modified_date", "create_date" }
    )
    private Saving saving;
}
