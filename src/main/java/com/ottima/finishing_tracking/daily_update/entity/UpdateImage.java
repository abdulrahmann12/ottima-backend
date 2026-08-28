package com.ottima.finishing_tracking.daily_update.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "update_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateImage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID updateImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_update_id", nullable = false)
    private DailyUpdate dailyUpdate;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "is_approved")
    private Boolean approved;
}