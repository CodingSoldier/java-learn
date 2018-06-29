//package com.cpq.eurekaclient.controller;
//
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestPart;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//@RestController
//public class UploadController {
//
//    @PostMapping(value="/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public String handleFileUpload(@RequestPart(value = "file")MultipartFile file){
//        return file.getName();
//    }
//
//}
