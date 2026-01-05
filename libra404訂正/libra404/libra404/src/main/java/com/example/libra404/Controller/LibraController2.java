package com.example.libra404.Controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;

import com.example.libra404.model.Book;
import com.example.libra404.model.DispKasidasi;
import com.example.libra404.model.Member;
import com.example.libra404.service.LibraService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Libra404")
public class LibraController2 {
    private final LibraService libraService;
    LibraController2(LibraService libraService){
        this.libraService = libraService;
    }
    @Autowired
    HttpSession session;

    @GetMapping("/lentconfDisp/{honid}")
    public String rent(@PathVariable("honid") int honid,Model model){
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>lhonid>" + honid);
        session.setAttribute("honid", honid);
        session.setAttribute("login_info", honid);

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>blogin>" + session.getAttribute("blogin") );
        if (!(Boolean)session.getAttribute("login")){
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" + (Boolean)session.getAttribute("login"));
            session.setAttribute("blogin", "bdetail");
            return "redirect:/Libra404/loginDisp";
        }else{
            // Book book = libraService.findByIdBook(honid);
            Book book=(Book)session.getAttribute("book");
            model.addAttribute("title", book.getTitle());
            return "lentconf";
        }
    }

    @GetMapping("/bookshelf")
    public String hondana(Model model){
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>bookshelf" + session.getAttribute("login"));
        if (!(Boolean)session.getAttribute("login")){
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" + (Boolean)session.getAttribute("login"));
            session.setAttribute("blogin", "bookshelf");
            return "redirect:/Libra404/loginDisp";
        }else{
            Member member = (Member)session.getAttribute("loginfo");
            int mid=member.getMid();
            ArrayList<DispKasidasi> bookList = (ArrayList<DispKasidasi>) libraService.getShelfBookList(mid);
            model.addAttribute("bookList", bookList);
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>bookshelf2");
            return "bookshelf";
        }
    }
}
