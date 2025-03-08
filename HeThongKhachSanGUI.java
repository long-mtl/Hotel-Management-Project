
package canhan;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.text.DecimalFormat;

// Lớp đại diện cho một phòng khách sạn
class Phong {
    private int soPhong;
    private boolean daDat;
    private String tenKhach;
    private LocalDate ngayNhanPhong;
    private double giaPhong;

    public Phong(int soPhong, double giaPhong) {
        this.soPhong = soPhong;
        this.daDat = false;
        this.tenKhach = "";
        this.giaPhong = giaPhong;
    }

    public int getSoPhong() { return soPhong; }
    public boolean isDaDat() { return daDat; }
    public String getTenKhach() { return tenKhach; }
    public LocalDate getNgayNhanPhong() { return ngayNhanPhong; }
    public double getGiaPhong() { return giaPhong; }

    public void datPhong(String tenKhach, LocalDate ngayNhanPhong) {
        this.daDat = true;
        this.tenKhach = tenKhach;
        this.ngayNhanPhong = ngayNhanPhong;
    }

    public double traPhong(LocalDate ngayTraPhong) {
        if (ngayNhanPhong == null || ngayTraPhong.isBefore(ngayNhanPhong)) {
            return 0;
        }
        long soNgayLuuTru = ChronoUnit.DAYS.between(ngayNhanPhong, ngayTraPhong);
        this.daDat = false;
        this.tenKhach = "";
        this.ngayNhanPhong = null;
        return soNgayLuuTru * giaPhong;
    }

    @Override
    public String toString() {
        return "Phòng " + soPhong + " - " + (daDat ? "Đã đặt bởi: " + tenKhach + " | Giá: " + giaPhong + " VND/ngày" : "Trống | Giá: " + giaPhong + " VND/ngày");
    }
}

public class HeThongKhachSanGUI extends JFrame {
    private static final int SO_PHONG = 10;
    private Phong[] danhSachPhong;
    private JTextArea textArea;
    private JTextField tenKhachField, ngayNhanField, ngayTraField, soPhongField;
    private DecimalFormat df;

    public HeThongKhachSanGUI() {
        danhSachPhong = new Phong[SO_PHONG];
        df = new DecimalFormat("###,### VND");
        for (int i = 0; i < SO_PHONG; i++) {
            danhSachPhong[i] = new Phong(i + 1, 500000 + (i * 100000));
        }

        setTitle("Hệ Thống Quản Lý Khách Sạn");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 240, 240));

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("Số phòng:"));
        soPhongField = new JTextField();
        panel.add(soPhongField);
        panel.add(new JLabel("Tên khách:"));
        tenKhachField = new JTextField();
        panel.add(tenKhachField);
        panel.add(new JLabel("Ngày nhận (yyyy-mm-dd):"));
        ngayNhanField = new JTextField();
        panel.add(ngayNhanField);
        panel.add(new JLabel("Ngày trả (yyyy-mm-dd):"));
        ngayTraField = new JTextField();
        panel.add(ngayTraField);

        JButton datPhongButton = new JButton("Đặt phòng");
        datPhongButton.setBackground(new Color(100, 149, 237));
        datPhongButton.setForeground(Color.WHITE);
        datPhongButton.addActionListener(e -> datPhong());
        panel.add(datPhongButton);

        JButton traPhongButton = new JButton("Trả phòng");
        traPhongButton.setBackground(new Color(220, 20, 60));
        traPhongButton.setForeground(Color.WHITE);
        traPhongButton.addActionListener(e -> traPhong());
        panel.add(traPhongButton);

        add(panel, BorderLayout.SOUTH);

        hienThiDanhSachPhong();
        setVisible(true);
    }

    private void hienThiDanhSachPhong() {
        textArea.setText("--- DANH SÁCH PHÒNG ---\n\n");
        for (Phong phong : danhSachPhong) {
            textArea.append(phong.toString() + "\n");
        }
    }

    private void datPhong() {
        try {
            int soPhong = Integer.parseInt(soPhongField.getText());
            if (soPhong < 1 || soPhong > SO_PHONG || danhSachPhong[soPhong - 1].isDaDat()) {
                JOptionPane.showMessageDialog(this, "Phòng không hợp lệ hoặc đã được đặt.");
                return;
            }
            String tenKhach = tenKhachField.getText();
            LocalDate ngayNhan = LocalDate.parse(ngayNhanField.getText());
            danhSachPhong[soPhong - 1].datPhong(tenKhach, ngayNhan);
            JOptionPane.showMessageDialog(this, "Đặt phòng thành công!");
            hienThiDanhSachPhong();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ.");
        }
    }

    private void traPhong() {
        try {
            int soPhong = Integer.parseInt(soPhongField.getText());
            if (soPhong < 1 || soPhong > SO_PHONG || !danhSachPhong[soPhong - 1].isDaDat()) {
                JOptionPane.showMessageDialog(this, "Phòng không hợp lệ hoặc chưa được đặt.");
                return;
            }
            LocalDate ngayTra = LocalDate.parse(ngayTraField.getText());
            double thanhTien = danhSachPhong[soPhong - 1].traPhong(ngayTra);
            JOptionPane.showMessageDialog(this, "Trả phòng thành công! Tổng tiền: " + df.format(thanhTien));
            hienThiDanhSachPhong();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HeThongKhachSanGUI::new);
    }
}
