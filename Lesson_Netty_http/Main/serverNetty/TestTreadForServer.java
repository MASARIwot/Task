package serverNetty;

import io.netty.channel.ChannelFuture;

public class TestTreadForServer extends Thread{
	ChannelFuture channelsFuture = null;
	
	public TestTreadForServer(ChannelFuture channelsFuture){
		this.channelsFuture =  channelsFuture;
	}
	
	@Override
	public void run() {
		try {
			channelsFuture.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
