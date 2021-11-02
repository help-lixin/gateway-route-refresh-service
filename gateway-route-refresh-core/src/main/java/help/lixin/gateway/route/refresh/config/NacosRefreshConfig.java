package help.lixin.gateway.route.refresh.config;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import help.lixin.gateway.route.refresh.nacos.NacosRefreshConfigListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.reactive.DispatcherHandler;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

@Configuration
@ConditionalOnClass({DispatcherHandler.class, Listener.class})
//@ConditionalOnMissingBean(NacosConfigManager.class)
public class NacosRefreshConfig {

    private Logger logger = LoggerFactory.getLogger(NacosRefreshConfig.class);

    @Bean
    public Listener nacosRefreshConfigListener() {
        return new NacosRefreshConfigListener();
    }


    @Configuration
    public class NacosNamespaceManagerListenerConfig {

        @Autowired
        private Listener nacosRefreshConfigListener;

        @Autowired
        private Environment environment;

        @Autowired
        private NacosConfigProperties nacosConfigProperties;

        @Autowired
        private NacosConfigManager nacosConfigManager;

        /**
         * 给所有的dataid/group添加监听
         */
        @PostConstruct
        public void init() {
            // 为默认的dataid设置监听,默认的dataid=应用程序名称.扩展名称
            String rootGroup = nacosConfigProperties.getGroup();
            String appName = environment.resolvePlaceholders("${spring.application.name}");
            String fileExtension = nacosConfigProperties.getFileExtension();
            addListener(appName + "." + fileExtension, rootGroup);

            List<NacosConfigProperties.Config> extConfig = nacosConfigProperties.getExtensionConfigs();
            List<NacosConfigProperties.Config> sharedConfigs = nacosConfigProperties.getSharedConfigs();
            if (null != extConfig) {
                extConfig.stream().forEach(item -> {
                    String dataId = item.getDataId();
                    String group = item.getGroup();
                    addListener(dataId, group);
                });
            }

            if (null != sharedConfigs) {
                sharedConfigs.stream().forEach(item -> {
                    String dataId = item.getDataId();
                    String group = item.getGroup();
                    addListener(dataId, group);
                });
            }
        }

        @PreDestroy
        public void destroy() {
            List<NacosConfigProperties.Config> extConfig = nacosConfigProperties.getExtensionConfigs();
            List<NacosConfigProperties.Config> sharedConfigs = nacosConfigProperties.getSharedConfigs();

            extConfig.stream().forEach(item -> {
                String dataId = item.getDataId();
                String group = item.getGroup();
                removeListener(dataId, group);
            });

            sharedConfigs.stream().forEach(item -> {
                String dataId = item.getDataId();
                String group = item.getGroup();
                removeListener(dataId, group);
            });
        }

        protected void removeListener(String dataId, String group) {
            nacosConfigManager.getConfigService().removeListener(dataId, group, nacosRefreshConfigListener);
        }

        protected void addListener(String dataId, String group) {
            try {
                nacosConfigManager.getConfigService().addListener(dataId, group, nacosRefreshConfigListener);
            } catch (NacosException e) {
                logger.warn("add event fail:[{}]", e);
            }
        }
    }
}
