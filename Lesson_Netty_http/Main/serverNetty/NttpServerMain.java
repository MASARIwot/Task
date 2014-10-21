package serverNetty;


import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 
 * @author Саня
 *
 */
public class NttpServerMain {
	/*ServerBootstrap::
	 * a helper class to initialize the server. 
	 * */
	private ServerBootstrap serverBoot = null;
	/*NioEventLoopGroup:: 
	 *is a multi-threaded event loop, 
 	 which handles I / O operations.
	 * */
	/*bossGroup::  
	 * accepts incoming connections. 
	 * */
	private EventLoopGroup bossGroup = null;
	/* workerGroup::   
	* processes traffic from the accepted connection bossGroup takes 
	* Connect and register connection to the worker.
	 * */
	private EventLoopGroup workerGroup = null;
	/*ChannelFuture::  
	 * The result of the asynchronous operation I / O channel. 
	 * All I / O operations are asynchronous in Netty. 
	 * This means any calls to the I / O will be returned 
	 * value immediately with no guarantee that the requested operation 
	 * I / O operation has been completed at the end of the call. Instead, it will 
	 * are returned with a copy of ChannelFuturekotory gives you information 
	 * About the outcome or status of I / O operations. 
	 * */
	ChannelFuture channelsFuture = null;
	private TestTreadForServer testThred = null;
	private int PORT = 0; 
	
	public NttpServerMain(int port){
		this.PORT = port;
		serverBoot = new ServerBootstrap();
		bossGroup = new NioEventLoopGroup();
		workerGroup = new NioEventLoopGroup();
	} 
	
	public void configureServer() throws InterruptedException, NoSuchProviderException, NoSuchAlgorithmException{
		try{
		serverBoot.group(bossGroup, workerGroup)
				  .channel(NioServerSocketChannel.class) 
				  /*NioServerSocketChannel
				   is used to create an instance of the new channel, 
						to accept incoming connections
				   */
				  .childHandler(new HttpServerInit()/*new ChannelInitializer<SocketChannel>() { // (4)
		                 @Override
		                 public void initChannel(SocketChannel ch) throws Exception {
		                     ch.pipeline().addLast(new HttpServerInit());
		                 }
		             }*/);
		/*Bind and start to accept incoming connections. 
			Here we connect with the port 8085 (int PORT) all NICs (network cards) for a PC. 
			Now you can call the Bind () as many times as you want 
			(with different addresses binding.)
		 */
		 
		channelsFuture = serverBoot.bind(PORT).sync();
		testThred = new TestTreadForServer(channelsFuture);
		testThred.start();
		/*sync()::-Waits for this future until it is done, 
		 * and throws(InterruptedException) if the future failed.
		 * */
		System.out.println("Open your web browser and navigate to " +
                "http" + "://127.0.0.1:" + PORT + '/');
		/* shut down server.
		 * */
		channelsFuture.channel().closeFuture().sync();
		
		
		}finally{
			if(bossGroup != null)
			bossGroup.shutdownGracefully();
			if(workerGroup != null)
			workerGroup.shutdownGracefully();
			
		}
	}
		
	public static void main(String args[]) throws InterruptedException, NoSuchProviderException, NoSuchAlgorithmException {
		/*init PORT*/
		int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8085;
        }
		try {
			/*Start Server*/
			new NttpServerMain(port).configureServer();
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new InterruptedException();
		}
	}/*Main*/

}/*NttpServerMain.class*/
