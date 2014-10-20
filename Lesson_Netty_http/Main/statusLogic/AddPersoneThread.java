package statusLogic;

import box.TrafficCounter;
/**
 * This class add  new persone
 * @author Саня
 * @see TrafficCounter
 */
public class AddPersoneThread extends Thread {
	
	private TrafficCounter traficCoute = null;
	/*
	 *Persone ID 
	 */
	private String scr_IP = "def";
	/*
	 *Last  Redirect url
	 */
	private String url = null;
	/*
	 *byt statistic 
	 */
	private int sent_bytes = 0; 
	private int intreceived_bytes = 0; 
	private int speed = 0;
	/*
	 * Data counter 
	 */
	private String lastDate = " "; 
	
	
	/**
	 * 
	 * @param scr_IP
	 * @param uri
	 * @param sent_bytes
	 * @param intreceived_bytes
	 * @param speed
	 * @param lastDate
	 */
	public AddPersoneThread(String scr_IP,String uri,int sent_bytes,int intreceived_bytes,int speed,String lastDate){
		traficCoute = TrafficCounter.getInstance();
		this.scr_IP = scr_IP;
		this.url = uri;
		this.sent_bytes = sent_bytes;
		this.intreceived_bytes = intreceived_bytes;
		this.speed = speed;
		this.lastDate = lastDate;
	}

	@Override
	public void run() {
		traficCoute.addPersone(scr_IP, url, sent_bytes, intreceived_bytes, speed, lastDate); 
		
		
	}

	
}
