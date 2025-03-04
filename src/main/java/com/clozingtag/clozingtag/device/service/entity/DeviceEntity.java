package com.clozingtag.clozingtag.device.service.entity;

import com.clozingtag.clozingtag.device.service.enums.DeviceState;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "devices")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class DeviceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Brand is required")
    @Column(nullable = false)
    private String brand;

    @Enumerated(EnumType.STRING)
    private DeviceState state;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime creationTime;

//    @CreatedBy
//    private String createdBy;


}