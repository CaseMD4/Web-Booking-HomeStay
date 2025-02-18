package com.example.case_team_3.service;


import com.example.case_team_3.model.ImageRoomDetail;
import com.example.case_team_3.repository.ImageRoomDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageRoomDetailService {
    @Autowired
    private ImageRoomDetailRepository imageRoomDetailRepository;


    public void addImageRoomDetail(Long imageId, ImageRoomDetail imageRoomDetail) {
        imageRoomDetailRepository.save(imageRoomDetail);
    }


    public void updateImageRoomDetail(Long id, ImageRoomDetail updatedImageRoomDetail) {
        updatedImageRoomDetail.setId(id);
        imageRoomDetailRepository.save(updatedImageRoomDetail);
    }

    public void deleteImageRoomDetail(Long id) {
        imageRoomDetailRepository.deleteById(id);
    }

//    // Xóa hàng loạt hình ảnh theo roomId
//    public void deleteAllImagesByRoomId(Long roomId) {
//        List<ImageRoomDetail> images = imageRoomDetailRepository.findByRoomId(roomId);
//        imageRoomDetailRepository.deleteAll(images);
//    }

    public int getMaxImageRoomDetailPlaceCountPlusOne() {
        Integer maxCount = imageRoomDetailRepository.findMaxImageRoomDetailPlaceCount();
        return (maxCount != null ? maxCount : 0) + 1;
    }


    public List<ImageRoomDetail> findAllDetailImageByRoomId(Long roomId) {
        return imageRoomDetailRepository.findByRoomId(roomId);
    }

}
