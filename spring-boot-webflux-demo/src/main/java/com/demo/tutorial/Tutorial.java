package com.demo.tutorial;



import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Table("tutorials")
public class Tutorial {

	@Id
	private long id;

	private String title;

	private String description;

	private boolean published;
	
	private LocalDateTime createdDate;

	public Tutorial() {

	}

	public Tutorial(String title, String description, boolean published) {
		this.title = title;
		this.description = description;
		this.published = published;
		this.createdDate = LocalDateTime.now();
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean isPublished) {
		this.published = isPublished;
	}

	@Override
	public String toString() {
		return "Tutorial [id=" + id + ", title=" + title + ", desc=" + description + ", published=" + published + ", created_date=" + createdDate + "]";
	}

	public LocalDateTime getCratedDate() {
		return createdDate;
	}

	public void setCratedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

}
