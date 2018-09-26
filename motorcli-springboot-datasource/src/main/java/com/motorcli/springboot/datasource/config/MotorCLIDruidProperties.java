package com.motorcli.springboot.datasource.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(
    prefix = "spring.datasource.druid"
)
public class MotorCLIDruidProperties {

    //初始化池大小
    private Integer initialSize;

    //最大活动连接数
    private Integer maxActive;

    //最小连接数
    private Integer minIdle;

    //配置获取连接等待超时的时间, 单位是毫秒
    private Integer maxWait;

    //配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
    private Integer timeBetweenEvictionRunsMillis;

    //配置一个连接在池中最小生存的时间，单位是毫秒
    private Integer minEvictableIdleTimeMillis;

    //验证 SQL
    private String validationQuery = "SELECT 1 FROM DUAL";

    private Boolean testOnBorrow;

    private Boolean testWhileIdle;

    private Boolean testOnReturn;

    private Boolean poolPreparedStatements;

    //打开PSCache，并且指定每个连接上PSCache的大小
    private Integer maxPoolPreparedStatementPerConnectionSize;

    //配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙
    private String filters = "stat,wall,log4j";

    //通过connectProperties属性来打开mergeSql功能；慢SQL记录
    private String connectionProperties = "druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000";

    private String druidStatViewAllow = "127.0.0.1";

    private String druidStatViewDeny = "";

    private String druidStatViewLoginUsername = "admin";

    private String druidStatViewLoginPassword = "admin";

    private String druidStatViewResetEnable = "false";
}
