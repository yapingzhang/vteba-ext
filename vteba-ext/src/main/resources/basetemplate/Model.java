package com.vteba.${packages}.model;

#foreach($c in $importList)
${c}
#end

import javax.persistence.Entity;
import javax.persistence.Table;

import com.vteba.common.model.AstModel;

@Entity
@Table(name = "${table}", catalog = "${schema}")
public class ${className} implements AstModel {

	private static final long serialVersionUID = 3391739370239888528L;
	
	#foreach($c in $fieldList)
${c}
	#end
	
	public ${className}() {
	}
	
	#foreach($c in $getsetMethodList)
public ${c.fieldType} get${c.methodName}() {
		return this.${c.methodParam};
	}

	public void set${c.methodName}(${c.fieldType} ${c.methodParam}) {
		this.${c.methodParam} = ${c.methodParam};
	}
	
	#end

}
