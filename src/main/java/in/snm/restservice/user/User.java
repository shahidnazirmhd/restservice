package in.snm.restservice.user;

import in.snm.restservice.token.Token;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "appuser")
public class User implements UserDetails {
    @Id
    @SequenceGenerator(name = "appuser_seq", sequenceName = "appuser_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appuser_seq")
    @Column(updatable = false)
    private Long id;
    private String name;
    private String surname;
    @Column(unique = true, nullable = false)
    private String uid;
    @Column(name = "mobileno", unique = true,  nullable = false)
    private String mobileNo;
    @Column(unique = true)
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(nullable = false)
    private String password;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime created;
    @Column(name = "signedin")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime signedIn;
    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return mobileNo;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
