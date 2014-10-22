package statusLogic;


import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import box.IpPerson;
import box.TrafficCounter;
/**
 * Class what create answer for ..../status
 * @author ����
 *
 */
public class Status {
	
	private TrafficCounter traficCoute = null;
	private List<IpPerson> listOfPerson = null;
	private StringBuilder bufHtml =null;
	private HashMap<String ,Integer> allUriMap = null;
	
	public Status(){
		
		traficCoute = TrafficCounter.getInstance();
		bufHtml = new StringBuilder();
		
	}
	/**
	 * Create Head of Html doc. 
	 */
	private String getHead(/*Handler h*/){
		bufHtml.append(  "<!DOCTYPE html>\r\n"	);
		bufHtml.append(  "<html> \r\n"			);
		bufHtml.append(  "<head> \r\n"			);
		bufHtml.append(  "<title>My first littel server</title> \r\n");
		bufHtml.append(  "</head>\r\n"			);
		bufHtml.append(  "<body> \r\n"			);
		int activeConnection = traficCoute.getEctivConnect()-2;
		int allReqvest = traficCoute.getNamberOfCounnect();
		int equlReqvest = traficCoute.getNamberOfEqualCounnect();
		bufHtml.append(  "<h3>The number of connections currently open : "+activeConnection+"</h3>\r\n"	);
		bufHtml.append(  "<h3>Total number of requests : "+allReqvest+"</h3>\r\n"	);
		bufHtml.append(  "<h3>The number of unique queries  : "+equlReqvest+"</h3>\r\n"	);
		return null;
	}
	/**
	 * Create END of Html doc.
	 */
	private String getTail(/*Handler h*/){
		bufHtml.append(  "</body> \r\n"			);
		bufHtml.append(  "</html> \r\n"			);
		
		return null;
	}
	/**
	 * ADD information 
	 */
	private String format(/*LogRecord record*/){
		
		/*Counter of redirection
		 * head tabel
		 * */
		bufHtml.append(  "<h3>Number of redirects to url'��</h3>\r\n"	);
		bufHtml.append(  "<table class=brd border=1>\r\n"		);
		bufHtml.append(  "<tr>\r\n" 							);
		
		bufHtml.append(  "<th>Number of redirects</th>\r\n"	);
		bufHtml.append(  "<th>URL</th>\r\n"						);
		
		bufHtml.append(  "</tr>\r\n"							);
	    /*Info in tabel
	     * */
	    listOfPerson = traficCoute.getListOfPerson();
	    for(IpPerson persone: listOfPerson){
	    	allUriMap = persone.getAllUri();
	    	for (String nameUrlKey: allUriMap.keySet()){
	            String urlKey =nameUrlKey.toString();
	            String replase = allUriMap.get(nameUrlKey).toString();
	            bufHtml.append(  "<tr>\r\n"					);
	            bufHtml.append(  "<td>" + replase +"</td> "	);
	            bufHtml.append(  "<td>" + urlKey +"</td> "	);
	            bufHtml.append(  "</tr>\r\n"				);
	         }/*for (String name: allUriMap.keySet())*/ 
	    }/* for(IpPerson persone: listOfPerson){*/
	    	    
		/*Close Tabel : Counter of redirection*/
	    bufHtml.append(  "</table>\r\n"	);
	    
	    /*Reqvest counter for each IP
		 * */
	    bufHtml.append(  "<h3>Counter requests for each IP</h3>\r\n");
		bufHtml.append(  "<table class=brd border=1>\r\n"		);
		bufHtml.append(  "<tr>\r\n"								);
		
		bufHtml.append(  "<th>IP</th>\r\n"						);
		bufHtml.append(  "<th>number of requests</th>\r\n"			);
		bufHtml.append(  "<th>the last request time</th>\r\n");
		
		bufHtml.append(  "</tr>\r\n"							);
		/*Info in tabel
	     * */
		listOfPerson = traficCoute.getListOfPerson();
		for(IpPerson persone: listOfPerson){
			String idPersone = persone.getScr_IP();
			int numberOfReqvest = persone.getReqvestCounter();
			String lastReqvestData = persone.getLastDate();
			bufHtml.append(  "<tr>\r\n"								);
			
			bufHtml.append(  "<td>" + idPersone +"</td>\r\n "		);
            bufHtml.append(  "<td>" + numberOfReqvest +"</td>\r\n "	);
            bufHtml.append(  "<td>" + lastReqvestData +"</td>\r\n "	);
            
            bufHtml.append(  "</tr>\r\n"							);
		}/*for(IpPerson persone: listOfPerson)*/
		/*Close Tabel: Reqvest counter for each*/
		bufHtml.append(  "</table>\r\n"	);
		 
		 /* 16 last Reqvest 
			 * */
		bufHtml.append(  "<h3>16 Last processed connections</h3>\r\n"	);
		bufHtml.append(  "<table class=brd  border=1>\r\n"		);
		bufHtml.append(  "<tr>\r\n"								);
		
		bufHtml.append(  "<th>scr_Ip</th>\r\n"					);
		bufHtml.append(  "<th>URL</th>\r\n"						);
		bufHtml.append(  "<th>timestamp</th>\r\n"				);
		bufHtml.append(  "<th>sent_bytes</th>\r\n"				);
		bufHtml.append(  "<th>received_bytes</th>\r\n"			);
		bufHtml.append(  "<th>speed (bytes/sec)</th>\r\n"		);
		
		bufHtml.append(  "</tr>\r\n"							);
		/*Info in tabel
		* */
		listOfPerson = (traficCoute.getListOfPerson());
		Collections.reverse(listOfPerson);
		int i = 0;
		for(IpPerson persone: listOfPerson){
			
			String scr_Ip = persone.getScr_IP();
			String URL = persone.getLastUri();
			String timestamp = persone.getLastDate();
			int sent_bytes = persone.getSent_bytes();
			int received_bytes = persone.getIntreceived_bytes();
			double speed = persone.getSpeed();
			bufHtml.append(  "<tr>\r\n"								);
			
			bufHtml.append(  "<td>" + scr_Ip +"</td>\r\n "			);
            bufHtml.append(  "<td>" + URL +"</td>\r\n "				);
            bufHtml.append(  "<td>" + timestamp +"</td>\r\n "		);
            bufHtml.append(  "<td>" + sent_bytes +"</td>\r\n "		);
            bufHtml.append(  "<td>" + received_bytes +"</td>\r\n "	);
            bufHtml.append(  "<td>" + speed +"</td>\r\n "			);
			
			bufHtml.append(  "</tr>\r\n"							);
			if(i == 16) break;
			i++;
		}/*for(IpPerson persone: listOfPerson){*/
		/*Close Tabel: 16 last Reqvest*/ 
		bufHtml.append(  "</table>\r\n"	); 
		 
		 /* 16 last Operation 
			 * */
		last16Operation();
	
		return null;
	}
	/**
	 * This method return html doc. with iformation of reqvests
	 * @return StringBuilder 
	 */
	public StringBuilder getStatus(){
		getHead();
		format();
		getTail();
		
				
		return bufHtml;
	}
	

	private void last16Operation(){
		/* 16 last Operation getLastOperation
		 * */
		
		bufHtml.append(  "<h3>16 Last processed Operation</h3>\r\n"	);
		bufHtml.append(  "<table class=brd  border=1>\r\n"		);
		bufHtml.append(  "<tr>\r\n"								);
		
		bufHtml.append(  "<th>scr_Ip</th>\r\n"					);
		bufHtml.append(  "<th>URL</th>\r\n"						);
		bufHtml.append(  "<th>timestamp</th>\r\n"				);
		bufHtml.append(  "<th>sent_bytes</th>\r\n"				);
		bufHtml.append(  "<th>received_bytes</th>\r\n"			);
		bufHtml.append(  "<th>speed (bytes/sec)</th>\r\n"		);
		
		bufHtml.append(  "</tr>\r\n"							);
		/*Info in tabel
		* */
		listOfPerson = (traficCoute.getLastOperation());
		for(IpPerson persone: listOfPerson){
			
			String scr_Ip = persone.getScr_IP();
			String URL = persone.getLastUri();
			String timestamp = persone.getLastDate();
			int sent_bytes = persone.getSent_bytes();
			int received_bytes = persone.getIntreceived_bytes();
			double speed = persone.getSpeed();
			bufHtml.append(  "<tr>\r\n"								);
			
			bufHtml.append(  "<td>" + scr_Ip +"</td>\r\n "			);
            bufHtml.append(  "<td>" + URL +"</td>\r\n "				);
            bufHtml.append(  "<td>" + timestamp +"</td>\r\n "		);
            bufHtml.append(  "<td>" + sent_bytes +"</td>\r\n "		);
            bufHtml.append(  "<td>" + received_bytes +"</td>\r\n "	);
            bufHtml.append(  "<td>" + speed +"</td>\r\n "			);
			
			bufHtml.append(  "</tr>\r\n"							);
			
			
		}/*for(IpPerson persone: listOfPerson){*/
		/*Close Tabel: 16 last Reqvest*/ 
		bufHtml.append(  "</table>\r\n"	); 
		
		
	    }
	
	

}
