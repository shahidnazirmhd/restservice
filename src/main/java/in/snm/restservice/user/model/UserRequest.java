package in.snm.restservice.user.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserRequest(
        @NotBlank(message = "is required")
        @Pattern(regexp = "^[+]?[0-9]{1,3}[-\\s.]?[(]?[0-9]{1,4}[)]?[-\\s.]?[0-9]{1,4}[-\\s.]?[0-9]{1,4}$")
        String username
) {}
