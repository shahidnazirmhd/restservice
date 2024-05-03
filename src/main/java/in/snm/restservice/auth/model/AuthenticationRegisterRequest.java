package in.snm.restservice.auth.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRegisterRequest {
    @NotBlank(message = "is required")
    private String uid;
    @NotBlank(message = "is required")
    @Pattern(regexp = "^[+]?[0-9]{1,3}[-\\s.]?[(]?[0-9]{1,4}[)]?[-\\s.]?[0-9]{1,4}[-\\s.]?[0-9]{1,4}$")
    private String mobileNo;
    @NotBlank
    private String password;
}
