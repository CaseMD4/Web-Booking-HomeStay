package com.example.case_team_3.controller;

import com.example.case_team_3.model.Employee;
import com.example.case_team_3.model.Room;
import com.example.case_team_3.model.RoomType;
import com.example.case_team_3.model.User;
import com.example.case_team_3.model.ImageRoomDetail;
import com.example.case_team_3.service.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageUploadService imgUploadService;

    @Autowired
    private ImageRoomDetailService imageRoomDetailService;


    @GetMapping("/image-details/{id}")
    public String showImageDetails(@PathVariable Long id, Model model) {
        List<ImageRoomDetail> images = imageRoomDetailService.findAllDetailImageByRoomId(id);
        model.addAttribute("images", images);
        Room room = roomService.getRoomById(Math.toIntExact(id));
        model.addAttribute("room", room);

        System.out.println(room.toString());
        return "room-details-by-admin";
    }


    @GetMapping("/edit-room/{id}")
    public String showEditRoomForm(@PathVariable Long id, Model model) {
        // Fetch the room by ID
        Room room = roomService.findById(id);

        if (room == null) {
            // Handle case when room is not found
            return "redirect:/admin/all-rooms?error=Room not found";
        }

        List<RoomType> roomTypes = roomService.getAllRoomTypes();
        model.addAttribute("roomTypes", roomTypes);
        model.addAttribute("room", room);
        return "edit-room-with-file";
    }


    @PostMapping("/edit-room/{id}")
    public String updateRoom(@PathVariable Long id,
                             @ModelAttribute("room") Room roomDetails,
                             @RequestParam(required = false) MultipartFile[] imgFiles,
                             BindingResult bindingResult,
                             Model model) {
        // Validate room details
        if (bindingResult.hasErrors()) {
            model.addAttribute("roomTypes", roomService.getAllRoomTypes());
            return "edit-room";
        }

        try {

            // Nếu có tệp hình ảnh được tải lên, xử lý việc lưu hình ảnh
            if (imgFiles != null && imgFiles.length > 0) {
                String imagePaths = imgUploadService.uploadImages(imgFiles);
                // Cập nhật đường dẫn hình ảnh vào đối tượng roomDetails
                roomDetails.setRoomImg(imagePaths); // Giả sử roomDetails có phương thức setRoomImg
            } else {
                // Không có hình ảnh mới, giữ nguyên hình ảnh hiện tại
                Room existingRoom = roomService.getRoomById(Math.toIntExact(id));
                roomDetails.setRoomImg(existingRoom.getRoomImg());
            }

            // Cập nhật thông tin phòng
            roomService.updateRoom(id, roomDetails);
            return "redirect:/admin/all-rooms?success=Room updated successfully";
        } catch (Exception e) {
            model.addAttribute("error", "Error updating room: " + e.getMessage());
            model.addAttribute("roomTypes", roomService.getAllRoomTypes());
            return "edit-room-with-file";
        }
    }



    // Delete Room Method
    @GetMapping("/delete-room/{id}")
    public String deleteRoom(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            // Attempt to delete the room
            roomService.deleteRoom(id);
            redirectAttributes.addFlashAttribute("successMessage", "Phòng đã được xóa thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa phòng: " + e.getMessage());
        }
        return "redirect:/admin/all-rooms";
    }


    @GetMapping("/all-rooms")
    public String showAllRooms(Model model) {
        List<Room> rooms = roomService.getAllRooms();
        model.addAttribute("rooms", rooms);
        return "all-rooms-with-file";
    }


    @GetMapping("/create-room")
    public String showCreateRoomForm(Model model, HttpServletRequest request) {
        // Explicitly add CSRF token to the model
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (csrfToken != null) {
            model.addAttribute("_csrf", csrfToken);
        }

        Room room = new Room();
        List<RoomType> roomTypes = roomService.getAllRoomTypes();
        model.addAttribute("room", room);
        model.addAttribute("roomTypes", roomTypes);
        return "create-room-with-file";
    }


    @PostMapping("/create-room")
    public String createRoom(@ModelAttribute Room room,
                             @RequestParam(required = false) Integer existingRoomTypeId,
                             @RequestParam(required = false) String newRoomTypeName,
                             @RequestParam(required = false) MultipartFile[] imgFiles, // Updated parameter
                             RedirectAttributes redirectAttributes) {
        try {
            RoomType roomType;
            if (existingRoomTypeId != null) {
                roomType = roomService.getRoomTypeById(existingRoomTypeId);
            } else if (StringUtils.hasText(newRoomTypeName)) {
                roomType = roomService.createRoomType(newRoomTypeName);
            } else {
                redirectAttributes.addFlashAttribute("error", "Vui lòng chọn hoặc tạo loại phòng");
                return "redirect:/admin/create-room";
            }

            // Upload images and get the paths
            String roomImg = imgUploadService.uploadImages(imgFiles);

            room.setRoomType(roomType);
            room.setRoomStatus(Room.RoomStatus.available);
            room.setRoomImg(roomImg);
            roomService.createRoom(room);

            redirectAttributes.addFlashAttribute("success", "Tạo phòng thành công");
            return "redirect:/admin/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi tạo phòng: " + e.getMessage());
            return "redirect:/admin/create-room";
        }
    }


    @PostMapping("/create-room-type")
    @ResponseBody
    public ResponseEntity<?> createRoomType(@RequestBody Map<String, String> payload) {

        try {
            String roomTypeName = payload.get("roomTypeName");
            RoomType newRoomType = roomService.createRoomType(roomTypeName);

            return ResponseEntity.ok(Map.of(
                    "id", newRoomType.getRoomTypeId(),
                    "roomTypeName", newRoomType.getRoomTypeName()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }


    @PostMapping("/room/change-status")
    public ResponseEntity<?> changeRoomStatus(
            @RequestParam Integer roomId,
            @RequestParam String status) {
        try {
            Room updatedRoom = roomService.updateRoomStatus(roomId, status);
            return ResponseEntity.ok(Map.of(
                    "message", "Cập nhật trạng thái phòng thành công",
                    "roomId", updatedRoom.getRoomId(),
                    "newStatus", updatedRoom.getRoomStatus().toString()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Không thể cập nhật trạng thái phòng: " + e.getMessage()
            ));
        }
    }


    @GetMapping("/switch-to-customer-view")
    public String switchToCustomerView(Model model) {
        // Retrieve available rooms for customer view
        List<Room> availableRooms = roomService.getAllRooms();
        model.addAttribute("rooms", availableRooms);
        return "all-rooms-by-customer-f-view";
    }


    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/register-employee")
    public String showEmployeeRegistrationForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "register-employee";
    }

    @PostMapping("/register-employee")
    public String registerEmployee(@ModelAttribute Employee employee, RedirectAttributes redirectAttributes) {
        try {
            employeeService.createEmployee(employee);
            redirectAttributes.addFlashAttribute("successMessage", "Tạo nhân viên thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/all-employees";
    }


    // user management
    @GetMapping("/all-users")
    public String showAllUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "all-users";
    }

//    create là đăng ký

    @GetMapping("/edit-user/{id}")
    public String showEditUserForm(@PathVariable Integer id, Model model) {
        try {
            User user = userService.findById(id);
            model.addAttribute("user", user);
            return "edit-user";
        } catch (EntityNotFoundException e) {
            return "redirect:/admin/all-users?error=User not found";
        }
    }

    @PostMapping("/edit-user/{id}")
    public String updateUser(
            @PathVariable Integer id,
            @RequestParam String userUsername,
            @RequestParam String userEmail,
            @RequestParam(required = false) String userPassword,
            RedirectAttributes redirectAttributes
    ) {
        try {

            User existingUser = userService.findById(id);

            User userToUpdate = new User();
            userToUpdate.setUserId(id);
            userToUpdate.setUserUsername(userUsername);
            userToUpdate.setUserEmail(userEmail);
            userToUpdate.setUserPassword(userPassword);

            userService.updateUser(id, userToUpdate);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật người dùng thành công");
            return "redirect:/admin/all-users";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
            return "redirect:/admin/edit-user/" + id;
        }
    }


    @GetMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable Integer id,
                             RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa người dùng thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/all-users";
    }


    @GetMapping("/create-user")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user", new User());
        return "create-user-by-admin";
    }

    @PostMapping("/create-user")
    public String createUser(@ModelAttribute User user,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        try {
            userService.createUser(user);
            redirectAttributes.addFlashAttribute("errorMessage", "Tạo người dùng thành công");
            return "redirect:/admin/create-user";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
            return "redirect:/admin/create-user";
        }
    }

//    employee management

    @GetMapping("/all-employees")
    public String showAllEmployees(Model model) {
        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        return "all-employees";
    }


    @GetMapping("/edit-employee/{id}")
    public String showEditEmployeeForm(@PathVariable Integer id, Model model) {
        try {
            Employee employee = employeeService.findById(id);
            model.addAttribute("employee", employee);
            return "edit-employee";
        } catch (EntityNotFoundException e) {
            return "redirect:/admin/all-employees?error=Employee not found";
        }
    }

    @PostMapping("/edit-employee/{id}")
    public String updateEmployee(@PathVariable Integer id,
                                 @ModelAttribute Employee employee,
                                 RedirectAttributes redirectAttributes) {
        try {
            employeeService.updateEmployee(id, employee);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật nhân viên thành công");
            return "redirect:/admin/all-employees";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
            return "redirect:/admin/edit-employee/" + id;
        }
    }

    @GetMapping("/delete-employee/{id}")
    public String deleteEmployee(@PathVariable Integer id,
                                 RedirectAttributes redirectAttributes) {
        try {
            employeeService.deleteEmployee(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa nhân viên thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/all-employees";
    }



}
