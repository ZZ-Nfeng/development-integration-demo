package top.bulk.socket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import top.bulk.socket.context.WebSocketContext;
import top.bulk.socket.enums.MsgTypeEnum;
import top.bulk.socket.message.ChatMsgMessage;

/**
 * top.bulk.lock.zk.controller
 *
 * @author 散装java
 */
@Controller
@RequestMapping("/socket")
public class WebSocketTestController {
    /**
     * 测试发送消息
     *
     * @param str 消息
     */
    @GetMapping("/send/{str}")
    public void send(@PathVariable("str") String str) {
        ChatMsgMessage chatMsgMessage = new ChatMsgMessage().setMsg(str);
        WebSocketContext.broadcast(MsgTypeEnum.CHAT_MSG.getCode(), chatMsgMessage, null);
    }

    @GetMapping("/")
    public ModelAndView test() {
        return new ModelAndView("index.html");
    }
}
