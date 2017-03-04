package net.benrowland.heatmap.security;

import net.benrowland.heatmap.entity.UserEntity;
import net.benrowland.heatmap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(userName);
        if(user == null) {
            throw new UsernameNotFoundException("could not find the user '" + userName + "'");
        }

        return new User(user.getUsername(), user.getPassword(), true, true, true, true,
            AuthorityUtils.createAuthorityList("USER"));
    }
}
