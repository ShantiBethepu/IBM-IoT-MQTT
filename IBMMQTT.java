package com.ociweb.ibm;

import com.ociweb.gl.api.*;
import com.ociweb.pronghorn.network.TLSCertificates;

public class IBMMQTT implements GreenApp
{
    MQTTBridge mqttBridge;
    String host="<orgid>.messaging.internetofthings.ibmcloud.com"; //orgid is the organization ID of your IBM IoT console
    int port=8883;
    String CliendID="d:<orgid>:<devicetype>:<devicename>"; //tip: this is easy to find if we create a dashboard for devices(device info)
    String topic="iot-2/evt/status/fmt/json";

    @Override
    public void declareConfiguration(Builder builder)
    {
       // builder.useInsecureNetClient();
        mqttBridge=builder.useMQTT(host,port,CliendID)
               .cleanSession(true)
                .authentication("<username>", "<password>", new TLSCertificates() { //username is "use-token-auth" and password is the token created for device
                    @Override
                    public String keyStoreResourceName() {
                        return "/certificate/<your cert name>"; // .jks/.p12 files are supported. Certs are needed to be put in the resources/certificate folder
                    }

                    @Override
                    public String trustStroreResourceName() {
                        return "/certificate/<your truststore name>"; //.jks files are supported. Certs are needed to be put in the resources/certificate folder
                    }

                    @Override
                    public String keyStorePassword() {
                        return "<your keystore password>";
                    }

                    @Override
                    public String keyPassword() {
                        return "<your truststore password>";
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
