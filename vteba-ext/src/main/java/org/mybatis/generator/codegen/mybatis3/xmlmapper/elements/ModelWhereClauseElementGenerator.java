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
package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

/**
 * 根据model更新或者查询的where语句
 * @author yinlei
 * @since 2013-12-15 11:07
 */
public class ModelWhereClauseElementGenerator extends
        AbstractXmlElementGenerator {

    public ModelWhereClauseElementGenerator() {
        super();
    }

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("sql"); //$NON-NLS-1$

        answer.addAttribute(new Attribute("id", introspectedTable.getModelWhereClauseId()));
        
//        String parameterType;
//        if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
//            parameterType = introspectedTable.getRecordWithBLOBsType();
//        } else {
//            parameterType = introspectedTable.getBaseRecordType();
//        }
//        answer.addAttribute(new Attribute("parameterType", parameterType));
        
        answer.setComments("根据model条件，查询或更新的where语句。");
        context.getCommentGenerator().addComment(answer);

        XmlElement whereElement = new XmlElement("where");
        answer.addElement(whereElement);

        XmlElement trimElement = new XmlElement("trim");
//        trimElement.addAttribute(new Attribute("prefix", "("));
//        trimElement.addAttribute(new Attribute("suffix", ")"));
        trimElement.addAttribute(new Attribute("prefixOverrides", "and"));
        whereElement.addElement(trimElement);
        
        StringBuilder sb = new StringBuilder();
        for (IntrospectedColumn introspectedColumn : introspectedTable
                .getNonPrimaryKeyColumns()) {
            XmlElement isNotNullElement = new XmlElement("if");
            sb.setLength(0);
            sb.append(introspectedColumn.getJavaProperty("params."));
            sb.append(" != null"); //$NON-NLS-1$
            isNotNullElement.addAttribute(new Attribute("test", sb.toString())); //$NON-NLS-1$
            trimElement.addElement(isNotNullElement);

            sb.setLength(0);
            sb.append("and ");
            sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            sb.append(" = "); //$NON-NLS-1$
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, "params."));

            isNotNullElement.addElement(new TextElement(sb.toString()));
        }
        
        // 排序语句
        XmlElement orderByElement = new XmlElement("if");
        orderByElement.addAttribute(new Attribute("test", "orderBy != null"));
        orderByElement.addElement(new TextElement("order by ${orderBy}"));
        answer.addElement(orderByElement);
        
        // 分页数据
        XmlElement ifElement = new XmlElement("if");
        ifElement.addAttribute(new Attribute("test", "start != null"));
        ifElement.addElement(new TextElement("limit ${start}, ${pageSize}"));
        answer.addElement(ifElement);
        
        if (context.getPlugins()
                .sqlMapExampleWhereClauseElementGenerated(answer, introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
    
}
