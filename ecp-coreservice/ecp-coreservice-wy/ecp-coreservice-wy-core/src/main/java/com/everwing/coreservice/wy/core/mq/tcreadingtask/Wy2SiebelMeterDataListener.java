package com.everwing.coreservice.wy.core.mq.tcreadingtask;

import com.everwing.coreservice.wy.core.resourceDI.ServiceRrcs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service("wy2SiebelMeterDataListener")
public class Wy2SiebelMeterDataListener extends ServiceRrcs implements MessageListener{

	private static final Logger logger = LogManager.getLogger(Wy2SiebelMeterDataListener.class);
	
	@Override
	public void onMessage(Message message) {
		
		String jsonStr = null;
		
		try {
			jsonStr = new String(message.getBody(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
	}

}
