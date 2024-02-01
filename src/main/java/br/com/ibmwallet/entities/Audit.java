package br.com.ibmwallet.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.Date;


@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Audit { //Aqui fazemos uma Virtual Class de Auditoria, para ser herdada pelas transações, ou outras entidades da aplicação, que sempre irão conter a data de criação
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    protected LocalDate date;

    @PrePersist
    protected void prePersist() {
        if (date == null) {
            date = LocalDate.now();
        }
    }
}