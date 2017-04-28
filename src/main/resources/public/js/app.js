$(function() {
    init();

    $('.pdg-form').submit(function() {
        var textareaId = $(this).find("textarea").attr('id');
        sessionStorage.setItem("textareaId", textareaId);
        storePrograms();
    });

    $('.align-pdg-form').submit(function(event) {
        var program1 = $("#program1").val();
        var program2 = $("#program2").val();

        if(program1 && program2){
            $("#originalProgram").val(program1);
            $("#suspectProgram").val(program2);
        } else {
            event.preventDefault();
            alert("Two programs required to align");
        }
        storePrograms();
    });

    if(graphJson) {
        var graph = JSON.parse(graphJson);
        var containerId = sessionStorage.getItem("textareaId") + "-graph-area";
        createNetwork(graph, containerId);
    }

    if(originalGraphJson && suspectGraphJson) {
        var graph1 = JSON.parse(originalGraphJson);
        createNetwork(graph1, "program1-graph-area");

        var graph2 = JSON.parse(suspectGraphJson);
        createNetwork(graph2, "program2-graph-area");
    }

    if(alignmentJson){
        var alignment = JSON.parse(alignmentJson);
        var temp = alignment.map(function(obj) {
            return obj[0]["label"]+"  <------>  "+obj[1]["label"];
        });

        var list = $("#alignments>ul")
        $.each(temp, function(i, data) {
            $('<li/>').addClass('list-group-item').text(data).appendTo(list);
        });
    }

    if(edgeCorrectness){
        $("#edgeCorrectness").text(edgeCorrectness+"%");
    }
});

var createVisDataSetNodes = function(vertexSet) {
    var visDataSetNodes = vertexSet.map(function(obj) {
        var temp = {};
        temp["id"] = obj["vertexId"];
        temp["label"] = obj["label"];
        return temp;
    });

    return visDataSetNodes;
}

var createVisDataSetEdges = function(edgeSet) {
    var visDataSetEdges = edgeSet.map(function(obj) {
        var temp = {};
        temp["from"] = obj["sourceVertexId"];
        temp["to"] = obj["targetVertexId"];
        temp["label"] = obj["type"];
        return temp;
    });

    return visDataSetEdges;
}

var createNetwork = function(graph, containerId) {
    var vertexSet = graph[0];
    var edgeSet = graph[1];

    var visDataSetNodes = createVisDataSetNodes(vertexSet);
    var visDataSetEdges = createVisDataSetEdges(edgeSet);

    var nodes = new vis.DataSet(visDataSetNodes);
    var edges = new vis.DataSet(visDataSetEdges);

    var data = {
        nodes: nodes,
        edges: edges
    };

    var container = document.getElementById(containerId);
    var network = new vis.Network(container, data, options);
}

var options = {
    clickToUse: true,
    nodes:{
        color: {
              border: '#FF4500',
              background: '#FF7F50',
              highlight: {
                border: '#FF4500',
                background: '#FF6347'
              },
              hover: {
                border: '#FF4500',
                background: '#FF6347'
              }
        }
    },
    edges: {
        color: {
            color:'#A9A9A9',
            highlight:'#696969'
        },
        font: {
             size: 10
        },
        width: 1.5
    },
    interaction:{
        hover: true
    }
}

var storePrograms = function() {
    var program1 = $("#program1").val();
    var program2 = $("#program2").val();
    sessionStorage.setItem("program1", program1);
    sessionStorage.setItem("program2", program2);
}

var init = function() {
    new Behave({
        textarea: document.getElementById('program1')
    });

    new Behave({
        textarea: document.getElementById('program2')
    });

    if(sessionStorage.getItem("program1")) {
        $("#program1").val(sessionStorage.getItem("program1"));
    }

    if(sessionStorage.getItem("program2")) {
        $("#program2").val(sessionStorage.getItem("program2"));
    }
}