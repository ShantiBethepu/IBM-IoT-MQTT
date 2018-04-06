package com.ociweb.ibm;

import com.ociweb.gl.api.GreenRuntime;

public class GreenLightning {

	public static void main(String[] args) {
		GreenRuntime.run(new IBMMQTT(),args);
	}
	
}
