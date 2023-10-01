package top.bulk.socket.server;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.bulk.socket.context.WebSocketContext;
import top.bulk.socket.enums.MsgTypeEnum;
import top.bulk.socket.message.ChatMsgMessage;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

/**
 * 配置接入点
 * ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 *
 * @author 散装java
 */
@Component
@ServerEndpoint("/chat")
@Slf4j
public class WebSocketServer {

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        log.info("===> onOpen:{}", session.getId());
        // 上线，并且通知到其他人
        WebSocketContext.add(session, session.getId());
        WebSocketContext.countNotice();
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        log.info("===> onClose:{}", session.getId());
        // 下线，并且通知到其他人
        WebSocketContext.remove(session);
        WebSocketContext.countNotice();
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("===> onMessage:{},message:{}", session.getId(), message);
        // 进行消息的转发，同步到其他的客户端上
        ChatMsgMessage msg = JSON.parseObject(message, ChatMsgMessage.class);
        WebSocketContext.broadcast(MsgTypeEnum.CHAT_MSG.getCode(), msg, session);
    }

    /**
     * 监听错误
     *
     * @param session session
     * @param error   错误
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("SessionId:{},出现异常：{}", session.getId(), error.getMessage());
        error.printStackTrace();
    }

}
