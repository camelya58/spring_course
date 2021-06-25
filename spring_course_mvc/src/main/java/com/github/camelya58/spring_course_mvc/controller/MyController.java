package com.github.camelya58.spring_course_mvc.controller;

import com.github.camelya58.spring_course_mvc.Employee;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MyController {

    @RequestMapping("/")
    public String showFirstView() {
        return "first_view";
    }

//    @RequestMapping("/askDetails")
//    public String askEmployeeDetails() {
//        return "ask_emp_details_view";
//    }

    @RequestMapping("/askDetails")
    public String askEmployeeDetails(Model model) {

        model.addAttribute("employee", new Employee());
        return "ask_emp_details_view";
    }

//    @RequestMapping("/showDetails")
//    public String showEmployeeDetails() {
//        return "show_emp_details_view";
//    }

//    @RequestMapping("/showDetails")
//    public String showEmployeeDetails(HttpServletRequest request, Model model) {
//        String empName = request.getParameter("employeeName");
//        empName = "Mrs. " + empName;
//        model.addAttribute("nameAttribute", empName);
//        model.addAttribute("description", " - java developer");
//        return "show_emp_details_view";
//    }

//    @RequestMapping("/showDetails")
//    public String showEmployeeDetails(@RequestParam("employeeName") String empName, Model model) {
//        empName = "Mrs. " + empName;
//        model.addAttribute("nameAttribute", empName);
//        model.addAttribute("description", " - java developer");
//        return "show_emp_details_view";
//    }

    @RequestMapping("/showDetails")
    public String showEmployeeDetails(@Valid @ModelAttribute("employee") Employee emp, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "ask_emp_details_view";
        } else {
            return "show_emp_details_view";
        }
    }
}
