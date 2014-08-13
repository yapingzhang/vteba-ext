package com.vteba.ext.codegen.model;

import java.util.ArrayList;
import java.util.List;

public class MethodPart {
	private String methodName;
	private String methodParam;
	private String methodBody;
	private String fieldType;
	private List<String> annotations;

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getMethodParam() {
		return methodParam;
	}

	public void setMethodParam(String methodParam) {
		this.methodParam = methodParam;
	}

	public String getMethodBody() {
		return methodBody;
	}

	public void setMethodBody(String methodBody) {
		this.methodBody = methodBody;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public List<String> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(List<String> annotations) {
		this.annotations = annotations;
	}

	public void addAnnotation(String annotation) {
	    if (annotations == null) {
	        annotations = new ArrayList<String>();
	    }
	    annotations.add(annotation);
	}
}
