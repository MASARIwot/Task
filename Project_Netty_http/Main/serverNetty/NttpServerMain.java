package serverNetty;


import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

//import box.TrafficCounter;
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
	private int PORT = 0; 
	/**
	 * @param port - Port for server init.
	 */
	public NttpServerMain(int port){
		this.PORT = port;
		serverBoot = new ServerBootstrap();
		bossGroup = new NioEventLoopGroup(100);
		workerGroup = new NioEventLoopGroup(100);
	} 
	/**
	 * Server Configuration
	 * @throws InterruptedException
	 * @throws NoSuchProviderException
	 * @throws NoSuchAlgorithmException
	 */
	public void configureServer() throws InterruptedException, NoSuchProviderException, NoSuchAlgorithmException{
		try{
		serverBoot.group(bossGroup, workerGroup)
				  .channel(NioServerSocketChannel.class) 
				  /*NioServerSocketChannel
				   is used to create an instance of the new channel, 
						to accept incoming connections
				   */
				  .childHandler(new HttpServerInit()/*new ChannelInitializer<SocketChannel>() { 
		                 @Override
		                 public void initChannel(SocketChannel ch) throws Exception {
		                     ch.pipeline().addLast(new {*********}());
		                 }
		             }*/
				  	
				  );//.localAddress("somedomain", PORT)/**/;
		/*Bind and start to accept incoming connections. 
			Here we connect with the port 8085 (int PORT) all NICs (network cards) for a PC. 
			Now you can call the Bind () as many times as you want 
			(with different addresses binding.)
		 */
		 
		channelsFuture = serverBoot.bind(PORT).sync();
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
//			workerGroup.shutdownGracefully().awaitUninterruptibly(10, TimeUnit.SECONDS);
//          bossGroup.shutdownGracefully().awaitUninterruptibly(10, TimeUnit.SECONDS);
			
		}
	}/*configureServer END*/
	/**
	 * Start server
	 * @param PORT
	 */
	public static void startServer(int PORT){
		final int port = PORT;

		 /*Start Server*/
		 Thread myThready = new Thread(new Runnable()
	        {
	            public void run() 
	            {
	            	try {
						new NttpServerMain(port).configureServer();
					} catch (NoSuchProviderException e) {
						
						e.printStackTrace();
					} catch (NoSuchAlgorithmException e) {
						
						e.printStackTrace();
					} catch (InterruptedException e) {
						
						e.printStackTrace();
					}
	            }
	        });
	        myThready.start();	//Запуск потока
		
	}
	
	public static void main(String args[]) throws InterruptedException, NoSuchProviderException, NoSuchAlgorithmException {
		/*init PORT*/
		int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8085;
        }
		/*Start Port*/
       // new NttpServerMain(port).configureServer();
        startServer(port);
		
		
	}/*Main END*/

}/*NttpServerMain.class END*/
