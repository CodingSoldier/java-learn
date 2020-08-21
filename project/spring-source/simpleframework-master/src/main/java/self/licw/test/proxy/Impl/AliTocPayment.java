package self.licw.test.proxy.Impl;

import self.licw.test.proxy.ToCPayment;

public class AliTocPayment implements ToCPayment {
    ToCPayment toCPayment;
    public AliTocPayment(ToCPayment toCPayment){
        this.toCPayment = toCPayment;
    }

    @Override
    public void pay() {
        before();
        toCPayment.pay();
        after();
    }

    private void after() {
        System.out.println("银行取款");
    }

    private void before() {
        System.out.println("给淘宝");
    }
}
