package com.ottima.finishing_tracking.project.entity;

import com.ottima.finishing_tracking.project.enums.ProjectStatus;
import com.ottima.finishing_tracking.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "projects", indexes = {
        @Index(name = "idx_project_client_active", columnList = "client_id, deletes_at"),
        @Index(name = "idx_project_engineer_active", columnList = "engineer_id, deletes_at")
})@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLRestriction("deletes_at IS NULL")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID projectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "engineer_id", nullable = false)
    private User engineer;

    @Column(name = "name_ar", nullable = false)
    private String nameAr;

    @Column(name = "name_en", nullable = false)
    private String nameEn;

    @Enumerated(EnumType.STRING)
    @Column(name = "overall_status", nullable = false)
    private ProjectStatus overallStatus;;

    @Column(name = "address_ar")
    private String addressAr;

    @Column(name = "address_en")
    private String addressEn;

    @Column(name = "estimated_budget", precision = 12, scale = 2)
    private BigDecimal estimatedBudget;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "target_completion_date")
    private LocalDate targetCompletionDate;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProjectItem> projectItems = new ArrayList<>();

    // --- Audit Fields ---
    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @Column(name = "deletes_at")
    private Instant deletesAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Project)) return false;
        Project project = (Project) o;
        return projectId != null && projectId.equals(project.getProjectId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
