package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.DTO.*;
import com.bezkoder.springjwt.constants.ApplicationConstants;
import com.bezkoder.springjwt.models.Lobby;
import com.bezkoder.springjwt.models.Message;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.repository.UserRepository;
import com.bezkoder.springjwt.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;


@RestController
@RequestMapping("/api/v1")
public class WebSocketHandlerLocalTesting {

    @Autowired
    AuthController authController;

    @Autowired
    LobbyController lobbyController;

    @Autowired
    CardProcessingResource cardProcessingResource;

    @Autowired
    NextPlayerFinder nextPlayerFinder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;


    @GetMapping("/badamsatti/getmessage")
    public Message handleRequest(@RequestBody Message message) throws JsonProcessingException {
        System.out.println("<<<<<<<<<<<<<<<<<<<<<< HANDLING MESSAGING REQUEST >>>>>>>>>>>>>>>>>>>>");
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("Message object in WebSocketHandler: " + message);
        Message sendMessage = new Message();

        String destinationUrl = null;


        //TODO::::::::::::::::::::::::::::::::::::::::::::::: WIN ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        try {
            if (message.getMsg().equals(ApplicationConstants.WIN)) {
                JoinLobbyDetails joinLobbyDetails = objectMapper.readValue(objectMapper.writeValueAsString(message.getRequestBody()), JoinLobbyDetails.class);
                PointsDto pointsDto = userService.calculatePoints(joinLobbyDetails.getLobbyId());
                sendMessage.setMsg(ApplicationConstants.WIN);
                sendMessage.setRequestBody(pointsDto);
                return sendMessage;
//                destinationUrl = "/subscribe/get-subscription/" + joinLobbyDetails.getLobbyId();
//                simpMessagingTemplate.convertAndSend("/subscribe/get-subscription/" + joinLobbyDetails.getLobbyId(), sendMessage);
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
            sendMessage.setMsg(ApplicationConstants.ERROR);
            sendMessage.setRequestBody("Unable to calculate win points");
            return sendMessage;
//            if (null != destinationUrl) {
//                simpMessagingTemplate.convertAndSend(destinationUrl, sendMessage);
//            }
        }


        //TODO::::::::::::::::::::::::::::::::::::::::::::::: CHATTING_APP :::::::::::::::::::::::::::::::::::::::::::::::::::::::::

        try {
            if (message.getMsg().equals(ApplicationConstants.CHATTING_APP)) {
                ChattingWindow chattingWindow = objectMapper.readValue(objectMapper.writeValueAsString(message.getRequestBody()), ChattingWindow.class);
                User user = userRepository.findById(chattingWindow.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
                chattingWindow.setUserName(user.getUsername());
                sendMessage.setMsg(ApplicationConstants.CHATTING_APP);
                sendMessage.setRequestBody(chattingWindow);
                return sendMessage;
//                destinationUrl = "/subscribe/get-subscription/" + chattingWindow.getJoinCode();
//                simpMessagingTemplate.convertAndSend("/subscribe/get-subscription/" + chattingWindow.getJoinCode(), sendMessage);
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
            sendMessage.setMsg(ApplicationConstants.ERROR);
            sendMessage.setRequestBody("Unable to send messages");
            return sendMessage;
//            if (null != destinationUrl) {
//                simpMessagingTemplate.convertAndSend(destinationUrl, sendMessage);
//            }
        }

        //TODO::::::::::::::::::::::::::::::::::::::::::::::: JOIN_LOBBY :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

        try {
            if (message.getMsg().equals(ApplicationConstants.JOIN_LOBBY)) {
                JoinLobbyDetails joinLobbyDetails = objectMapper.readValue(objectMapper.writeValueAsString(message.getRequestBody()), JoinLobbyDetails.class);
                Lobby object = (Lobby) lobbyController.joinLobby(joinLobbyDetails.getLobbyId(), joinLobbyDetails.getUserId()).getBody();
                sendMessage.setMsg(ApplicationConstants.JOIN_LOBBY);
                sendMessage.setRequestBody(object);
                return sendMessage;
//                destinationUrl = "/subscribe/get-subscription/" + joinLobbyDetails.getLobbyId();
//                simpMessagingTemplate.convertAndSend(destinationUrl, sendMessage);
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
            sendMessage.setMsg(ApplicationConstants.ERROR);
            sendMessage.setRequestBody("Unable to join lobby.");
            return sendMessage;
//            if (null != destinationUrl) {
//                simpMessagingTemplate.convertAndSend(destinationUrl, sendMessage);
//            }
        }


        //TODO::::::::::::::::::::::::::::::::::::::::::::::: CARD_DISTRIBUTE :::::::::::::::::::::::::::::::::::::::::::::::::::::::::

        try {
            if (message.getMsg().equals(ApplicationConstants.CARD_DISTRIBUTE)) {
                JoinLobbyDetails joinLobbyDetails = objectMapper.readValue(objectMapper.writeValueAsString(message.getRequestBody()), JoinLobbyDetails.class);
                int id =cardProcessingResource.shuffleAndDistribute(joinLobbyDetails.getLobbyId());
                sendMessage.setMsg(ApplicationConstants.CARD_DISTRIBUTE);
                CardDisDto cardDisDto = new CardDisDto();
                cardDisDto.setRequestBody("GAME_STARTED");
                cardDisDto.setBadamSattiOwnerId(id);
                sendMessage.setRequestBody(cardDisDto);
                return sendMessage;
//                destinationUrl = "/subscribe/get-subscription/" + joinLobbyDetails.getLobbyId();
//                simpMessagingTemplate.convertAndSend(destinationUrl, sendMessage);

            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
            sendMessage.setMsg(ApplicationConstants.ERROR);
            sendMessage.setRequestBody("Unable to distribute cards for the current lobby");
            return sendMessage;
//            if (null != destinationUrl) {
//                simpMessagingTemplate.convertAndSend(destinationUrl, sendMessage);
//        }
        }

        //TODO::::::::::::::::::::::::::::::::::::::::::::::: FIND_NEXT_PLAYER :::::::::::::::::::::::::::::::::::::::::::::::::::::::::

        try {
            if (message.getMsg().equals(ApplicationConstants.FIND_NEXT_PLAYER)) {
                PlayerDetails playerDetails = objectMapper.readValue(objectMapper.writeValueAsString(message.getRequestBody()), PlayerDetails.class);
                PlayerDetails object = nextPlayerFinder.findNextPlayer(playerDetails);
                sendMessage.setMsg(ApplicationConstants.FIND_NEXT_PLAYER);
                sendMessage.setRequestBody(object);
                return sendMessage;
//            destinationUrl = "/subscribe/get-subscription/" + playerDetails.getLobbyJoinCode();
//            simpMessagingTemplate.convertAndSend(destinationUrl, sendMessage);
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
            sendMessage.setMsg(ApplicationConstants.ERROR);
            sendMessage.setRequestBody("Unable to find next player. Something went wrong.");
            return sendMessage;
//            if (null != destinationUrl) {
//                simpMessagingTemplate.convertAndSend(destinationUrl, sendMessage);
//            }
        }
        return null;
    }
}


