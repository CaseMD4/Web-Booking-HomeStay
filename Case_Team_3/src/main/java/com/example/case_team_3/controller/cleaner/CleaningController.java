package com.example.case_team_3.controller.cleaner;

import com.example.case_team_3.service.cashier_and_cleaner.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/cleaner-home")
public class CleaningController {
    @Autowired
     private RoomService roomService;
    @GetMapping()
    public ModelAndView showRoom() {
        ModelAndView mav = new ModelAndView("/cleanerManagement/cleanerHome");
        mav.addObject("rooms", roomService.getRoomsByStatus("cleaning"));
        return mav;
    }
    @PostMapping("/update")
    public String updateRoom(@RequestParam(value = "roomId", required = false) Integer roomId) {
        roomService.updateRoomStatusToAvailable(roomId);
        return "redirect:/cleaner-home";
    }
}
