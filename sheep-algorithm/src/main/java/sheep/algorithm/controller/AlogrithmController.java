package sheep.algorithm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import sheep.algorithm.pojo.NetworkResult;
import sheep.algorithm.service.RelationNetwork;
import sheep.common.utils.ResultDTO;

import javax.naming.directory.SearchResult;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;


@RestController
public class AlogrithmController {
    @Autowired
    private RelationNetwork relationNetwork;

    @GetMapping(value = {"/network"})
    public Object Search(@RequestParam(value = "portal_id") String portal_id) throws IOException {
        NetworkResult network = relationNetwork.getNetwork(portal_id);
       // NetworkResult network= relationNetwork.generateNetwork(portal_id);
        return ResultDTO.okOf(network);
    }
}