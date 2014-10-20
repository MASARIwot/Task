package box;

import java.util.ArrayList;
import java.util.HashMap;
//import java.util.HashSet;




public class IpPerson {
	/*
	 *Persone ID 
	 */
	private String scr_IP = " ";
	/*
	 *Last  Redirect url
	 */
	private String lastUri = null;
	/*
	 * Redirect url counter
	 */
	private int uriRedirection = 0;
	/*
	 * all Redirect url
	 */
	private HashMap<String ,Integer> allUri = null;
	/*
	 * number of request(counter) 
	 */
	private int reqvestCounter = 0;
	/*
	 * Data counter 
	 */
	private String lastDate = " ";
	private ArrayList<String> allDateOfReqvest =null;
	/*
	 *byt statistic 
	 */
	private int sent_bytes = 0;
	private int intreceived_bytes = 0 ;
	private double speed = 0;
	
	/**
	 * @param scr_IP
	 * @param uri
	 * @param sent_bytes
	 * @param intreceived_bytes
	 * @param speed
	 */
	public  IpPerson(String scr_IP,String uri,int sent_bytes,int intreceived_bytes,int speed,String lastDate){
		this.allUri = new HashMap<String ,Integer>();
		this.allDateOfReqvest = new ArrayList<String>();
		this.scr_IP = scr_IP;
		this.lastUri = uri;
		this.sent_bytes = sent_bytes;
		this.intreceived_bytes = intreceived_bytes;
		this.speed = speed;
		this.lastDate = lastDate;
	}

	
	/**
	 * Return IP of person/Client for examp: 121.0.0.1
	 * @return the scr_IP
	 */
	public String getScr_IP() {
		return scr_IP;
	}

	/**
	 * @param scr_IP the scr_IP to set
	 */
	public void setScr_IP(String scr_IP) {
		this.scr_IP = scr_IP;
	}

	/**
	 * This method return url of last redirection
	 * @return the lastUri
	 */
	public String getLastUri() {
		return lastUri;
	}

	/**
	 * @param uri the uri to set
	 */
	public void setLastUri(String uri) {
		if(null != (uri)){
			this.lastUri = uri;
			int i = 1;
			if(!this.allUri.containsKey(uri)){
			this.allUri.put(uri,i);
			}else
			if(this.allUri.containsKey(uri)){
				int iw = 1;
				iw = this.allUri.get(uri);
				iw++;
				this.allUri.put(uri,iw);
				iw = 0;
				
			}
	   }
		
		
	}

	/**
	 * this method return namber of redirect for 
	 * diferent url 
	 * @return HashMap<String ,Integer>
	 */
	public HashMap<String ,Integer> getAllUri() {
		return allUri;
	}
	/**
	 * Thwi method return nambe of all reqvest
	 * @return the reqvestCounter
	 */
	public int getReqvestCounter() {
		return reqvestCounter/2;
	}

	/**
	 * @param reqvestCounter the reqvestCounter to set
	 */
	public void setReqvestCounter(/*int reqvestCounter*/) {
		this.reqvestCounter += 1;
	}

	/**
	 * this method return Date off last reqvest
	 * @return the String :lastDate
	 * @see  IpPerson().getAllDateOfReqvest()
	 * yuo can get all date !
	 * 
	 */
	public String getLastDate() {
		return lastDate;
	}

	/**
	 * @param lastDate the lastDate to set
	 */
	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
		this.allDateOfReqvest.add(lastDate);
	}

	/**
	 * this method return All Date off reqvest
	 * @return ArrayList<String>  :allDateOfReqvest
	 * @see IpPerson().getLastDate() 
	 * yuo can get last date !
	 * 
	 */
	public ArrayList<String> getAllDateOfReqvest() {
		return allDateOfReqvest;
	}
	/**
	 * this method return namder of sended bytes
	 * @return the int: sent_bytes
	 */
	public int getSent_bytes() {
		return sent_bytes;
	}

	/**
	 * Count sending bytes
	 * @param the int : sent_bytes to set
	 */
	public void setSent_bytes(int sent_bytes) {
		this.sent_bytes += sent_bytes;
	}

	/**
	 * @return return received bytes
	 */
	public int getIntreceived_bytes() {
		return intreceived_bytes;
	}

	/**
	 * @param intreceived_bytes the intreceived_bytes to set
	 */
	public void setIntreceived_bytes(int intreceived_bytes) {
		this.intreceived_bytes += intreceived_bytes;
	}

	/**
	 * @return the speed,Speed of last connection
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(double speed) {
		if(speed != 0){
		this.speed = speed;
		}
	}
	/**
	 * @return the uriRedirection
	 */
	public int getUriRedirection() {
		return uriRedirection;
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "IpPerson [scr_IP=" + scr_IP + ", uri=" + lastUri + ",  reqvestCounter=" + reqvestCounter + ", lastDate="
				+ lastDate 
				+ ", sent_bytes=" + sent_bytes + ", intreceived_bytes="
				+ intreceived_bytes + ", speed=" + speed + "]";
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof IpPerson)){
			return false;
		}
		IpPerson other = (IpPerson) obj;
		if (scr_IP == null) {
			if (other.scr_IP != null)
				return false;
		} else if (!scr_IP.equals(other.scr_IP))
			return false;
		return true;
	}

	
	
	
	
}
