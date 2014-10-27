package serverNetty;



//import box.MyHttpRequestDecoder;
//import box.MyHttpResponseEncoder;
import box.TrafficCounter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslContext;


/**
 * 
 * @author Саня
 *
 */
public class HttpServerInit extends ChannelInitializer<SocketChannel>{
	//Timer timer = new HashedWheelTimer();
	public static TrafficCounter traficCouteInServer = TrafficCounter.getInstance();
    /*(see)http://netty.io/4.0/api/io/netty/handler/ssl/SslContext.html
     * Safe socket implementation a protocol that acts
     *  as a factory for SSLEngine and SslHandler. Inside, it 
     *  implemented through SSLContext JDK or SSL_CTX OpenSSL,
     * */
	private SslContext sslCtx;
	
		
	public HttpServerInit(SslContext sslCtx){
		this.sslCtx = sslCtx;
		
	}
	public HttpServerInit(){
		
	}
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
        if (sslCtx != null) {
//        pipeline.addFirst(sslCtx.newHandler(ch.alloc()));
        }
        pipeline.addLast("encoder",		new HttpResponseEncoder());
        pipeline.addLast("decoder",		new HttpRequestDecoder());
	    pipeline.addLast("aggregator",	new HttpObjectAggregator(1048576));
        pipeline.addLast("handle",		new MyHttpServerHandler());

		
	}

}
