package pcs.springframework.pointsshow.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pcs.springframework.pointsshow.model.TableInfo;

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
}
