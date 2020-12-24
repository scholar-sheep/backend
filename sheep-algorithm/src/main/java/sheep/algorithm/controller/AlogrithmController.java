package sheep.algorithm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import sheep.algorithm.pojo.FieldResult;
import sheep.algorithm.pojo.NetworkResult;
import sheep.algorithm.service.HotField;
import sheep.algorithm.service.OrgRank;
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
    @Autowired
    private HotField hotField;
    @Autowired
    private OrgRank orgRank;

    @GetMapping(value = {"/algorithm/network"})
    public Object Search(@RequestParam(value = "portal_id") String portal_id) throws IOException {
        NetworkResult network = relationNetwork.getNetwork(portal_id);
       // NetworkResult network= relationNetwork.generateNetwork(portal_id);
        return ResultDTO.okOf(network);
    }
    @GetMapping(value = {"/algorithm/hotfields"})
    public Object Search() throws IOException {
        FieldResult hotfields=hotField.getHot(10,2019,2020);
        return ResultDTO.okOf(hotfields);
    }
    @GetMapping(value = {"/algorithm/orgRank"})
    public Object Rank() throws IOException {
       return ResultDTO.okOf(orgRank.getOrgRank());
    }
}