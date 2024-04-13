package com.example.OneToOneChat.domain.chatRomm.controller;

import com.example.OneToOneChat.domain.chatRomm.dto.Request.ChatMessageCreateRequest;
import com.example.OneToOneChat.domain.chatMessage.dto.response.ChatRoomAllResponse;
import com.example.OneToOneChat.domain.chatMessage.dto.request.ChatRoomRequest;
import com.example.OneToOneChat.domain.chatRomm.dto.Response.ChatRoomMessageResnpose;
import com.example.OneToOneChat.domain.chatMessage.repository.ChatRoomRepository;
import com.example.OneToOneChat.domain.chatMessage.service.ChatMessageService;
import com.example.OneToOneChat.domain.chatRomm.service.ChatRoomSerivce;
import com.example.OneToOneChat.global.util.ApiUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageService chatMessageService;
    private final ChatRoomSerivce chatRoomSerivce;



    @PostMapping("/room")
    public ResponseEntity<ApiUtil.ApiSuccessResult<Long>> createChatRoom(
            @RequestBody ChatRoomRequest chatRoomRequest
            ){

        Long saveChatRoomId = chatRoomSerivce.saveChatRoom(chatRoomRequest);
        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.CREATED, saveChatRoomId));
    }
    @PostMapping("/{roomId}/message")
    public ResponseEntity<ApiUtil.ApiSuccessResult<Long>> saveChatMessage(
            @RequestBody ChatMessageCreateRequest chatMessageCreateRequest,
            @PathVariable(name ="roomId") Long roomId
            ){
        Long saveChatId = chatMessageService.saveMessage(chatMessageCreateRequest,roomId);
        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.CREATED, saveChatId));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ApiUtil.ApiSuccessResult<List<ChatRoomMessageResnpose>>> readAll(
            @PathVariable(name="roomId") Long roomId
            )
    {
        List<ChatRoomMessageResnpose> chatRoomResponse = chatMessageService.readChatMessageByWriter(roomId);

     return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.OK, chatRoomResponse));
}

    @GetMapping("/all")
    public ResponseEntity<List<ChatRoomAllResponse>> allName(){

        List<ChatRoomAllResponse> all =chatMessageService.all();

        return ResponseEntity.ok().body(all);
    }
}
