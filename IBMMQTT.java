package com.ociweb.ibm;

import com.ociweb.gl.api.*;
import com.ociweb.pronghorn.network.TLSCertificates;

public class IBMMQTT implements GreenApp
{
    MQTTBridge mqttBridge;
    String host="gysmrx.messaging.internetofthings.ibmcloud.com";
    int port=8883;
    String CliendID="d:gysmrx:pi:mqttdevice";
    String topic="iot-2/evt/status/fmt/json";

    @Override
    public void declareConfiguration(Builder builder)
    {
       // builder.useInsecureNetClient();
        mqttBridge=builder.useMQTT(host,port,CliendID)
               .cleanSession(true)
                .authentication("use-token-auth", "nopassword", new TLSCertificates() {
                    @Override
                    public String keyStoreResourceName() {
                        return "/certificate/IBMCertandKey.p12";
                    }

                    @Override
                    public String trustStroreResourceName() {
                        return "/certificate/IBMtruststore.jks";
                    }

                    @Override
                    public String keyStorePassword() {
                        return "nopassword";
                    }

                    @Override
                    public String keyPassword() {
                        return "nopassword";
                    }

                    @Override
                    public boolean trustAllCerts() {
                        return false;
                    }
                });
        //.authentication("use-token-auth","nopassword");
    }
    @Override
    public void declareBehavior(GreenRuntime runtime) {
        System.out.println("-------------- begin Declare behavior--------------");
        MQTTQoS QoS=MQTTQoS.atLeastOnce;
        String publishtopic=topic;
        String subscribetopic="";

        //Publish the topic
        Publish publish=new Publish(runtime,publishtopic);
        runtime.registerListener(publish);
        runtime.bridgeTransmission(publishtopic,mqttBridge);

        //Subscribe the topic
       /* Subscribe subscribe=new Subscribe(runtime,subscribetopic);
        runtime.registerListener(subscribe).addSubscription(subscribetopic,subscribe::recvmesg);
        runtime.bridgeTransmission(subscribetopic,mqttBridge);
        runtime.bridgeSubscription(subscribetopic,mqttBridge).setQoS(QoS);*/
        System.out.println("-------------- end Declare behavior--------------");
    }
}
