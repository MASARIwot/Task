package box;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
	private volatile static  int activConnect = 0;
	/*All reqves*/
	private volatile int reqvestCounter = 0;
	/*For diferent IP*/
	private int reqvestEqvalCounter = 0;
	
	/*ArrayList for save IpPerson information
	 *(@see) IpPerson
	 * */
	private  List<IpPerson> listOfPerson =  new ArrayList<IpPerson>();
	/*ArrayList for Last 16 operation
	 *(@see) IpPerson
	 * */
	private  List<IpPerson> lastOperation =  new ArrayList<IpPerson>(16);
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
	public  void  addPersone(String scr_IP,String uri,int sent_bytes,int intreceived_bytes,double speed,String lastDate){
		lock.lock();try{
		if(listOfPerson.size() == 0){
			listOfPerson.add(new IpPerson(scr_IP, uri, sent_bytes, intreceived_bytes, speed,lastDate)); 
			addEqualCounter();
			addCount();
			/*Save last 16 operation*/
			setLastOperation(new IpPerson(scr_IP, uri, sent_bytes, intreceived_bytes, speed, lastDate));
			}else if(listOfPerson.size() != 0) {
					for(int i = 0; i < listOfPerson.size(); i++ ){
						if( !(listOfPerson.get(i).getScr_IP()).equals(scr_IP)){
						listOfPerson.add(new IpPerson(scr_IP, uri, sent_bytes, intreceived_bytes, speed,lastDate));	
						addEqualCounter();
						addCount();
						/*Save last 16 operation*/
						setLastOperation(new IpPerson(scr_IP, uri, sent_bytes, intreceived_bytes, speed, lastDate));			
						}else if( (listOfPerson.get(i).getScr_IP()).equals(scr_IP)){
							listOfPerson.get(i).setIntreceived_bytes(intreceived_bytes);
							listOfPerson.get(i).setLastDate(lastDate);
							listOfPerson.get(i).setLastUri(uri);
							listOfPerson.get(i).setReqvestCounter();
							listOfPerson.get(i).setSent_bytes(sent_bytes);
							listOfPerson.get(i).setSpeed(speed);
							addCount();
							/*Save last 16 operation*/
							setLastOperation(new IpPerson(scr_IP, uri, sent_bytes, intreceived_bytes, speed, lastDate));
						}/*else*/
					}/*For*/
			}/*else if(listOfPerson.size() != 0)*/
		}finally{lock.unlock();}
	}/*addPersone*/
	/**
	 * This method  serializ information in file
	 * @param path to serializ Information in File
	 * @throws IOException
	 * @see box.IpPerson.class
	 */
	public void  serializInformation(String path) throws IOException {
		lock.lock(); try ( ObjectOutputStream out = new ObjectOutputStream( new FileOutputStream(path))){
            out.writeObject(this.listOfPerson); 
         System.out.println("\n Write in File Successful.Checkout your output file..\n" + path );
		}finally{lock.unlock();}
			
	}
	/**
	 * This method read information from file to List<IpPerson> listOfPerson 
	 * @param path path to DeSerializ Information from File
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @see box.IpPerson.class 
	 */
	@SuppressWarnings("unchecked")
	public void  deSerializInformation(String path) throws IOException, ClassNotFoundException {
		lock.lock(); try ( ObjectInputStream in = new ObjectInputStream(new FileInputStream(path))){
			this.listOfPerson = (ArrayList<IpPerson>) in.readObject();
       	}finally{lock.unlock();}
		
	}
	/**
	 * @return  List<IpPerson> of last 16 operation
	 */
	public List<IpPerson> getLastOperation() {
		lock.lock();try{return Collections.unmodifiableList(this.lastOperation);
		}finally{lock.unlock();}
	}
	/**
	 * this method save last 16 operation
	 * @param lastOperation the lastOperation to set
	 */
	private void setLastOperation(IpPerson newPersone) {
			if(lastOperation.size() > 15)
	    	lastOperation.remove(0);
	    	lastOperation.add(newPersone);
	}
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
	public static void Offconnect() {
		lock.lock();try{activConnect--;
		}finally{lock.unlock();}
	}
	/**
	 *Off conection
	 */
	public static void Addconnect() {
		lock.lock();try{activConnect++ ;
		}finally{lock.unlock();}
	}
	/**
	 * @return the namber of connection 
	 */
	public int getNamberOfCounnect() {
		lock.lock();try{return reqvestCounter;
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
	private void addCount() {
		lock.lock();try{this.reqvestCounter +=1;
		}finally{lock.unlock();}
	}
    /**
	 * @param add individual connection
	 */
	private void addEqualCounter() {
		this.reqvestEqvalCounter++;
	}
	

	
}/*TrafficCounter END */
