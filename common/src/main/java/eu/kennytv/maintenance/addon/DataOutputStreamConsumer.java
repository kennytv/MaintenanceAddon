package eu.kennytv.maintenance.addon;

import java.io.DataOutputStream;
import java.io.IOException;

@FunctionalInterface
public interface DataOutputStreamConsumer {

    void accept(DataOutputStream out) throws IOException;
}