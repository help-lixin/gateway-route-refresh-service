package help.lixin.gateway.route.refresh.apollo;

import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import help.lixin.gateway.route.refresh.listener.impl.AbstractRefreshConfigListener;

public class ApolloRefreshConfigListener extends AbstractRefreshConfigListener implements ConfigChangeListener {
	@Override
	public void onChange(ConfigChangeEvent change) {
		onEvent(change);
	}
}
