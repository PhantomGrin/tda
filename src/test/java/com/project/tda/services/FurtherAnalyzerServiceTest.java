package com.project.tda.services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class FurtherAnalyzerServiceTest {
    private String result_dump = "[{\"name\":\"Attach Listener\",\"id\":\"#15\",\"daemon\":\"daemon\",\"prio\":\"prio\\u003d9\",\"os_prio\":\"os_prio\\u003d0\",\"tid\":\"tid\\u003d0x00007f0424001000\",\"nid\":\"nid\\u003d0x357d\",\"sp\":\"[0x0000000000000000]\",\"status\":\"waiting on condition\",\"getStatus\":\"non-Java thread\",\"frames\":[],\"locksHeld\":[],\"synchronizerClasses\":{},\"threadState\":\"RUNNABLE\"},{\"name\":\"DestroyJavaVM\",\"id\":\"#14\",\"daemon\":\"undefined\",\"prio\":\"prio\\u003d5\",\"os_prio\":\"os_prio\\u003d0\",\"tid\":\"tid\\u003d0x00007f046000d000\",\"nid\":\"nid\\u003d0x355c\",\"sp\":\"[0x0000000000000000]\",\"status\":\"waiting on condition\",\"getStatus\":\"non-Java thread\",\"frames\":[],\"locksHeld\":[],\"synchronizerClasses\":{},\"threadState\":\"RUNNABLE\"},{\"name\":\"Thread-3\",\"id\":\"#13\",\"daemon\":\"undefined\",\"prio\":\"prio\\u003d5\",\"os_prio\":\"os_prio\\u003d0\",\"tid\":\"tid\\u003d0x00007f04602f8000\",\"nid\":\"nid\\u003d0x3579\",\"sp\":\"[0x00007f04495dd000]\",\"status\":\"runnable\",\"getStatus\":\"running\",\"frames\":[\"java.io.FileOutputStream.writeBytes(Native Method)\",\"java.io.FileOutputStream.write(FileOutputStream.java:326)\",\"java.io.BufferedOutputStream.flushBuffer(BufferedOutputStream.java:82)\",\"java.io.BufferedOutputStream.flush(BufferedOutputStream.java:140)\",\"java.io.PrintStream.write(PrintStream.java:482)\",\"sun.nio.cs.StreamEncoder.writeBytes(StreamEncoder.java:221)\",\"sun.nio.cs.StreamEncoder.implFlushBuffer(StreamEncoder.java:291)\",\"sun.nio.cs.StreamEncoder.flushBuffer(StreamEncoder.java:104)\",\"java.io.OutputStreamWriter.flushBuffer(OutputStreamWriter.java:185)\",\"java.io.PrintStream.write(PrintStream.java:527)\",\"java.io.PrintStream.print(PrintStream.java:597)\",\"java.io.PrintStream.println(PrintStream.java:736)\",\"raceCondition_ctricalSections.Counter.add(Counter.java:14)\",\"TestThreads.lambda$main$3(TestThreads.java:25)\",\"TestThreads$$Lambda$4/2065951873.run(Unknown Source)\",\"java.lang.Thread.run(Thread.java:748)\"],\"locksHeld\":[\"0x00000006c6c5dbd8\",\"0x00000006c6c02898\",\"0x00000006c6c5dca8\"],\"synchronizerClasses\":{\"0x00000006c6c5dbd8\":\"java.io.BufferedOutputStream\",\"0x00000006c6c02898\":\"java.io.PrintStream\",\"0x00000006c6c5dca8\":\"java.io.OutputStreamWriter\"},\"threadState\":\"RUNNABLE\"},{\"name\":\"Thread-2\",\"id\":\"#12\",\"daemon\":\"undefined\",\"prio\":\"prio\\u003d5\",\"os_prio\":\"os_prio\\u003d0\",\"tid\":\"tid\\u003d0x00007f04602f6000\",\"nid\":\"nid\\u003d0x3578\",\"sp\":\"[0x00007f04496de000]\",\"status\":\"waiting for monitor entry\",\"getStatus\":\"waiting to acquire\",\"frames\":[\"java.io.PrintStream.println(PrintStream.java:735)\",\"raceCondition_ctricalSections.Counter.add(Counter.java:14)\",\"TestThreads.lambda$main$2(TestThreads.java:22)\",\"TestThreads$$Lambda$3/363771819.run(Unknown Source)\",\"java.lang.Thread.run(Thread.java:748)\"],\"wantToAcquire\":\"0x00000006c6c02898\",\"locksHeld\":[],\"synchronizerClasses\":{\"0x00000006c6c02898\":\"java.io.PrintStream\"},\"threadState\":\"BLOCKED (on object monitor)\"},{\"name\":\"Thread-1\",\"id\":\"#11\",\"daemon\":\"undefined\",\"prio\":\"prio\\u003d5\",\"os_prio\":\"os_prio\\u003d0\",\"tid\":\"tid\\u003d0x00007f04602f4000\",\"nid\":\"nid\\u003d0x3577\",\"sp\":\"[0x00007f04497df000]\",\"status\":\"waiting for monitor entry\",\"getStatus\":\"waiting to acquire\",\"frames\":[\"java.io.PrintStream.println(PrintStream.java:735)\",\"raceCondition_ctricalSections.Counter.add(Counter.java:14)\",\"TestThreads.lambda$main$1(TestThreads.java:19)\",\"TestThreads$$Lambda$2/1329552164.run(Unknown Source)\",\"java.lang.Thread.run(Thread.java:748)\"],\"wantToAcquire\":\"0x00000006c6c02898\",\"locksHeld\":[],\"synchronizerClasses\":{\"0x00000006c6c02898\":\"java.io.PrintStream\"},\"threadState\":\"BLOCKED (on object monitor)\"},{\"name\":\"Thread-0\",\"id\":\"#10\",\"daemon\":\"undefined\",\"prio\":\"prio\\u003d5\",\"os_prio\":\"os_prio\\u003d0\",\"tid\":\"tid\\u003d0x00007f04602f2800\",\"nid\":\"nid\\u003d0x3576\",\"sp\":\"[0x00007f04498e0000]\",\"status\":\"waiting for monitor entry\",\"getStatus\":\"waiting to acquire\",\"frames\":[\"java.io.PrintStream.println(PrintStream.java:735)\",\"raceCondition_ctricalSections.Counter.add(Counter.java:14)\",\"TestThreads.lambda$main$0(TestThreads.java:16)\",\"TestThreads$$Lambda$1/2129789493.run(Unknown Source)\",\"java.lang.Thread.run(Thread.java:748)\"],\"wantToAcquire\":\"0x00000006c6c02898\",\"locksHeld\":[],\"synchronizerClasses\":{\"0x00000006c6c02898\":\"java.io.PrintStream\"},\"threadState\":\"BLOCKED (on object monitor)\"},{\"name\":\"Monitor Ctrl-Break\",\"id\":\"#5\",\"daemon\":\"daemon\",\"prio\":\"prio\\u003d5\",\"os_prio\":\"os_prio\\u003d0\",\"tid\":\"tid\\u003d0x00007f046024d800\",\"nid\":\"nid\\u003d0x3570\",\"sp\":\"[0x00007f044a0fe000]\",\"status\":\"runnable\",\"getStatus\":\"running\",\"frames\":[\"java.net.SocketInputStream.socketRead0(Native Method)\",\"java.net.SocketInputStream.socketRead(SocketInputStream.java:116)\",\"java.net.SocketInputStream.read(SocketInputStream.java:171)\",\"java.net.SocketInputStream.read(SocketInputStream.java:141)\",\"sun.nio.cs.StreamDecoder.readBytes(StreamDecoder.java:284)\",\"sun.nio.cs.StreamDecoder.implRead(StreamDecoder.java:326)\",\"sun.nio.cs.StreamDecoder.read(StreamDecoder.java:178)\",\"java.io.InputStreamReader.read(InputStreamReader.java:184)\",\"java.io.BufferedReader.fill(BufferedReader.java:161)\",\"java.io.BufferedReader.readLine(BufferedReader.java:324)\",\"java.io.BufferedReader.readLine(BufferedReader.java:389)\",\"com.intellij.rt.execution.application.AppMainV2$1.run(AppMainV2.java:64)\"],\"locksHeld\":[\"0x00000006c6c038e0\"],\"synchronizerClasses\":{\"0x00000006c6c038e0\":\"java.io.InputStreamReader\"},\"threadState\":\"RUNNABLE\"},{\"name\":\"Finalizer\",\"id\":\"#3\",\"daemon\":\"daemon\",\"prio\":\"prio\\u003d8\",\"os_prio\":\"os_prio\\u003d0\",\"tid\":\"tid\\u003d0x00007f046014e000\",\"nid\":\"nid\\u003d0x3568\",\"sp\":\"[0x00007f044acd0000]\",\"status\":\"in Object.wait()\",\"getStatus\":\"awaiting notification\",\"frames\":[\"java.lang.Object.wait(Native Method)\",\"java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:144)\",\"java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:165)\",\"java.lang.ref.Finalizer$FinalizerThread.run(Finalizer.java:216)\"],\"wantNotificationOn\":\"0x00000006c6c144b0\",\"locksHeld\":[],\"synchronizerClasses\":{\"0x00000006c6c144b0\":\"java.lang.ref.ReferenceQueue$Lock\"},\"threadState\":\"WAITING (on object monitor)\"},{\"name\":\"Reference Handler\",\"id\":\"#2\",\"daemon\":\"daemon\",\"prio\":\"prio\\u003d10\",\"os_prio\":\"os_prio\\u003d0\",\"tid\":\"tid\\u003d0x00007f046014b800\",\"nid\":\"nid\\u003d0x3567\",\"sp\":\"[0x00007f044add1000]\",\"status\":\"in Object.wait()\",\"getStatus\":\"awaiting notification\",\"frames\":[\"java.lang.Object.wait(Native Method)\",\"java.lang.Object.wait(Object.java:502)\",\"java.lang.ref.Reference.tryHandlePending(Reference.java:191)\",\"java.lang.ref.Reference$ReferenceHandler.run(Reference.java:153)\"],\"wantNotificationOn\":\"0x00000006c6c146e0\",\"locksHeld\":[],\"synchronizerClasses\":{\"0x00000006c6c146e0\":\"java.lang.ref.Reference$Lock\"},\"threadState\":\"WAITING (on object monitor)\"},{\"name\":\"VM Thread\",\"id\":\"ud1\",\"daemon\":\"undefined\",\"prio\":\"undefined\",\"os_prio\":\"os_prio\\u003d0\",\"tid\":\"tid\\u003d0x00007f0460141800\",\"nid\":\"nid\\u003d0x3564\",\"sp\":\"undefined\",\"status\":\"runnable\",\"getStatus\":\"non-Java thread\",\"frames\":[],\"locksHeld\":[],\"synchronizerClasses\":{}},{\"name\":\"GC task thread#0 (ParallelGC)\",\"id\":\"ud2\",\"daemon\":\"undefined\",\"prio\":\"undefined\",\"os_prio\":\"os_prio\\u003d0\",\"tid\":\"tid\\u003d0x00007f0460022800\",\"nid\":\"nid\\u003d0x355d\",\"sp\":\"undefined\",\"status\":\"runnable\",\"getStatus\":\"non-Java thread\",\"frames\":[],\"locksHeld\":[],\"synchronizerClasses\":{}},{\"name\":\"VM Periodic Task Thread\",\"id\":\"ud3\",\"daemon\":\"undefined\",\"prio\":\"undefined\",\"os_prio\":\"os_prio\\u003d0\",\"tid\":\"tid\\u003d0x00007f046025a800\",\"nid\":\"nid\\u003d0x3575\",\"sp\":\"undefined\",\"status\":\"waiting on condition\",\"getStatus\":\"non-Java thread\",\"frames\":[],\"locksHeld\":[],\"synchronizerClasses\":{}}]\n";
    private FurtherAnalyzerService furtherAnalyzerService = new FurtherAnalyzerService();

    private Map<String,List<String>> threadStatus = new HashMap<>();
    private Map<String,List<String>> deamons = new HashMap<>();
    private Map<String,List<String>> stackLength = new HashMap<>();
    private Map<String,List<String>> identicleStacks = new HashMap<>();
    private Map<String,JsonArray> stackMap = new HashMap<>();

    @Test
    public void stateviseSummary() {
        threadStatus.put("running", Arrays.asList( "#13", "#5"));
        threadStatus.put("waiting to acquire", Arrays.asList( "#12", "#11", "#10"));
        threadStatus.put("non-Java thread", Arrays.asList( "#15", "#14", "ud1", "ud2", "ud3"));
        threadStatus.put("awaiting notification", Arrays.asList( "#3", "#2"));

        assertTrue(threadStatus.equals(furtherAnalyzerService.stateviseSummary(result_dump)));
    }

    @Test
    public void deamonSummary() {
        deamons.put("daemon", Arrays.asList( "#15", "#5", "#3", "#2"));
        deamons.put("undefined", Arrays.asList( "#14", "#13", "#12", "#11", "#10", "ud1", "ud2", "ud3"));

        assertTrue(deamons.equals(furtherAnalyzerService.deamonSummary(result_dump)));
    }

    @Test
    public void stackLengthSummary() {
        stackLength.put("slength_less10", Arrays.asList( "#15", "#14", "#12", "#11", "#10", "#3", "#2", "ud1", "ud2", "ud3"));
        stackLength.put("slength_to100", Arrays.asList( "#13", "#5"));

        assertTrue(stackLength.equals(furtherAnalyzerService.stackLengthSummary(result_dump)));
    }

    @Test
    public void identicalStackTraceSummary() {
        identicleStacks.put("ST2", Arrays.asList( "#13"));
        identicleStacks.put("ST4", Arrays.asList( "#5"));
        identicleStacks.put("ST3", Arrays.asList( "#12", "#11", "#10"));
        identicleStacks.put("ST6", Arrays.asList( "#2"));
        identicleStacks.put("ST5", Arrays.asList( "#3"));
        identicleStacks.put("EMPTY", Arrays.asList( "#15", "#14", "ud1", "ud2", "ud3"));

        assertTrue(identicleStacks.equals(furtherAnalyzerService.identicalStackTraceSummary(result_dump)));
    }

    @Test
    public void getStackTraceMap() {
        List frames = new ArrayList();
        JsonArray array = null;

        frames = Arrays.asList("java.io.FileOutputStream.writeBytes(Native Method)","java.io.FileOutputStream.write(FileOutputStream.java:326)","java.io.BufferedOutputStream.flushBuffer(BufferedOutputStream.java:82)","java.io.BufferedOutputStream.flush(BufferedOutputStream.java:140)","java.io.PrintStream.write(PrintStream.java:482)","sun.nio.cs.StreamEncoder.writeBytes(StreamEncoder.java:221)","sun.nio.cs.StreamEncoder.implFlushBuffer(StreamEncoder.java:291)","sun.nio.cs.StreamEncoder.flushBuffer(StreamEncoder.java:104)","java.io.OutputStreamWriter.flushBuffer(OutputStreamWriter.java:185)","java.io.PrintStream.write(PrintStream.java:527)","java.io.PrintStream.print(PrintStream.java:597)","java.io.PrintStream.println(PrintStream.java:736)","raceCondition_ctricalSections.Counter.add(Counter.java:14)","TestThreads.lambda$main$3(TestThreads.java:25)","TestThreads$$Lambda$4/2065951873.run(Unknown Source)","java.lang.Thread.run(Thread.java:748)");
        array = new JsonParser().parse(new Gson().toJson(frames)).getAsJsonArray();
        stackMap.put("ST2", array);

        frames = Arrays.asList("java.net.SocketInputStream.socketRead0(Native Method)","java.net.SocketInputStream.socketRead(SocketInputStream.java:116)","java.net.SocketInputStream.read(SocketInputStream.java:171)","java.net.SocketInputStream.read(SocketInputStream.java:141)","sun.nio.cs.StreamDecoder.readBytes(StreamDecoder.java:284)","sun.nio.cs.StreamDecoder.implRead(StreamDecoder.java:326)","sun.nio.cs.StreamDecoder.read(StreamDecoder.java:178)","java.io.InputStreamReader.read(InputStreamReader.java:184)","java.io.BufferedReader.fill(BufferedReader.java:161)","java.io.BufferedReader.readLine(BufferedReader.java:324)","java.io.BufferedReader.readLine(BufferedReader.java:389)","com.intellij.rt.execution.application.AppMainV2$1.run(AppMainV2.java:64)");
        array = new JsonParser().parse(new Gson().toJson(frames)).getAsJsonArray();
        stackMap.put("ST4", array);

        frames = Arrays.asList("java.io.PrintStream.println(PrintStream.java:735)","raceCondition_ctricalSections.Counter.add(Counter.java:14)", "TestThreads.lambda$main$2(TestThreads.java:22)","TestThreads$$Lambda$3/363771819.run(Unknown Source)","java.lang.Thread.run(Thread.java:748)");
        array = new JsonParser().parse(new Gson().toJson(frames)).getAsJsonArray();
        stackMap.put("ST3", array);

        frames = Arrays.asList("java.lang.Object.wait(Native Method)","java.lang.Object.wait(Object.java:502)", "java.lang.ref.Reference.tryHandlePending(Reference.java:191)","java.lang.ref.Reference$ReferenceHandler.run(Reference.java:153)");
        array = new JsonParser().parse(new Gson().toJson(frames)).getAsJsonArray();
        stackMap.put("ST6", array);

        frames = Arrays.asList("java.lang.Object.wait(Native Method)","java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:144)","java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:165)","java.lang.ref.Finalizer$FinalizerThread.run(Finalizer.java:216)");
        array = new JsonParser().parse(new Gson().toJson(frames)).getAsJsonArray();
        stackMap.put("ST5", array);

        frames = new ArrayList();
        array = new JsonParser().parse(new Gson().toJson(frames)).getAsJsonArray();
        stackMap.put("EMPTY", array);

        furtherAnalyzerService.identicalStackTraceSummary(result_dump);
        assertTrue(stackMap.equals(furtherAnalyzerService.getStackTraceMap()));
    }
}