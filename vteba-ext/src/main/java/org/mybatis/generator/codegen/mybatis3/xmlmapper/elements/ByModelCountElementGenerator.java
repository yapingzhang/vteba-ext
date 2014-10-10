package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 * mybatis分页查询代码生成器
 * @author yinlei
 * 2013-12-14
 */
public class ByModelCountElementGenerator extends AbstractXmlElementGenerator {

	public ByModelCountElementGenerator() {
		
	}

	@Override
	public void addElements(XmlElement parentElement) {
//	    FullyQualifiedJavaType parameterType = new FullyQualifiedJavaType(
//                    introspectedTable.getBaseRecordType());

        XmlElement answer = new XmlElement("select");
        answer.setComments("根据params所携带条件计数。");
        answer.addAttribute(new Attribute("id", introspectedTable.getCountByStatementId()));
//        answer.addAttribute(new Attribute("parameterType", parameterType.getFullyQualifiedName()));
        answer.addAttribute(new Attribute("parameterType", "com.vteba.tx.jdbc.params.UpdateBean"));
        answer.addAttribute(new Attribute("resultType", "java.lang.Integer"));
        
        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();
        sb.append("select count(*) from {{");
        sb.append(introspectedTable
                .getAliasedFullyQualifiedTableNameAtRuntime());
        sb.append("}}");
        answer.addElement((new TextElement(sb.toString())));
        answer.addElement(getModelWhereClauseElement());
        
        if (context.getPlugins()
                .sqlMapSelectByExampleWithoutBLOBsElementGenerated(answer, introspectedTable)) {
            parentElement.addElement(answer);
        }
		
	}

}
