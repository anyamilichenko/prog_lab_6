package server.utilities;

import common.data.Dragon;
import common.utilities.CollectionManager;

import java.time.LocalDate;
import java.util.LinkedList;

public class CollectionManagerImpl implements CollectionManager {

    private final LocalDate creationDate = LocalDate.now();
    private LinkedList<Dragon> mainData = new LinkedList<>();

    public void initialiseData(LinkedList<Dragon> linkedList) {
        this.mainData = linkedList;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public LinkedList<Dragon> getMainData() {
        return mainData;
    }



    public Long getMaxId() {
        Long maxId = 0L;
        for (Dragon dragon : mainData) {
            if (dragon.getID() > maxId) {
                maxId = dragon.getID();
            }
        }
        return (maxId);
    }
}
