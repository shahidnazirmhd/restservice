package in.snm.restservice.user;

import in.snm.restservice.user.model.UserRequest;
import in.snm.restservice.user.model.UserDataRequestResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping
    public ResponseEntity<UserDataRequestResponse> getUser(@Valid @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userService.getUserData(userRequest.username()));
    }

    @PutMapping
    public ResponseEntity<UserDataRequestResponse> updateUser(@Valid @RequestBody UserDataRequestResponse UserDataRequestResponse) {
        return ResponseEntity.ok(userService.updateUserData(UserDataRequestResponse));
    }
}
