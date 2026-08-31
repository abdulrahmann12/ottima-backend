package com.ottima.finishing_tracking.project.entity;

import com.ottima.finishing_tracking.project.enums.ProjectItemStatus;
import com.ottima.finishing_tracking.standard_item.entity.StandardItem;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "project_items", indexes = {
        @Index(name = "idx_proj_item_proj_seq", columnList = "project_id, sequence_order"),
        @Index(name = "idx_proj_item_std_item", columnList = "standard_item_id")
})@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID projectItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "standard_item_id", nullable = false)
    private StandardItem standardItem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ProjectItemStatus status = ProjectItemStatus.PENDING;

    @Column(precision = 12, scale = 2)
    private BigDecimal budget;

    @Column(name = "weight_percentage", precision = 5, scale = 2)
    private BigDecimal weightPercentage;

    @Column(name = "completion_percentage", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal completionPercentage = BigDecimal.ZERO;

    @Column(name = "sequence_order", nullable = false)
    private Integer sequenceOrder;

    @Column(name = "general_notes", columnDefinition = "TEXT")
    private String generalNotes;

    @Version
    @Column(name = "version", nullable = false, columnDefinition = "bigint default 0")
    private Long version;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectItem)) return false;
        ProjectItem projectItem = (ProjectItem) o;
        return projectItemId != null && projectItemId.equals(projectItem.getProjectItemId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}