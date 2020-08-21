package self.licw.test.proxy.Impl;

import self.licw.test.proxy.ToCPayment;

public class ToCPaymentImpl implements ToCPayment {
    @Override
    public void pay() {
        System.out.println("用户支付");
    }
}
