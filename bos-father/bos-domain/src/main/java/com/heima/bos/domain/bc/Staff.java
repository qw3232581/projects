package com.heima.bos.domain.bc;
// Generated 2017-7-18 16:26:12 by Hibernate Tools 3.2.2.GA


import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Staff generated by hbm2java
 */
@Entity
@Table(name = "bc_staff"
        , catalog = "bos"
        , uniqueConstraints = @UniqueConstraint(columnNames = "TELEPHONE")
)
public class Staff implements java.io.Serializable {


    private String id;
    private String name;
    private String telephone;
    private Integer haspda;
    private Integer deltag = 0;
    private String station;
    private String standard;
    private Set<Decidedzone> decidedzones = new HashSet<Decidedzone>(0);

    public Staff() {
    }


    public Staff(String telephone) {
        this.telephone = telephone;
    }

    public Staff(String name, String telephone, Integer haspda, Integer deltag, String station, String standard, Set<Decidedzone> decidedzones) {
        this.name = name;
        this.telephone = telephone;
        this.haspda = haspda;
        this.deltag = deltag;
        this.station = station;
        this.standard = standard;
        this.decidedzones = decidedzones;
    }

    @GenericGenerator(name = "generator", strategy = "uuid")
    @Id
    @GeneratedValue(generator = "generator")

    @Column(name = "ID", unique = true, nullable = false, length = 32)
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "NAME", length = 20)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "TELEPHONE", unique = true, nullable = false, length = 20)
    public String getTelephone() {
        return this.telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Column(name = "HASPDA")
    public Integer getHaspda() {
        return this.haspda;
    }

    public void setHaspda(Integer haspda) {
        this.haspda = haspda;
    }

    @Column(name = "DELTAG")
    public Integer getDeltag() {
        return this.deltag;
    }

    public void setDeltag(Integer deltag) {
        this.deltag = deltag;
    }

    @Column(name = "STATION", length = 40)
    public String getStation() {
        return this.station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    @Column(name = "STANDARD", length = 100)
    public String getStandard() {
        return this.standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "staff")
    @JSONField(serialize=false)
    public Set<Decidedzone> getDecidedzones() {
        return this.decidedzones;
    }

    public void setDecidedzones(Set<Decidedzone> decidedzones) {
        this.decidedzones = decidedzones;
    }
}


