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
        return updatePoints(request, this::addPointsToTeam);
    }

    @PutMapping("/update-score")
    public ResponseEntity<String> updateScore(@RequestBody UpdatePointsRequest request) {
        return updatePoints(request, this::changeTeamScore);
    }

    @PutMapping("/reset-points")
    public ResponseEntity<String> resetPoints() {
        resetTablePoints();
        sendUpdateToClients(tableInfo);
        return ResponseEntity.ok("Points reset successfully");
    }

    @GetMapping("/table-updates")
    public SseEmitter handleTableUpdates() {
        return createSseEmitter();
    }

    @GetMapping("/get-total-visibility")
    public SseEmitter getTotalVisibility() {
        SseEmitter emitter = createSseEmitter();

        try {
            emitter.send(SseEmitter.event().data(tableInfo.isTotalVisibility()));
        } catch (IOException e) {
            emitters.remove(emitter);
        }

        return emitter;
    }

    @PutMapping("/change-total-visibility")
    public ResponseEntity<String> changeTotalVisibility() {
        tableInfo.setTotalVisibility(!tableInfo.isTotalVisibility());
        logger.info("Total visibility changed to: " + tableInfo.isTotalVisibility());

        sendTotalVisibilityUpdateToClients();

        return ResponseEntity.ok("Total visibility changed successfully");
    }

    private SseEmitter createSseEmitter() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError((e) -> emitters.remove(emitter));

        return emitter;
    }

    private void resetTablePoints() {
        for(List<Integer> list : tableInfo.getPoints()){
            Collections.fill(list, 0);
        }
    }

    public void sendTotalVisibilityUpdateToClients() {
        boolean totalVisibilityData = tableInfo.isTotalVisibility();
        synchronized(emitters) {
            for (SseEmitter emitter : new ArrayList<>(emitters)) {
                try {
                    emitter.send(SseEmitter.event().data(totalVisibilityData));
                } catch (IOException e) {
                    emitters.remove(emitter);
                }
            }
        }
    }
    public void sendUpdateToClients(Object updateData) {
        synchronized(emitters) {
            for (SseEmitter emitter : new ArrayList<>(emitters)) {
                try {
                    emitter.send(SseEmitter.event().data(updateData));
                } catch (IOException e) {
                    emitters.remove(emitter);
                }
            }
        }
    }

    private ResponseEntity<String> updatePoints(UpdatePointsRequest request, PointsUpdater pointsUpdater) {
        int teamIndex = request.getTeamId() - 1;
        int categoryIndex = request.getCategoryId() - 1;
        int points = request.getPoints();

        logger.info(request.toString());

        if (isInvalidIndex(teamIndex, categoryIndex)) {
            return ResponseEntity.badRequest().body("Invalid team ID or category");
        }

        if (tableInfo.getPoints() == null) {
            initializePointsList(tableInfo.getNumTeams(), tableInfo.getCategories().size());
        }

        pointsUpdater.updatePoints(teamIndex, categoryIndex, points);
        sendUpdateToClients(tableInfo);
        return ResponseEntity.ok("Points updated successfully");
    }


    private void initializePointsList(int numTeams, int numCategories) {
        List<List<Integer>> points = new ArrayList<>();
        for (int i = 0; i < numCategories; i++) {
            List<Integer> teamPoints = new ArrayList<>(Collections.nCopies(numTeams, 0));
            points.add(teamPoints);
        }
        tableInfo.setPoints(points);
    }

    private boolean isInvalidIndex(int teamIndex, int categoryIndex) {
        return categoryIndex < 0 || teamIndex < 0 || teamIndex >= tableInfo.getNumTeams();
    }

    private interface PointsUpdater {
        void updatePoints(int teamIndex, int categoryIndex, int points);
    }

    private void addPointsToTeam(int teamIndex, int categoryIndex, int points) {
        int currentPoints = tableInfo.getPoints().get(categoryIndex).get(teamIndex);
        tableInfo.getPoints().get(categoryIndex).set(teamIndex, currentPoints + points);
    }

    private void changeTeamScore(int teamIndex, int categoryIndex, int points) {
        tableInfo.getPoints().get(categoryIndex).set(teamIndex, points);
    }

}