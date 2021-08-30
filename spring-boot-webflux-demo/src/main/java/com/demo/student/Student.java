package com.demo.student;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Table("students")
public class Student implements Persistable<String>{
	
	@Id
    private String id;
	
    private String name;
	
    private boolean male;
	
    private int grade;
	
    private String status;
	
	private LocalDateTime createdDate;
	
	private LocalDateTime lastModifiedDate;
	
	private LocalDateTime deletedDate;
	
	public Student() {
		// TODO Auto-generated constructor stub
	}
	
	public Student(String id) {
        this.id = id;
    }

    public Student(String id, String name, boolean male, int grade, String status) {
        this.id = id;
        this.name = name;
        this.male = male;
        this.grade = grade;
        this.status = status;
    }
    
    public Student(Student student, String id) {
    	this.id = id;
        this.name = student.name;
        this.male = student.male;
        this.grade = student.grade;
        this.status = student.status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	
	public LocalDateTime getDeletedDate() {
		return deletedDate;
	}

	public void setDeletedDate(LocalDateTime deletedDate) {
		this.deletedDate = deletedDate;
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", name=" + name + ", male=" + male + ", grade=" + grade + ", status=" + status
				+ ", createdDate=" + createdDate + ", lastModifiedDate=" + lastModifiedDate + ", deletedDate="
				+ deletedDate + "]";
	}

	@Override
	@JsonIgnore
	public boolean isNew() {
		// TODO Auto-generated method stub
		if(this.createdDate == null) {
			this.createdDate = LocalDateTime.now(); // because this executed only when deal with database
		}
		
		if(this.deletedDate != null) {
			this.deletedDate = null;
			return true; // when undo delete from redis, use insert statement
		}
		
		return this.lastModifiedDate == null;
	}
}