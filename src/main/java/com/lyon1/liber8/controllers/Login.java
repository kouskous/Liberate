/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyon1.liber8.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author zekri
 */
@Controller
public class Login {
    
    @RequestMapping(value="/home", method = RequestMethod.GET)
    public String index(){
        return "index";
    }
    
    @ResponseBody
    @RequestMapping(value="/login", method = RequestMethod.GET, produces = "application/json")
    public String login(){
        return "login";
    }
}
