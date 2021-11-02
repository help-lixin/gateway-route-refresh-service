package help.lixin.gateway.route.refresh.properties;

import help.lixin.gateway.route.refresh.constants.Constants;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = Constants.GATEWAY_ROUTE_REFRESH)
public class GatewayRouteRefreshProperties {
    private boolean enabled = false;

    public boolean isEnabled() {
        return enabled;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
