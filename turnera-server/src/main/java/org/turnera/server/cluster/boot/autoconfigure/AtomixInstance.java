package org.turnera.server.cluster.boot.autoconfigure;

import java.util.List;

import io.atomix.core.Atomix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.Lifecycle;

public final class AtomixInstance implements Lifecycle {
    private static final Logger LOGGER = LoggerFactory.getLogger(AtomixInstance.class);

    private final Atomix atomix;
    private final List<Listener> listeners;

    public AtomixInstance(Atomix atomix, List<Listener> listeners) {
        this.atomix = atomix;
        this.listeners = listeners;
    }

    @Override
    public void start() {
        if (!atomix.isRunning()) {
            atomix.start().thenAccept(
                    a -> {
                        LOGGER.info("Node {} started", atomix.getMembershipService().getLocalMember().id());

                        for (Listener listener: listeners) {
                            listener.started(atomix);
                        }
                    }
            );
        }
    }

    @Override
    public void stop() {
        atomix.stop().thenAccept(
                a -> {
                    LOGGER.info("Node {} stopped", atomix.getMembershipService().getLocalMember().id());

                    for (Listener listener: listeners) {
                        listener.stopped(atomix);
                    }
                }
        );
    }

    @Override
    public boolean isRunning() {
        return atomix.isRunning();
    }

    public Atomix getNode() {
        return atomix;
    }

    // ****************
    //
    // ****************

    public interface Listener {
        void started(Atomix atomix);

        void stopped(Atomix atomix);
    }
}
