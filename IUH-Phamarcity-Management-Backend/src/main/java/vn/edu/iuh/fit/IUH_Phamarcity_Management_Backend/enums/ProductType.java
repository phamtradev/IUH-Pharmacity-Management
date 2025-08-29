package vn.edu.iuh.fit.IUH_Phamarcity_Management_Backend.enums;

public enum ProductType {
    MEDICINE("Thuốc"),
    MEDICALSUPPLIES("Vật tư y tế"),
    DIETARYSUPPLEMENT("Thực phẩm chức năng"),
    BABYCARE("Chăm sóc trẻ em"),
    MEDICALEQUIPMENT("Thiết bị y tế");

    private final String description;

    ProductType(String description) {
        this.description = description;
    }
}
