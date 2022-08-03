## Configuration
The `reactor.netty.ioWorkerCount` set to 2!!

```kotlin
fun main(args: Array<String>) {
    System.setProperty("reactor.netty.ioWorkerCount", "2");
    runApplication<DemoApplication>(*args)
}
```

## Calls:
Call the following example URLs immediately without delay in a row. Let there be a moment where URLs all run at the same time.

### It works fine
Example run and result. `reactor-http-nio-2` has been recycled and not overridden.
- http://localhost:8080/work/20?data=20
```json
{
    "data": "20",
    "threadName-in-context": "kotlinx.coroutines.DefaultExecutor",
    "threadName": "reactor-http-nio-2"
}
```
- http://localhost:8080/work/10?data=10 
```json
{
    "data": "10",
    "threadName-in-context": "kotlinx.coroutines.DefaultExecutor",
    "threadName": "reactor-http-nio-1"
}
```
- http://localhost:8080/work/1?data=1 
```json
{
    "data": "1",
    "threadName-in-context": "kotlinx.coroutines.DefaultExecutor",
    "threadName": "reactor-http-nio-2"
}
```

### It runs with unexpectedly
- http://localhost:8080/notwork/20?data=20
```json
{
    "data": "10",
    "reactor.onDiscard.local": "java.util.function.Consumer$$Lambda$803/0x0000000800649040@65606def",
    "threadName-in-context": "kotlinx.coroutines.DefaultExecutor"
}
```
- http://localhost:8080/notwork/10?data=10
```json
{
    "data": "1",
    "reactor.onDiscard.local": "java.util.function.Consumer$$Lambda$803/0x0000000800649040@54f92a0",
    "threadName-in-context": "kotlinx.coroutines.DefaultExecutor"
}
```
- http://localhost:8080/notwork/1?data=1
```json
{
    "threadName-in-context": "kotlinx.coroutines.DefaultExecutor"
}
```