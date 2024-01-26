package com.dassda.response;

import com.dassda.entity.Member;
import com.dassda.entity.Reply;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Setter
@Getter
@Schema(description = "일기 조회 응답 데이터")
public class DiaryResponse {

    private Member members;
    private Integer stickerId;
    private List<MultipartFile> images;
    private String diaryTitle;
    private String contents;
    private Integer likesCount;
    private Integer commentCount;
    private List<List<Reply>> commentList;

}
