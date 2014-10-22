package test;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import box.IpPerson;
@SuppressWarnings("unused")
public class TestServer {
	private StringBuilder strigFromURL = null;
	private String readString = null;
    private String urlHello =  "http://127.0.0.1:8085/hello";
 
    private List<String> lastOperation =  new ArrayList<String>(4);
	
	private String urlstatus =  "http://127.0.0.1:8085/status";
    private String[] redirect = {
    		"http://127.0.0.1:8085/redirect?url=http://stopgame.ru/",
    		"http://127.0.0.1:8085/redirect?url=http://google.ru/",
    		"http://127.0.0.1:8085/redirect?url=http://google.ua/",
    		"http://127.0.0.1:8085/redirect?url=http://facebook.com/",
    		"http://127.0.0.1:8085/redirect?url=http://stopgame.ru/",
    		"http://127.0.0.1:8085/redirect?url=http://google.ru/",
    		"http://127.0.0.1:8085/redirect?url=http://google.ua/",
    		"http://127.0.0.1:8085/redirect?url=http://facebook.com"};
	
    
    @Ignore 
    @Test
	public void test2() {
    	for(int i = 0; i < redirect.length; i++ ){
    		 aggPers(redirect[i]);
		}
    	System.out.print(lastOperation.toString());
        	
    }
    public void aggPers(String str){
    	if(lastOperation.size() > 4){
    	lastOperation.remove(0);
    	lastOperation.add(str);}else lastOperation.add(str);
    	
    }
    
    
    
  
	@Test
	public void test() throws InterruptedException {
		float startTime =  System.currentTimeMillis();
		
		TestThread_Hello testHello = null;
		
		TestThread_Redirect testRedirect = null;
		
		
		for(int i = 0; i < redirect.length; i++ ){
			testRedirect = new TestThread_Redirect(redirect[i]);
			testRedirect.start();
		}
		
		for(int i = 0; i < 10; i++ ){
			testHello = new TestThread_Hello(urlHello);
			testHello.start();
			//connect(urlHello);
			}
				
		testHello.join();
		testRedirect.join();
		testHello.join();
		testRedirect.join();
				
		float stopTime =  System.currentTimeMillis() - startTime;
		
		System.out.print(stopTime);
	}
	
	
	private void connect(String url){
    	
    	try(BufferedReader  buff  = 
				new BufferedReader(
						new InputStreamReader(
								new URL(url)
								.openConnection().getInputStream(), "UTF8"));){ 
			
			strigFromURL = new StringBuilder();
			/*while((readString = buff.readLine()) != null){
				strigFromURL.append(readString);
				strigFromURL.append("\n");
			}*/
        
		}catch(MalformedURLException e){
		
		}catch(IOException e){}
	//	System.out.println(strigFromURL.toString());
	//return strigFromURL.toString();
    	
    	
    }
	

}
