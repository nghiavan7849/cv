package com.snackviet.model;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.Nationalized;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "TrangThaiHoaDon")
public class TrangThaiHoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer maTrangThaiHoaDon;
    @Nationalized
    String tenTrangThai;
    
    @JsonIgnore
    @OneToMany(mappedBy = "trangThaiHoaDon")
    List<HoaDon> listHoaDon;
}
