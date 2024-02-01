package com.dassda.service;

import com.dassda.entity.*;
import com.dassda.repository.*;
import com.dassda.request.DiaryRequest;
import com.dassda.response.DiaryDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final StickerRepository stickerRepository;
    private final DiaryRepository diaryRepository;
    private final DiaryImgRepository diaryImgRepository;
    private final LikesRepository likesRepository;
    private final CommentRepository commentRepository;

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
        Sticker sticker = stickerRepository.findById(diaryRequest.getEmotionId())
                .orElseThrow(() -> new Exception("해당하는 스티커가 없습니다."));

        LocalDateTime selectTime = LocalDateTime.parse(diaryRequest.getSelectedDate());

        Diary diary = new Diary();
        diary.setBoard(board);
        diary.setSticker(sticker);
        diary.setMember(member());
        diary.setDiaryTitle(diaryRequest.getTitle());
        diary.setDiaryContent(diaryRequest.getContents());
        diary.setRegDate(LocalDateTime.now());
        diary.setSelectDate(selectTime);
        diary.setUpdateDate(LocalDateTime.now());
        diary.setRead(false);
        diary.setBackUp(false);
        diaryRepository.save(diary);

        for(MultipartFile file : diaryRequest.getImages()) {
            DiaryImg diaryImg = new DiaryImg();
            String oriImgName = file.getOriginalFilename();
            String imgName = "";
            String imgUrl = "";
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

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        UUID uuid = UUID.randomUUID();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = uuid.toString() + extension;
        String fileUploadFullUrl = uploadPath + "/" +savedFileName;
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(fileData);
        fos.close();
        return savedFileName;
    }

    public List<DiaryDetailResponse> getDiaries(Long boardId, String date) {
        LocalDateTime selectDate = LocalDateTime.parse(date);
        List<Diary> selectDiary = diaryRepository.findByBoardIdAndRegDateWithFetchJoin(boardId, selectDate);

        List<DiaryDetailResponse> diaryDetailResponses = new ArrayList<>();

        for (Diary detail : selectDiary) {
            DiaryDetailResponse diaryDetailResponse = new DiaryDetailResponse();

            diaryDetailResponse.setId(detail.getId());

            diaryDetailResponse.setWriterId(detail.getMember().getId());
            diaryDetailResponse.setNickname(detail.getMember().getNickname());
            diaryDetailResponse.setProfileUrl(detail.getMember().getProfile_image_url());

            diaryDetailResponse.setEmotionId(detail.getSticker().getId());
            diaryDetailResponse.setTitle(detail.getDiaryTitle());
            diaryDetailResponse.setContents(detail.getDiaryContent());
            diaryDetailResponse.setRegDate(detail.getRegDate());
            diaryDetailResponse.setLikeCount(likesRepository.countByDiaryId(detail.getId()));
            diaryDetailResponse.setCommentCount(commentRepository.countByDiaryId(detail.getId()));

            List<DiaryImg> diaryImgs = diaryImgRepository.findByDiaryId(detail.getId());
            diaryDetailResponse.setImages(diaryImgs);

            diaryDetailResponses.add(diaryDetailResponse);

        }

        return diaryDetailResponses;
    }

    public void updateDiary(DiaryRequest diaryRequest) throws Exception {
        Optional<Diary> diary = diaryRepository.findById(diaryRequest.getId());
        Optional<Sticker> sticker = stickerRepository.findById(diaryRequest.getEmotionId());
        List<DiaryImg> diaryImg = diaryImgRepository.findByDiaryId(diaryRequest.getId());
        for(DiaryImg image : diaryImg) {
            diaryImgRepository.delete(image);
        }

        for(MultipartFile file : diaryRequest.getImages()) {
            DiaryImg diaryImgs = new DiaryImg();
            String oriImgName = file.getOriginalFilename();
            String imgName = "";
            String imgUrl = "";
            if(!StringUtils.isEmpty(oriImgName)) {
                imgName = uploadFile(itemImgLocation, oriImgName, file.getBytes());
                imgUrl = "/images/item/" + imgName;
            }
            diaryImgs.updateDiaryImg(oriImgName, imgName, imgUrl);
            diaryImgs.setDiary(diary.get());
            diaryImgRepository.save(diaryImgs);
        }

        Diary updateDiaryInfo = new Diary();
        updateDiaryInfo.setSticker(sticker.get());
        updateDiaryInfo.setDiaryTitle(diaryRequest.getTitle());
        updateDiaryInfo.setDiaryContent(diaryRequest.getContents());
        updateDiaryInfo.setUpdateDate(LocalDateTime.now());
        diaryRepository.save(updateDiaryInfo);

    }

    public void deleteDiary(Long diaryId) {
        Optional<Diary> diaryOptional = diaryRepository.findById(diaryId);
        if(diaryOptional.isPresent()) {
            Diary diary = diaryOptional.get();
            diary.setBackUp(true);
            diaryRepository.save(diary);
        } else {
            throw new IllegalStateException("삭제된 일기입니다.");
        }
    }
}
