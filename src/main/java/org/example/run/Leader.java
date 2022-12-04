package org.example.run;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class Leader implements ApplicationRunner {

    public volatile static boolean isLeader;

    private CuratorFramework zkClient;

    @Value("${zk.path}")
    private String path;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        zkClient.start();
        LeaderLatch latch = new LeaderLatch(zkClient, path);
//        Util.initAll();
        // 添加选举监听
        latch.addListener(new LeaderLatchListener() {
            @Override
            public void isLeader() {
                isLeader = true;
            }

            @Override
            public void notLeader() {
                isLeader = false;
            }
        });
        // 加入选举
        latch.start();
    }

    @Autowired
    public void setZkClient(CuratorFramework zkClient) {
        this.zkClient = zkClient;
    }
}
