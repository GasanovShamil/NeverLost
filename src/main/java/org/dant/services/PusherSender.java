package org.dant.services;

import java.util.ArrayList;
import java.util.HashMap;
import javax.ejb.Stateless;
import com.pusher.rest.Pusher;

@Stateless
public class PusherSender {
	public final String APP_KEY = "5102cb4079a2a7367004";
	public final String APP_SECRET = "1a09ce5f0d01611a3344";
	public final String APP_ID = "195820";
	public final String APP_CLUSTER = "eu";
	
	public static String APP_KEY_V2 = "badd279471eca66c0f77";
	public static String APP_SECRET_V2 = "bb0cd4baa5fc0cedcf99";
	public static String APP_ID_V2 = "210964";
	
	Pusher pusher;

	public PusherSender() {
		pusher = new Pusher(APP_ID_V2, APP_KEY_V2, APP_SECRET_V2);
		//pusher.setCluster(APP_CLUSTER);
		pusher.setEncrypted(true);

	}

	public void send(ArrayList<String> channels, String event, HashMap<String, String> data) {

		pusher.trigger(channels, event, data);

	}
}
