package com.focus.test.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import java.sql.Timestamp;
import javax.persistence.*;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "auth_user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer companyId;

	private String name;

	private String password;

	private String email;

	private String avatar;

	private String role;

	private Timestamp updatedAt;

	private Timestamp createdAt;
}