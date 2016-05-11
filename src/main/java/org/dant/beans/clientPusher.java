package org.dant.beans;

import com.pusher.client.Pusher;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.ChannelEventListener;

public class clientPusher implements ChannelEventListener{
	
	final static String app_id = "195820" ;
	final static String key = "5102cb4079a2a7367004";
	final static String secret = "1a09ce5f0d01611a3344";
	
	public clientPusher(String nom){
		Pusher p = new  Pusher(key);
		Channel channel = p.subscribe(nom);
		p.connect();
		//channel.bind(("evenement de "+nom),envoieMessage());
	}
	
	@Override
	public void onEvent(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSubscriptionSucceeded(String arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args){
		clientPusher user1 = new clientPusher("khaled");
		System.out.print("1st user created");
	}

	
}
