package io.bluemoon.authorizationserver2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class SignController {

    @PostMapping("/middleWare")
    @ResponseBody
    private String mid(@RequestBody Map request, @RequestHeader Map header) {
        System.out.println(request);
        System.out.println(header);
        return "a";
    }
}
