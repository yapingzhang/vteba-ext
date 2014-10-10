package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 * mybatis分页查询代码生成器
 * @author yinlei
 * 2013-12-14
 */
public class ByModelQueryListElementGenerator extends AbstractXmlElementGenerator {

	public ByModelQueryListElementGenerator() {
		
	}

	@Override
	public void addElements(XmlElement parentElement) {
//	    FullyQualifiedJavaType parameterType = new FullyQualifiedJavaType(
//                    introspectedTable.getBaseRecordType());
		String fqjt = "com.vteba.tx.jdbc.params.QueryBean";

        XmlElement answer = new XmlElement("select"); //$NON-NLS-1$
        answer.setComments("根据params所携带条件查询数据。");
        answer.addAttribute(new Attribute("id", //$NON-NLS-1$
                introspectedTable.getQueryListStatementId()));
        answer.addAttribute(new Attribute(
                "resultMap", introspectedTable.getBaseResultMapId())); //$NON-NLS-1$
//        answer.addAttribute(new Attribute("parameterType", parameterType.getFullyQualifiedName()));
        answer.addAttribute(new Attribute("parameterType", fqjt));

        context.getCommentGenerator().addComment(answer);

        answer.addElement(new TextElement("select")); //$NON-NLS-1$
        
        // 去掉distinct
        XmlElement ifElement = new XmlElement("if"); //$NON-NLS-1$
        ifElement.addAttribute(new Attribute("test", "distinct")); //$NON-NLS-1$ //$NON-NLS-2$
        ifElement.addElement(new TextElement("distinct")); //$NON-NLS-1$
        answer.addElement(ifElement);

        StringBuilder sb = new StringBuilder();
        answer.addElement(getBaseColumnListElement());

        sb.setLength(0);
        sb.append("from {{");
        sb.append(introspectedTable
                .getAliasedFullyQualifiedTableNameAtRuntime());
        sb.append("}}");
        answer.addElement((new TextElement(sb.toString())));
        answer.addElement(getModelWhereClauseElement());

//        // 排序语句
//        ifElement = new XmlElement("if"); //$NON-NLS-1$
//        ifElement.addAttribute(new Attribute("test", "params.orderBy != null")); //$NON-NLS-1$ //$NON-NLS-2$
//        ifElement.addElement(new TextElement("order by ${params.orderBy}")); //$NON-NLS-1$
//        answer.addElement(ifElement);

        if (context.getPlugins()
                .sqlMapSelectByExampleWithoutBLOBsElementGenerated(answer,
                        introspectedTable)) {
            parentElement.addElement(answer);
        }
		
	}

}
