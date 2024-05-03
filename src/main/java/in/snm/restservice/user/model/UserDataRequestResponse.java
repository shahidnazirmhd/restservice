package in.snm.restservice.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDataRequestResponse {
    @NotNull
    private Long id;
    @JsonProperty("mobile_no")
    @NotBlank
    private String mobileNo;
    @NotBlank
    private String name;
    @NotBlank
    private String surname;
    @NotBlank
    private String email;
}
