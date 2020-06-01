package org.example.service;

import org.example.entity.JwtUser;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service      //自定义认证 必须实现此接口的loadUserByUsername方法，此方法返回一个UserDetails对象（这也是一个接口，包含一些描述用户的信息）
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    //使用springSecurity需要实现UserDetailsService接口供权限框架调用，该方法只需要实现一个方法就可以了，
    // 那就是根据用户名去获取用户，那就是上面UserRepository定义的方法了，这里直接调用了。
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(s);
        return new JwtUser(user);
    }

}