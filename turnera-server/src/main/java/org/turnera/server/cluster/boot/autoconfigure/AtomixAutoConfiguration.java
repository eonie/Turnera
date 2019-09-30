package org.turnera.server.cluster.boot.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import io.atomix.cluster.Node;
import io.atomix.cluster.discovery.BootstrapDiscoveryProvider;
import io.atomix.cluster.discovery.NodeDiscoveryProvider;
import io.atomix.core.Atomix;
import io.atomix.core.AtomixRegistry;
import io.atomix.core.profile.Profile;
import io.atomix.utils.NamedType;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.*;

@Configuration
@ConditionalOnProperty(prefix = "atomix", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(AtomixConfiguration.class)
public class AtomixAutoConfiguration {
    @ConditionalOnMissingBean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Bean(name = "atomix-instance", initMethod = "start", destroyMethod = "stop")
    public AtomixInstance atomix(
            AtomixConfiguration configuration,
            AtomixRegistry registry,
            Optional<List<AtomixInstance.Listener>> listeners,
            NodeDiscoveryProvider discoveryProvider) {

        final String clusterId = configuration.getClusterId();
        final String memberId = configuration.getMember().getId();

        final Atomix atomix = Atomix.builder(registry)
                .withClusterId(clusterId)
                .withMemberId(memberId)
                .withAddress(configuration.getMember().getHost(), configuration.getMember().getPort())
                .withMembershipProvider(discoveryProvider)
                .withProfiles(Profile.dataGrid())
                .build();

        return new AtomixInstance(
                atomix,
                listeners.orElseGet(Collections::emptyList)
        );
    }

    @ConditionalOnMissingBean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Bean(name = "atomix-bootstrap-discovery")
    public NodeDiscoveryProvider atomixBootstrapDiscovery(AtomixConfiguration configuration) {
        List<Node> nodes = new ArrayList<>();

        for (AtomixConfiguration.Member member: configuration.getNodes()) {
            nodes.add(
                    Node.builder()
                            .withId(member.getId())
                            .withAddress(member.getHost(), member.getPort())
                            .build()
            );
        }

        return BootstrapDiscoveryProvider.builder().withNodes(nodes).build();
    }

    @ConditionalOnMissingBean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Bean(name = "atomix-registry")
    public AtomixRegistry atomixRegistry(ApplicationContext context) {
        return new AtomixRegistry() {
            @Override
            public <T extends NamedType> Collection<T> getTypes(Class<T> type) {
                try {
                    return context.getBeansOfType(type).values();
                } catch (BeansException e) {
                    return Collections.emptyList();
                }
            }

            @Override
            public <T extends NamedType> T getType(Class<T> type, String name) {
                try {
                    return context.getBean("atomix:" + name, type);
                } catch (BeansException e) {
                    return null;
                }
            }
        };
    }
}
