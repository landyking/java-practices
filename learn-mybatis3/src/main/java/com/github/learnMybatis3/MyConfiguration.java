package com.github.learnMybatis3;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author: landy
 * @date: 2018-01-20 16:19
 */
public class MyConfiguration extends Configuration {

    public static final String RESOURCE_ADD = "__add__";

    @Override
    public MappedStatement getMappedStatement(String id, boolean validateIncompleteStatements) {
        if (!mappedStatements.containsKey(id)) {
            System.out.println("can't found mapped statement: " + id);
            throw new IllegalArgumentException("Container does not contain value for " + id);
        } else {
            return super.getMappedStatement(id, validateIncompleteStatements);
        }
    }

    public boolean isResourceLoaded(String resource) {
        if (resource.equals(RESOURCE_ADD)) {
            return false;
        } else {
            return super.isResourceLoaded(resource);
        }
    }

    private Collection<String> validXml(String xml) {

        ByteArrayInputStream inputStream = new ByteArrayInputStream(xml.getBytes());
        Configuration cfg = new Configuration();
        XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, cfg, "__add__", cfg.getSqlFragments());
        mapperParser.parse();
        return cfg.getMappedStatementNames();
    }

    public void addSelectXml(String xml) {
        Collection<String> ids = validXml(xml);
        if (!isOnlyOne(ids)) {
            throw new IllegalStateException("包含多于一个sql语句: " + ids);
        }
        String id = ids.iterator().next();
        if (mappedStatements.containsKey(id)) {
            throw new IllegalStateException("语句已存在: " + id);
        } else {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(xml.getBytes());
            XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, this, "__add__", this.getSqlFragments());
            mapperParser.parse();
        }
    }

    private boolean isOnlyOne(Collection<String> ids) {
        if (ids.size() == 1) {
            return true;
        } else if (ids.size() == 2) {
            Iterator<String> iterator = ids.iterator();
            String first = iterator.next();
            String second = iterator.next();
            if (first.length() < second.length()) {
                return second.endsWith(first);
            } else {
                return first.endsWith(second);
            }
        }
        return false;
    }

    public void replaceSelectXml(String xml) {
        Collection<String> ids = validXml(xml);
        if (ids.size() != 1) {
            throw new IllegalStateException("包含多于一个sql语句");
        }
        String id = ids.iterator().next();
        if (!mappedStatements.containsKey(id)) {
            throw new IllegalStateException("语句不存在：" + id);
        } else {
            mappedStatements.remove(id);//todo 同时清除resultMap
            ByteArrayInputStream inputStream = new ByteArrayInputStream(xml.getBytes());
            XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, this, "__add__", this.getSqlFragments());
            mapperParser.parse();
        }

    }

    public void removeXml(String id) {
        mappedStatements.remove(id);
    }
}
