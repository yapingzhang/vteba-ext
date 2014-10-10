/*
 *  Copyright 2009 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.mybatis.generator.codegen.mybatis3.javamapper.elements;

import java.util.Set;
import java.util.TreeSet;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;

/**
 * 根据model的分页查询方法产生器
 * @author yinlei
 * @since 2013-12-15 9:45
 */
public class ByModelUpdateBatchMethodGenerator extends
        AbstractJavaMapperMethodGenerator {

    public ByModelUpdateBatchMethodGenerator() {
        super();
    }

    @Override
    public void addInterfaceElements(Interface interfaze) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setName(introspectedTable
                .getUpdateBatchStatementId());
        method.addJavaDocLine("根据params所携带条件更新指定字段，条件是等于，且是and关系。");
        
        method.addJavaDocLine("@param params update的where条件，以及定位分区表的条件");
		FullyQualifiedJavaType parameterType = new FullyQualifiedJavaType("com.vteba.tx.jdbc.params.UpdateBean");
		method.addParameter(new Parameter(parameterType, "params"));
		importedTypes.add(parameterType);
        
//        method.addJavaDocLine("@param record 要更新的数据");
//        method.addJavaDocLine("@param params update的where条件");
//        FullyQualifiedJavaType parameterType =
//            introspectedTable.getRules().calculateAllFieldsClass();
//        method.addParameter(new Parameter(parameterType,
//                "record", "@Param(\"record\")"));
//        importedTypes.add(parameterType);
//
//        FullyQualifiedJavaType exampleType = new FullyQualifiedJavaType(
//                introspectedTable.getBaseRecordType());
//        method.addParameter(new Parameter(exampleType,
//                "params", "@Param(\"params\")"));
//        //importedTypes.add(exampleType);
//
//        importedTypes.add(new FullyQualifiedJavaType(
//                "org.apache.ibatis.annotations.Param"));

        context.getCommentGenerator().addGeneralMethodComment(method,
                introspectedTable);

        addMapperAnnotations(interfaze, method);
        
        if (context.getPlugins()
                .clientUpdateByExampleSelectiveMethodGenerated(method, interfaze,
                        introspectedTable)) {
            interfaze.addImportedTypes(importedTypes);
            interfaze.addMethod(method);
        }
    }

    public void addMapperAnnotations(Interface interfaze, Method method) {
        return;
    }
}
