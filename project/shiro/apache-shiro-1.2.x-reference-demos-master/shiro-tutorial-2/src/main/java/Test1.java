import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;


public class Test1 {

    public static void main(String[] args) {
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        Subject currentUser = SecurityUtils.getSubject();

        Session session = currentUser.getSession();
        session.setAttribute("someKey", "aValue");
        String value = String.valueOf(session.getAttribute("someKey"));
        System.out.println(value);

        if (!currentUser.isAuthenticated()){
            UsernamePasswordToken token = new UsernamePasswordToken("lonestarr", "vespa");
            token.setRememberMe(true);

            currentUser.login(token);
            System.out.println(currentUser.getPrincipals());

            System.out.println( currentUser.hasRole("schwartz") );
            System.out.println( currentUser.isPermitted("lightsaber:weild") );
            System.out.println( currentUser.isPermitted("winnebago:drive:eagle5") );

            currentUser.logout();
        }

    }

}
