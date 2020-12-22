package sheep.algorithm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import sheep.algorithm.service.RelationNetwork;
import sheep.common.utils.ResultDTO;

import javax.naming.directory.SearchResult;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;


@RestController
public class AlogrithmController {
    @Autowired
    private RelationNetwork relationNetwork;

    @GetMapping(value = {"/network"})
    public Object Search(@RequestParam(value = "portal_id") String portal_id) {
        ArrayList<ArrayList> network = relationNetwork.generateNetwork(portal_id);
        return ResultDTO.okOf(network);
    }
}