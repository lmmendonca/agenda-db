package model;

public class Phone {

    private Integer phoneId;
    private String phone;

    public Phone(Integer phoneId, String phone) {
        this.phoneId = phoneId;
        this.phone = phone;
    }

    public Phone(String phone) {
        this.phone = phone;
    }

    public Integer getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(Integer phoneId) {
        this.phoneId = phoneId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "phoneId=" + phoneId +
                ", phone='" + phone + '\'' +
                '}';
    }
}
