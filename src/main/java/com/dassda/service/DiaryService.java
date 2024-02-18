package com.dassda.service;

import com.dassda.entity.*;
import com.dassda.event.DiaryCreatedEvent;
import com.dassda.repository.*;
import com.dassda.request.DiaryImgRequest;
import com.dassda.request.DiaryRequest;
import com.dassda.response.DiaryDetailResponse;
import com.dassda.utils.GetMember;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final BoardRepository boardRepository;
    private final StickerRepository stickerRepository;
    private final DiaryRepository diaryRepository;
    private final DiaryImgRepository diaryImgRepository;
    private final LikesRepository likesRepository;
    private final CommentRepository commentRepository;
    private final ReadDiaryRepository readDiaryRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ShareRepository shareRepository;

    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private Member member() {
        return GetMember.getCurrentMember();
    }

    private Board findBoard(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalStateException("해당 일기장이 없다."));
    }

    private Sticker findSticker(Long stickerId) {
        return stickerRepository.findById(stickerId)
                .orElseThrow(() -> new IllegalStateException("해당 스티커가 없다."));
    }

    private void validateDiaryDate(Long boardId, LocalDateTime selectDate) {
        if (diaryRepository.exitsByDiaryAtSelectDate(member().getId(), boardId, selectDate)) {
            throw new IllegalStateException("이미 해당 날짜에 일기를 작성함");
        }
    }
    @Transactional
    public void addDiary(DiaryRequest diaryRequest) throws Exception {
        Long boardId = diaryRequest.getBoardId();
        Long memberId = member().getId();
        Long emotionId = diaryRequest.getEmotionId();

        Board board = findBoard(boardId);
        Sticker sticker = findSticker(emotionId);
        LocalDateTime selectTime = LocalDateTime.parse(diaryRequest.getSelectedDate());
        if(diaryRepository.exitsByDiaryAtSelectDate(memberId, boardId, selectTime)) {
            throw new IllegalStateException("이미 해당 날짜에 일기를 작성함");
        }
        if (!selectTime.isBefore(LocalDate.now().minusYears(5).atStartOfDay())) {
            throw new IllegalStateException("5년 전 일기 작성 불가");
        }
        if(selectTime.toLocalDate().isAfter(LocalDate.now())) {
            throw new IllegalStateException("미래 일기 불가");
        }
        Optional<Share> share = shareRepository.findByMemberIdAndBoardId(memberId, boardId);
            if(boardRepository.existsMemberIdAboutBoardId(memberId, boardId) || share.isPresent() && shareRepository.existsById(memberId) && Objects.equals(boardId, share.get().getBoard().getId())) {
                Diary diary = new Diary();
                diary.setBoard(board);
                diary.setSticker(sticker);
                diary.setMember(member());
                diary.setDiaryTitle(diaryRequest.getTitle());
                diary.setDiaryContent(diaryRequest.getContents());
                diary.setRegDate(LocalDateTime.now());
                diary.setSelectDate(selectTime);
                diary.setUpdateDate(LocalDateTime.now());
                diary.setBackUp(false);
                diaryRepository.save(diary);

                if(diaryRequest.getImages().isEmpty()) {
                    diaryImgRepository.save(null);
                } else {
                    for(MultipartFile file : diaryRequest.getImages()) {
                        DiaryImg diaryImg = new DiaryImg();
                        String oriImgName = file.getOriginalFilename();
                        String imgName = "";
                        String imgUrl = "";
                        if(!StringUtils.isEmpty(oriImgName)) {
                            imgName = uploadFile(itemImgLocation, oriImgName, file.getBytes());
                            imgUrl = "http://118.67.143.25:8080/root/items/" + imgName;
                        }
                        diaryImg.updateDiaryImg(oriImgName, imgName, imgUrl);
                        diaryImg.setDiary(diary);
                        diaryImg.setBackUp(false);
                        diaryImgRepository.save(diaryImg);
                    }
                }

                eventPublisher.publishEvent(new DiaryCreatedEvent(this, diary));
        }
         else {
            throw new IllegalArgumentException("참여한 일기장에만 일기를 작성할 수 있습니다.");
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
    @Transactional
    public DiaryDetailResponse getDiaries(Long memberId, Long boardId, String date) {
        String dateparsing = date.substring(0, 10);
        LocalDate selectDate = LocalDate.parse(dateparsing);
        Optional<Diary> diary = diaryRepository.findByMemberIdAndBoardIdAndSelectDate(memberId, boardId, selectDate);

        if(!diary.isPresent()) {
            throw new IllegalStateException("해당 날짜에 일기가 존재하지 않습니다.");
        }

        if(diary.get().isBackUp()) {
            throw new IllegalStateException("삭제된 일기입니다.");
        }

        DiaryDetailResponse diaryDetailResponse = new DiaryDetailResponse();
        diaryDetailResponse.setId(diary.get().getId());
        diaryDetailResponse.setMemberId(diary.get().getMember().getId());
        diaryDetailResponse.setNickname(diary.get().getMember().getNickname());
        diaryDetailResponse.setProfileUrl(diary.get().getMember().getProfile_image_url());
        diaryDetailResponse.setEmotionId(diary.get().getSticker().getId());

        List<DiaryImgRequest> imgRequests = diaryImgRepository
                .findByDiaryId(diary.get().getId())
                        .stream()
                                .map(diaryImg -> new DiaryImgRequest(diaryImg.getId(), diaryImg.getImgUrl().isEmpty() ? null : diaryImg.getImgUrl() ))
                                        .collect(Collectors.toList());
        diaryDetailResponse.setImages(imgRequests);

        diaryDetailResponse.setTitle(diary.get().getDiaryTitle());
        diaryDetailResponse.setContents(diary.get().getDiaryContent());

        String time = diaryRepository.findDiaryWithTimeAge(diary.get().getId());
        diaryDetailResponse.setTimeStamp(time);

        diaryDetailResponse.setSelectedDate(diary.get().getSelectDate());

        int likeCount = likesRepository.countByDiaryId(diary.get().getId());
        diaryDetailResponse.setLikeCount(likeCount);

        int commentCount = commentRepository.countByDiaryId(diary.get().getId());
        diaryDetailResponse.setCommentCount(commentCount);

        if(member().getId() == diary.get().getMember().getId()) {
            diaryDetailResponse.setOwned(true);
        } else {
            diaryDetailResponse.setOwned(false);
            ReadDiary readDiary = new ReadDiary();
            readDiary.setReadId(member());
            readDiary.setDiary(diary.get());
            readDiaryRepository.save(readDiary);
        }
    return diaryDetailResponse;
    }

    public void updateDiary(Long diaryId, DiaryRequest diaryRequest) throws Exception {
        Optional<Diary> diary = diaryRepository.findById(diaryId);
        if(!diary.get().getMember().getId().equals(member().getId())) {
            throw new IllegalStateException("내가 작성한 일기가 아닙니다");
        }
        Optional<Sticker> sticker = stickerRepository.findById(diaryRequest.getEmotionId());
        List<DiaryImg> diaryImg = diaryImgRepository.findByDiaryId(diaryId);
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
                imgUrl = "/root/items/" + imgName;
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
        if(!diaryOptional.get().getMember().getId().equals(member().getId())) {
            throw new IllegalStateException("내가 작성한 일기가 아닙니다");
        }
        if(diaryOptional.isPresent()) {
            Diary diary = diaryOptional.get();
            diary.setBackUp(true);
            diaryRepository.save(diary);
        } else {
            throw new IllegalStateException("삭제된 일기입니다.");
        }
    }
}
