package com.calderagames.ld37.system;

import com.artemis.Aspect;
import com.artemis.EntitySubscription;
import com.artemis.utils.IntBag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class GroupManager extends PassiveSystem {
    private HashMap<String, IntBag> entitiesByGroup;
    private HashMap<Integer, ArrayList<String>> groupsByEntity;

    @Override
    protected void initialize() {
        entitiesByGroup = new HashMap<>();
        groupsByEntity = new HashMap<>();

        world.getAspectSubscriptionManager()
                .get(Aspect.all())
                .addSubscriptionListener(new EntitySubscription.SubscriptionListener() {
                    @Override
                    public void removed(IntBag entities) {
                        int[] ids = entities.getData();
                        for (int i = 0, s = entities.size(); s > i ; i++) {
                            removeFromAllGroups(ids[i]);
                        }
                    }

                    @Override
                    public void inserted(IntBag entities) {
                    }
                });;
    }

    public void addTo(String groupName, int entityId) {
        IntBag entities = entitiesByGroup.get(groupName);

        if(entities == null) {
            entities = new IntBag();
            entitiesByGroup.put(groupName, entities);
        }

        if(!entities.contains(entityId))
            entities.add(entityId);

        ArrayList<String> groups = groupsByEntity.get(entityId);

        if(groups == null) {
            groups = new ArrayList<>();
            groupsByEntity.put(entityId, groups);
        }

        if(!groups.contains(groupName))
            groups.add(groupName);
    }

    public void removeFrom(String groupName, int entityId) {
        IntBag entities = entitiesByGroup.get(groupName);

        if(entities != null) {
            entities.remove(entityId);
        }

        ArrayList<String> groups = groupsByEntity.get(entityId);

        if(groups != null) {
            groups.remove(groupName);
            if (groups.size() == 0) groupsByEntity.remove(entityId);
        }
    }

    public void removeFromAllGroups(int entityId) {
        ArrayList<String> groups = groupsByEntity.get(entityId);

        if(groups == null)
            return;

        for(int i = 0; i < groups.size(); i++) {
            IntBag entities = entitiesByGroup.get(groups.get(i));
            if(entities != null && entityId < entities.size()) {
                entities.remove(entityId);
            }
        }
        groupsByEntity.remove(entityId);
    }

    public List<String> getGroups(int entityId) {
        List<String> groups = groupsByEntity.get(entityId);

        return groups == null? Collections.emptyList(): Collections.unmodifiableList(groups);
    }

    public IntBag getEntities(String groupName) {
        IntBag entities = entitiesByGroup.get(groupName);
        if(entities == null) {
            entities = new IntBag();
            entitiesByGroup.put(groupName, entities);
        }
        return entities;
    }
}
