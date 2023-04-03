package com.sonouno.bookshelf.exceptions;

import lombok.Data;

@Data
public class ResourceForbiddenException extends RuntimeException {

	private String resourceName;
	private String fieldName;
	private Object fieldValue;

	public ResourceForbiddenException(String resourceName, String fieldName, Object fieldValue) {
		super(String.format("%s forbidden with %s : '%s'", resourceName, fieldName, fieldValue));
		this.resourceName = resourceName;
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}
}

