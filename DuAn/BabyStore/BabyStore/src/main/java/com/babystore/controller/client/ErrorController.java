package com.babystore.controller.client;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {


    @RequestMapping("/error")
    public String handleError(HttpServletRequest req){
        Object status = req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null){
            int statusCode = Integer.parseInt(status.toString());
            if (statusCode == 404){
                return "clients/404";
            }
        }
        return "error";
    }

    public String getErrorPath() {
        return "/error";
    }
}
