package com.snackviet.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "QuanHuyen")
public class QuanHuyen {
    @Id
    @Column(name = "maQuanHuyen")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

    @Column(name = "quanHuyenID")
    @JsonProperty("DistrictID")
    private Integer districtID;

    @Column(name = "quanHuyenTen")
    @JsonProperty("DistrictName")
    private String districtName;

    @ManyToOne
    @JoinColumn(name = "maTinhThanh", referencedColumnName = "maTinhThanh")
    private TinhThanh tinhThanhId;

}
