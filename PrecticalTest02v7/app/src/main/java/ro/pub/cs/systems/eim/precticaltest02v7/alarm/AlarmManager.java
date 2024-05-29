package ro.pub.cs.systems.eim.precticaltest02v7.alarm;

public class AlarmManager {
    private Map<String, Alarm> alarms = new HashMap<>();

    public void setAlarm(String clientIp, Alarm alarm) {
        alarms.put(clientIp, alarm);
    }

    public void resetAlarm(String clientIp) {
        alarms.remove(clientIp);
    }

    public Alarm getAlarm(String clientIp) {
        return alarms.get(clientIp);
    }
}