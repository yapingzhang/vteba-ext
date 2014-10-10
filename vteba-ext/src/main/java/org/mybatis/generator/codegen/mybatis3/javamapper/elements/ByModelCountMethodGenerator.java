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
 * 根据model的统计计数方法产生器
 * @author yinlei
 * @since 2013-12-15 9:45
 */
public class ByModelCountMethodGenerator extends
        AbstractJavaMapperMethodGenerator {

    public ByModelCountMethodGenerator() {
        super();
    }

    @Override
    public void addInterfaceElements(Interface interfaze) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
//        FullyQualifiedJavaType type = new FullyQualifiedJavaType(
//                introspectedTable.getBaseRecordType());
        FullyQualifiedJavaType type = new FullyQualifiedJavaType("com.vteba.tx.jdbc.params.QueryBean");
        importedTypes.add(type);
        importedTypes.add(FullyQualifiedJavaType.getNewListInstance());

        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addJavaDocLine("根据params所携带条件进行计数，条件是等于，且是and关系。");
        method.addJavaDocLine("@param params 查询条件");
        
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());

        method.setName(introspectedTable.getCountByStatementId());
        method.addParameter(new Parameter(type, "params")); //$NON-NLS-1$

        context.getCommentGenerator().addGeneralMethodComment(method,
                introspectedTable);

        if (context.getPlugins()
                .clientSelectByExampleWithoutBLOBsMethodGenerated(method,
                        interfaze, introspectedTable)) {
            interfaze.addImportedTypes(importedTypes);
            interfaze.addMethod(method);
        }
    }

}
