package com.dassda.service;

import com.dassda.entity.*;
import com.dassda.event.DiaryCreatedEvent;
import com.dassda.repository.*;
import com.dassda.request.DiaryImgRequest;
import com.dassda.request.DiaryRequest;
import com.dassda.request.DiaryUpdateRequest;
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
import java.time.format.DateTimeFormatter;
import java.util.*;
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

    private void validateDiaryDate(Long boardId, LocalDate selectDate) {
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
        LocalDate selectTime = LocalDateTime.parse(diaryRequest.getSelectedDate()).toLocalDate();
        if(diaryRepository.exitsByDiaryAtSelectDate(memberId, boardId, selectTime)) {
            throw new IllegalStateException("이미 해당 날짜에 일기를 작성함");
        }
        boolean isOwnerOrSharedMember = boardRepository.existsMemberIdAboutBoardId(boardId, memberId) ||
                shareRepository.existsByBoardIdAndMemberId(boardId, memberId);
        if (!isOwnerOrSharedMember) {
            throw new IllegalStateException("참여한 일기장이 아닙니다.");
        }
        if(selectTime.isAfter(LocalDate.now())) {
            throw new IllegalStateException("미래 일기 불가");
        }

        Diary diary = new Diary();
        diary.setBoard(board);
        diary.setSticker(sticker);
        diary.setMember(member());
        diary.setDiaryTitle(diaryRequest.getTitle());
        diary.setDiaryContent(diaryRequest.getContents());
        diary.setRegDate(LocalDateTime.now());
        diary.setSelectDate(LocalDateTime.parse(diaryRequest.getSelectedDate()));
        diary.setUpdateDate(LocalDateTime.now());
        diary.setBackUp(false);
        diaryRepository.save(diary);

        if(diaryRequest.getImages() == null) {

        } else {
            for(MultipartFile file : diaryRequest.getImages()) {
                DiaryImg diaryImg = new DiaryImg();
                String oriImgName = file.getOriginalFilename();
                String imgName = "";
                String imgUrl = "";
                if(!StringUtils.isEmpty(oriImgName)) {
                    imgName = uploadFile(itemImgLocation, oriImgName, file.getBytes());
                    imgUrl = "https://dassda.today/images/" + imgName;
                }
                diaryImg.updateDiaryImg(oriImgName, imgName, imgUrl);
                diaryImg.setDiary(diary);
                diaryImg.setBackUp(false);
                diaryImgRepository.save(diaryImg);
            }
        }
        eventPublisher.publishEvent(new DiaryCreatedEvent(this, diary));
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
    @Transactional(readOnly = true)
    public List<DiaryDetailResponse> getDiary(Long diaryId) {
        Long memberId = member().getId();
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new IllegalStateException("해당 날짜에 일기가 존재하지 않습니다"));
        validateDiry(diary);

        Long boardId = diary.getBoard().getId();
        LocalDate selectedDateTime = diary.getSelectDate().toLocalDate();

        return diaryRepository.findByBoardIdAndSelectedDate(boardId, selectedDateTime)
                .stream().map(this::convertToDiaryResponse)
                .collect(Collectors.toList());
    }
    private void validateDiry(Diary diary) {
        if(diary.isBackUp()) {
            throw new IllegalStateException("삭제된 일기입니다");
        }
    }
    private DiaryDetailResponse convertToDiaryResponse(Diary diary) {
        return new DiaryDetailResponse(
                diary.getId(),
                diary.getMember().getId(),
                diary.getMember().getNickname(),
                diary.getMember().getProfile_image_url(),
                diary.getSticker().getId(),
                diaryImgRepository.findByDiaryId(diary.getId())
                        .stream()
                        .map(img -> new DiaryImgRequest(img.getId(), img.getImgUrl().isEmpty() ? null : img.getImgUrl()))
                        .collect(Collectors.toList()),
                diary.getDiaryTitle(),
                diary.getDiaryContent(),
                diaryRepository.findDiaryWithTimeAge(diary.getId()),
                diary.getSelectDate(),
                likesRepository.countByDiaryId(diary.getId()),
                commentRepository.countByDiaryId(diary.getId()) +
                        Optional.of(commentRepository.countRepliesByDiaryId(diary.getId())).orElse(0),
                diary.getMember().getId().equals(member().getId()),
                likesRepository.existsByDiaryIdAndMemberId(diary.getId(), member().getId())
        );
    }

    @Transactional
    public DiaryDetailResponse getDiaries(Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new IllegalStateException("해당 날짜에 일기가 존재하지 않습니다"));
        Long current = member().getId();

        DiaryDetailResponse diaryDetailResponse = new DiaryDetailResponse(
            diaryId,
                diary.getMember().getId(),
                diary.getMember().getNickname(),
                diary.getMember().getProfile_image_url(),
                diary.getSticker().getId(),
                diaryImgRepository.findByDiaryId(diaryId)
                        .stream()
                        .map(diaryImg -> new DiaryImgRequest(diaryImg.getId(), diaryImg.getImgUrl().isEmpty() ? null : diaryImg.getImgUrl()))
                        .collect(Collectors.toList()),
                diary.getDiaryTitle(),
                diary.getDiaryContent(),
                diaryRepository.findDiaryWithTimeAge(diaryId),
                diary.getSelectDate(),
                likesRepository.countByDiaryId(diaryId),
                commentRepository.countByDiaryId(diaryId),
                diaryIsOwned(diary),
                likesRepository.existsByDiaryIdAndMemberId(diaryId, current)
        );
    return diaryDetailResponse;
    }
    private boolean diaryIsOwned(Diary diary) {
        Long diaryId = diary.getId();
        Long memberId = member().getId();
        if (!memberId.equals(diary.getMember().getId())) {
            boolean isReadDiary = readDiaryRepository.existsByMemberIdAndDiaryId(diaryId, memberId);
            if (isReadDiary) {
                ReadDiary readDiary = new ReadDiary();
                readDiary.setReadId(member());
                readDiary.setDiary(diary);
                readDiaryRepository.save(readDiary);
            }
            return false;
        } else {
            return true;
        }
    }
    public void updateDiary(Long diaryId, DiaryUpdateRequest diaryRequest) throws Exception {
        Optional<Diary> diary = diaryRepository.findById(diaryId);
        if(!diary.get().getMember().getId().equals(member().getId())) {
            throw new IllegalStateException("내가 작성한 일기가 아닙니다");
        }
        Optional<Sticker> sticker = stickerRepository.findById(diaryRequest.getEmotionId());
//        List<DiaryImg> diaryImg = diaryImgRepository.findByDiaryId(diaryId);
//        for(DiaryImg image : diaryImg) {
//            diaryImgRepository.delete(image);
//        }

        Set<Long> requestImgIds = new HashSet<>(diaryRequest.getImgId());

// 데이터베이스에서 해당 diaryId에 해당하는 모든 DiaryImg 엔티티를 조회
        List<DiaryImg> existingImgs = diaryImgRepository.findByDiaryId(diaryId);

// 삭제할 DiaryImg 엔티티를 식별
        List<DiaryImg> imgsToDelete = existingImgs.stream()
                .filter(img -> !requestImgIds.contains(img.getId()))
                .toList();

// 식별된 DiaryImg 엔티티들을 데이터베이스에서 삭제
        for (DiaryImg img : imgsToDelete) {
            diaryImgRepository.delete(img);
        }

        for(MultipartFile file : diaryRequest.getImages()) {
            DiaryImg diaryImgs = new DiaryImg();
            String oriImgName = file.getOriginalFilename();
            String imgName = "";
            String imgUrl = "";
            if(!StringUtils.isEmpty(oriImgName)) {
                imgName = uploadFile(itemImgLocation, oriImgName, file.getBytes());
                imgUrl = "https://dassda.today/images/" + imgName;
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
