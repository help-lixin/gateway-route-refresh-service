package help.lixin.gateway.route.refresh.listener.impl;

import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import help.lixin.gateway.route.refresh.listener.IRefreshConfigListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

/**
 * @author lixin
 */
public abstract class AbstractRefreshConfigListener
        implements IRefreshConfigListener, ApplicationEventPublisherAware, DisposableBean {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected ApplicationEventPublisher applicationEventPublisher;

    // 30 秒
    @Value("${route.refresh.event.delay:30}")
    protected int delay = 30;

    // 定义定时任务的线程池
    protected ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1,
            new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("RefreshConfigConfigChangeListener");
                    return thread;
                }
            }, new ThreadPoolExecutor.DiscardOldestPolicy());

    @Override
    public void onEvent(Object... args) {
        // 在这里为了什么要用"定时任务"来做事情,原因有两点:
        // 1. 当有监听到事件时,有可能:Environment里还没有"最新的全量数据",就算发布了刷新事件(RefreshRoutesEvent),也会无济于事,所以,稍缓30秒之左右.
        // 2. Apollo规定是每隔5分钟去轮询一次服务器,为什么要这样做?是因为:push方式有着不稳定性,5分钟再去拉一次,保证了数据的最终一致性.
        int randomDelay = new Random(delay).nextInt(delay);
        scheduledExecutorService.schedule(() -> {
            applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
            logger.info("*************************** Revice Config Refresh Event!");
        }, randomDelay, TimeUnit.SECONDS);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void destroy() throws Exception {
        scheduledExecutorService.shutdown();
    }
}
