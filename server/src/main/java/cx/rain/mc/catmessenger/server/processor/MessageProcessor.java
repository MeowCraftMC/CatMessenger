package cx.rain.mc.catmessenger.server.processor;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.protocol.AsyncUserProcessor;
import cx.rain.mc.catmessenger.api.message.Message;
import net.afyer.afybroker.server.BrokerServer;
import net.afyer.afybroker.server.aware.BrokerServerAware;

public class MessageProcessor extends AsyncUserProcessor<Message> implements BrokerServerAware {
    @Override
    public void handleRequest(BizContext bizContext, AsyncContext asyncContext, Message message) throws Exception {

    }

    @Override
    public String interest() {
        return "";
    }

    @Override
    public void setBrokerServer(BrokerServer brokerServer) {

    }
}
