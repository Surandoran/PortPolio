package com.example.portfolio.controller;


import com.example.portfolio.dto.UserAccount;
import com.example.portfolio.entity.UserEntity;
import com.example.portfolio.repository.UserRepository;
import com.example.portfolio.service.UserService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(
            @Autowired UserService userService,
            @Autowired UserRepository userRepository
            ) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/user/new")
    public String createForm(Model model) {
        model.addAttribute("useraccount", new UserAccount());

        return "sing-up";
    }

    @PostMapping("/user/new")
    public String create(UserEntity userEntity) {
        try {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            UserAccount userAccount = new UserAccount();
            userAccount.setEmail(userEntity.getEmail());
            userAccount.setPw(passwordEncoder.encode(userEntity.getPw()));
            userAccount.setName(userEntity.getName());
            userAccount.setPhone(userEntity.getPhone());
            userAccount.setAddr(userEntity.getAddr());

            System.out.println(userAccount);
            userService.save(userAccount);

            if(userAccount.getUserid() != null) {
                System.out.println(userAccount);
                return "redirect:index";
            } else {
                System.out.println(userAccount);
                return "/user/new";
            }
        } catch (Exception e) {
            return "sing-up";
        }
    }


    @RequestMapping(value = "/user/emailChk", method = {RequestMethod.POST})
    public void memberChk(HttpServletResponse res, HttpServletRequest req, Model model) throws IOException {
        //request 요청 파라미터로 넣어온 값 중 userid 를 가져와서 저장
        String email = req.getParameter("email");

        //아이디 중복 체크를 확인하기 위한 변수
        boolean result = true;

        try{
            System.out.println("email: " + email);

            //findById 로 DB에 아이디가 저장되어있는지 여부 확인
            //만약 저장되어 있다면 chkID 값에 DB 에 있는 아이디가 찾아진 후 result = false
            //아니면 데이터를 찾을 수 없어 에러가 발생할 것임
            String Chkemail = userRepository.findByEmail(email).toString();
            System.out.println("DB: " + Chkemail);
            if(Chkemail.equals(email)){
                result = false;
                System.out.println("중복된 아이디입니다" + result);
            }
            //데이터가 없어서 에러가 발생하면 try ~ catch로 잡아서 중복된 아이디가 아닌것을 확인후 result = true
        }catch (Exception e){
            result = true;
            System.out.println("가입가능한 아이디 입니다." + result);

        }
        /* Josn 방식으로 데이터를 만들고, [ result : value ] 로 만들어서 html로 넘겨줌
        이러면 ajax 스크립트가 동작하면서 result : value 를 인식하고 value 값에 따라서
        alert 를 생성함
         */
        JSONObject jso = new JSONObject();
        System.out.println("result: " + result);
        jso.put("result", result);

        //alert는 object를 띄울 수 없어서 따로 text로 변환해서 띄움
        res.setContentType("text/html;charset=utf-8");
        PrintWriter out = res.getWriter();
        out.print(jso.toString());
    }

    @GetMapping("/login")
    public String login123() {
        return "login";
    }

}
