package com.vteba.test;

import com.vteba.ext.codegen.CodeBuilder;
import com.vteba.ext.codegen.KeyType;
import com.vteba.ext.codegen.TempType;

public class TestCoder {

	public static void main(String[] args) {
		String rootPath = "D:\\Documents\\GitHub\\vteba-ext\\vteba-ext\\";
		CodeBuilder builder = new CodeBuilder(rootPath, TempType.Mybatis);
		builder.schema("skmbw")
		.className("EmpUser")
		.setSrcPath("src/main/java/")
		.keyType(KeyType.Integer)
		.tableDesc("系统用户")
		.tableName("emp_user")
		.module("com.skmbw.user")
		.setGenAction(false)
		.setGenDao(false)
		.setGenMapper(false)
		.setGenModel(false)
		.setGenService(false)
		.build();

	}

}
