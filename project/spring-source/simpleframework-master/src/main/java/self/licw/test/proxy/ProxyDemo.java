package self.licw.test.proxy;

import net.sf.cglib.proxy.MethodInterceptor;
import self.licw.test.proxy.Impl.AliTocPayment;
import self.licw.test.proxy.Impl.ToCPaymentImpl;
import self.licw.test.proxy.cglibproxy.AlipayMethodIntercepter;
import self.licw.test.proxy.cglibproxy.cglibProxyUtil;
import self.licw.test.proxy.jdkproxy.AliPayInvocationHandler;
import self.licw.test.proxy.jdkproxy.jdkProxyUtil;

import java.lang.reflect.InvocationHandler;

public class ProxyDemo {
    public static void main(String[] args) {
//        ToCPayment toCPayment = new AliTocPayment(new ToCPaymentImpl());
//        toCPayment.pay();
        ToCPayment toCPayment = new ToCPaymentImpl();
        InvocationHandler handler = new AliPayInvocationHandler(toCPayment);
        ToCPayment toCPayment1 = jdkProxyUtil.newProxyInstance(toCPayment,handler);
        toCPayment1.pay();

        CommonPay commonPay = new CommonPay();
        MethodInterceptor methodInterceptor = new AlipayMethodIntercepter();
        CommonPay commonPay1 = cglibProxyUtil.createProxy(commonPay,methodInterceptor);
        commonPay1.pay();
    }
}
