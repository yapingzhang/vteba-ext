package ${packages}.model;

#foreach($c in $importList)
${c}
#end
#if($pojo == false)
import javax.persistence.Entity;
import javax.persistence.Table;
#end

import com.vteba.common.model.AstModel;

#foreach($c in $annotationList)
${c}
#end
#if($pojo == false)
@Entity
@Table(name = "${table}"#if(${schema}), schema = "${schema}"#end#if(${catalog}), catalog = "${catalog}"#end)
#end
public class ${className} implements AstModel {

	private static final long serialVersionUID = 3391739370239888528L;
	
	#foreach($c in $fieldList)
${c}
	#end
	
	public ${className}() {
	}
	
	#set($size = $getsetMethodList.size())
public ${className}(#foreach($c in $getsetMethodList)#if($velocityCount != 1), #end${c.fieldType} ${c.methodParam}#end) {
        super();
	    #foreach($c in $getsetMethodList)
#if($velocityCount == 1)
this.${c.methodParam} = ${c.methodParam};
	#else
	this.${c.methodParam} = ${c.methodParam};
	#end#end}
	
	#foreach($c in $getsetMethodList)
#if($c.annotations)
#foreach($an in $c.annotations)
${an}
#end
#end
    public ${c.fieldType} get${c.methodName}() {
		return this.${c.methodParam};
	}

	public void set${c.methodName}(${c.fieldType} ${c.methodParam}) {
		this.${c.methodParam} = ${c.methodParam};
	}
	
	#end

}
