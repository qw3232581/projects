package com.heima.activemq;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

public class SendPackagingInfo {

    public static void sendMessage(String username, String usertelephone, String addr, String stafftelephone) throws Exception {
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        String product = "Dysmsapi";
        String domain = "dysmsapi.aliyuncs.com";
        String accessKeyId = "LTAIpaAo6YNkfBIF";
        String accessKeySecret = "cpukvjW8B7NCxhWI20DZID0WFEfe9y";
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAIpaAo6YNkfBIF", "cpukvjW8B7NCxhWI20DZID0WFEfe9y");
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Dysmsapi", "dysmsapi.aliyuncs.com");
        IAcsClient acsClient = new DefaultAcsClient(profile);
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(stafftelephone);
        request.setSignName("激活码");
        request.setTemplateCode("SMS_80140029");
        request.setTemplateParam("{\"username\":\"" + username + "\",\"tel\":\"" + usertelephone + "\",\"addr\":\"" + addr + "\"}");
        request.setOutId("null");
        SendSmsResponse sendSmsResponse = (SendSmsResponse) acsClient.getAcsResponse(request);
        if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            System.out.println("短信发送成功");
        }

    }
}
