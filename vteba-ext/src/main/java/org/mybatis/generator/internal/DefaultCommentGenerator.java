/*
 *  Copyright 2008 The Apache Software Foundation
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

package org.mybatis.generator.internal;

import static org.mybatis.generator.internal.util.StringUtility.isTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.InnerEnum;
import org.mybatis.generator.api.dom.java.JavaElement;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.PropertyRegistry;

/**
 * 代码工具的注释生成
 * @author Jeff Butler
 * @author 尹雷
 */
public class DefaultCommentGenerator implements CommentGenerator {

    private Properties properties;
    private boolean suppressDate;
    private boolean suppressAllComments;

    public DefaultCommentGenerator() {
        super();
        properties = new Properties();
        suppressDate = true;//yinlei
        suppressAllComments = false;
    }

    public void addJavaFileComment(CompilationUnit compilationUnit) {
        // add no file level comments by default
        return;
    }

    /**
     * Adds a suitable comment to warn users that the element was generated, and
     * when it was generated.
     */
    public void addComment(XmlElement xmlElement) {
        if (suppressAllComments) {
            return;
        }

        xmlElement.addElement(new TextElement("<!--")); //$NON-NLS-1$

        StringBuilder sb = new StringBuilder();
        sb.append("  WARNING - "); //$NON-NLS-1$
        sb.append(xmlElement.getComments());
        xmlElement.addElement(new TextElement(sb.toString()));
//        xmlElement.addElement(new TextElement(
//                        "  MyBatis automatically generated, do not modify.")); //$NON-NLS-1$

        String s = getDateString();
        if (s != null) {
//            sb.setLength(0);
//            sb.append("  This element was generated on "); //$NON-NLS-1$
//            sb.append(s);
//            sb.append('.');
//            xmlElement.addElement(new TextElement(sb.toString()));
        	String comment = "  MyBatis代码工具自动生成，不要修改！ " + s;
        	xmlElement.addElement(new TextElement(comment));
        } else {
        	xmlElement.addElement(new TextElement("  MyBatis代码工具自动生成，不要修改！"));
        }
        //xmlElement.addElement(new TextElement("  " + xmlElement.getComments()));
        xmlElement.addElement(new TextElement("-->")); //$NON-NLS-1$
    }

    public void addRootComment(XmlElement rootElement) {
        // add no document level comments by default
        return;
    }

    public void addConfigurationProperties(Properties properties) {
        this.properties.putAll(properties);

        suppressDate = isTrue(properties
                .getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_DATE));
        
        suppressAllComments = isTrue(properties
                .getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_ALL_COMMENTS));
    }

    /**
     * This method adds the custom javadoc tag for. You may do nothing if you do
     * not wish to include the Javadoc tag - however, if you do not include the
     * Javadoc tag then the Java merge capability of the eclipse plugin will
     * break.
     * 
     * @param javaElement
     *            the java element
     */
    protected void addJavadocTag(JavaElement javaElement,
            boolean markAsDoNotDelete) {
//        javaElement.addJavaDocLine(" *"); //$NON-NLS-1$
        StringBuilder sb = new StringBuilder();
//        sb.append(" * "); //$NON-NLS-1$
        //sb.append(MergeConstants.NEW_ELEMENT_TAG);
        //sb.append("@date");
        if (markAsDoNotDelete) {
            sb.append(" do_not_delete_during_merge"); //$NON-NLS-1$
        }
        String s = getDateString();
        if (s != null) {
            sb.append(' ');
            sb.append(s);
        }
//        javaElement.addJavaDocLine(sb.toString());
    }

    /**
     * This method returns a formated date string to include in the Javadoc tag
     * and XML comments. You may return null if you do not want the date in
     * these documentation elements.
     * 
     * @return a string representing the current timestamp, or null
     */
    protected String getDateString() {
        if (suppressDate) {
            return null;
        } else {
        	DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        	return format.format(new Date()).toString();
        }
    }

    public void addClassComment(InnerClass innerClass,
            IntrospectedTable introspectedTable) {
        if (suppressAllComments) {
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        addComment(innerClass);
//        innerClass
//                .addJavaDocLine(" * This class was generated by MyBatis Generator."); //$NON-NLS-1$
        sb.append(" * 该类对应数据库表 "); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTable());
        innerClass.addJavaDocLine(sb.toString());

        addJavadocTag(innerClass, false);

        innerClass.addJavaDocLine(" */"); //$NON-NLS-1$
    }

    public void addEnumComment(InnerEnum innerEnum,
            IntrospectedTable introspectedTable) {
        if (suppressAllComments) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        addComment(innerEnum);
//        innerEnum
//                .addJavaDocLine(" * This enum was generated by MyBatis Generator."); //$NON-NLS-1$

        sb.append(" * 该枚举对应数据库表 "); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTable());
        innerEnum.addJavaDocLine(sb.toString());

        addJavadocTag(innerEnum, false);

        innerEnum.addJavaDocLine(" */"); //$NON-NLS-1$
    }

    public void addFieldComment(Field field,
            IntrospectedTable introspectedTable,
            IntrospectedColumn introspectedColumn) {
        if (suppressAllComments) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        addComment(field);
        //field.addJavaDocLine(" * This field was generated by MyBatis Generator."); //$NON-NLS-1$

//        sb.append(" * 对应数据库表属性 "); //$NON-NLS-1$
        sb.append(" * ");
        sb.append(introspectedColumn.getRemarks());
        sb.append("。");
        sb.append(introspectedTable.getFullyQualifiedTable());
        sb.append('.');
        sb.append(introspectedColumn.getActualColumnName());
        field.addJavaDocLine(sb.toString());

        addJavadocTag(field, false);

        field.addJavaDocLine(" */"); //$NON-NLS-1$
    }

    public void addFieldComment(Field field, IntrospectedTable introspectedTable) {
        if (suppressAllComments) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        addComment(field);
//        field
//                .addJavaDocLine(" * This field was generated by MyBatis Generator."); //$NON-NLS-1$

        sb.append(" * 对应数据库表属性 "); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTable());
        field.addJavaDocLine(sb.toString());

        addJavadocTag(field, false);

        field.addJavaDocLine(" */"); //$NON-NLS-1$
    }

    public void addGeneralMethodComment(Method method,
            IntrospectedTable introspectedTable) {
        if (suppressAllComments) {
            return;
        }

        addComment(method);
//        method
//                .addJavaDocLine(" * This method was generated by MyBatis Generator."); //$NON-NLS-1$

//        StringBuilder sb = new StringBuilder();
//        sb.append(" * This method corresponds to the database table "); //$NON-NLS-1$
//        sb.append(introspectedTable.getFullyQualifiedTable());
//        method.addJavaDocLine(sb.toString());

        addJavadocTag(method, false);

        method.addJavaDocLine(" */"); //$NON-NLS-1$
    }

    public void addGetterComment(Method method,
            IntrospectedTable introspectedTable,
            IntrospectedColumn introspectedColumn) {
        if (suppressAllComments) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        addComment(method);
//        method
//                .addJavaDocLine(" * This method was generated by MyBatis Generator."); //$NON-NLS-1$

        sb.append(" * 获得属性，"); //$NON-NLS-1$
        sb.append(introspectedColumn.getRemarks());
        sb.append("(");
        sb.append(introspectedTable.getFullyQualifiedTable());
        sb.append('.');
        sb.append(introspectedColumn.getActualColumnName());
        sb.append(")的值");
        method.addJavaDocLine(sb.toString());

        method.addJavaDocLine(" *"); //$NON-NLS-1$

        sb.setLength(0);
        sb.append(" * @return 属性"); //$NON-NLS-1$
//        sb.append(introspectedTable.getFullyQualifiedTable());
//        sb.append('.');
//        sb.append(introspectedColumn.getActualColumnName());
        sb.append(introspectedColumn.getRemarks());
        sb.append("的值");
        method.addJavaDocLine(sb.toString());

        addJavadocTag(method, false);

        method.addJavaDocLine(" */"); //$NON-NLS-1$
    }

    public void addSetterComment(Method method,
            IntrospectedTable introspectedTable,
            IntrospectedColumn introspectedColumn) {
        if (suppressAllComments) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        addComment(method);
//        method
//                .addJavaDocLine(" * This method was generated by MyBatis Generator."); //$NON-NLS-1$

        sb.append(" * 设置属性，"); //$NON-NLS-1$
        sb.append(introspectedColumn.getRemarks());
        sb.append("(");
        sb.append(introspectedTable.getFullyQualifiedTable());
        sb.append('.');
        sb.append(introspectedColumn.getActualColumnName());
        sb.append(")的值");
        method.addJavaDocLine(sb.toString());

        method.addJavaDocLine(" *"); //$NON-NLS-1$

        Parameter parm = method.getParameters().get(0);
        sb.setLength(0);
        sb.append(" * @param "); //$NON-NLS-1$
        sb.append(parm.getName());
//        sb.append(" the value for "); //$NON-NLS-1$
//        sb.append(introspectedTable.getFullyQualifiedTable());
//        sb.append('.');
//        sb.append(introspectedColumn.getActualColumnName());
        sb.append(" 属性");
        sb.append(introspectedColumn.getRemarks());
        sb.append("的值");
        method.addJavaDocLine(sb.toString());

        addJavadocTag(method, false);

        method.addJavaDocLine(" */"); //$NON-NLS-1$
    }

    public void addClassComment(InnerClass innerClass,
            IntrospectedTable introspectedTable, boolean markAsDoNotDelete) {
        if (suppressAllComments) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        addComment(innerClass);
//        innerClass
//                .addJavaDocLine(" * This class was generated by MyBatis Generator."); //$NON-NLS-1$

        sb.append(" * 该类对应数据库表 "); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTable());
        innerClass.addJavaDocLine(sb.toString());

        addJavadocTag(innerClass, markAsDoNotDelete);

        innerClass.addJavaDocLine(" */"); //$NON-NLS-1$
    }
    
    public void addComment(JavaElement javaElement) {
    	List<String> comments = javaElement.getJavaDocLines();
    	
    	if (comments != null && comments.size() >= 1) {
    		List<String> javaDocs = new ArrayList<String>();
    		for (String docs : comments) {
    			javaDocs.add(docs);
    		}
    		//Collections.copy(javaDocs, comments);
    		comments.clear();
    		javaElement.addJavaDocLine("/**");
    		for (String comment : javaDocs) {
    			javaElement.addJavaDocLine(" * " + comment);
        	}
    	} else {
    		javaElement.addJavaDocLine("/**");
    	}
    	
    	
//    	if (comments != null && comments.size() >= 1) {
//    		String comment = javaElement.getJavaDocLines().get(0);
//    		javaElement.getJavaDocLines().clear();
//    		javaElement.addJavaDocLine("/**");
//    		javaElement.addJavaDocLine(" * " + comment);
//    	} else {
//    		javaElement.addJavaDocLine("/**");
//    	}
    }
}
