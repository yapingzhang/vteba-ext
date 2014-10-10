package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

/**
 * mybatis分页查询代码生成器
 * @author yinlei
 * 2013-12-14
 */
public class ByModelUpdateBatchElementGenerator extends AbstractXmlElementGenerator {

	public ByModelUpdateBatchElementGenerator() {
		
	}

	@Override
	public void addElements(XmlElement parentElement) {
	    XmlElement answer = new XmlElement("update"); //$NON-NLS-1$
        answer.setComments("根据params所携带条件更新指定字段。");
        answer.addAttribute(new Attribute(
                        "id", introspectedTable.getUpdateBatchStatementId())); //$NON-NLS-1$

//        answer.addAttribute(new Attribute("parameterType", "map"));
        answer.addAttribute(new Attribute("parameterType", "com.vteba.tx.jdbc.params.UpdateBean"));

        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();
        sb.append("update {{"); //$NON-NLS-1$
        sb.append(introspectedTable
                .getAliasedFullyQualifiedTableNameAtRuntime());
        sb.append("}}");
        answer.addElement(new TextElement(sb.toString()));

        XmlElement dynamicElement = new XmlElement("set"); //$NON-NLS-1$
        answer.addElement(dynamicElement);

        for (IntrospectedColumn introspectedColumn : introspectedTable
                .getAllColumns()) {
            XmlElement isNotNullElement = new XmlElement("if");
            sb.setLength(0);
            sb.append(introspectedColumn.getJavaProperty("record."));
            sb.append(" != null");
            isNotNullElement.addAttribute(new Attribute("test", sb.toString()));
            dynamicElement.addElement(isNotNullElement);

            sb.setLength(0);
            sb.append(MyBatis3FormattingUtilities
                    .getAliasedEscapedColumnName(introspectedColumn));
            sb.append(" = ");
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, "record."));
            sb.append(',');

            isNotNullElement.addElement(new TextElement(sb.toString()));
        }

        
        answer.addElement(getUpdateModelWhereClauseElement());
        
        if (context.getPlugins()
                .sqlMapSelectByExampleWithoutBLOBsElementGenerated(answer,
                        introspectedTable)) {
            parentElement.addElement(answer);
        }
		
	}

}
