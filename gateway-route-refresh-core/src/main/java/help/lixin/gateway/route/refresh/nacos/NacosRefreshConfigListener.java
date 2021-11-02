package help.lixin.gateway.route.refresh.nacos;

import com.alibaba.nacos.api.config.listener.Listener;
import help.lixin.gateway.route.refresh.listener.impl.AbstractRefreshConfigListener;

import java.util.concurrent.Executor;

public class NacosRefreshConfigListener extends AbstractRefreshConfigListener implements Listener {

    @Override
    public Executor getExecutor() {
        return null;
    }

    @Override
    public void receiveConfigInfo(String s) {
        super.onEvent(s);
    }
}
