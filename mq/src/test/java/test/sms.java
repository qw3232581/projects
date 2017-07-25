package test;

import com.aliyuncs.sendsms.SmsSystem;

public class sms {
	public static void main(String[] args) throws Exception {
		SmsSystem.sendMessage("123456", "15021798921");
	}
}
