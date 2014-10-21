package serverNetty;

import box.TrafficCounter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslContext;

/**
 * 
 * @author Саня
 *
 */
public class HttpServerInit extends ChannelInitializer<SocketChannel>{
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
        //pipeline.addLast(sslCtx.newHandler(ch.alloc()));
        }
        //p.addLast(new SslHandler());
        pipeline.addLast(new HttpRequestDecoder());
	    pipeline.addLast(new HttpResponseEncoder());
        //pipeline.addLast(new HttpResponseEncoder());
        pipeline.addLast(new HttpServerHandler());
        //pipeline.addLast(new *other*Handler());
		
	}

}
