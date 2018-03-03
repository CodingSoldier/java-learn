package projectnote.spring.aop.c.service;

import org.springframework.stereotype.Service;

@Service
public class CServiceImpl implements CService {
    @Override
    public void addUser() {
        System.out.println("userService.addUser");
    }
}
