package com.thinkinginjava;

import static com.thinkinginjava.A_String.print;

public class GG_Exception {
}


class Rethrowing {
    public static void inFn() throws Exception {
        System.out.println("里层inFn()");
        throw new Exception("里层inFn() 发生异常");
    }

    public static void outNotFill() throws Exception {
        try {
            inFn();
        } catch (Exception e) {
            System.out.println("outNotFill未返回原来的异常，打印异常");
            throw e;
        }
    }

    public static void outFill() throws Exception {
        try {
            inFn();
        } catch (Exception e) {
            System.out.println("outFill返回原来的异常，打印异常");
            e.printStackTrace(System.out);
            throw (Exception) e.fillInStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            outNotFill();
        } catch (Exception e) {
            System.out.println("catch outNotFill");
            e.printStackTrace(System.out);
        }

    }
}


class OneException extends Exception {
    public OneException(String s) {
        super(s);
    }
}

class TwoException extends Exception {
    public TwoException(String s) {
        super(s);
    }
}

class RethrowNew {
    public static void throwsOneException() throws OneException {
        throw new OneException("OneException");
    }

    public static void catchOneThrowTwo() throws TwoException {
        try {
            throwsOneException();
        } catch (OneException e) {
            TwoException two = new TwoException("TwoException");

            //把two和e这两个异常连接起来
            two.initCause(e);
            throw two;
        }
    }

    public static void main(String[] args) {
        try {
            catchOneThrowTwo();
        } catch (TwoException e) {
            e.printStackTrace(System.out);
        }
    }
}






class SubRuntimeException extends RuntimeException{
    public SubRuntimeException(String s){
        super(s);
    }
}

class SubException extends Exception{
    public SubException(String s){
        super(s);
    }
}


class CheckNoCheckException {

    //RuntimeException为：不受检查异常。可以不catch、不抛出
    public static void noCheckFn(){
        throw new SubRuntimeException("可以不catch、不抛出");
    }

    //非RuntimeException的异常为：被检查的异常。要求程序员catch 或者 往上抛出
    public static void checkFn() throws SubException{
        throw new SubException("需要catch或抛出");
    }

    public static void main(String[] args) {

        try {
            checkFn();
        }catch (SubException e){
            e.printStackTrace(System.out);
        }

        System.out.println("**********catch异常后继续执行**********");

        noCheckFn();

    }
}







class DynamicFieldsException extends Exception {
}

class DynamicFields {
    private Object[][] fields;

    public DynamicFields(int initialSize) {
        fields = new Object[initialSize][2];
        for (int i = 0; i < initialSize; i++)
            fields[i] = new Object[]{null, null};
    }

    public static void main(String[] args) {
        DynamicFields df = new DynamicFields(3);
        print(df);
        try {
            df.setField("d", "A value for d");
            df.setField("number", 47);
            df.setField("number2", 48);
            print(df);
            df.setField("d", "A new value for d");
            df.setField("number3", 11);
            print("df: " + df);
            print("df.getField(\"d\") : " + df.getField("d"));
            Object field = df.setField("d", null); // Exception
        } catch (NoSuchFieldException e) {
            e.printStackTrace(System.out);
        } catch (DynamicFieldsException e) {
            e.printStackTrace(System.out);
        }
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Object[] obj : fields) {
            result.append(obj[0]);
            result.append(": ");
            result.append(obj[1]);
            result.append("\n");
        }
        return result.toString();
    }

    private int hasField(String id) {
        for (int i = 0; i < fields.length; i++)
            if (id.equals(fields[i][0]))
                return i;
        return -1;
    }

    private int getFieldNumber(String id) throws NoSuchFieldException {
        int fieldNum = hasField(id);
        if (fieldNum == -1)
            throw new NoSuchFieldException();
        return fieldNum;
    }

    private int makeField(String id) {
        for (int i = 0; i < fields.length; i++)
            if (fields[i][0] == null) {
                fields[i][0] = id;
                return i;
            }
        // No empty fields. Add one:
        Object[][] tmp = new Object[fields.length + 1][2];
        for (int i = 0; i < fields.length; i++)
            tmp[i] = fields[i];
        for (int i = fields.length; i < tmp.length; i++)
            tmp[i] = new Object[]{null, null};
        fields = tmp;
        // Recursive call with expanded fields:
        return makeField(id);
    }

    public Object getField(String id) throws NoSuchFieldException {
        return fields[getFieldNumber(id)][1];
    }

    public Object setField(String id, Object value) throws DynamicFieldsException {
        if (value == null) {
            // Most exceptions don't have a "cause" constructor.
            // In these cases you must use initCause(),
            // available in all Throwable subclasses.
            DynamicFieldsException dfe = new DynamicFieldsException();
            dfe.initCause(new NullPointerException());
            /**
             * DynamicFieldsException 执行 initCause(new NullPointerException()) 后，可以把NullPointerException和DynamicFieldsException连接起来，打印的异常堆栈如下：

             com.thinkinginjava.DynamicFieldsException
             at com.thinkinginjava.DynamicFields.setField(GG_Exception.java:184)
             at com.thinkinginjava.DynamicFields.main(GG_Exception.java:125)
             Caused by: java.lang.NullPointerException
             at com.thinkinginjava.DynamicFields.setField(GG_Exception.java:185)
             ... 1 more
             */
            throw dfe;
        }
        int fieldNumber = hasField(id);
        if (fieldNumber == -1)
            fieldNumber = makeField(id);
        Object result = null;
        try {
            result = getField(id); // Get old value
        } catch (NoSuchFieldException e) {
            // Use constructor that takes "cause":
            throw new RuntimeException(e);
        }
        fields[fieldNumber][1] = value;
        return result;
    }
}












class VeryImportantException extends Exception {
    public String toString() {
        return "A very important exception!";
    }
}

class HoHumException extends Exception {
    public String toString() {
        return "A trivial exception";
    }
}

class LostMessage {
    void f() throws VeryImportantException {
        throw new VeryImportantException();
    }
    void dispose() throws HoHumException {
        throw new HoHumException();
    }
    public static void main(String[] args) {

        //try catch中有try finally，则里面finally中的异常会导致里层try的异常丢失
        try {
            LostMessage lm = new LostMessage();
            try {
                lm.f();
            }finally {
                lm.dispose();
            }
        } catch(Exception e) {
            System.out.println(e);
        }
    }
}

























