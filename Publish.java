package com.ociweb.ibm;

import com.ociweb.gl.api.*;
import com.ociweb.pronghorn.pipe.ChannelReader;

public class Publish implements StartupListener,PubSubMethodListener
{
    GreenCommandChannel cmd;
    String publishtopic;

    Writable message = writer -> writer.append("{\"d\":{\"status\":\"Hello Watson IoT\", \"sender\":\"I'm GreenLightning\"}}");
    public Publish(GreenRuntime runtime, String topic)
    {
        System.out.println("\nOk, this is in mqttbehavior");
        cmd = runtime.newCommandChannel(DYNAMIC_MESSAGING);
        this.publishtopic = topic;
    }
    @Override
    public void startup() {
        try {
            System.out.println("\nPublishing this topic : " + publishtopic);
            cmd.publishTopic(publishtopic, message);
            System.out.println("\nSuccessfully published this message ! :  "+message+"\nCheck ibm now ");
        } catch (Exception e) {
            System.out.println("Error in startup: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public boolean recvmesg(CharSequence topic, ChannelReader reader) {
        String receivedmessage = reader.readUTFOfLength(reader.available());
        System.out.println("Message received: " + receivedmessage);
        return true;
    }
}
