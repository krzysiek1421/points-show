package pcs.springframework.pointsshow.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pcs.springframework.pointsshow.model.TableCreationRequest;
import pcs.springframework.pointsshow.model.TableInfo;
import pcs.springframework.pointsshow.model.UpdatePointsRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class TableDataController {
    private final Logger logger = LoggerFactory.getLogger(TableDataController.class);
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
        int teamIndex = request.getTeamId() - 1; // Zakładając, że ID zespołu zaczyna się od 1
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
}