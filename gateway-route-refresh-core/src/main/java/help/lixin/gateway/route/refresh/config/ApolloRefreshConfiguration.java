package help.lixin.gateway.route.refresh.config;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.core.ConfigConsts;
import com.ctrip.framework.apollo.spring.config.PropertySourcesConstants;
import com.google.common.base.Splitter;
import help.lixin.gateway.route.refresh.apollo.ApolloRefreshConfigListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.reactive.DispatcherHandler;

import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.spring.boot.ApolloApplicationContextInitializer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

@Configuration
@ConditionalOnClass({DispatcherHandler.class, ConfigChangeListener.class})
@ConditionalOnMissingBean(ApolloApplicationContextInitializer.class)
public class ApolloRefreshConfiguration {

    private Logger logger = LoggerFactory.getLogger(ApolloRefreshConfiguration.class);

    /**
     * Apollo事件监听handler
     *
     * @return
     */
    @Bean
    public ApolloRefreshConfigListener apolloRefreshConfigListener() {
        return new ApolloRefreshConfigListener();
    }

    /**
     * 为Apollo每个命名空间配置事件.
     *
     * @return
     */
    @Configuration
    public class ApolloDataIdConfigListenerConfig {
        private final Splitter NAMESPACE_SPLITTER = Splitter.on(",").omitEmptyStrings().trimResults();

        @Autowired
        private Environment environment;

        @Autowired
        private ApolloRefreshConfigListener apolloRefreshConfigListener;

        @PostConstruct
        public void init() {
            List<String> namespaceList = namespaces();
            for (String namespace : namespaceList) { // 遍历所有的namespace,添加监听器
                Config config = ConfigService.getConfig(namespace);
                config.addChangeListener(apolloRefreshConfigListener);
            }
        }

        @PreDestroy
        public void destroy() {
            List<String> namespaceList = namespaces();
            for (String namespace : namespaceList) { // 遍历所有的namespace,移除监听器
                Config config = ConfigService.getConfig(namespace);
                config.removeChangeListener(apolloRefreshConfigListener);
            }
        }

        private List<String> namespaces() {
            String namespaces = environment.getProperty(PropertySourcesConstants.APOLLO_BOOTSTRAP_NAMESPACES,
                    ConfigConsts.NAMESPACE_APPLICATION);
            logger.debug("Apollo bootstrap namespaces: {}", namespaces);
            List<String> namespaceList = NAMESPACE_SPLITTER.splitToList(namespaces);
            return namespaceList;
        }
    }
}
