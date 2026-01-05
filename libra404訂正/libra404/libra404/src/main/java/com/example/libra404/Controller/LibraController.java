package com.example.libra404.Controller;

// import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.libra404.model.Book;
import com.example.libra404.model.Category;
import com.example.libra404.model.DispKasidasi;
import com.example.libra404.model.Kasidasi;
import com.example.libra404.model.Member;
import com.example.libra404.service.LibraService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Libra404")
public class LibraController {
    private final LibraService libraService;
    LibraController(LibraService libraService){
        this.libraService = libraService;
    }
    @Autowired
    HttpSession session;

    @GetMapping("/home")
    public String homeDisp(){
        if (session.getAttribute("login") == null){
            session.setAttribute("login", false);
        }
        session.setAttribute("blogin", "home");
        session.setAttribute("bsignup", "home");
        return "home";
    }

    @GetMapping("/loginDisp")
    public String loginDisp(Model model){
        model.addAttribute("loginForm", new Member());
        session.setAttribute("bsignup", "login");
        return "login";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute("loginForm")Member member, BindingResult result, Model model){
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>blogin::" +  (String)session.getAttribute("blogin"));
        if (result.hasErrors()){
            model.addAttribute("loginForm", member);
            return "login";
        }
 
        Map<String, String> login_info = new HashMap<>();
        login_info.put("mail", member.getMail());
        login_info.put("password", member.getPassword());

        Member member_info = libraService.findByLoginMember(login_info);
        if (member_info == null){
            model.addAttribute("loginForm", member);
            return "login";
        } else {
            // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>koko>>" + member_info.getMid() + ":" + member_info.getMname());
            session.setAttribute("loginfo", member_info);
            session.setAttribute("login", true);
            if (session.getAttribute("blogin") == null){
                session.setAttribute("blogin", "home");
            }
            String blogin = (String)session.getAttribute("blogin");
            if (blogin.equals("home")){
                return "redirect:/Libra404/home";
            } else if (blogin.equals("bookshelf")){
                return "redirect:/Libra404/bookshelf";
            } else if (blogin.equals("bdetail")){
                // ＞＞＞詳細にいくように変更
                Book book=(Book)session.getAttribute("book");
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>bdetail＞" + book.getHonid());
                model.addAttribute("title", book.getTitle());
                // return "redirect:/Libra404/bdetail";
                return "lentconf";
            } else {
                return "redirect:/Libra404/home";
            }
        }
    }

    @GetMapping("/cancelLogin")
    public String cancelLogin(){
        if (session.getAttribute("blogin") == null){
            session.setAttribute("blogin", "home");
        }
        String blogin = (String)session.getAttribute("blogin");
        if (blogin.equals("home")){
            return "redirect:/Libra404/home";
        } else if (blogin.equals("bookshelf")){
            return "redirect:/Libra404/bookshelf";
        } else if (blogin.equals("bdetail")){
            return "redirect:/Libra404/bdetail";
        } else {
            return "redirect:/Libra404/home";
        }
    }

    @GetMapping("/logout")
    public String logout(){
        session.setAttribute("login", false);
        session.setAttribute("loginfo", null);
        return "redirect:/Libra404/home";
    }

    @GetMapping("/signupDisp")
    public String signupDisp(Model model){
        model.addAttribute("signupForm", new Member());
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@Validated @ModelAttribute("signupForm")Member member, BindingResult result, Model model){
        if (result.hasErrors()){
            model.addAttribute("signupForm", member);
            return "signup";
        }
        Integer existing = libraService.findByMailMember((String)member.getMail());
        if (existing != null) {
            model.addAttribute("signupForm", member);
            model.addAttribute("err", "このアドレスは使用できません。");
            return "signup";
        }
        libraService.insertMember(member);
        session.setAttribute("login", true);
        if (session.getAttribute("blogin") == null){
            session.setAttribute("blogin", "home");
        }
        String blogin = (String)session.getAttribute("blogin");
        if (blogin.equals("home")){
            return "home";
        } else if (blogin.equals("bookshelf")){
            return "redirect:/Libra404/bookshelf";
        } else if (blogin.equals("bdetail")){
            return "redirect:/Libra404/bdetail";
        } else {
            return "home";
        }
    }

    @GetMapping("/cancelSignup")
    public String cancelSignup(){
        if (session.getAttribute("bsignup") == null){
            session.setAttribute("bsignup", "home");
        }
        String bsignup = (String)session.getAttribute("bsignup");
        if (bsignup.equals("home")){
            return "home";
        } else if (bsignup.equals("login")){
            return "redirect:/Libra404/loginDisp";
        } else {
            return "home";
        }
    }

    @GetMapping("/acinfoDisp")
    public String acinfoDisp(Model model){
        Member member = (Member)session.getAttribute("loginfo");
        model.addAttribute("member", member);
        return "acinfo";
    }

    @GetMapping("/acinfochangeDisp")
    public String acinfochangeDisp(Model model){
        Member member = (Member)session.getAttribute("loginfo");
        model.addAttribute("acinfochangeForm", member);
        return "acinfochange";
    }

    @PostMapping("/acinfochange")
    public String acinfochange(@Validated @ModelAttribute("acinfochangeForm")Member member, BindingResult result, Model model){
        if(result.hasErrors()){
            model.addAttribute("acinfochangeForm", member);
            return "acinfochange";
        }
        Member tempmember = (Member)session.getAttribute("loginfo");
        member.setMid(tempmember.getMid());
        libraService.updateMember(member);
        session.setAttribute("loginfo", member);
        return "redirect:/Libra404/acinfoDisp";
    }

    @GetMapping("/cancel")
    public String cancel(){
        String blogin = (String)session.getAttribute("blogin");
        if (blogin.equals("home")){
            return "home";
        } else if (blogin.equals("bookshelf")){
            return "redirect:/Libra404/bookshelf";
        } else if (blogin.equals("bdetail")){
            return "redirect:/Libra404/bdetail";
        } else {
            return "home";
        }
    }

    //メソッドの中身を記述
    @GetMapping("/bookshelfDisp")
    public String bookshelfDisp(Model model){
        List<Kasidasi> kList = libraService.getKasidasiList();
        List<DispKasidasi> kasibList = new ArrayList<>();
        for(Kasidasi k : kList){
            DispKasidasi diskasi = new DispKasidasi();
            diskasi.setBoid(k.getBoid());
            Book btemp = libraService.findByIdBook(k.getHonid());
            diskasi.setTitle(btemp.getTitle());
            diskasi.setPhoto(btemp.getPhoto());
            diskasi.setBoday(k.getBoday());
            diskasi.setReday(k.getReday());
            kasibList.add(diskasi);
        }
        model.addAttribute("bookList", kasibList);
        return "bookshelf";
    }

    @GetMapping("/searchDisp")
    public String searchDisp(Model model){
        List<Book> bookList = libraService.getBookList();
        model.addAttribute("bookList", bookList);
        List<Category> categoryList = libraService.getCategoryList();
        model.addAttribute("cList", categoryList);
        return "search";
    }

    @GetMapping("/search/filter")
    public String searchFilter(@RequestParam("cid") int cid, @RequestParam(name="word", required = false) String word, Model model){
        if (word == null){
            word = "";
        }
        List<Book> bookList = new ArrayList<>();
        bookList = libraService.searchBook(word, cid);
        model.addAttribute("bookList", bookList);
        List<Category> categoryList = libraService.getCategoryList();
        model.addAttribute("cList", categoryList);
        model.addAttribute("selectCid", cid);
        model.addAttribute("word", word);
        return "search";
    }

    @GetMapping("/bdetail")
    public String bookDetail(@RequestParam("honid") int honid, Model model) {
        Book book = libraService.findByIdBook(honid);
        // ここで書籍情報をsessionに入れました
        session.setAttribute("book", book);
        model.addAttribute("book", book);
        return "bdetail";
    }

    // @GetMapping("/lentconfDisp")
    // public String letconfDisp(@RequestParam("honid") int honid, Model model){
    //     if (!(Boolean)session.getAttribute("login")){
    //         session.setAttribute("blogin", "bdetail");
    //         return "redirect:/Libra404/loginDisp";
    //     }
    //     Book book = libraService.findByIdBook(honid);
    //     model.addAttribute("honid", book.getHonid());
    //     model.addAttribute("title", book.getTitle());
    //     return "lentconf";
    // }

    //以下のメソッドは書き直しました
    @GetMapping("/lentcomp")
    public String letconp(Model model){
        System.out.println("貸し出すよ・・・・・・・・・・・・・・・・");
        int l_honid=(int)session.getAttribute("honid");
        Book book = (Book)session.getAttribute("book");
        //loginfoが空・・・・・
        Member member = (Member)session.getAttribute("loginfo");
        int mid=member.getMid();
        //今日の日付の取得
        Date today = new Date(Calendar.getInstance().getTimeInMillis());
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>日付：" + today);

        Kasidasi kasidasi=new Kasidasi();
        kasidasi.setBoday(today);
        kasidasi.setMid(mid);
        kasidasi.setHonid(l_honid);

        libraService.lentBook(kasidasi);
        libraService.updateBookLent(l_honid);

        model.addAttribute("title", book.getTitle());
        return "lentcomp";
    }
}
