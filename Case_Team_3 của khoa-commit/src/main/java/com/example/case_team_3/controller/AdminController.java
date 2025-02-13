package com.example.case_team_3.controller;

import com.example.case_team_3.model.Employee;
import com.example.case_team_3.model.Room;
import com.example.case_team_3.service.EmployeeService;
import com.example.case_team_3.service.RoomService;
import com.example.case_team_3.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomTypeService roomTypeService;

    // Dashboard của ADMIN
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";  // Ví dụ file: src/main/resources/templates/dashboard.html
    }

    // ----------------------- Employee -----------------------
    // Hiển thị form đăng ký nhân viên
    @GetMapping("/register-employee")
    public String showEmployeeRegistrationForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "register-employee"; // Ví dụ file: src/main/resources/templates/register-employee.html
    }

    // Xử lý đăng ký nhân viên
    @PostMapping("/register-employee")
    public String registerEmployee(@ModelAttribute Employee employee) {
        employeeService.registerEmployee(employee);
        return "redirect:/admin/dashboard?employeeCreated=true";
    }

    // ----------------------- Room Management -----------------------
    // Liệt kê danh sách phòng
    @GetMapping("/rooms")
    public String listRooms(Model model) {
        model.addAttribute("rooms", roomService.getAllRooms());
        return "admin/room-list"; // Ví dụ file: src/main/resources/templates/admin/room-list.html
    }

    // Hiển thị form tạo phòng mới
    @GetMapping("/rooms/create")
    public String showRoomCreateForm(Model model) {
        model.addAttribute("room", new Room());
        model.addAttribute("roomTypes", roomTypeService.getAllRoomTypes());
        return "admin/room-form"; // Ví dụ file: src/main/resources/templates/admin/room-form.html
    }

    // Xử lý tạo phòng mới
    @PostMapping("/rooms/create")
    public String createRoom(@ModelAttribute Room room) {
        roomService.createRoom(room);
        return "redirect:/admin/rooms?created=true";
    }

    // Hiển thị form chỉnh sửa phòng
    @GetMapping("/rooms/edit/{roomId}")
    public String showRoomEditForm(@PathVariable("roomId") Integer roomId, Model model) {
        Room room = roomService.getRoomById(roomId);
        model.addAttribute("room", room);
        model.addAttribute("roomTypes", roomTypeService.getAllRoomTypes());
        return "admin/room-form";  // Sử dụng chung form với tạo mới
    }

    // Xử lý cập nhật phòng
    @PostMapping("/rooms/edit/{roomId}")
    public String updateRoom(@PathVariable("roomId") Integer roomId, @ModelAttribute Room room) {
        roomService.updateRoom(roomId, room);
        return "redirect:/admin/rooms?updated=true";
    }

    // Xóa phòng
    @GetMapping("/rooms/delete/{roomId}")
    public String deleteRoom(@PathVariable("roomId") Integer roomId) {
        roomService.deleteRoom(roomId);
        return "redirect:/admin/rooms?deleted=true";
    }
}
