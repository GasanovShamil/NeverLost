package org.dant.services;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;
import com.pusher.rest.Pusher;

@Stateless
public class Sender {
	public static String APP_KEY = "5102cb4079a2a7367004";
	public static String APP_SECRET = "1a09ce5f0d01611a3344";
	public static String APP_ID = "195820";
	public static String APP_CLUSTER = "eu";
	Pusher pusher;
	
	
	public Sender(){
		pusher = new Pusher(APP_ID, APP_KEY, APP_SECRET);
		pusher.setCluster(APP_CLUSTER);
		pusher.setEncrypted(true);
		
	}
	
	
	public void sendPosToFriends(List<String> channels, String lon, String lat ){
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("lon", lon);
		data.put("lat", lat);
		pusher.trigger(channels, "coordinates", data);
		
	}
	public void sendToOne(String channel, String event, String message){
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("message", message);
		pusher.trigger(channel, event, data);
		
	}
}
