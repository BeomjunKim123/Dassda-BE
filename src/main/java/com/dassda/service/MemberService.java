package com.dassda.service;

import com.dassda.entity.Member;
import com.dassda.repository.MemberRepository;
import com.dassda.request.MembersRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private Member member() {
        return memberRepository
                .findByEmail(
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName()
                )
                .orElseThrow(() -> new IllegalStateException("로그인 다시하셈"));
    }
    public void updateProfile(MembersRequest membersRequest) throws Exception {
        Optional<Member> member = memberRepository.findById(member().getId());
        if (!membersRequest.getProfileUrl().isEmpty()) {
            MultipartFile file = membersRequest.getProfileUrl();
            String oriImgName = file.getOriginalFilename();
            String imgName = "";
            String imgUrl = "";
            if(!StringUtils.isEmpty(oriImgName)) {
                imgName = uploadFile(itemImgLocation, oriImgName, file.getBytes());
                imgUrl = "/Users/proxy/items/item/" + imgName;
            }
            member.get().setProfile_image_url(imgUrl);
            memberRepository.save(member.get());
        }
        if(!membersRequest.getNickname().isEmpty()) {
            member.get().setNickname(membersRequest.getNickname());
            memberRepository.save(member.get());
        }

    }
    private String uploadFile(String updatePate, String originalFileName, byte[] fileDate) throws Exception {
        File uploadDir = new File(updatePate);
        if(!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        UUID uuid = UUID.randomUUID();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = uuid.toString() + extension;
        String fileUploadFullUrl = updatePate + "/" + savedFileName;
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(fileDate);
        fos.close();
        return savedFileName;
    }
}
