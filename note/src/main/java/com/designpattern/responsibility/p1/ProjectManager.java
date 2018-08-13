package com.designpattern.responsibility.p1;

public class ProjectManager extends Handler {
    @Override
    public String handleFeeRequest(String user, double fee) {
        String str = "";
        if (fee < 500){
            if ("张三".equals(user)){
                str = String.format("成功，项目经理同意 %s 的申请，聚餐费用为：%s", user, fee);
            }else {
                str= String.format("失败，项目经理不同意 %s 的申请，聚餐费用为：%s", user, fee);
            }
        }else {
           if (getSuccessor() != null){
               return getSuccessor().handleFeeRequest(user, fee);
           }
        }
        return str;
    }
}
