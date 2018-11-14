package com.util.chatone;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HController {

    @RequestMapping("/hello")
    public String index() {
        System.out.print("===========成功=========");
        return "Hello World";
    }

}