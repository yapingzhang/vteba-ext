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
package org.mybatis.generator.codegen.mybatis3.model;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.Plugin;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.codegen.RootClassInfo;

/**
 * 
 * @author Jeff Butler，尹雷
 * 
 */
public class BaseRecordGenerator extends AbstractJavaGenerator {

    public BaseRecordGenerator() {
        super();
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
        progressCallback.startTask(getString(
                "Progress.8", table.toString())); //$NON-NLS-1$
        Plugin plugins = context.getPlugins();
        CommentGenerator commentGenerator = context.getCommentGenerator();
        
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(
                introspectedTable.getBaseRecordType());
        TopLevelClass topLevelClass = new TopLevelClass(type);
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addJavaFileComment(topLevelClass);

        
//        //排序语句
//        Field field = new Field();
//        field.setVisibility(JavaVisibility.PROTECTED);
//        field.setType(FullyQualifiedJavaType.getStringInstance());
//        field.setName("orderBy"); //$NON-NLS-1$
//        field.addJavaDocLine("order by 排序语句");
//        commentGenerator.addFieldComment(field, introspectedTable);
//        topLevelClass.addField(field);
//
//        Method method = new Method();
//        method.addJavaDocLine("设置 order by 排序语句");
//        method.setVisibility(JavaVisibility.PUBLIC);
//        method.setName("setOrderBy"); //$NON-NLS-1$
//        method.addParameter(new Parameter(FullyQualifiedJavaType
//                .getStringInstance(), "orderBy")); //$NON-NLS-1$
//        method.addBodyLine("this.orderBy = orderBy;"); //$NON-NLS-1$
//        commentGenerator.addGeneralMethodComment(method, introspectedTable);
//        topLevelClass.addMethod(method);
//
//        method = new Method();
//        method.addJavaDocLine("获得 order by 排序语句");
//        method.setVisibility(JavaVisibility.PUBLIC);
//        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
//        method.setName("getOrderBy"); //$NON-NLS-1$
//        method.addBodyLine("return orderBy;"); //$NON-NLS-1$
//        commentGenerator.addGeneralMethodComment(method, introspectedTable);
//        topLevelClass.addMethod(method);
//        
//        // 分页语句
//        // 分页起始记录
//        field = new Field();
//        field.setVisibility(JavaVisibility.PROTECTED);
//        field.setType(FullyQualifiedJavaType.getIntInstance());
//        field.setName("start"); //$NON-NLS-1$
//        field.addJavaDocLine("分页开始记录");
//        commentGenerator.addFieldComment(field, introspectedTable);
//        topLevelClass.addField(field);
//
//        method = new Method();
//        method.addJavaDocLine("设置 start，分页开始记录");
//        method.setVisibility(JavaVisibility.PUBLIC);
//        method.setName("setStart"); //$NON-NLS-1$
//        method.addParameter(new Parameter(FullyQualifiedJavaType
//                .getIntInstance(), "start")); //$NON-NLS-1$
//        method.addBodyLine("this.start = start;"); //$NON-NLS-1$
//        commentGenerator.addGeneralMethodComment(method, introspectedTable);
//        topLevelClass.addMethod(method);
//
//        method = new Method();
//        method.addJavaDocLine("获得 start，分页开始记录");
//        method.setVisibility(JavaVisibility.PUBLIC);
//        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
//        method.setName("getStart"); //$NON-NLS-1$
//        method.addBodyLine("return start;"); //$NON-NLS-1$
//        commentGenerator.addGeneralMethodComment(method, introspectedTable);
//        topLevelClass.addMethod(method);
//        
//        // pageSize 分页大小
//        field = new Field();
//        field.setVisibility(JavaVisibility.PROTECTED);
//        field.setType(FullyQualifiedJavaType.getIntInstance());
//        field.setName("pageSize = 10"); //$NON-NLS-1$
//        field.addJavaDocLine("分页大小");
//        commentGenerator.addFieldComment(field, introspectedTable);
//        topLevelClass.addField(field);
//
//        method = new Method();
//        method.addJavaDocLine("设置 pageSize，分页大小");
//        method.setVisibility(JavaVisibility.PUBLIC);
//        method.setName("setPageSize"); //$NON-NLS-1$
//        method.addParameter(new Parameter(FullyQualifiedJavaType
//                .getIntInstance(), "pageSize")); //$NON-NLS-1$
//        method.addBodyLine("this.pageSize = pageSize;"); //$NON-NLS-1$
//        commentGenerator.addGeneralMethodComment(method, introspectedTable);
//        topLevelClass.addMethod(method);
//
//        method = new Method();
//        method.addJavaDocLine("获得 pageSize，分页大小");
//        method.setVisibility(JavaVisibility.PUBLIC);
//        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
//        method.setName("getPageSize"); //$NON-NLS-1$
//        method.addBodyLine("return pageSize;"); //$NON-NLS-1$
//        commentGenerator.addGeneralMethodComment(method, introspectedTable);
//        topLevelClass.addMethod(method);
//        
//        // 是否去重，distinct
//        field = new Field();
//        field.setVisibility(JavaVisibility.PROTECTED);
//        field.setType(FullyQualifiedJavaType.getBooleanPrimitiveInstance());
//        field.setName("distinct"); //$NON-NLS-1$
//        field.addJavaDocLine("是否去重");
//        commentGenerator.addFieldComment(field, introspectedTable);
//        topLevelClass.addField(field);
//
//        method = new Method();
//        method.addJavaDocLine("设置 distinct，是否去重");
//        method.setVisibility(JavaVisibility.PUBLIC);
//        method.setName("setDistinct");
//        method.addParameter(new Parameter(FullyQualifiedJavaType
//                .getBooleanPrimitiveInstance(), "distinct"));
//        method.addBodyLine("this.distinct = distinct;");
//        commentGenerator.addGeneralMethodComment(method, introspectedTable);
//        topLevelClass.addMethod(method);
//
//        method = new Method();
//        method.addJavaDocLine("获得 distinct，是否去重");
//        method.setVisibility(JavaVisibility.PUBLIC);
//        method.setReturnType(FullyQualifiedJavaType.getBooleanPrimitiveInstance());
//        method.setName("isDistinct"); //$NON-NLS-1$
//        method.addBodyLine("return distinct;"); //$NON-NLS-1$
//        commentGenerator.addGeneralMethodComment(method, introspectedTable);
//        topLevelClass.addMethod(method);
//        // 分页语句结束
        
        FullyQualifiedJavaType superClass = getSuperClass();
        if (superClass != null) {
            topLevelClass.setSuperClass(superClass);
            topLevelClass.addImportedType(superClass);
        }

        List<IntrospectedColumn> introspectedColumns = getColumnsInThisClass();

        if (introspectedTable.isConstructorBased()) {
            addParameterizedConstructor(topLevelClass);
            
            if (!introspectedTable.isImmutable()) {
                addDefaultConstructor(topLevelClass);
            }
        }
        
        String rootClass = getRootClass();
        for (IntrospectedColumn introspectedColumn : introspectedColumns) {
            if (RootClassInfo.getInstance(rootClass, warnings)
                    .containsProperty(introspectedColumn)) {
                continue;
            }

            Field field2 = getJavaBeansField(introspectedColumn);
            if (plugins.modelFieldGenerated(field2, topLevelClass,
                    introspectedColumn, introspectedTable,
                    Plugin.ModelClassType.BASE_RECORD)) {
                topLevelClass.addField(field2);
                topLevelClass.addImportedType(field2.getType());
            }

            Method method2 = getJavaBeansGetter(introspectedColumn);
            if (plugins.modelGetterMethodGenerated(method2, topLevelClass,
                    introspectedColumn, introspectedTable,
                    Plugin.ModelClassType.BASE_RECORD)) {
                topLevelClass.addMethod(method2);
            }

            if (!introspectedTable.isImmutable()) {
                method2 = getJavaBeansSetter(introspectedColumn);
                if (plugins.modelSetterMethodGenerated(method2, topLevelClass,
                        introspectedColumn, introspectedTable,
                        Plugin.ModelClassType.BASE_RECORD)) {
                    topLevelClass.addMethod(method2);
                }
            }
        }

        List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
        if (context.getPlugins().modelBaseRecordClassGenerated(
                topLevelClass, introspectedTable)) {
            answer.add(topLevelClass);
        }
        return answer;
    }

    private FullyQualifiedJavaType getSuperClass() {
        FullyQualifiedJavaType superClass;
        if (introspectedTable.getRules().generatePrimaryKeyClass()) {
            superClass = new FullyQualifiedJavaType(introspectedTable
                    .getPrimaryKeyType());
        } else {
            String rootClass = getRootClass();
            if (rootClass != null) {
                superClass = new FullyQualifiedJavaType(rootClass);
            } else {
                superClass = null;
            }
        }

        return superClass;
    }

    private boolean includePrimaryKeyColumns() {
        return !introspectedTable.getRules().generatePrimaryKeyClass()
                && introspectedTable.hasPrimaryKeyColumns();
    }

    private boolean includeBLOBColumns() {
        return !introspectedTable.getRules().generateRecordWithBLOBsClass()
                && introspectedTable.hasBLOBColumns();
    }

    private void addParameterizedConstructor(TopLevelClass topLevelClass) {
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setConstructor(true);
        method.setName(topLevelClass.getType().getShortName());
        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);

        List<IntrospectedColumn> constructorColumns =
            includeBLOBColumns() ? introspectedTable.getAllColumns() :
                introspectedTable.getNonBLOBColumns();
            
        for (IntrospectedColumn introspectedColumn : constructorColumns) {
            method.addParameter(new Parameter(introspectedColumn.getFullyQualifiedJavaType(),
                    introspectedColumn.getJavaProperty()));
        }
        
        StringBuilder sb = new StringBuilder();
        if (introspectedTable.getRules().generatePrimaryKeyClass()) {
            boolean comma = false;
            sb.append("super("); //$NON-NLS-1$
            for (IntrospectedColumn introspectedColumn : introspectedTable
                    .getPrimaryKeyColumns()) {
                if (comma) {
                    sb.append(", "); //$NON-NLS-1$
                } else {
                    comma = true;
                }
                sb.append(introspectedColumn.getJavaProperty());
            }
            sb.append(");"); //$NON-NLS-1$
            method.addBodyLine(sb.toString());
        }

        List<IntrospectedColumn> introspectedColumns = getColumnsInThisClass();
        
        for (IntrospectedColumn introspectedColumn : introspectedColumns) {
            sb.setLength(0);
            sb.append("this."); //$NON-NLS-1$
            sb.append(introspectedColumn.getJavaProperty());
            sb.append(" = "); //$NON-NLS-1$
            sb.append(introspectedColumn.getJavaProperty());
            sb.append(';');
            method.addBodyLine(sb.toString());
        }

        topLevelClass.addMethod(method);
    }
    
    private List<IntrospectedColumn> getColumnsInThisClass() {
        List<IntrospectedColumn> introspectedColumns;
        if (includePrimaryKeyColumns()) {
            if (includeBLOBColumns()) {
                introspectedColumns = introspectedTable.getAllColumns();
            } else {
                introspectedColumns = introspectedTable.getNonBLOBColumns();
            }
        } else {
            if (includeBLOBColumns()) {
                introspectedColumns = introspectedTable
                        .getNonPrimaryKeyColumns();
            } else {
                introspectedColumns = introspectedTable.getBaseColumns();
            }
        }
        
        return introspectedColumns;
    }
}
