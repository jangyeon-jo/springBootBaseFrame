package io.security.corespringsecurity.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ACCESS_IP")
@Data
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessIp implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "IP_ID", unique = true, nullable = false)
    private Long id;

    @Column(name = "IP_ADDRESS", nullable = false)
    private String ipAddress;

}


/**
 * DB 정보
 * ACCESS_IP
 * Account
 * RESOURCES
 * ROLE
 * ROLE_HIERARCHY
 * 
 * select * from ACCESS_IP;
 * select * from Account;	회원
 * select * from RESOURCES;	자원
 * select * from ROLE;		권한
 * select * from ROLE_HIERARCHY;
 * 
 * 
 * 
 * 
 * 
*/
