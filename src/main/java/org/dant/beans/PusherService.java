package org.dant.beans;

import java.util.ArrayList;
import java.util.Collections;

import com.pusher.rest.*;
import com.pusher.rest.data.*;
import com.pusher.rest.util.*;

public class PusherService {

	final static String app_id = "195820" ;
	final static String key = "5102cb4079a2a7367004";
	final static String secret = "1a09ce5f0d01611a3344";
	
	private void envoieMessage(ArrayList<String> list,Pusher p,String message){
		p.trigger(list, "my event", Collections.singletonMap("message", message));

	}
			
	public static void main(String[] args){
		
		String url = "http://"+key+":"+secret+"@api.pusherapp.com:80/apps/"+app_id;
		Pusher pusher = new Pusher(url);
		pusher.trigger("test_channel", "my_event", Collections.singletonMap("message", "Hello World"));
		
	}
}
