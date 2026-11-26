package vn.iotstar.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Size(min = 3, max = 50, message = "Tên đăng nhập phải từ 3 đến 50 ký tự")
    @Column(name = "username", length = 50, nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    @Column(name = "email", length = 150, nullable = false, unique = true)
    private String email;

    @Column(name = "fullname", columnDefinition = "nvarchar(100)")
    private String fullname;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "avatar", length = 500)
    private String avatar;

    // 1: User thường, 2: Quản trị viên (Admin)
    @Column(name = "roleid")
    private Integer roleid = 1;

    // 0: Khóa / Chưa kích hoạt, 1: Hoạt động
    @Column(name = "status")
    private Integer status = 1;

    @Column(name = "code", length = 10)
    private String code;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createddate")
    private Date createddate = new Date();

    public User() {
    }

    public User(Integer id, String username, String password, String email, String fullname, String phone,
                String avatar, Integer roleid, Integer status, String code, Date createddate) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullname = fullname;
        this.phone = phone;
        this.avatar = avatar;
        this.roleid = roleid;
        this.status = status;
        this.code = code;
        this.createddate = createddate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getRoleid() {
        return roleid;
    }

    public void setRoleid(Integer roleid) {
        this.roleid = roleid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getCreateddate() {
        return createddate;
    }

    public void setCreateddate(Date createddate) {
        this.createddate = createddate;
    }

    public boolean isAdmin() {
        return this.roleid != null && this.roleid == 2;
    }
}
