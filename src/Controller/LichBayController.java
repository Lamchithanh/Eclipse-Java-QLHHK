package Controller;

import Model.LichBay;
import Service.LichBayService;
import View.QuanLyLichBay;
import View.NotificationType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LichBayController {
    private QuanLyLichBay view;
    private LichBayService service;

    public LichBayController(QuanLyLichBay view) {
        this.view = view;
        this.service = new LichBayService();
    }

      // Thêm phương thức getAllFlights
      public List<LichBay> getAllFlights() {
        return service.getAllLichBay();
    }

    public List<String> getAllFlightCodes() {
        return service.getAllMaChuyenBay();
    }

    public void addFlight(LichBay flight) {
        if (service.isDepartureOrArrivalTimeConflict(flight.getMaChuyenBay(), 
            flight.getGioKhoiHanh(), flight.getGioHaCanh())) {
            view.showNotification("Giờ khởi hành hoặc giờ hạ cánh đã trùng với lịch bay hiện có!", NotificationType.ERROR);
            return;
        }

        if (!isValidFlightTime(flight.getGioKhoiHanh(), flight.getGioHaCanh())) {
            view.showNotification("Giờ khởi hành phải sớm hơn giờ hạ cánh!", NotificationType.ERROR);
            return;
        }

        try {
            service.addLichBay(flight);
            updateFlightList();
            view.clearFields();
            view.showNotification("Thêm lịch bay thành công!", NotificationType.SUCCESS);
        } catch (Exception e) {
            view.showNotification("Lỗi khi thêm lịch bay: " + e.getMessage(), NotificationType.ERROR);
        }
    }

    public void updateFlight(LichBay flight) {
        if (!isValidFlightTime(flight.getGioKhoiHanh(), flight.getGioHaCanh())) {
            view.showNotification("Giờ khởi hành phải sớm hơn giờ hạ cánh!", NotificationType.ERROR);
            return;
        }

        try {
            service.updateLichBay(flight);
            updateFlightList();
            view.clearFields();
            view.showNotification("Cập nhật lịch bay thành công!", NotificationType.SUCCESS);
        } catch (Exception e) {
            view.showNotification("Lỗi khi cập nhật lịch bay: " + e.getMessage(), NotificationType.ERROR);
        }
    }

    public void deleteFlight(String flightCode) {
        try {
            service.deleteLichBay(flightCode);
            updateFlightList();
            view.clearFields();
            view.showNotification("Xóa lịch bay thành công!", NotificationType.SUCCESS);
        } catch (Exception e) {
            view.showNotification("Lỗi khi xóa lịch bay: " + e.getMessage(), NotificationType.ERROR);
        }
    }

    public void searchFlights(String searchQuery) {
        List<LichBay> filteredList = service.getAllLichBay().stream()
            .filter(flight -> 
                flight.getMaChuyenBay().toLowerCase().contains(searchQuery) ||
                flight.getGioKhoiHanh().toLowerCase().contains(searchQuery) ||
                flight.getGioHaCanh().toLowerCase().contains(searchQuery))
            .toList();
        view.updateTableData(filteredList);
    }

    public void updateFlightList() {
        List<LichBay> flightList = service.getAllLichBay();
        view.updateTableData(flightList);
    }

    private boolean isValidFlightTime(String departureTime, String arrivalTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            Date depTime = sdf.parse(departureTime);
            Date arrTime = sdf.parse(arrivalTime);
            return depTime.before(arrTime);
        } catch (ParseException e) {
            view.showNotification("Định dạng giờ không hợp lệ. Vui lòng nhập giờ theo định dạng HH:mm!", NotificationType.ERROR);
            return false;
        }
    }

     // Add new method to calculate flight duration
     public int calculateFlightDuration(String departureTime, String arrivalTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Date depTime = sdf.parse(departureTime);
            Date arrTime = sdf.parse(arrivalTime);
            
            long diffInMillies = arrTime.getTime() - depTime.getTime();
            // Convert to minutes
            return (int) TimeUnit.MILLISECONDS.toMinutes(diffInMillies);
        } catch (ParseException e) {
            return 0;
        }
    }

    public LichBay createFlightFromInput(String flightCode, String departureTime, 
                                       String arrivalTime, String flightDuration) {
        try {
            // Calculate duration automatically
            int duration = calculateFlightDuration(departureTime, arrivalTime);
            return new LichBay(flightCode, departureTime, arrivalTime, duration);
        } catch (Exception e) {
            view.showNotification("Lỗi khi tạo lịch bay!", NotificationType.ERROR);
            return null;
        }
    }

    // Add method to format duration
    public String formatDuration(int minutes) {
        int hours = minutes / 60;
        int mins = minutes % 60;
        return String.format("%02d:%02d", hours, mins);
    }
}