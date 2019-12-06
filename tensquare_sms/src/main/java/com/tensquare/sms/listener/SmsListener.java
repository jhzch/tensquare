package com.tensquare.sms.listener;


import com.aliyuncs.exceptions.ClientException;
import com.tensquare.sms.utils.SmsUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Map;

@Component
@RabbitListener(queues = "sms")
public class SmsListener {

    @Resource
    private SmsUtil smsUtil;

    @Value("${aliyun.sms.template_code}")
    private String template_code;//短信模板名称

    @Value("${aliyun.sms.sign_name}")
    private String sign_name;//签名

    @RabbitHandler
    public void executeSms(Map<String, String> map) {
        String mobile = map.get("mobile");
        String checkCode = map.get("check_code");
        System.out.println("手机号： " + mobile);
        System.out.println("验证码： " + checkCode);
        try {
            //向用户发送验证码（阿里云短信验证）
            smsUtil.sendSms(mobile, template_code, sign_name, "{\"code\": \"" + checkCode + "\"}");
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
