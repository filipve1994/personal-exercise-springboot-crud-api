package com.filipve1994.personalexercisespringbootcrudapi.persistence.auditing;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDate;
import java.util.Date;

/**
 * http://progressivecoder.com/spring-boot-jpa-auditing-example-with-auditoraware-interface/
 * https://attacomsian.com/blog/spring-data-jpa-auditing
 * https://www.baeldung.com/database-auditing-jpa
 * https://www.kindsonthegenius.com/2019/09/12/auditing-in-spring-bootstep-by-step-tutorial/
 * @param <U>
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable<U> {

    @CreatedBy
    public U createdBy;

    @CreatedDate
    public Date createdDate;

    @LastModifiedBy
    public U lastModifiedBy;

    @LastModifiedDate
    // @Temporal(TemporalType.TIMESTAMP)
    public Date lastModifiedDate;
}
