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


    public ImageRoomDetail addImageRoomDetail(ImageRoomDetail imageRoomDetail) {
        return imageRoomDetailRepository.save(imageRoomDetail);
    }


    public ImageRoomDetail updateImageRoomDetail(Long id, ImageRoomDetail updatedImageRoomDetail) {
        updatedImageRoomDetail.setId(id);
        return imageRoomDetailRepository.save(updatedImageRoomDetail);
    }

    public void deleteImageRoomDetail(Long id) {
        imageRoomDetailRepository.deleteById(id);
    }

    // Xóa hàng loạt hình ảnh theo roomId
    public void deleteAllImagesByRoomId(Long roomId) {
        List<ImageRoomDetail> images = imageRoomDetailRepository.findByRoomId(roomId);
        imageRoomDetailRepository.deleteAll(images);
    }


    public List<ImageRoomDetail> findAllDetailImageByRoomId(Long roomId) {
        return imageRoomDetailRepository.findByRoomId(roomId);
    }

}
