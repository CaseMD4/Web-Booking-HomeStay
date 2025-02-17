package com.example.case_team_3.controller;


import com.example.case_team_3.model.Room;
import com.example.case_team_3.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/cleaner")
@PreAuthorize("hasRole('ROLE_CLEANER')")
public class CleaningController {
    @Autowired
    private RoomService roomService;
    @GetMapping("/showCleanerHome")
    public ModelAndView showRoom() {
        ModelAndView mav = new ModelAndView("/cleanerHome");
        mav.addObject("rooms", roomService.getRoomsByStatus("cleaning"));
        return mav;
    }
    @PostMapping("/update")
    public String updateRoom(@RequestParam(value = "roomId", required = false) Integer roomId) {
        roomService.updateRoomStatusToAvailable(roomId);
        return "redirect:/cleaner/showCleanerHome";

    }
}


