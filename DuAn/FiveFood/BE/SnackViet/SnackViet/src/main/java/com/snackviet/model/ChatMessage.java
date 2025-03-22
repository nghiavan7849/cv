package com.snackviet.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@ToString
public class ChatMessage {
    private String nickname;
    private String content;
    private Date timeStamp;
}
