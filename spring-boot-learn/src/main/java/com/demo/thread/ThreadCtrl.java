//package com.demo.thread;
//
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//
//@RestController
//@RequestMapping("thread")
//public class ThreadCtrl {
//    IdleConnectionMonitorThread it1 = new IdleConnectionMonitorThread();
//    IdleConnectionMonitorThread it2 = new IdleConnectionMonitorThread();
//    IdleConnectionMonitorThread it3 = new IdleConnectionMonitorThread();
//    IdleConnectionMonitorThread it4 = new IdleConnectionMonitorThread();
//    IdleConnectionMonitorThread it5 = new IdleConnectionMonitorThread();
//
//    @GetMapping("/start1")
//    public Object start1(HttpServletRequest request) throws Exception{
//        it1.start();
//        return "start1";
//    }
//
//    @GetMapping("/shutdown1")
//    public Object p1(HttpServletRequest request) throws Exception{
//        //Map<String, Object> map1 = new HashMap<>();
//        //IdleConnectionMonitorThread it = new IdleConnectionMonitorThread();
//        it1.shutdown();
//        return "/shutdown1";
//    }
//
//    @GetMapping("/start2")
//    public Object start2(HttpServletRequest request) throws Exception{
//        it2.start();
//        return "start2";
//    }
//
//    @GetMapping("/shutdown2")
//    public Object shutdown2(HttpServletRequest request) throws Exception{
//        //Map<String, Object> map1 = new HashMap<>();
//        //IdleConnectionMonitorThread it = new IdleConnectionMonitorThread();
//        it2.shutdown();
//        return "/shutdown2";
//    }
//
//    @GetMapping("/start3")
//    public Object start3(HttpServletRequest request) throws Exception{
//        it3.start();
//        return "start3";
//    }
//
//    @GetMapping("/shutdown3")
//    public Object p3(HttpServletRequest request) throws Exception{
//        //Map<String, Object> map3 = new HashMap<>();
//        //IdleConnectionMonitorThread it = new IdleConnectionMonitorThread();
//        it3.shutdown();
//        return "/shutdown3";
//    }
//
//    @GetMapping("/start4")
//    public Object start4(HttpServletRequest request) throws Exception{
//        it4.start();
//        return "start4";
//    }
//
//    @GetMapping("/shutdown4")
//    public Object p4(HttpServletRequest request) throws Exception{
//        //Map<String, Object> map4 = new HashMap<>();
//        //IdleConnectionMonitorThread it = new IdleConnectionMonitorThread();
//        it4.shutdown();
//        return "/shutdown4";
//    }
//
//}
