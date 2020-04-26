package io.indrian.celenganbersama.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.indrian.celenganbersama.utils.DateUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class AuditModel {

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = false, updatable = false)
    @CreatedDate
    @JsonIgnore
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_date", nullable = false)
    @LastModifiedDate
    @JsonIgnore
    private Date modifiedDate;


    @JsonProperty("create_date")
    public String showCreateDate() {

        return DateUtils.dateToString(createDate);
    }

    @JsonProperty("modified_date")
    public String showModifiedDate() {

        return DateUtils.dateToString(modifiedDate);
    }
}
