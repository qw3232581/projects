package test;


import com.heima.activemq.SendValidationCode;

public class sms {
    public static void main(String[] args) throws Exception {
        SendValidationCode.sendMessage("123456", "13262885079");
    }
}
