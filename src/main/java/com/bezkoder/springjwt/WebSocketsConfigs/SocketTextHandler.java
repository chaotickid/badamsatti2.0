//package com.bezkoder.springjwt.WebSocketsConfigs;
//
//
//import com.bezkoder.springjwt.repository.UserRepository;
//import com.bezkoder.springjwt.services.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import java.io.IOException;
//
//@Component
//public class SocketTextHandler extends TextWebSocketHandler {
//
////    @Autowired
//
////
////    @Autowired
////    UserRepository userRepository;
//
//
//    private WebSocketSessionManager webSocketSessionManager;
//
//    public SocketTextHandler(WebSocketSessionManager webSocketSessionManager) {
//        this.webSocketSessionManager = webSocketSessionManager;
//    }
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        this.webSocketSessionManager.addWebSocketSession(session);
//    }
//
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        this.webSocketSessionManager.removeWebSocketSession(session);
//    }
//
//    @Override
//    public void handleTextMessage(WebSocketSession session, TextMessage message)
//            throws IOException {
//        String payload = message.getPayload();
//
//
////        Test test = new Test();
////        System.out.println("Repo object: "+test.get());
//
//
////        UserService userService = new UserService();
////        userService.calculatePoints(9295847);
//        this.webSocketSessionManager.getWebSocketSessionsExcept(session).forEach(webSocketSession -> {
//            try {
//                webSocketSession.sendMessage(new TextMessage("message = "+payload));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
//    }
//}