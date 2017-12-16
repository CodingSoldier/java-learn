package ssm.projectweb.fileupdown.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import ssm.utils.OutPutData;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2017/12/16
 */
@Controller
@RequestMapping("/fileUpDown")
public class FileUpDown {
    public static final String FILE_ROOT_DIR = "E:\\workspace\\picture\\";  //tomcat中配置的文件服务器根目录
    public static final String FILE_ROOT_REQUEST = "/file";  //访问根目录
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
            file1.transferTo(new File(serverPath));  //图片写入磁盘
            pathMap.put("path",requestPath);
        }


        outPutData.setSuccess(true);
        outPutData.setData(pathMap);
        return outPutData;
    }
}
