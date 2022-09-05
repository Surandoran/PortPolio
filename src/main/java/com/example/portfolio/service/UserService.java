package com.example.portfolio.service;

import com.example.portfolio.dto.Role;
import com.example.portfolio.dto.UserAccount;
import com.example.portfolio.repository.UserRepository;
import com.example.portfolio.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private void validateDuplicateMember(UserAccount userAccount){
        userRepository.findById(userAccount.getUserid())
                .ifPresent(u -> {
                    throw new IllegalStateException("이미 존재하는 아이디입니다.");
                });
    }

    //회원가입
    public Long join(UserAccount userAccount) {
        validateDuplicateMember(userAccount);
        userRepository.save(userAccount);
        return userAccount.getUserid();
    }


    //전체 회원 조회
    public List<UserAccount> findMembers() {
        return userRepository.findAll();
    }

    //멤버 찾기
    public Optional<UserAccount> findOne(Long id){
        return userRepository.findById(id);
    }


    //수정
    public UserAccount get(Long id) throws UserNotFoundException {
        Optional<UserAccount> result = userRepository.findById(id);
        if (result.isPresent()){
            return result.get();
        }
        throw new UserNotFoundException("클라우드에서 해당하는 id가 없습니다. " + id);
    }


    //삭제
    public void deleteById(Long id){
        Long count = id;
        if ( count == null || count == 0) {
            throw new UsernameNotFoundException("클라우드에서 해당하는 id가 없습니다 " + count);
        }
        userRepository.deleteById(id);
    }

    //로그인 권한
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserAccount> OuserAccount = userRepository.findByEmail(email);
        UserAccount userAccount = OuserAccount.get();

        List<GrantedAuthority> authorities = new ArrayList<>();

        if (("admin").equals(email)) {
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
            System.out.println("어드민로그인성공");
        } else {
            authorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));
            System.out.println("멤버로그인성공");
        }

        return new User(userAccount.getEmail(), userAccount.getPw(), authorities);
    }

}
