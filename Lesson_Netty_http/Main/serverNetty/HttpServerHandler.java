package serverNetty;

import java.util.Date;

import statusLogic.AddPersoneThread;
import statusLogic.Status;
import box.TrafficCounter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;
import static io.netty.handler.codec.http.HttpResponseStatus.*;//FOUND
import static io.netty.handler.codec.http.HttpVersion.*;//HTTP_1_1
import static io.netty.handler.codec.http.HttpHeaders.Names.*;//LOCATION
/**
 * this if main Handler
 * @author Саня
 */
public class HttpServerHandler extends ChannelInboundHandlerAdapter { /* SimpleChannelInboundHandler<FullHttpRequest>{*/
	TrafficCounter traficCoute = TrafficCounter.getInstance();
	//TrafficCounter traficCouteInServer = HttpServerInit.traficCouteInServer.getInstance();
	/*
	 *Persone ID 
	 */
	private String scr_IP = "def";
	/*
	 *Last  Redirect url
	 */
	private String url = null; 
	/*
	 *byt statistic 
	 */
	private int sent_bytes = 0; 
	private int intreceived_bytes = 0; 
	private int speed;
	/*
	 * Data counter 
	 */
	private String lastDate = " "; 
	
	private long currentTime = 0;
	private long lastWriteTime = 0;
	private AddPersoneThread addIfo = null;
	
	
      @Override
     public void channelReadComplete(ChannelHandlerContext ctx) {
    	  /*ADD last Write Time*/
    	  traficCoute.Offconnect();
    	  this.lastWriteTime = System.currentTimeMillis();
    	  calculateSpeed();
    	  /*ADD persone in new Tread*/
    	 
		    	  if(!"def".equals(this.scr_IP)){
		    	  addIfo = new AddPersoneThread(this.scr_IP, this.url, this.sent_bytes, this.intreceived_bytes, this.speed, this.lastDate);
		    	  addIfo.start();  
		    	 }
    	 
          ctx.flush();
      }/*channelReadComplete*/
      
      @Override
      public void channelActive(ChannelHandlerContext ctx) {
    	  		/*ADD Current Time*/
    	        this.currentTime = System.currentTimeMillis();
    	        traficCoute.Addconnect();
    	        /*Add DATE*/
                this.lastDate = ((new Date()).toString());
                /*Add src_IP*/
                this.scr_IP = (ctx.channel().localAddress().toString());
                String[] parts = this.scr_IP.split(":");
                this.scr_IP = parts[0]; // =<url>
              
      }/*channelActive*/
  
      @Override
      public void channelRead(ChannelHandlerContext ctx, Object msg) {
    	  	     	 
    	  if (msg instanceof HttpRequest) {
              HttpRequest req = (HttpRequest) msg;
            /*ADD intreceived_bytes*/  
        	this.intreceived_bytes += (msg.toString().getBytes()).length;
        	 	 
            /*getUri()::http://127.0.0.1:8085/String
             * we will grt /String
             */
        	String urlIn = req.getUri();
     		  		         		 
      		if("/hello".equals(urlIn)){
      			printhelloWorld(ctx);
      		}/*if /hello*/ 
      		if("/status".equals(urlIn)){
      			status(ctx);
      		
      		}/*if /status*/ 
      		if(urlIn.startsWith("/redirect?url=")){
      			String[] parts = urlIn.split("url=");
      			String part2 = parts[1]; // =<url>
      			this.url = part2;
      			/*ADD url*/
      			//System.out.println("Redirect url:" + this.url);
      			sendRedirect(ctx,part2);
      		}/*if /redirect?url=*/
      		else {
    			standartReqvest(ctx);
    			}/*else*/
      		      		
      	  }/*if (msg instanceof HttpRequest) {*/
    	  else {
    			standartReqvest(ctx);
    			}/*else*/
      }/*channelRead*/
      
      private void sendRedirect(ChannelHandlerContext ctx,String newUri) {
	        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, FOUND);
	        response.headers().set(LOCATION, newUri);
	        calculateSentBytes(response);
	        /*
            * Close the connection as soon as the error message is sent.
            * */
			ctx.write(response).addListener(ChannelFutureListener.CLOSE);
	  	}/*sendRedirect*/
  
      private void printhelloWorld(ChannelHandlerContext ctx){
    	  
    	  FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK);
	        response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
	        StringBuilder buf = new StringBuilder();
	        buf.append("<!DOCTYPE html>\r\n"	);
	        buf.append("<html><head><title>"	);
	        buf.append("My first littel server"	);
	        buf.append("</title></head><body>\r\n");
	        buf.append("<h3>Hello World!!!"		);
	        buf.append("</h3>\r\n"				);
	        buf.append("</body></html>\r\n"		);
	        ByteBuf buffer = Unpooled.copiedBuffer(buf, CharsetUtil.UTF_8);
	        response.content().writeBytes(buffer);
	        buffer.release();
	        /*ADD sent_bytes*/
	        calculateSentBytes(response);
	        /*Close the connection as soon as the error message is sent.*/
	        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);  
    	  
      }/*printhelloWorld*/
      
    private void standartReqvest(ChannelHandlerContext ctx){
    	  
    	  FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK);
	        response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
	        StringBuilder buf = new StringBuilder();
	        buf.append("<!DOCTYPE html>\r\n"	);
	        buf.append("<html><head><title>"	);
	        buf.append("My first littel server"	);
	        buf.append("</title></head><body>\r\n"		);
	        buf.append("<h3>Sorry we Did not find it ;(");
	        buf.append("</h3>\r\n"				);
	        buf.append("</body></html>\r\n"		);
	        ByteBuf buffer = Unpooled.copiedBuffer(buf, CharsetUtil.UTF_8);
	        response.content().writeBytes(buffer);
	        buffer.release();
	        calculateSentBytes(response);
	        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);  
    	  
      }/*standartReqvest*/

		private void status(ChannelHandlerContext ctx){
			 FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK);
			 response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
		     StringBuilder buf = new StringBuilder();
		     buf = (new Status().getStatus());
		     ByteBuf buffer = Unpooled.copiedBuffer(buf, CharsetUtil.UTF_8);
		     response.content().writeBytes(buffer);
		     buffer.release();
		     calculateSentBytes(response);
		     ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
		}/*status*/

     @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
          cause.printStackTrace();
          ctx.close();
      }/*exceptionCaught*/
      /**
  	 * This method add new person(scr_IP, url, sent_bytes, intreceived_bytes, speed, lastDate) 
  	 * in TrafficCounter.class;
  	 */
  	@SuppressWarnings("unused")
	private void addIDPersone(){
  		traficCoute.addPersone(scr_IP, url, sent_bytes, intreceived_bytes, speed, lastDate);
  	}
      /**
       * This method is caculate sending Byt*s
       * @param response
       */
      private void calculateSentBytes(FullHttpResponse response) {
    	  /*ADD sent_bytes*/
	        this.sent_bytes += response.toString().getBytes().length;
	    //    System.out.println("sent_bytes Of Redirect:" + this.sent_bytes);
      }/*calculateSentBytes*/
      /**
       * This method calculate speed between channelActive(...) and channelReadComplete(...)
       */
      private void calculateSpeed(){
    	  try{
    	 this.speed =  this.sent_bytes + this.intreceived_bytes;
    	 this.speed = (int) (this.speed/((this.lastWriteTime - this.currentTime)));
    	 this.speed = this.speed*1000;
    	
    	  }catch(ArithmeticException e){
    		this.speed = 9;  
    	  }
      }
	 
   
}
