package com.example.OneToOneChat.domain.chatRoom.controller;

import com.example.OneToOneChat.domain.chatMessage.repository.ChatRoomRepository;
import com.example.OneToOneChat.domain.chatRoom.dto.Request.ChatMessageCreateRequest;
import com.example.OneToOneChat.domain.chatMessage.dto.response.ChatRoomAllResponse;
import com.example.OneToOneChat.domain.chatMessage.dto.request.ChatRoomRequest;
import com.example.OneToOneChat.domain.chatRoom.dto.Response.ChatRoomMessageResnpose;
import com.example.OneToOneChat.domain.chatMessage.service.ChatMessageService;
import com.example.OneToOneChat.domain.chatRoom.service.ChatRoomSerivce;
import com.example.OneToOneChat.global.util.ApiUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatMessageService chatMessageService;
    private final ChatRoomSerivce chatRoomSerivce;
    private final ChatRoomRepository chatRoomRepository;

    @PostMapping("/room")
    public ResponseEntity<ApiUtil.ApiSuccessResult<Long>> createChatRoom(
            @RequestBody ChatRoomRequest chatRoomRequest
            ){

        Long saveChatRoomId = chatRoomSerivce.saveChatRoom(chatRoomRequest);
        log.info("채팅방 만들어지는 시점입니다.");
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
        log.info("ChatController에서 채팅 메세지를 불러오는 부분 ");
        List<ChatRoomMessageResnpose> chatRoomResponse = chatMessageService.readChatMessageByWriter(roomId);

     return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.OK, chatRoomResponse));
}

    @GetMapping("/all")
    public ResponseEntity<List<ChatRoomAllResponse>> allName(){

        List<ChatRoomAllResponse> all =chatMessageService.all();

        return ResponseEntity.ok().body(all);
    }

/*    @GetMapping("/todayChatNum")
    public ResponseEntity<Long> todayChat(){
        Long todayChat = chatRoomSerivce.readTodayChatRoom();

        return ResponseEntity.ok().body(todayChat);
    }

    @GetMapping("/AllChatNum")
    public ResponseEntity<Long> AllChat(){
        Long AllChatNum = (long) chatRoomRepository.findAll().size();

        return ResponseEntity.ok().body(AllChatNum);
    }*/
}
