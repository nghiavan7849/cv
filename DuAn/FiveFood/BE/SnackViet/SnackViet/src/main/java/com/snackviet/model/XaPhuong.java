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
@Table(name = "XaPhuong")
public class XaPhuong {
    @Id
    @Column(name = "maXaPhuong")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

    @Column(name = "xaPhuongID")
    @JsonProperty("WardCode")
    private Integer wardCode;

    @Column(name = "xaPhuongTen")
    @JsonProperty("WardName")
    private String wardName;

    @ManyToOne
    @JoinColumn(name = "maQuanHuyen", referencedColumnName = "maQuanHuyen")
    private QuanHuyen quanHuyenId;
}
