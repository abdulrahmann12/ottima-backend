package com.ottima.finishing_tracking.daily_update.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "update_images", indexes = {
        @Index(name = "idx_upd_img_daily_update", columnList = "daily_update_id")
})
@Setter
@Getter
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UpdateImage)) return false;
        UpdateImage updateImage = (UpdateImage) o;
        return updateImageId != null && updateImageId.equals(updateImage.getUpdateImageId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}