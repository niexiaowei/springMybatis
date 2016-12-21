package cn.com.fero.test.configuration;

import cn.com.fero.framework.page.pagehelper.PageHelper;
import cn.com.fero.framework.utils.ArrayUtils;
import cn.com.fero.framework.utils.ExceptionUtils;
import cn.com.fero.framework.web.converter.CustomerJsonMapperConverter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.IOException;


/**
 * Created by niexiaowei on 2015/12/11
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"cn.com.fero.test"})
@MapperScan(sqlSessionFactoryRef = "sessionFactory", basePackages = {"cn.com.fero.test.dao"})
public class ApplicationConfiguration extends WebMvcConfigurerAdapter {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Bean(initMethod = "init", destroyMethod = "close")
    public DataSource datasource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/niexiaowei?useUnicode=true&characterEncoding=utf-8");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        return dataSource;
    }


    @Bean
    public SqlSessionFactory sessionFactory() throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(datasource());
        sessionFactory.setTypeAliasesPackage("cn.com.fero.test.vo");

        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath*:cn/com/fero/test/mapper/*.xml");
            sessionFactory.setMapperLocations(resources);

            PageHelper pageHelper = new PageHelper();
            sessionFactory.setPlugins(ArrayUtils.toArray(pageHelper));

        } catch (IOException e) {
            log.error("初始化sessionFactory异常:{}", ExceptionUtils.getStackTrace(e));
        }

        return sessionFactory.getObject();
    }

    @Bean
    public DataSourceTransactionManager transactionManager() throws PropertyVetoException {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(datasource());
        dataSourceTransactionManager.setDefaultTimeout(10);
        return dataSourceTransactionManager;
    }

    @Bean
    public MappingJackson2HttpMessageConverter jsonMapperConverter() {
        return new CustomerJsonMapperConverter();
    }


    @Bean
    public FilterRegistrationBean webStatFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.ico,/druid/*");
        return filterRegistrationBean;
    }

    @Bean
    public ServletRegistrationBean statViewServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        servletRegistrationBean.addInitParameter("resetEnable", "false");
        return servletRegistrationBean;
    }


}
