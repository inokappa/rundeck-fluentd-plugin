import com.dtolabs.rundeck.plugins.logging.StreamingLogWriterPlugin;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dtolabs.rundeck.core.logging.LogEvent;
import com.dtolabs.rundeck.core.logging.LogLevel;
//
import org.msgpack.MessagePack

/**
 * Opens a TCP connection, and writes event messages to the socket
 */
rundeckPlugin(StreamingLogWriterPlugin){
    configuration{
        host defaultValue:"localhost", required:true, description: "Hostname to connect to"
        port required:true, description: "Port to connect to", type: 'Integer'
        tag required:true, description: "fluentd tag ex)rundeck.log"
    }
    /**
     * open the socket, prepare some metadata
     */
    open { Map execution, Map config ->
        def socket = new Socket(config.host, config.port.toInteger());
        def socketStream = socket.getOutputStream();
        def e2 = [:]
        execution.each{ e2["execution.${it.key}"]=it.value }
        def long epoch = System.currentTimeMillis()/1000;
        //
        [socket:socket, count:0, write:{socketStream<< MessagePack.pack([config.tag + "." + execution.project, epoch, e2 + it])}]
        //
    }

    /**
     * write the log event and metadata to the socket
     */
    addEvent { Map context, LogEvent event ->
        
        context.count++
        
        def emeta=[:]
        
        event.metadata?.each{ emeta["event.${it.key}"]=it.value }

        def data= emeta + [
            line:context.count,
            datetime:event.datetime.time,
            loglevel:event.loglevel.toString(),
            message:event.message,
            eventType:event.eventType,
        ]

        context.write data
    }
    /**
     * close the socket
     */
    close { 
        context.write ending:true, totallines:context.count, message: 'Execution '+context.executionid+' finished.'
        context.socket.close();
    }
}
