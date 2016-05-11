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
	
	// Cette méthode permet d'envoie un message groupé
	private void envoieMessage(ArrayList<String> list,Pusher p,String message,String event,String email){
		p.trigger(list, event, Collections.singletonMap("message", message));
	}
	// Cette méthode envoie la position de l'utilisateur à sa liste de contacts
	private void mesCoordonnées(ArrayList<String> list, Pusher p,String event,int x,int y){
		p.trigger(list, event, Collections.singletonMap("ma position", (x+" "+y)));
	}
	
	public static void main(String[] args){
		
		String url = "http://"+key+":"+secret+"@api.pusherapp.com:80/apps/"+app_id;
		Pusher pusher = new Pusher(url);
		pusher.trigger("test_channel", "my_event", Collections.singletonMap("message", "Hello World"));
		
	}
}
