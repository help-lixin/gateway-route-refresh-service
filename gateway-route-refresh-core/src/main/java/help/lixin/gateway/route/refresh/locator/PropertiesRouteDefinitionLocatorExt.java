package help.lixin.gateway.route.refresh.locator;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.config.PropertiesRouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import reactor.core.publisher.Flux;

/**
 * 触发Environment的配置到业务模型(GatewayProperties)的转换.<br/>
 * RouteDefinitionLocator的实现有:<br/>
 * 1. PropertiesRouteDefinitionLocator : 解析Properties,并转换成业务模型.<br/>
 * 2. InMemoryRouteDefinitionRepository : 支持对业务模型的CURD.<br/>
 * 3. CompositeRouteDefinitionLocator : 组合模式.<br/>
 * 按理来说,应该是对:InMemoryRouteDefinitionRepository进行扩展即可,但是,没有这样做的原因是:<br/>
 * 经PropertiesRouteDefinitionLocator处理的模型,是不支持热更新的.<br/>
 * 思来想去,感觉Spring这样设计的想法是:Properties里的配置,不需要热更新,而通过:InMemoryRouteDefinitionRepository去支持那些需要热更新的配置.
 * 明显:他的思想不符合我的业务场景.比如:发版时,运维人员配置错误,要进行更改咋办,所以,比较干脆的就是自己重写:PropertiesRouteDefinitionLocator,并替换掉,庆幸的是:Spring允许我换掉实现.
 *
 * @author lixin
 */
public class PropertiesRouteDefinitionLocatorExt extends PropertiesRouteDefinitionLocator implements EnvironmentAware {

    private Logger logger = LoggerFactory.getLogger(PropertiesRouteDefinitionLocatorExt.class);

    private Environment environment;

    public PropertiesRouteDefinitionLocatorExt(GatewayProperties properties) {
        super(properties);
    }

    /**
     * 把Environment中的配置转换成业务模型:GatewayProperties,并返回最新的:List<RouteDefinition>.
     */
    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        List<RouteDefinition> routeDefinitions = envToRouteDefinitions();
        if (logger.isDebugEnabled()) {
            logger.debug("*************************** GATEWAY ROUTE LIST={}", routeDefinitions);
        }
        return Flux.fromIterable(routeDefinitions);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    /**
     * 重新解析RouteDefinition列表.
     */
    public List<RouteDefinition> envToRouteDefinitions() {
        // 读取:Environment信息,转换成业务模型:GatewayProperties
        GatewayProperties properties = Binder.get(environment) //
                .bind("spring.cloud.gateway", GatewayProperties.class) //
                .get();//
        List<RouteDefinition> routeDefinitionList = properties.getRoutes();
        return routeDefinitionList;
    }
}
