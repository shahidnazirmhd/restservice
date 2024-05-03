package in.snm.restservice.user;

import in.snm.restservice.user.model.UserDataRequestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public Optional<User> getUserByMobileNo(String mobileNo) {
        return userRepository.findByMobileNo(mobileNo);
    }
    public User saveUser(User user) {
        return userRepository.save(user);
    }
    public UserDataRequestResponse getUserData(String mobileNo){
        User user = userRepository.findByMobileNo(mobileNo).orElseThrow();
        return UserDataRequestResponse.builder()
                .id(user.getId())
                .mobileNo(user.getMobileNo())
                .email(user.getEmail())
                .name(user.getName())
                .surname(user.getSurname())
                .build();
    }

    public UserDataRequestResponse updateUserData(UserDataRequestResponse userDataRequestResponse) {
        User user = userRepository.findByMobileNo(userDataRequestResponse.getMobileNo()).orElseThrow();
        user.setName(userDataRequestResponse.getName());
        user.setSurname(userDataRequestResponse.getSurname());
        user.setEmail(userDataRequestResponse.getEmail());
        User updatedUser = userRepository.save(user);
        return UserDataRequestResponse.builder()
                .id(updatedUser.getId())
                .mobileNo(updatedUser.getMobileNo())
                .email(updatedUser.getEmail())
                .name(updatedUser.getName())
                .surname(updatedUser.getSurname())
                .build();
    }
}
