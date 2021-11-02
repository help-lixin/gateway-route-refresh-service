package help.lixin.gateway.route.refresh.config;

import help.lixin.gateway.route.refresh.locator.PropertiesRouteDefinitionLocatorExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.config.PropertiesRouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.DispatcherHandler;

@Configuration
@ConditionalOnClass(DispatcherHandler.class)
public class CommonRefreshConfiguration {
    private Logger logger = LoggerFactory.getLogger(CommonRefreshConfiguration.class);

    @Bean
    public PropertiesRouteDefinitionLocator propertiesRouteDefinitionLocator(GatewayProperties gatewayProperties) {
        return new PropertiesRouteDefinitionLocatorExt(gatewayProperties);
    }
}
