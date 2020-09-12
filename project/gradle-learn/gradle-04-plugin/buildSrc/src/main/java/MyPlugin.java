import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class MyPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        for (int i=0; i<5; i++){
            project.task("task"+i, (it)->{
                System.out.println("###输出："+it);
            });
        }
    }
}
