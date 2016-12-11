package com.calderagames.ld37.system;

import com.artemis.BaseSystem;
import com.calderagames.ld37.system.event.SystemEvent;
import com.calderagames.ld37.system.event.SystemEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class EventSystem extends BaseSystem {


    private HashMap<Class<? extends SystemEvent>, ArrayList<SystemEventListener>> subscriptionMap;
    private ArrayList<SystemEvent> events;

    public EventSystem() {
        subscriptionMap = new HashMap<Class<? extends SystemEvent>, ArrayList<SystemEventListener>>();
        events = new ArrayList<SystemEvent>();
    }

    public void subscribe(Class<? extends SystemEvent> type, SystemEventListener listener) {
        ArrayList<SystemEventListener> listeners = subscriptionMap.get(type);

        if(listeners == null) {
            listeners = new ArrayList<>();
            subscriptionMap.put(type, listeners);
        }

        listeners.add(listener);
    }

    public void unSubscribe(Class<? extends SystemEvent> type, SystemEventListener listener) {
        ArrayList<SystemEventListener> listeners = subscriptionMap.get(type);

        if(listeners == null)
            return;

        listeners.remove(listener);

        if(listeners.isEmpty())
            subscriptionMap.remove(type);
    }

    public void dispatch(SystemEvent event) {
        events.add(event);
    }

    @Override
    protected void processSystem() {
        ArrayList<SystemEventListener> listeners = null;
        Class<? extends SystemEvent> prevType = null;
        Class<? extends SystemEvent> type = null;
        for(SystemEvent event: events) {
            type = event.getClass();

            if(prevType != type)
                listeners = subscriptionMap.get(event.getClass());

            prevType = type;

            if(listeners != null) {
                for(SystemEventListener l: listeners)
                    l.receive(event);
            }
        }

        events.clear();
    }

}
