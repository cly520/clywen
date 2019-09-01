package com.example.demo.cy.ly.cache;

import com.example.demo.cy.ly.dao.TagDao;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TagCache {
    public static List<TagDao> get(){
        List<TagDao> tagDaos = new ArrayList<>();
        TagDao program = new TagDao();
        program.setCategoryName("开发语言");
        program.setTags(Arrays.asList("javascript ","php","css","html","html5","java","node","python","c++","c","golang","objective","typescript","shell ","c#","swift","sass","bash","ruby","less","asp",".net","lua","scala","coffeescript","actionscript","rust","erlang","perl"));
        tagDaos.add(program);
        TagDao framework = new TagDao();
        framework.setCategoryName("平台框架");
        framework.setTags(Arrays.asList("laravel","Spring","Spring Boot","SpringMVC","Mybatis","express","django","flask","yii","ruby-on-rails","tornado","koa","struts"));
        tagDaos.add(framework);
        TagDao servlet = new TagDao();
        servlet.setCategoryName("服务器");
        servlet.setTags(Arrays.asList("linux","nginx","docker","apache","ubuntu","centos","缓存 ","tomcat","负载均衡","unix","hadoop","windows-server"));
        tagDaos.add(servlet);
        TagDao db = new TagDao();
        db.setCategoryName("数据库和缓存");
        db.setTags(Arrays.asList("mysql","redis","mongodb","sql","oracle","nosql ","memcached","sqlserver","postgresql","sqlite"));
        tagDaos.add(db);
        TagDao tool = new TagDao();
        tool.setCategoryName("开发工具");
        tool.setTags(Arrays.asList("git","github","visual-studio-code","vim","sublime-text","xcode","intellij-idea","eclipse","maven","ide","svn","visual-studio","atom","emacs","textmate","hg"));
        tagDaos.add(tool);
        return tagDaos;
    }

    public static String tagsEquals(String tags){
        String[] split = StringUtils.split(tags, ",");
        List<TagDao> tagDaos = get();
        List<String> tagList = tagDaos.stream().flatMap(tag -> tag.getTags().stream()).collect(Collectors.toList());
        String collect = Arrays.stream(split).filter(t -> !tagList.contains(t)).collect(Collectors.joining(","));
        return collect;
    }
}