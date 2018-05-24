package ssm.fileupdown.controller;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import ssm.fileupdown.mapper.ImgTestMapper;
import ssm.fileupdown.model.ImgTest;
import ssm.utils.OutPutData;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/fileUpDown")
public class FileUpDown {
    public static final String FILE_ROOT_DIR = "E:\\workspace\\picture\\";  //tomcat中配置的文件服务器根目录
    public static final String FILE_ROOT_REQUEST = "/file";  //访问根目录

    @Autowired
    ImgTestMapper imgTestMapper;

    /**
     * String getContentType()//获取文件MIME类型
     * InputStream getInputStream()//获取去文件流
     * String getName() //获取表单中文件组件的名字
     * String getOriginalFilename() //获取上传文件的原名
     * long getSize()  //获取文件的字节大小，单位byte
     * boolean isEmpty() //是否为空
     * void transferTo(File dest) //保存到一个目标文件中。
     */
    @RequestMapping("jqFileUpload/one")
    @ResponseBody
    public OutPutData jqFileUploadOne(HttpServletRequest request, MultipartFile file1) throws Exception {
        OutPutData outPutData = new OutPutData();
        //图片上传功能
        String picDir = "dome1\\";  //图片在磁盘的路径
        String originalFileName = file1.getOriginalFilename();
        Map<String, String> pathMap = new HashMap<String, String>();
        if(file1 != null && originalFileName != null && originalFileName.length() > 0)
        {
            //定义一个新图片名称防止图片目录中的图片重名
            String newFileName = UUID.randomUUID() + originalFileName.substring(originalFileName.lastIndexOf("."));
            String serverPath = FILE_ROOT_DIR + picDir + newFileName;
            String requestPath = FILE_ROOT_REQUEST + "/" + picDir + newFileName;
            File file = new File(serverPath);
            if(file.exists() == false){
                file.mkdirs();
            }
            file1.transferTo(file);  //图片写入磁盘
            pathMap.put("path",requestPath);
        }

        outPutData.setSuccess(true);
        outPutData.setData(pathMap);
        return outPutData;
    }

    @RequestMapping("/down01")
    public ResponseEntity<byte[]> down01() throws Exception{
        String path = "E:\\workspace\\picture\\dome1\\8df3c863-4e96-4150-8881-e14d4c9e1084.jpg";
        File file = new File(path);
        HttpHeaders headers = new HttpHeaders();
        String fileName = new String("图片01.png".getBytes("utf-8"), "iso-8859-1");
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
    }

    //图片存储到数据库
    @RequestMapping("jqFileUpload/toDatabase")
    @ResponseBody
    public OutPutData toDatabase(HttpServletRequest request, MultipartFile file2) throws Exception {
        OutPutData outPutData = new OutPutData();
        ImgTest imgTest = new ImgTest();
        imgTest.setName(file2.getOriginalFilename());
        imgTest.setPic(file2.getBytes());
        imgTestMapper.insert(imgTest);
        outPutData.setSuccess(true);
        return outPutData;
    }

    /**
     * 1、form设置enctype="multipart/form-data"
     * 2、$.ajax  type: "post", contentType: false, processData: false
     * 3、MultipartFile参数名和Vo类参数名不能相同，要另起一个名字接收
     */
    @RequestMapping("/formData")
    @ResponseBody
    public OutPutData formData(HttpServletRequest request, MultipartFile picFile, ImgTest imgTest) throws Exception {
        OutPutData outPutData = new OutPutData();
        System.out.println(imgTest);
        outPutData.setSuccess(true);
        return outPutData;
    }

    /**
     * 多文件上传使用MultipartFile[]来接受
     * 前台input设置multiple
     */
    @RequestMapping("/formData2")
    @ResponseBody
    public OutPutData formData2(HttpServletRequest request, MultipartFile[] picFile, ImgTest imgTest) throws Exception {
        OutPutData outPutData = new OutPutData();
        System.out.println(imgTest);
        outPutData.setSuccess(true);
        return outPutData;
    }

}
