package myCredit.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "users")
public class User  implements UserDetails {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Integer id;

        String username;
        String password;
        String picture;
        String email;


        @ToString.Exclude
        @OneToOne
        UserGoogle userGoogle;

        @ToString.Exclude
        @OneToOne
        UserFacebook userFacebook;

        @Enumerated(EnumType.STRING)
        @ManyToMany(fetch = FetchType.EAGER)
        Set<Role> roles;

        @ToString.Exclude
        @JsonManagedReference
        @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
        List<Person> persons;

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
                return getRoles();
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
