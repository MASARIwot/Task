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
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import static io.netty.handler.codec.http.HttpResponseStatus.*;//FOUND
import static io.netty.handler.codec.http.HttpVersion.*;//HTTP_1_1
import static io.netty.handler.codec.http.HttpHeaders.Names.*;//LOCATION
/**
 * this if main Handler
 * @author Саня
 */
public class MyHttpServerHandler extends ChannelInboundHandlerAdapter { /* SimpleChannelInboundHandler<FullHttpRequest>{*/
	TrafficCounter traficCoute = TrafficCounter.getInstance();
	/*
	 *Persone ID 
	 */
	private String scr_IP = "def";
	/*
	 *Last  Redirect url
	 */
	private String url = "def"; 
	/*
	 *byt statistic 
	 */
	private int sent_bytes = 0; 
	private int intreceived_bytes = 0; 
	private double speed;
	/*
	 * Data counter 
	 */
	private String lastDate = " "; 
	
	private long currentTime = 0;
	private long lastWriteTime = 0;
	private AddPersoneThread addIfo = null;
	
	
      @Override
     public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    	  TrafficCounter.Offconnect();
    	  /*ADD last Write Time*/
    	   this.lastWriteTime = System.currentTimeMillis();
    	  calculateSpeed();
    	  /*ADD persone in new Tread*/
    	 
    	  		if(!"/favicon.ico".equals(this.scr_IP)){
		    		  if(!"def".equals(this.url)){
		    	  addIfo = new AddPersoneThread(this.scr_IP, this.url, this.sent_bytes, this.intreceived_bytes, this.speed, this.lastDate);
		    	  addIfo.start();  
		    	 }}
		    	  
        if(ctx != null)
	    ctx.flush();
		super.channelReadComplete(ctx);
		
      }/*channelReadComplete END*/
      
     @Override
      public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	  TrafficCounter.Addconnect();
    	  super.channelActive(ctx);
		
      }/*channelActive END*/
      private void writeStartServerInformation(ChannelHandlerContext ctx) {
    	  		/*ADD Current Time*/
    	        this.currentTime = System.currentTimeMillis();
    	        /*Add DATE*/
                this.lastDate = ((new Date()).toString());
                /*Add src_IP*/
                this.scr_IP = (ctx.channel().localAddress().toString());
                String[] parts1 = this.scr_IP.split(":");
                this.scr_IP = parts1[0]; // =<url>
              
      }
  
      @Override
      public void channelRead(ChannelHandlerContext ctx, Object msg) {
    	    	  
    	  if (msg instanceof HttpRequest) {
          HttpRequest req = (HttpRequest) msg;
          	// Handle a bad request. Modife StanDart Rwqvest!!!!!
//	         if (!req.getDecoderResult().isSuccess()) {
//	        	  DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST,
//	                      Unpooled.copiedBuffer("BAD_REQUEST", CharsetUtil.UTF_8));
//	              response.headers().set(/*HttpHeaders.Names.*/CONTENT_TYPE, "text/html; charset=UTF-8");
//	              response.headers().set(/*HttpHeaders.Names.*/CONTENT_LENGTH, response.content().readableBytes());
//	              ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
//	              return;
//	        }          
          
          	if(HttpHeaders.is100ContinueExpected(req)){
          		/*100 HttpResponseStatus.CONTINUE — сервер удовлетворён начальными сведениями о запросе, клиент может продолжать пересылать заголовки*/
          		ctx.write(new DefaultFullHttpResponse(/*HttpVersion.*/HTTP_1_1,/*HttpResponseStatus.*/CONTINUE));
          	}
           	//filter the request from the client to /favicon.ico from browser (tested on Google Chrome) 
          	if("/favicon.ico".equalsIgnoreCase(req.getUri())){
          		/*200 HttpResponseStatus.OK — успешный запрос. Если клиентом были запрошены какие-либо данные, то они находятся в заголовке и/или теле сообщения.*/
          		ctx.write(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK));
          		ctx.flush();
          	}else {
          		writeStartServerInformation(ctx); 
            /*ADD intreceived_bytes*/  
        	this.intreceived_bytes += (msg.toString().getBytes()).length;
        	/*getUri()::http://127.0.0.1:8085/String
             * we will grt /String
             */
        	String urlIn = req.getUri();	 
            if("/hello".equals(urlIn)){
      			this.url = "/hello";
      			printhelloWorld(ctx);
      		}/*if /hello*/ 
      		if("/status".equals(urlIn)){
      			this.url = "/status";
      			status(ctx);
      		}/*if /status*/ 
      		if(urlIn.startsWith("/redirect?url=")){
      			String[] parts = urlIn.split("url=");
      			String part2 = parts[1]; // =<url>
      			this.url = part2;
      			/*ADD url*/
      			sendRedirect(ctx,part2);
      		}/*if /redirect?url=*/
      		else {
      			this.url = urlIn;
    			standartReqvest(ctx);
    			}/*2else*/
          	}/*1 else {*/
      		      		
      	  }/*if (msg instanceof HttpRequest) {*/
    	 
      }/*channelRead*/
      
      private void sendRedirect(ChannelHandlerContext ctx,String newUri) {
	        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, FOUND);
	        response.headers().set(LOCATION, newUri);
	        calculateSentBytes(response);
	        /*
            * Close the connection as soon as the error message is sent.
            * */
	        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
			//ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
	  	}/*sendRedirect END*/
  
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
	        try {
	        	Thread.sleep(10000); 
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    	  
      }/*printhelloWorld END*/
      
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
    	  
      }/*standartReqvest END*/

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
		}/*status END*/

     @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    	 cause.printStackTrace();
    	 DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR,
                 Unpooled.copiedBuffer("500 Internal Server Error", CharsetUtil.UTF_8));
         response.headers().set(/*HttpHeaders.Names.*/CONTENT_TYPE, "text/html; charset=UTF-8");
         response.headers().set(/*HttpHeaders.Names.*/CONTENT_LENGTH, response.content().readableBytes());
         ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
                 
    	 
      }/*exceptionCaught END*/
      /**
  	 * This method add new person(scr_IP, url, sent_bytes, intreceived_bytes, speed, lastDate) 
  	 * in TrafficCounter.class;
  	 */
  	@SuppressWarnings("unused")
  	@Deprecated
	private void addIDPersone(){
  		traficCoute.addPersone(scr_IP, url, sent_bytes, intreceived_bytes, speed, lastDate);
  	}
      /**
       * This method is caculate sending Byt*s
       * @param response
       */
      private void calculateSentBytes(FullHttpResponse response) {
    	      this.sent_bytes += response.toString().getBytes().length;
	    
      }/*calculateSentBytes END*/
      /**
       * This method calculate speed between channelActive(...) and channelReadComplete(...)
       */
      private void calculateSpeed(){
    	  try{
    		  	  
    		  this.speed =  this.sent_bytes + this.intreceived_bytes;
    		  this.speed =  (this.speed/((this.lastWriteTime - this.currentTime)));
    	      this.speed =  (this.speed*1000);
    	      
    	
    	  }catch(ArithmeticException e){
    		this.speed = 9;  
    	  }
      }
	 
   
}
