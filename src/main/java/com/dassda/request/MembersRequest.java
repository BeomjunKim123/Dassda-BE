package com.dassda.request;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class MembersRequest {

    private MultipartFile profileUrl;
    private String nickname;

}
