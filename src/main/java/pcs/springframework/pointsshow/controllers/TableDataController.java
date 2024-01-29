package pcs.springframework.pointsshow.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import pcs.springframework.pointsshow.model.TableCreationRequest;
import pcs.springframework.pointsshow.model.TableInfo;
import pcs.springframework.pointsshow.model.UpdatePointsRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
public class TableDataController {
    private final Logger logger = LoggerFactory.getLogger(TableDataController.class);
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final TableInfo tableInfo;

    private TableDataController(TableInfo tableInfo){
        this.tableInfo = tableInfo;
    }

    @PostMapping("/create-points-table")
    public ResponseEntity<String> createTable(@RequestBody TableCreationRequest request) {
        logger.info("Received request: " + request);
        tableInfo.setNumTeams(request.getNumberOfTeams());
        tableInfo.setCategories(request.getCategories());
        return ResponseEntity.ok("Table created successfully");
    }



    @GetMapping("/get-table-info")
    public TableInfo getTable() {
        return tableInfo;
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateTableData(@RequestBody UpdatePointsRequest request) {
        int teamIndex = request.getTeamId() - 1;
        int categoryIndex = tableInfo.getCategories().indexOf(request.getCategory());

        if (categoryIndex < 0 || teamIndex < 0 || teamIndex >= tableInfo.getNumTeams()) {
            return ResponseEntity.badRequest().body("Invalid team ID or category");
        }

        if (tableInfo.getPoints() == null) {
            initializePointsList(tableInfo.getNumTeams(), tableInfo.getCategories().size());
        }
        Integer currentPoints = tableInfo.getPoints().get(categoryIndex).get(teamIndex);
        currentPoints += request.getPoints();
        tableInfo.getPoints().get(categoryIndex).set(teamIndex, currentPoints);
        sendUpdateToClients(tableInfo);
        return ResponseEntity.ok("Points updated successfully");
    }

    @GetMapping("/reset-points")
    public ResponseEntity<String> resetPoints() {
        resetTablePoints();
        sendUpdateToClients(tableInfo);
        return ResponseEntity.ok("Points reset successfully");
    }

    private SseEmitter createSseEmitter() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError((e) -> emitters.remove(emitter));

        return emitter;
    }


    @GetMapping("/table-updates")
    public SseEmitter handleTableUpdates() {
        return createSseEmitter();
    }

    private void resetTablePoints() {
        for(List<Integer> list : tableInfo.getPoints()){
            list.replaceAll(i -> 0);
        }
    }

    private void sendResetToClients() {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("reset").data("{\"reset\":true}"));
            } catch (IOException e) {
                emitters.remove(emitter);
            }
        }
    }
    public void sendUpdateToClients(Object updateData) {
        synchronized(emitters) {
            for (SseEmitter emitter : new ArrayList<>(emitters)) { // Użyj kopii listy tutaj również
                try {
                    emitter.send(SseEmitter.event().data(updateData));
                } catch (IOException e) {
                    emitters.remove(emitter);
                }
            }
        }
    }

    private void initializePointsList(int numTeams, int numCategories) {
        List<List<Integer>> points = new ArrayList<>();
        for (int i = 0; i < numCategories; i++) {
            List<Integer> teamPoints = new ArrayList<>(Collections.nCopies(numTeams, 0));
            points.add(teamPoints);
        }
        tableInfo.setPoints(points);
    }
}