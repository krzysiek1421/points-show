package pcs.springframework.pointsshow.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pcs.springframework.pointsshow.model.TableInfo;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class TableViewController {
    private final TableInfo tableInfo;
    public TableViewController(TableInfo tableInfo) {
        this.tableInfo = tableInfo;
    }

    @GetMapping("/get-table")
    public String getTable(Model model) {
        model.addAttribute("tableInfo", tableInfo);
        return "index";
    }

    @GetMapping("/get-sample")
    public String getSample(Model model) {
        initializeSampleTableInfo();
        model.addAttribute("tableInfo", tableInfo);
        return "sampleIndex";
    }


    private void initializeSampleTableInfo() {
        int numTeams = 5;
        List<String> sampleCategories = Arrays.asList("Pop", "Metal", "Classical", "Jazz", "Rock");

        tableInfo.setNumTeams(numTeams);
        tableInfo.setCategories(sampleCategories);

        List<List<Integer>> points = IntStream.range(0, sampleCategories.size())
                .mapToObj(i -> IntStream.range(0, numTeams)
                        .mapToObj(j -> 0)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
        tableInfo.setPoints(points);
    }

}
