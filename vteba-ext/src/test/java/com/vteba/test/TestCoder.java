package com.vteba.test;

import com.vteba.ext.codegen.CodeBuilder;
import com.vteba.ext.codegen.KeyType;
import com.vteba.ext.codegen.TempType;

public class TestCoder {

	public static void main(String[] args) {
		String rootPath = "D:\\Documents\\GitHub\\vteba-ext\\vteba-ext\\";
		CodeBuilder builder = new CodeBuilder(rootPath, TempType.Basic);
		builder.schema("skmbw")
		.className("EmpUser")
		.keyType(KeyType.Integer)
		.tableDesc("系统用户")
		.tableName("emp_user")
		.module("user")
		.build();

	}

}
