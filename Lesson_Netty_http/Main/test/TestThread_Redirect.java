package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class TestThread_Redirect extends Thread {
	private String str;
	private StringBuilder strigFromURL = null;
	private String readString = null;
	
	public TestThread_Redirect(String str){
		this.str = str;
	}
	
	@Override
	public void run() {
		for(int i = 0; i < 100; i++){
		try(BufferedReader  buff  = 
				new BufferedReader(
						new InputStreamReader(
								new URL(this.str)
								.openConnection().getInputStream(), "UTF8"));){ 
			
			strigFromURL = new StringBuilder();
			/*while((readString = buff.readLine()) != null){
				strigFromURL.append(readString);
				strigFromURL.append("\n");
			}*/
        
		}catch(MalformedURLException e){
		
		}catch(IOException e){}
		
		}	
		
	}
	
	

}
