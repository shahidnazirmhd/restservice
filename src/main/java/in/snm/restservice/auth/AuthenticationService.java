package in.snm.restservice.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.snm.restservice.auth.model.AuthenticationRegisterRequest;
import in.snm.restservice.auth.model.AuthenticationRegisterResponse;
import in.snm.restservice.auth.model.AuthenticationRequest;
import in.snm.restservice.auth.model.AuthenticationResponse;
import in.snm.restservice.security.TokenService;
import in.snm.restservice.token.Token;
import in.snm.restservice.token.TokenRepository;
import in.snm.restservice.token.TokenType;
import in.snm.restservice.user.Role;
import in.snm.restservice.user.User;
import in.snm.restservice.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    public AuthenticationRegisterResponse register(AuthenticationRegisterRequest request) {
        Optional<User> userOptional  = userService.getUserByMobileNo(request.getMobileNo());
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setUid(request.getUid());
            var updatedUser = userService.saveUser(user);
            return AuthenticationRegisterResponse.builder()
                    .appUserId(updatedUser.getId())
                    .message("UPDATED")
                    .build();
        } else {
            var user = User.builder()
                    .mobileNo(request.getMobileNo())
                    .uid(request.getUid())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.USER)
                    .created(LocalDateTime.now())
                    .build();
            var savedUser = userService.saveUser(user);
            return AuthenticationRegisterResponse.builder()
                    .appUserId(savedUser.getId())
                    .message("CREATED")
                    .build();
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        var user = userService.getUserByMobileNo(request.getUsername()).orElseThrow();
        user.setSignedIn(LocalDateTime.now());  //Updating User sign in time
        var updatedUser = userService.saveUser(user);
        var jwtToken = tokenService.generateToken(updatedUser);
        var refreshToken = tokenService.generateRefreshToken(updatedUser);
        revokeAllUserTokens(updatedUser);
        saveUserToken(updatedUser, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userMobileNo;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userMobileNo = tokenService.extractUserMobileNo(refreshToken);
        if (userMobileNo != null) {
            var user = userService.getUserByMobileNo(userMobileNo)
                    .orElseThrow();
            if (tokenService.isTokenValid(refreshToken, user)) {
                var accessToken = tokenService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
