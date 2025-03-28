package com.snackviet.model;



import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "TinhThanh")
public class TinhThanh {
    @Id
    @Column(name = "maTinhThanh")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

    @Column(name = "tinhThanhID")
    @JsonProperty("ProvinceID")
    private Integer provinceID;

    @Column(name = "tinhThanhTen")
    @JsonProperty("ProvinceName")
    private String provinceName;

    // @JsonIgnore
    // @OneToMany(mappedBy = "tinhThanh", cascade = CascadeType.REMOVE, orphanRemoval = true)
    // private List<QuanHuyen> tinhThanhId;

}
