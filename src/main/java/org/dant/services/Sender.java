package org.dant.services;

import java.util.ArrayList;
import java.util.HashMap;
import javax.ejb.Stateless;
import com.pusher.rest.Pusher;

@Stateless
public class Sender {
	public final String APP_KEY = "5102cb4079a2a7367004";
	public final String APP_SECRET = "1a09ce5f0d01611a3344";
	public final String APP_ID = "195820";
	public final String APP_CLUSTER = "eu";
	Pusher pusher;

	public Sender() {
		pusher = new Pusher(APP_ID, APP_KEY, APP_SECRET);
		pusher.setCluster(APP_CLUSTER);
		pusher.setEncrypted(true);

	}

	public void send(ArrayList<String> channels, String event, HashMap<String, String> data) {

		pusher.trigger(channels, event, data);

	}
}
