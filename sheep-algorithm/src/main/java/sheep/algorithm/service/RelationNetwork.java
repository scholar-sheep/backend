package sheep.algorithm.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Service;
import sheep.algorithm.pojo.Scholar;
import sheep.algorithm.config.Client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class RelationNetwork {
    public  String scholarIndex = "sheep-scholar", paperIndex = "sheep-paper";
    public  ArrayList<ArrayList> generateNetwork(String scholarId){
        RestHighLevelClient client = Client.getClient();

        ArrayList<RelationLine> relationLines = new ArrayList<>();
        ArrayList<RelationNode> relationNodes = new ArrayList<>();
        ArrayList<ArrayList> result = new ArrayList<>();
        int[] a = {2, 3};
        ArrayList<String> relationResult1 = new ArrayList<>(), relationResult2 = new ArrayList<>();
        relationResult1.add(scholarId);
        //n为网络层数
        int n = 2, i;
        for(i=0; i<n; i++){
            for(String id : relationResult1){
                ArrayList<String> singleNetwork=generateSingleNetwork(id, client, relationLines, relationNodes);
                if(singleNetwork!=null&&!singleNetwork.isEmpty())
                    relationResult2.addAll(singleNetwork);
            }
            relationResult1 = relationResult2;
            relationResult2 = new ArrayList<>();
        }

        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        result.add(relationLines);
        result.add(relationNodes);
        return result;

    }

    public  ArrayList<String> generateSingleNetwork(String scholarId, RestHighLevelClient client,
                                                          ArrayList<RelationLine> relationLines, ArrayList<RelationNode> relationNodes){
        ArrayList<String> papers = new ArrayList<>();
        ArrayList<Scholar> scholars = new ArrayList<>();
        ArrayList<String>ids = new ArrayList<>();
        String tid ;
        Scholar sourceScholar,tscholar;
        JSONObject temp;
        Map classMap = new HashMap();
        classMap.put("pubs",Scholar.Pub.class);
        classMap.put("tags",Scholar.Tag.class);
        try {
            GetResponse response = client.get((new GetRequest(scholarIndex)).id(scholarId), RequestOptions.DEFAULT);
            JSONObject jsonObject = JSONObject.fromObject(response.getSource());
            if(jsonObject.isNullObject()){
                return null;
            }
            //sourceName = jsonObject.get("name").toString();
            //
            sourceScholar=(Scholar) JSONObject.toBean(jsonObject,Scholar.class,classMap);
            JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("pubs"));
            for(int i=0; i<jsonArray.size(); i++){
                papers.add(jsonArray.getJSONObject(i).get("i").toString());
            }
            //jsonObject论文
            for(String s : papers){
                response = client.get((new GetRequest(paperIndex)).id(s), RequestOptions.DEFAULT);
                jsonObject = JSONObject.fromObject(response.getSource());
                if(jsonObject.isNullObject()){
                    continue;
                }
                jsonArray = JSONArray.fromObject(jsonObject.get("authors"));
                for(int i=0; i<jsonArray.size(); i++){
                    temp = jsonArray.getJSONObject(i);
                    if(temp.isNullObject()){
                        continue;
                    }
                    tid = temp.get("id").toString();
                    tscholar=(Scholar)JSONObject.toBean(temp,Scholar.class,classMap);
                    //tname = temp.get("name").toString();
                    if(!tid.isEmpty() && !ids.contains(tid)){
                        ids.add(tid);
                        scholars.add(tscholar);
                    }
                }
                System.out.println(jsonObject);
            }

            relationNodes.add(new RelationNode(sourceScholar));
            for(Scholar targetScholar : scholars){
                relationNodes.add(new RelationNode(targetScholar));
                relationLines.add(new RelationLine(sourceScholar, targetScholar));
            }

//            System.out.println(jsonObject);
            System.out.println(papers);
            System.out.println(ids);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ids;
    }
}

@Data
@AllArgsConstructor
class RelationLine{
    Scholar source;
    Scholar target;

}
@Data
@AllArgsConstructor
class RelationNode{
    Scholar scholar;
}