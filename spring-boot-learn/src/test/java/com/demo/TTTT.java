package com.demo;

import com.tencentcloudapi.asr.v20190614.AsrClient;
import com.tencentcloudapi.asr.v20190614.models.SentenceRecognitionRequest;
import com.tencentcloudapi.asr.v20190614.models.SentenceRecognitionResponse;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncodingAttributes;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;

public class TTTT {


    /**
     * 将本地音频文件转换成mp3格式文件
     *
     * @param localFilePath 本地音频文件物理路径
     * @param targetPath    转换后mp3文件的物理路径
     */
    public static void changeLocalSourceToMp3(String localFilePath, String targetPath) throws Exception {

        File source = new File(localFilePath);
        File target = new File(targetPath);
        AudioAttributes audio = new AudioAttributes();
        Encoder encoder = new Encoder();

        //audio.setCodec("libmp3lame");
        audio.setBitRate(16);
        audio.setChannels(1);
        audio.setSamplingRate(16000);

        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("mp3");

        attrs.setAudioAttributes(audio);

        encoder.encode(source, target, attrs);
    }

    //@Test
    //public void changeLocalSourceToMp31() throws Exception {
    //
    //    AudioInfo audioInfo = new AudioInfo();
    //
    //    File source = new File(localFilePath);
    //    File target = new File(targetPath);
    //    AudioAttributes audio = new AudioAttributes();
    //    Encoder encoder = new Encoder();
    //
    //    audio.setCodec("libmp3lame");
    //    //audio.
    //    EncodingAttributes attrs = new EncodingAttributes();
    //    attrs.setFormat("mp3");
    //    //attrs.set
    //    attrs.setAudioAttributes(audio);
    //
    //    encoder.encode(source, target, attrs);
    //}



    @Test
    public void test0011() throws Exception{
        //采用本地语音上传方式调用
        try{
            //重要：<Your SecretId>、<Your SecretKey>需要替换成客户自己的账号信息
            //请参考接口说明中的使用步骤1进行获取。
            Credential cred = new Credential("", "");

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("asr.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            AsrClient client = new AsrClient(cred, "ap-shanghai", clientProfile);

            String params = "{\"ProjectId\":0,\"SubServiceType\":2,\"EngSerViceType\":\"16k\",\"SourceType\":1,\"Url\":\"\",\"VoiceFormat\":\"mp3\",\"UsrAudioKey\":\"session-123\"}";
            SentenceRecognitionRequest req = SentenceRecognitionRequest.fromJsonString(params, SentenceRecognitionRequest.class);


            //changeLocalSourceToMp3("D:\\lfasr.wav", "D:\\lfasr.mp3");
            changeLocalSourceToMp3("D:\\1111.m4a", "D:\\lfasr.mp3");
            File file = new File("D:\\lfasr.mp3");
            FileInputStream inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int)file.length()];
            req.setDataLen(file.length());
            inputFile.read(buffer);
            inputFile.close();
            String encodeData = Base64.encodeBase64String(buffer);
            req.setData(encodeData);

            SentenceRecognitionResponse resp = client.SentenceRecognition(req);

            System.out.println(SentenceRecognitionRequest.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }

    }
}



