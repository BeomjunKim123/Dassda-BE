package com.dassda.service;

import com.dassda.entity.*;
import com.dassda.repository.*;
import com.dassda.request.DiaryRequest;
import com.dassda.response.DiaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final StickerRepository stickerRepository;
    private final DiaryRepository diaryRepository;
    private final DiaryImgRepository diaryImgRepository;

    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private Member member() {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        return memberRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new IllegalStateException("존재하지 않음")
                );
    }

    public void addDiary(DiaryRequest diaryRequest) throws Exception {
        Board board = boardRepository.findById(diaryRequest.getBoardId())
                .orElseThrow(() -> new Exception("해당하는 보드가 없습니다."));
        Sticker sticker = stickerRepository.findById(diaryRequest.getStickerId())
                .orElseThrow(() -> new Exception("해당하는 스티커가 없습니다."));
        Diary diary = new Diary();
        diary.setBoard(board);
        diary.setSticker(sticker);
        diary.setWrites(member());
        diary.setDiaryTitle(diaryRequest.getDiaryTitle());
        diary.setDiaryContent(diaryRequest.getContents());
        diary.setRegDate(LocalDateTime.now());
        diary.setUpdateDate(LocalDateTime.now());
        diary.setBackUp(false);
        diaryRepository.save(diary);

        for(MultipartFile file : diaryRequest.getDiaryImgs()) {
            DiaryImg diaryImg = new DiaryImg();
            String oriImgName = file.getOriginalFilename();
            String imgName = "";
            String imgUrl = "";
//mm
            if(!StringUtils.isEmpty(oriImgName)) {
                imgName = uploadFile(itemImgLocation, oriImgName, file.getBytes());
                imgUrl = "/images/item/" + imgName;
            }
            diaryImg.updateDiaryImg(oriImgName, imgName, imgUrl);
            diaryImg.setDiary(diary);
            diaryImgRepository.save(diaryImg);
        }
    }

    private String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception {
        UUID uuid = UUID.randomUUID();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = uuid.toString() + extension;
        String fileUploadFullUrl = uploadPath + "/" +savedFileName;
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(fileData);
        fos.close();
        return savedFileName;
    }

    public List<DiaryResponse> getDiaries(Long boardId) {

        return null;
    }

    public void updateDiary(DiaryRequest diaryRequest) {
        Optional<Diary> diary = diaryRepository.findById(diaryRequest.getId());
        Optional<Sticker> sticker = stickerRepository.findById(diaryRequest.getStickerId());
        List<DiaryImg> diaryImg = diaryImgRepository.findByDiaryId(diaryRequest.getId());
        Diary updateDiaryInfo = new Diary();
        updateDiaryInfo.setSticker(sticker.get());
        updateDiaryInfo.setDiaryTitle(diaryRequest.getDiaryTitle());
        updateDiaryInfo.setDiaryContent(diaryRequest.getContents());
        updateDiaryInfo.setUpdateDate(LocalDateTime.now());
        diaryRepository.save(updateDiaryInfo);
    }

    public void deleteDiary() {

    }


}
