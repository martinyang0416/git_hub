package usecase.event.facade;

import usecase.event.EventManage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RecurringEventCheck implements Serializable {
    private final EventManage em;

    public RecurringEventCheck(EventManage em){
        this.em = em;
    }

    public boolean RecurringCheck(UUID event){
        List<UUID> events = em.getAll();
        int count = 0;

        for(UUID e : events){
            if(em.get(e).getTitle().equals(em.get(event).getTitle())){
                count ++;
            }
            if(count > 1){
                return true;
            }
        }

        return false;
    }

    public List<UUID> RecurringEvents(UUID event){
        List<UUID> results = new ArrayList<>();
        boolean check = this.RecurringCheck(event);
        if(check){
            for(UUID e : em.getAll()){
                if(e != event &&em.get(e).getTitle().equals(em.get(event).getTitle())){
                    results.add(e);
                }
            }
        }
        return results;
    }
}
