package com.project.tda.services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SingleThreadAnalyzerService {
    String name, id, daemon, prio, os_prio, tid, nid, sp, status, getStatus;

    ArrayList frames = new ArrayList();
    String wantNotificationOn = null;
    String wantToAcquire = null;
    ArrayList locksHeld = new ArrayList();

    Map synchronizerClasses = new HashMap();
    String threadState = null;
    // Only synchronized(){} style locks
    String classicalLockHeld = null;

    public SingleThreadAnalyzerService() {
    }

    public SingleThreadAnalyzerService(String lines) {
        parseThread(lines);
    }

    private void parseThread(String line) {
        String[] regex = {"(\"[^\"]+\")", "(#([0-9]+))", "(daemon)", "(os_prio=([0-9a-fx,]+))", "(prio=\\d{1,2})"
                , "(tid=0[xX][0-9a-fA-F]+)", "(nid=0[xX][0-9a-fA-F]+)",
                "(\\[.*?\\])"};
        String[] attrib = {"name", "id", "daemon", "os_prio", "prio", "tid", "nid", "sp"};

        for (int i = 0; i < regex.length; i++) {
            String values[] = regex_matcher(line, regex[i]);
            line = values[0];
            switch (attrib[i]) {
                case "name":
                    this.name = values[1].replace("\"", "");
                    break;
                case "id":
                    this.id = values[1];
                    break;
                case "daemon":
                    this.daemon = values[1];
                    break;
                case "prio":
                    this.prio = values[1];
                    break;
                case "os_prio":
                    this.os_prio = values[1];
                    break;
                case "tid":
                    this.tid = values[1];
                    break;
                case "nid":
                    this.nid = values[1];
                    break;
                case "sp":
                    this.sp = values[1];
                    break;
            }
        }
        this.status = line;

    }

    private String[] regex_matcher(String line, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String data = matcher.group().trim();
            line = line.replace(data, "").trim();
            String[] values = {line, data};
            return values;
        }
        return new String[]{line, "undefined"};
    }

    void parse_Stack(String line) {
        //method calls
        String frames_reg = "^\\s+at (.*)";
        String frame = stack_regex(frames_reg, line, 0);
        if (frame != "undefined") {
            frames.add(frame);
            return;
        }

        //thread state
        String thread_states_reg = "^\\s*java.lang.Thread.State: (.*)";
        String state = stack_regex(thread_states_reg, line, 1);
        if (state != "undefined") {
            this.threadState = state;
            return;
        }

        //sync_status
        String syncStatus_reg = "^\\s+- (.*?) +<([x0-9a-f]+)> \\(a (.*)\\)";
        Matcher matcher = build_matcher(syncStatus_reg, line);
        String sync_status[] = new String[3];
        //- locked <0x00000006c6c5dbd8> (a java.io.BufferedOutputStream)
        while (matcher.find()) {

            sync_status[0] = matcher.group(1); //state
            sync_status[1] = matcher.group(2); //id
            sync_status[2] = matcher.group(3); //classname
            //add only unique locks
            if (synchronizerClasses.containsKey(sync_status[1])) {
                return;
            }
            synchronizerClasses.put(sync_status[1], sync_status[2]); // id:classsName
            thread_state_(sync_status[0], sync_status[1]);
            return;
        }

        String held_lock_reg = "^\\s+- <([x0-9a-f]+)> \\(a (.*)\\)";
        matcher = build_matcher(held_lock_reg, line);
        while (matcher.find()) {

            String lockId = matcher.group(1);
            String lockClassName = matcher.group(2);
            this.synchronizerClasses.put(lockId, lockClassName);
            // Threads can take the same lock in different frames, but
            // we just want a mapping between threads and locks so we
            // must not list any lock more than once.
            if (locksHeld.contains(lockId)) {
                return;
            }
            locksHeld.add(lockId);
            return;
        }

        String LOCKED_OWNABLE_SYNCHRONIZERS_REG = "^\\s+Locked ownable synchronizers:";
        matcher = build_matcher(LOCKED_OWNABLE_SYNCHRONIZERS_REG, line);
        while (matcher.find()) {
            // Ignore these lines
            return;
        }

        String NONE_HELD_REG = "^\\s+- None";
        matcher = build_matcher(NONE_HELD_REG, line);
        while (matcher.find()) {
            // Ignore these lines
            return;
        }

//        return false;


    }


    boolean thread_state_(String state, String id) {
        switch (state) {
            case "eliminated":
                // JVM internal optimization, not sure why it's in the
                // thread dump at all
                return true;

            case "waiting on":
                wantNotificationOn = id;
                return true;

            case "parking to wait for":
                wantNotificationOn = id;
                return true;

            case "waiting to lock":
                wantToAcquire = id;
                return true;

            case "locked":
                if (wantNotificationOn == id) {
                    // Lock is released while waiting for the notification
                    return true;
                }
                // Threads can take the same lock in different frames,
                // but we just want a mapping between threads and
                // locks so we must not list any lock more than once.
                if (!locksHeld.contains(id)) {
                    locksHeld.add(id);
                }

                // lock on the 3rd line is probably the wait when waiting
                // lock is not reported explicitly
                String text = "";
                try {
                    text = (String) frames.get(this.frames.size() - 2);
                } catch (IndexOutOfBoundsException ex) {
                    System.out.println(ex.toString());
                }

                if (this.frames.size() >= 2 && this.classicalLockHeld == null && text.equals("java.lang.Object.wait")) {
                    this.classicalLockHeld = id;
                }

            default:
                return false;
        }
    }


    String stack_regex(String regex, String line, int pos) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            return matcher.group(pos);
        }
        return "undefined";
    }

    Matcher build_matcher(String reg, String line) {
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(line);
        return matcher;
    }

    void setWantNotificationOn(String lockID) {

        this.wantNotificationOn = lockID;
        int lockIndex = this.locksHeld.indexOf(lockID);
        if (lockIndex >= 0) {
            this.locksHeld.subList(lockIndex, 1);
        }
        if (this.classicalLockHeld.equals(lockID)) {
            this.classicalLockHeld = null;
        }
    }


    @Override
    public String toString() {
        return "DumpAnalyzerService{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", daemon='" + daemon + '\'' +
                ", prio='" + prio + '\'' +
                ", os_prio='" + os_prio + '\'' +
                ", tid='" + tid + '\'' +
                ", nid='" + nid + '\'' +
                ", sp='" + sp + '\'' +
                ", status='" + status + '\'' +
                ", frames=" + frames +
                ", wantNotificationOn='" + wantNotificationOn + '\'' +
                ", wantToAcquire='" + wantToAcquire + '\'' +
                ", locksHeld=" + locksHeld +
                ", synchronizerClasses=" + synchronizerClasses +
                ", threadState='" + threadState + '\'' +
                ", classicalLockHeld='" + classicalLockHeld + '\'' +
                '}';
    }
}
