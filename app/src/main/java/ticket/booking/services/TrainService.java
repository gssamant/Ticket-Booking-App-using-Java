package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Train;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TrainService {

    private List<Train> trainList;
    private ObjectMapper objectMapper = new ObjectMapper();
    private static final String TRAINS_PATH = "trains.json";

    public TrainService() throws IOException {
        File trains = new File(TRAINS_PATH);
        trainList = objectMapper.readValue(trains, new TypeReference<List<Train>>() {});
    }

    public List<Train> searchTrains(String source, String destination) {
        return trainList.stream().filter(train -> validTrain(train, source, destination)).collect(Collectors.toList());
    }

    public void addTrain(Train newtrain) {
        Optional<Train> existingTrain = trainList.stream().filter(train -> train.getTrainId().equalsIgnoreCase(newtrain.getTrainId())).findFirst();

        if(existingTrain.isPresent()) {
            updateTrain(newtrain);
        }
        else {
            trainList.add(newtrain);
            saveTrainToList();
        }
    }

    public void updateTrain(Train updatedtrain) {
        OptionalInt index = IntStream.range(0, trainList.size()).filter(i -> trainList.get(i).getTrainId().equalsIgnoreCase(updatedtrain.getTrainId())).findFirst();
        if(index.isPresent()) {
            trainList.set(index.getAsInt(), updatedtrain);
            saveTrainToList();
        }
        else {
            addTrain(updatedtrain);
        }
    }

    private void saveTrainToList() {
        try {
            objectMapper.writeValue(new File(TRAINS_PATH), trainList);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Boolean validTrain(Train train, String source, String destination) {
        List<String> stationOrder = train.getStations();
        int sourceIndex = stationOrder.indexOf(source.toLowerCase());
        int destinationIndex = stationOrder.indexOf(destination.toLowerCase());
        return sourceIndex != -1 && destinationIndex != -1 && sourceIndex < destinationIndex;
    }

}
