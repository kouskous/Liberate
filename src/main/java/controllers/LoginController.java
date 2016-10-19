/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author zekri
 */
@Controller
public class LoginController {
    @RequestMapping(value="/login", method = RequestMethod.GET)
    public String index(){
        return "login";
    }
    
 /** utilisation de JSON
    @ResponseBody @RequestMapping(value="/login", method = RequestMethod.GET,produces = "application/json") 
    public String login(){ 
    return "login"; 
     }
  */   
    
}
