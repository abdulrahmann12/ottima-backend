package com.ottima.finishing_tracking.daily_update.entity;

import com.ottima.finishing_tracking.daily_update.enums.UpdateStatus;
import com.ottima.finishing_tracking.project.entity.ProjectItem;
import com.ottima.finishing_tracking.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "daily_updates")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID dailyUpdateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_item_id", nullable = false)
    private ProjectItem projectItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "engineer_id", nullable = false)
    private User engineer;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UpdateStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by_admin_id")
    private User approvedByAdmin;

    @OneToMany(mappedBy = "dailyUpdate", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UpdateImage> images = new ArrayList<>();

    @CreationTimestamp
    private Instant createdAt;
}