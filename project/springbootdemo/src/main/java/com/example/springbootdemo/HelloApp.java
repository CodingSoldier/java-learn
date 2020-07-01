package com.example.springbootdemo;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chenpiqian
 * @date: 2020-06-30
 */
public class HelloApp {


    public static void main(String[] args) {
        KieContainer kContainer = null;
        try {
            KieServices ks = KieServices.Factory.get();
            kContainer = ks.getKieClasspathContainer();
            KieSession kSession = kContainer.newKieSession("ksession-hello");

            Message message1 = new Message(Message.MessageType.HI, "杨过");
            kSession.insert(message1);
            kSession.fireAllRules();

            Message message2 = new Message(Message.MessageType.GOODBYE, "姑姑");
            kSession.insert(message2);
            kSession.fireAllRules();

            Message message3 = new Message(Message.MessageType.CHAT, "美羊羊");
            kSession.insert(message3);
            kSession.fireAllRules();

            //Message message34 = new Message(Message.MessageType.CHAT, "beggar");
            //kSession.setGlobal("temp", "我是谁？我在哪？我要干什么？");
            //kSession.insert(message34);
            //kSession.fireAllRules();

            Message message4 = new Message(null, "beggar");
            kSession.setGlobal("temp", "我是谁？我在哪？我要干什么？");
            kSession.insert(message4);
            kSession.fireAllRules();

            Message message5 = new Message(null, "loop");
            kSession.setGlobal("count", new AtomicInteger(0));
            kSession.insert(message5);
            kSession.fireAllRules();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (kContainer != null) {
                kContainer.dispose();
            }
        }

    }
}
