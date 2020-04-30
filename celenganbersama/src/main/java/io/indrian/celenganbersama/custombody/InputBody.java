package io.indrian.celenganbersama.custombody;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class InputBody {

    @NotNull
    private Double amount;

    @Lob
    private String note;

    @NotNull
    @JsonProperty("user_id")
    private Long userId;

    @NotNull
    @JsonProperty("saving_id")
    private Long savingId;
}
