# Rundeck Fluentd Plugin

This is a simple Rundeck streaming Log Writer plugin that will pipe all log output to a Fluentd. *For Rundeck version 1.6.0 or later.*

## Special Thanks

Referred to [rundeck-logstatsh-plugin](https://github.com/rundeck-plugins/rundeck-logstash-plugin).
Very helpful for me.

Thank you very much.

***

## Installation

Copy the `FluentdPlugin.groovy` to your `$RDECK_BASE/libext/` directory for Rundeck.

Enable the plugin in your `rundeck-config.properties` file:

```
rundeck.execution.logs.streamingWriterPlugins=FluentdPlugin
```

***

## Dependency library

- msgpack-0.6.12.jar
- javassist-3.17.1-GA.jar

***

## Configure Rundeck

The plugin supports these configuration properties:

- `host` - hostname of the Fluentd.(ex: 127.0.0.1)
- `port` - TCP port of the Fluentd.(ex: 24224)
- `tag_prefix` - Tag Prefix of the Fluentd.(ex: rundeck.log)

You can update the your framework/project.properties file to set these configuration values:

in `framework.properties`:

```
framework.plugin.StreamingLogWriter.FluentdPlugin.port=24224
framework.plugin.StreamingLogWriter.FluentdPlugin.host=localhost
```

or in `project.properties`:

```
project.plugin.StreamingLogWriter.FluentdPlugin.port=24224
project.plugin.StreamingLogWriter.FluentdPlugin.host=localhost
```

***

## Configure Fluentd

Refer to following `rundeck-fluentd.conf`:

```
<source>
  type forward
  port 24224
  bind 0.0.0.0
</source>

<match rundeck.**>
  type stdout
</match>
```

***

## Start Fluentd

Use the config file when starting fluentd.

```
$ fluentd -c rundeck-fluentd.conf -l debug.log
```

in debug.log.

```
2015-10-22 17:41:00 +0900 rundeck.log.hello-world: {"execution.loglevel":"INFO","execution.wasRetry":"false","execution.url":"http://xxx.xxx.xxx.xxx:4440/project/hello-world/execution/follow/1213","execution.id":"fdf1cca5-e729-491b-a1fa-3d8fef373d46","execution.project":"hello-world","execution.username":"admin","execution.retryAttempt":"0","execution.user.name":"admin","execution.name":"hello-world","execution.serverUUID":null,"execution.group":null,"execution.execid":"1213","execution.serverUrl":"http://xxx.xxx.xxx.xxx:4440/","event.stepctx":"1","event.step":"1","line":1,"datetime":1445503260180,"loglevel":"NORMAL","message":null,"eventType":"stepbegin"}
2015-10-22 17:41:00 +0900 rundeck.log.hello-world: {"execution.loglevel":"INFO","execution.wasRetry":"false","execution.url":"http://xxx.xxx.xxx.xxx:4440/project/hello-world/execution/follow/1213","execution.id":"fdf1cca5-e729-491b-a1fa-3d8fef373d46","execution.project":"hello-world","execution.username":"admin","execution.retryAttempt":"0","execution.user.name":"admin","execution.name":"hello-world","execution.serverUUID":null,"execution.group":null,"execution.execid":"1213","execution.serverUrl":"http://xxx.xxx.xxx.xxx:4440/","event.node":"localhost","event.stepctx":"1","event.user":"rundeck","event.step":"1","line":2,"datetime":1445503260181,"loglevel":"NORMAL","message":null,"eventType":"nodebegin"}
2015-10-22 17:41:00 +0900 rundeck.log.hello-world: {"execution.loglevel":"INFO","execution.wasRetry":"false","execution.url":"http://xxx.xxx.xxx.xxx:4440/project/hello-world/execution/follow/1213","execution.id":"fdf1cca5-e729-491b-a1fa-3d8fef373d46","execution.project":"hello-world","execution.username":"admin","execution.retryAttempt":"0","execution.user.name":"admin","execution.name":"hello-world","execution.serverUUID":null,"execution.group":null,"execution.execid":"1213","execution.serverUrl":"http://xxx.xxx.xxx.xxx:4440/","event.node":"localhost","event.stepctx":"1","event.user":"rundeck","event.step":"1","line":3,"datetime":1445503260599,"loglevel":"NORMAL","message":null,"eventType":"nodeend"}
2015-10-22 17:41:00 +0900 rundeck.log.hello-world: {"execution.loglevel":"INFO","execution.wasRetry":"false","execution.url":"http://xxx.xxx.xxx.xxx:4440/project/hello-world/execution/follow/1213","execution.id":"fdf1cca5-e729-491b-a1fa-3d8fef373d46","execution.project":"hello-world","execution.username":"admin","execution.retryAttempt":"0","execution.user.name":"admin","execution.name":"hello-world","execution.serverUUID":null,"execution.group":null,"execution.execid":"1213","execution.serverUrl":"http://xxx.xxx.xxx.xxx:4440/","event.stepctx":"1","event.step":"1","line":4,"datetime":1445503260600,"loglevel":"NORMAL","message":null,"eventType":"stepend"}
2015-10-22 17:41:00 +0900 rundeck.log.hello-world: {"execution.loglevel":"INFO","execution.wasRetry":"false","execution.url":"http://xxx.xxx.xxx.xxx:4440/project/hello-world/execution/follow/1213","execution.id":"fdf1cca5-e729-491b-a1fa-3d8fef373d46","execution.project":"hello-world","execution.username":"admin","execution.retryAttempt":"0","execution.user.name":"admin","execution.name":"hello-world","execution.serverUUID":null,"execution.group":null,"execution.execid":"1213","execution.serverUrl":"http://xxx.xxx.xxx.xxx:4440/","ending":true,"totallines":4,"message":"Execution null finished."}
```
