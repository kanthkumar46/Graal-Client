package edu.graal.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.graal.graphs.PDGraph;
import edu.graal.graphs.types.PDGEdge;
import edu.graal.graphs.types.PDGVertex;
import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.jackson.datatype.JavaslangModule;
import org.jgrapht.DirectedGraph;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * Created by KanthKumar on 3/30/17.
 */
@Component
public class GraalAdapter {
    private final ObjectMapper mapper = new ObjectMapper();

    @PostConstruct
    private void init(){
        mapper.registerModule(new JavaslangModule());
    }

    public String convertToJson(PDGraph pdGraph) throws JsonProcessingException {
        DirectedGraph<PDGVertex, PDGEdge> directedGraph = pdGraph.getDefaultGraph();
        Tuple2<Set<PDGVertex>, Set<PDGEdge>> graph = Tuple.of(directedGraph.vertexSet(), directedGraph.edgeSet());
        return convertToJson(graph);
    }

    public String convertToJson(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }
}
