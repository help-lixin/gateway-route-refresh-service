package help.lixin.gateway.route.refresh;

import help.lixin.gateway.route.refresh.config.ApolloRefreshConfiguration;
import help.lixin.gateway.route.refresh.config.CommonRefreshConfiguration;
import help.lixin.gateway.route.refresh.config.NacosRefreshConfig;
import help.lixin.gateway.route.refresh.constants.Constants;
import help.lixin.gateway.route.refresh.properties.GatewayRouteRefreshProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ImportAutoConfiguration({CommonRefreshConfiguration.class, ApolloRefreshConfiguration.class, NacosRefreshConfig.class})
@EnableConfigurationProperties(GatewayRouteRefreshProperties.class)
@ConditionalOnProperty(prefix = Constants.GATEWAY_ROUTE_REFRESH, name = Constants.ENABLED, havingValue = "true", matchIfMissing = false)
public class GatewayAutoRefreshConfiguration {

    private Logger logger = LoggerFactory.getLogger(GatewayAutoRefreshConfiguration.class);

    private GatewayRouteRefreshProperties gatewayRouteRefreshProperties;

    public GatewayAutoRefreshConfiguration(GatewayRouteRefreshProperties gatewayRouteRefreshProperties) {
        this.gatewayRouteRefreshProperties = gatewayRouteRefreshProperties;
    }

    {
        if (logger.isDebugEnabled()) {
            logger.debug("enabled Module [{}] SUCCESS.", GatewayAutoRefreshConfiguration.class.getSimpleName());
        }
    }

}
