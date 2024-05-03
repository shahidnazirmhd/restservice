package in.snm.restservice.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRegisterResponse {
    @JsonProperty("app_user_id")
    private Long appUserId;
    private String message;
}
