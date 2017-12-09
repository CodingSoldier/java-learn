package ssm.utils.exception;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import ssm.utils.OutPutData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/* 全局异常处理器  */
public class CustomExceptionResolver implements HandlerExceptionResolver {
	public static final Logger logger = LoggerFactory.getLogger(CustomExceptionResolver.class);
	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex){

		//控制台打印异常信息
		logger.error("发生异常",ex);
		
		/*形参ex是Exception类型，editItems方法上抛出的异常也是Exception，方法内抛出的异常是CustomException
		如果ex是CustomException，则使用传进来的ex，否则自定义一个CustomException*/
		CustomException exception = ex instanceof CustomException ?
				(CustomException)ex : new CustomException("未知错误");

		/*通过url发送的请求可以跳转到错误页面，
		* 通过ajax发送的请求似乎不能跳转到错误页面*/
		//Map<String, Object> model = new HashMap<String, Object>();
		//model.put("message", ce.getMessage());
		//return new ModelAndView("error", model);

		/*返回json信息*/
		OutPutData outPutData = new OutPutData();
		outPutData.setMessage(exception.getMessage());
		outPutData.setSuccess(false);
		response.setContentType("application/text; charset=utf-8");
		try {
			PrintWriter out = response.getWriter();
			out.print(JSON.toJSONString(outPutData));
		} catch (IOException e) {
			logger.error("与客户端通讯异常:"+ e.getMessage(), e);
		}
		return new ModelAndView();
	}
}







