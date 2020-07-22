package myCredit.service;

import myCredit.domain.Role;
import myCredit.domain.User;
import myCredit.domain.UserFacebook;
import myCredit.domain.UserGoogle;
import myCredit.repository.RoleRepository;
import myCredit.repository.UserFacebookRepository;
import myCredit.repository.UserGoogleRepository;
import myCredit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Service
@Transactional
public class  UserService implements UserDetailsService {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;
    @Autowired
    UserGoogleRepository userGoogleRepository;
    @Autowired
    UserFacebookRepository userFacebookRepository;

    private static String authorizationRequestBaseUri
            = "oauth2/authorization";
    Map<String, String> oauth2AuthenticationUrls
            = new HashMap<>();

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    public User getUser(Integer id) {
        return userRepository.findById(id).get();
    }

    public User getUserByUsername(String username){return  userRepository.findByUsername(username);}

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public boolean saveUser(User user) {
        User byUsername = userRepository.findByUsername(user.getUsername());
        User byEmail = userRepository.findByEmail(user.getEmail());
        if (byUsername != null || byEmail != null ) {
            return false;
        }

        user.setRoles(Collections.singleton(new Role(1, "ROLE_USER")));
        user.setPicture("https://i.ibb.co/DtHCdc6/account.png");
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    public List<User> listAll() {
        List<User> list = new ArrayList<>();
        userRepository.findAll().forEach(list::add);
        return list;
    }

    public void deleteUser(Integer id){
        userRepository.deleteById(id);
    }

    public List<User> listAllUsers() {
        List<User> list = new ArrayList<>();
        userRepository.findAll().forEach(list::add);
        return list;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }



    public UserFacebook getUserFacebook(@AuthenticationPrincipal OAuth2AuthenticationToken authenticationToken){
        OAuth2User user = authenticationToken.getPrincipal();
        UserFacebook userFacebook = userFacebookRepository.findByIdOauth(user.getName());
        User byEmail = userRepository.findByEmail(user.getAttribute("email"));
      if  (userFacebook!= null) {
          userFacebook = userFacebook;
      } else{
        userFacebook = new UserFacebook();
        userFacebook.setIdOauth(user.getName());
        userFacebook.setName(user.getAttribute("name"));
        userFacebook.setEmail(user.getAttribute("email"));
        userFacebook.setPicture("https://graph.facebook.com/" + user.getName() + "/picture?type=normal");
            if (byEmail!=null){userFacebook.setUser(byEmail);}
            else {
                User userDet = new User();
                userDet.setEmail(user.getAttribute("email"));
                userDet.setPicture("https://graph.facebook.com/" + user.getName() + "/picture?type=normal");
                userDet.setUsername(user.getAttribute("email"));
                userDet.setPassword(user.getName());
                saveUser(userDet);
                userFacebook.setUser(userDet);
            }
        userFacebookRepository.save(userFacebook);
        }
        System.out.println(userFacebook);
        return userFacebook;
    }

    public UserGoogle getUserGoogle(@AuthenticationPrincipal OAuth2AuthenticationToken authenticationToken){
        OAuth2User user = authenticationToken.getPrincipal();
        User byEmail = userRepository.findByEmail(user.getAttribute("email"));
        UserGoogle userGoogle = userGoogleRepository.findByIdOauth(user.getName());
        if (userGoogle!= null){
            userGoogle = userGoogle;
        }
        else
        {
            userGoogle = new UserGoogle();
            userGoogle.setIdOauth(user.getName());
            userGoogle.setName(user.getAttribute("name"));
            userGoogle.setEmail(user.getAttribute("email"));
            userGoogle.setPicture(user.getAttribute("picture"));
            if (byEmail!=null){userGoogle.setUser(byEmail);}
            else {
                User userDet = new User();
                userDet.setEmail(user.getAttribute("email"));
                userDet.setPicture("https://graph.facebook.com/" + user.getName() + "/picture?type=normal");
                userDet.setUsername(user.getAttribute("email"));
                userDet.setPassword(user.getName());
                saveUser(userDet);
                userGoogle.setUser(userDet);
            }
            userGoogleRepository.save(userGoogle);
        }
        System.out.println(userGoogle);
        return userGoogle;
    }


}
