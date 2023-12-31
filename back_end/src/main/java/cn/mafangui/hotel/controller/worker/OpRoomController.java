package cn.mafangui.hotel.controller.worker;

import cn.mafangui.hotel.entity.Room;
import cn.mafangui.hotel.entity.RoomType;
import cn.mafangui.hotel.response.AjaxResult;
import cn.mafangui.hotel.response.ResponseTool;
import cn.mafangui.hotel.service.RoomService;
import cn.mafangui.hotel.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/op/room")
public class OpRoomController {

    @Autowired
    private RoomService roomService;
    @Autowired
    private RoomTypeService roomTypeService;

    @RequestMapping(value = "/add")
    public AjaxResult addRoom(String roomNumber, Integer typeId, String roomType, double roomPrice, double roomDiscount, int roomStatus, String remark) {
        if (roomService.selectByNumber(roomNumber) != null) return ResponseTool.failed("Số phòng đã tồn tại");
        Room room = new Room(roomNumber, typeId, roomType, roomPrice, roomDiscount, roomStatus, remark);
        RoomType rt = new RoomType();
        if (roomService.insert(room) == 1) {
            rt.setTypeId(typeId);
            rt.setRest(roomTypeService.selectById(typeId).getRest() + 1);
            if (roomTypeService.update(rt) != 1) return ResponseTool.failed("Thêm không thành công");
        } else return ResponseTool.failed("Thêm không thành công");
        return ResponseTool.success("Thêm thành công");
    }

    @RequestMapping(method = RequestMethod.POST, value = "/delete/{roomId}")
    public AjaxResult deleteRoom(@PathVariable Integer roomId) {
        if (roomService.delete(roomId) != 1) return ResponseTool.failed("Xóa không thành công");
        return ResponseTool.success("Xóa thành công");
    }

    @RequestMapping(value = "/update")
    public AjaxResult updateRoom(Integer roomId, String roomNumber, Integer typeId, String roomType, double roomPrice, double roomDiscount, int roomStatus, String remark) {
        Room room = new Room(roomNumber, typeId, roomType, roomPrice, roomDiscount, roomStatus, remark);
        room.setRoomId(roomId);
        if (roomService.update(room) != 1) return ResponseTool.failed("Cập nhật không thành công");
        return ResponseTool.success("Cập nhật thành công");
    }

    @RequestMapping(value = "/{id}")
    public AjaxResult getById(@PathVariable Integer id) {
        return ResponseTool.success(roomService.selectById(id));
    }

    @RequestMapping(value = "/type/{typeId}")
    public AjaxResult getByType(@PathVariable Integer typeId) {
        return ResponseTool.success(roomService.selectByType(typeId));
    }

    @RequestMapping(value = "/status/{statusId}")
    public AjaxResult getByStatus(@PathVariable Integer statusId) {
        return ResponseTool.success(roomService.selectByStatus(statusId));
    }

    @RequestMapping(value = "")
    public AjaxResult getAll() {
        return ResponseTool.success(roomService.selectAll());
    }
}
