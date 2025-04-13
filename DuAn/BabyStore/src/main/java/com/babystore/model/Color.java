package com.babystore.model;

import java.util.List;

import org.hibernate.annotations.Nationalized;

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
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Colors")
public class Color {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Nationalized
    private String name;
    @OneToMany(mappedBy = "color")
    private List<ProductDetail> productDetails;
}
