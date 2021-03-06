package site.metacoding.dbproject.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.metacoding.dbproject.domain.user.User;
import site.metacoding.dbproject.domain.user.UserRepository;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    // 서비스는 한글로 하는게 좋다.
    // 아래 스택들에 들어가는 코드를 비즈니스 로직이라고 한다.
    public String 유저네임중복검사(String username) {

        // 1. SELECT * FROM user WHERE username = "ssar";
        User userEntity = userRepository.mUsernameSameCheck(username);

        // 2. userEntity가 있으면? 없으면?
        if (userEntity == null) {
            return "없어";
        } else {
            return "있어";
        }
    }

    @Transactional
    public void 회원가입(User user) {

        // 2. 핵심로직 서비스의 역할
        userRepository.save(user);
    }

    public User 로그인(User user) {

        // 1. DB연결해서 username, password 있는지 확인
        return userRepository.mLogin(user.getUsername(), user.getPassword());
    }

    public User 유저정보보기(Integer id) {
        Optional<User> userOp = userRepository.findById(id); // 유저정보

        // 3. 핵심 로직
        if (userOp.isPresent()) { // 박스안에 뭐가 있으면
            User userEntity = userOp.get();

            return userEntity;
        } else {
            return null;
        }
    }

    @Transactional
    public User 유저수정(Integer id, User user) {
        // 1. 영속화(를 시켜야 변경감지하여 더티체킹 가능)
        Optional<User> userOp = userRepository.findById(id);

        if (userOp.isPresent()) { // 영속화 됨
            User userEntity = userOp.get();
            userEntity.setPassword(user.getPassword());
            userEntity.setEmail(user.getEmail());

            return userEntity;
        }

        return null;
    } // 2. 트랜잭션 종료 + 영속화 되어있는 것들 전부 더티체킹 (변경감지해서 DB에 flush) → UPDATE

}
