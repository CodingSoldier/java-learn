package ssm.projectnote.spring.aop.d.service;

import org.springframework.stereotype.Service;

@Service
public class DServiceImpl implements DService {
    @Override
    public void addUser() {
        System.out.println("userService.addUser");
    }
}
