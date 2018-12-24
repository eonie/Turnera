package org.turnera.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.turnera.server.netty.TurneraServer;

@Configuration
public class TurnerServerConfiguration {

    @Bean
    public TurneraServer turneraServer() throws Exception {
        TurneraServer turneraServer = new TurneraServer(20000);
        turneraServer.start();
        return turneraServer;
    }
}
