package box;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Саня
 *This Class is counting individual person traffic , 
 *creating Object<IpPerson> of using his IP information and monitoring for persons activity
 *All  Object<IpPerson> add in to ArrayList<IpPerson>
 *
 */
public class TrafficCounter  {
	/*Namber off Active connect
	 * */
	private int activConnect = 0;
	private volatile int reqvestCounter = 0;
	private int reqvestEqvalCounter = 0;
	
	/*ArrayList for save IpPerson information
	 *(@see) IpPerson
	 * */
	private  List<IpPerson> listOfPerson =  new ArrayList<IpPerson>();
	/*
	 * for ReenTrantlock
	 * */
	private static final Lock lock = new ReentrantLock();
	/*
	 * Singleton
	 */
	private volatile static TrafficCounter instance = null;
	
		
    /*Singleton Double Checked Locking*/
	public static TrafficCounter getInstance(){
		if(instance == null){
			synchronized (TrafficCounter.class) {
				if(instance == null) instance = new TrafficCounter();
				 
			}
		}
		return instance; // or Singleton.instance
		
	}/*TrafficCounter getInstance()*/
	/**
	 * @param scr_IP
	 * @param uri
	 * @param sent_bytes
	 * @param intreceived_bytes
	 * @param speed
	 * @param lastDate
	 * This method add new Person information to arraylist ,
	 * If sach person exist he will modify his information
	 * @see IpPerson
	 */
	public void addPersone(String scr_IP,String uri,int sent_bytes,int intreceived_bytes,int speed,String lastDate){
		lock.lock();try{
		if(listOfPerson.size() == 0){
			listOfPerson.add(new IpPerson(scr_IP, uri, sent_bytes, intreceived_bytes, speed,lastDate)); 
			addEqualCounter();
			addCount();
			}
		for(int i = 0; i < listOfPerson.size(); i++ ){
			if( !(listOfPerson.get(i).getScr_IP()).equals(scr_IP)){
			addEqualCounter();
			addCount();
			listOfPerson.add(new IpPerson(scr_IP, uri, sent_bytes, intreceived_bytes, speed,lastDate));	
			}else if( (listOfPerson.get(i).getScr_IP()).equals(scr_IP)){
				addCount();
				listOfPerson.get(i).setIntreceived_bytes(intreceived_bytes);
				listOfPerson.get(i).setLastDate(lastDate);
				listOfPerson.get(i).setLastUri(uri);
				listOfPerson.get(i).setReqvestCounter();
				listOfPerson.get(i).setSent_bytes(sent_bytes);
				listOfPerson.get(i).setSpeed(speed);
						
			}/*else*/
		}/*For*/
	// System.out.println(listOfPerson.toString());
		}finally{
			lock.unlock();
			 
			}
	}/*addPersone*/
	/**
	 * 
	 * @return unmodifiableList List<IpPerson>
	 */
	public  List<IpPerson> getListOfPerson(){
		lock.lock();try{return Collections.unmodifiableList(this.listOfPerson);
		}finally{lock.unlock();}
	}
	
	/**
	 * @return the ectivConnect
	 */
	public int getEctivConnect() {
		lock.lock();try{return activConnect;
		}finally{lock.unlock();}
	}

	/**
	 * ADD work Connection
	 */
	public void Offconnect() {
		lock.lock();try{this.activConnect--;
		}finally{lock.unlock();}
	}
	/**
	 *Off conection
	 */
	public void Addconnect() {
		lock.lock();try{this.activConnect++;
		}finally{lock.unlock();}
	}
	/**
	 * @return the namber of connection 
	 */
	public int getNamberOfCounnect() {
		lock.lock();try{return reqvestCounter/2;
		}finally{lock.unlock();}
	}
	/**
	 * @return the namber of individual connection 
	 */
	public int getNamberOfEqualCounnect() {
		lock.lock();try{return reqvestEqvalCounter;
		}finally{lock.unlock();}
	}
	/**
	 * @param add connection
	 */
	public void addCount() {
		lock.lock();try{this.reqvestCounter +=1;
		}finally{lock.unlock();}
	}
	
	/**
	 * @param add individual connection
	 */
	private void addEqualCounter() {
		this.reqvestEqvalCounter++;
	}
	
	
	
}
