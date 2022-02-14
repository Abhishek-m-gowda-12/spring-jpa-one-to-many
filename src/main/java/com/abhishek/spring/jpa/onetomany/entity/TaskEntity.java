package com.abhishek.spring.jpa.onetomany.entity;

import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "task", schema = "public")
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    private String description;

    @CreationTimestamp
    private Timestamp createdDate;

    @OneToMany(orphanRemoval = true, mappedBy = "taskEntity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<SubTaskEntity> subTaskEntities = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskEntity that = (TaskEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(createdDate, that.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, createdDate);
    }

    public void setSubTaskEntities(Set<SubTaskEntity> subTaskEntities) {
        this.subTaskEntities.clear();
        if (!subTaskEntities.isEmpty()) {
            subTaskEntities.forEach(subTaskEntity -> {
                subTaskEntity.setTaskEntity(this);
                this.subTaskEntities.add(subTaskEntity);
            });
        }
    }

    @Override
    public String toString() {
        return "TaskEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
}
