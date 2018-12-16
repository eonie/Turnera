package org.turnera.server.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tur_job_detail")
public class JobDetail {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String jobBean;
    private String cron;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJobBean() {
        return jobBean;
    }

    public void setJobBean(String jobBean) {
        this.jobBean = jobBean;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }
}
