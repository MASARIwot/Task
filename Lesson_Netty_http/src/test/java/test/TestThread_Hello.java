package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
@SuppressWarnings("unused")
public class TestThread_Hello extends Thread{
	private StringBuilder strigFromURL = null;
	private String readString = null;
	private String str;
	
	public TestThread_Hello(String str){
		this.str = str;
	}
	
	@Override
	public void run() {
		
		for(int i = 0; i < 10; i++){
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
