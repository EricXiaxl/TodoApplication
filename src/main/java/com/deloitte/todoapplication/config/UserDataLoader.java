package com.deloitte.todoapplication.config;

import com.deloitte.todoapplication.dao.RoleDao;
import com.deloitte.todoapplication.dao.UserDao;
import com.deloitte.todoapplication.pojo.Role;
import com.deloitte.todoapplication.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserDataLoader implements CommandLineRunner {

    @Autowired
    UserDao userDao;
    @Autowired
    RoleDao roleDao;
    @Autowired
    PasswordEncoder passwordEncoder;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDataLoader.class);

    @Override
    public void run(String... args) throws Exception {
        loadUserSeedData();
    }

    /**
     * Initialize database data
     */
    private void loadUserSeedData(){
        if (userDao.count() == 0) {
            User user1 = new User("test1",
                    passwordEncoder.encode("pwd123"),"test1@gmail.com","0892366111");
            User user2 = new User("test2",
                    passwordEncoder.encode("pwd123"),"test2@gmail.com","0892366222");

            userDao.save(user1);
            userDao.save(user2);
        }

        if (roleDao.count() == 0){
            Role role1 = new Role("FREE","FREE","");
            Role role2 = new Role("PRO","PRO","");
            Role role3 = new Role("ENT","ENTERPRISE","");

            roleDao.save(role1);
            roleDao.save(role2);
            roleDao.save(role3);
        }
    }
}
