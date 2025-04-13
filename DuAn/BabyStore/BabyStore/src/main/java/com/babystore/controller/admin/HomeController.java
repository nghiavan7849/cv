package com.babystore.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller(value = "homeControllerOfAdmin")
public class HomeController {
    @RequestMapping("/admin/home-page-1")
    public String showHomePage1(){
        return "admins/index1";
    }
    
    @RequestMapping("/admin/home-page-2")
    public String showHomePage2(){
        return "admins/index2";
    }
    
    @RequestMapping("/admin/home-page-3")
    public String showHomePage3(){
        return "admins/index3";
    }
    
    @RequestMapping("/admin/widgets")
    public String widget(){
        return "admins/widgets";
    }
    
    @RequestMapping("/admin/chartjs")
    public String chartjs(){
        return "admins/charts/chartjs";
    }
    
    @RequestMapping("/admin/ui/general")
    public String uiGeneral(){
        return "admins/UI/general";
    }
    
    @RequestMapping("/admin/ui/button")
    public String uiButton(){
        return "admins/UI/button";
    }
    
    @RequestMapping("/admin/ui/modal")
    public String uiModal(){
        return "admins/UI/modalAndAlert";
    }
    
    @RequestMapping("/admin/forms/general")
    public String formsGeneral(){
        return "admins/forms/general";
    }
    
    @RequestMapping("/admin/tables/data")
    public String tablesData(){
        return "admins/tables/dataTables";
    }
    
    @RequestMapping("/admin/templates/invoice")
    public String templatesInvoice(){
        return "admins/TEMPLATES/invoice";
    }
    
    
}
