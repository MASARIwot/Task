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
	 * это вспомогательный класс для инициализации сервера. 
	 * Можно и без него, но прийдется много кода писать
	 * */
	private ServerBootstrap serverBoot = null;
	/*NioEventLoopGroup:: 
	 * является многопоточной цикл событий, 
	 * который обрабатывает операции ввода / вывода. 
	 * */
	/*bossGroup::  
	 * принимает входящие соединения. 
	 * */
	private EventLoopGroup bossGroup = null;
	/* workerGroup::   
	 * обрабатывает 
	 * трафик от принятого соединения bossGroup принимает 
	 * соединение и регистрирует принятыe подключение к рабочему.
	 * */
	private EventLoopGroup workerGroup = null;
	/*ChannelFuture::  
	 * Результат асинхронной операции Канал ввода / вывода.
	 *Все операции ввода / вывода в Netty являются асинхронными. 
	 *Это означает любые вызовы ввода / вывода будут возвращают 
	 *значения немедленно без гарантии, что запрошенная операция 
	 *ввода / вывода была завершена в конце вызова. Вместо этого будет 
	 *выполнен возврат с экземпляром ChannelFutureкоторый дает вам информацию
	 * о результате или статусе операции ввода / вывода.
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
				   используется для создания экземпляра нового канала,
				   чтобы принимать входящие соединения
				   */
				  .childHandler(new HttpServerInit()/*new ChannelInitializer<SocketChannel>() { // (4)
		                 @Override
		                 public void initChannel(SocketChannel ch) throws Exception {
		                     ch.pipeline().addLast(new HttpServerInit());
		                 }
		             }*/);
		/*Привязка и начать принимать входящие соединения.
		 * Здесь мы связываемся с портом 8085( int PORT) всех NICs (сетевые карты) в машине.
		 *  Теперь вы можете вызвать метод Bind () столько раз, сколько вы хотите 
		 *  (с разным адресам связывания.)
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
