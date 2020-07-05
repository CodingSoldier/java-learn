package annotation;

@CourseInfoAnnotation(courseName = "剑指java面试",
courseTag = "面试",
courseProfile = "课程内容是XXX")
public class ImoocCourse {

	@PersonInfoAnnotation(name = "翔仔", language={"java, js"})
	private String author;

	@CourseInfoAnnotation(courseName = "校园商铺",
	courseTag = "实战",
	courseProfile = "模块内容XXX",
	courseIndex = 144)
	public void getCourseInfo(){

	}

}
