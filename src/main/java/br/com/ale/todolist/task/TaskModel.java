package br.com.ale.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SqlResultSetMapping;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity(name="tb_tasks")
@Data
@Getter
@Setter
public class TaskModel {
	
	@Id
	@GeneratedValue(generator="UUID")
	private UUID id;
	private String description;
	
	@Column(length=50)
	private String title;
	private LocalDateTime startAt;
	private LocalDateTime endAt;
	private String priority;
	
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	
	private UUID idUser;
	
	

}
