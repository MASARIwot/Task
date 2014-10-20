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
 * @author ����
 *
 */
public class NttpServerMain {
	/*ServerBootstrap::
	 * ��� ��������������� ����� ��� ������������� �������. 
	 * ����� � ��� ����, �� ��������� ����� ���� ������
	 * */
	private ServerBootstrap serverBoot = null;
	/*NioEventLoopGroup:: 
	 * �������� ������������� ���� �������, 
	 * ������� ������������ �������� ����� / ������. 
	 * */
	/*bossGroup::  
	 * ��������� �������� ����������. 
	 * */
	private EventLoopGroup bossGroup = null;
	/* workerGroup::   
	 * ������������ 
	 * ������ �� ��������� ���������� bossGroup ��������� 
	 * ���������� � ������������ �������e ����������� � ��������.
	 * */
	private EventLoopGroup workerGroup = null;
	/*ChannelFuture::  
	 * ��������� ����������� �������� ����� ����� / ������.
	 *��� �������� ����� / ������ � Netty �������� ������������. 
	 *��� �������� ����� ������ ����� / ������ ����� ���������� 
	 *�������� ���������� ��� ��������, ��� ����������� �������� 
	 *����� / ������ ���� ��������� � ����� ������. ������ ����� ����� 
	 *�������� ������� � ����������� ChannelFuture������� ���� ��� ����������
	 * � ���������� ��� ������� �������� ����� / ������.
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
				   ������������ ��� �������� ���������� ������ ������,
				   ����� ��������� �������� ����������
				   */
				  .childHandler(new HttpServerInit()/*new ChannelInitializer<SocketChannel>() { // (4)
		                 @Override
		                 public void initChannel(SocketChannel ch) throws Exception {
		                     ch.pipeline().addLast(new HttpServerInit());
		                 }
		             }*/);
		/*�������� � ������ ��������� �������� ����������.
		 * ����� �� ����������� � ������ 8085( int PORT) ���� NICs (������� �����) � ������.
		 *  ������ �� ������ ������� ����� Bind () ������� ���, ������� �� ������ 
		 *  (� ������ ������� ����������.)
		 * */
		 
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
